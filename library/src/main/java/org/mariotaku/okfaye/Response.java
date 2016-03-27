package org.mariotaku.okfaye;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public class Response {
    @JsonField(name = "channel")
    String channel;
    @JsonField(name = "successful")
    boolean successful;
    @JsonField(name = "id")
    String id;
    @JsonField(name = "advice")
    Faye.Advice advice;
    @JsonField(name = "error")
    String error;

    public String getChannel() {
        return channel;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getId() {
        return id;
    }

    public Faye.Advice getAdvice() {
        return advice;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "Response{" +
                "channel='" + channel + '\'' +
                ", successful=" + successful +
                ", id='" + id + '\'' +
                ", advice=" + advice +
                ", error='" + error + '\'' +
                '}';
    }
}
