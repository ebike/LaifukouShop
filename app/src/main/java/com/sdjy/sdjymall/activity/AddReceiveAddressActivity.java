package com.sdjy.sdjymall.activity;

import android.widget.EditText;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;

import butterknife.Bind;

public class AddReceiveAddressActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.et_consignee)
    EditText consigneeText;
    @Bind(R.id.et_phone)
    EditText phoneText;
    @Bind(R.id.tv_area)
    TextView areaView;
    @Bind(R.id.et_address)
    EditText addressText;


    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_add_receive_address);
    }

    @Override
    public void init() {
        titleView.setText("添加收货地址");


    }


}
