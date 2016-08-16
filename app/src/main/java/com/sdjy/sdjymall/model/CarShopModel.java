package com.sdjy.sdjymall.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * 购物车中的店铺
 */
public class CarShopModel extends RealmObject {
    //店铺ID
    private String shopId;
    //商家类型：1自营；2联盟商家
    private int shopType;
    //店铺名称
    private String shopName;
    //购物车中的商品
    private RealmList<CarGoodsModel> goods;

    public CarShopModel() {
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public RealmList<CarGoodsModel> getGoods() {
        return goods;
    }

    public void setGoods(RealmList<CarGoodsModel> goods) {
        this.goods = goods;
    }
}
