package org.mariotaku.okfaye;

/**
 * Created by mariotaku on 16/3/27.
 */
public interface Callback<T> {
    void callback(T response);
}
