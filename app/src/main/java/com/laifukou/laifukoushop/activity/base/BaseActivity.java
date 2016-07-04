package com.laifukou.laifukoushop.activity.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import butterknife.ButterKnife;

/**
 * Activity的基类
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载布局文件
        loadLoyout();
        //注入申请
        ButterKnife.bind(this);
        //初始化
        init();
    }

    public abstract void loadLoyout();

    public abstract void init();
}
