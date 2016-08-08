package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.CommonDataModel;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 一级分类
 */
public class FirstSortsAdapter extends TAdapter<CommonDataModel> {

    private int checkedPosition;

    public FirstSortsAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_first_sorts, parent, false);
        }

        LinearLayout rootLayout = ViewHolder.get(convertView, R.id.ll_root);
        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);

        CommonDataModel model = mList.get(position);
        if (model != null) {
            nameView.setText(model.name);
            if (model.isChecked) {
                rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.main_bg));
                nameView.setTextColor(mContext.getResources().getColor(R.color.red));
            } else {
                rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                nameView.setTextColor(mContext.getResources().getColor(R.color.black));
            }
        }
        return convertView;
    }

    public void setDefaultChecked() {
        if (mList != null && mList.size() > 0) {
            mList.get(0).isChecked = true;
        }
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
    }

    public int getCheckedPosition() {
        return checkedPosition;
    }
}
