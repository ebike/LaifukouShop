package com.sdjy.sdjymall.activity;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseListActivity;
import com.sdjy.sdjymall.adapter.OrderAdapter;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.OrderModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.util.CommonUtils;
import com.sdjy.sdjymall.view.PullListActivityHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class OrderActivity extends BaseListActivity {

    @Bind(R.id.rl_top)
    RelativeLayout topLayout;
    @Bind(R.id.tv_state)
    TextView stateView;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private PullListActivityHandler handler;
    private OrderAdapter adapter;
    private List<OrderModel> orderList;
    private PopupWindow popupWindow;
    private View popupView;
    private String state;
    private SubscriberNextErrorListener<CommonListModel<List<OrderModel>>> listener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_order);
    }

    @Override
    public void init() {
        state = getIntent().getStringExtra("state");

        listener = new SubscriberNextErrorListener<CommonListModel<List<OrderModel>>>() {
            @Override
            public void onNext(CommonListModel<List<OrderModel>> listCommonListModel) {
                if (listCommonListModel != null) {
                    if (mPage == 1) {
                        orderList = listCommonListModel.dataList;
                    } else {
                        orderList.addAll(listCommonListModel.dataList);
                    }
                    mIsMore = listCommonListModel.totalPage - listCommonListModel.page > 0 ? true : false;
                } else {
                    mIsMore = false;
                }

                if (orderList == null || orderList.size() == 0) {
                    orderList = new ArrayList<>();
                    handler.setEmptyViewVisible(View.VISIBLE);
                } else {
                    handler.setEmptyViewVisible(View.GONE);
                }
                adapter.setList(orderList);
                handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
            }

            @Override
            public void onError() {
                handler.setEmptyViewVisible(View.VISIBLE);
                handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
            }
        };

        handler = new PullListActivityHandler(this, listView);
        handler.setEmptyViewContent("您还没有相关订单");
        listView.setPullLoadEnabled(false);
        listView.setScrollLoadEnabled(true);
        listView.setDriverLine();
        adapter = new OrderAdapter(this);
        listView.setAdapter(adapter);
        listView.doPullRefreshing(true, DELAY_MILLIS);

        initPop();
    }

    private void initPop() {
        popupView = LayoutInflater.from(this).inflate(R.layout.view_order_state, null);
        final TextView allView = (TextView) popupView.findViewById(R.id.tv_all);
        final TextView paymentView = (TextView) popupView.findViewById(R.id.tv_payment);
        final TextView deliveryView = (TextView) popupView.findViewById(R.id.tv_delivery);
        final TextView takeDeliveryView = (TextView) popupView.findViewById(R.id.tv_take_delivery);

        final Drawable drawable = getResources().getDrawable(R.mipmap.icon_sel_active);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        allView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allView.setCompoundDrawables(null, null, drawable, null);
                paymentView.setCompoundDrawables(null, null, null, null);
                deliveryView.setCompoundDrawables(null, null, null, null);
                takeDeliveryView.setCompoundDrawables(null, null, null, null);
            }
        });
        paymentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allView.setCompoundDrawables(null, null, null, null);
                paymentView.setCompoundDrawables(null, null, drawable, null);
                deliveryView.setCompoundDrawables(null, null, null, null);
                takeDeliveryView.setCompoundDrawables(null, null, null, null);
            }
        });
        deliveryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allView.setCompoundDrawables(null, null, null, null);
                paymentView.setCompoundDrawables(null, null, null, null);
                deliveryView.setCompoundDrawables(null, null, drawable, null);
                takeDeliveryView.setCompoundDrawables(null, null, null, null);
            }
        });
        takeDeliveryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allView.setCompoundDrawables(null, null, null, null);
                paymentView.setCompoundDrawables(null, null, null, null);
                deliveryView.setCompoundDrawables(null, null, null, null);
                takeDeliveryView.setCompoundDrawables(null, null, drawable, null);
            }
        });
    }

    @Override
    public void requestDatas() {
        if (StaticValues.userModel != null) {
            HttpMethods.getInstance().orderList(new NoProgressSubscriber<CommonListModel<List<OrderModel>>>(listener, this), mPage, state);
        } else {
            handler.setEmptyViewVisible(View.VISIBLE);
            handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_state)
    public void state() {
        if (popupWindow == null) {
            popupWindow = CommonUtils.createPopupWindow(popupView);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Drawable drawable = getResources().getDrawable(R.mipmap.icon_order_arrow_down);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    stateView.setCompoundDrawables(null, null, drawable, null);
                }
            });
        }
        popupWindow.showAsDropDown(topLayout);

        Drawable drawable = getResources().getDrawable(R.mipmap.icon_order_arrow_up);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        stateView.setCompoundDrawables(null, null, drawable, null);
    }
}
