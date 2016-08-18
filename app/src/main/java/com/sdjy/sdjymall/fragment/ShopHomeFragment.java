package com.sdjy.sdjymall.fragment;

import android.os.Bundle;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.common.fragment.LazyFragment;

/**
 * 店铺首页
 */
public class ShopHomeFragment extends LazyFragment {

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_shop_home);
    }
}
