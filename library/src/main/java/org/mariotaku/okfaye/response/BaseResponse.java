package org.mariotaku.okfaye.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.mariotaku.okfaye.Advice;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public class BaseResponse {
    @JsonField(name = "channel")
    String channel;
    @JsonField(name = "successful")
    boolean successful;
    @JsonField(name = "id")
    String id;
    @JsonField(name = "advice")
    Advice advice;

    public String getChannel() {
        return channel;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getId() {
        return id;
    }

    public Advice getAdvice() {
        return advice;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "channel='" + channel + '\'' +
                ", successful=" + successful +
                ", id='" + id + '\'' +
                ", advice=" + advice +
                '}';
    }
}
