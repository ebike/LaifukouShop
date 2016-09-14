package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.OrderActivity;
import com.sdjy.sdjymall.activity.OrderInfoActivity;
import com.sdjy.sdjymall.activity.ShopInfoActivity;
import com.sdjy.sdjymall.common.util.DensityUtils;
import com.sdjy.sdjymall.common.util.ScreenUtils;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.http.CommonMethods;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.OrderModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.GoodsUtils;
import com.sdjy.sdjymall.view.ViewHolder;

import de.greenrobot.event.EventBus;

/**
 * 订单列表
 */
public class OrderAdapter extends TAdapter<OrderModel> {

    public OrderAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_order, parent, false);
        }

        TextView shopNameView = ViewHolder.get(convertView, R.id.tv_shop_name);
        TextView cancelView = ViewHolder.get(convertView, R.id.tv_cancel);
        TextView payView = ViewHolder.get(convertView, R.id.tv_pay);
        TextView deliveryView = ViewHolder.get(convertView, R.id.tv_delivery);
        TextView submitView = ViewHolder.get(convertView, R.id.tv_submit);
        ImageView deleteView = ViewHolder.get(convertView, R.id.iv_delete);
        LinearLayout picsLayout = ViewHolder.get(convertView, R.id.ll_pics);
        TextView totalGoodsView = ViewHolder.get(convertView, R.id.tv_total_goods);
        TextView amountView = ViewHolder.get(convertView, R.id.tv_amount);

        final OrderModel model = mList.get(position);
        if (model != null) {
            if (model.shopType == 1) {
                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_order_jy);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                shopNameView.setCompoundDrawables(drawable, null, null, null);
            } else {
                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_order_shop);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                Drawable drawable1 = mContext.getResources().getDrawable(R.mipmap.navigation);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                shopNameView.setCompoundDrawables(drawable, null, drawable1, null);
                shopNameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ShopInfoActivity.class);
                        intent.putExtra("shopId", model.shopId);
                        mContext.startActivity(intent);
                    }
                });
            }
            shopNameView.setText(model.shopName);
            showViewByState(model.state, cancelView, payView, deliveryView, submitView, deleteView);
            if (model.pics != null && model.pics.size() > 0) {
                for (String pic : model.pics) {
                    ImageView imageView = new ImageView(mContext);
                    Glide.with(mContext)
                            .load(pic)
                            .error(R.mipmap.img_goods_default)
                            .into(imageView);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dp2px(mContext, 70), DensityUtils.dp2px(mContext, 70));
                    params.setMargins(0, 0, DensityUtils.dp2px(mContext, 12), 0);
                    picsLayout.addView(imageView, params);
                }

                //计算商品图片是否超出屏幕，如果没有，需要补充剩余部分，为解决HorizontalScrollView点击无效
                int width = ScreenUtils.getScreenWidth(mContext) - model.pics.size() * DensityUtils.dp2px(mContext, 82) - 2 * DensityUtils.dp2px(mContext, 12);
                if (width >= 0) {
                    LinearLayout layout = new LinearLayout(mContext);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(width, DensityUtils.dp2px(mContext, 70));
                    picsLayout.addView(layout, params1);
                }
            }

            totalGoodsView.setText(model.totalGoods + "");
            amountView.setText(GoodsUtils.formatPrice(model.money, model.goldCoin, model.coin));
            picsLayout.setOnClickListener(new OrderClick(model.orderId));
            convertView.setOnClickListener(new OrderClick(model.orderId));

            cancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpMethods.getInstance().updateOrderState(new ProgressSubscriber(new SubscriberOnNextListener() {
                        @Override
                        public void onNext(Object o) {
                            T.showShort(mContext, "取消成功");
                            EventBus.getDefault().post(new RefreshEvent(OrderActivity.class.getSimpleName()));
                        }
                    }, mContext), model.orderId, "6");
                }
            });
            payView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonMethods.toPayOrder(mContext, model.orderId);
                }
            });
            submitView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpMethods.getInstance().updateOrderState(new ProgressSubscriber(new SubscriberOnNextListener() {
                        @Override
                        public void onNext(Object o) {
                            T.showShort(mContext, "确认收货成功");
                            EventBus.getDefault().post(new RefreshEvent(OrderActivity.class.getSimpleName()));
                        }
                    }, mContext), model.orderId, "9");
                }
            });
            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpMethods.getInstance().updateOrderState(new ProgressSubscriber(new SubscriberOnNextListener() {
                        @Override
                        public void onNext(Object o) {
                            T.showShort(mContext, "删除成功");
                            EventBus.getDefault().post(new RefreshEvent(OrderActivity.class.getSimpleName()));
                        }
                    }, mContext), model.orderId, "9");
                }
            });
        }

        return convertView;
    }

    private void showViewByState(int state, TextView cancelView, TextView payView,
                                 TextView deliveryView, TextView submitView, ImageView deleteView) {
        switch (state) {
            case 1:
                cancelView.setVisibility(View.VISIBLE);
                payView.setVisibility(View.VISIBLE);
                deliveryView.setVisibility(View.GONE);
                submitView.setVisibility(View.GONE);
                deleteView.setVisibility(View.GONE);
                break;
            case 3:
                cancelView.setVisibility(View.GONE);
                payView.setVisibility(View.GONE);
                deliveryView.setVisibility(View.VISIBLE);
                submitView.setVisibility(View.GONE);
                deleteView.setVisibility(View.GONE);
                break;
            case 4:
                cancelView.setVisibility(View.GONE);
                payView.setVisibility(View.GONE);
                deliveryView.setVisibility(View.GONE);
                submitView.setVisibility(View.VISIBLE);
                deleteView.setVisibility(View.GONE);
                break;
            case 5:
                cancelView.setVisibility(View.GONE);
                payView.setVisibility(View.GONE);
                deliveryView.setVisibility(View.GONE);
                submitView.setVisibility(View.GONE);
                deleteView.setVisibility(View.VISIBLE);
                break;
            default:
                cancelView.setVisibility(View.GONE);
                payView.setVisibility(View.GONE);
                deliveryView.setVisibility(View.GONE);
                submitView.setVisibility(View.GONE);
                deleteView.setVisibility(View.GONE);
                break;
        }
    }

    class OrderClick implements View.OnClickListener {
        private String orderId;

        public OrderClick(String orderId) {
            this.orderId = orderId;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, OrderInfoActivity.class);
            intent.putExtra("orderId", orderId);
            mContext.startActivity(intent);
        }
    }
}
