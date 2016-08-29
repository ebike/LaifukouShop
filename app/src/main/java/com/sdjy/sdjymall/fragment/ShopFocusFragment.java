package com.sdjy.sdjymall.fragment;

import android.os.Bundle;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.fragment.base.BaseListFragment;

/**
 * 商家关注
 */
public class ShopFocusFragment extends BaseListFragment {

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_shop_focus);

    }

    @Override
    public void requestDatas() {

    }
}