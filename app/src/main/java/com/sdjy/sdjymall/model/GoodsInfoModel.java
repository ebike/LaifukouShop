package com.sdjy.sdjymall.model;

import java.util.List;

/**
 * 商品详细信息
 */
public class GoodsInfoModel {

    public String id;
    public String goodsName;
    public String describ;
    public int priceType;
    public int shopType;
    public int commentNum;
    public String praiseRate;
    public int stock;
    public List<String> goodsPics;
    public int includePost;
    public int postPrice;
    public List<GoodsPricesModel> goodsPrices;

    public GoodsInfoModel() {
    }
}
