package com.sdjy.sdjymall.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.adapter.RechargeAdapter;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.constants.FinalValues;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.OrderInfoModel;
import com.sdjy.sdjymall.model.PayResult;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.view.ScrollGridView;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity {

    @Bind(R.id.grid_view)
    ScrollGridView gridView;
    @Bind(R.id.tv_amount)
    TextView amountView;

    private RechargeAdapter adapter;
    private Integer amount;
    private OrderInfoModel infoModel;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_recharge);
    }

    @Override
    public void init() {
        adapter = new RechargeAdapter(this);
        gridView.setAdapter(adapter);
        SubscriberOnNextListener listener = new SubscriberOnNextListener<List<Integer>>() {
            @Override
            public void onNext(List<Integer> list) {
                adapter.setList(list);
                amount = list.get(0);
                amountView.setText("合计：￥" + amount);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        amount = (Integer) parent.getItemAtPosition(position);
                        amountView.setText("合计：￥" + amount);
                        adapter.setSelectedPosition(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        HttpMethods.getInstance().findRechargeNums(new ProgressSubscriber<List<Integer>>(listener, this));
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_recharge)
    public void recharge() {
        if (StaticValues.userModel != null) {
            SubscriberOnNextListener listener = new SubscriberOnNextListener<OrderInfoModel>() {
                @Override
                public void onNext(OrderInfoModel model) {
                    infoModel = model;
                    pay(model.orderInfo);
                }
            };
            HttpMethods.getInstance().recharge(new ProgressSubscriber<OrderInfoModel>(listener, this), amount, "alipay");
        } else {
            T.showShort(this, "请您先登录，再充值");
        }
    }

    private void pay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
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
                        HttpMethods.getInstance().appNotifyRecharge(new ProgressSubscriber(new SubscriberOnNextListener() {
                            @Override
                            public void onNext(Object o) {
                                T.showShort(RechargeActivity.this, "充值成功");
                            }
                        }, RechargeActivity.this), infoModel.orderId);
                    } else {
                        T.showShort(RechargeActivity.this, "支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
