package com.sdjy.sdjymall.dao;

import com.sdjy.sdjymall.model.CarShopModel;

import java.util.List;

/**
 * 数据库接口
 */
public interface IDao {

    /**
     * 插入购物车店铺
     *
     * @param carShopModel
     * @throws Exception
     */
    void insertCarShop(CarShopModel carShopModel) throws Exception;

    /**
     * 获取所有购物车店铺
     *
     * @return
     * @throws Exception
     */
    List<CarShopModel> getAllCarShop() throws Exception;

}
