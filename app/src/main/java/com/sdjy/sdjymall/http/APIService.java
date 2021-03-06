package com.sdjy.sdjymall.http;

import com.sdjy.sdjymall.model.AddressModel;
import com.sdjy.sdjymall.model.BankInfoModel;
import com.sdjy.sdjymall.model.CarShopModel;
import com.sdjy.sdjymall.model.CommonDataModel;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.GoodsBrowsingModel;
import com.sdjy.sdjymall.model.GoodsEvaluateCountModel;
import com.sdjy.sdjymall.model.GoodsEvaluateModel;
import com.sdjy.sdjymall.model.GoodsInfoModel;
import com.sdjy.sdjymall.model.GoodsItemModel;
import com.sdjy.sdjymall.model.GoodsModel;
import com.sdjy.sdjymall.model.HistorySearchModel;
import com.sdjy.sdjymall.model.HomePageDataModel;
import com.sdjy.sdjymall.model.HomeScrollImageModel;
import com.sdjy.sdjymall.model.HotSearchWordModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.model.OrderConfirmModel;
import com.sdjy.sdjymall.model.OrderInfoModel;
import com.sdjy.sdjymall.model.OrderModel;
import com.sdjy.sdjymall.model.RefereeUserModel;
import com.sdjy.sdjymall.model.ShopHotGroupModel;
import com.sdjy.sdjymall.model.ShopModel;
import com.sdjy.sdjymall.model.TeamGoodsModel;
import com.sdjy.sdjymall.model.TeamModel;
import com.sdjy.sdjymall.model.UserCashBalanceModel;
import com.sdjy.sdjymall.model.UserModel;
import com.sdjy.sdjymall.model.ValidateCodeModel;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
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
            @Query("id") String id,
            @QueryMap Map<String, String> params
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

    //更新购物车接口
    @POST("cart/updateCart.do")
    Observable<HttpResult> updateCart(
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
    Observable<HttpResult<OrderInfoModel>> recharge(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("amount") Integer amount,
            @Query("payMode") String payMode
    );

    //通知充值支付成功接口
    @POST("recharge/appNotifyRecharge.do")
    Observable<HttpResult> appNotifyRecharge(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("orderId") String orderId
    );

    //删除购物车接口
    @POST("cart/delShoppingCart.do")
    Observable<HttpResult> delShoppingCart(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("goodsIds") String goodsIds,
            @Query("priceIds") String priceIds
    );

    //查询创业套餐列表接口
    @GET("team/teamGoods.do")
    Observable<HttpResult<CommonListModel<List<TeamGoodsModel>>>> teamGoods(
            @Query("page") int page,
            @QueryMap Map<String, String> params
    );

    //退出账号接口
    @GET("logout.do")
    Observable<HttpResult> logout(
            @Query("userId") String userId
    );

    //发送找回密码短信验证码接口
    @POST("sms/sendUpdatePassCode.do")
    Observable<HttpResult<ValidateCodeModel>> sendUpdatePassCode(
            @Query("phone") String phone
    );

    //发送注册短信验证码接口
    @POST("sms/sendRegCode.do")
    Observable<HttpResult<ValidateCodeModel>> sendRegCode(
            @Query("phone") String phone
    );

    //根据手机号验证用户是否存在接口
    @GET("user/checkUserByPhone.do")
    Observable<HttpResult<String>> checkUserByPhone(
            @Query("phone") String phone
    );

    //重置密码接口
    @POST("user/resetPassword.do")
    Observable<HttpResult> resetPassword(
            @Query("userId") String userId,
            @Query("password") String password
    );

    //查看创业套餐详细信息接口
    @GET("team/findTeamGoods.do")
    Observable<HttpResult<TeamGoodsModel>> findTeamGoods(
            @Query("id") String id,
            @QueryMap Map<String, String> params
    );

    //修改用户头像接口
    @Multipart
    @POST("user/updateHeadPic.do")
    Observable<HttpResult<UserModel>> updateHeadPic(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @PartMap Map<String, RequestBody> map
    );

    //修改用户资料接口
    @FormUrlEncoded
    @POST("user/updateUserData.do")
    Observable<HttpResult<UserModel>> updateUserData(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @FieldMap Map<String, String> params
    );

    //用户注册接口
    @POST("user/regUser.do")
    Observable<HttpResult<UserModel>> regUser(
            @Query("phone") String phone,
            @Query("password") String password,
            @Query("clientId") String clientId,
            @Query("platform") String platform
    );

    //获取问题反馈类型接口
    @GET("feedback/getFeedbackType.do")
    Observable<HttpResult<List<String>>> getFeedbackType();

    //问题反馈接口
    @FormUrlEncoded
    @POST("feedback/saveFeedback.do")
    Observable<HttpResult> saveFeedback(
            @Field("feedType") String feedType,
            @Field("content") String content,
            @Field("contact") String contact
    );

    //获取浏览记录接口
    @POST("user/userBrowse.do")
    Observable<HttpResult<List<GoodsBrowsingModel>>> userBrowse(
            @Header("Authorization") String auth,
            @Query("userId") String userId
    );

    //获取搜索热词接口
    @GET("search/hotSearchWord.do")
    Observable<HttpResult<List<HotSearchWordModel>>> hotSearchWord();

    //获取搜索历史记录接口
    @POST("search/searchHis.do")
    Observable<HttpResult<List<HistorySearchModel>>> searchHis(
            @Header("Authorization") String auth,
            @Query("userId") String userId
    );

    //清空用户历史记录接口
    @POST("search/clearSearchHis.do")
    Observable<HttpResult> clearSearchHis(
            @Header("Authorization") String auth,
            @Query("userId") String userId
    );

    //删除指定搜索历史接口
    @POST("search/delSearchWord.do")
    Observable<HttpResult> delSearchWord(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("id") String id
    );

    //获取关注商品记录接口
    @POST("user/collectGoods.do")
    Observable<HttpResult<CommonListModel<List<GoodsBrowsingModel>>>> collectGoods(
            @Header("Authorization") String auth,
            @Query("userId") String userId
    );

    //获取关注商家记录接口
    @POST("user/collectShops.do")
    Observable<HttpResult<CommonListModel<List<ShopModel>>>> collectShops(
            @Header("Authorization") String auth,
            @Query("userId") String userId
    );

    //批量删除浏览记录接口
    @POST("user/delUserBrowse.do")
    Observable<HttpResult> delUserBrowse(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("ids") String ids
    );

    //查询用户所在的所有团队接口
    @POST("team/findUserTeams.do")
    Observable<HttpResult<CommonListModel<List<TeamModel>>>> findUserTeams(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("page") int page
    );

    //查看用户推荐的所有人接口
    @POST("team/findRefereeUser.do")
    Observable<HttpResult<CommonListModel<List<RefereeUserModel>>>> findRefereeUser(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("page") int page
    );

    //获取收货地址接口
    @POST("address/addressList.do")
    Observable<HttpResult<List<AddressModel>>> addressList(
            @Header("Authorization") String auth,
            @Query("userId") String userId
    );

    //添加、更新、设为默认的接口
    @FormUrlEncoded
    @POST("address/saveOrUpdateAddress.do")
    Observable<HttpResult> saveOrUpdateAddress(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @FieldMap Map<String, String> params
    );

    //删除收货地址接口
    @POST("address/delAddress.do")
    Observable<HttpResult> delAddress(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("id") String id
    );

    //获取银行卡信息接口
    @POST("team/findBankInfo.do")
    Observable<HttpResult<BankInfoModel>> findBankinfo(
            @Header("Authorization") String auth,
            @Query("userId") String userId
    );

    //修改银行卡信息接口
    @FormUrlEncoded
    @POST("team/updateBankInfo.do")
    Observable<HttpResult> updateBankInfo(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @FieldMap Map<String, String> params
    );

    //验证推荐人接口
    @POST("team/checkRefereeUser.do")
    Observable<HttpResult<TeamModel>> checkRefereeUser(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("goodsId") String goodsId,
            @Query("phone") String phone
    );

    //支付加入团队接口
    @POST("team/payJoinTeam.do")
    Observable<HttpResult<OrderInfoModel>> payJoinTeam(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("refereeId") String refereeId,
            @Query("goodsId") String goodsId,
            @Query("payMode") String payMode
    );

    //通知加入团队成功接口
    @POST("team/appNotifyJoinTeam.do")
    Observable<HttpResult> appNotifyJoinTeam(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("orderId") String orderId
    );

    //支付创建团队接口
    @POST("team/payCreateTeam.do")
    Observable<HttpResult<OrderInfoModel>> payCreateTeam(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("teamName") String teamName,
            @Query("goodsId") String goodsId,
            @Query("payMode") String payMode
    );

    //通知创建团队成功接口
    @POST("team/appNotifyCreateTeam.do")
    Observable<HttpResult> appNotifyCreateTeam(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("orderId") String orderId
    );

    //获取商家热门分类接口
    @GET("shop/findShopHotGroup.do")
    Observable<HttpResult<List<ShopHotGroupModel>>> findShopHotGroup(
            @Query("shopId") String shopId
    );

    //根据分类获取商品接口
    @GET("goods/findGoodsByGroup.do")
    Observable<HttpResult<CommonListModel<List<GoodsModel>>>> findGoodsByGroup(
            @Query("shopId") String shopId,
            @Query("groupId") String groupId,
            @Query("page") int page
    );

    //查询订单接口
    @POST("order/orderList.do")
    Observable<HttpResult<CommonListModel<List<OrderModel>>>> orderList(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("page") int page,
            @Query("state") String state
    );

    //查询订单详细信息接口
    @POST("order/findOrder.do")
    Observable<HttpResult<OrderInfoModel>> findOrder(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("orderId") String orderId
    );

    //查询订单接口
    @POST("order/waitCommentOrder.do")
    Observable<HttpResult<CommonListModel<List<GoodsItemModel>>>> waitCommentOrder(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("page") int page
    );

    //更新订单状态接口
    @POST("order/updateOrderState.do")
    Observable<HttpResult> updateOrderState(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("orderId") String orderId,
            @Query("state") String state
    );

    //商品评论接口
    @POST("order/commentGoods.do")
    Observable<HttpResult> commentGoods(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("orderItemId") String orderItemId,
            @Query("goodsId") String goodsId,
            @Query("score") int score,
            @Query("content") String content
    );

    //提交订单确认接口
    @POST("order/confirmOrder.do")
    Observable<HttpResult<OrderConfirmModel>> confirmOrder(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("ids") String ids
    );

    //提交订单接口
    @POST("order/submitOrder.do")
    Observable<HttpResult<OrderInfoModel>> submitOrder(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("ids") String ids,
            @Query("addressId") String addressId
    );

    //支付订单接口
    @POST("order/payOrder.do")
    Observable<HttpResult<OrderInfoModel>> payOrder(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("orderId") String orderId,
            @Query("useBalance") int useBalance,
            @Query("payMode") String payMode
    );

    //去付款接口
    @POST("order/toPayOrder.do")
    Observable<HttpResult<OrderInfoModel>> toPayOrder(
            @Header("Authorization") String auth,
            @Query("userId") String userId,
            @Query("orderId") String orderId
    );

}
