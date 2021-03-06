package com.sdjy.sdjymall.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.event.FinishEvent;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.CommonUtils;
import com.sdjy.sdjymall.util.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class UpdatePasswordActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.iv_password)
    ImageView passwordView;
    @Bind(R.id.et_password)
    EditText passwordText;
    @Bind(R.id.v_line_password)
    View passwordLine;
    @Bind(R.id.iv_password1)
    ImageView passwordView1;
    @Bind(R.id.et_password1)
    EditText passwordText1;
    @Bind(R.id.v_line_password1)
    View passwordLine1;
    @Bind(R.id.btn_confirm)
    Button confirmButton;

    private String password;
    private String password1;
    private String phone;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_update_password);
    }

    @Override
    public void init() {
        phone = getIntent().getStringExtra("phone");
        passwordText.addTextChangedListener(this);
        passwordText1.addTextChangedListener(this);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        password = passwordText.getText().toString().trim();
        password1 = passwordText1.getText().toString().trim();
        if (StringUtils.strIsEmpty(password) || password.length() < 6 || password.length() > 16) {
            T.showShort(this, "密码为6-16位字母或数字");
            return;
        }
        if (StringUtils.strIsEmpty(password1)) {
            T.showShort(this, "请再次输入新密码");
            return;
        }
        if (!password.equals(password1)) {
            T.showShort(this, "两次密码不一致");
            return;
        }
        SubscriberOnNextListener<String> listener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String userId) {
                SubscriberOnNextListener listener1 = new SubscriberOnNextListener<HttpResult>() {
                    @Override
                    public void onNext(HttpResult httpResult) {
                        T.showShort(UpdatePasswordActivity.this, httpResult.message);
                        EventBus.getDefault().post(new FinishEvent(FindPasswordActivity.class.getSimpleName()));
                        UpdatePasswordActivity.this.finish();
                    }
                };
                HttpMethods.getInstance().resetPassword(new ProgressSubscriber(listener1, UpdatePasswordActivity.this), userId, CommonUtils.MD5(password));
            }
        };
        HttpMethods.getInstance().checkUserByPhone(new ProgressSubscriber<String>(listener, this), phone);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        password = passwordText.getText().toString().trim();
        password1 = passwordText1.getText().toString().trim();
        if (!StringUtils.strIsEmpty(password)) {
            passwordView.setImageResource(R.mipmap.icon_pass_active);
            passwordLine.setBackgroundColor(getResources().getColor(R.color.red2));
            passwordText.setTextColor(getResources().getColor(R.color.red2));
        } else {
            passwordView.setImageResource(R.mipmap.icon_pass);
            passwordLine.setBackgroundColor(getResources().getColor(R.color.gray9));
        }
        if (!StringUtils.strIsEmpty(password1)) {
            passwordView1.setImageResource(R.mipmap.icon_pass_active);
            passwordLine1.setBackgroundColor(getResources().getColor(R.color.red2));
            passwordText1.setTextColor(getResources().getColor(R.color.red2));
        } else {
            passwordView1.setImageResource(R.mipmap.icon_pass);
            passwordLine1.setBackgroundColor(getResources().getColor(R.color.gray9));
        }
        if (!StringUtils.strIsEmpty(password) && !StringUtils.strIsEmpty(password1)) {
            confirmButton.setAlpha(1);
        } else {
            confirmButton.setAlpha(0.7f);
        }
    }
}
