package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.CommonUtils;
import com.sdjy.sdjymall.util.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class FeedbackActivity extends BaseActivity {

    @Bind(R.id.tv_type)
    TextView typeView;
    @Bind(R.id.tv_max_num)
    TextView maxNumView;
    @Bind(R.id.et_content)
    EditText contentText;
    @Bind(R.id.et_phone_email)
    EditText phoneEmailText;

    private String feedbackType;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_feedback);
    }

    @Override
    public void init() {

    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_submit)
    public void submit() {
        String phoneEmail = phoneEmailText.getText().toString();
        String content = contentText.getText().toString();
        if (StringUtils.strIsEmpty(feedbackType)) {
            T.showShort(this, "请选择反馈类型");
            return;
        }
        if (StringUtils.strIsEmpty(content)) {
            T.showShort(this, "请填写意见或建议");
            return;
        }
        if (StringUtils.strIsEmpty(phoneEmail)
                || (!StringUtils.strIsEmpty(phoneEmail) && !CommonUtils.isPhoneNumber(phoneEmail) && !CommonUtils.checkEmail(phoneEmail))) {
            T.showShort(this, "手机号或邮箱格式不正确");
            return;
        }
        SubscriberOnNextListener<HttpResult> listener = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult httpResult) {
                T.showShort(FeedbackActivity.this, httpResult.message);
                if (httpResult.code.equals("1")) {
                    FeedbackActivity.this.finish();
                }
            }
        };
        HttpMethods.getInstance().saveFeedback(new ProgressSubscriber(listener, this), feedbackType, content, phoneEmail);
    }

    @OnClick(R.id.rl_type)
    public void type() {
        Intent intent = new Intent(this, FeedbackTypeActivity.class);
        intent.putExtra("selectedType", feedbackType);
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == 200) {
            feedbackType = data.getStringExtra("feedbackType");
            typeView.setText(feedbackType);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnTextChanged(R.id.et_content)
    public void changeContent(CharSequence text) {
        int num = 200 - text.length();
        maxNumView.setText(num + "/200");
    }
}
