package com.sdjy.sdjymall.http;


import com.sdjy.sdjymall.constants.FinalValues;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.model.CarShopModel;
import com.sdjy.sdjymall.model.CommonDataModel;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.GoodsEvaluateCountModel;
import com.sdjy.sdjymall.model.GoodsEvaluateModel;
import com.sdjy.sdjymall.model.GoodsInfoModel;
import com.sdjy.sdjymall.model.GoodsModel;
import com.sdjy.sdjymall.model.HomePageDataModel;
import com.sdjy.sdjymall.model.HomeScrollImageModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.model.ShopModel;
import com.sdjy.sdjymall.model.UserCashBalanceModel;
import com.sdjy.sdjymall.model.UserModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
        //手动创建一个OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置超时时间
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //设置打印日志
        if (FinalValues.IS_DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

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

    /**
     * 用来统一处理Http的resultCode
     * 只将错误的情况处理掉，成功的直接返回原对象
     */
    public class HttpResultFunc2<T> implements Func1<HttpResult<T>, HttpResult<T>> {

        @Override
        public HttpResult<T> call(HttpResult<T> httpResult) {
            if ("0".equals(httpResult.code)) {
                return httpResult;
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

    //搜索店铺
    public void searchShop(Subscriber<CommonListModel<List<ShopModel>>> subscriber, String shopName, int page) {
        Observable observable = apiService.searchShop(shopName, page)
                .map(new HttpResultFunc<CommonListModel<List<ShopModel>>>());

        toSubscribe(observable, subscriber);
    }

    //搜索商品
    public void searchGoods(Subscriber<CommonListModel<List<GoodsModel>>> subscriber, Map<String, String> params, int page) {
        Observable observable = apiService.searchGoods(params, page)
                .map(new HttpResultFunc<CommonListModel<List<GoodsModel>>>());

        toSubscribe(observable, subscriber);
    }

    //获取商品详细信息接口
    public void findGoods(Subscriber<GoodsInfoModel> subscriber, String id) {
        Observable observable = apiService.findGoods(id)
                .map(new HttpResultFunc<GoodsInfoModel>());
        toSubscribe(observable, subscriber);
    }

    //获取商家详细信息接口
    public void findShop(Subscriber<ShopModel> subscriber, Map<String, String> params) {
        Observable observable = apiService.findShop(params)
                .map(new HttpResultFunc<ShopModel>());
        toSubscribe(observable, subscriber);
    }

    //关注商品或店铺接口
    public void userCollect(Subscriber<String> subscriber, String userId, int collectType, String oid) {
        Observable observable = apiService.userCollect(StaticValues.userModel.userToken, userId, collectType, oid)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    //取消关注商品或店铺接口
    public void cancelCollect(Subscriber subscriber, String userId, String id) {
        Observable observable = apiService.cancelCollect(StaticValues.userModel.userToken, userId, id)
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    //登录
    public void login(Subscriber<UserModel> subscriber, String userName, String password) {
        Observable observable = apiService.login(userName, password, StaticValues.imei, "android:" + android.os.Build.VERSION.RELEASE)
                .map(new HttpResultFunc<UserModel>());
        toSubscribe(observable, subscriber);
    }

    //获取商品评论个数接口
    public void findGoodsCommentsNum(Subscriber<GoodsEvaluateCountModel> subscriber, String id) {
        Observable observable = apiService.findGoodsCommentsNum(id)
                .map(new HttpResultFunc<GoodsEvaluateCountModel>());
        toSubscribe(observable, subscriber);
    }

    //获取商品评论接口
    public void findGoodsComments(Subscriber<CommonListModel<List<GoodsEvaluateModel>>> subscriber, Map<String, String> params) {
        Observable observable = apiService.findGoodsComments(params)
                .map(new HttpResultFunc<CommonListModel<List<GoodsEvaluateModel>>>());
        toSubscribe(observable, subscriber);
    }

    //获取用户资料接口：用户登录后进入我的首页调用此接口获取数据
    public void userCashBalance(Subscriber<UserCashBalanceModel> subscriber, String userId) {
        Observable observable = apiService.userCashBalance(StaticValues.userModel.userToken, userId)
                .map(new HttpResultFunc<UserCashBalanceModel>());
        toSubscribe(observable, subscriber);
    }

    //加入购物车接口
    public void addToCart(Subscriber subscriber, String userId, String goodsId, String priceId) {
        Observable observable = apiService.addToCart(StaticValues.userModel.userToken, userId, goodsId, priceId)
                .map(new HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    //获取购物车信息接口
    public void cartGoods(Subscriber<List<CarShopModel>> subscriber) {
        Observable observable = apiService.cartGoods(StaticValues.userModel.userToken, StaticValues.userModel.userId)
                .map(new HttpResultFunc<List<CarShopModel>>());
        toSubscribe(observable, subscriber);
    }

    //同步购物车接口
    public void syncShoppingCart(Subscriber subscriber, Map<String, String> params) {
        Observable observable = apiService.syncShoppingCart(StaticValues.userModel.userToken, StaticValues.userModel.userId, params)
                .map(new HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    //获取充值返现数值接口
    public void findRechargeNums(Subscriber<List<Integer>> subscriber) {
        Observable observable = apiService.findRechargeNums()
                .map(new HttpResultFunc<List<Integer>>());
        toSubscribe(observable, subscriber);
    }
}
