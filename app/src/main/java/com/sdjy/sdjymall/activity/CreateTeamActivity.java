package com.sdjy.sdjymall.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseListActivity;
import com.sdjy.sdjymall.adapter.TeamGoodsAdapter;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.TeamGoodsModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.view.PullListActivityHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class CreateTeamActivity extends BaseListActivity {

    @Bind(R.id.tv_level)
    TextView levelView;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private Map<String, String> params = new HashMap<>();
    private SubscriberNextErrorListener nextErrorListener;
    private PullListActivityHandler handler;
    private List<TeamGoodsModel> teamGoodsList;
    private TeamGoodsAdapter adapter;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_create_team);
    }

    @Override
    public void init() {
        nextErrorListener = new SubscriberNextErrorListener<CommonListModel<List<TeamGoodsModel>>>() {
            @Override
            public void onNext(CommonListModel<List<TeamGoodsModel>> listCommonListModel) {
                if (listCommonListModel != null) {
                    if (mPage == 1) {
                        teamGoodsList = listCommonListModel.dataList;
                    } else {
                        teamGoodsList.addAll(listCommonListModel.dataList);
                    }
                    mIsMore = listCommonListModel.totalPage - listCommonListModel.page > 0 ? true : false;
                } else {
                    mIsMore = false;
                }

                if (teamGoodsList == null || teamGoodsList.size() == 0) {
                    teamGoodsList.clear();
                    handler.setEmptyViewVisible(View.VISIBLE);
                } else {
                    handler.setEmptyViewVisible(View.GONE);
                }
                adapter.setList(teamGoodsList);
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
        listView.setDriverLine();
        adapter = new TeamGoodsAdapter(this);
        listView.setAdapter(adapter);
        listView.doPullRefreshing(true, DELAY_MILLIS);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_level)
    public void level() {
        levelView.setTextColor(getResources().getColor(R.color.red3));

    }

    @Override
    public void requestDatas() {
        HttpMethods.getInstance().teamGoods(new NoProgressSubscriber<CommonListModel<List<TeamGoodsModel>>>(nextErrorListener, this), mPage);
    }
}
