package com.sdjy.sdjymall.model;

import java.util.List;

/**
 * 确认订单
 */
public class OrderConfirmModel {

    public String money;
    public String goldCoin;
    public String coin;
    public String postMoney;
    public String totalGoods;
    public List<GoodsSampleItemModel> items;
    public AddressModel address;

    public OrderConfirmModel() {
    }
}
