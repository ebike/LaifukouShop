package com.sdjy.sdjymall.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.adapter.ViewPagerFragmentAdapter;
import com.sdjy.sdjymall.common.model.TabIndicator;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.common.util.ViewPagerUtil;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.fragment.GoodsDetailFragment;
import com.sdjy.sdjymall.fragment.TeamGoodsGoodsFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.TeamGoodsModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.view.CreateOrAddTeamDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class TeamGoodsInfoActivity extends BaseActivity {

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind({R.id.v_goods, R.id.v_detail})
    View[] views;
    @Bind(R.id.tv_amount)
    TextView amountView;

    private List<Fragment> fragmentList;
    private List<TabIndicator> tabIndicatorList;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private int currentFragment;
    private TeamGoodsModel teamGoodsModel;
    private String id;
    private CreateOrAddTeamDialog dialog;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_team_goods_info);
    }

    @Override
    public void init() {
        id = getIntent().getStringExtra("id");

        SubscriberOnNextListener<TeamGoodsModel> listener = new SubscriberOnNextListener<TeamGoodsModel>() {
            @Override
            public void onNext(TeamGoodsModel model) {
                teamGoodsModel = model;
                amountView.setText("￥" + teamGoodsModel.total);
                viewPager.setOffscreenPageLimit(2);
                tabIndicatorList = ViewPagerUtil.getTabIndicator(2);
                fragmentList = new ArrayList<>();
                TeamGoodsGoodsFragment goodsFragment = new TeamGoodsGoodsFragment();
                goodsFragment.setTeamGoodsModel(teamGoodsModel);
                fragmentList.add(goodsFragment);
                GoodsDetailFragment detailFragment = new GoodsDetailFragment();
                detailFragment.setData(teamGoodsModel.description);
                fragmentList.add(detailFragment);
                // 设置ViewPager适配器
                viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), tabIndicatorList, fragmentList);
                viewPager.setAdapter(viewPagerFragmentAdapter);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        changeTab(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        };
        HttpMethods.getInstance().findTeamGoods(new ProgressSubscriber<TeamGoodsModel>(listener, this), id);
    }

    @OnClick({R.id.rl_goods, R.id.rl_detail})
    public void onClickTab(View view) {
        switch (view.getId()) {
            case R.id.rl_goods:
                changeTab(0);
                break;
            case R.id.rl_detail:
                changeTab(1);
                break;
        }
        viewPager.setCurrentItem(currentFragment);
    }

    private void changeTab(int position) {
        currentFragment = position;
        for (int i = 0; i < views.length; i++) {
            if (i == position) {
                views[i].setVisibility(View.VISIBLE);
            } else {
                views[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_join)
    public void join() {
        if (StaticValues.userModel != null) {
            if (dialog == null) {
                dialog = new CreateOrAddTeamDialog(this, teamGoodsModel.id)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true);
            }
            dialog.show();
        } else {
            T.showShort(this, "请先登录，再参与团队");
        }
    }

}
