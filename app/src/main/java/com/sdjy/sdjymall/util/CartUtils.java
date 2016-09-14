package com.sdjy.sdjymall.util;

import com.sdjy.sdjymall.model.CarGoodsModel;
import com.sdjy.sdjymall.model.CarShopModel;

import java.util.List;

/**
 * 购物车工具
 */
public class CartUtils {

    /**
     * 购物车是否全部选中
     *
     * @param carShopList
     * @return 是否全部选中
     */
    public static boolean isAllSelected(List<CarShopModel> carShopList) {
        boolean flag = true;
        for (CarShopModel shopModel : carShopList) {
            for (CarGoodsModel goodsModel : shopModel.getGoods()) {
                if (!goodsModel.isSelected() && goodsModel.getStock() != 0) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean isAllSelected(CarShopModel model) {
        boolean flag = true;
        for (CarGoodsModel goodsModel : model.getGoods()) {
            if (!goodsModel.isSelected() && goodsModel.getStock() != 0) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean hasSelected(List<CarShopModel> carShopList) {
        boolean flag = false;
        for (CarShopModel shopModel : carShopList) {
            for (CarGoodsModel goodsModel : shopModel.getGoods()) {
                if (goodsModel.isSelected()) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 购物车是否全部选中(编辑状态下)
     *
     * @param carShopList
     * @return 是否全部选中
     */
    public static boolean isAllSelectedInEdit(List<CarShopModel> carShopList) {
        boolean flag = true;
        for (CarShopModel shopModel : carShopList) {
            for (CarGoodsModel goodsModel : shopModel.getGoods()) {
                if (!goodsModel.isSelectedInEdit()) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean isAllSelectedInEdit(CarShopModel model) {
        boolean flag = true;
        for (CarGoodsModel goodsModel : model.getGoods()) {
            if (!goodsModel.isSelectedInEdit()) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean hasSelectedInEdit(List<CarShopModel> carShopList) {
        boolean flag = false;
        for (CarShopModel shopModel : carShopList) {
            for (CarGoodsModel goodsModel : shopModel.getGoods()) {
                if (goodsModel.isSelectedInEdit()) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
