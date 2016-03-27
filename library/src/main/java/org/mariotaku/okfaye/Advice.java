package org.mariotaku.okfaye;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public class Advice {
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
