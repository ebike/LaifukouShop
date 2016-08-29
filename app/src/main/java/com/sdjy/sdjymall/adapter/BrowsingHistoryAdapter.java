package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.GoodsBrowsingModel;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 浏览记录
 */
public class BrowsingHistoryAdapter extends TAdapter<GoodsBrowsingModel> {

    private ChangeSelectedCallback callback;
    private boolean isEdit;

    public BrowsingHistoryAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_browsing_history, parent, false);
        }

        ImageView chooseView = ViewHolder.get(convertView, R.id.iv_choose);
        ImageView picView = ViewHolder.get(convertView, R.id.iv_pic);
        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        TextView priceView = ViewHolder.get(convertView, R.id.tv_price);
        TextView timeView = ViewHolder.get(convertView, R.id.tv_time);

        final GoodsBrowsingModel model = mList.get(position);
        if (model != null) {
            if (isEdit) {
                chooseView.setVisibility(View.VISIBLE);
                if (model.isSelected) {
                    chooseView.setImageResource(R.mipmap.icon_circle_sel);
                } else {
                    chooseView.setImageResource(R.mipmap.icon_circle_nosel);
                }
                chooseView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(callback != null){
                            callback.onChanged(position);
                        }
                    }
                });
            } else {
                chooseView.setVisibility(View.GONE);
            }
            Glide.with(mContext)
                    .load(model.imageUrl)
                    .error(R.mipmap.img_goods_default)
                    .into(picView);
            nameView.setText(model.goodsName);
            if (model.priceType == 1) {
                priceView.setText("￥" + model.priceType);
            } else if (model.priceType == 2) {
                priceView.setText("￥" + model.priceType + " + 金币 " + model.priceType);
            } else if (model.priceType == 3) {
                priceView.setText("币 " + model.priceType);
            }
            timeView.setText(model.browseTime);
        }
        return convertView;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public interface ChangeSelectedCallback {
        void onChanged(int position);
    }

    public void setCallback(ChangeSelectedCallback callback) {
        this.callback = callback;
    }

}
