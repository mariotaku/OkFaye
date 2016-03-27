package org.mariotaku.okfaye.internal;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public class SubscriptionRequest extends IdentifiedRequest {
    @JsonField(name = "subscription")
    String subscription;

    public SubscriptionRequest() {

    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    @Override
    public String toString() {
        return "SubscriptionRequest{" +
                "subscription='" + subscription + '\'' +
                "} " + super.toString();
    }

    public static SubscriptionRequest create(String clientId, String channel, String subscription) {
        final SubscriptionRequest request = new SubscriptionRequest();
        request.setChannel(channel);
        request.setClientId(clientId);
        request.setSubscription(subscription);
        return request;
    }
}
