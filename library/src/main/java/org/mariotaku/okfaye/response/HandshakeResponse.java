package org.mariotaku.okfaye.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Arrays;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public class HandshakeResponse extends BaseResponse {

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
