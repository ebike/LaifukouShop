package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseListActivity;
import com.sdjy.sdjymall.adapter.BrowsingHistoryAdapter;
import com.sdjy.sdjymall.common.util.DialogUtils;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.GoodsBrowsingModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.GoodsUtils;
import com.sdjy.sdjymall.view.PullListActivityHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class BrowsingHistoryActivity extends BaseListActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.tv_right)
    TextView rightView;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;
    @Bind(R.id.rl_bottom)
    RelativeLayout bottomLayout;
    @Bind(R.id.tv_choose_all)
    TextView chooseAllView;

    private PullListActivityHandler handler;
    private BrowsingHistoryAdapter adapter;
    private List<GoodsBrowsingModel> browsingModelList;
    private SubscriberNextErrorListener listener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_browsing_history);
    }

    @Override
    public void init() {
        titleView.setText("浏览记录");
        rightView.setVisibility(View.VISIBLE);
        rightView.setText("编辑");

        listener = new SubscriberNextErrorListener<List<GoodsBrowsingModel>>() {
            @Override
            public void onNext(List<GoodsBrowsingModel> goodsBrowsingModels) {
                browsingModelList = goodsBrowsingModels;
                if (browsingModelList == null || browsingModelList.size() == 0) {
                    browsingModelList = new ArrayList<>();
                    handler.setEmptyViewVisible(View.VISIBLE);
                    rightView.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                } else {
                    handler.setEmptyViewVisible(View.GONE);
                }
                adapter.setList(browsingModelList);
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
        adapter = new BrowsingHistoryAdapter(this);
        adapter.setCallback(new BrowsingHistoryAdapter.ChangeSelectedCallback() {
            @Override
            public void onChanged(int position) {
                GoodsBrowsingModel model = browsingModelList.get(position);
                if (model.isSelected) {
                    model.isSelected = false;
                } else {
                    model.isSelected = true;
                }
                adapter.setList(browsingModelList);
                boolean isAllSelected = GoodsUtils.isAllSelected(browsingModelList);
                if (isAllSelected) {
                    Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_sel);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    chooseAllView.setCompoundDrawables(drawable, null, null, null);
                } else {
                    Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_nosel);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    chooseAllView.setCompoundDrawables(drawable, null, null, null);
                }
            }
        });
        listView.setAdapter(adapter);
        listView.doPullRefreshing(true, DELAY_MILLIS);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsBrowsingModel model = (GoodsBrowsingModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(BrowsingHistoryActivity.this, GoodsInfoActivity.class);
                intent.putExtra("GoodsId", model.goodsId);
                startActivity(intent);
            }
        });
        adapter.setLongClickListener(new BrowsingHistoryAdapter.LongClickListener() {
            @Override
            public void onLongClick(final GoodsBrowsingModel model) {
                DialogUtils.showDialog(BrowsingHistoryActivity.this, "删除该条浏览记录", "删除", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HttpMethods.getInstance().delUserBrowse(new ProgressSubscriber(new SubscriberOnNextListener() {
                            @Override
                            public void onNext(Object o) {
                                T.showShort(BrowsingHistoryActivity.this, "删除成功");
                                listView.doPullRefreshing(true, DELAY_MILLIS);
                            }
                        }, BrowsingHistoryActivity.this), model.oid);
                    }
                });
            }
        });
    }

    @Override
    public void requestDatas() {
        if (StaticValues.userModel != null) {
            HttpMethods.getInstance().userBrowse(new NoProgressSubscriber<List<GoodsBrowsingModel>>(listener, this));
        } else {
            rightView.setVisibility(View.GONE);
            handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void edit() {
        if (adapter.isEdit()) {
            rightView.setText("编辑");
            adapter.setEdit(false);
            bottomLayout.setVisibility(View.GONE);
        } else {
            rightView.setText("完成");
            adapter.setEdit(true);
            bottomLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_choose_all)
    public void chooseAll() {
        boolean isAllSelected = GoodsUtils.isAllSelected(browsingModelList);
        if (isAllSelected) {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_nosel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            chooseAllView.setCompoundDrawables(drawable, null, null, null);
            for (GoodsBrowsingModel model : browsingModelList) {
                model.isSelected = false;
            }
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_sel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            chooseAllView.setCompoundDrawables(drawable, null, null, null);
            for (GoodsBrowsingModel model : browsingModelList) {
                model.isSelected = true;
            }
        }
        adapter.setList(browsingModelList);
    }

    @OnClick(R.id.tv_delete)
    public void delete() {
        boolean hasSelected = GoodsUtils.hasSelected(browsingModelList);
        if (hasSelected) {
            StringBuffer ids = new StringBuffer();
            for (GoodsBrowsingModel browsingModel : browsingModelList) {
                if (browsingModel.isSelected) {
                    ids.append(browsingModel.oid).append(";");
                }
            }
            ids.deleteCharAt(ids.length() - 1);
            HttpMethods.getInstance().delUserBrowse(new ProgressSubscriber(new SubscriberOnNextListener() {
                @Override
                public void onNext(Object o) {
                    T.showShort(BrowsingHistoryActivity.this, "删除成功");
                    listView.doPullRefreshing(true, DELAY_MILLIS);
                }
            }, BrowsingHistoryActivity.this), ids.toString());
        } else {
            T.showShort(this, "您还没有选择商品哦！");
        }
    }
}
