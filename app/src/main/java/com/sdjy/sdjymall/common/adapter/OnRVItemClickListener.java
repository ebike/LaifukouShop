package com.sdjy.sdjymall.common.adapter;

import android.view.View;

/**
 * RecyclerView的item点击监听
 */
public interface OnRVItemClickListener<T> {

    void onItemClick(int position, T t, View v);
}
