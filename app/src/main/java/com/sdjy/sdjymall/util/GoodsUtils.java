package com.sdjy.sdjymall.util;

import com.sdjy.sdjymall.model.CarGoodsModel;
import com.sdjy.sdjymall.model.GoodsBrowsingModel;
import com.sdjy.sdjymall.model.GoodsItemModel;
import com.sdjy.sdjymall.model.GoodsModel;
import com.sdjy.sdjymall.model.GoodsPricesModel;
import com.sdjy.sdjymall.model.HomePageDataItemModel;

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
                price = "￥" + pricesModel.priceMoney + "+" + pricesModel.priceGoldCoin + "金币";
                break;
            case 3:
                price = pricesModel.priceCoin + "银币";
                break;
        }
        return price;
    }

    public static String getPrice(int priceType, GoodsModel model) {
        String price = "";
        switch (priceType) {
            case 1:
                price = "￥" + model.priceMoney;
                break;
            case 2:
                price = "￥" + model.priceMoney + "+" + model.priceGoldCoin + "金币";
                break;
            case 3:
                price = model.priceCoin + "银币";
                break;
        }
        return price;
    }

    public static String getPrice(int priceType, CarGoodsModel model) {
        String price = "";
        switch (priceType) {
            case 1:
                price = "￥" + model.getPriceMoney();
                break;
            case 2:
                price = "￥" + model.getPriceMoney() + "+" + model.getPriceGoldCoin() + "金币";
                break;
            case 3:
                price = model.getPriceCoin() + "银币";
                break;
        }
        return price;
    }

    public static String getPrice(int priceType, GoodsItemModel model) {
        String price = "";
        switch (priceType) {
            case 1:
                price = "￥" + model.priceMoney;
                break;
            case 2:
                price = "￥" + model.priceMoney + "+" + model.priceGoldCoin + "金币";
                break;
            case 3:
                price = model.priceCoin + "银币";
                break;
        }
        return price;
    }

    public static String getPrice(int priceType, HomePageDataItemModel model) {
        String price = "";
        switch (priceType) {
            case 1:
                price = "￥" + model.priceMoney;
                break;
            case 2:
                price = "￥" + model.priceMoney + "+" + model.priceGoldCoin + "金币";
                break;
            case 3:
                price = model.priceCoin + "银币";
                break;
        }
        return price;
    }

    public static String formatPrice(String money, int goldCoin, int coin) {
        StringBuffer amount = new StringBuffer();
        if (!"0.0".equals(money) && goldCoin != 0 && coin != 0) {
            amount.append("￥").append(money);
            amount.append("+").append(goldCoin).append("金币");
            amount.append("+").append(coin).append("银币");
        } else if (!"0.0".equals(money) && goldCoin != 0) {
            amount.append("￥").append(money);
            amount.append("+").append(goldCoin).append("金币");
        } else if (!"0.0".equals(money) && coin != 0) {
            amount.append("￥").append(money);
            amount.append("+").append(coin).append("银币");
        } else if (goldCoin != 0 && coin != 0) {
            amount.append(goldCoin).append("金币");
            amount.append("+").append(coin).append("银币");
        } else if (!"0.0".equals(money)) {
            amount.append("￥").append(money);
        } else if (goldCoin != 0) {
            amount.append(goldCoin).append("金币");
        } else if (coin != 0) {
            amount.append(coin).append("银币");
        }
        return amount.toString();
    }
}
