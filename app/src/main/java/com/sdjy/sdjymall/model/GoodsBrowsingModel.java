package com.sdjy.sdjymall.model;

import java.io.Serializable;
import java.util.List;

/**
 * 浏览记录
 */
public class GoodsBrowsingModel implements Serializable{
    //商品ID
    public String goodsId;
    //商品名称
    public String goodsName;
    //图片地址
    public String imageUrl;
    //价格类型
    public int priceType;
    //状态 1、正常销售 2、待审核 3、审核未通过 4、下架 5、删除
    public int state;
    //浏览时间
    public String browseTime;
    //是否选中
    public boolean isSelected;
    //商品规格
    public List<GoodsPricesModel> goodsPrices;

    public GoodsBrowsingModel() {
    }
}
