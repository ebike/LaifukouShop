package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 充值立返
 */
public class RechargeAdapter extends TAdapter<Integer> {

    private int selectedPosition;

    public RechargeAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_recharge, parent, false);
        }

        LinearLayout rootLayout = ViewHolder.get(convertView, R.id.ll_root);
        TextView amountView = ViewHolder.get(convertView, R.id.tv_amount);

        Integer integer = mList.get(position);
        if (integer != null) {
            amountView.setText(integer + "");
            if (selectedPosition == position) {
                amountView.setTextColor(mContext.getResources().getColor(R.color.red1));
                rootLayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.border_red));
            } else {
                amountView.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        }
        return convertView;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
