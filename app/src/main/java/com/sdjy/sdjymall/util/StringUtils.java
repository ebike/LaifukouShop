package com.sdjy.sdjymall.util;

/**
 * 字符串为空的判断
 * Created by dive on 2015/7/8.
 */
public class StringUtils {

    //判断是否为空
    public static Boolean strIsEmpty(String orgStr) {
        if (orgStr == null || orgStr.equals("null") || orgStr.length() <= 0) {
            return true;
        }
        return false;
    }

    //字符串最后一位是小数点，则去掉小数点
    public static String removeLastPoint(String number) {
        char c = number.charAt(number.length() - 1);
        if (".".equals(c + "")) {
            return number.substring(0, number.length() - 1);
        }
        return number;
    }
}
