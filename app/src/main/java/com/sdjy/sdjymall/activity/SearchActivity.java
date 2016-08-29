package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.adapter.HistorySearchAdapter;
import com.sdjy.sdjymall.common.util.DensityUtils;
import com.sdjy.sdjymall.common.util.DialogUtils;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.HistorySearchModel;
import com.sdjy.sdjymall.model.HotSearchWordModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.StringUtils;
import com.sdjy.sdjymall.view.ScrollListView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @Bind(R.id.et_search)
    EditText searchText;
    @Bind(R.id.ll_hot)
    LinearLayout hotLayout;
    @Bind(R.id.lv_history)
    ScrollListView listView;

    private List<HistorySearchModel> historySearchList;
    private HistorySearchAdapter adapter;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_search);
    }

    @Override
    public void init() {
        adapter = new HistorySearchAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                HistorySearchModel model = (HistorySearchModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(SearchActivity.this, GoodsActivity.class);
                intent.putExtra("key", model.word);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long l) {
                final HistorySearchModel model = (HistorySearchModel) parent.getItemAtPosition(position);
                DialogUtils.showDialog(SearchActivity.this, "确定要删除该条搜索历史？", "删除", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HttpMethods.getInstance().delSearchWord(new ProgressSubscriber(new SubscriberOnNextListener<HttpResult>() {
                            @Override
                            public void onNext(HttpResult httpResult) {
                                T.showShort(SearchActivity.this, httpResult.message);
                                historySearchList.remove(model);
                                adapter.setList(historySearchList);
                            }
                        }, SearchActivity.this), model.oid);
                    }
                });
                return true;
            }
        });

        SubscriberOnNextListener<List<HotSearchWordModel>> hotListener = new SubscriberOnNextListener<List<HotSearchWordModel>>() {
            @Override
            public void onNext(List<HotSearchWordModel> hotSearchWordModels) {
                for (final HotSearchWordModel model : hotSearchWordModels) {
                    TextView textView = new TextView(SearchActivity.this);
                    textView.setText(model.word);
                    textView.setTextAppearance(SearchActivity.this, R.style.gray6_12);
                    textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, DensityUtils.dp2px(SearchActivity.this, 12), 0);
                    hotLayout.addView(textView, params);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(SearchActivity.this, GoodsActivity.class);
                            intent.putExtra("key", model.word);
                            startActivity(intent);
                        }
                    });
                }
            }
        };
        HttpMethods.getInstance().hotSearchWord(new ProgressSubscriber<List<HotSearchWordModel>>(hotListener, this));

        SubscriberOnNextListener<List<HistorySearchModel>> historyListener = new SubscriberOnNextListener<List<HistorySearchModel>>() {
            @Override
            public void onNext(List<HistorySearchModel> historySearchModels) {
                historySearchList = historySearchModels;
                adapter.setList(historySearchList);
            }
        };
        HttpMethods.getInstance().searchHis(new ProgressSubscriber<List<HistorySearchModel>>(historyListener, this));
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_clear)
    public void clear() {
        HttpMethods.getInstance().clearSearchHis(new ProgressSubscriber(new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult httpResult) {
                T.showShort(SearchActivity.this, httpResult.message);
                historySearchList.clear();
                adapter.setList(historySearchList);
            }
        }, this));
    }

    @OnClick(R.id.tv_search)
    public void search() {
        String search = searchText.getText().toString();
        if (!StringUtils.strIsEmpty(search)) {
            Intent intent = new Intent(SearchActivity.this, GoodsActivity.class);
            intent.putExtra("key", search);
            startActivity(intent);
        } else {
            T.showShort(this, "请输入要搜索的内容");
        }
    }
}
