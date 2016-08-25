package com.sdjy.sdjymall.http;

import android.content.Context;

import com.sdjy.sdjymall.common.util.SPUtils;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.event.LogoutEvent;
import com.sdjy.sdjymall.model.CarGoodsModel;
import com.sdjy.sdjymall.model.CarShopModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;

import java.util.List;

import de.greenrobot.event.EventBus;
import io.realm.Realm;

/**
 * 公用方法
 */
public class CommonMethods {

    //同步购物车
    public static void syncShoppingCart(Context context) {
        final Realm realm = Realm.getDefaultInstance();
        List<CarGoodsModel> carGoodsList = realm.where(CarGoodsModel.class).findAll();
        if (carGoodsList != null && carGoodsList.size() > 0) {
            StringBuffer goodsIds = new StringBuffer();
            StringBuffer priceIds = new StringBuffer();
            StringBuffer nums = new StringBuffer();
            for (int i = 0; i < carGoodsList.size(); i++) {
                goodsIds.append(carGoodsList.get(i).getId()).append(";");
                priceIds.append(carGoodsList.get(i).getPriceId()).append(";");
                nums.append(carGoodsList.get(i).getNum() + "").append(";");
            }
            goodsIds.deleteCharAt(goodsIds.length() - 1);
            priceIds.deleteCharAt(priceIds.length() - 1);
            nums.deleteCharAt(nums.length() - 1);
            SubscriberOnNextListener listener = new SubscriberOnNextListener<HttpResult>() {
                @Override
                public void onNext(HttpResult httpResult) {
                    realm.beginTransaction();
                    realm.delete(CarShopModel.class);
                    realm.delete(CarGoodsModel.class);
                    realm.commitTransaction();
                }
            };
            HttpMethods.getInstance().syncShoppingCart(new ProgressSubscriber(listener, context), goodsIds.toString(), priceIds.toString(), nums.toString());
        }
        realm.close();
    }

    //退出登录
    public static void logout(final Context context) {
        SubscriberOnNextListener listener = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult httpResult) {
                T.showShort(context, httpResult.message);
                StaticValues.userModel = null;
                SPUtils.remove(context, "loginName");
                SPUtils.remove(context, "password");
                SPUtils.remove(context, "loginTime");
                EventBus.getDefault().post(new LogoutEvent());
            }
        };
        HttpMethods.getInstance().logout(new ProgressSubscriber(listener, context));
    }
}
