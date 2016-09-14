package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.os.Handler;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.SPUtils;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.http.CommonMethods;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.UserModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.util.DateUtil;
import com.sdjy.sdjymall.util.StringUtils;

import java.util.Date;

import de.greenrobot.event.EventBus;

public class WelcomeActivity extends BaseActivity {

    private Handler handler;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_welcome);
    }

    @Override
    public void init() {
        String loginName = (String) SPUtils.get(this, "loginName", "");
        String password = (String) SPUtils.get(this, "password", "");
        String loginTime = (String) SPUtils.get(this, "loginTime", "");
        if (!StringUtils.strIsEmpty(loginName)
                && !StringUtils.strIsEmpty(password)
                && !DateUtil.moreThanAWeek(loginTime)) {
            SubscriberNextErrorListener listener = new SubscriberNextErrorListener<UserModel>() {
                @Override
                public void onNext(UserModel model) {
                    StaticValues.userModel = model;
                    SPUtils.put(WelcomeActivity.this, "loginTime", DateUtil.DateToString(new Date(), null));
                    EventBus.getDefault().post(model);
                    CommonMethods.userCashBalance(WelcomeActivity.this);
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    WelcomeActivity.this.finish();
                }

                @Override
                public void onError() {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    WelcomeActivity.this.finish();
                }
            };
            HttpMethods.getInstance().login(new NoProgressSubscriber<UserModel>(listener, this), loginName, password);
        } else {
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
}
