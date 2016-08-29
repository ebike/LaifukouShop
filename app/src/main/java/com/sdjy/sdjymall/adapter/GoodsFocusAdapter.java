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
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.GoodsBrowsingModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.GoodsUtils;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 商品关注
 */
public class GoodsFocusAdapter extends TAdapter<GoodsBrowsingModel> {

    private LongClickListener longClickListener;

    public GoodsFocusAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_goods_focus, parent, false);
        }

        ImageView chooseView = ViewHolder.get(convertView, R.id.iv_choose);
        ImageView picView = ViewHolder.get(convertView, R.id.iv_pic);
        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        TextView priceView = ViewHolder.get(convertView, R.id.tv_price);
        ImageView intoCarView = ViewHolder.get(convertView, R.id.iv_into_car);

        final GoodsBrowsingModel model = mList.get(position);
        if (model != null) {
            Glide.with(mContext)
                    .load(model.imageUrl)
                    .error(R.mipmap.img_goods_default)
                    .into(picView);
            nameView.setText(model.goodsName);
            priceView.setText(GoodsUtils.getPrice(model.priceType, model.goodsPrices.get(0)));
            intoCarView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HttpMethods.getInstance().addToCart(new ProgressSubscriber(new SubscriberOnNextListener<HttpResult>() {
                        @Override
                        public void onNext(HttpResult httpResult) {
                            T.showShort(mContext, httpResult.message);
                        }
                    }, mContext), StaticValues.userModel.userId, model.goodsId, model.goodsPrices.get(0).id, 1);
                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(longClickListener != null){
                        longClickListener.onLongClick();
                    }
                    return true;
                }
            });
        }

        return convertView;
    }

    public interface LongClickListener{
        void onLongClick();
    }

    public void setLongClickListener(LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}
