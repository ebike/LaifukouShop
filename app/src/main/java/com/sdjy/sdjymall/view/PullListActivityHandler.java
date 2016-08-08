package com.sdjy.sdjymall.view;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.sdjy.sdjymall.activity.base.BaseListActivity;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.view.pullrefresh.EmptyViewForList;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshBase;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshGridView;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

/**
 * 封装加载的列表数据的handler
 * 适用于Activity
 */
public class PullListActivityHandler extends Handler {
    private static final int PULL_DOWN_TO_REFRESH = 0;
    private static final int PULL_UP_TO_REFRESH = 1;
    private static final int PULL_TO_REFRESH_COMPLETE = 2;
    private BaseListActivity activity;
    private PullToRefreshListView pullToRefreshListView;
    private PullToRefreshGridView gridView;
    private EmptyViewForList emptyView;
    private int type;

    public PullListActivityHandler(BaseListActivity activity, PullToRefreshListView pullToRefreshListView) {
        this.activity = activity;
        this.pullToRefreshListView = pullToRefreshListView;
        type = 0;
        initListView();
    }

    public PullListActivityHandler(BaseListActivity activity, PullToRefreshGridView gridView) {
        this.activity = activity;
        this.gridView = gridView;
        type = 1;
        initGridView();
    }

    private void initListView() {
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView, boolean isAutoRefresh) {
                sendEmptyMessage(PULL_DOWN_TO_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                sendEmptyMessage(PULL_UP_TO_REFRESH);
            }
        });
        emptyView = new EmptyViewForList(activity);
        emptyView.setContent("无数据");
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) pullToRefreshListView.getRefreshableView().getParent()).addView(emptyView);
    }

    private void initGridView() {

        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView, boolean isAutoRefresh) {
                sendEmptyMessage(PULL_DOWN_TO_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                sendEmptyMessage(PULL_UP_TO_REFRESH);
            }
        });
        emptyView = new EmptyViewForList(activity);
        emptyView.setContent("无数据");
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) gridView.getRefreshableView().getParent()).addView(emptyView);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PULL_DOWN_TO_REFRESH:
                activity.mPage = 1;
                activity.requestDatas();
                break;
            case PULL_UP_TO_REFRESH:
                if (activity.mIsMore) {
                    activity.mPage++;
                    activity.requestDatas();
                } else {
                    if (type == 1) {
                        gridView.doComplete(false);
                        gridView.setHasMoreData(false);
                    } else {
                        pullToRefreshListView.doComplete(false);
                        pullToRefreshListView.setHasMoreData(false);
                    }
                    T.showShort(activity, "暂无更多数据");
                }
                break;
            case PULL_TO_REFRESH_COMPLETE:
                if (type == 1) {
                    gridView.setHasMoreData(activity.mIsMore);
                    if (activity.mPage == 1) {
                        gridView.doComplete(true);
                    } else if (activity.mPage > 1) {
                        gridView.doComplete(false);
                    }
                } else {
                    pullToRefreshListView.setHasMoreData(activity.mIsMore);
                    if (activity.mPage == 1) {
                        pullToRefreshListView.doComplete(true);
                    } else if (activity.mPage > 1) {
                        pullToRefreshListView.doComplete(false);
                    }
                }
                break;
        }
    }

    public void setEmptyViewVisible(int visible) {
        emptyView.setVisibility(visible);
    }

    public void setEmptyViewContent(String content) {
        emptyView.setContent(content);
    }
}