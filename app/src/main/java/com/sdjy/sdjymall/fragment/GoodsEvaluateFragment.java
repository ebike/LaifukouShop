package com.sdjy.sdjymall.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.adapter.GoodsEvaluateAdapter;
import com.sdjy.sdjymall.fragment.base.BaseListFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.GoodsEvaluateCountModel;
import com.sdjy.sdjymall.model.GoodsEvaluateModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.StringUtils;
import com.sdjy.sdjymall.view.PullListFragmentHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 商品-评价
 */
public class GoodsEvaluateFragment extends BaseListFragment {

    @Bind({R.id.tv_all_label, R.id.tv_good_label, R.id.tv_middle_label, R.id.tv_poor_label})
    TextView[] labelViews;
    @Bind({R.id.tv_all_count, R.id.tv_good_count, R.id.tv_middle_count, R.id.tv_poor_count})
    TextView[] countViews;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private PullListFragmentHandler handler;
    private GoodsEvaluateAdapter adapter;
    private String goodsId;
    private String commentType;
    private List<GoodsEvaluateModel> evaluateList;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_goods_evaluate);

        SubscriberOnNextListener listener = new SubscriberOnNextListener<GoodsEvaluateCountModel>() {
            @Override
            public void onNext(GoodsEvaluateCountModel model) {
                countViews[0].setText((model.good + model.medium + model.poor) + "");
                countViews[1].setText(model.good + "");
                countViews[2].setText(model.medium + "");
                countViews[3].setText(model.poor + "");
            }
        };
        HttpMethods.getInstance().findGoodsCommentsNum(new NoProgressSubscriber<GoodsEvaluateCountModel>(listener, getActivity()), goodsId);

        handler = new PullListFragmentHandler(this, listView);
        listView.setPullRefreshEnabled(false);
        listView.setPullLoadEnabled(false);
        listView.setScrollLoadEnabled(true);
        listView.setDriverLine();
        adapter = new GoodsEvaluateAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.doPullRefreshing(true, DELAY_MILLIS);
    }

    @Override
    public void requestDatas() {
        SubscriberNextErrorListener listener = new SubscriberNextErrorListener<CommonListModel<List<GoodsEvaluateModel>>>() {
            @Override
            public void onNext(CommonListModel<List<GoodsEvaluateModel>> listCommonListModel) {
                if (listCommonListModel != null) {
                    if (mPage == 1) {
                        evaluateList = listCommonListModel.dataList;
                    } else {
                        evaluateList.addAll(listCommonListModel.dataList);
                    }
                    mIsMore = listCommonListModel.totalPage - listCommonListModel.page > 0 ? true : false;
                } else {
                    mIsMore = false;
                }

                if (evaluateList == null || evaluateList.size() == 0) {
                    evaluateList = new ArrayList<>();
                    handler.setEmptyViewVisible(View.VISIBLE);
                } else {
                    handler.setEmptyViewVisible(View.GONE);
                }
                adapter.setList(evaluateList);
                handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
            }

            @Override
            public void onError() {
                handler.setEmptyViewVisible(View.VISIBLE);
                handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("id", goodsId);
        if (!StringUtils.strIsEmpty(commentType)) {
            params.put("commentType", commentType);
        }
        HttpMethods.getInstance().findGoodsComments(new NoProgressSubscriber<CommonListModel<List<GoodsEvaluateModel>>>(listener, getActivity()), params);
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    @OnClick(R.id.ll_all)
    public void all() {
        changeTab(0);
        commentType = "";
        listView.doPullRefreshing(true, DELAY_MILLIS);
    }

    @OnClick(R.id.ll_good)
    public void good() {
        changeTab(1);
        commentType = "1";
        listView.doPullRefreshing(true, DELAY_MILLIS);
    }

    @OnClick(R.id.ll_middle)
    public void middle() {
        changeTab(2);
        commentType = "2";
        listView.doPullRefreshing(true, DELAY_MILLIS);
    }

    @OnClick(R.id.ll_poor)
    public void poor() {
        changeTab(3);
        commentType = "3";
        listView.doPullRefreshing(true, DELAY_MILLIS);
    }

    private void changeTab(int index) {
        for (int i = 0; i < labelViews.length; i++) {
            if (i == index) {
                labelViews[i].setTextColor(getResources().getColor(R.color.red1));
                countViews[i].setTextColor(getResources().getColor(R.color.red1));
            } else {
                labelViews[i].setTextColor(getResources().getColor(R.color.black1));
                countViews[i].setTextColor(getResources().getColor(R.color.black1));
            }
        }
    }
}
