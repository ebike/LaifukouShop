package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.CarGoodsModel;
import com.sdjy.sdjymall.model.CarShopModel;
import com.sdjy.sdjymall.util.CartUtils;
import com.sdjy.sdjymall.view.ScrollListView;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 购物车商品
 */
public class ShoppingCartAdapter extends TAdapter<CarShopModel> {

    private ChangeSelectedCallback callback;
    private boolean inEdit;

    public ShoppingCartAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_shopping_cart, parent, false);
        }

        ImageView selectedView = ViewHolder.get(convertView, R.id.iv_selected);
        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        ScrollListView listView = ViewHolder.get(convertView, R.id.list_view);

        final CarShopModel model = mList.get(position);
        if (model != null) {
            nameView.setText(model.getShopName());
            if (inEdit) {
                if (model.isSelectedInEdit()) {
                    selectedView.setImageResource(R.mipmap.icon_circle_sel);
                } else {
                    selectedView.setImageResource(R.mipmap.icon_circle_nosel);
                }
            } else {
                if (model.isSelected()) {
                    selectedView.setImageResource(R.mipmap.icon_circle_sel);
                } else {
                    selectedView.setImageResource(R.mipmap.icon_circle_nosel);
                }
            }
            ShoppingCartGoodsAdapter adapter = new ShoppingCartGoodsAdapter(mContext);
            adapter.setInEdit(inEdit);
            adapter.setCallback(new ShoppingCartGoodsAdapter.ChangeSelectedCallback() {
                @Override
                public void onChanged() {
                    if (inEdit) {
                        boolean isAllSelected = CartUtils.isAllSelectedInEdit(model);
                        if (isAllSelected) {
                            model.setSelectedInEdit(true);
                        } else {
                            model.setSelectedInEdit(false);
                        }
                    } else {
                        boolean isAllSelected = CartUtils.isAllSelected(model);
                        if (isAllSelected) {
                            model.setSelected(true);
                        } else {
                            model.setSelected(false);
                        }
                    }
                    notifyDataSetChanged();
                    if (callback != null) {
                        callback.onChanged();
                    }
                }
            });
            listView.setAdapter(adapter);
            adapter.setList(model.getGoods());
            selectedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (inEdit) {
                        boolean isAllSelected = CartUtils.isAllSelectedInEdit(model);
                        if (isAllSelected) {
                            model.setSelectedInEdit(false);
                            for (CarGoodsModel goodsModel : model.getGoods()) {
                                goodsModel.setSelectedInEdit(false);
                            }
                        } else {
                            model.setSelectedInEdit(true);
                            for (CarGoodsModel goodsModel : model.getGoods()) {
                                goodsModel.setSelectedInEdit(true);
                            }
                        }
                    } else {
                        boolean isAllSelected = CartUtils.isAllSelected(model);
                        if (isAllSelected) {
                            model.setSelected(false);
                            for (CarGoodsModel goodsModel : model.getGoods()) {
                                goodsModel.setSelected(false);
                            }
                        } else {
                            model.setSelected(true);
                            for (CarGoodsModel goodsModel : model.getGoods()) {
                                goodsModel.setSelected(true);
                            }
                        }
                    }
                    notifyDataSetChanged();
                    if (callback != null) {
                        callback.onChanged();
                    }
                }
            });
        }

        return convertView;
    }

    public interface ChangeSelectedCallback {
        void onChanged();
    }

    public void setCallback(ChangeSelectedCallback callback) {
        this.callback = callback;
    }

    public void setInEdit(boolean inEdit) {
        this.inEdit = inEdit;
    }
}
