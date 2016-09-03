package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseListActivity;
import com.sdjy.sdjymall.adapter.GoodsAdapter;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.GoodsModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.view.PullListActivityHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ShopHotGroupGoodsActivity extends BaseListActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private PullListActivityHandler handler;
    private GoodsAdapter adapter;
    private List<GoodsModel> goodsList;
    private String shopId;
    private String groupId;
    private String groupName;
    private SubscriberNextErrorListener nextErrorListener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_shop_hot_group_goods);
    }

    @Override
    public void init() {
        shopId = getIntent().getStringExtra("shopId");
        groupId = getIntent().getStringExtra("groupId");
        groupName = getIntent().getStringExtra("groupName");

        titleView.setText(groupName);

        nextErrorListener = new SubscriberNextErrorListener<CommonListModel<List<GoodsModel>>>() {
            @Override
            public void onNext(CommonListModel<List<GoodsModel>> listCommonListModel) {
                if (listCommonListModel != null) {
                    if (mPage == 1) {
                        goodsList = listCommonListModel.dataList;
                    } else {
                        goodsList.addAll(listCommonListModel.dataList);
                    }
                    mIsMore = listCommonListModel.totalPage - listCommonListModel.page > 0 ? true : false;
                } else {
                    mIsMore = false;
                }

                if (goodsList == null || goodsList.size() == 0) {
                    goodsList = new ArrayList<>();
                    handler.setEmptyViewVisible(View.VISIBLE);
                } else {
                    handler.setEmptyViewVisible(View.GONE);
                }
                adapter.setList(goodsList);
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
        adapter = new GoodsAdapter(this);
        listView.setAdapter(adapter);
        listView.doPullRefreshing(true, DELAY_MILLIS);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsModel goodsModel = (GoodsModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(ShopHotGroupGoodsActivity.this, GoodsInfoActivity.class);
                intent.putExtra("GoodsId", goodsModel.id);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    public void requestDatas() {
        HttpMethods.getInstance().findGoodsByGroup(new NoProgressSubscriber<CommonListModel<List<GoodsModel>>>(nextErrorListener, this), shopId, groupId, mPage);
    }
}
