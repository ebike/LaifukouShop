package com.sdjy.sdjymall.activity;

import android.widget.EditText;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.db.ProvinceInfoDao;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.AddressModel;
import com.sdjy.sdjymall.model.LocationJson;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.StringUtils;
import com.sdjy.sdjymall.view.wheel.AddressThreeWheelViewDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

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

    private AddressThreeWheelViewDialog dialog;
    private ProvinceInfoDao provinceDao;
    private List<LocationJson> mProvinceList;
    private AddressModel addressModel;
    private String province;
    private String city;
    private String area;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_add_receive_address);
    }

    @Override
    public void init() {
        addressModel = (AddressModel) getIntent().getSerializableExtra("addressModel");

        titleView.setText("添加收货地址");
        dialog = new AddressThreeWheelViewDialog(this);
        provinceDao = new ProvinceInfoDao(this);
        mProvinceList = provinceDao.queryAll();

        if (addressModel != null) {
            consigneeText.setText(addressModel.consignee);
            phoneText.setText(addressModel.mobile);
            areaView.setText(addressModel.province + addressModel.city + addressModel.area);
            addressText.setText(addressModel.address);
            province = addressModel.province;
            city = addressModel.city;
            area = addressModel.area;
            dialog.setData(mProvinceList, addressModel.province, addressModel.city, addressModel.area);
        } else {
            dialog.setData(mProvinceList);
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.rl_area)
    public void area() {
        dialog.show(new AddressThreeWheelViewDialog.ConfirmAction() {
            @Override
            public void doAction(final LocationJson root, final LocationJson child, final LocationJson child2) {
                province = root.getName();
                city = child.getName();
                area = child2.getName();
                areaView.setText(province + city + area);
            }
        });
    }

    @OnClick(R.id.btn_save)
    public void save() {
        String consignee = consigneeText.getText().toString();
        String phone = phoneText.getText().toString();
        String address = addressText.getText().toString();
        if (StringUtils.strIsEmpty(consignee)) {
            T.showShort(this, "请填写收货人");
            return;
        }
        if (StringUtils.strIsEmpty(phone)) {
            T.showShort(this, "请填写手机号码");
            return;
        }
        if (StringUtils.strIsEmpty(areaView.getText().toString())) {
            T.showShort(this, "请选择所在地区");
            return;
        }
        if (StringUtils.strIsEmpty(address)) {
            T.showShort(this, "请填写详细地址");
            return;
        }
        Map<String, String> params = new HashMap<>();
        if (addressModel != null) {
            params.put("id", addressModel.id);
        }
        params.put("consignee", consignee);
        params.put("mobile", phone);
        params.put("province", province);
        params.put("city", city);
        params.put("area", area);
        params.put("address", address);
        SubscriberOnNextListener listener = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                T.showShort(AddReceiveAddressActivity.this, "保存成功");
                EventBus.getDefault().post(new RefreshEvent(ReceiveAddressActivity.class.getSimpleName()));
                AddReceiveAddressActivity.this.finish();
            }
        };
        HttpMethods.getInstance().saveOrUpdateAddress(new ProgressSubscriber(listener, this), params);
    }
}
