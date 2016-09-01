package com.sdjy.sdjymall.activity;

import android.view.View;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseListActivity;
import com.sdjy.sdjymall.adapter.MyTeamAdapter;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.TeamModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.view.PullListActivityHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MyTeamActivity extends BaseListActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private PullListActivityHandler handler;
    private MyTeamAdapter adapter;
    private List<TeamModel> teamModelList;
    private SubscriberNextErrorListener listener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_refresh_list);
    }

    @Override
    public void init() {
        titleView.setText("我所在团队");

        listener = new SubscriberNextErrorListener<CommonListModel<List<TeamModel>>>() {
            @Override
            public void onNext(CommonListModel<List<TeamModel>> listCommonListModel) {
                if (listCommonListModel != null) {
                    if (mPage == 1) {
                        teamModelList = listCommonListModel.dataList;
                    } else {
                        teamModelList.addAll(listCommonListModel.dataList);
                    }
                    mIsMore = listCommonListModel.totalPage - listCommonListModel.page > 0 ? true : false;
                } else {
                    mIsMore = false;
                }

                if (teamModelList == null || teamModelList.size() == 0) {
                    teamModelList = new ArrayList<>();
                    handler.setEmptyViewVisible(View.VISIBLE);
                } else {
                    handler.setEmptyViewVisible(View.GONE);
                }
                adapter.setList(teamModelList);
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
        listView.setScrollLoadEnabled(true);
        listView.setDriverLine(R.color.main_line, 1);
        adapter = new MyTeamAdapter(this);
        listView.setAdapter(adapter);
        listView.doPullRefreshing(true, DELAY_MILLIS);
    }

    @Override
    public void requestDatas() {
        HttpMethods.getInstance().findUserTeams(new NoProgressSubscriber<CommonListModel<List<TeamModel>>>(listener, this), mPage);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}
