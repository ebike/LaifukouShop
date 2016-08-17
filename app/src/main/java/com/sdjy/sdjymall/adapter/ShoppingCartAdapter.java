package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.CarShopModel;
import com.sdjy.sdjymall.view.ScrollListView;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 购物车商品
 */
public class ShoppingCartAdapter extends TAdapter<CarShopModel> {

    public ShoppingCartAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_shopping_cart, parent, false);
        }

        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        ScrollListView listView = ViewHolder.get(convertView, R.id.list_view);

        CarShopModel model = mList.get(position);
        if (model != null) {
            nameView.setText(model.getShopName());
            ShoppingCartGoodsAdapter adapter = new ShoppingCartGoodsAdapter(mContext);
            listView.setAdapter(adapter);
            adapter.setList(model.getGoods());
        }

        return convertView;
    }
}
