package com.laifukou.laifukoushop.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.laifukou.laifukoushop.R;
import com.laifukou.laifukoushop.activity.base.BaseListActivity;
import com.laifukou.laifukoushop.adapter.GoodsAdapter;
import com.laifukou.laifukoushop.http.HttpMethods;
import com.laifukou.laifukoushop.model.CommonListModel;
import com.laifukou.laifukoushop.model.GoodsModel;
import com.laifukou.laifukoushop.subscribers.NoProgressSubscriber;
import com.laifukou.laifukoushop.subscribers.SubscriberNextErrorListener;
import com.laifukou.laifukoushop.view.PullListActivityHandler;
import com.laifukou.laifukoushop.view.pullrefresh.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class GoodsActivity extends BaseListActivity {

    @Bind(R.id.list_view)
    PullToRefreshListView listView;
    @Bind(R.id.tv_sales)
    TextView salesView;
    @Bind(R.id.tv_price)
    TextView priceView;
    @Bind(R.id.iv_up_arrow)
    ImageView upArrowView;
    @Bind(R.id.iv_down_arrow)
    ImageView downArrowView;

    private String pageSorts;
    private PullListActivityHandler handler;
    private GoodsAdapter adapter;
    private List<GoodsModel> goodsList;
    private String userId;
    private String shopId;
    private String goodsName;
    private String priceOrder = "1";
    private Map<String, String> params = new HashMap<>();
    private SubscriberNextErrorListener nextErrorListener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_goods);
    }

    @Override
    public void init() {
        pageSorts = getIntent().getStringExtra("pageSorts");

        params.put("pageSorts", pageSorts);

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
                    goodsList.clear();
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

            }
        });
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_search)
    public void search() {

    }

    @OnClick(R.id.tv_sales)
    public void sales() {
        salesView.setTextColor(getResources().getColor(R.color.red));
        priceView.setTextColor(getResources().getColor(R.color.black));
        params.put("sortTerm", "2");
        params.put("sortOrder", "2");
        listView.doPullRefreshing(true, DELAY_MILLIS);
        priceOrder = "1";
        upArrowView.setImageResource(R.mipmap.icon_up_arrow_gray);
        downArrowView.setImageResource(R.mipmap.icon_down_arrow_gray);
    }

    @OnClick(R.id.ll_price)
    public void price() {
        salesView.setTextColor(getResources().getColor(R.color.black));
        priceView.setTextColor(getResources().getColor(R.color.red));
        params.put("sortTerm", "1");
        params.put("sortOrder", priceOrder);
        if (priceOrder.equals("1")) {
            priceOrder = "2";
            upArrowView.setImageResource(R.mipmap.icon_up_arrow_red);
            downArrowView.setImageResource(R.mipmap.icon_down_arrow_gray);
        } else {
            priceOrder = "1";
            upArrowView.setImageResource(R.mipmap.icon_up_arrow_gray);
            downArrowView.setImageResource(R.mipmap.icon_down_arrow_red);
        }
        listView.doPullRefreshing(true, DELAY_MILLIS);
    }

    @Override
    public void requestDatas() {
        HttpMethods.getInstance().searchGoods(new NoProgressSubscriber<CommonListModel<List<GoodsModel>>>(nextErrorListener, this), params, mPage);
    }
}
