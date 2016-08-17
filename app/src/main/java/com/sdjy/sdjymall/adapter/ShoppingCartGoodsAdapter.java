package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.CarGoodsModel;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 购物车商品
 */
public class ShoppingCartGoodsAdapter extends TAdapter<CarGoodsModel> {

    public ShoppingCartGoodsAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_shopping_cart_goods, parent, false);
        }

        ImageView chooseView = ViewHolder.get(convertView, R.id.iv_choose);
        ImageView picView = ViewHolder.get(convertView, R.id.iv_pic);
        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        TextView standardView = ViewHolder.get(convertView, R.id.tv_standard);
        TextView minusView = ViewHolder.get(convertView, R.id.tv_minus);
        TextView countView = ViewHolder.get(convertView, R.id.tv_count);
        TextView plusView = ViewHolder.get(convertView, R.id.tv_plus);
        TextView priceView = ViewHolder.get(convertView, R.id.tv_price);

        CarGoodsModel model = mList.get(position);
        if (model != null) {
            Glide.with(mContext)
                    .load(model.getImageUrl())
                    .error(R.mipmap.img_goods_default)
                    .into(picView);
            nameView.setText(model.getGoodsName());
            standardView.setText("规格：" + model.getStardand());
            countView.setText(model.getNum() + "");
            if (model.getPriceType() == 1) {
                priceView.setText("￥" + model.getPriceMoney());
            } else if (model.getPriceType() == 2) {
                priceView.setText("￥" + model.getPriceMoney() + " + 金币 " + model.getPriceGoldCoin());
            } else if (model.getPriceType() == 3) {
                priceView.setText("币 " + model.getPriceCoin());
            }
        }

        return convertView;
    }
}
