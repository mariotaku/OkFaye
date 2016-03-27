package org.mariotaku.okfaye.internal;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public abstract class IdentifiedRequest extends Request {
    @JsonField(name = "clientId")
    String clientId;

    public IdentifiedRequest() {

    }

    @Override
    public String toString() {
        return "IdentifiedRequest{" +
                "clientId='" + clientId + '\'' +
                "} " + super.toString();
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
