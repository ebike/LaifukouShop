package com.sdjy.sdjymall.util;

import com.sdjy.sdjymall.model.GoodsBrowsingModel;
import com.sdjy.sdjymall.model.GoodsPricesModel;

import java.util.List;

/**
 * 商品工具类
 */
public class GoodsUtils {

    public static boolean isAllSelected(List<GoodsBrowsingModel> list) {
        boolean flag = true;
        for (GoodsBrowsingModel browsingModel : list) {
            if (!browsingModel.isSelected) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean hasSelected(List<GoodsBrowsingModel> list) {
        boolean flag = false;
        for (GoodsBrowsingModel browsingModel : list) {
            if (browsingModel.isSelected) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static String getPrice(int priceType, GoodsPricesModel pricesModel) {
        String price = "";
        switch (priceType) {
            case 1:
                price = "￥" + pricesModel.priceMoney;
                break;
            case 2:
                price = "￥" + pricesModel.priceMoney + " + 金币 " + pricesModel.priceGoldCoin;
                break;
            case 3:
                price = "币 " + pricesModel.priceCoin;
                break;
        }
        return price;
    }
}
