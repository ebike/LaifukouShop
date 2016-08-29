package com.sdjy.sdjymall.util;

import com.sdjy.sdjymall.model.GoodsBrowsingModel;

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
}
