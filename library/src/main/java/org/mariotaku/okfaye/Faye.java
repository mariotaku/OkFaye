package org.mariotaku.okfaye;

import okhttp3.OkHttpClient;
import okhttp3.ws.WebSocketCall;
import org.mariotaku.okfaye.response.BaseResponse;
import org.mariotaku.okfaye.response.ConnectionResponse;
import org.mariotaku.okfaye.response.HandshakeResponse;

/**
 * Created by mariotaku on 16/3/27.
 */
public abstract class Faye {

    public static final int UNCONNECTED = 1;
    public static final int CONNECTING = 2;
    public static final int CONNECTED = 3;
    public static final int DISCONNECTED = 4;

    public abstract void setExtension(Extension extension);

    public abstract void handshake(Callback<HandshakeResponse> callback);

    public abstract void connect(Callback<ConnectionResponse> callback);

    public abstract void disconnect();

    public abstract void subscribe(String subscription, MessageCallback callback);

    public abstract void unsubscribe(String subscription);

    public abstract void publish(String channel, String data, Callback<BaseResponse> callback);

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

}
