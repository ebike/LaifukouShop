package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.HistorySearchModel;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 历史搜索
 */
public class HistorySearchAdapter extends TAdapter<HistorySearchModel> {

    public HistorySearchAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_history_search, parent, false);
        }

        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);

        HistorySearchModel model = mList.get(position);
        if (model != null) {
            nameView.setText(model.word);
        }

        return convertView;
    }
}
