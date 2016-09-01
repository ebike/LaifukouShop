package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.RefereeUserModel;
import com.sdjy.sdjymall.view.ViewHolder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 推荐人
 */
public class MyRecommendAdapter extends TAdapter<RefereeUserModel> {

    public MyRecommendAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_my_recommend, parent, false);
        }

        ImageView headerView = ViewHolder.get(convertView, R.id.iv_header);
        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        TextView timeView = ViewHolder.get(convertView, R.id.tv_time);

        RefereeUserModel model = mList.get(position);
        if (model != null) {
            Glide.with(mContext)
                    .load(model.headPic)
                    .error(R.mipmap.icon_comment_head)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(headerView);
            nameView.setText(model.name);
            timeView.setText("加入时间：" + model.joinTime);
        }

        return convertView;
    }
}
