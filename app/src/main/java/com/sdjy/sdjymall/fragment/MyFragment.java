package com.sdjy.sdjymall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.LoginActivity;
import com.sdjy.sdjymall.common.fragment.LazyFragment;
import com.sdjy.sdjymall.constants.StaticValues;

import butterknife.Bind;
import butterknife.OnClick;

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
//        if(){
//
//        }
    }

    @OnClick(R.id.ll_login)
    public void login() {
        if (StaticValues.userModel == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @OnClick(R.id.iv_setting)
    public void setting() {

    }

    @OnClick(R.id.iv_message)
    public void message() {

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

    @OnClick(R.id.tv_refund_after_sales)
    public void refundAfterSales() {

    }

    @OnClick(R.id.rl_amount)
    public void amount() {

    }

    @OnClick(R.id.ll_account_balance)
    public void accountBalance() {

    }

    @OnClick(R.id.ll_gold_coins)
    public void goldCoins() {

    }

    @OnClick(R.id.ll_silver_coins)
    public void silverCoins() {

    }

    @OnClick(R.id.tv_goods_focus)
    public void goodsFocus() {

    }

    @OnClick(R.id.tv_shop_focus)
    public void shopFocus() {

    }

    @OnClick(R.id.tv_browsing_history)
    public void browsingHistory() {

    }

    @OnClick(R.id.tv_feedback)
    public void feedback() {

    }
}
