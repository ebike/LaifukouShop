package com.sdjy.sdjymall.activity.base;

/***
 * 封装分页需要的参数
 */
public abstract class BaseListActivity extends BaseActivity {
    protected static final int DELAY_MILLIS = 50;
    protected static final int PULL_DOWN_TO_REFRESH = 0;
    protected static final int PULL_UP_TO_REFRESH = 1;
    protected static final int PULL_TO_REFRESH_COMPLETE = 2;
    public int mPage = 1;
    public boolean mIsMore = false;

    public abstract void requestDatas();
}
