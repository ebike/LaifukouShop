package com.sdjy.sdjymall.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    /**
     * 进行MD5加密
     *
     * @param s
     * @return
     */
    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取包名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 给组件设置margin
     *
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /**
     * 手机号码校验 11位以1开头
     *
     * @param mobileNumber
     * @return
     */
    public static boolean isPhoneNumber(String mobileNumber) {
        if (!StringUtils.strIsEmpty(mobileNumber)) {
            if (mobileNumber.startsWith("+86"))
                mobileNumber = mobileNumber.replace("+86", "");
        }
        boolean flag = false;
        try {
            if (mobileNumber.startsWith("1") && mobileNumber.length() == 11) {
                flag = true;
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * @param mail 邮件
     * @return
     */
    public static Boolean checkEmail(String mail) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(mail);
        boolean isMatched = matcher.matches();
        return isMatched;
    }
}
