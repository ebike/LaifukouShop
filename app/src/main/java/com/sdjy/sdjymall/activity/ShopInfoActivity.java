package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.DialogUtils;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.ShopModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 店铺首页
 */
public class ShopInfoActivity extends BaseActivity {

    @Bind(R.id.iv_logo)
    ImageView logoView;
    @Bind(R.id.tv_name)
    TextView nameView;
    @Bind(R.id.tv_shop_type)
    TextView shopTypeView;
    @Bind(R.id.tv_focus_count)
    TextView focusCountView;
    @Bind(R.id.tv_focus)
    TextView focusView;
    @Bind(R.id.tv_cus_phone)
    TextView cusPhoneView;

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
                initView();
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("id", shopId);
        if (StaticValues.userModel != null) {
            params.put("userId", StaticValues.userModel.userId);
        }
        HttpMethods.getInstance().findShop(new ProgressSubscriber<ShopModel>(nextListener, this), params);
    }

    private void initView() {
        Glide.with(this)
                .load(shopModel.logo)
                .error(R.mipmap.icon_shop_logo)
                .into(logoView);
        nameView.setText(shopModel.shopName);
        if (shopModel.shopType == 1) {
            shopTypeView.setText("自营商家");
        } else if (shopModel.shopType == 2) {
            shopTypeView.setText("联盟商家");
        }
        focusCountView.setText("");
        if (!StringUtils.strIsEmpty(shopModel.collectId)) {//已关注
            focusView.setText("已关注");
        } else {//未关注
            focusView.setText("关注");
        }
        if (!StringUtils.strIsEmpty(shopModel.cusPhone)) {
            cusPhoneView.setText("联系客服");
        } else {
            cusPhoneView.setText("暂无客服");
        }
    }

    @OnClick(R.id.iv_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.tv_shop_detail)
    public void shopDetail() {

    }

    @OnClick(R.id.ll_hot_sorts)
    public void hotSorts() {

    }

    @OnClick(R.id.ll_cus_phone)
    public void cusPhone() {
        if (!StringUtils.strIsEmpty(shopModel.cusPhone)) {
            DialogUtils.showDialog(this, "", shopModel.cusPhone, "呼叫", "取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 用intent启动拨打电话
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + shopModel.cusPhone));
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
