package com.laifukou.laifukoushop.fragment;

import android.os.Bundle;

import com.laifukou.laifukoushop.R;
import com.laifukou.laifukoushop.common.fragment.LazyFragment;

/**
 * 我的
 */
public class MyFragment extends LazyFragment {

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_my);

    }
}
