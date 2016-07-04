package com.laifukou.laifukoushop.http;

import com.laifukou.laifukoushop.model.CommonDataModel;
import com.laifukou.laifukoushop.model.HomePageDataModel;
import com.laifukou.laifukoushop.model.HomeScrollImageModel;
import com.laifukou.laifukoushop.model.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
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
    Observable<HttpResult<List<CommonDataModel>>> getSecondSorts(@Query("sortId") String sortId);
}
