package com.sdjy.sdjymall.activity;

import android.view.View;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseListActivity;
import com.sdjy.sdjymall.adapter.OrderNoCommentAdapter;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.GoodsItemModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.view.PullListActivityHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class OrderNoCommentActivity extends BaseListActivity {

    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private OrderNoCommentAdapter adapter;
    private PullListActivityHandler handler;
    private List<GoodsItemModel> itemList;
    private SubscriberNextErrorListener<CommonListModel<List<GoodsItemModel>>> listener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_order_no_comment);
    }

    @Override
    public void init() {
        listener = new SubscriberNextErrorListener<CommonListModel<List<GoodsItemModel>>>() {
            @Override
            public void onNext(CommonListModel<List<GoodsItemModel>> listCommonListModel) {
                if (listCommonListModel != null) {
                    if (mPage == 1) {
                        itemList = listCommonListModel.dataList;
                    } else {
                        itemList.addAll(listCommonListModel.dataList);
                    }
                    mIsMore = listCommonListModel.totalPage - listCommonListModel.page > 0 ? true : false;
                } else {
                    mIsMore = false;
                }

                if (itemList == null || itemList.size() == 0) {
                    itemList = new ArrayList<>();
                    handler.setEmptyViewVisible(View.VISIBLE);
                } else {
                    handler.setEmptyViewVisible(View.GONE);
                }
                adapter.setList(itemList);
                handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
            }

            @Override
            public void onError() {
                handler.setEmptyViewVisible(View.VISIBLE);
                handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
            }
        };

        handler = new PullListActivityHandler(this, listView);
        handler.setEmptyViewContent("您还没有相关订单");
        listView.setPullLoadEnabled(false);
        listView.setScrollLoadEnabled(true);
        listView.setDriverLine();
        adapter = new OrderNoCommentAdapter(this);
        listView.setAdapter(adapter);
        listView.doPullRefreshing(true, DELAY_MILLIS);
    }

    @Override
    public void requestDatas() {
        if (StaticValues.userModel != null) {
            HttpMethods.getInstance().waitCommentOrder(new NoProgressSubscriber<CommonListModel<List<GoodsItemModel>>>(listener, this), mPage);
        } else {
            handler.setEmptyViewVisible(View.VISIBLE);
            handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}
