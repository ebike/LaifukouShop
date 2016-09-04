package com.sdjy.sdjymall.model;

import java.util.List;

/**
 * 订单详情
 */
public class OrderInfoModel {

    public String orderId;

    public String shopId;
    public String shopName;
    public int shopType;

    public int totalGoods;

    public String money;
    public int goldCoin;
    public int coin;
    public String postMoney;

    public int state;
    public String stateTime;
    //快递名称
    public String express;
    //快递单号
    public String expressNum;
    //收货人
    public String consignee;
    //手机号
    public  String phone;
    //地址
    public String address;

    public String orderTime;

    public Long expireTime;

    public List<GoodsItemModel> orderItems;

    public OrderInfoModel() {
    }
}
