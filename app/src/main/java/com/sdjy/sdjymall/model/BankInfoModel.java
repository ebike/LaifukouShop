package com.sdjy.sdjymall.model;

import java.io.Serializable;

/**
 * 银行卡信息
 */
public class BankInfoModel implements Serializable{

    public String userId;
    //账户名
    public String accountName;
    //开户行
    public String openBank;
    //卡号
    public String bankAccount;

    public BankInfoModel() {
    }
}
