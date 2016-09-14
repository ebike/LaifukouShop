package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.fragment.ShoppingCartFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.GoodsItemModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.GoodsUtils;
import com.sdjy.sdjymall.view.ViewHolder;

import de.greenrobot.event.EventBus;

/**
 * 订单商品
 */
public class OrderGoodsAdapter extends TAdapter<GoodsItemModel> {

    public OrderGoodsAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_order_goods, parent, false);
        }

        ImageView picView = ViewHolder.get(convertView, R.id.iv_pic);
        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        TextView countView = ViewHolder.get(convertView, R.id.tv_count);
        TextView standardView = ViewHolder.get(convertView, R.id.tv_standard);
        TextView priceView = ViewHolder.get(convertView, R.id.tv_price);
        ImageView intoCarView = ViewHolder.get(convertView, R.id.iv_into_car);

        final GoodsItemModel model = mList.get(position);
        if (model != null) {
            Glide.with(mContext)
                    .load(model.pic)
                    .error(R.mipmap.img_goods_default)
                    .into(picView);
            nameView.setText(model.goodsName);
            countView.setText("数量：" + model.num);
            standardView.setText("规格：" + model.standard);
            priceView.setText(GoodsUtils.getPrice(model.priceType, model));
            intoCarView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HttpMethods.getInstance().addToCart(new ProgressSubscriber(new SubscriberOnNextListener<HttpResult>() {
                        @Override
                        public void onNext(HttpResult httpResult) {
                            T.showShort(mContext, httpResult.message);
                            EventBus.getDefault().post(new RefreshEvent(ShoppingCartFragment.class.getSimpleName()));
                        }
                    }, mContext), StaticValues.userModel.userId, model.goodsId, model.priceId, 1);
                }
            });
        }

        return convertView;
    }
}
