package com.laifukou.laifukoushop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.laifukou.laifukoushop.R;
import com.laifukou.laifukoushop.model.HomePageDataItemModel;
import com.laifukou.laifukoushop.view.ViewHolder;

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
            if ("1".equals(model.priceType)) {
                moneyView.setText("￥" + model.priceMoney);
            } else if ("2".equals(model.priceType)) {
                moneyView.setText("￥" + model.priceMoney + " + 金币 " + model.priceGoldCoin);
            } else if ("3".equals(model.priceType)) {
                moneyView.setText("币 " + model.priceCoin);
            }

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
