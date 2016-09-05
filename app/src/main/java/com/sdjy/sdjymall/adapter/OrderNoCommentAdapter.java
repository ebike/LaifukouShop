package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.GoodsItemModel;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 待付款
 */
public class OrderNoCommentAdapter extends TAdapter<GoodsItemModel> {

    public OrderNoCommentAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_order_nocomment, parent, false);
        }

        ImageView picView = ViewHolder.get(convertView, R.id.iv_pic);
        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        TextView standardView = ViewHolder.get(convertView, R.id.tv_standard);
        TextView evaluateView = ViewHolder.get(convertView, R.id.tv_evaluate);

        final GoodsItemModel model = mList.get(position);
        if (model != null) {
            Glide.with(mContext)
                    .load(model.pic)
                    .error(R.mipmap.img_goods_default)
                    .into(picView);
            nameView.setText(model.goodsName);
            standardView.setText("规格：" + model.standard);
            evaluateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        return convertView;
    }

}
