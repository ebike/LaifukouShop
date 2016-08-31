package com.sdjy.sdjymall.model;

/**
 * 用户信息
 */
public class UserModel {
    //用户ID
    public String userId;
    //登录名
    public String loginName;
    //姓名
    public String name;
    //性别
    public int sex;
    //身份证
    public String idCard;
    //电话
    public String phone;
    //QQ
    public String qq;
    //省
    public String province;
    //市
    public String city;
    //区县
    public String area;
    //详细地址
    public String address;
    //头像地址
    public String headPic;
    //出生日期
    public String birthday;
    //注册日期
    public String regTime;
    //用户类别：1普通用户；2互助创业用户
    public int userType;
    //互助创业用户级别
    public String userGrade;
    //请求头部信息
    public String userToken;

    public UserModel() {
    }
}
