package com.sdjy.sdjymall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.AccountManageActivity;
import com.sdjy.sdjymall.activity.BrowsingHistoryActivity;
import com.sdjy.sdjymall.activity.FeedbackActivity;
import com.sdjy.sdjymall.activity.GoodsShopFocusActivity;
import com.sdjy.sdjymall.activity.LoginActivity;
import com.sdjy.sdjymall.activity.MessageActivity;
import com.sdjy.sdjymall.activity.SettingsActivity;
import com.sdjy.sdjymall.common.fragment.LazyFragment;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.event.LogoutEvent;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.UserCashBalanceModel;
import com.sdjy.sdjymall.model.UserModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 我的
 */
public class MyFragment extends LazyFragment {

    @Bind(R.id.iv_header)
    ImageView headerView;
    @Bind(R.id.tv_name)
    TextView nameView;
    @Bind(R.id.tv_manage)
    TextView manageView;
    @Bind(R.id.tv_account_balance)
    TextView accountBalanceView;
    @Bind(R.id.tv_gold_coins)
    TextView goldCoinsView;
    @Bind(R.id.tv_silver_coins)
    TextView silverCoinsView;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_my);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        if (StaticValues.userModel != null) {
            Glide.with(getActivity())
                    .load(StaticValues.userModel.headPic)
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .error(R.mipmap.icon_default_head)
                    .into(headerView);
            nameView.setText(StaticValues.userModel.loginName);
            manageView.setVisibility(View.VISIBLE);
            SubscriberOnNextListener listener = new SubscriberOnNextListener<UserCashBalanceModel>() {
                @Override
                public void onNext(UserCashBalanceModel model) {
                    accountBalanceView.setText("￥" + model.money);
                    goldCoinsView.setText(model.goldCoin);
                    silverCoinsView.setText(model.coin);
                }
            };
            HttpMethods.getInstance().userCashBalance(new NoProgressSubscriber<UserCashBalanceModel>(listener, getActivity()), StaticValues.userModel.userId);
        } else {
            headerView.setImageResource(R.mipmap.icon_default_head);
            nameView.setText("登录/注册");
            manageView.setVisibility(View.GONE);
            accountBalanceView.setText("￥0");
            goldCoinsView.setText("0");
            silverCoinsView.setText("0");
        }
    }

    public void onEvent(UserModel model) {
        init();
    }

    public void onEvent(LogoutEvent event) {
        init();
    }

    @OnClick(R.id.ll_login)
    public void login() {
        if (StaticValues.userModel != null) {
            startActivity(new Intent(getActivity(), AccountManageActivity.class));
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @OnClick(R.id.iv_setting)
    public void setting() {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    @OnClick(R.id.iv_message)
    public void message() {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_all_order)
    public void allOrder() {

    }

    @OnClick(R.id.tv_payment)
    public void payment() {

    }

    @OnClick(R.id.tv_delivery)
    public void delivery() {

    }

    @OnClick(R.id.tv_take_delivery)
    public void takeDelivery() {

    }

    @OnClick(R.id.tv_comment)
    public void comment() {

    }

    @OnClick(R.id.rl_amount)
    public void amount() {

    }

    @OnClick(R.id.tv_goods_focus)
    public void goodsFocus() {
        Intent intent = new Intent(getActivity(), GoodsShopFocusActivity.class);
        intent.putExtra("currentFragment", 0);
        startActivity(intent);
    }

    @OnClick(R.id.tv_shop_focus)
    public void shopFocus() {
        Intent intent = new Intent(getActivity(), GoodsShopFocusActivity.class);
        intent.putExtra("currentFragment", 1);
        startActivity(intent);
    }

    @OnClick(R.id.tv_browsing_history)
    public void browsingHistory() {
        startActivity(new Intent(getActivity(), BrowsingHistoryActivity.class));
    }

    @OnClick(R.id.tv_feedback)
    public void feedback() {
        startActivity(new Intent(getActivity(), FeedbackActivity.class));
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }
}
