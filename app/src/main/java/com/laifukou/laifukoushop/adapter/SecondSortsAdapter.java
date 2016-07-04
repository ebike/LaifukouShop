package com.laifukou.laifukoushop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.laifukou.laifukoushop.R;
import com.laifukou.laifukoushop.model.CommonDataModel;
import com.laifukou.laifukoushop.view.ViewHolder;

/**
 * 二级分类
 */
public class SecondSortsAdapter extends TAdapter<CommonDataModel> {

    public SecondSortsAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_second_sorts, parent, false);
        }

        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        ImageView pictureView = ViewHolder.get(convertView, R.id.iv_picture);

        CommonDataModel model = mList.get(position);
        if (model != null) {
            nameView.setText(model.name);
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
