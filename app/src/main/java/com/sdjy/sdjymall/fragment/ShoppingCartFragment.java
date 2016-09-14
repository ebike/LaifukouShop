package com.sdjy.sdjymall.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.OrderConfirmActivity;
import com.sdjy.sdjymall.adapter.ShoppingCartAdapter;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.event.LogoutEvent;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.fragment.base.BaseListFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CarGoodsModel;
import com.sdjy.sdjymall.model.CarShopModel;
import com.sdjy.sdjymall.model.HttpResult;
import com.sdjy.sdjymall.model.UserModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.CartUtils;
import com.sdjy.sdjymall.util.GoodsUtils;
import com.sdjy.sdjymall.view.PullListFragmentHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.realm.Realm;

/**
 * 购物车
 */
public class ShoppingCartFragment extends BaseListFragment {

    @Bind(R.id.tv_edit)
    TextView editView;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;
    @Bind(R.id.ll_bottom)
    LinearLayout bottomLayout;
    @Bind(R.id.tv_choose_all)
    TextView chooseAllView;
    @Bind(R.id.tv_total)
    TextView totalView;
    @Bind(R.id.tv_checkout)
    TextView checkoutView;
    @Bind(R.id.rl_bottom_edit)
    RelativeLayout bottomEditLayout;
    @Bind(R.id.tv_choose_all_edit)
    TextView chooseAllEditView;

    private PullListFragmentHandler handler;
    private ShoppingCartAdapter adapter;
    private List<CarShopModel> carShopList;
    private Realm realm;
    //编辑状态
    private boolean inEdit;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_shopping_car);
        EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();

        handler = new PullListFragmentHandler(this, listView);
        listView.setPullLoadEnabled(false);
        listView.setScrollLoadEnabled(false);
        listView.setDriverLine();
        adapter = new ShoppingCartAdapter(getActivity());
        adapter.setRealm(realm);
        adapter.setCallback(new ShoppingCartAdapter.ChangeSelectedCallback() {
            @Override
            public void onChanged() {
                if (inEdit) {
                    updateBottomInEdit();
                } else {
                    updateBottom();
                }
            }
        });
        listView.setAdapter(adapter);
        listView.doPullRefreshing(true, DELAY_MILLIS);

    }

    @Override
    public void requestDatas() {
        if (StaticValues.userModel != null) {
            SubscriberNextErrorListener listener = new SubscriberNextErrorListener<List<CarShopModel>>() {
                @Override
                public void onNext(List<CarShopModel> carShopModels) {
                    if (carShopModels != null && carShopModels.size() > 0) {
                        listView.setBackgroundColor(getResources().getColor(R.color.main_bg));
                        carShopList = carShopModels;
                        editView.setVisibility(View.VISIBLE);
                        if (inEdit) {
                            bottomEditLayout.setVisibility(View.VISIBLE);
                        } else {
                            bottomLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        carShopList = new ArrayList<>();
                        listView.setBackgroundColor(getResources().getColor(R.color.transparent));
                        editView.setVisibility(View.GONE);
                        bottomLayout.setVisibility(View.GONE);
                        bottomEditLayout.setVisibility(View.GONE);
                    }
                    adapter.setList(carShopList);
                    handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
                }

                @Override
                public void onError() {
                    listView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    editView.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                    bottomEditLayout.setVisibility(View.GONE);
                    handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
                }
            };
            HttpMethods.getInstance().cartGoods(new NoProgressSubscriber<List<CarShopModel>>(listener, getActivity()));
        } else {
            carShopList = realm.where(CarShopModel.class).findAll();
            if (carShopList != null && carShopList.size() > 0) {
                listView.setBackgroundColor(getResources().getColor(R.color.main_bg));
                editView.setVisibility(View.VISIBLE);
                if (inEdit) {
                    bottomEditLayout.setVisibility(View.VISIBLE);
                } else {
                    bottomLayout.setVisibility(View.VISIBLE);
                }
                updateBottom();
            } else {
                listView.setBackgroundColor(getResources().getColor(R.color.transparent));
                editView.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.GONE);
                bottomEditLayout.setVisibility(View.GONE);
            }
            adapter.setList(carShopList);
            handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
        }
    }

    @OnClick(R.id.tv_edit)
    public void edit() {
        if (inEdit) {
            inEdit = false;
            editView.setText("编辑");
            bottomEditLayout.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.VISIBLE);
        } else {
            inEdit = true;
            editView.setText("完成");
            bottomEditLayout.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.GONE);
        }
        adapter.setInEdit(inEdit);
        adapter.setList(carShopList);
    }

    @OnClick(R.id.tv_choose_all_edit)
    public void chooseAllEdit() {
        if (StaticValues.userModel == null) {
            realm.beginTransaction();
        }
        boolean isAllSelected = CartUtils.isAllSelectedInEdit(carShopList);
        if (isAllSelected) {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_nosel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            chooseAllEditView.setCompoundDrawables(drawable, null, null, null);
            for (CarShopModel shopModel : carShopList) {
                shopModel.setSelectedInEdit(false);
                for (CarGoodsModel goodsModel : shopModel.getGoods()) {
                    goodsModel.setSelectedInEdit(false);
                }
            }
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_sel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            chooseAllEditView.setCompoundDrawables(drawable, null, null, null);
            for (CarShopModel shopModel : carShopList) {
                shopModel.setSelectedInEdit(true);
                for (CarGoodsModel goodsModel : shopModel.getGoods()) {
                    goodsModel.setSelectedInEdit(true);
                }
            }
        }
        adapter.setList(carShopList);
        if (StaticValues.userModel == null) {
            realm.commitTransaction();
        }
    }

    @OnClick(R.id.tv_choose_all)
    public void chooseAll() {
        if (StaticValues.userModel == null) {
            realm.beginTransaction();
        }
        boolean isAllSelected = CartUtils.isAllSelected(carShopList);
        int count = 0;
        Double amount = 0d;
        int goldCoin = 0;
        int coin = 0;
        if (isAllSelected) {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_nosel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            chooseAllView.setCompoundDrawables(drawable, null, null, null);
            for (CarShopModel shopModel : carShopList) {
                shopModel.setSelected(false);
                for (CarGoodsModel goodsModel : shopModel.getGoods()) {
                    goodsModel.setSelected(false);
                }
            }
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_sel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            chooseAllView.setCompoundDrawables(drawable, null, null, null);
            for (CarShopModel shopModel : carShopList) {
                shopModel.setSelected(true);
                for (CarGoodsModel goodsModel : shopModel.getGoods()) {
                    if (goodsModel.getStock() == 0) {
                        goodsModel.setSelected(false);
                    } else {
                        goodsModel.setSelected(true);
                        count += goodsModel.getNum();
                        switch (goodsModel.getPriceType()) {
                            case 1:
                                amount += Double.valueOf(goodsModel.getPriceMoney()) * goodsModel.getNum();
                                break;
                            case 2:
                                amount += Double.valueOf(goodsModel.getPriceMoney()) * goodsModel.getNum();
                                goldCoin += Integer.valueOf(goodsModel.getPriceGoldCoin()) * goodsModel.getNum();
                                break;
                            case 3:
                                coin += Integer.valueOf(goodsModel.getPriceCoin()) * goodsModel.getNum();
                                break;
                        }
                    }
                }
            }
        }
        adapter.setList(carShopList);
        totalView.setText("合计：" + GoodsUtils.formatPrice(amount + "", goldCoin, coin));
        if (count > 99) {
            checkoutView.setText("去结算(99+)");
        } else {
            checkoutView.setText("去结算(" + count + ")");
        }
        if (StaticValues.userModel == null) {
            realm.commitTransaction();
        }
    }

    @OnClick(R.id.tv_checkout)
    public void checkout() {
        boolean hasSelected = CartUtils.hasSelected(carShopList);
        if (hasSelected) {
            if (StaticValues.userModel != null) {
                StringBuffer ids = new StringBuffer();
                for (CarShopModel shopModel : carShopList) {
                    for (CarGoodsModel goodsModel : shopModel.getGoods()) {
                        if (goodsModel.isSelected()) {
                            ids.append(goodsModel.getOid()).append(";");
                        }
                    }
                }
                ids.deleteCharAt(ids.length() - 1);
                Intent intent = new Intent(getActivity(), OrderConfirmActivity.class);
                intent.putExtra("ids", ids.toString());
                startActivity(intent);
            } else {
                T.showShort(getActivity(), "请您先登录");
            }
        } else {
            T.showShort(getActivity(), "您还没有选择商品哦！");
        }
    }

    @OnClick(R.id.tv_delete)
    public void delete() {
        boolean hasSelectedInEdit = CartUtils.hasSelectedInEdit(carShopList);
        if (hasSelectedInEdit) {
            if (StaticValues.userModel != null) {
                StringBuffer goodsIds = new StringBuffer();
                StringBuffer priceIds = new StringBuffer();
                for (CarShopModel shopModel : carShopList) {
                    for (CarGoodsModel goodsModel : shopModel.getGoods()) {
                        if (goodsModel.isSelectedInEdit()) {
                            goodsIds.append(goodsModel.getId()).append(";");
                            priceIds.append(goodsModel.getPriceId()).append(";");
                        }
                    }
                }
                goodsIds.deleteCharAt(goodsIds.length() - 1);
                priceIds.deleteCharAt(priceIds.length() - 1);
                SubscriberOnNextListener listener = new SubscriberOnNextListener<HttpResult>() {
                    @Override
                    public void onNext(HttpResult httpResult) {
                        T.showShort(getActivity(), httpResult.message);
                        if (httpResult.code.equals("1")) {
                            requestDatas();
                        }
                    }
                };
                HttpMethods.getInstance().delShoppingCart(new ProgressSubscriber<HttpResult>(listener, getActivity()), goodsIds.toString(), priceIds.toString());
            } else {
                realm.beginTransaction();
                for (CarShopModel shopModel : carShopList) {
                    if (shopModel.isSelectedInEdit()) {
                        shopModel.deleteFromRealm();
                    }
                }
                List<CarGoodsModel> carGoodsList = realm.where(CarGoodsModel.class).equalTo("isSelectedInEdit", true).findAll();
                for (CarGoodsModel goodsModel : carGoodsList) {
                    goodsModel.deleteFromRealm();
                }
                realm.commitTransaction();
                requestDatas();
            }
        } else {
            T.showShort(getActivity(), "您还没有选择商品哦！");
        }
    }

    private void updateBottom() {
        boolean isAllSelected = CartUtils.isAllSelected(carShopList);
        int count = 0;
        Double amount = 0d;
        int goldCoin = 0;
        int coin = 0;
        if (isAllSelected) {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_sel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            chooseAllView.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_nosel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            chooseAllView.setCompoundDrawables(drawable, null, null, null);
        }
        for (CarShopModel shopModel : carShopList) {
            for (CarGoodsModel goodsModel : shopModel.getGoods()) {
                if (goodsModel.isSelected()) {
                    count += goodsModel.getNum();
                    switch (goodsModel.getPriceType()) {
                        case 1:
                            amount += Double.valueOf(goodsModel.getPriceMoney()) * goodsModel.getNum();
                            break;
                        case 2:
                            amount += Double.valueOf(goodsModel.getPriceMoney()) * goodsModel.getNum();
                            goldCoin += Integer.valueOf(goodsModel.getPriceGoldCoin()) * goodsModel.getNum();
                            break;
                        case 3:
                            coin += Integer.valueOf(goodsModel.getPriceCoin()) * goodsModel.getNum();
                            break;
                    }
                }
            }
        }
        totalView.setText("合计：" + GoodsUtils.formatPrice(amount + "", goldCoin, coin));
        if (count > 99) {
            checkoutView.setText("去结算(99+)");
        } else {
            checkoutView.setText("去结算(" + count + ")");
        }
    }

    private void updateBottomInEdit() {
        boolean isAllSelected = CartUtils.isAllSelectedInEdit(carShopList);
        if (isAllSelected) {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_sel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            chooseAllEditView.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_circle_nosel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            chooseAllEditView.setCompoundDrawables(drawable, null, null, null);
        }
    }

    public void onEvent(UserModel model) {
        requestDatas();
    }

    public void onEvent(LogoutEvent event) {
        requestDatas();
    }

    public void onEvent(RefreshEvent event) {
        if (event.simpleName.equals(this.getClass().getSimpleName())) {
            requestDatas();
        }
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
        if (realm != null) {
            realm.close();
        }
    }
}
