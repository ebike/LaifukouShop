package com.sdjy.sdjymall.activity;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.BankInfoModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;

public class BankInfoActivity extends BaseActivity {


    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_bank_info);
    }

    @Override
    public void init() {
        SubscriberOnNextListener listener = new SubscriberOnNextListener<BankInfoModel>() {
            @Override
            public void onNext(BankInfoModel bankInfoModel) {

            }
        };
        HttpMethods.getInstance().findBankinfo(new ProgressSubscriber<BankInfoModel>(listener, this));
    }
}
