package com.laifukou.laifukoushop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laifukou.laifukoushop.R;
import com.laifukou.laifukoushop.model.ResourceModel;
import com.laifukou.laifukoushop.view.ViewHolder;

/**
 * 首页导航
 */
public class NavigationAdapter extends TAdapter<ResourceModel> {

    public NavigationAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_navigation,parent,false);
        }

        ImageView iconView = ViewHolder.get(convertView,R.id.iv_icon);
        TextView nameView = ViewHolder.get(convertView,R.id.tv_name);

        ResourceModel model = mList.get(position);
        if(model != null){
            iconView.setImageResource(model.resourceId);
            nameView.setText(model.name);
        }
        return convertView;
    }
}
