package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.AddReceiveAddressActivity;
import com.sdjy.sdjymall.activity.ReceiveAddressActivity;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.AddressModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.view.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

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

        final AddressModel model = mList.get(position);
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
                isDefaultView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", model.id);
                        if (model.isDefault == 1) {
                            params.put("default", "2");
                        } else {
                            params.put("default", "1");
                        }
                        SubscriberOnNextListener listener = new SubscriberOnNextListener() {
                            @Override
                            public void onNext(Object o) {
                                T.showShort(mContext, "设置成功");
                                EventBus.getDefault().post(new RefreshEvent(ReceiveAddressActivity.class.getSimpleName()));
                            }
                        };
                        HttpMethods.getInstance().saveOrUpdateAddress(new ProgressSubscriber(listener, mContext), params);
                    }
                });
            }
            editView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AddReceiveAddressActivity.class);
                    intent.putExtra("addressModel", model);
                    mContext.startActivity(intent);
                }
            });
            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HttpMethods.getInstance().delAddress(new ProgressSubscriber(new SubscriberOnNextListener() {
                        @Override
                        public void onNext(Object o) {
                            T.showShort(mContext, "删除成功");
                            EventBus.getDefault().post(new RefreshEvent(ReceiveAddressActivity.class.getSimpleName()));
                        }
                    }, mContext), model.id);
                }
            });
        }

        return convertView;
    }
}
