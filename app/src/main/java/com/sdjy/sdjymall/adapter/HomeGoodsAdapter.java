package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.HomePageDataItemModel;
import com.sdjy.sdjymall.util.GoodsUtils;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 热销商品
 */
public class HomeGoodsAdapter extends TAdapter<HomePageDataItemModel> {

    public HomeGoodsAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_goods, parent, false);
        }

        ImageView pictureView = ViewHolder.get(convertView, R.id.iv_picture);
        TextView titleView = ViewHolder.get(convertView, R.id.tv_title);
        TextView moneyView = ViewHolder.get(convertView, R.id.tv_money);

        HomePageDataItemModel model = mList.get(position);
        if (model != null) {
            titleView.setText(model.title);
            moneyView.setText(GoodsUtils.getPrice(model.priceType, model));
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
