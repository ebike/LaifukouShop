package com.sdjy.sdjymall.fragment;

import android.os.Bundle;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.common.fragment.LazyFragment;

/**
 * 商品-商品
 */
public class GoodsGoodsFragment extends LazyFragment {

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_shopping_car);

    }
}
