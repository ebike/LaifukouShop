package com.sdjy.sdjymall.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

/**
 * 用于recyclerview的适配器继承
 */
public abstract class AutoRVAdapter<T> extends RecyclerView.Adapter<RVHolder> {

    public List<T> list;
    public Context context;
    public OnRVItemClickListener rvItemClickListener;

    public AutoRVAdapter(Context context, List<T> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(onCreateViewLayoutID(viewType), null);

        return new RVHolder(view);
    }

    public abstract int onCreateViewLayoutID(int viewType);


    @Override
    public void onViewRecycled(final RVHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final RVHolder holder, final int position) {
        onBindViewHolder(holder.getViewHolder(), position);
    }

    public abstract void onBindViewHolder(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return list.size();
    }

    private AdapterView.OnItemClickListener onItemClickListener;

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setRvItemClickListener(OnRVItemClickListener rvItemClickListener) {
        this.rvItemClickListener = rvItemClickListener;
    }
}
