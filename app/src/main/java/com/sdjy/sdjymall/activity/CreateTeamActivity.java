package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class CreateTeamActivity extends BaseListActivity {

    @Bind(R.id.tv_amount)
    TextView amountView;
    @Bind(R.id.tv_join_num)
    TextView joinNumView;
    @Bind(R.id.iv_up_arrow)
    ImageView upArrowView;
    @Bind(R.id.iv_down_arrow)
    ImageView downArrowView;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private Map<String, String> params = new HashMap<>();
    private SubscriberNextErrorListener nextErrorListener;
    private PullListActivityHandler handler;
    private List<TeamGoodsModel> teamGoodsList;
    private TeamGoodsAdapter adapter;
    private String sortTerm = "1";
    private String sortOrder = "2";

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
                    teamGoodsList = new ArrayList<>();
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
                TeamGoodsModel model = (TeamGoodsModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(CreateTeamActivity.this, TeamGoodsInfoActivity.class);
                intent.putExtra("id", model.id);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_amount)
    public void amount() {
        amountView.setTextColor(getResources().getColor(R.color.red3));
        joinNumView.setTextColor(getResources().getColor(R.color.text_gray));
        sortTerm = "2";
        sortOrder = "2";
        upArrowView.setImageResource(R.mipmap.icon_up_arrow_gray);
        downArrowView.setImageResource(R.mipmap.icon_down_arrow_gray);
        listView.doPullRefreshing(true, DELAY_MILLIS);
    }

    @OnClick(R.id.ll_join_num)
    public void joinNum() {
        amountView.setTextColor(getResources().getColor(R.color.text_gray));
        joinNumView.setTextColor(getResources().getColor(R.color.red3));
        sortTerm = "3";
        if ("2".equals(sortOrder)) {
            sortOrder = "1";
            upArrowView.setImageResource(R.mipmap.icon_up_arrow_red);
            downArrowView.setImageResource(R.mipmap.icon_down_arrow_gray);
        } else {
            sortOrder = "2";
            upArrowView.setImageResource(R.mipmap.icon_up_arrow_gray);
            downArrowView.setImageResource(R.mipmap.icon_down_arrow_red);
        }
        listView.doPullRefreshing(true, DELAY_MILLIS);
    }

    @Override
    public void requestDatas() {
        params.put("sortTerm", sortTerm);
        params.put("sortOrder", sortOrder);
        HttpMethods.getInstance().teamGoods(new NoProgressSubscriber<CommonListModel<List<TeamGoodsModel>>>(nextErrorListener, this), mPage, params);
    }
}
