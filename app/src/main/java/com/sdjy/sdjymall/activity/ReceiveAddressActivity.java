package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseListActivity;
import com.sdjy.sdjymall.adapter.ReceiveAddressAdapter;
import com.sdjy.sdjymall.common.util.DensityUtils;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.AddressModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.view.PullListActivityHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class ReceiveAddressActivity extends BaseListActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;
    @Bind(R.id.ll_add)
    LinearLayout addLayout;

    private PullListActivityHandler handler;
    private ReceiveAddressAdapter adapter;
    private List<AddressModel> addressList;
    private SubscriberNextErrorListener listener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_receive_address);
        EventBus.getDefault().register(this);
    }

    @Override
    public void init() {
        titleView.setText("收货地址");

        listener = new SubscriberNextErrorListener<List<AddressModel>>() {
            @Override
            public void onNext(List<AddressModel> addressModels) {
                addressList = addressModels;
                if (addressList == null || addressList.size() == 0) {
                    addressList = new ArrayList<>();
                    handler.setEmptyViewVisible(View.VISIBLE);
                } else {
                    handler.setEmptyViewVisible(View.GONE);
                }
                adapter.setList(addressList);
                if (addressList != null && addressList.size() >= 10) {
                    addLayout.setVisibility(View.GONE);
                } else {
                    addLayout.setVisibility(View.VISIBLE);
                }
                handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
            }

            @Override
            public void onError() {
                handler.setEmptyViewVisible(View.VISIBLE);
                handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
            }
        };

        handler = new PullListActivityHandler(this, listView);
        listView.setPullRefreshEnabled(false);
        listView.setPullLoadEnabled(false);
        listView.setScrollLoadEnabled(false);
        listView.setDriverLine(R.color.main_bg, DensityUtils.dp2px(this, 14));
        adapter = new ReceiveAddressAdapter(this);
        listView.setAdapter(adapter);
        listView.doPullRefreshing(true, DELAY_MILLIS);
    }

    @Override
    public void requestDatas() {
        HttpMethods.getInstance().addressList(new NoProgressSubscriber<List<AddressModel>>(listener, this));
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_add)
    public void add() {
        startActivity(new Intent(this, AddReceiveAddressActivity.class));
    }

    public void onEvent(RefreshEvent event) {
        if (event.simpleName.equals(this.getClass().getSimpleName())) {
            listView.doPullRefreshing(true, DELAY_MILLIS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
