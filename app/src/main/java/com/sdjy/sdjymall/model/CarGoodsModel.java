package com.sdjy.sdjymall.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 购物车中的商品
 */
public class CarGoodsModel extends RealmObject {
    //商品ID
    @PrimaryKey
    private String id;
    //商品名称
    private String goodsName;
    //商品图片
    private String imageUrl;
    //价格类型
    private int priceType;
    //现金
    private String priceMoney;
    //金币
    private String priceGoldCoin;
    //银币
    private String priceCoin;
    //数量
    private int num;
    //价格ID
    private String priceId;
    //规格
    private String stardand;
    //评价个数
    private int commentNum;
    //好评率
    private String praiseRate;

    public CarGoodsModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public String getPriceMoney() {
        return priceMoney;
    }

    public void setPriceMoney(String priceMoney) {
        this.priceMoney = priceMoney;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getStardand() {
        return stardand;
    }

    public void setStardand(String stardand) {
        this.stardand = stardand;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getPraiseRate() {
        return praiseRate;
    }

    public void setPraiseRate(String praiseRate) {
        this.praiseRate = praiseRate;
    }

    public String getPriceGoldCoin() {
        return priceGoldCoin;
    }

    public void setPriceGoldCoin(String priceGoldCoin) {
        this.priceGoldCoin = priceGoldCoin;
    }

    public String getPriceCoin() {
        return priceCoin;
    }

    public void setPriceCoin(String priceCoin) {
        this.priceCoin = priceCoin;
    }
}
