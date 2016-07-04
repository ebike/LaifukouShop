package com.laifukou.laifukoushop.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.laifukou.laifukoushop.common.model.TabIndicator;
import com.laifukou.laifukoushop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager工具类, 用于提供ViewPager相关的公用方法
 */
public class ViewPagerUtil {

    /**
     * 获取ViewPager的TabIndicator列表
     */
    public static List<TabIndicator> getTabIndicator(Integer number) {
        List<TabIndicator> list = new ArrayList<TabIndicator>();
        for (int i = 0; i < number; i++) {
            TabIndicator indicator = new TabIndicator();
            indicator.position = i;
            list.add(indicator);
        }
        return list;
    }

    /**
     * 变换底部选项卡图标和字体颜色
     *
     * @param context
     * @param index
     * @param textViews
     */
    public static void changeBottomTab(Context context, int index, TextView[] textViews) {
        Resources resources = context.getResources();
        TypedArray hoverIcons = resources.obtainTypedArray(R.array.bottom_tab_icon_hover);
        TypedArray icons = resources.obtainTypedArray(R.array.bottom_tab_icon);

        for (int i = 0; i < textViews.length; i++) {
            TextView textView = textViews[i];
            if (index == i) {
                textView.setTextColor(context.getResources().getColor(R.color.text_selected));
                Drawable drawable = resources.getDrawable(hoverIcons.getResourceId(i, 0));
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(null, drawable, null, null);
            } else {
                textView.setTextColor(context.getResources().getColor(R.color.text_gray));
                Drawable drawable = resources.getDrawable(icons.getResourceId(i, 0));
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(null, drawable, null, null);
            }
        }
    }
}
