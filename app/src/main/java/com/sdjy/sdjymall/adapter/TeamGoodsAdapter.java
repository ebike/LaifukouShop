package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

        LinearLayout gradeLayout = ViewHolder.get(convertView, R.id.ll_grade);
        TextView gradeView = ViewHolder.get(convertView, R.id.tv_grade);
        TextView resumeView = ViewHolder.get(convertView, R.id.tv_resume);
        TextView peopleNumView = ViewHolder.get(convertView, R.id.tv_people_num);
        TextView totalView = ViewHolder.get(convertView, R.id.tv_total);

        TeamGoodsModel model = mList.get(position);
        if (model != null) {
            switch (model.grade) {
                case 1:
                    gradeLayout.setBackgroundResource(R.mipmap.bg_team_1);
                    break;
                case 2:
                    gradeLayout.setBackgroundResource(R.mipmap.bg_team_2);
                    break;
                case 3:
                    gradeLayout.setBackgroundResource(R.mipmap.bg_team_3);
                    break;
                case 4:
                    gradeLayout.setBackgroundResource(R.mipmap.bg_team_4);
                    break;
                case 5:
                    gradeLayout.setBackgroundResource(R.mipmap.bg_team_5);
                    break;
                default:
                    gradeLayout.setBackgroundResource(R.mipmap.bg_team_5);
                    break;
            }
            gradeView.setText(model.gradeStr);
            resumeView.setText(model.resume);
            peopleNumView.setText(model.joinNum + "人参与");
            totalView.setText("￥" + model.total);
        }

        return convertView;
    }
}
