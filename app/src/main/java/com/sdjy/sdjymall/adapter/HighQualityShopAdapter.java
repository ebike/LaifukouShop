package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.HomePageDataItemModel;
import com.sdjy.sdjymall.util.StringUtils;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 优质商家
 */
public class HighQualityShopAdapter extends TAdapter<HomePageDataItemModel> {

    public HighQualityShopAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_high_quality_shop, parent, false);
        }

        TextView titleView = ViewHolder.get(convertView, R.id.tv_title);
        TextView contentView = ViewHolder.get(convertView, R.id.tv_content);
        ImageView pictureView = ViewHolder.get(convertView, R.id.iv_picture);
        TextView promotionView = ViewHolder.get(convertView, R.id.tv_promotion);

        HomePageDataItemModel model = mList.get(position);
        if (model != null) {
            titleView.setText(model.title);
            contentView.setText(model.content);
            if (!StringUtils.strIsEmpty(model.promotionType) && model.promotionType.equals("1")) {
                promotionView.setVisibility(View.VISIBLE);
            } else {
                promotionView.setVisibility(View.GONE);
            }
            Glide.with(mContext)
                    .load(model.imageUrl)
                    .placeholder(R.mipmap.icon_no_pic)
                    .error(R.mipmap.icon_no_pic)
                    .centerCrop()
                    .into(pictureView);
        }
        return convertView;
    }
}
