package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CarGoodsModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.GoodsUtils;
import com.sdjy.sdjymall.view.ViewHolder;

import io.realm.Realm;

/**
 * 购物车商品
 */
public class ShoppingCartGoodsAdapter extends TAdapter<CarGoodsModel> {

    private ChangeSelectedCallback callback;
    private boolean inEdit;
    private Realm realm;

    public ShoppingCartGoodsAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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

        final CarGoodsModel model = mList.get(position);
        if (model != null) {
            if (inEdit) {
                if (model.isSelectedInEdit()) {
                    chooseView.setImageResource(R.mipmap.icon_circle_sel);
                } else {
                    chooseView.setImageResource(R.mipmap.icon_circle_nosel);
                }
            } else {
                if (model.isSelected()) {
                    chooseView.setImageResource(R.mipmap.icon_circle_sel);
                } else {
                    chooseView.setImageResource(R.mipmap.icon_circle_nosel);
                }
            }
            Glide.with(mContext)
                    .load(model.getImageUrl())
                    .error(R.mipmap.img_goods_default)
                    .into(picView);
            nameView.setText(model.getGoodsName());
            standardView.setText("规格：" + model.getStandard());
            countView.setText(model.getNum() + "");
            priceView.setText(GoodsUtils.getPrice(model.getPriceType(), model));
            minusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intoCar(model, position, 0);
                }
            });
            plusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intoCar(model, position, 1);
                }
            });
            chooseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (StaticValues.userModel == null) {
                        realm.beginTransaction();
                    }
                    if (inEdit) {
                        if (model.isSelectedInEdit()) {
                            model.setSelectedInEdit(false);
                        } else {
                            model.setSelectedInEdit(true);
                        }
                    } else {
                        if (model.isSelected() || model.getGoodsPrices().stock == 0) {
                            model.setSelected(false);
                        } else {
                            model.setSelected(true);
                        }
                    }
                    if (StaticValues.userModel == null) {
                        realm.commitTransaction();
                    }
                    if (callback != null) {
                        callback.onChanged();
                    }
                }
            });

        }

        return convertView;
    }

    /**
     * 修改购物车商品个数
     *
     * @param type:0：减，1：加
     */
    private void intoCar(CarGoodsModel model, final int position, int type) {
        final int count = type == 0 ? model.getNum() - 1 : model.getNum() + 1;
        if (count == 0) {
            return;
        }
        if (StaticValues.userModel != null) {
            SubscriberOnNextListener listener = new SubscriberOnNextListener<HttpResult>() {
                @Override
                public void onNext(HttpResult httpResult) {
                    mList.get(position).setNum(count);
                    notifyDataSetChanged();
                }
            };
            HttpMethods.getInstance().updateCart(new ProgressSubscriber(listener, mContext), StaticValues.userModel.userId, model.getId(), model.getPriceId(), count);
        } else {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            mList.get(position).setNum(count);
            realm.copyToRealmOrUpdate(mList.get(position));
            realm.commitTransaction();
            realm.close();
            notifyDataSetChanged();
        }
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

    public void setRealm(Realm realm) {
        this.realm = realm;
    }
}
