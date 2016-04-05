package org.mariotaku.okfaye;

import com.bluelinelabs.logansquare.LoganSquare;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;
import org.mariotaku.okfaye.internal.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by mariotaku on 16/3/27.
 */
public final class FayeImpl extends Faye {
    final WebSocketCall call;
    final long pingInterval;

    InternalWebSocketListener socketListener;
    int state;
    Advice advice;
    String clientId;
    Extension extension;
    boolean connectRequest;
    long messageId;
    ErrorListener errorListener;

    FayeImpl(WebSocketCall call, long pingInterval) {
        this.call = call;
        this.pingInterval = pingInterval;
        state = UNCONNECTED;
        advice = new Advice();
    }

    @Override
    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    @Override
    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    protected void handshake(final Callback<HandshakeResponse> callback) {
        if (advice != null && Advice.NONE.equals(advice.reconnect)) return;
        if (state != UNCONNECTED) return;
        state = CONNECTING;
        final HandshakeRequest message = HandshakeRequest.create();
        sendMessage(message, HandshakeResponse.class, new Callback<HandshakeResponse>() {
            @Override
            public void callback(HandshakeResponse response) {
                if (response.isSuccessful()) {
                    state = CONNECTED;
                    clientId = response.getClientId();
                    callback.callback(response);
                } else {
                    state = UNCONNECTED;
                    // TODO retry
                }
            }
        });
    }

    @Override
    protected void connect(final Callback<ConnectionResponse> callback) {
        if (advice != null && Advice.NONE.equals(advice.reconnect)) return;
        if (state == DISCONNECTED) return;

        if (socketListener == null) {
            socketListener = new InternalWebSocketListener(this) {
                @Override
                public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                    super.onOpen(webSocket, response);
                    connect(callback);
                }
            };
            call.enqueue(socketListener);
            return;
        }

        if (state == UNCONNECTED) {
            handshake(new Callback<HandshakeResponse>() {
                @Override
                public void callback(HandshakeResponse response) {
                    connect(callback);
                }
            });
            return;
        }
        if (callback != null) {
            callback.callback(null);
        }
        if (state != CONNECTED) return;

        if (connectRequest) return;
        connectRequest = true;

        ConnectionRequest message = ConnectionRequest.create(clientId);
        sendMessage(message, Response.class, new Callback<Response>() {
            @Override
            public void callback(Response response) {
                cycleConnection();
            }
        });
    }

    @Override
    public void disconnect() {
        if (state != CONNECTED) return;
        state = DISCONNECTED;

        DisconnectRequest message = DisconnectRequest.create(clientId);
        sendMessage(message, Response.class, new Callback<Response>() {
            @Override
            public void callback(Response response) {
                // TODO do something for errors
                socketListener.disconnect();
            }
        });
        socketListener.clearSubscription();
    }

    @Override
    public void subscribe(final String subscription, final Callback<String> callback) {
        connect(new Callback<ConnectionResponse>() {
            @Override
            public void callback(ConnectionResponse response) {
                final SubscriptionRequest request = SubscriptionRequest.create(clientId, Channel.SUBSCRIBE, subscription);
                sendMessage(request, SubscriptionResponse.class, new Callback<SubscriptionResponse>() {
                    @Override
                    public void callback(SubscriptionResponse response) {
                        if (!response.isSuccessful()) {
                            socketListener.removeSubscription(subscription);
                            return;
                        }
                        // TODO what will happen if subscribed?
                        socketListener.addSubscription(subscription, callback);
                    }
                });
            }
        });
    }

    @Override
    public void unsubscribe(final String subscription) {
        if (socketListener.removeSubscription(subscription) == null) return;
        connect(new Callback<ConnectionResponse>() {
            @Override
            public void callback(ConnectionResponse response) {
                final SubscriptionRequest request = SubscriptionRequest.create(clientId, Channel.UNSUBSCRIBE, subscription);
                sendMessage(request, SubscriptionResponse.class, new Callback<SubscriptionResponse>() {
                    @Override
                    public void callback(SubscriptionResponse response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        // TODO what will happen if unsubscribed?
                    }
                });
            }
        });
    }

    @Override
    public void publish(final String channel, final String data, final Callback<Response> callback) {
        connect(new Callback<ConnectionResponse>() {
            @Override
            public void callback(ConnectionResponse response) {
                PublishRequest message = PublishRequest.create(channel, data);
                sendMessage(message, Response.class, callback);
            }
        });
    }

    void cycleConnection() {
        if (this.connectRequest) {
            this.connectRequest = false;
        }
        long interval;
        if (advice != null) {
            interval = advice.interval;
        } else {
            interval = Defaults.CONNECTION_TIMEOUT_MILLIS;
        }
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            // Ignore
        }
        connect(null);
    }

    <Req extends Request, Resp extends Response> boolean sendMessage(Req message, Class<Resp> respCls,
                                                                     Callback<Resp> callback) {
        message.setExtension(extension);
        message.setId(nextId());
        return socketListener.sendMessage(message, respCls, callback);
    }

    void handleAdvice(Advice advice) {
        this.advice = advice;
        if (Advice.HANDSHAKE.equals(advice.reconnect) && state != DISCONNECTED) {
            state = UNCONNECTED;
            clientId = null;
            cycleConnection();
        }
    }

    String nextId() {
        return Long.toString(++messageId, 36);
    }

    void unconnected(IOException e, int code, String reason) {
        if (state == DISCONNECTED) return;
        state = UNCONNECTED;
        if (errorListener != null) {
            errorListener.error(e, code, reason);
        }
    }


    /**
     * Created by mariotaku on 16/3/27.
     */
    static class InternalWebSocketListener implements WebSocketListener {
        final FayeImpl faye;
        final Map<String, SendResultHandler<?>> resultHandlers = new HashMap<>();
        final Map<String, Callback<String>> messageCallbacks = new HashMap<>();

        final Timer timer = new Timer(true);
        WebSocket webSocket;

        TimerTask pingTask;

        public InternalWebSocketListener(FayeImpl faye) {
            this.faye = faye;
        }

        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            this.webSocket = webSocket;
            reschedulePing();
        }


        <Req extends Request, Resp extends Response> boolean sendMessage(Req message, Class<Resp> respCls,
                                                                         Callback<Resp> callback) {
            if (webSocket == null) return false;
            try {
                String messageJson = LoganSquare.serialize(message);
                final String id = message.getId();
                resultHandlers.put(id, new SendResultHandler<>(id, respCls, callback));
                webSocket.sendMessage(RequestBody.create(WebSocket.TEXT, messageJson));
            } catch (IOException e) {
                faye.unconnected(e, -1, null);
                disconnect();
                return false;
            }
            return true;
        }

        @Override
        public void onFailure(IOException e, okhttp3.Response response) {
            if (response != null) {
                faye.unconnected(e, response.code(), response.message());
            } else {
                faye.unconnected(e, -1, null);
            }
        }

        @Override
        public void onMessage(ResponseBody message) throws IOException {
            final String json = message.string();
            List<Response> responseList = LoganSquare.parseList(json, Response.class);
            if (responseList != null) {
                for (Response response : responseList) {
                    Advice advice = response.getAdvice();
                    if (advice != null) {
                        faye.handleAdvice(advice);
                    }
                    final String channel = response.getChannel(), id = response.getId();
                    Callback<String> callback = messageCallbacks.get(channel);
                    if (callback != null) {
                        callback.callback(response.getData());
                    }
                    SendResultHandler<?> handler = resultHandlers.remove(id);
                    if (handler != null) {
                        handler.call(json);
                    }
                }
            }
            reschedulePing();
        }

        @Override
        public void onPong(Buffer payload) {
            reschedulePing();
        }

        @Override
        public void onClose(int code, String reason) {
            webSocket = null;
            faye.unconnected(null, code, reason);
            cancelPing();
        }

        public boolean isClosed() {
            return webSocket == null;
        }

        public void addSubscription(String subscription, Callback<String> callback) {
            messageCallbacks.put(subscription, callback);
        }

        public Callback<String> removeSubscription(String subscription) {
            return messageCallbacks.remove(subscription);
        }

        void reschedulePing() {
            cancelPing();
            pingTask = new TimerTask() {

                @Override
                public void run() {
                    if (isClosed()) return;
                    try {
                        final Buffer buffer = new Buffer();
                        buffer.writeUtf8("[]");
                        webSocket.sendPing(buffer);
                    } catch (IOException e) {
                        // Ignore
                        faye.unconnected(e, -1, null);
                        disconnect();
                    }
                }
            };
            timer.schedule(pingTask, faye.pingInterval);
        }


        private void cancelPing() {
            if (pingTask == null) return;
            pingTask.cancel();
            pingTask = null;
        }

        void clearSubscription() {
            messageCallbacks.clear();
        }

        void disconnect() {
            try {
                webSocket.close(1000, "Closed");
            } catch (IOException e) {
                // Ignore
            }
        }

        static class SendResultHandler<T extends Response> {
            private final String id;
            private final Class<T> cls;

            private final Callback<T> callback;

            SendResultHandler(String id, Class<T> cls, Callback<T> callback) {
                this.id = id;
                this.cls = cls;
                this.callback = callback;
            }

            void call(String json) {
                if (callback == null || json == null) return;
                try {
                    for (T item : LoganSquare.parseList(json, cls)) {
                        if (id != null && !id.equals(item.getId())) continue;
                        callback.callback(item);
                    }
                } catch (IOException e) {
                    // TODO handle this
                }
            }
        }

    }
}
