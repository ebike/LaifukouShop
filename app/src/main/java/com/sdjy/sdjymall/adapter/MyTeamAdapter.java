package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.TeamModel;
import com.sdjy.sdjymall.view.ViewHolder;

/**
 * 我所在的团队
 */
public class MyTeamAdapter extends TAdapter<TeamModel> {

    public MyTeamAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_my_team, parent, false);
        }

        LinearLayout gradeLayout = ViewHolder.get(convertView, R.id.ll_grade);
        TextView gradeView = ViewHolder.get(convertView, R.id.tv_grade);
        TextView teamNameView = ViewHolder.get(convertView, R.id.tv_team_name);
        TextView creatorView = ViewHolder.get(convertView, R.id.tv_creator);
        TextView createTimeView = ViewHolder.get(convertView, R.id.tv_create_time);
        TextView totalView = ViewHolder.get(convertView, R.id.tv_total);

        TeamModel model = mList.get(position);
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
            teamNameView.setText(model.teamName);
            creatorView.setText("创建人：" + model.createUserName);
            createTimeView.setText("创建时间：" + model.joinTime);
            totalView.setText("总人数：" + model.totalUser + "人");
        }

        return convertView;
    }
}
