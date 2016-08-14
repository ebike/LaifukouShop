package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.GoodsEvaluateModel;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 商品评价
 */
public class GoodsEvaluateAdapter extends TAdapter<GoodsEvaluateModel> {

    public GoodsEvaluateAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_goods_evaluate, parent, false);
        }

        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        TextView timaView = ViewHolder.get(convertView, R.id.tv_time);
        TextView contentView = ViewHolder.get(convertView, R.id.tv_content);

        GoodsEvaluateModel model = mList.get(position);
        if(model != null){
            nameView.setText(model.loginName);
            timaView.setText(model.commentTime);
            contentView.setText(model.content);
        }

        return convertView;
    }
}
