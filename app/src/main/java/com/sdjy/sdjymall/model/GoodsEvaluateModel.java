package com.sdjy.sdjymall.model;

/**
 * 商品评价
 */
public class GoodsEvaluateModel {
    //商品ID
    public String goodsId;
    //用户ID
    public String userId;
    //用户名
    public String logiName;
    //头像地址
    public String headPic;
    //规格
    public String standard;
    //购买时间
    public String buyTime;
    //评价内容
    public String content;
    //评价时间
    public String commentTime;
    //评分
    public int score;

    public GoodsEvaluateModel() {
    }
}
