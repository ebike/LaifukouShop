package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.util.StringUtils;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 反馈类型
 */
public class FeedbackTypeAdapter extends TAdapter<String> {

    private String selectType;

    public FeedbackTypeAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_feedback_type, parent, false);
        }

        TextView textView = ViewHolder.get(convertView, R.id.tv_text);
        ImageView selectedView = ViewHolder.get(convertView, R.id.iv_selected);

        String type = mList.get(position);
        if (!StringUtils.strIsEmpty(type)) {
            textView.setText(type);
            if (!StringUtils.strIsEmpty(selectType) && type.equals(selectType)) {
                selectedView.setVisibility(View.VISIBLE);
            } else {
                selectedView.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }
}
