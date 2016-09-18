package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.adapter.ViewPagerFragmentAdapter;
import com.sdjy.sdjymall.common.model.TabIndicator;
import com.sdjy.sdjymall.common.util.DensityUtils;
import com.sdjy.sdjymall.common.util.DialogUtils;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.common.util.ViewPagerUtil;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.fragment.ShopGoodsFragment;
import com.sdjy.sdjymall.fragment.ShopHomeFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.ShopHotGroupModel;
import com.sdjy.sdjymall.model.ShopModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.CommonUtils;
import com.sdjy.sdjymall.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 店铺首页
 */
public class ShopInfoActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.iv_logo)
    ImageView logoView;
    @Bind(R.id.tv_name)
    TextView nameView;
    @Bind(R.id.tv_shop_type)
    TextView shopTypeView;
    @Bind(R.id.tv_focus_count)
    TextView focusCountView;
    @Bind(R.id.tv_focus)
    TextView focusView;
    @Bind(R.id.tv_cus_phone)
    TextView cusPhoneView;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.iv_home)
    ImageView homeView;
    @Bind({R.id.tv_all_count, R.id.tv_hot_count, R.id.tv_new_count})
    TextView[] countViews;
    @Bind({R.id.tv_home, R.id.tv_all_label, R.id.tv_hot_label, R.id.tv_new_label})
    TextView[] labelViews;
    @Bind(R.id.ll_order)
    LinearLayout orderLayout;
    @Bind(R.id.tv_recommend)
    TextView recommendView;
    @Bind(R.id.tv_sales)
    TextView salesView;
    @Bind(R.id.tv_price)
    TextView priceView;
    @Bind(R.id.iv_up_arrow)
    ImageView upArrowView;
    @Bind(R.id.iv_down_arrow)
    ImageView downArrowView;

    private String shopId;
    private ShopModel shopModel;
    private SubscriberOnNextListener<ShopModel> nextListener;
    private List<Fragment> fragmentList;
    private List<TabIndicator> tabIndicatorList;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private int currentFragment;
    private PopupWindow popupWindow;
    private View popupView;
    private List<ShopHotGroupModel> hotGroupList;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_shop_info);
    }

    @Override
    public void init() {
        shopId = getIntent().getStringExtra("shopId");

        nextListener = new SubscriberOnNextListener<ShopModel>() {
            @Override
            public void onNext(ShopModel model) {
                shopModel = model;
                titleView.setText(shopModel.shopName);
                initView();
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("id", shopId);
        if (StaticValues.userModel != null) {
            params.put("userId", StaticValues.userModel.userId);
        }
        HttpMethods.getInstance().findShop(new ProgressSubscriber<ShopModel>(nextListener, this), params);

        initPagerView();

        initHotGroup();
    }

    private void initHotGroup() {
        popupView = LayoutInflater.from(ShopInfoActivity.this).inflate(R.layout.view_shop_hot_group, null);
        final LinearLayout listView = (LinearLayout) popupView.findViewById(R.id.ll_root);

        HttpMethods.getInstance().findShopHotGroup(new NoProgressSubscriber<List<ShopHotGroupModel>>(new SubscriberOnNextListener<List<ShopHotGroupModel>>() {
            @Override
            public void onNext(List<ShopHotGroupModel> shopHotGroupModels) {
                if (shopHotGroupModels != null) {
                    hotGroupList = shopHotGroupModels;
                    for (int i = 0; i < hotGroupList.size(); i++) {
                        final ShopHotGroupModel model = hotGroupList.get(i);
                        TextView textView = new TextView(ShopInfoActivity.this);
                        textView.setTextAppearance(ShopInfoActivity.this, R.style.gray_16);
                        textView.setText(model.groupName);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        int p16 = DensityUtils.dp2px(ShopInfoActivity.this, 16);
                        int p9 = DensityUtils.dp2px(ShopInfoActivity.this, 9);
                        textView.setPadding(p16, p9, p16, p9);
                        if (i != hotGroupList.size() - 1) {
                            textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_bottom_line));
                        }
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent = new Intent(ShopInfoActivity.this, ShopHotGroupGoodsActivity.class);
                                intent.putExtra("shopId", shopId);
                                intent.putExtra("groupId", model.groupId);
                                intent.putExtra("groupName", model.groupName);
                                startActivity(intent);
                            }
                        });
                        listView.addView(textView);

//                        if (i != hotGroupList.size() - 1) {
//                            TextView lineView = new TextView(ShopInfoActivity.this);
//                            lineView.setTextAppearance(ShopInfoActivity.this, R.style.line_1);
//                            lineView.setBackgroundColor(getResources().getColor(R.color.main_line));
//                            listView.addView(lineView);
//                        }
                    }
                    popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                }

            }
        }, this), shopId);
    }

    private void initPagerView() {
        viewPager.setOffscreenPageLimit(4);
        tabIndicatorList = ViewPagerUtil.getTabIndicator(4);
        fragmentList = new ArrayList<>();
        ShopHomeFragment shopHomeFragment = new ShopHomeFragment();
        fragmentList.add(shopHomeFragment);
        ShopGoodsFragment allFragment = new ShopGoodsFragment();
        allFragment.setShopId(shopId);
        allFragment.setSortTerm("2");
        fragmentList.add(allFragment);
        ShopGoodsFragment hotFragment = new ShopGoodsFragment();
        hotFragment.setShopId(shopId);
        hotFragment.setPageSorts("11");
        fragmentList.add(hotFragment);
        ShopGoodsFragment newFragment = new ShopGoodsFragment();
        newFragment.setShopId(shopId);
        newFragment.setPageSorts("12");
        fragmentList.add(newFragment);
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

    private void initView() {
        Glide.with(this)
                .load(shopModel.logo)
                .error(R.mipmap.icon_shop_logo)
                .centerCrop()
                .into(logoView);
        nameView.setText(shopModel.shopName);
        if (shopModel.shopType == 1) {
            shopTypeView.setText("自营商家");
        } else if (shopModel.shopType == 2) {
            shopTypeView.setText("联盟商家");
        }
        focusCountView.setText(shopModel.collectNum + "人关注");
        if (!StringUtils.strIsEmpty(shopModel.collectId)) {//已关注
            focusView.setText("已关注");
            focusView.setTextColor(getResources().getColor(R.color.red1));
            focusView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white));
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_shop_unfocus);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            focusView.setCompoundDrawables(drawable, null, null, null);
        } else {//未关注
            focusView.setText("关注");
            focusView.setTextColor(getResources().getColor(R.color.white));
            focusView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red1));
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_shop_focus);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            focusView.setCompoundDrawables(drawable, null, null, null);
        }
        if (!StringUtils.strIsEmpty(shopModel.cusPhone)) {
            cusPhoneView.setText("联系客服");
        } else {
            cusPhoneView.setText("暂无客服");
        }

        ((ShopHomeFragment) fragmentList.get(0)).setData(shopModel.describ);
        countViews[0].setText(shopModel.goodsNum);
        countViews[1].setText(shopModel.hotGoodsNum);
        countViews[2].setText(shopModel.newGoodsNum);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_focus)
    public void focus() {
        if (StaticValues.userModel != null) {
            if (!StringUtils.strIsEmpty(shopModel.collectId)) {
                SubscriberOnNextListener listener = new SubscriberOnNextListener() {
                    @Override
                    public void onNext(Object o) {
                        T.showShort(ShopInfoActivity.this, "取消关注");
                        shopModel.collectId = null;
                        focusView.setText("关注");
                        focusView.setTextColor(getResources().getColor(R.color.white));
                        focusView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red1));
                        Drawable drawable = getResources().getDrawable(R.mipmap.icon_shop_focus);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        focusView.setCompoundDrawables(drawable, null, null, null);
                    }
                };
                HttpMethods.getInstance().cancelCollect(new NoProgressSubscriber(listener, this), StaticValues.userModel.userId, shopModel.collectId);
            } else {
                SubscriberOnNextListener<String> listener = new SubscriberOnNextListener<String>() {
                    @Override
                    public void onNext(String s) {
                        T.showShort(ShopInfoActivity.this, "关注成功");
                        shopModel.collectId = s;
                        focusView.setText("已关注");
                        focusView.setTextColor(getResources().getColor(R.color.red1));
                        focusView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white));
                        Drawable drawable = getResources().getDrawable(R.mipmap.icon_shop_unfocus);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        focusView.setCompoundDrawables(drawable, null, null, null);
                    }
                };
                HttpMethods.getInstance().userCollect(new NoProgressSubscriber<String>(listener, this), StaticValues.userModel.userId, 2, shopModel.id);
            }
        } else {
            T.showShort(this, "使用关注功能需要先进行登录");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @OnClick({R.id.ll_home, R.id.ll_all, R.id.ll_hot, R.id.ll_new})
    public void clickTab(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                changeTab(0);
                break;
            case R.id.ll_all:
                changeTab(1);
                break;
            case R.id.ll_hot:
                changeTab(2);
                break;
            case R.id.ll_new:
                changeTab(3);
                break;
        }
        viewPager.setCurrentItem(currentFragment);
    }

    @OnClick(R.id.tv_recommend)
    public void recommend() {
        recommendView.setTextColor(getResources().getColor(R.color.red3));
        salesView.setTextColor(getResources().getColor(R.color.text_gray));
        priceView.setTextColor(getResources().getColor(R.color.text_gray));
        ShopGoodsFragment fragment = (ShopGoodsFragment) fragmentList.get(1);
        fragment.setPageSorts("13");
        fragment.setSortTerm("");
        fragment.setSortOrder("");
        fragment.requestDatas();
        upArrowView.setImageResource(R.mipmap.icon_up_arrow_gray);
        downArrowView.setImageResource(R.mipmap.icon_down_arrow_gray);
    }

    @OnClick(R.id.tv_sales)
    public void sales() {
        recommendView.setTextColor(getResources().getColor(R.color.text_gray));
        salesView.setTextColor(getResources().getColor(R.color.red3));
        priceView.setTextColor(getResources().getColor(R.color.text_gray));
        ShopGoodsFragment fragment = (ShopGoodsFragment) fragmentList.get(1);
        fragment.setPageSorts("");
        fragment.setSortTerm("2");
        fragment.setSortOrder("");
        fragment.requestDatas();
        upArrowView.setImageResource(R.mipmap.icon_up_arrow_gray);
        downArrowView.setImageResource(R.mipmap.icon_down_arrow_gray);
    }

    @OnClick(R.id.ll_price)
    public void price() {
        recommendView.setTextColor(getResources().getColor(R.color.text_gray));
        salesView.setTextColor(getResources().getColor(R.color.text_gray));
        priceView.setTextColor(getResources().getColor(R.color.red3));
        ShopGoodsFragment fragment = (ShopGoodsFragment) fragmentList.get(1);
        fragment.setPageSorts("");
        fragment.setSortTerm("1");
        if (fragment.getSortOrder() != null && fragment.getSortOrder().equals("1")) {
            fragment.setSortOrder("2");
            upArrowView.setImageResource(R.mipmap.icon_up_arrow_gray);
            downArrowView.setImageResource(R.mipmap.icon_down_arrow_red);
        } else {
            fragment.setSortOrder("1");
            upArrowView.setImageResource(R.mipmap.icon_up_arrow_red);
            downArrowView.setImageResource(R.mipmap.icon_down_arrow_gray);
        }
        fragment.requestDatas();
    }

    @OnClick(R.id.tv_shop_detail)
    public void shopDetail() {
        if (shopModel != null) {
            Intent intent = new Intent(this, ShopDetailActivity.class);
            intent.putExtra("ShopModel", shopModel);
            startActivity(intent);
        }
    }

    @OnClick(R.id.ll_hot_sorts)
    public void hotSorts(View v) {
        if (hotGroupList != null && hotGroupList.size() > 0) {
            if (popupWindow == null) {
                popupWindow = CommonUtils.createAbovePopupWindow(popupView);
            }
            int popupWidth = popupView.getMeasuredWidth();
            int popupHeight = popupView.getMeasuredHeight();
            int[] vLocation = new int[2];
            v.getLocationOnScreen(vLocation);
            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (vLocation[0] + v.getWidth() / 2) - popupWidth / 2,
                    vLocation[1] - popupHeight);
        } else {
            T.showShort(this, "暂无热门分类");
        }
    }

    @OnClick(R.id.ll_cus_phone)
    public void cusPhone() {
        if (!StringUtils.strIsEmpty(shopModel.cusPhone)) {
            DialogUtils.showDialog(this, "", shopModel.cusPhone, "呼叫", "取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 用intent启动拨打电话
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + shopModel.cusPhone));
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void changeTab(int index) {
        currentFragment = index;
        if (currentFragment == 1) {
            orderLayout.setVisibility(View.VISIBLE);
        } else {
            orderLayout.setVisibility(View.GONE);
        }
        for (int i = 0; i < labelViews.length; i++) {
            if (index == i) {
                if (i == 0) {
                    homeView.setImageResource(R.mipmap.icon_shop_active);
                } else {
                    countViews[i - 1].setTextColor(getResources().getColor(R.color.red3));
                }
                labelViews[i].setTextColor(getResources().getColor(R.color.red3));
            } else {
                if (i == 0) {
                    homeView.setImageResource(R.mipmap.icon_goodsinfo_shop);
                } else {
                    countViews[i - 1].setTextColor(getResources().getColor(R.color.text_gray));
                }
                labelViews[i].setTextColor(getResources().getColor(R.color.text_gray));
            }
        }
    }
}
