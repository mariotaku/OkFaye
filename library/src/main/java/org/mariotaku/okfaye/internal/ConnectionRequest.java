package org.mariotaku.okfaye.internal;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.mariotaku.okfaye.Faye;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public class ConnectionRequest extends IdentifiedRequest {
    @JsonField(name = "connectionType")
    String connectionType;

    public ConnectionRequest() {

    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public String toString() {
        return "ConnectionRequest{" +
                "connectionType='" + connectionType + '\'' +
                "} " + super.toString();
    }

    public static ConnectionRequest create(String clientId) {
        ConnectionRequest request = new ConnectionRequest();
        request.setChannel(Faye.Channel.CONNECT);
        request.setClientId(clientId);
        request.setConnectionType("websocket");
        return request;
    }
}
