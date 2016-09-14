package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.common.adapter.AutoRVAdapter;
import com.sdjy.sdjymall.common.adapter.ViewHolder;
import com.sdjy.sdjymall.model.GoodsModel;
import com.sdjy.sdjymall.util.GoodsUtils;

import java.util.List;

/**
 * 商品列表
 */
public class GoodsRVAdapter extends AutoRVAdapter<GoodsModel> {

    public GoodsRVAdapter(Context context, List<GoodsModel> list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.adapter_goods;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        LinearLayout rootLayout = holder.get(R.id.ll_root);
        ImageView pictureView = holder.get(R.id.iv_picture);
        TextView titleView = holder.get(R.id.tv_title);
        TextView moneyView = holder.get(R.id.tv_money);
        TextView commentsCountView = holder.get(R.id.tv_comments_count);
        TextView commentsRateView = holder.get(R.id.tv_comments_rate);

        final GoodsModel model = list.get(position);
        if (model != null) {
            Glide.with(context)
                    .load(model.imageUrl)
                    .placeholder(R.mipmap.icon_no_pic)
                    .error(R.mipmap.icon_no_pic)
                    .centerCrop()
                    .into(pictureView);
            titleView.setText(model.goodsName);
            moneyView.setText(GoodsUtils.getPrice(model.priceType, model));
            commentsCountView.setText(model.commentNum + "条评论");
            commentsRateView.setText("好评" + model.praiseRate);
            rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rvItemClickListener != null) {
                        rvItemClickListener.onItemClick(position, model, v);
                    }
                }
            });
        }
    }
}
