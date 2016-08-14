package com.sdjy.sdjymall.model;

/**
 * 商家/店铺
 */
public class ShopModel {

    public String id;

    public String shopName;

    public String title;

    public String logo;

    public String cover;
    //客服QQ
    public String qq;
    //客服电话
    public String cusPhone;
    //商家类型：1自营商家；2联盟商家
    public int shopType;
    //关注记录id：存在表示商家已被关注
    public String collectId;

    public ShopModel() {
    }
}
