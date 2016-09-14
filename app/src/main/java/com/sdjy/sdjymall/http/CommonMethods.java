package com.sdjy.sdjymall.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sdjy.sdjymall.activity.OrderPayActivity;
import com.sdjy.sdjymall.activity.PaySuccessActivity;
import com.sdjy.sdjymall.common.util.SPUtils;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.event.LogoutEvent;
import com.sdjy.sdjymall.model.CarGoodsModel;
import com.sdjy.sdjymall.model.CarShopModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.model.OrderInfoModel;
import com.sdjy.sdjymall.model.UserCashBalanceModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
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

    //账户资金
    public static void userCashBalance(Context context) {
        SubscriberOnNextListener listener = new SubscriberOnNextListener<UserCashBalanceModel>() {
            @Override
            public void onNext(UserCashBalanceModel model) {
                StaticValues.balanceModel = model;
            }
        };
        HttpMethods.getInstance().userCashBalance(new NoProgressSubscriber<UserCashBalanceModel>(listener, context), StaticValues.userModel.userId);
    }

    //去付款
    public static void toPayOrder(final Context context, String orderId) {
        HttpMethods.getInstance().toPayOrder(new ProgressSubscriber<OrderInfoModel>(new SubscriberOnNextListener<HttpResult<OrderInfoModel>>() {
            @Override
            public void onNext(HttpResult<OrderInfoModel> httpResult) {
                Intent intent = new Intent();
                intent.putExtra("OrderInfoModel", httpResult.data);
                if ("1".equals(httpResult.code) || "2".equals(httpResult.code)) {
                    intent.setClass(context, OrderPayActivity.class);
                    intent.putExtra("code", httpResult.code);
                    context.startActivity(intent);
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                } else if ("3".equals(httpResult.code)) {
                    intent.setClass(context, PaySuccessActivity.class);
                    context.startActivity(intent);
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                } else {
                    T.showShort(context, httpResult.message);
                }
            }
        }, context), orderId);
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
