package com.sdjy.sdjymall.fragment.base;

import com.sdjy.sdjymall.common.fragment.LazyFragment;

public abstract class BaseListFragment extends LazyFragment {
    protected static final int DELAY_MILLIS = 50;
    protected static final int PULL_DOWN_TO_REFRESH = 0;
    protected static final int PULL_UP_TO_REFRESH = 1;
    protected static final int PULL_TO_REFRESH_COMPLETE = 2;
    public int mPage = 1;
    protected int mPageCount = 15;
    public boolean mIsMore = false;

    public abstract void requestDatas();
}
