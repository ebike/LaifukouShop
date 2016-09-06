package com.sdjy.sdjymall.model;

import java.io.Serializable;

/**
 * 商品项
 */
public class GoodsItemModel implements Serializable{

    public String orderItemId;
    public String goodsId;
    public String goodsName;
    public String pic;
    public int num;
    public String standard;
    public String priceId;
    public int priceType;
    public String priceMoney;
    public int priceGoldCoin;
    public int priceCoin;

    public GoodsItemModel() {
    }
}
