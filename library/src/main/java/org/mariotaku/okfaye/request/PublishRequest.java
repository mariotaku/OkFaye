package org.mariotaku.okfaye.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public class PublishRequest extends IdentifiedRequest {
    @JsonField(name = "data")
    String data;

    public PublishRequest() {

    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PublishRequest{" +
                "data='" + data + '\'' +
                "} " + super.toString();
    }

    public static PublishRequest create(String channel, String data) {
        PublishRequest request = new PublishRequest();
        request.setChannel(channel);
        request.setData(data);
        return request;
    }

}
