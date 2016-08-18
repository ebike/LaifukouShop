package com.sdjy.sdjymall.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.GoodsActivity;
import com.sdjy.sdjymall.activity.GoodsInfoActivity;
import com.sdjy.sdjymall.activity.RechargeActivity;
import com.sdjy.sdjymall.activity.ShopActivity;
import com.sdjy.sdjymall.activity.ShopInfoActivity;
import com.sdjy.sdjymall.adapter.HighQualityShopAdapter;
import com.sdjy.sdjymall.adapter.HomeGoodsAdapter;
import com.sdjy.sdjymall.adapter.NavigationAdapter;
import com.sdjy.sdjymall.adapter.SortsAdapter1;
import com.sdjy.sdjymall.adapter.SortsAdapter2;
import com.sdjy.sdjymall.common.fragment.LazyFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.HomePageDataItemModel;
import com.sdjy.sdjymall.model.HomePageDataModel;
import com.sdjy.sdjymall.model.HomeScrollImageModel;
import com.sdjy.sdjymall.model.ResourceModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.StringUtils;
import com.sdjy.sdjymall.view.ScrollGridView;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshBase;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 首页
 */
public class HomeFragment extends LazyFragment {

    @Bind(R.id.scrollView)
    PullToRefreshScrollView scrollView;

    View rootLayout;
    ConvenientBanner convenientBanner;
    ScrollGridView navigationView;
    ScrollGridView shopsView;
    ScrollGridView sortsView1;
    ScrollGridView sortsView2;
    ScrollGridView goodsView;
    private NavigationAdapter navigationAdapter;
    private HighQualityShopAdapter highQualityShopAdapter;
    private HomeGoodsAdapter homeGoodsAdapter;
    private SortsAdapter1 sortsAdapter1;
    private SortsAdapter2 sortsAdapter2;
    private List<HomePageDataItemModel> shopModelList;
    private List<HomePageDataItemModel> sortsModelList1;
    private List<HomePageDataItemModel> sortsModelList2;
    private List<HomePageDataItemModel> goodsModelList;

    private SubscriberOnNextListener<List<HomeScrollImageModel>> nextListener;
    private SubscriberOnNextListener<HomePageDataModel> nextListener1;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_home);

        initHomeContent();

        scrollView.setPullLoadEnabled(false);
        scrollView.setScrollLoadEnabled(false);
        scrollView.getRefreshableView().addView(rootLayout);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView, boolean isAutoRefresh) {
                HttpMethods.getInstance().getHomeScrollImages(new ProgressSubscriber<List<HomeScrollImageModel>>(nextListener, getActivity()));
                HttpMethods.getInstance().getHomePageDatas(new ProgressSubscriber<HomePageDataModel>(nextListener1, getActivity()));
                scrollView.doComplete(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        //滚动图片
        initBanner();
        //分类导航
        initNavigation();
        //优质商家
        initShops();
        //热门分类
        initSorts();
        //热销商品
        initGoods();
        //首页数据
        initData();
    }

    private void initData() {
        nextListener1 = new SubscriberOnNextListener<HomePageDataModel>() {
            @Override
            public void onNext(HomePageDataModel homePageDataModel) {
                highQualityShopAdapter.setList(homePageDataModel.shops);
                sortsAdapter1.setList(homePageDataModel.sorts.subList(0, homePageDataModel.sorts.size() - 4));
                sortsAdapter2.setList(homePageDataModel.sorts.subList(homePageDataModel.sorts.size() - 4, homePageDataModel.sorts.size()));
                homeGoodsAdapter.setList(homePageDataModel.goods);
            }
        };

        HttpMethods.getInstance().getHomePageDatas(new NoProgressSubscriber<HomePageDataModel>(nextListener1, getActivity()));
    }

    private void initGoods() {
        goodsModelList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            goodsModelList.add(new HomePageDataItemModel());
        }
        homeGoodsAdapter = new HomeGoodsAdapter(getActivity());
        goodsView.setAdapter(homeGoodsAdapter);
        homeGoodsAdapter.setList(goodsModelList);
        goodsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomePageDataItemModel model = (HomePageDataItemModel) parent.getItemAtPosition(position);
                if (!StringUtils.strIsEmpty(model.id)) {
                    Intent intent = new Intent(getActivity(), GoodsInfoActivity.class);
                    intent.putExtra("GoodsId", model.id);
                    startActivity(intent);
                }
            }
        });
    }

    private void initSorts() {
        sortsModelList1 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            sortsModelList1.add(new HomePageDataItemModel());
        }
        sortsAdapter1 = new SortsAdapter1(getActivity());
        sortsView1.setAdapter(sortsAdapter1);
        sortsAdapter1.setList(sortsModelList1);

        sortsModelList2 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            sortsModelList2.add(new HomePageDataItemModel());
        }
        sortsAdapter2 = new SortsAdapter2(getActivity());
        sortsView2.setAdapter(sortsAdapter2);
        sortsAdapter2.setList(sortsModelList2);
    }

    private void initShops() {
        shopModelList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            shopModelList.add(new HomePageDataItemModel());
        }
        highQualityShopAdapter = new HighQualityShopAdapter(getActivity());
        shopsView.setAdapter(highQualityShopAdapter);
        highQualityShopAdapter.setList(shopModelList);
        shopsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomePageDataItemModel model = (HomePageDataItemModel) parent.getItemAtPosition(position);
                if (!StringUtils.strIsEmpty(model.id)) {
                    Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
                    intent.putExtra("shopId", model.id);
                    startActivity(intent);
                }
            }
        });
    }

    private void initNavigation() {
        TypedArray icons = getResources().obtainTypedArray(R.array.home_navigation);
        String[] names = getResources().getStringArray(R.array.home_navigation_name);
        List<ResourceModel> modelList = new ArrayList<>();
        ResourceModel model;
        for (int i = 0; i < icons.length(); i++) {
            model = new ResourceModel();
            model.resourceId = icons.getResourceId(i, 0);
            model.name = names[i];
            modelList.add(model);
        }
        icons.recycle();

        navigationAdapter = new NavigationAdapter(getActivity());
        navigationView.setAdapter(navigationAdapter);
        navigationAdapter.setList(modelList);
        navigationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0://联盟商家
                        startActivity(new Intent(getActivity(), ShopActivity.class));
                        break;
                    case 1://金币专区
                        intent = new Intent(getActivity(), GoodsActivity.class);
                        intent.putExtra("pageSorts", "2");
                        startActivity(intent);
                        break;
                    case 2://银币专区
                        intent = new Intent(getActivity(), GoodsActivity.class);
                        intent.putExtra("pageSorts", "3");
                        startActivity(intent);
                        break;
                    case 3://充值立返
                        intent = new Intent(getActivity(), RechargeActivity.class);
                        startActivity(intent);
                        break;
                    case 5://最新上架
                        intent = new Intent(getActivity(), GoodsActivity.class);
                        intent.putExtra("pageSorts", "12");
                        startActivity(intent);
                        break;
                    case 6://热销商品
                        intent = new Intent(getActivity(), GoodsActivity.class);
                        intent.putExtra("pageSorts", "11");
                        startActivity(intent);
                        break;
                    case 7://所有分类

                        break;
                }
            }
        });
    }

    private void initBanner() {
        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        convenientBanner.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .startTurning(3000);

        nextListener = new SubscriberOnNextListener<List<HomeScrollImageModel>>() {
            @Override
            public void onNext(List<HomeScrollImageModel> homeScrollImageModels) {
                convenientBanner.setPages(
                        new CBViewHolderCreator<NetworkImageHolderView>() {
                            @Override
                            public NetworkImageHolderView createHolder() {
                                return new NetworkImageHolderView();
                            }
                        }, homeScrollImageModels);
            }
        };

        HttpMethods.getInstance().getHomeScrollImages(new ProgressSubscriber<List<HomeScrollImageModel>>(nextListener, getActivity()));
    }

    private void initHomeContent() {
        rootLayout = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_content, null);
        convenientBanner = (ConvenientBanner) rootLayout.findViewById(R.id.convenientBanner);
        navigationView = (ScrollGridView) rootLayout.findViewById(R.id.gv_navigation);
        shopsView = (ScrollGridView) rootLayout.findViewById(R.id.gv_shops);
        sortsView1 = (ScrollGridView) rootLayout.findViewById(R.id.gv_sorts1);
        sortsView2 = (ScrollGridView) rootLayout.findViewById(R.id.gv_sorts2);
        goodsView = (ScrollGridView) rootLayout.findViewById(R.id.gv_goods);
    }

    public class NetworkImageHolderView implements Holder<HomeScrollImageModel> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.mipmap.img_banner_default);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, HomeScrollImageModel data) {
            Glide.with(getActivity())
                    .load(data.imageUrl)
                    .error(R.mipmap.img_banner_default)
                    .into(imageView);
        }
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
    }

}