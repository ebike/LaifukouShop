package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.GoodsEvaluateModel;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 商品评价
 */
public class GoodsEvaluateAdapter extends TAdapter<GoodsEvaluateModel> {

    public GoodsEvaluateAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_goods_evaluate, parent, false);
        }

        ImageView headView = ViewHolder.get(convertView, R.id.iv_header);
        TextView nameView = ViewHolder.get(convertView, R.id.tv_name);
        TextView timaView = ViewHolder.get(convertView, R.id.tv_time);
        ImageView starView = ViewHolder.get(convertView, R.id.iv_star);
        TextView contentView = ViewHolder.get(convertView, R.id.tv_content);
        TextView standardView = ViewHolder.get(convertView, R.id.tv_standard);
        TextView buyTimeView = ViewHolder.get(convertView, R.id.tv_buy_time);

        GoodsEvaluateModel model = mList.get(position);
        if (model != null) {
            Glide.with(mContext)
                    .load(model.headPic)
                    .error(R.mipmap.icon_comment_head)
                    .into(headView);
            nameView.setText(model.logiName);
            timaView.setText(model.commentTime);
            switch (model.score) {
                case 1:
                    starView.setImageResource(R.mipmap.icon_star_1);
                    break;
                case 2:
                    starView.setImageResource(R.mipmap.icon_star_2);
                    break;
                case 3:
                    starView.setImageResource(R.mipmap.icon_star_3);
                    break;
                case 4:
                    starView.setImageResource(R.mipmap.icon_star_4);
                    break;
                case 5:
                    starView.setImageResource(R.mipmap.icon_star_5);
                    break;
                default:
                    starView.setImageResource(R.mipmap.icon_star_0);
                    break;
            }
            contentView.setText(model.content);
            standardView.setText(model.standard);
            buyTimeView.setText(model.buyTime);
        }

        return convertView;
    }
}
