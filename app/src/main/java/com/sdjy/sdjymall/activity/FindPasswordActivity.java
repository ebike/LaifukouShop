package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.ValidateCodeModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.CommonUtils;
import com.sdjy.sdjymall.util.StringUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

public class FindPasswordActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.iv_phone)
    ImageView phoneView;
    @Bind(R.id.et_phone)
    EditText phoneText;
    @Bind(R.id.v_line_phone)
    View phoneLine;
    @Bind(R.id.iv_validatecode)
    ImageView validatecodeView;
    @Bind(R.id.et_validatecode)
    EditText validateCodeText;
    @Bind(R.id.v_line_validatecode)
    View validatecodeLine;
    @Bind(R.id.tv_send_validatecode)
    TextView sendValidatecodeView;
    @Bind(R.id.btn_next)
    Button nextButton;

    private Handler handler;
    private Runnable runnable;
    private int minute = 60;
    private String phone;
    private String validateCode;
    private ValidateCodeModel validateCodeModel;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_find_password);
    }

    @Override
    public void init() {
        phoneText.addTextChangedListener(this);
        validateCodeText.addTextChangedListener(this);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_send_validatecode)
    public void sendValidatecode() {
        phone = phoneText.getText().toString().trim();
        if (StringUtils.strIsEmpty(phone) || !CommonUtils.isPhoneNumber(phone)) {
            T.showShort(this, "手机号码格式不正确");
            return;
        }
        SubscriberOnNextListener<ValidateCodeModel> listener = new SubscriberOnNextListener<ValidateCodeModel>() {
            @Override
            public void onNext(ValidateCodeModel model) {
                validateCodeModel = model;
                T.showShort(FindPasswordActivity.this, "验证码已发送至手机");
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (minute > 0) {
                            sendValidatecodeView.setEnabled(false);
                            sendValidatecodeView.setText(minute + "s后可重发");
                            minute--;
                            handler.postDelayed(this, 1000);
                        } else {
                            sendValidatecodeView.setText("获取验证码");
                            minute = 60;
                            sendValidatecodeView.setEnabled(true);
                        }
                    }
                };
                handler.postDelayed(runnable, 1000);
            }
        };
        HttpMethods.getInstance().sendUpdatePassCode(new ProgressSubscriber<ValidateCodeModel>(listener, this), phone);
    }

    @OnClick(R.id.btn_next)
    public void next() {
        phone = phoneText.getText().toString().trim();
        validateCode = validateCodeText.getText().toString().trim();
        if (StringUtils.strIsEmpty(phone) || !CommonUtils.isPhoneNumber(phone)) {
            T.showShort(this, "手机号码格式不正确");
            return;
        }
        if (StringUtils.strIsEmpty(validateCode)) {
            T.showShort(this, "请输入验证码");
            return;
        }
        if (validateCodeModel != null && new Date().getTime() < validateCodeModel.expireTime) {
            Intent intent = new Intent(this, UpdatePasswordActivity.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
        } else {
            T.showShort(this, "验证码不正确");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        phone = phoneText.getText().toString().trim();
        validateCode = validateCodeText.getText().toString().trim();
        if (!StringUtils.strIsEmpty(phone)) {
            phoneView.setImageResource(R.mipmap.icon_phone_active);
            phoneLine.setBackgroundColor(getResources().getColor(R.color.red2));
            phoneText.setTextColor(getResources().getColor(R.color.red2));
        } else {
            phoneView.setImageResource(R.mipmap.icon_phone);
            phoneLine.setBackgroundColor(getResources().getColor(R.color.gray9));
        }
        if (!StringUtils.strIsEmpty(validateCode)) {
            validatecodeView.setImageResource(R.mipmap.icon_code_active);
            validatecodeLine.setBackgroundColor(getResources().getColor(R.color.red2));
            validateCodeText.setTextColor(getResources().getColor(R.color.red2));
        } else {
            validatecodeView.setImageResource(R.mipmap.icon_code);
            validatecodeLine.setBackgroundColor(getResources().getColor(R.color.gray9));
        }
        if (!StringUtils.strIsEmpty(phone) && !StringUtils.strIsEmpty(validateCode)) {
            nextButton.setAlpha(1);
        } else {
            nextButton.setAlpha(0.7f);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
