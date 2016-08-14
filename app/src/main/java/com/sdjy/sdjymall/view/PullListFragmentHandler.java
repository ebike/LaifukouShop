package com.sdjy.sdjymall.view;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.fragment.base.BaseListFragment;
import com.sdjy.sdjymall.view.pullrefresh.EmptyViewForList;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshBase;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

/**
 * 封装加载的列表数据的handler
 * 适用于Fragment
 */
public class PullListFragmentHandler extends Handler {
    private static final int PULL_DOWN_TO_REFRESH = 0;
    private static final int PULL_UP_TO_REFRESH = 1;
    private static final int PULL_TO_REFRESH_COMPLETE = 2;
    private BaseListFragment fragment;
    private PullToRefreshListView pullToRefreshListView;
    private EmptyViewForList emptyView;

    public PullListFragmentHandler(BaseListFragment fragment, PullToRefreshListView pullToRefreshListView) {
        this.fragment = fragment;
        this.pullToRefreshListView = pullToRefreshListView;
        init();
    }

    private void init() {
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
        emptyView = new EmptyViewForList(fragment.getActivity());
        emptyView.setContent("无数据");
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) pullToRefreshListView.getRefreshableView().getParent()).addView(emptyView);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PULL_DOWN_TO_REFRESH:
                fragment.mPage = 1;
                fragment.requestDatas();
                break;
            case PULL_UP_TO_REFRESH:
                if (fragment.mIsMore) {
                    fragment.mPage++;
                    fragment.requestDatas();
                } else {
                    pullToRefreshListView.doComplete(false);
                    T.showShort(fragment.getActivity(), "暂无更多数据");
                    pullToRefreshListView.setHasMoreData(false);
                }
                break;
            case PULL_TO_REFRESH_COMPLETE:
                pullToRefreshListView.setHasMoreData(fragment.mIsMore);
                if (fragment.mPage == 1) {//pull down refresh
                    pullToRefreshListView.doComplete(true);
                } else if (fragment.mPage > 1) {//pull up load more
                    pullToRefreshListView.doComplete(false);
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

    public void setEmptyViewContent1(String content) {
        emptyView.setContent1(content);
    }
}