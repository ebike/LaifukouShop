package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.sdjy.sdjymall.fragment.GoodsEvaluateFragment;
import com.sdjy.sdjymall.fragment.GoodsGoodsFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.GoodsInfoModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 商品详情
 */
public class GoodsInfoActivity extends BaseActivity {

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind({R.id.v_goods, R.id.v_detail, R.id.v_evaluate})
    View[] views;
    @Bind(R.id.tv_goods_focus)
    TextView goodsFocusView;

    private List<Fragment> fragmentList;
    private List<TabIndicator> tabIndicatorList;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private int currentFragment;

    private SubscriberOnNextListener<GoodsInfoModel> nextListener;
    private String goodsId;
    private GoodsInfoModel goodsInfoModel;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_goods_info);
    }

    @Override
    public void init() {
        goodsId = getIntent().getStringExtra("GoodsId");

        nextListener = new SubscriberOnNextListener<GoodsInfoModel>() {
            @Override
            public void onNext(GoodsInfoModel model) {
                goodsInfoModel = model;
                viewPager.setOffscreenPageLimit(3);
                tabIndicatorList = ViewPagerUtil.getTabIndicator(3);
                fragmentList = new ArrayList<>();
                GoodsGoodsFragment goodsFragment = new GoodsGoodsFragment();
                goodsFragment.setGoodsInfoModel(goodsInfoModel);
                fragmentList.add(goodsFragment);
                GoodsDetailFragment detailFragment = new GoodsDetailFragment();
                detailFragment.setData(goodsInfoModel.describ);
                fragmentList.add(detailFragment);
                GoodsEvaluateFragment evaluateFragment = new GoodsEvaluateFragment();
                evaluateFragment.setGoodsId(goodsInfoModel.id);
                fragmentList.add(evaluateFragment);
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

                if (!StringUtils.strIsEmpty(goodsInfoModel.collectId)) {
                    Drawable drawable = getResources().getDrawable(R.mipmap.icon_goodsinfo_iscollect);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    goodsFocusView.setCompoundDrawables(null, drawable, null, null);
                } else {
                    Drawable drawable = getResources().getDrawable(R.mipmap.icon_goodsinfo_collect);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    goodsFocusView.setCompoundDrawables(null, drawable, null, null);
                }
            }
        };
        HttpMethods.getInstance().findGoods(new ProgressSubscriber<GoodsInfoModel>(nextListener, this), goodsId);
    }

    @OnClick({R.id.rl_goods, R.id.rl_detail, R.id.rl_evaluate})
    public void onClickTab(View view) {
        switch (view.getId()) {
            case R.id.rl_goods:
                changeTab(0);
                break;
            case R.id.rl_detail:
                changeTab(1);
                break;
            case R.id.rl_evaluate:
                changeTab(2);
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

    @OnClick(R.id.tv_shop)
    public void shop() {
        Intent intent = new Intent(this, ShopInfoActivity.class);
        intent.putExtra("shopId", goodsInfoModel.shopId);
        startActivity(intent);
    }

    @OnClick(R.id.tv_goods_focus)
    public void goodsFocus() {
        if (StaticValues.userModel != null) {
            if (!StringUtils.strIsEmpty(goodsInfoModel.collectId)) {
                SubscriberOnNextListener listener = new SubscriberOnNextListener() {
                    @Override
                    public void onNext(Object o) {
                        T.showShort(GoodsInfoActivity.this, "取消关注");
                        Drawable drawable = getResources().getDrawable(R.mipmap.icon_goodsinfo_collect);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        goodsFocusView.setCompoundDrawables(null, drawable, null, null);
                    }
                };
                HttpMethods.getInstance().cancelCollect(new NoProgressSubscriber(listener, this), StaticValues.userModel.id, goodsInfoModel.collectId);
            } else {
                SubscriberOnNextListener<String> listener = new SubscriberOnNextListener<String>() {
                    @Override
                    public void onNext(String s) {
                        T.showShort(GoodsInfoActivity.this, "关注成功");
                        goodsInfoModel.collectId = s;
                        Drawable drawable = getResources().getDrawable(R.mipmap.icon_goodsinfo_iscollect);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        goodsFocusView.setCompoundDrawables(null, drawable, null, null);
                    }
                };
                HttpMethods.getInstance().userCollect(new NoProgressSubscriber<String>(listener, this), StaticValues.userModel.id, 1, goodsInfoModel.id);
            }
        } else {
            T.showShort(this, "使用关注功能需要先进行登录");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
