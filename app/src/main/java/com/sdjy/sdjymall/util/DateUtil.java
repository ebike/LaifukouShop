package com.sdjy.sdjymall.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    // 默认日期转换格式
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    //转化时间格式
    public static String DateToString(Date date, String format) {
        if (StringUtils.strIsEmpty(format)) {
            format = DEFAULT_DATE_FORMAT;
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    // 取得当前日期
    public static String getCurrentDateString(String format) {
        if (StringUtils.strIsEmpty(format)) {
            format = DEFAULT_DATE_FORMAT;
        }
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(d);
    }

    //时间差大于一周(到今天为止)
    public static boolean moreThanAWeek(String start) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            Date startDate = df.parse(start);
            Date curDate = new Date();
            long t1 = startDate.getTime();
            long t2 = curDate.getTime();
            if (t2 - t1 > 7 * 24 * 60 * 60 * 1000) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
