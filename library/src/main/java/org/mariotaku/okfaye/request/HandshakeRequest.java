package org.mariotaku.okfaye.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.mariotaku.okfaye.Channel;
import org.mariotaku.okfaye.Defaults;

import java.util.Arrays;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public class HandshakeRequest extends BaseRequest {
    @JsonField(name = "version")
    String version;
    @JsonField(name = "supportedConnectionTypes")
    String[] supportedConnectionTypes;

    public HandshakeRequest() {
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setSupportedConnectionTypes(String[] supportedConnectionTypes) {
        this.supportedConnectionTypes = supportedConnectionTypes;
    }

    @Override
    public String toString() {
        return "HandshakeRequest{" +
                "version='" + version + '\'' +
                ", supportedConnectionTypes=" + Arrays.toString(supportedConnectionTypes) +
                "} " + super.toString();
    }

    public static HandshakeRequest create() {
        HandshakeRequest request = new HandshakeRequest();
        request.setChannel(Channel.HANDSHAKE);
        request.setVersion(Defaults.BAYEUX_VERSION);
        final String[] supportedConnectionTypes = {"websocket", "eventsource", "long-polling",
                "cross-origin-long-polling", "callback-polling"};
        request.setSupportedConnectionTypes(supportedConnectionTypes);
        return request;
    }
}
