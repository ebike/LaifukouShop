package com.sdjy.sdjymall.fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.common.fragment.LazyFragment;

import butterknife.Bind;

/**
 * 购物车
 */
public class ShoppingCartFragment extends LazyFragment {

    @Bind(R.id.iv_message)
    ImageView messageView;
    @Bind(R.id.tv_edit)
    TextView editView;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_shopping_car);

    }
}
