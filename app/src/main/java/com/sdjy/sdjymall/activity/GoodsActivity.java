package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseListActivity;
import com.sdjy.sdjymall.adapter.GoodsAdapter;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.GoodsModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.util.StringUtils;
import com.sdjy.sdjymall.view.PullListActivityHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
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
    private String key;
    private String userId;
    private String shopId;
    private String goodsName;
    private String sortId;
    private String priceOrder = "1";
    private Map<String, String> params = new HashMap<>();
    private SubscriberNextErrorListener nextErrorListener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_goods);
    }

    @Override
    public void init() {
        key = getIntent().getStringExtra("key");
        pageSorts = getIntent().getStringExtra("pageSorts");
        sortId = getIntent().getStringExtra("sortId");

        if (StaticValues.userModel != null) {
            params.put("userId", StaticValues.userModel.userId);
        }
        if (!StringUtils.strIsEmpty(key)) {
            params.put("key", key);
        }
        if (!StringUtils.strIsEmpty(pageSorts)) {
            params.put("pageSorts", pageSorts);
        }
        if (!StringUtils.strIsEmpty(sortId)) {
            params.put("sortId", sortId);
        }

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
                Intent intent = new Intent(GoodsActivity.this, GoodsInfoActivity.class);
                intent.putExtra("GoodsId", goodsModel.id);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_sales)
    public void sales() {
        salesView.setTextColor(getResources().getColor(R.color.red3));
        priceView.setTextColor(getResources().getColor(R.color.text_gray));
        params.put("sortTerm", "2");
        params.put("sortOrder", "2");
        listView.doPullRefreshing(true, DELAY_MILLIS);
        priceOrder = "1";
        upArrowView.setImageResource(R.mipmap.icon_up_arrow_gray);
        downArrowView.setImageResource(R.mipmap.icon_down_arrow_gray);
    }

    @OnClick(R.id.ll_price)
    public void price() {
        salesView.setTextColor(getResources().getColor(R.color.text_gray));
        priceView.setTextColor(getResources().getColor(R.color.red3));
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

    @OnClick(R.id.tv_search)
    public void search() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void requestDatas() {
        HttpMethods.getInstance().searchGoods(new NoProgressSubscriber<CommonListModel<List<GoodsModel>>>(nextErrorListener, this), params, mPage);
    }
}
