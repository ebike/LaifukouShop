package com.laifukou.laifukoushop.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.laifukou.laifukoushop.R;
import com.laifukou.laifukoushop.adapter.FirstSortsAdapter;
import com.laifukou.laifukoushop.adapter.SecondSortsAdapter;
import com.laifukou.laifukoushop.common.fragment.LazyFragment;
import com.laifukou.laifukoushop.http.HttpMethods;
import com.laifukou.laifukoushop.model.CommonDataModel;
import com.laifukou.laifukoushop.subscribers.NoProgressSubscriber;
import com.laifukou.laifukoushop.subscribers.ProgressSubscriber;
import com.laifukou.laifukoushop.subscribers.SubscriberOnNextListener;

import java.util.List;

import butterknife.Bind;

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


}