package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.AddressModel;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 收货地址
 */
public class ReceiveAddressAdapter extends TAdapter<AddressModel> {

    public ReceiveAddressAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_receive_address, parent, false);
        }

        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        TextView phoneView = ViewHolder.get(convertView, R.id.tv_phone);
        TextView addressView = ViewHolder.get(convertView, R.id.tv_address);
        TextView isDefaultView = ViewHolder.get(convertView, R.id.tv_is_default);
        TextView deleteView = ViewHolder.get(convertView, R.id.tv_delete);
        TextView editView = ViewHolder.get(convertView, R.id.tv_edit);

        AddressModel model = mList.get(position);
        if (model != null) {
            nameView.setText(model.consignee);
            phoneView.setText(model.mobile);
            addressView.setText(model.province + model.city + model.area + model.address);
            if (model.isDefault == 1) {
                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_circle_sel);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                isDefaultView.setCompoundDrawables(drawable, null, null, null);
            } else {
                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_circle_nosel);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                isDefaultView.setCompoundDrawables(drawable, null, null, null);
            }
        }

        return convertView;
    }
}
