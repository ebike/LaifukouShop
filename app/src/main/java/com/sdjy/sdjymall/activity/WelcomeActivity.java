package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.os.Handler;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    private Handler handler;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_welcome);
    }

    @Override
    public void init() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                WelcomeActivity.this.finish();
            }
        }, 1000);
    }
}
