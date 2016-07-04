package com.laifukou.laifukoushop.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 用于recyclerview的适配器的数据绑定
 */
public class RVHolder extends RecyclerView.ViewHolder {


    private ViewHolder viewHolder;

    public RVHolder(View itemView) {
        super(itemView);
        viewHolder = ViewHolder.getViewHolder(itemView);
    }


    public ViewHolder getViewHolder() {
        return viewHolder;
    }

}
