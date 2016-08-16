package com.sdjy.sdjymall;

import android.app.Application;
import android.telephony.TelephonyManager;

import com.sdjy.sdjymall.constants.StaticValues;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * 启动应用是执行的类
 * 主要初始化第三方SDK
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //数据库
        RealmConfiguration config = new RealmConfiguration.Builder(this).name("sdjymall.realm").build();
        Realm.setDefaultConfiguration(config);
        //获取手机imei码
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        StaticValues.imei = TelephonyMgr.getDeviceId();

    }
}
