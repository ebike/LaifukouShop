package com.laifukou.laifukoushop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.laifukou.laifukoushop.R;
import com.laifukou.laifukoushop.model.HomePageDataItemModel;
import com.laifukou.laifukoushop.util.StringUtils;
import com.laifukou.laifukoushop.view.ViewHolder;

/**
 * 热门分类（上）
 */
public class SortsAdapter1 extends TAdapter<HomePageDataItemModel> {

    public SortsAdapter1(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_sorts1, parent, false);
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
                    .into(pictureView);
        }
        return convertView;
    }
}
