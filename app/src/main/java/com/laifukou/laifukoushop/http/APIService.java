package com.laifukou.laifukoushop.http;

import com.laifukou.laifukoushop.model.CommonDataModel;
import com.laifukou.laifukoushop.model.CommonListModel;
import com.laifukou.laifukoushop.model.GoodsModel;
import com.laifukou.laifukoushop.model.HomePageDataModel;
import com.laifukou.laifukoushop.model.HomeScrollImageModel;
import com.laifukou.laifukoushop.model.HttpResult;
import com.laifukou.laifukoushop.model.ShopModel;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
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

}
