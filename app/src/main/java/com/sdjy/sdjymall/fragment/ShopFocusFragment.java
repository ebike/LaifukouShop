package com.sdjy.sdjymall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.ShopInfoActivity;
import com.sdjy.sdjymall.adapter.ShopFocusAdapter;
import com.sdjy.sdjymall.common.util.DialogUtils;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.fragment.base.BaseListFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.ShopModel;
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
 * 商家关注
 */
public class ShopFocusFragment extends BaseListFragment {

    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private PullListFragmentHandler handler;
    private ShopFocusAdapter adapter;
    private SubscriberNextErrorListener listener;
    private List<ShopModel> shopList;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_shop_focus);

        listener = new SubscriberNextErrorListener<CommonListModel<List<ShopModel>>>() {
            @Override
            public void onNext(CommonListModel<List<ShopModel>> listCommonListModel) {
                if (listCommonListModel != null) {
                    if (mPage == 1) {
                        shopList = listCommonListModel.dataList;
                    } else {
                        shopList.addAll(listCommonListModel.dataList);
                    }
                    mIsMore = listCommonListModel.totalPage - listCommonListModel.page > 0 ? true : false;
                } else {
                    mIsMore = false;
                }

                if (shopList == null || shopList.size() == 0) {
                    shopList = new ArrayList<>();
                    handler.setEmptyViewVisible(View.VISIBLE);
                } else {
                    handler.setEmptyViewVisible(View.GONE);
                }
                adapter.setList(shopList);
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
        adapter = new ShopFocusAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.doPullRefreshing(true, DELAY_MILLIS);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShopModel model = (ShopModel) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
                intent.putExtra("shopId", model.shopId);
                startActivity(intent);
            }
        });
        adapter.setLongClickListener(new ShopFocusAdapter.LongClickListener() {
            @Override
            public void onLongClick(final ShopModel model) {
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
            HttpMethods.getInstance().collectShops(new NoProgressSubscriber<CommonListModel<List<ShopModel>>>(listener, getActivity()));
        } else {
            handler.setEmptyViewVisible(View.VISIBLE);
            handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
        }
    }
}