package com.sdjy.sdjymall.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.common.adapter.ViewPagerFragmentAdapter;
import com.sdjy.sdjymall.common.fragment.LazyFragment;
import com.sdjy.sdjymall.common.model.TabIndicator;
import com.sdjy.sdjymall.common.util.ViewPagerUtil;
import com.sdjy.sdjymall.model.GoodsInfoModel;
import com.sdjy.sdjymall.model.GoodsPricesModel;
import com.sdjy.sdjymall.view.ChooseStandardDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 商品-商品
 */
public class GoodsGoodsFragment extends LazyFragment {

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.tv_cur_num)
    TextView curNumView;
    @Bind(R.id.tv_total_num)
    TextView totalNumView;
    @Bind(R.id.tv_name)
    TextView nameView;
    @Bind(R.id.tv_amount)
    TextView amountView;
    @Bind(R.id.tv_post_price)
    TextView postPriceView;
    @Bind(R.id.tv_standard)
    TextView standardView;
    @Bind(R.id.tv_count)
    TextView countView;

    private List<Fragment> fragmentList;
    private List<TabIndicator> tabIndicatorList;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private GoodsInfoModel goodsInfoModel;
    private ChooseStandardDialog dialog;
    private GoodsPricesModel selectedPricesModel;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_goods_goods);

        if (goodsInfoModel != null) {
            //滚动图片
            totalNumView.setText("/" + goodsInfoModel.goodsPics.size());
            viewPager.setOffscreenPageLimit(goodsInfoModel.goodsPics.size());
            tabIndicatorList = ViewPagerUtil.getTabIndicator(goodsInfoModel.goodsPics.size());
            fragmentList = new ArrayList<>();
            for (String pic : goodsInfoModel.goodsPics) {
                GoodsPicsFragment picsFragment = new GoodsPicsFragment();
                picsFragment.setGoodsPic(pic);
                fragmentList.add(picsFragment);
            }
            // 设置ViewPager适配器
            viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), tabIndicatorList, fragmentList);
            viewPager.setAdapter(viewPagerFragmentAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    curNumView.setText((position + 1) + "");
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            //商品名称
            if (goodsInfoModel.shopType == 1) {
                ImageSpan span = new ImageSpan(getActivity(), R.mipmap.icon_self_sale);
                String goodsName = goodsInfoModel.goodsName + "  ";
                int strLength = goodsName.length();
                SpannableString spannableString = new SpannableString(goodsName);
                spannableString.setSpan(span, strLength - 1, strLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                nameView.setText(spannableString);
            } else {
                nameView.setText(goodsInfoModel.goodsName);
            }

            //商品价格
            if (goodsInfoModel.goodsPrices != null && goodsInfoModel.goodsPrices.size() > 0) {
                GoodsPricesModel goodsPricesModel = goodsInfoModel.goodsPrices.get(0);
                if (goodsInfoModel.priceType == 1) {
                    amountView.setText("￥" + goodsPricesModel.priceMoney);
                } else if (goodsInfoModel.priceType == 2) {
                    amountView.setText("￥" + goodsPricesModel.priceMoney + " + 金币 " + goodsPricesModel.priceGoldCoin);
                } else if (goodsInfoModel.priceType == 3) {
                    amountView.setText("币 " + goodsPricesModel.priceCoin);
                }
                standardView.setText(goodsPricesModel.standard);
            }

            //包邮
            if (goodsInfoModel.includePost == 1) {
                postPriceView.setVisibility(View.VISIBLE);
                postPriceView.setText("(包邮)");
            } else if (goodsInfoModel.includePost == 2) {
                postPriceView.setVisibility(View.VISIBLE);
                postPriceView.setText("(邮费：" + goodsInfoModel.postPrice + ")");
            } else {
                postPriceView.setVisibility(View.GONE);
            }

            //规格（默认选中第一项）
            selectedPricesModel = goodsInfoModel.goodsPrices.get(0);
        }
    }

    @OnClick(R.id.ll_choose_standard)
    public void chooseStandard() {
        if (dialog == null) {
            dialog = new ChooseStandardDialog(getActivity(), goodsInfoModel.goodsPrices)
                    .builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true);
            dialog.setCallback(new ChooseStandardDialog.ChooseStandardCallback() {
                @Override
                public void callback(GoodsPricesModel model) {
                    selectedPricesModel = model;
                }
            });
            dialog.setCountCallback(new ChooseStandardDialog.ChangeCountCallback() {
                @Override
                public void changed(int count) {
                    countView.setText(count + "个");
                }
            });
        }
        dialog.show();
    }

    public void setGoodsInfoModel(GoodsInfoModel goodsInfoModel) {
        this.goodsInfoModel = goodsInfoModel;
    }

    public GoodsPricesModel getSelectedPricesModel() {
        return selectedPricesModel;
    }
}
