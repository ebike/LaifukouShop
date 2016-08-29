package com.sdjy.sdjymall.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.adapter.ViewPagerFragmentAdapter;
import com.sdjy.sdjymall.common.model.TabIndicator;
import com.sdjy.sdjymall.common.util.ViewPagerUtil;
import com.sdjy.sdjymall.fragment.GoodsFocusFragment;
import com.sdjy.sdjymall.fragment.ShopFocusFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class GoodsShopFocusActivity extends BaseActivity {

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind({R.id.v_goods, R.id.v_shop})
    View[] views;

    private List<Fragment> fragmentList;
    private List<TabIndicator> tabIndicatorList;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private int currentFragment;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_goods_shop_focus);
    }

    @Override
    public void init() {
        currentFragment = getIntent().getIntExtra("currentFragment", 0);

        viewPager.setOffscreenPageLimit(2);
        tabIndicatorList = ViewPagerUtil.getTabIndicator(2);
        fragmentList = new ArrayList<>();
        fragmentList.add(new GoodsFocusFragment());
        fragmentList.add(new ShopFocusFragment());
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
        changeTab(currentFragment);
        viewPager.setCurrentItem(currentFragment);
    }

    @OnClick({R.id.rl_goods, R.id.rl_shop})
    public void onClickTab(View view) {
        switch (view.getId()) {
            case R.id.rl_goods:
                changeTab(0);
                break;
            case R.id.rl_shop:
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
}
