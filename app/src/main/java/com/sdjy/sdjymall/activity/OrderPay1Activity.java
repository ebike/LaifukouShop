package com.sdjy.sdjymall.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.constants.FinalValues;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.OrderInfoModel;
import com.sdjy.sdjymall.model.PayResult;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class OrderPay1Activity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.tv_amount)
    TextView amountView;
    @Bind(R.id.tv_real_amount)
    TextView realAmountView;

    private OrderInfoModel infoModel;
    private int type;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_order_pay1);
    }

    @Override
    public void init() {
        infoModel = (OrderInfoModel) getIntent().getSerializableExtra("OrderInfoModel");
        type = getIntent().getIntExtra("type", -1);

        titleView.setText("支付订单");

        amountView.setText("￥" + infoModel.money);
        realAmountView.setText("￥" + infoModel.money);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        if (infoModel != null && type != -1) {
            pay(infoModel.orderInfo);
        }
    }

    private void pay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderPay1Activity.this);
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
                        switch (type) {
                            case 1://充值
                                HttpMethods.getInstance().appNotifyRecharge(new ProgressSubscriber(new SubscriberOnNextListener() {
                                    @Override
                                    public void onNext(Object o) {
                                        T.showShort(OrderPay1Activity.this, "充值成功");
                                        OrderPay1Activity.this.finish();
                                    }
                                }, OrderPay1Activity.this), infoModel.orderId);
                                break;
                            case 2://创建团队
                                HttpMethods.getInstance().appNotifyCreateTeam(new ProgressSubscriber(new SubscriberOnNextListener() {
                                    @Override
                                    public void onNext(Object o) {
                                        T.showShort(OrderPay1Activity.this, "成功创建团队");
                                        EventBus.getDefault().post(new RefreshEvent(TeamGoodsInfoActivity.class.getSimpleName()));
                                        startActivity(new Intent(OrderPay1Activity.this, MyTeamActivity.class));
                                        OrderPay1Activity.this.finish();
                                    }
                                }, OrderPay1Activity.this), infoModel.orderId);
                                break;
                            case 3://加入团队
                                HttpMethods.getInstance().appNotifyJoinTeam(new ProgressSubscriber(new SubscriberOnNextListener() {
                                    @Override
                                    public void onNext(Object o) {
                                        T.showShort(OrderPay1Activity.this, "成功加入团队");
                                        EventBus.getDefault().post(new RefreshEvent(TeamGoodsInfoActivity.class.getSimpleName()));
                                        startActivity(new Intent(OrderPay1Activity.this, MyTeamActivity.class));
                                        OrderPay1Activity.this.finish();
                                    }
                                }, OrderPay1Activity.this), infoModel.orderId);
                                break;
                        }
                    } else {
                        T.showShort(OrderPay1Activity.this, "支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
