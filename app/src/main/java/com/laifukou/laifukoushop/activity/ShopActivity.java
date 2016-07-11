package com.laifukou.laifukoushop.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.laifukou.laifukoushop.R;
import com.laifukou.laifukoushop.activity.base.BaseListActivity;
import com.laifukou.laifukoushop.adapter.ShopAdapter;
import com.laifukou.laifukoushop.common.util.DensityUtils;
import com.laifukou.laifukoushop.http.HttpMethods;
import com.laifukou.laifukoushop.model.CommonListModel;
import com.laifukou.laifukoushop.model.ShopModel;
import com.laifukou.laifukoushop.subscribers.NoProgressSubscriber;
import com.laifukou.laifukoushop.subscribers.SubscriberNextErrorListener;
import com.laifukou.laifukoushop.view.PullListActivityHandler;
import com.laifukou.laifukoushop.view.pullrefresh.PullToRefreshGridView;

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
        gridView.setPullLoadEnabled(false);
        gridView.setScrollLoadEnabled(true);
        gridView.getRefreshableView().setNumColumns(2);
        adapter = new ShopAdapter(this);
        gridView.getRefreshableView().setAdapter(adapter);
        gridView.getRefreshableView().setHorizontalSpacing(DensityUtils.dp2px(this, 4));
        gridView.getRefreshableView().setVerticalSpacing(DensityUtils.dp2px(this, 4));
        gridView.doPullRefreshing(true, DELAY_MILLIS);

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
                    shopList.clear();
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
