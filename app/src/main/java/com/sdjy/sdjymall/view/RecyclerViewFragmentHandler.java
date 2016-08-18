package com.sdjy.sdjymall.view;

import android.os.Handler;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.fragment.base.BaseListFragment;

/**
 * 封装加载的列表数据的handler
 * 适用于Fragment
 */
public class RecyclerViewFragmentHandler extends Handler {
    private BaseListFragment fragment;
    private IRecyclerView iRecyclerView;

    public RecyclerViewFragmentHandler(BaseListFragment fragment, IRecyclerView iRecyclerView) {
        this.fragment = fragment;
        this.iRecyclerView = iRecyclerView;
        init();
    }

    private void init() {
        iRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                fragment.mPage = 1;
                fragment.requestDatas();
            }
        });
        iRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(View loadMoreView) {
                if (fragment.mIsMore) {
                    fragment.mPage++;
                    fragment.requestDatas();
                } else {
                    iRecyclerView.setLoadMoreEnabled(false);
                    iRecyclerView.getLoadMoreFooterView().setVisibility(View.GONE);
                    T.showShort(fragment.getActivity(), "暂无更多数据");
                }
            }
        });
    }

    public void doComplete() {
        iRecyclerView.setRefreshing(false);
        iRecyclerView.setLoadMoreEnabled(fragment.mIsMore);
        if (fragment.mPage == 1 && fragment.mIsMore) {
            iRecyclerView.getLoadMoreFooterView().setVisibility(View.VISIBLE);
        } else {
            iRecyclerView.getLoadMoreFooterView().setVisibility(View.GONE);
        }
    }
}
