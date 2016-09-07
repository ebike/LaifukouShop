package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.DensityUtils;
import com.sdjy.sdjymall.common.util.DialogUtils;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.AddressModel;
import com.sdjy.sdjymall.model.GoodsSampleItemModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.model.OrderConfirmModel;
import com.sdjy.sdjymall.model.OrderInfoModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class OrderConfirmActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.ll_pics)
    LinearLayout picsLayout;
    @Bind(R.id.tv_good_count)
    TextView goodCountView;
    @Bind(R.id.tv_amount)
    TextView amountView;
    @Bind(R.id.tv_gold_coin)
    TextView goldCoinView;
    @Bind(R.id.tv_coin)
    TextView coinView;
    @Bind(R.id.tv_post_price)
    TextView postPriceView;
    @Bind(R.id.tv_real_amount)
    TextView realAmountView;
    @Bind(R.id.tv_consignee)
    TextView consigneeView;
    @Bind(R.id.tv_phone)
    TextView phoneView;
    @Bind(R.id.tv_address)
    TextView addressView;
    @Bind(R.id.tv_is_default)
    TextView isDefaultView;

    private AddressModel addressModel;
    private String ids;
    private OrderConfirmModel orderConfirmModel;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_order_confirm);
        EventBus.getDefault().register(this);
    }

    @Override
    public void init() {
        ids = getIntent().getStringExtra("ids");

        titleView.setText("确认订单");

        HttpMethods.getInstance().confirmOrder(new ProgressSubscriber<OrderConfirmModel>(new SubscriberOnNextListener<OrderConfirmModel>() {
            @Override
            public void onNext(OrderConfirmModel model) {
                orderConfirmModel = model;
                initViews();
            }
        }, this), ids);
    }

    private void initViews() {
        if (orderConfirmModel != null) {
            addressModel = orderConfirmModel.address;
            if (addressModel == null) {
                DialogUtils.showDialog(this, "您还没有收货地址哦，赶快去设置一个吧！", "去设置", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(OrderConfirmActivity.this, AddReceiveAddressActivity.class));
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OrderConfirmActivity.this.finish();
                    }
                });
                return;
            }
            consigneeView.setText(addressModel.consignee);
            phoneView.setText(addressModel.mobile);
            addressView.setText(addressModel.address);
            if (addressModel.isDefault == 1) {
                isDefaultView.setVisibility(View.VISIBLE);
            } else {
                isDefaultView.setVisibility(View.GONE);
            }

            for (GoodsSampleItemModel model : orderConfirmModel.items) {
                ImageView imageView = new ImageView(this);
                Glide.with(this)
                        .load(model.pic)
                        .error(R.mipmap.img_goods_default)
                        .into(imageView);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dp2px(this, 70), DensityUtils.dp2px(this, 70));
                params.setMargins(0, 0, DensityUtils.dp2px(this, 12), 0);
                picsLayout.addView(imageView, params);
            }
            goodCountView.setText("共" + orderConfirmModel.totalGoods + "件");
            amountView.setText("￥" + orderConfirmModel.money);
            goldCoinView.setText(orderConfirmModel.goldCoin);
            coinView.setText(orderConfirmModel.coin);
            postPriceView.setText(orderConfirmModel.postMoney);
            double total = 0;
            if (!StringUtils.strIsEmpty(orderConfirmModel.money)) {
                total += Double.valueOf(orderConfirmModel.money);
            }
            if (!StringUtils.strIsEmpty(orderConfirmModel.postMoney)) {
                total += Double.valueOf(orderConfirmModel.postMoney);
            }
            realAmountView.setText("实付款：￥" + total);
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.rl_address)
    public void address() {
        Intent intent = new Intent(this, ReceiveAddressActivity.class);
        intent.putExtra("isChoose", true);
        startActivity(intent);
    }

    @OnClick(R.id.tv_submit)
    public void submit() {
        if (addressModel == null) {
            T.showShort(this, "请选择收货地址");
            return;
        }
        HttpMethods.getInstance().submitOrder(new ProgressSubscriber<OrderInfoModel>(new SubscriberOnNextListener<HttpResult<OrderInfoModel>>() {
            @Override
            public void onNext(HttpResult<OrderInfoModel> httpResult) {
                Intent intent = new Intent();
                intent.putExtra("OrderInfoModel", httpResult.data);
                if ("1".equals(httpResult.code) || "2".equals(httpResult.code)) {
                    intent.setClass(OrderConfirmActivity.this, OrderPayActivity.class);
                    intent.putExtra("code", httpResult.code);
                    startActivity(intent);
                } else if ("3".equals(httpResult.code)) {
                    intent.setClass(OrderConfirmActivity.this, PaySuccessActivity.class);
                    startActivity(intent);
                } else {
                    T.showShort(OrderConfirmActivity.this, httpResult.message);
                }
            }
        }, this), ids, addressModel.id);
    }


    public void onEvent(AddressModel model) {
        addressModel = model;
    }

    public void onEvent(RefreshEvent event) {
        if (event.simpleName.equals(this.getClass().getSimpleName())) {
            HttpMethods.getInstance().confirmOrder(new ProgressSubscriber<OrderConfirmModel>(new SubscriberOnNextListener<OrderConfirmModel>() {
                @Override
                public void onNext(OrderConfirmModel model) {
                    orderConfirmModel = model;
                    initViews();
                }
            }, this), ids);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
