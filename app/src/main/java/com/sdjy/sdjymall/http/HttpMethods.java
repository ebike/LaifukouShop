package com.sdjy.sdjymall.http;


import com.sdjy.sdjymall.constants.FinalValues;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.model.AddressModel;
import com.sdjy.sdjymall.model.BankInfoModel;
import com.sdjy.sdjymall.model.CarShopModel;
import com.sdjy.sdjymall.model.CommonDataModel;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.GoodsBrowsingModel;
import com.sdjy.sdjymall.model.GoodsEvaluateCountModel;
import com.sdjy.sdjymall.model.GoodsEvaluateModel;
import com.sdjy.sdjymall.model.GoodsInfoModel;
import com.sdjy.sdjymall.model.GoodsModel;
import com.sdjy.sdjymall.model.HistorySearchModel;
import com.sdjy.sdjymall.model.HomePageDataModel;
import com.sdjy.sdjymall.model.HomeScrollImageModel;
import com.sdjy.sdjymall.model.HotSearchWordModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.model.ShopModel;
import com.sdjy.sdjymall.model.TeamGoodsModel;
import com.sdjy.sdjymall.model.UserCashBalanceModel;
import com.sdjy.sdjymall.model.UserModel;
import com.sdjy.sdjymall.model.ValidateCodeModel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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
            if ("1".equals(httpResult.code)) {
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
    public void findGoods(Subscriber<GoodsInfoModel> subscriber, String id, Map<String, String> params) {
        Observable observable = apiService.findGoods(id, params)
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
    public void addToCart(Subscriber subscriber, String userId, String goodsId, String priceId, int num) {
        Observable observable = apiService.addToCart(StaticValues.userModel.userToken, userId, goodsId, priceId, num)
                .map(new HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    //更新购物车接口
    public void updateCart(Subscriber subscriber, String userId, String goodsId, String priceId, int num) {
        Observable observable = apiService.updateCart(StaticValues.userModel.userToken, userId, goodsId, priceId, num)
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
    public void syncShoppingCart(Subscriber subscriber, String goodsIds, String priceIds, String nums) {
        Observable observable = apiService.syncShoppingCart(StaticValues.userModel.userToken, StaticValues.userModel.userId, goodsIds, priceIds, nums)
                .map(new HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    //获取充值返现数值接口
    public void findRechargeNums(Subscriber<List<Integer>> subscriber) {
        Observable observable = apiService.findRechargeNums()
                .map(new HttpResultFunc<List<Integer>>());
        toSubscribe(observable, subscriber);
    }

    //充值接口
    public void recharge(Subscriber subscriber, Integer num) {
        Observable observable = apiService.recharge(StaticValues.userModel.userToken, StaticValues.userModel.userId, num)
                .map(new HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    //查询创业套餐列表接口
    public void teamGoods(Subscriber<CommonListModel<List<TeamGoodsModel>>> subscriber, int page, Map<String, String> params) {
        Observable observable = apiService.teamGoods(page, params)
                .map(new HttpResultFunc<CommonListModel<List<TeamGoodsModel>>>());
        toSubscribe(observable, subscriber);
    }

    //删除购物车接口
    public void delShoppingCart(Subscriber subscriber, String goodsIds, String priceIds) {
        Observable observable = apiService.delShoppingCart(StaticValues.userModel.userToken, StaticValues.userModel.userId, goodsIds, priceIds)
                .map(new HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    //退出账号接口
    public void logout(Subscriber subscriber) {
        Observable observable = apiService.logout(StaticValues.userModel.userId)
                .map(new HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    //发送找回密码短信验证码接口
    public void sendUpdatePassCode(Subscriber<ValidateCodeModel> subscriber, String phone) {
        Observable observable = apiService.sendUpdatePassCode(phone)
                .map(new HttpResultFunc<ValidateCodeModel>());
        toSubscribe(observable, subscriber);
    }

    //发送注册短信验证码接口
    public void sendRegCode(Subscriber<ValidateCodeModel> subscriber, String phone) {
        Observable observable = apiService.sendRegCode(phone)
                .map(new HttpResultFunc<ValidateCodeModel>());
        toSubscribe(observable, subscriber);
    }

    //根据手机号验证用户是否存在接口
    public void checkUserByPhone(Subscriber<String> subscriber, String phone) {
        Observable observable = apiService.checkUserByPhone(phone)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    //重置密码接口
    public void resetPassword(Subscriber subscriber, String userId, String password) {
        Observable observable = apiService.resetPassword(userId, password)
                .map(new HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    //查看创业套餐详细信息接口
    public void findTeamGoods(Subscriber<TeamGoodsModel> subscriber, String id) {
        Observable observable = apiService.findTeamGoods(id)
                .map(new HttpResultFunc<TeamGoodsModel>());
        toSubscribe(observable, subscriber);
    }

    //修改用户头像接口
    public void updateHeadPic(Subscriber<UserModel> subscriber, File file) {
        Map<String, RequestBody> map = new HashMap<String, RequestBody>();
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        map.put("headPic\"; filename=\"" + file.getName(), fileBody);

        Observable observable = apiService.updateHeadPic(StaticValues.userModel.userToken, StaticValues.userModel.userId, map)
                .map(new HttpResultFunc<UserModel>());
        toSubscribe(observable, subscriber);
    }

    //修改用户资料接口
    public void updateUserData(Subscriber<UserModel> subscriber, Map<String, String> params) {
        Observable observable = apiService.updateUserData(StaticValues.userModel.userToken, StaticValues.userModel.userId, params)
                .map(new HttpResultFunc<UserModel>());
        toSubscribe(observable, subscriber);
    }

    //用户注册接口
    public void regUser(Subscriber<UserModel> subscriber, String phone, String password) {
        Observable observable = apiService.regUser(phone, password, StaticValues.imei, "android:" + android.os.Build.VERSION.RELEASE)
                .map(new HttpResultFunc<UserModel>());
        toSubscribe(observable, subscriber);
    }

    //获取问题反馈类型接口
    public void getFeedbackType(Subscriber<List<String>> subscriber) {
        Observable observable = apiService.getFeedbackType()
                .map(new HttpResultFunc<List<String>>());
        toSubscribe(observable, subscriber);
    }

    //问题反馈接口
    public void saveFeedback(Subscriber subscriber, String feedType, String content, String contact) {
        Observable observable = apiService.saveFeedback(feedType, content, contact)
                .map(new HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    //获取浏览记录接口
    public void userBrowse(Subscriber<List<GoodsBrowsingModel>> subscriber) {
        Observable observable = apiService.userBrowse(StaticValues.userModel.userToken, StaticValues.userModel.userId)
                .map(new HttpResultFunc<List<GoodsBrowsingModel>>());
        toSubscribe(observable, subscriber);
    }

    //获取搜索热词接口
    public void hotSearchWord(Subscriber<List<HotSearchWordModel>> subscriber) {
        Observable observable = apiService.hotSearchWord()
                .map(new HttpResultFunc<List<HotSearchWordModel>>());
        toSubscribe(observable, subscriber);
    }

    //获取搜索历史记录接口
    public void searchHis(Subscriber<List<HistorySearchModel>> subscriber) {
        Observable observable = apiService.searchHis(StaticValues.userModel.userToken, StaticValues.userModel.userId)
                .map(new HttpResultFunc<List<HistorySearchModel>>());
        toSubscribe(observable, subscriber);
    }

    //清空用户历史记录接口
    public void clearSearchHis(Subscriber subscriber) {
        Observable observable = apiService.clearSearchHis(StaticValues.userModel.userToken, StaticValues.userModel.userId)
                .map(new HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    //删除指定搜索历史接口
    public void delSearchWord(Subscriber subscriber, String id) {
        Observable observable = apiService.delSearchWord(StaticValues.userModel.userToken, StaticValues.userModel.userId, id)
                .map(new HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    //获取关注商品记录接口
    public void collectGoods(Subscriber<CommonListModel<List<GoodsBrowsingModel>>> subscriber) {
        Observable observable = apiService.collectGoods(StaticValues.userModel.userToken, StaticValues.userModel.userId)
                .map(new HttpResultFunc<CommonListModel<List<GoodsBrowsingModel>>>());
        toSubscribe(observable, subscriber);
    }

    //获取关注商家记录接口
    public void collectShops(Subscriber<CommonListModel<List<ShopModel>>> subscriber) {
        Observable observable = apiService.collectShops(StaticValues.userModel.userToken, StaticValues.userModel.userId)
                .map(new HttpResultFunc<CommonListModel<List<ShopModel>>>());
        toSubscribe(observable, subscriber);
    }

    //批量删除浏览记录接口
    public void delUserBrowse(Subscriber subscriber, String ids) {
        Observable observable = apiService.delUserBrowse(StaticValues.userModel.userToken, StaticValues.userModel.userId, ids)
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    //查询用户所在的所有团队接口
    public void findUserTeams(Subscriber<CommonListModel<List<String>>> subscriber, int page) {
        Observable observable = apiService.findUserTeams(StaticValues.userModel.userToken, StaticValues.userModel.userId, page)
                .map(new HttpResultFunc<CommonListModel<List<String>>>());
        toSubscribe(observable, subscriber);
    }

    //查看用户推荐的所有人接口
    public void findRefereeUser(Subscriber<CommonListModel<List<String>>> subscriber, int page) {
        Observable observable = apiService.findRefereeUser(StaticValues.userModel.userToken, StaticValues.userModel.userId, page)
                .map(new HttpResultFunc<CommonListModel<List<String>>>());
        toSubscribe(observable, subscriber);
    }

    //获取收货地址接口
    public void addressList(Subscriber<List<AddressModel>> subscriber) {
        Observable observable = apiService.addressList(StaticValues.userModel.userToken, StaticValues.userModel.userId)
                .map(new HttpResultFunc<List<AddressModel>>());
        toSubscribe(observable, subscriber);
    }

    //添加、更新、设为默认的接口
    public void saveOrUpdateAddress(Subscriber subscriber, Map<String, String> params) {
        Observable observable = apiService.saveOrUpdateAddress(StaticValues.userModel.userToken, StaticValues.userModel.userId, params)
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    //删除收货地址接口
    public void delAddress(Subscriber subscriber, String id) {
        Observable observable = apiService.delAddress(StaticValues.userModel.userToken, StaticValues.userModel.userId, id)
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    //获取银行号卡信息
    public void findBankinfo(Subscriber<BankInfoModel> subscriber) {
        Observable observable = apiService.findBankinfo(StaticValues.userModel.userToken, StaticValues.userModel.userId)
                .map(new HttpResultFunc<BankInfoModel>());
        toSubscribe(observable, subscriber);
    }
}
