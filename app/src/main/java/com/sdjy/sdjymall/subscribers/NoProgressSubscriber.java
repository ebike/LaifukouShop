package com.sdjy.sdjymall.subscribers;

import android.content.Context;
import android.widget.Toast;

import com.sdjy.sdjymall.progress.ProgressCancelListener;
import com.sdjy.sdjymall.util.StringUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public class NoProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    //只处理成功的观察者
    private SubscriberOnNextListener mSubscriberOnNextListener;
    //处理成功和错误的观察者
    private SubscriberNextErrorListener nextErrorListener;

    private Context context;

    public NoProgressSubscriber(Context context) {
        this.context = context;
    }

    public NoProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
    }

    public NoProgressSubscriber(SubscriberNextErrorListener nextErrorListener, Context context) {
        this.nextErrorListener = nextErrorListener;
        this.context = context;
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (nextErrorListener != null) {
            nextErrorListener.onError();
        }

        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (!StringUtils.strIsEmpty(e.getMessage()) && e.getMessage().equals("no_prompt")) {
            //不需要提示信息
        } else if (e instanceof RuntimeException) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }

        if (nextErrorListener != null) {
            nextErrorListener.onNext(t);
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}