package com.sdjy.sdjymall.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.constants.FinalValues;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.model.OrderInfoModel;
import com.sdjy.sdjymall.model.PayResult;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.StringUtils;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class OrderPayActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.tv_amount)
    TextView amountView;
    @Bind(R.id.tv_gold_coin)
    TextView goldCoinView;
    @Bind(R.id.tv_coin)
    TextView coinView;
    @Bind(R.id.tv_post_price)
    TextView postPriceView;
    @Bind(R.id.tv_account_balance)
    TextView accountBalanceView;
    @Bind(R.id.tv_less)
    TextView lessView;
    @Bind(R.id.tv_warn)
    TextView warnView;

    private OrderInfoModel infoModel;
    private String code;
    private boolean unuseWallet;
    double less = 0;
    double total = 0;
    private int useBalance = 2;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_order_pay);
    }

    @Override
    public void init() {
        infoModel = (OrderInfoModel) getIntent().getSerializableExtra("OrderInfoModel");
        code = getIntent().getStringExtra("code");

        titleView.setText("支付订单");

        amountView.setText("￥" + infoModel.money);
        goldCoinView.setText(infoModel.goldCoin + "");
        coinView.setText(infoModel.coin + "");
        postPriceView.setText("￥" + infoModel.postMoney);
        accountBalanceView.setText("账户余额：￥" + StaticValues.balanceModel.money);

        if (!StringUtils.strIsEmpty(infoModel.money)) {
            total += Double.valueOf(infoModel.money);
        }
        if (!StringUtils.strIsEmpty(infoModel.postMoney)) {
            total += Double.valueOf(infoModel.postMoney);
        }
        if (!StringUtils.strIsEmpty(StaticValues.balanceModel.money)) {
            less = total - Double.valueOf(StaticValues.balanceModel.money);
        }
        if (less > 0) {
            lessView.setText("￥" + less);
        } else {
            lessView.setText("￥0");
        }

        if ("1".equals(code)) {
            warnView.setVisibility(View.GONE);
        } else if ("2".equals(code)) {
            warnView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_account_balance)
    public void balance() {
        if (unuseWallet) {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_sel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            accountBalanceView.setCompoundDrawables(drawable, null, null, null);
            if (less > 0) {
                lessView.setText("￥" + less);
            } else {
                lessView.setText("￥0");
            }
            useBalance = 2;
            unuseWallet = false;
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_nosel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            accountBalanceView.setCompoundDrawables(drawable, null, null, null);
            lessView.setText("￥" + total);
            useBalance = 1;
            unuseWallet = true;
        }
    }

    @OnClick(R.id.tv_warn)
    public void warn() {
        startActivity(new Intent(this, RechargeActivity.class));
        finish();
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        if ("1".equals(code)) {
            HttpMethods.getInstance().payOrder(new ProgressSubscriber<OrderInfoModel>(new SubscriberOnNextListener<HttpResult<OrderInfoModel>>() {
                @Override
                public void onNext(HttpResult<OrderInfoModel> httpResult) {
                    infoModel = httpResult.data;
                    if ("1".equals(httpResult.code)) {
                        pay(httpResult.data.orderInfo);
                    } else if ("2".equals(httpResult.code)) {
                        Intent intent = new Intent(OrderPayActivity.this, PaySuccessActivity.class);
                        intent.putExtra("OrderInfoModel", infoModel);
                        startActivity(intent);
                        OrderPayActivity.this.finish();
                    } else {
                        T.showShort(OrderPayActivity.this, httpResult.message);
                    }
                }
            }, this), infoModel.orderId, useBalance, "alipay");
        }
    }

    private void pay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderPayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FinalValues.SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Intent intent = new Intent(OrderPayActivity.this, PaySuccessActivity.class);
                        intent.putExtra("OrderInfoModel", infoModel);
                        startActivity(intent);
                        OrderPayActivity.this.finish();
                    } else {
                        T.showShort(OrderPayActivity.this, "支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
