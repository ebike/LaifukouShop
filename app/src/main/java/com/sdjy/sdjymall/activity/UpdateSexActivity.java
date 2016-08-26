package com.sdjy.sdjymall.activity;

import android.view.View;
import android.widget.ImageView;
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

public class UpdateSexActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind({R.id.iv_man_selected, R.id.iv_woman_selected, R.id.iv_other_selected})
    ImageView[] imageViews;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_update_sex);
    }

    @Override
    public void init() {
        titleView.setText("性别");
        if (StaticValues.userModel != null) {
            imageViews[StaticValues.userModel.sex - 1].setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick({R.id.rl_man, R.id.rl_woman, R.id.rl_other})
    public void update(View view) {
        String sex = "3";
        switch (view.getId()) {
            case R.id.rl_man:
                sex = "1";
                change(1);
                break;
            case R.id.rl_woman:
                sex = "2";
                change(2);
                break;
            case R.id.rl_other:
                sex = "3";
                change(3);
                break;
        }
        Map<String, String> params = new HashMap<>();
        params.put("sex", sex);
        SubscriberOnNextListener listener = new SubscriberOnNextListener<UserModel>() {
            @Override
            public void onNext(UserModel model) {
                StaticValues.userModel = model;
                EventBus.getDefault().post(model);
                UpdateSexActivity.this.finish();
            }
        };
        HttpMethods.getInstance().updateUserData(new ProgressSubscriber<UserModel>(listener, this), params);
    }

    private void change(int index) {
        for (int i = 0; i < imageViews.length; i++) {
            if (i == index) {
                imageViews[i].setVisibility(View.VISIBLE);
            } else {
                imageViews[i].setVisibility(View.GONE);
            }
        }

    }

}
