package com.sdjy.sdjymall.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.common.fragment.LazyFragment;
import com.sdjy.sdjymall.model.TeamGoodsModel;

import butterknife.Bind;

/**
 * 互助创业-套餐
 */
public class TeamGoodsGoodsFragment extends LazyFragment {

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
            gradeView.setText(teamGoodsModel.gradeStr);
            resumeView.setText(teamGoodsModel.resume);
        }
    }

    public void setTeamGoodsModel(TeamGoodsModel teamGoodsModel) {
        this.teamGoodsModel = teamGoodsModel;
    }
}
