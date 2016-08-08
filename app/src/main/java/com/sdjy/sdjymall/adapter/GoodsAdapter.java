package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.GoodsModel;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 商品
 */
public class GoodsAdapter extends TAdapter<GoodsModel> {

    public GoodsAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_goods, parent, false);
        }

        ImageView pictureView = ViewHolder.get(convertView, R.id.iv_picture);
        TextView titleView = ViewHolder.get(convertView, R.id.tv_title);
        TextView moneyView = ViewHolder.get(convertView, R.id.tv_money);
        TextView commentsCountView = ViewHolder.get(convertView, R.id.tv_comments_count);
        TextView commentsRateView = ViewHolder.get(convertView, R.id.tv_comments_rate);

        GoodsModel model = mList.get(position);
        if (model != null) {
            Glide.with(mContext)
                    .load(model.imageUrl)
                    .placeholder(R.mipmap.icon_no_pic)
                    .error(R.mipmap.icon_no_pic)
                    .centerCrop()
                    .into(pictureView);
            titleView.setText(model.goodsName);
            if ("1".equals(model.priceType)) {
                moneyView.setText("￥" + model.priceMoney);
            } else if ("2".equals(model.priceType)) {
                moneyView.setText("￥" + model.priceMoney + " + 金币 " + model.priceGoldCoin);
            } else if ("3".equals(model.priceType)) {
                moneyView.setText("币 " + model.priceCoin);
            }

        }
        return convertView;
    }
}
