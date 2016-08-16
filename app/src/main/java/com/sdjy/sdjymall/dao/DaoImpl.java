package com.sdjy.sdjymall.dao;

import com.sdjy.sdjymall.model.CarShopModel;

import java.util.List;

/**
 * 数据库接口实现
 */
public class DaoImpl implements IDao {

    @Override
    public void insertCarShop(CarShopModel carShopModel) throws Exception {
//        mRealm.beginTransaction();
//        mRealm.copyToRealm(carShopModel);
//        mRealm.commitTransaction();
    }

    @Override
    public List<CarShopModel> getAllCarShop() throws Exception {
        List<CarShopModel> mlist = null;
//        mlist = mRealm.where(CarShopModel.class).findAll();
        return mlist;
    }


}
