package org.mariotaku.okfaye.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public class SubscriptionResponse extends BaseResponse {
    @Override
    public String toString() {
        return "SubscriptionResponse{} " + super.toString();
    }
}
