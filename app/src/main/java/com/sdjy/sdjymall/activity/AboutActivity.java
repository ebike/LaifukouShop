package com.sdjy.sdjymall.activity;

import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.util.CommonUtils;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 关于
 */
public class AboutActivity extends BaseActivity {

    @Bind(R.id.tv_logo)
    TextView logoTextView;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_about);
    }

    @Override
    public void init() {
        String versionName = CommonUtils.getVersionName(this);
        logoTextView.setText("For Android V" + versionName);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}
