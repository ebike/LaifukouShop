package com.sdjy.sdjymall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.GoodsInfoActivity;
import com.sdjy.sdjymall.adapter.GoodsAdapter;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.fragment.base.BaseListFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.GoodsModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.view.PullListFragmentHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 推荐
 */
public class RecommendedFragment extends BaseListFragment {

    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private PullListFragmentHandler handler;
    private GoodsAdapter adapter;
    private List<GoodsModel> goodsList;
    private SubscriberNextErrorListener nextErrorListener;
    private Map<String, String> params = new HashMap<>();

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_recommended);

        handler = new PullListFragmentHandler(this, listView);
        listView.setPullLoadEnabled(false);
        listView.setScrollLoadEnabled(true);
        listView.setDriverLine();
        adapter = new GoodsAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsModel goodsModel = (GoodsModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), GoodsInfoActivity.class);
                intent.putExtra("GoodsId", goodsModel.id);
                startActivity(intent);
            }
        });

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

        listView.doPullRefreshing(true, DELAY_MILLIS);
    }

    @Override
    public void requestDatas() {
        if(StaticValues.userModel != null){
            params.put("userId",StaticValues.userModel.userId);
        }
        params.put("pageSorts","13");
        HttpMethods.getInstance().searchGoods(new NoProgressSubscriber<CommonListModel<List<GoodsModel>>>(nextErrorListener, getActivity()), params, mPage);
    }
}
