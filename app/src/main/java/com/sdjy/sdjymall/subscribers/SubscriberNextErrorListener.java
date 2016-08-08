package com.sdjy.sdjymall.subscribers;

/**
 * Created by jimmy on 16/4/15.
 */
public interface SubscriberNextErrorListener<T> {
    void onNext(T t);

    void onError();
}