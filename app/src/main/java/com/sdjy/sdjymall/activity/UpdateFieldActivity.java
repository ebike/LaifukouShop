package com.sdjy.sdjymall.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.BankInfoModel;
import com.sdjy.sdjymall.model.UserModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class UpdateFieldActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.tv_right)
    TextView rightView;
    @Bind(R.id.et_text)
    EditText editText;
    @Bind(R.id.et_id_card)
    EditText idCardText;
    @Bind(R.id.et_number)
    EditText numberText;

    private String title;
    private BankInfoModel bankInfoModel;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_update_field);
    }

    @Override
    public void init() {
        title = getIntent().getStringExtra("title");
        bankInfoModel = (BankInfoModel) getIntent().getSerializableExtra("bankInfoModel");

        titleView.setText(title);
        rightView.setText("保存");
        rightView.setVisibility(View.VISIBLE);

        if ("姓名".equals(title)) {
            editText.setVisibility(View.VISIBLE);
            editText.setText(StaticValues.userModel.name);
            if (!StringUtils.strIsEmpty(StaticValues.userModel.name)) {
                editText.setSelection(StaticValues.userModel.name.length());
            }
        } else if ("身份证号".equals(title)) {
            idCardText.setVisibility(View.VISIBLE);
            idCardText.setText(StaticValues.userModel.idCard);
            if (!StringUtils.strIsEmpty(StaticValues.userModel.idCard)) {
                idCardText.setSelection(StaticValues.userModel.idCard.length());
            }
        } else if ("详细地址".equals(title)) {
            editText.setVisibility(View.VISIBLE);
            editText.setText(StaticValues.userModel.address);
            if (!StringUtils.strIsEmpty(StaticValues.userModel.address)) {
                editText.setSelection(StaticValues.userModel.address.length());
            }
        } else if ("开户姓名".equals(title)) {
            editText.setVisibility(View.VISIBLE);
            if (bankInfoModel != null && !StringUtils.strIsEmpty(bankInfoModel.accountName)) {
                editText.setText(bankInfoModel.accountName);
                editText.setSelection(bankInfoModel.accountName.length());
            }
        } else if ("开户银行".equals(title)) {
            editText.setVisibility(View.VISIBLE);
            if (bankInfoModel != null && !StringUtils.strIsEmpty(bankInfoModel.openBank)) {
                editText.setText(bankInfoModel.openBank);
                editText.setSelection(bankInfoModel.openBank.length());
            }
        } else if ("银行卡号".equals(title)) {
            numberText.setVisibility(View.VISIBLE);
            if (bankInfoModel != null && !StringUtils.strIsEmpty(bankInfoModel.bankAccount)) {
                numberText.setText(bankInfoModel.bankAccount);
                numberText.setSelection(bankInfoModel.bankAccount.length());
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void save() {
        Map<String, String> params = new HashMap<>();
        if ("姓名".equals(title)) {
            params.put("name", editText.getText().toString());
        } else if ("身份证号".equals(title)) {
            params.put("idCard", idCardText.getText().toString());
        } else if ("详细地址".equals(title)) {
            params.put("address", editText.getText().toString());
        } else if ("开户姓名".equals(title)) {
            params.put("accountName", editText.getText().toString());
        } else if ("开户银行".equals(title)) {
            params.put("openBank", editText.getText().toString());
        } else if ("银行卡号".equals(title)) {
            params.put("bankAccount", numberText.getText().toString());
        }

        if ("姓名".equals(title) || "身份证号".equals(title) || "详细地址".equals(title)) {
            SubscriberOnNextListener<UserModel> listener = new SubscriberOnNextListener<UserModel>() {
                @Override
                public void onNext(UserModel model) {
                    StaticValues.userModel = model;
                    EventBus.getDefault().post(model);
                    UpdateFieldActivity.this.finish();
                }
            };
            HttpMethods.getInstance().updateUserData(new ProgressSubscriber<UserModel>(listener, this), params);
        } else if ("开户姓名".equals(title) || "开户银行".equals(title) || "银行卡号".equals(title)) {
            HttpMethods.getInstance().updateBankInfo(new ProgressSubscriber(new SubscriberOnNextListener() {
                @Override
                public void onNext(Object o) {
                    EventBus.getDefault().post(new RefreshEvent(BankInfoActivity.class.getSimpleName()));
                    UpdateFieldActivity.this.finish();
                }
            }, this), params);
        }

    }
}
