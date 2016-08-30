package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseListActivity;
import com.sdjy.sdjymall.adapter.ShopAdapter;
import com.sdjy.sdjymall.common.util.DensityUtils;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.ShopModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.view.PullListActivityHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ShopActivity extends BaseListActivity {

    @Bind(R.id.iv_back)
    ImageView backView;
    @Bind(R.id.tv_search)
    TextView searchView;
    @Bind(R.id.grid_view)
    PullToRefreshGridView gridView;

    private String shopName;
    private ShopAdapter adapter;
    private List<ShopModel> shopList;
    private PullListActivityHandler handler;
    private SubscriberNextErrorListener nextErrorListener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_shop);
    }

    @Override
    public void init() {
        shopName = getIntent().getStringExtra("shopName");

        handler = new PullListActivityHandler(this, gridView);
        gridView.setPullRefreshEnabled(false);
        gridView.setPullLoadEnabled(false);
        gridView.setScrollLoadEnabled(true);
        gridView.getRefreshableView().setNumColumns(2);
        adapter = new ShopAdapter(this);
        gridView.getRefreshableView().setAdapter(adapter);
        gridView.getRefreshableView().setHorizontalSpacing(DensityUtils.dp2px(this, 4));
        gridView.getRefreshableView().setVerticalSpacing(DensityUtils.dp2px(this, 4));
        gridView.doPullRefreshing(true, DELAY_MILLIS);
        adapter.setItemClickListener(new ShopAdapter.onItemClickListener() {
            @Override
            public void onItem(ShopModel model) {
                Intent intent = new Intent(ShopActivity.this, ShopInfoActivity.class);
                intent.putExtra("shopId", model.id);
                startActivity(intent);
            }
        });

        nextErrorListener = new SubscriberNextErrorListener<CommonListModel<List<ShopModel>>>() {
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
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_search)
    public void search() {

    }

    @Override
    public void requestDatas() {
        HttpMethods.getInstance().searchShop(new NoProgressSubscriber<CommonListModel<List<ShopModel>>>(nextErrorListener, this), shopName, mPage);
    }
}
