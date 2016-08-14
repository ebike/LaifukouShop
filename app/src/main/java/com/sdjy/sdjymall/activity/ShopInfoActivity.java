package com.sdjy.sdjymall.activity;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.ShopModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 店铺首页
 */
public class ShopInfoActivity extends BaseActivity {

    private String shopId;
    private ShopModel shopModel;
    private SubscriberOnNextListener<ShopModel> nextListener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_shop_info);
    }

    @Override
    public void init() {
        shopId = getIntent().getStringExtra("shopId");

        nextListener = new SubscriberOnNextListener<ShopModel>() {
            @Override
            public void onNext(ShopModel model) {
                shopModel = model;
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("id", shopId);
        if (StaticValues.userModel != null) {
            params.put("userId", StaticValues.userModel.id);
        }
        HttpMethods.getInstance().findShop(new ProgressSubscriber<ShopModel>(nextListener, this), params);
    }
}
