package com.laifukou.laifukoushop.http;


import com.laifukou.laifukoushop.model.CommonDataModel;
import com.laifukou.laifukoushop.model.HomePageDataModel;
import com.laifukou.laifukoushop.model.HomeScrollImageModel;
import com.laifukou.laifukoushop.model.HttpResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HttpMethods {

    public static final String BASE_URL = "http://www.gnets.cn:8088/jymall_api/app/";

    private static final int DEFAULT_TIMEOUT = 30;

    private Retrofit retrofit;
    private APIService apiService;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        apiService = retrofit.create(APIService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //订阅
    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.code.equals("1")) {
                return httpResult.data;
            } else {
                throw new RuntimeException(httpResult.message);
            }
        }
    }

    //获取首页滚动图片
    public void getHomeScrollImages(Subscriber<List<HomeScrollImageModel>> subscriber) {
        Observable observable = apiService.getHomeScrollImages()
                .map(new HttpResultFunc<List<HomeScrollImageModel>>());

        toSubscribe(observable, subscriber);
    }

    //获取首页数据
    public void getHomePageDatas(Subscriber<HomePageDataModel> subscriber) {
        Observable observable = apiService.getHomePageDatas()
                .map(new HttpResultFunc<HomePageDataModel>());

        toSubscribe(observable, subscriber);
    }

    //获取商品一级分类（底部菜单分类中的商品分类）
    public void getFirstSorts(Subscriber<List<CommonDataModel>> subscriber) {
        Observable observable = apiService.getFirstSorts()
                .map(new HttpResultFunc<List<CommonDataModel>>());

        toSubscribe(observable, subscriber);
    }

    //根据商品一级分类，获取分类下信息
    public void getSecondSorts(Subscriber<List<CommonDataModel>> subscriber, String sortId) {
        Observable observable = apiService.getSecondSorts(sortId)
                .map(new HttpResultFunc<List<CommonDataModel>>());

        toSubscribe(observable, subscriber);
    }

}
