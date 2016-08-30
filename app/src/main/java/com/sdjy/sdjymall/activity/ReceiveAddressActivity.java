package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseListActivity;
import com.sdjy.sdjymall.adapter.ReceiveAddressAdapter;
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

public class ReceiveAddressActivity extends BaseListActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private PullListActivityHandler handler;
    private ReceiveAddressAdapter adapter;
    private List<AddressModel> addressList;
    private SubscriberNextErrorListener listener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_receive_address);
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
        listView.setDriverLine();
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
}
