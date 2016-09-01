package com.sdjy.sdjymall.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.adapter.GoodsRVAdapter;
import com.sdjy.sdjymall.common.adapter.OnRVItemClickListener;
import com.sdjy.sdjymall.fragment.base.BaseListFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonListModel;
import com.sdjy.sdjymall.model.GoodsModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.util.StringUtils;
import com.sdjy.sdjymall.view.RecyclerViewFragmentHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 店铺首页
 */
public class ShopGoodsFragment extends BaseListFragment {

    @Bind(R.id.recycler_view)
    IRecyclerView iRecyclerView;

    private RecyclerViewFragmentHandler mHandler;
    private GoodsRVAdapter adapter;
    private List<GoodsModel> goodsList = new ArrayList<>();
    private SubscriberNextErrorListener nextErrorListener;
    private Map<String, String> params = new HashMap<>();
    private String shopId;
    private String pageSorts;
    private String sortTerm;
    private String sortOrder;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_shop_goods);

        params.put("shopId", shopId);

        mHandler = new RecyclerViewFragmentHandler(this, iRecyclerView);
        iRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new GoodsRVAdapter(getActivity(), goodsList);
        iRecyclerView.setIAdapter(adapter);
        adapter.setRvItemClickListener(new OnRVItemClickListener<GoodsModel>() {
            @Override
            public void onItemClick(int position, GoodsModel model, View v) {

            }
        });

        nextErrorListener = new SubscriberNextErrorListener<CommonListModel<List<GoodsModel>>>() {
            @Override
            public void onNext(CommonListModel<List<GoodsModel>> listCommonListModel) {
                if (listCommonListModel != null && listCommonListModel.dataList != null) {
                    if (mPage == 1) {
                        goodsList.clear();
                    }
                    goodsList.addAll(listCommonListModel.dataList);
                    mIsMore = listCommonListModel.totalPage - listCommonListModel.page > 0 ? true : false;
                } else {
                    mIsMore = false;
                }

                if (goodsList == null || goodsList.size() == 0) {
                    goodsList.clear();
                    iRecyclerView.setBackgroundColor(getResources().getColor(R.color.transparent));
                } else {
                    iRecyclerView.setBackgroundColor(getResources().getColor(R.color.white));
                }
                adapter.notifyDataSetChanged();
                mHandler.doComplete();
            }

            @Override
            public void onError() {
                iRecyclerView.setBackgroundColor(getResources().getColor(R.color.transparent));
                mHandler.doComplete();
            }
        };
        requestDatas();
    }

    @Override
    public void requestDatas() {
        params.put("pageSorts", !StringUtils.strIsEmpty(pageSorts) ? pageSorts : "");
        params.put("sortTerm", !StringUtils.strIsEmpty(sortTerm) ? sortTerm : "");
        params.put("sortOrder", !StringUtils.strIsEmpty(sortOrder) ? sortOrder : "");
        HttpMethods.getInstance().searchGoods(new NoProgressSubscriber<CommonListModel<List<GoodsModel>>>(nextErrorListener, getActivity()), params, mPage);
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setPageSorts(String pageSorts) {
        this.pageSorts = pageSorts;
    }

    public void setSortTerm(String sortTerm) {
        this.sortTerm = sortTerm;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortOrder() {
        return sortOrder;
    }
}
