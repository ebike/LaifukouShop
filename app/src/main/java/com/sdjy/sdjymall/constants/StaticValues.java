package com.sdjy.sdjymall.constants;

import com.sdjy.sdjymall.model.UserCashBalanceModel;
import com.sdjy.sdjymall.model.UserModel;

/**
 * 内存中的静态变量
 * 全局使用
 */
public class StaticValues {

    //当前登录的用户信息
    public static UserModel userModel;
    //当前账号余额
    public static UserCashBalanceModel balanceModel;
    //clientId
    public static String imei;
}
