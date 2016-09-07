package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.event.ChangeHomeTabEvent;
import com.sdjy.sdjymall.model.OrderInfoModel;
import com.sdjy.sdjymall.util.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class PaySuccessActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.tv_right)
    TextView rightView;
    @Bind(R.id.tv_total)
    TextView totalView;
    @Bind(R.id.tv_gold_coin)
    TextView goldCoinView;
    @Bind(R.id.tv_coin)
    TextView coinView;

    private OrderInfoModel infoModel;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_pay_success);
    }

    @Override
    public void init() {
        infoModel = (OrderInfoModel) getIntent().getSerializableExtra("OrderInfoModel");

        titleView.setText("订单支付成功");
        rightView.setText("完成");
        rightView.setVisibility(View.VISIBLE);

        double total = 0;
        if (!StringUtils.strIsEmpty(infoModel.money)) {
            total += Double.valueOf(infoModel.money);
        }
        if (!StringUtils.strIsEmpty(infoModel.postMoney)) {
            total += Double.valueOf(infoModel.postMoney);
        }
        totalView.setText("订单金额：￥" + total);
        goldCoinView.setText("金币个数：" + infoModel.goldCoin);
        coinView.setText("银币个数：" + infoModel.coin);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void right() {
        startActivity(new Intent(this, OrderActivity.class));
        finish();
    }

    @OnClick(R.id.tv_to_order)
    public void toOrder() {
        Intent intent = new Intent(this, OrderInfoActivity.class);
        intent.putExtra("orderId", infoModel.orderId);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tv_to_home)
    public void toHome() {
        EventBus.getDefault().post(new ChangeHomeTabEvent(0));
        finish();
    }
}
