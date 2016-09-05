package com.sdjy.sdjymall.activity;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.adapter.OrderGoodsAdapter;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.OrderInfoModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.GoodsUtils;
import com.sdjy.sdjymall.util.StringUtils;
import com.sdjy.sdjymall.view.ScrollListView;

import butterknife.Bind;
import butterknife.OnClick;

public class OrderInfoActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.tv_order_id)
    TextView orderIdView;
    @Bind(R.id.tv_state)
    TextView stateView;
    @Bind(R.id.tv_express)
    TextView expressView;
    @Bind(R.id.tv_express_num)
    TextView expressNumView;
    @Bind(R.id.tv_consignee)
    TextView consigneeView;
    @Bind(R.id.tv_phone)
    TextView phoneView;
    @Bind(R.id.tv_address)
    TextView addressView;
    @Bind(R.id.tv_shop_name)
    TextView shopNameView;
    @Bind(R.id.list_view)
    ScrollListView listView;
    @Bind(R.id.tv_amount)
    TextView amountView;
    @Bind(R.id.tv_post_price)
    TextView postPriceView;
    @Bind(R.id.tv_total)
    TextView totalView;
    @Bind(R.id.tv_time)
    TextView timeView;
    @Bind(R.id.ll_bottom)
    LinearLayout bottomLayout;
    @Bind(R.id.tv_cancel)
    TextView cancelView;
    @Bind(R.id.tv_pay)
    TextView payView;
    @Bind(R.id.tv_submit)
    TextView submitView;
    @Bind(R.id.tv_delete)
    TextView deleteView;

    private String orderId;
    private OrderInfoModel orderInfoModel;
    private OrderGoodsAdapter adapter;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_order_info);
    }

    @Override
    public void init() {
        orderId = getIntent().getStringExtra("orderId");
        titleView.setText("订单详情");

        HttpMethods.getInstance().findOrder(new ProgressSubscriber<OrderInfoModel>(new SubscriberOnNextListener<OrderInfoModel>() {
            @Override
            public void onNext(OrderInfoModel infoModel) {
                orderInfoModel = infoModel;
                if (orderInfoModel != null) {
                    orderIdView.setText("订单号：" + orderInfoModel.orderId);
                    switch (orderInfoModel.state) {
                        case 1:
                            stateView.setText("待付款");
                            bottomLayout.setVisibility(View.VISIBLE);
                            cancelView.setVisibility(View.VISIBLE);
                            payView.setVisibility(View.VISIBLE);
                            submitView.setVisibility(View.GONE);
                            deleteView.setVisibility(View.GONE);
                            break;
                        case 4:
                            stateView.setText("待收货");
                            bottomLayout.setVisibility(View.GONE);
                            cancelView.setVisibility(View.GONE);
                            payView.setVisibility(View.GONE);
                            submitView.setVisibility(View.VISIBLE);
                            deleteView.setVisibility(View.GONE);
                            break;
                        case 5:
                            stateView.setText("已完成");
                            bottomLayout.setVisibility(View.GONE);
                            cancelView.setVisibility(View.GONE);
                            payView.setVisibility(View.GONE);
                            submitView.setVisibility(View.GONE);
                            deleteView.setVisibility(View.VISIBLE);
                            break;
                        default:
                            bottomLayout.setVisibility(View.GONE);
                            break;
                    }
                    expressView.setText(orderInfoModel.express);
                    expressNumView.setText(orderInfoModel.expressNum);
                    consigneeView.setText(orderInfoModel.consignee);
                    phoneView.setText(orderInfoModel.phone);
                    addressView.setText(orderInfoModel.address);
                    shopNameView.setText(orderInfoModel.shopName);
                    if (orderInfoModel.shopType == 1) {
                        Drawable drawable = getResources().getDrawable(R.mipmap.icon_order_jy);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        shopNameView.setCompoundDrawables(drawable, null, null, null);
                    } else {
                        Drawable drawable = getResources().getDrawable(R.mipmap.icon_order_shop);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        shopNameView.setCompoundDrawables(drawable, null, null, null);
                    }
                    adapter = new OrderGoodsAdapter(OrderInfoActivity.this);
                    listView.setAdapter(adapter);
                    adapter.setList(orderInfoModel.orderItems);
                    amountView.setText(GoodsUtils.formatPrice(orderInfoModel.money, orderInfoModel.goldCoin, orderInfoModel.coin));
                    postPriceView.setText(orderInfoModel.postMoney);
                    double total = 0;
                    if (!StringUtils.strIsEmpty(orderInfoModel.money)) {
                        total += Double.valueOf(orderInfoModel.money);
                    }
                    if (!StringUtils.strIsEmpty(orderInfoModel.postMoney)) {
                        total += Double.valueOf(orderInfoModel.postMoney);
                    }
                    totalView.setText(GoodsUtils.formatPrice(total + "", orderInfoModel.goldCoin, orderInfoModel.coin));
                    timeView.setText(orderInfoModel.orderTime);
                }
            }
        }, this), orderId);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_cancel)
    public void cancel() {

    }

    @OnClick(R.id.tv_pay)
    public void pay() {

    }

    @OnClick(R.id.tv_submit)
    public void submit() {

    }

    @OnClick(R.id.tv_delete)
    public void delete() {

    }
}
