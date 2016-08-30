package com.sdjy.sdjymall.model;

/**
 * 收货地址
 */
public class AddressModel {

    public String id;
    //用户ID
    public String userId;
    //别名
    public String alias;
    //收货人
    public String consignee;
    //省
    public String province;
    //市
    public String city;
    //区县
    public String area;
    //详细地址
    public String address;
    //电话
    public String phone;
    //手机
    public String mobile;
    //是否默认地址
    public int isDefault;

    public AddressModel() {
    }
}
