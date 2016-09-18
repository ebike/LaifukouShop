package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.adapter.ViewPagerFragmentAdapter;
import com.sdjy.sdjymall.common.model.TabIndicator;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.common.util.ViewPagerUtil;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.fragment.GoodsDetailFragment;
import com.sdjy.sdjymall.fragment.GoodsEvaluateFragment;
import com.sdjy.sdjymall.fragment.GoodsGoodsFragment;
import com.sdjy.sdjymall.fragment.ShoppingCartFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CarGoodsModel;
import com.sdjy.sdjymall.model.CarShopModel;
import com.sdjy.sdjymall.model.GoodsInfoModel;
import com.sdjy.sdjymall.model.GoodsPricesModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.subscribers.IAddShopListener;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.AnimUtils;
import com.sdjy.sdjymall.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.realm.Realm;
import io.realm.RealmList;

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
    @Bind(R.id.tv_into_car)
    TextView intoCarView;
    @Bind(R.id.tv_car)
    TextView carView;

    private List<Fragment> fragmentList;
    private List<TabIndicator> tabIndicatorList;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private int currentFragment;

    private SubscriberOnNextListener<GoodsInfoModel> nextListener;
    private String goodsId;
    private GoodsInfoModel goodsInfoModel;
    private AnimUtils animUtils;
    private Realm realm;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_goods_info);
        realm = Realm.getDefaultInstance();
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

                if (goodsInfoModel.state != 1) {
                    intoCarView.setBackgroundColor(getResources().getColor(R.color.yellow1));
                    intoCarView.setText("商品已下架");
                } else if (goodsInfoModel.goodsPrices.get(0).stock <= 0) {
                    intoCarView.setBackgroundColor(getResources().getColor(R.color.yellow1));
                    intoCarView.setText("暂时缺货");
                } else {
                    intoCarView.setBackgroundColor(getResources().getColor(R.color.red));
                    intoCarView.setText("加入购物车");
                }
            }
        };
        Map<String, String> params = new HashMap<>();
        if (StaticValues.userModel != null) {
            params.put("userId", StaticValues.userModel.userId);
        }
        HttpMethods.getInstance().findGoods(new ProgressSubscriber<GoodsInfoModel>(nextListener, this), goodsId, params);
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
                        goodsInfoModel.collectId = null;
                        Drawable drawable = getResources().getDrawable(R.mipmap.icon_goodsinfo_collect);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        goodsFocusView.setCompoundDrawables(null, drawable, null, null);
                    }
                };
                HttpMethods.getInstance().cancelCollect(new NoProgressSubscriber(listener, this), StaticValues.userModel.userId, goodsInfoModel.collectId);
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
                HttpMethods.getInstance().userCollect(new NoProgressSubscriber<String>(listener, this), StaticValues.userModel.userId, 1, goodsInfoModel.id);
            }
        } else {
            T.showShort(this, "使用关注功能需要先进行登录");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.tv_car)
    public void car() {
        startActivity(new Intent(this, ShoppingCartActivity.class));
    }

    @OnClick(R.id.tv_into_car)
    public void intoCar() {
        if (goodsInfoModel.state != 1 || goodsInfoModel.stock <= 0) {
            return;
        }

        final ImageView imageView = new ImageView(this);
        Glide.with(this)
                .load(goodsInfoModel.goodsPics.get(0))
                .override(100, 100)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
        GoodsGoodsFragment goodsFragment = (GoodsGoodsFragment) fragmentList.get(0);
        GoodsPricesModel pricesModel = goodsFragment.getSelectedPricesModel();
        if (StaticValues.userModel != null) {
            SubscriberOnNextListener listener = new SubscriberOnNextListener<HttpResult>() {
                @Override
                public void onNext(HttpResult httpResult) {
                    T.showShort(GoodsInfoActivity.this, httpResult.message);
                    EventBus.getDefault().post(new RefreshEvent(ShoppingCartFragment.class.getSimpleName()));
                    intoCarAnim(imageView);
                }
            };
            HttpMethods.getInstance().addToCart(new NoProgressSubscriber(listener, this), StaticValues.userModel.userId, goodsInfoModel.id, pricesModel.id, goodsFragment.getGoodsNum());
        } else {
            //判断本地购物车是否存在该商品，若存在，修改个数，若不存在，加入购物车
            CarGoodsModel carGoodsModel = realm.where(CarGoodsModel.class).equalTo("id", goodsInfoModel.id).findFirst();
            if (carGoodsModel != null) {
                int num = carGoodsModel.getNum() + 1;
                realm.beginTransaction();
                carGoodsModel.setNum(num);
                realm.copyToRealmOrUpdate(carGoodsModel);
                realm.commitTransaction();
            } else {
                RealmList<CarGoodsModel> carGoodsList = new RealmList<>();

                carGoodsModel = new CarGoodsModel();
                carGoodsModel.setId(goodsInfoModel.id);
                carGoodsModel.setGoodsName(goodsInfoModel.goodsName);
                carGoodsModel.setNum(goodsFragment.getGoodsNum());
                carGoodsModel.setCommentNum(goodsInfoModel.commentNum);
                carGoodsModel.setImageUrl(goodsInfoModel.goodsPics.get(0));
                carGoodsModel.setPraiseRate(goodsInfoModel.praiseRate);
                carGoodsModel.setPriceId(pricesModel.id);
                carGoodsModel.setPriceMoney(pricesModel.priceMoney);
                carGoodsModel.setPriceGoldCoin(pricesModel.priceGoldCoin);
                carGoodsModel.setPriceCoin(pricesModel.priceCoin);
                carGoodsModel.setPriceType(goodsInfoModel.priceType);
                carGoodsModel.setStandard(pricesModel.standard);

                CarShopModel carShopModel = realm.where(CarShopModel.class).equalTo("shopId", goodsInfoModel.shopId).findFirst();
                if (carShopModel != null) {
                    carGoodsList = carShopModel.getGoods();
                    realm.beginTransaction();
                    carGoodsList.add(carGoodsModel);
                    realm.copyToRealmOrUpdate(carShopModel);
                    realm.commitTransaction();
                } else {
                    carGoodsList.add(carGoodsModel);

                    carShopModel = new CarShopModel();
                    carShopModel.setShopId(goodsInfoModel.shopId);
                    carShopModel.setShopName(goodsInfoModel.shopName);
                    carShopModel.setShopType(goodsInfoModel.shopType);
                    carShopModel.setGoods(carGoodsList);
                    realm.beginTransaction();
                    realm.copyToRealm(carShopModel);
                    realm.commitTransaction();
                }
            }
            intoCarAnim(imageView);
        }
    }

    private void intoCarAnim(ImageView imageView) {
        if (animUtils == null) {
            animUtils = new AnimUtils(this, intoCarView, carView, imageView);
        }
        animUtils.addShopCart(new IAddShopListener() {

            @Override
            public void addSucess() {
                //变化角标（目前不做）
            }
        });
    }

    public void changeCarBtn(GoodsPricesModel model){
        if (goodsInfoModel.state != 1) {
            intoCarView.setBackgroundColor(getResources().getColor(R.color.yellow1));
            intoCarView.setText("商品已下架");
        } else if (model.stock <= 0) {
            intoCarView.setBackgroundColor(getResources().getColor(R.color.yellow1));
            intoCarView.setText("暂时缺货");
        } else {
            intoCarView.setBackgroundColor(getResources().getColor(R.color.red));
            intoCarView.setText("加入购物车");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}
