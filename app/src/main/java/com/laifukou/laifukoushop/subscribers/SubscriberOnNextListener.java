package com.laifukou.laifukoushop.subscribers;

/**
 * Created by jimmy on 16/4/15.
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}