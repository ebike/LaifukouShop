package com.laifukou.laifukoushop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.laifukou.laifukoushop.R;
import com.laifukou.laifukoushop.model.ShopModel;
import com.laifukou.laifukoushop.view.ViewHolder;

/**
 * 商家
 */
public class ShopAdapter extends TAdapter<ShopModel> {

    public ShopAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_shop, parent, false);
        }

        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        TextView descView = ViewHolder.get(convertView, R.id.tv_desc);
        ImageView pictureView = ViewHolder.get(convertView, R.id.iv_picture);
        ImageView hotView = ViewHolder.get(convertView, R.id.iv_hot);

        ShopModel model = mList.get(position);
        if (model != null) {
            nameView.setText(model.shopName);
            descView.setText(model.title);
            Glide.with(mContext)
                    .load(model.cover)
                    .placeholder(R.mipmap.icon_no_pic)
                    .error(R.mipmap.icon_no_pic)
                    .centerCrop()
                    .into(pictureView);
        }
        return convertView;
    }
}
