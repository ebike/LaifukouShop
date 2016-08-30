package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.ShopModel;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 商家关注
 */
public class ShopFocusAdapter extends TAdapter<ShopModel> {

    private LongClickListener longClickListener;

    public ShopFocusAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_shop_focus, parent, false);
        }

        ImageView logoView = ViewHolder.get(convertView, R.id.iv_logo);
        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        TextView shopTypeView = ViewHolder.get(convertView, R.id.tv_shop_type);
        TextView focusCountView = ViewHolder.get(convertView, R.id.tv_focus_count);

        final ShopModel model = mList.get(position);
        if (model != null) {
            Glide.with(mContext)
                    .load(model.logo)
                    .centerCrop()
                    .error(R.mipmap.icon_shop_logo)
                    .into(logoView);
            nameView.setText(model.shopName);
            if (model.shopType == 1) {
                shopTypeView.setText("自营商家");
            } else if (model.shopType == 2) {
                shopTypeView.setText("联盟商家");
            }
            focusCountView.setText(model.collectNum + "人关注");
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (longClickListener != null) {
                        longClickListener.onLongClick(model);
                    }
                    return true;
                }
            });
        }

        return convertView;
    }

    public interface LongClickListener {
        void onLongClick(ShopModel model);
    }

    public void setLongClickListener(LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}
