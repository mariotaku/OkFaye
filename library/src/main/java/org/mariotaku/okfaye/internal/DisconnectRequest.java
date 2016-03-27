package org.mariotaku.okfaye.internal;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.mariotaku.okfaye.Faye;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public class DisconnectRequest extends IdentifiedRequest {

    public static DisconnectRequest create(String clientId) {
        final DisconnectRequest request = new DisconnectRequest();
        request.setChannel(Faye.Channel.DISCONNECT);
        request.setClientId(clientId);
        return request;
    }

}
