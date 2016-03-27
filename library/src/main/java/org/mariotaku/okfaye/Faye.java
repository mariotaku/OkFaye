package org.mariotaku.okfaye;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.ws.WebSocketCall;

import java.util.Arrays;

/**
 * Created by mariotaku on 16/3/27.
 */
public abstract class Faye {

    public static final int UNCONNECTED = 1;
    public static final int CONNECTING = 2;
    public static final int CONNECTED = 3;
    public static final int DISCONNECTED = 4;

    public abstract void handshake(Callback<HandshakeResponse> callback);

    public abstract void connect(Callback<ConnectionResponse> callback);

    public abstract void disconnect();

    public abstract void subscribe(String subscription, Callback<String> callback);

    public abstract void unsubscribe(String subscription);

    public abstract void publish(String channel, String data, Callback<Response> callback);

    public abstract void setExtension(Extension extension);

    public abstract int getState();

    @Override
    protected void finalize() throws Throwable {
        disconnect();
        super.finalize();
    }

    /**
     * Create a Faye client from WebSocket call
     */
    public static Faye create(OkHttpClient client, WebSocketCall call) {
        return new FayeImpl(call, client.readTimeoutMillis() / 2);
    }

    /**
     * Created by mariotaku on 16/3/27.
     */
    @JsonObject
    public abstract static class Extension {

    }

    /**
     * Created by mariotaku on 16/3/27.
     */
    public static class Defaults {

        public static final long CONNECTION_TIMEOUT_MILLIS = 60 * 1000;
        public static final int RETRY_INTERVAL = 0;

        public static final String BAYEUX_VERSION = "1.0";
    }

    /**
     * Created by mariotaku on 16/3/27.
     */
    public static class Channel {
        public static final String HANDSHAKE = "/meta/handshake";
        public static final String CONNECT = "/meta/connect";
        public static final String SUBSCRIBE = "/meta/subscribe";
        public static final String UNSUBSCRIBE = "/meta/unsubscribe";
        public static final String DISCONNECT = "/meta/disconnect";
    }

    /**
     * Created by mariotaku on 16/3/27.
     */
    public interface Callback<T> {
        void callback(T response);
    }

    /**
     * Created by mariotaku on 16/3/27.
     */
    @JsonObject
    public static class Advice {
        public static final String HANDSHAKE = "handshake";
        public static final String RETRY = "retry";
        public static final String NONE = "none";

        @JsonField(name = "reconnect")
        String reconnect = RETRY;
        @JsonField(name = "interval")
        int interval = Defaults.RETRY_INTERVAL;
        @JsonField(name = "timeout")
        long timeout = Defaults.CONNECTION_TIMEOUT_MILLIS;

        public String getReconnect() {
            return reconnect;
        }

        public int getInterval() {
            return interval;
        }

        public long getTimeout() {
            return timeout;
        }

        @Override
        public String toString() {
            return "Advice{" +
                    "reconnect='" + reconnect + '\'' +
                    ", interval=" + interval +
                    ", timeout=" + timeout +
                    '}';
        }
    }

    /**
     * Created by mariotaku on 16/3/27.
     */
    public static class ConnectionResponse extends Response {
        @Override
        public String toString() {
            return "ConnectionResponse{} " + super.toString();
        }
    }

    /**
     * Created by mariotaku on 16/3/27.
     */
    @JsonObject
    public static class HandshakeResponse extends Response {

        @JsonField(name = "clientId")
        String clientId;

        @JsonField(name = "supportedConnectionTypes")
        String[] supportedConnectionTypes;

        public String getClientId() {
            return clientId;
        }

        public String[] getSupportedConnectionTypes() {
            return supportedConnectionTypes;
        }

        @Override
        public String toString() {
            return "HandshakeResponse{" +
                    "clientId='" + clientId + '\'' +
                    ", supportedConnectionTypes=" + Arrays.toString(supportedConnectionTypes) +
                    "} " + super.toString();
        }
    }

    /**
     * Created by mariotaku on 16/3/27.
     */
    @JsonObject
    public static class SubscriptionResponse extends Response {
        @Override
        public String toString() {
            return "SubscriptionResponse{} " + super.toString();
        }
    }
}
