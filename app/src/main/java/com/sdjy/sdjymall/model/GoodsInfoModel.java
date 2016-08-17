package com.sdjy.sdjymall.model;

import java.util.List;

/**
 * 商品详细信息
 */
public class GoodsInfoModel {

    //商品ID
    public String id;
    //商品名称
    public String goodsName;
    //商品详情html内容
    public String describ;
    //价格类型 1现金 2现金+金币 3币
    public int priceType;
    //店铺ID
    public String shopId;
    //店铺名称
    public String shopName;
    //商家类型：1自营；2联盟商家
    public int shopType;
    //评论数
    public int commentNum;
    //关注记录id：存在表示商品已被关注
    public String collectId;
    //好评率
    public String praiseRate;
    //库存
    public int stock;
    public List<String> goodsPics;
    //是否包邮 1包邮2不包邮
    public int includePost;
    //邮费
    public int postPrice;
    public List<GoodsPricesModel> goodsPrices;

    public GoodsInfoModel() {
    }
}
