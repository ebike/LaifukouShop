package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.DensityUtils;
import com.sdjy.sdjymall.common.util.T;
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
            realAmountView.setText(total + "");
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
                if ("1".equals(httpResult.code)) {

                } else if ("2".equals(httpResult.code)) {

                } else if ("3".equals(httpResult.code)) {

                } else {

                }
            }
        }, this), ids, addressModel.id);
    }


    public void onEvent(AddressModel model) {
        addressModel = model;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
