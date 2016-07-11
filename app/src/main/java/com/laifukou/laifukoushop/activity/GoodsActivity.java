package com.laifukou.laifukoushop.activity;

import com.laifukou.laifukoushop.R;
import com.laifukou.laifukoushop.activity.base.BaseListActivity;

public class GoodsActivity extends BaseListActivity {

    private String pageSorts;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_goods);
    }

    @Override
    public void init() {
        pageSorts = getIntent().getStringExtra("pageSorts");
    }

    @Override
    public void requestDatas() {

    }
}
