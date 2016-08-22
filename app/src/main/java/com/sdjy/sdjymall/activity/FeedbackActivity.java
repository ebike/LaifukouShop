package com.sdjy.sdjymall.activity;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;

import butterknife.OnClick;

public class FeedbackActivity extends BaseActivity {



    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_feedback);
    }

    @Override
    public void init() {

    }

    @OnClick(R.id.iv_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.tv_submit)
    public void submit(){

    }

    @OnClick(R.id.rl_type)
    public void type(){

    }
}
