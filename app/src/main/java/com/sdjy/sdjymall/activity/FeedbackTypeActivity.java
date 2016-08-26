package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.adapter.FeedbackTypeAdapter;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.view.ScrollListView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class FeedbackTypeActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.list_view)
    ScrollListView listView;

    private FeedbackTypeAdapter adapter;
    private String selectedType;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_feedback_type);
    }

    @Override
    public void init() {
        selectedType = getIntent().getStringExtra("selectedType");

        titleView.setText("反馈类型");
        adapter = new FeedbackTypeAdapter(this);
        adapter.setSelectType(selectedType);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra("feedbackType", type);
                setResult(200, intent);
                FeedbackTypeActivity.this.finish();
            }
        });
        SubscriberOnNextListener<List<String>> listener = new SubscriberOnNextListener<List<String>>() {
            @Override
            public void onNext(List<String> strings) {
                adapter.setList(strings);
            }
        };
        HttpMethods.getInstance().getFeedbackType(new ProgressSubscriber(listener, this));
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

}
