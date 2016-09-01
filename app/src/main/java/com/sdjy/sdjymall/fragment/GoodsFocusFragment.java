package com.sdjy.sdjymall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.GoodsInfoActivity;
import com.sdjy.sdjymall.adapter.GoodsFocusAdapter;
import com.sdjy.sdjymall.common.util.DialogUtils;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.fragment.base.BaseListFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.GoodsBrowsingModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.view.PullListFragmentHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 商品关注
 */
public class GoodsFocusFragment extends BaseListFragment {

    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private PullListFragmentHandler handler;
    private GoodsFocusAdapter adapter;
    private SubscriberNextErrorListener listener;
    private List<GoodsBrowsingModel> browsingModelList;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_goods_focus);

        listener = new SubscriberNextErrorListener<CommonListModel<List<GoodsBrowsingModel>>>() {
            @Override
            public void onNext(CommonListModel<List<GoodsBrowsingModel>> listCommonListModel) {
                if (listCommonListModel != null) {
                    if (mPage == 1) {
                        browsingModelList = listCommonListModel.dataList;
                    } else {
                        browsingModelList.addAll(listCommonListModel.dataList);
                    }
                    mIsMore = listCommonListModel.totalPage - listCommonListModel.page > 0 ? true : false;
                } else {
                    mIsMore = false;
                }

                if (browsingModelList == null || browsingModelList.size() == 0) {
                    browsingModelList = new ArrayList<>();
                    handler.setEmptyViewVisible(View.VISIBLE);
                } else {
                    handler.setEmptyViewVisible(View.GONE);
                }
                adapter.setList(browsingModelList);
                handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
            }

            @Override
            public void onError() {
                handler.setEmptyViewVisible(View.VISIBLE);
                handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
            }
        };

        handler = new PullListFragmentHandler(this, listView);
        listView.setPullRefreshEnabled(false);
        listView.setPullLoadEnabled(false);
        listView.setScrollLoadEnabled(true);
        listView.setDriverLine();
        adapter = new GoodsFocusAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.doPullRefreshing(true, DELAY_MILLIS);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GoodsBrowsingModel model = (GoodsBrowsingModel) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), GoodsInfoActivity.class);
                intent.putExtra("GoodsId", model.goodsId);
                startActivity(intent);
            }
        });
        adapter.setLongClickListener(new GoodsFocusAdapter.LongClickListener() {
            @Override
            public void onLongClick(final GoodsBrowsingModel model) {
                DialogUtils.showDialog(getActivity(), "取消关注", "确定", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HttpMethods.getInstance().cancelCollect(new ProgressSubscriber(new SubscriberOnNextListener() {
                            @Override
                            public void onNext(Object o) {
                                T.showShort(getActivity(), "取消关注");
                                listView.doPullRefreshing(true, DELAY_MILLIS);
                            }
                        }, getActivity()), StaticValues.userModel.userId, model.oid);
                    }
                });
            }
        });
    }

    @Override
    public void requestDatas() {
        if (StaticValues.userModel != null) {
            HttpMethods.getInstance().collectGoods(new NoProgressSubscriber<CommonListModel<List<GoodsBrowsingModel>>>(listener, getActivity()));
        } else {
            handler.setEmptyViewVisible(View.VISIBLE);
            handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
        }
    }
}
