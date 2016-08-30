package com.sdjy.sdjymall.model;

/**
 * 商家
 */
public class ShopModel {
    //店铺ID
    public String id;
    //商家ID
    public String shopId;
    //商家名称
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
    //关注记录id
    public String oid;
    //关注人数
    public String collectNum;
    //关注时间
    public String collectTime;
    //全部商品个数
    public String goodsNum;
    //热销商品个数
    public String hotGoodsNum;
    //上新商品个数
    public String newGoodsNum;

    public ShopModel() {
    }
}
