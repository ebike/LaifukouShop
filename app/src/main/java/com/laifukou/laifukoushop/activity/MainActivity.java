package com.laifukou.laifukoushop.activity;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.laifukou.laifukoushop.common.adapter.ViewPagerFragmentAdapter;
import com.laifukou.laifukoushop.common.model.TabIndicator;
import com.laifukou.laifukoushop.common.util.ViewPagerUtil;
import com.laifukou.laifukoushop.R;
import com.laifukou.laifukoushop.activity.base.BaseActivity;
import com.laifukou.laifukoushop.fragment.HomeFragment;
import com.laifukou.laifukoushop.fragment.MyFragment;
import com.laifukou.laifukoushop.fragment.RecommendedFragment;
import com.laifukou.laifukoushop.fragment.ShoppingCartFragment;
import com.laifukou.laifukoushop.fragment.SortFragment;
import com.laifukou.laifukoushop.view.NotSlideViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.view_pager)
    NotSlideViewPager viewPager;

    @Bind({R.id.tv_home, R.id.tv_sorts, R.id.tv_recommend, R.id.tv_cart, R.id.tv_my})
    TextView[] tabViews;

    private List<Fragment> fragmentList;
    private List<TabIndicator> tabIndicatorList;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private int currentFragment;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void init() {
        viewPager.setOffscreenPageLimit(5);
        tabIndicatorList = ViewPagerUtil.getTabIndicator(5);
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new SortFragment());
        fragmentList.add(new RecommendedFragment());
        fragmentList.add(new ShoppingCartFragment());
        fragmentList.add(new MyFragment());
        // 设置ViewPager适配器
        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), tabIndicatorList, fragmentList);
        viewPager.setAdapter(viewPagerFragmentAdapter);
    }

    @OnClick({R.id.ll_home,
            R.id.ll_sorts,
            R.id.ll_recommend,
            R.id.ll_cart,
            R.id.ll_my})
    public void onClickTab(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                currentFragment = 0;
                break;
            case R.id.ll_sorts:
                currentFragment = 1;
                break;
            case R.id.ll_recommend:
                currentFragment = 2;
                break;
            case R.id.ll_cart:
                currentFragment = 3;
                break;
            case R.id.ll_my:
                currentFragment = 4;
                break;
        }
        ViewPagerUtil.changeBottomTab(this, currentFragment, tabViews);
        viewPager.setCurrentItem(currentFragment);
    }

}