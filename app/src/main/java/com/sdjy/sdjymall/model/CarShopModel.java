package com.sdjy.sdjymall.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 购物车中的店铺
 */
public class CarShopModel extends RealmObject {
    //店铺ID
    @PrimaryKey
    private String shopId;
    //商家类型：1自营；2联盟商家
    private int shopType;
    //店铺名称
    private String shopName;
    //购物车中的商品
    private RealmList<CarGoodsModel> goods;
    //是否选中
    private boolean isSelected;
    //是否选中（编辑状态下）
    private boolean isSelectedInEdit;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelectedInEdit() {
        return isSelectedInEdit;
    }

    public void setSelectedInEdit(boolean selectedInEdit) {
        isSelectedInEdit = selectedInEdit;
    }
}
