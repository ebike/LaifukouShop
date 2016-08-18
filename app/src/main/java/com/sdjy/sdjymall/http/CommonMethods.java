package com.sdjy.sdjymall.http;

import android.content.Context;

import com.sdjy.sdjymall.model.CarGoodsModel;
import com.sdjy.sdjymall.model.CarShopModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

/**
 * 公用方法
 */
public class CommonMethods {

    public static void syncShoppingCart(Context context) {
        final Realm realm = Realm.getDefaultInstance();
        List<CarGoodsModel> carGoodsList = realm.where(CarGoodsModel.class).findAll();
        if (carGoodsList != null && carGoodsList.size() > 0) {
            Map<String, String> params = new HashMap<>();
            for (int i = 0; i < carGoodsList.size(); i++) {
                params.put("goodsIds[" + i + "]", carGoodsList.get(i).getId());
                params.put("priceIds[" + i + "]", carGoodsList.get(i).getPriceId());
                params.put("nums[" + i + "]", carGoodsList.get(i).getNum() + "");
            }
            SubscriberOnNextListener listener = new SubscriberOnNextListener<HttpResult>() {
                @Override
                public void onNext(HttpResult httpResult) {
                    realm.beginTransaction();
                    realm.delete(CarShopModel.class);
                    realm.commitTransaction();
                }
            };
            HttpMethods.getInstance().syncShoppingCart(new ProgressSubscriber(listener, context), params);
        }
        realm.close();
    }
}
