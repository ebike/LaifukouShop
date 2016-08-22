package com.sdjy.sdjymall.http;

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
import com.sdjy.sdjymall.model.TeamGoodsModel;
import com.sdjy.sdjymall.model.UserCashBalanceModel;
import com.sdjy.sdjymall.model.UserModel;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface APIService {

    //获取首页滚动图片
    @GET("home/getHomeScrollImages.do")
    Observable<HttpResult<List<HomeScrollImageModel>>> getHomeScrollImages();

    //获取首页数据
    @GET("home/getHomePageDatas.do")
    Observable<HttpResult<HomePageDataModel>> getHomePageDatas();

    //获取商品一级分类（底部菜单分类中的商品分类）
    @GET("home/getFirstSorts.do")
    Observable<HttpResult<List<CommonDataModel>>> getFirstSorts();

    //根据商品一级分类，获取分类下信息
    @GET("home/getSecondSorts.do")
    Observable<HttpResult<List<CommonDataModel>>> getSecondSorts(
            @Query("sortId") String sortId
    );

    //搜索店铺
    @GET("shop/searchShop.do")
    Observable<HttpResult<CommonListModel<List<ShopModel>>>> searchShop(
            @Query("key") String shopName,
            @Query("page") int page
    );

    //搜索商品
    @GET("goods/searchGoods.do")
    Observable<HttpResult<CommonListModel<List<GoodsModel>>>> searchGoods(
            @QueryMap Map<String, String> params,
            @Query("page") int page
    );

    //获取商品详细信息接口
    @GET("goods/findGoods.do")
    Observable<HttpResult<GoodsInfoModel>> findGoods(
            @Query("id") String id
    );

    //获取商家详细信息接口
    @GET("shop/findShop.do")
    Observable<HttpResult<ShopModel>> findShop(
            @QueryMap Map<String, String> params
    );

    //关注商品或店铺接口
    @POST("user/userCollect.do")
    Observable<HttpResult<String>> userCollect(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("collectType") int collectType,
            @Query("oid") String oid
    );

    //取消关注商品或店铺接口
    @POST("user/cancelCollect.do")
    Observable<HttpResult> cancelCollect(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("id") String id
    );

    //登录
    @POST("checkLogin.do")
    Observable<HttpResult<UserModel>> login(
            @Query("userName") String userName,
            @Query("password") String password,
            @Query("clientId") String clientId,
            @Query("platform") String platform
    );

    //获取商品评论个数接口
    @GET("goods/findGoodsCommentsNum.do")
    Observable<HttpResult<GoodsEvaluateCountModel>> findGoodsCommentsNum(
            @Query("id") String id
    );

    //获取商品评论接口
    @GET("goods/findGoodsComments.do")
    Observable<HttpResult<CommonListModel<List<GoodsEvaluateModel>>>> findGoodsComments(
            @QueryMap Map<String, String> params
    );

    //获取用户资料接口：用户登录后进入我的首页调用此接口获取数据
    @POST("user/userCashBalance.do")
    Observable<HttpResult<UserCashBalanceModel>> userCashBalance(
            @Header("Authorization") String auth,
            @Query("userId") String userId
    );

    //加入购物车接口
    @POST("cart/addToCart.do")
    Observable<HttpResult> addToCart(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("goodsId") String goodsId,
            @Query("priceId") String priceId,
            @Query("num") int num
    );

    //获取购物车信息接口
    @POST("cart/cartGoods.do")
    Observable<HttpResult<List<CarShopModel>>> cartGoods(
            @Header("Authorization") String auth,
            @Query("userId") String userId
    );

    //同步购物车接口
    @POST("cart/syncShoppingCart.do")
    Observable<HttpResult> syncShoppingCart(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("goodsIds") String goodsIds,
            @Query("priceIds") String priceIds,
            @Query("nums") String nums
    );

    //获取充值返现数值接口
    @GET("recharge/findRechargeNums.do")
    Observable<HttpResult<List<Integer>>> findRechargeNums();

    //充值接口
    @POST("recharge/recharge.do")
    Observable<HttpResult> recharge(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("num") Integer num
    );

    //删除购物车接口
    @POST("cart/delShoppingCart.do")
    Observable<HttpResult> delShoppingCart(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @QueryMap Map<String, String> params
    );

    //查询创业套餐列表接口
    @GET("team/teamGoods.do")
    Observable<HttpResult<CommonListModel<List<TeamGoodsModel>>>> teamGoods(
            @Query("page") int page,
            @QueryMap Map<String, String> params
    );
}
