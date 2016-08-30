package com.sdjy.sdjymall.activity;

import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseListActivity;
import com.sdjy.sdjymall.adapter.BrowsingHistoryAdapter;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.GoodsBrowsingModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.view.PullListActivityHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MyRecommendActivity extends BaseListActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private PullListActivityHandler handler;
    private BrowsingHistoryAdapter adapter;
    private List<GoodsBrowsingModel> browsingModelList;
    private SubscriberOnNextListener listener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_refresh_list);
    }

    @Override
    public void init() {
        titleView.setText("我推荐的人");

    }

    @Override
    public void requestDatas() {
        HttpMethods.getInstance().findRefereeUser(new NoProgressSubscriber<CommonListModel<List<String>>>(listener, this), mPage);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}
