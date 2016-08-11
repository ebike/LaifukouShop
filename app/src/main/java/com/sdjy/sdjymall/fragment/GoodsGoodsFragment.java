package com.sdjy.sdjymall.fragment;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.common.fragment.LazyFragment;

import butterknife.Bind;

/**
 * 商品-商品
 */
public class GoodsGoodsFragment extends LazyFragment {

    @Bind(R.id.tv_name)
    TextView nameView;
    @Bind(R.id.tv_amount)
    TextView amountView;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_goods_goods);

        ImageSpan span = new ImageSpan(getActivity(), R.mipmap.icon_goodsinfo_cart);
        String str = "这个是什么的的短款的款式多的肯定是打打杀杀颠三倒四打瞌睡打瞌睡的说到底的时刻山东省开始";
        String space = " ";
        str = str + space;
        int strLength = str.length();
        SpannableString ss = new SpannableString(str);
        ss.setSpan(span, strLength - 1, strLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        nameView.setText(ss);

    }
}
