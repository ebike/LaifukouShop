package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.TeamGoodsModel;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 创业套餐
 */
public class TeamGoodsAdapter extends TAdapter<TeamGoodsModel> {

    public TeamGoodsAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_team_goods, parent, false);
        }

        TextView gradeView = ViewHolder.get(convertView, R.id.tv_grade);
        TextView resumeView = ViewHolder.get(convertView, R.id.tv_resume);
        TextView peopleNumView = ViewHolder.get(convertView, R.id.tv_people_num);
        TextView totalView = ViewHolder.get(convertView, R.id.tv_total);

        TeamGoodsModel model = mList.get(position);
        if (model != null) {
            gradeView.setText(model.grade);
            resumeView.setText(model.resume);
            totalView.setText("￥" + model.total);
        }

        return convertView;
    }
}
