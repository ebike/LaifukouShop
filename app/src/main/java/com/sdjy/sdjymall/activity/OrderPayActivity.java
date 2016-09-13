package com.sdjy.sdjymall.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.constants.FinalValues;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.model.OrderInfoModel;
import com.sdjy.sdjymall.model.PayResult;
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
        goldCoinView.setText(infoModel.goldCoin);
        coinView.setText(infoModel.coin);
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
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_nosel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            accountBalanceView.setCompoundDrawables(drawable, null, null, null);
            lessView.setText("￥" + total);
        }
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        if ("1".equals(code)) {
//            BizContentModel bizContentModel = new BizContentModel();
//            bizContentModel.body = "";
//            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(FinalValues.APPID);
//            String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//            String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
//            final String orderInfo = orderParam + "&" + sign;
            final String orderInfo = "";

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
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(OrderPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(OrderPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
