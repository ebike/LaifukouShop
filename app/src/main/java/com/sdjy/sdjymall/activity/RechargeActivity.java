package com.sdjy.sdjymall.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.adapter.RechargeAdapter;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.view.ScrollGridView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity {

    @Bind(R.id.grid_view)
    ScrollGridView gridView;
    @Bind(R.id.tv_amount)
    TextView amountView;

    private RechargeAdapter adapter;
    private Integer amount;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_recharge);
    }

    @Override
    public void init() {
        adapter = new RechargeAdapter(this);
        gridView.setAdapter(adapter);
        SubscriberOnNextListener listener = new SubscriberOnNextListener<List<Integer>>() {
            @Override
            public void onNext(List<Integer> list) {
                adapter.setList(list);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        amount = (Integer) parent.getItemAtPosition(position);
                        amountView.setText(""+amount);
                    }
                });
            }
        };
        HttpMethods.getInstance().findRechargeNums(new ProgressSubscriber<List<Integer>>(listener, this));
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_recharge)
    public void recharge() {

    }
}
