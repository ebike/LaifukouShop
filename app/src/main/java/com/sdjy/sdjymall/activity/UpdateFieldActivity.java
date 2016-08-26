package com.sdjy.sdjymall.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.UserModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;

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

    private String title;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_update_field);
    }

    @Override
    public void init() {
        title = getIntent().getStringExtra("title");

        titleView.setText(title);
        rightView.setText("保存");
        rightView.setVisibility(View.VISIBLE);
        if ("姓名".equals(title)) {
            editText.setVisibility(View.VISIBLE);
            editText.setText(StaticValues.userModel.name);
        } else if ("身份证号".equals(title)) {
            idCardText.setVisibility(View.VISIBLE);
            idCardText.setText(StaticValues.userModel.idCard);
        } else if ("详细地址".equals(title)) {
            editText.setVisibility(View.VISIBLE);
            editText.setText(StaticValues.userModel.address);
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
        }
        SubscriberOnNextListener<UserModel> listener = new SubscriberOnNextListener<UserModel>() {
            @Override
            public void onNext(UserModel model) {
                StaticValues.userModel = model;
                EventBus.getDefault().post(model);
                UpdateFieldActivity.this.finish();
            }
        };
        HttpMethods.getInstance().updateUserData(new ProgressSubscriber<UserModel>(listener, this), params);
    }
}
