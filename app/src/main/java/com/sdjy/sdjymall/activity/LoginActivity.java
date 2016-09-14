package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.SPUtils;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.event.FinishEvent;
import com.sdjy.sdjymall.http.CommonMethods;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.UserModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.CommonUtils;
import com.sdjy.sdjymall.util.DateUtil;
import com.sdjy.sdjymall.util.StringUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.iv_loginName)
    ImageView loginNameView;
    @Bind(R.id.et_loginName)
    EditText loginNameText;
    @Bind(R.id.v_line_loginName)
    View loginNameLine;
    @Bind(R.id.iv_password)
    ImageView passwordView;
    @Bind(R.id.et_password)
    EditText passwordText;
    @Bind(R.id.v_line_password)
    View passwordLine;
    @Bind(R.id.btn_login)
    Button loginButton;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);
    }

    @Override
    public void init() {
        loginNameText.addTextChangedListener(this);
        passwordText.addTextChangedListener(this);

    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_find_password)
    public void findPassword() {
        startActivity(new Intent(this, FindPasswordActivity.class));
    }

    @OnClick(R.id.btn_login)
    public void login() {
        final String loginName = loginNameText.getText().toString();
        final String password = passwordText.getText().toString();
        if (StringUtils.strIsEmpty(loginName)) {
            T.showShort(this, "请输入账号");
            return;
        }
        if (StringUtils.strIsEmpty(password)) {
            T.showShort(this, "请输入密码");
            return;
        }
        SubscriberOnNextListener listener = new SubscriberOnNextListener<UserModel>() {
            @Override
            public void onNext(UserModel model) {
                StaticValues.userModel = model;
                SPUtils.put(LoginActivity.this, "loginName", loginName);
                SPUtils.put(LoginActivity.this, "password", CommonUtils.MD5(password));
                SPUtils.put(LoginActivity.this, "loginTime", DateUtil.DateToString(new Date(), null));
                EventBus.getDefault().post(model);
                CommonMethods.syncShoppingCart(LoginActivity.this);
                CommonMethods.userCashBalance(LoginActivity.this);
                LoginActivity.this.finish();
            }
        };
        HttpMethods.getInstance().login(new ProgressSubscriber<UserModel>(listener, this), loginName, CommonUtils.MD5(password));
    }

    @OnClick(R.id.tv_register)
    public void register() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String loginName = loginNameText.getText().toString();
        String password = passwordText.getText().toString();
        if (!StringUtils.strIsEmpty(loginName)) {
            loginNameView.setImageResource(R.mipmap.icon_id_active);
            loginNameLine.setBackgroundColor(getResources().getColor(R.color.red2));
            loginNameText.setTextColor(getResources().getColor(R.color.red2));
        } else {
            loginNameView.setImageResource(R.mipmap.icon_id);
            loginNameLine.setBackgroundColor(getResources().getColor(R.color.gray9));
        }
        if (!StringUtils.strIsEmpty(password)) {
            passwordView.setImageResource(R.mipmap.icon_pass_active);
            passwordLine.setBackgroundColor(getResources().getColor(R.color.red2));
            passwordText.setTextColor(getResources().getColor(R.color.red2));
        } else {
            passwordView.setImageResource(R.mipmap.icon_pass);
            passwordLine.setBackgroundColor(getResources().getColor(R.color.gray9));
        }
        if (!StringUtils.strIsEmpty(loginName) && !StringUtils.strIsEmpty(password)) {
            loginButton.setAlpha(1);
        } else {
            loginButton.setAlpha(0.7f);
        }
    }

    public void onEvent(FinishEvent event) {
        if (event.simpleName.equals(this.getClass().getSimpleName())) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
