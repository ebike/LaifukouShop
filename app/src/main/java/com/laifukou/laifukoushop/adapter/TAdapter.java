package com.laifukou.laifukoushop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract  class TAdapter<T> extends android.widget.BaseAdapter implements Serializable {

    protected List<T> mList;
    protected Context mContext;
    protected ListView mListView;

    public TAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (mList != null)
            return mList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView,
                                 ViewGroup parent);

    public void setList(T[] list) {
        if (list == null || list.length == 0) {
            return;
        }
        List<T> arrayList = new ArrayList<T>(list.length);
        for (T t : list) {
            arrayList.add(t);
        }
        setList(arrayList);
    }

    public List<T> getData(){
        return mList;
    }

    public void setList(List<T> list) {
        if (list == null || list.isEmpty()) {
            notifyChangeEmpty();
            return;
        }
        this.mList = new ArrayList<T>();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void notifyChangeEmpty() {
        this.mList = new ArrayList<T>();
        notifyDataSetChanged();
    }

    public ListView getListView() {
        return mListView;
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }
}
