package com.sdjy.sdjymall.fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.adapter.ShoppingCartAdapter;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.fragment.base.BaseListFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CarShopModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberNextErrorListener;
import com.sdjy.sdjymall.view.PullListFragmentHandler;
import com.sdjy.sdjymall.view.pullrefresh.PullToRefreshListView;

import java.util.List;

import butterknife.Bind;
import io.realm.Realm;

/**
 * 购物车
 */
public class ShoppingCartFragment extends BaseListFragment {

    @Bind(R.id.iv_message)
    ImageView messageView;
    @Bind(R.id.tv_edit)
    TextView editView;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;

    private PullListFragmentHandler handler;
    private ShoppingCartAdapter adapter;
    private List<CarShopModel> carShopList;
    private Realm realm;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_shopping_car);
        realm = Realm.getDefaultInstance();

        handler = new PullListFragmentHandler(this, listView);
        listView.setPullLoadEnabled(false);
        listView.setScrollLoadEnabled(false);
        listView.setDriverLine();
        adapter = new ShoppingCartAdapter(getActivity());
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
                        adapter.setList(carShopList);
                    } else {
                        listView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    }
                    handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
                }

                @Override
                public void onError() {
                    listView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
                }
            };
            HttpMethods.getInstance().cartGoods(new NoProgressSubscriber<List<CarShopModel>>(listener, getActivity()));
        } else {
            carShopList = realm.where(CarShopModel.class).findAll();
            if(carShopList != null && carShopList.size()>0){
                listView.setBackgroundColor(getResources().getColor(R.color.main_bg));
                adapter.setList(carShopList);
            }else{
                listView.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
            handler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
        }
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        if (realm != null) {
            realm.close();
        }
    }
}
