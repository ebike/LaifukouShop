package com.sdjy.sdjymall;

import android.app.Application;
import android.telephony.TelephonyManager;

import com.sdjy.sdjymall.constants.StaticValues;

/**
 * 启动应用是执行的类
 * 主要初始化第三方SDK
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //获取手机imei码
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        StaticValues.imei = TelephonyMgr.getDeviceId();

    }
}
