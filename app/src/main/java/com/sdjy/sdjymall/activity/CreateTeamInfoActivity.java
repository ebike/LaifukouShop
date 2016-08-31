package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.constants.StaticValues;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class CreateTeamInfoActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.iv_header)
    ImageView headerView;
    @Bind(R.id.tv_name)
    TextView nameView;
    @Bind(R.id.tv_grade)
    TextView gradeView;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_create_team_info);
    }

    @Override
    public void init() {
        titleView.setText("互助创业");

        Glide.with(this)
                .load(StaticValues.userModel.headPic)
                .bitmapTransform(new CropCircleTransformation(this))
                .error(R.mipmap.icon_default_head)
                .into(headerView);
        nameView.setText(StaticValues.userModel.loginName);
        gradeView.setText("等级：" + StaticValues.userModel.userGrade);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.rlvv_bank)
    public void bank() {
        startActivity(new Intent(this, BankInfoActivity.class));
    }

    @OnClick(R.id.rlvv_team)
    public void team() {
        startActivity(new Intent(this, MyTeamActivity.class));
    }

    @OnClick(R.id.rlvv_recommend)
    public void recommend() {
        startActivity(new Intent(this, MyRecommendActivity.class));
    }
}
