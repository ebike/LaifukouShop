package com.sdjy.sdjymall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.MessageActivity;
import com.sdjy.sdjymall.activity.SearchActivity;
import com.sdjy.sdjymall.adapter.FirstSortsAdapter;
import com.sdjy.sdjymall.adapter.SecondSortsAdapter;
import com.sdjy.sdjymall.common.fragment.LazyFragment;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.CommonDataModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 分类
 */
public class SortFragment extends LazyFragment {

    @Bind(R.id.lv_first_sorts)
    ListView firstSortsView;
    @Bind(R.id.gv_second_sorts)
    GridView secondSortsView;
    private FirstSortsAdapter firstSortsAdapter;
    private List<CommonDataModel> firstSortsList;
    private SecondSortsAdapter secondSortsAdapter;
    private List<CommonDataModel> secondSortsList;

    private SubscriberOnNextListener<List<CommonDataModel>> nextListener;
    private SubscriberOnNextListener<List<CommonDataModel>> nextListener1;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_sort);

        firstSortsAdapter = new FirstSortsAdapter(getActivity());
        firstSortsView.setAdapter(firstSortsAdapter);
        nextListener = new SubscriberOnNextListener<List<CommonDataModel>>() {
            @Override
            public void onNext(List<CommonDataModel> commonDataModels) {
                firstSortsList = commonDataModels;
                firstSortsAdapter.setList(firstSortsList);
                firstSortsAdapter.setDefaultChecked();
                HttpMethods.getInstance().getSecondSorts(new ProgressSubscriber<List<CommonDataModel>>(nextListener1, getActivity()), firstSortsList.get(0).id);
            }
        };
        secondSortsAdapter = new SecondSortsAdapter(getActivity());
        secondSortsView.setAdapter(secondSortsAdapter);
        nextListener1 = new SubscriberOnNextListener<List<CommonDataModel>>() {
            @Override
            public void onNext(List<CommonDataModel> commonDataModels) {
                secondSortsList = commonDataModels;
                secondSortsAdapter.setList(secondSortsList);
            }
        };
        HttpMethods.getInstance().getFirstSorts(new NoProgressSubscriber<List<CommonDataModel>>(nextListener, getActivity()));
        firstSortsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int checkedPosition = firstSortsAdapter.getCheckedPosition();
                firstSortsList.get(checkedPosition).isChecked = false;
                firstSortsAdapter.setCheckedPosition(position);
                firstSortsList.get(position).isChecked = true;
                firstSortsAdapter.setList(firstSortsList);
                HttpMethods.getInstance().getSecondSorts(new ProgressSubscriber<List<CommonDataModel>>(nextListener1, getActivity()), firstSortsList.get(position).id);
            }
        });
    }

    @OnClick(R.id.tv_search)
    public void search() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_message)
    public void message(){
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        startActivity(intent);
    }
}