package com.sdjy.sdjymall.model;

import java.util.List;

/**
 * 订单列表
 */
public class OrderModel {

    public String orderId;

    public String shopId;

    public String shopName;

    public int shopType;

    public int totalGoods;

    public List<String> pics;

    public String money;

    public int goldCoin;

    public int coin;

    public int state;

    public OrderModel() {
    }
}
