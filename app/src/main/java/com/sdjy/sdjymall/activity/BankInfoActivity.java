package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.BankInfoModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.view.RowLabelValueView;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class BankInfoActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.rlvv_account_name)
    RowLabelValueView accountNameView;
    @Bind(R.id.rlvv_open_bank)
    RowLabelValueView openBankView;
    @Bind(R.id.rlvv_bank_account)
    RowLabelValueView bankAccountView;

    private BankInfoModel bankInfoModel;
    private SubscriberOnNextListener listener;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_bank_info);
        EventBus.getDefault().register(this);
    }

    @Override
    public void init() {
        titleView.setText("银行卡信息");

        listener = new SubscriberOnNextListener<BankInfoModel>() {
            @Override
            public void onNext(BankInfoModel model) {
                bankInfoModel = model;
                if (bankInfoModel != null) {
                    accountNameView.setValue(bankInfoModel.accountName);
                    openBankView.setValue(bankInfoModel.openBank);
                    bankAccountView.setValue(bankInfoModel.bankAccount);
                }
            }
        };
        HttpMethods.getInstance().findBankinfo(new ProgressSubscriber<BankInfoModel>(listener, this));
    }

    @OnClick({R.id.rlvv_account_name, R.id.rlvv_open_bank, R.id.rlvv_bank_account})
    public void updateField(View view) {
        Intent intent = new Intent(this, UpdateFieldActivity.class);
        intent.putExtra("bankInfoModel", bankInfoModel);
        switch (view.getId()) {
            case R.id.rlvv_account_name:
                intent.putExtra("title", "开户姓名");
                break;
            case R.id.rlvv_open_bank:
                intent.putExtra("title", "开户银行");
                break;
            case R.id.rlvv_bank_account:
                intent.putExtra("title", "银行卡号");
                break;
        }
        startActivity(intent);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    public void onEvent(RefreshEvent event) {
        if (event.simpleName.equals(this.getClass().getSimpleName())) {
            HttpMethods.getInstance().findBankinfo(new ProgressSubscriber<BankInfoModel>(listener, this));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
