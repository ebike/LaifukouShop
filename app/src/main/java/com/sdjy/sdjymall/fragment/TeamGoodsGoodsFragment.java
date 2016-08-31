package com.sdjy.sdjymall.fragment;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.common.fragment.LazyFragment;
import com.sdjy.sdjymall.model.TeamGoodsModel;

import butterknife.Bind;

/**
 * 互助创业-套餐
 */
public class TeamGoodsGoodsFragment extends LazyFragment {

    @Bind(R.id.ll_grade)
    LinearLayout gradeLayout;
    @Bind(R.id.tv_grade)
    TextView gradeView;
    @Bind(R.id.tv_resume)
    TextView resumeView;

    private TeamGoodsModel teamGoodsModel;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_team_goods_goods);

        if (teamGoodsModel != null) {
            switch (teamGoodsModel.grade) {
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
            gradeView.setText(teamGoodsModel.gradeStr);
            resumeView.setText(teamGoodsModel.resume);
        }
    }

    public void setTeamGoodsModel(TeamGoodsModel teamGoodsModel) {
        this.teamGoodsModel = teamGoodsModel;
    }
}
