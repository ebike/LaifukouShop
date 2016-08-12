package com.sdjy.sdjymall.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.util.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 商品-商品-滚动图片
 */
public class GoodsPicsFragment extends Fragment {

    @Bind(R.id.iv_pic)
    ImageView picView;

    private String goodsPic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_pics, container, false);
        ButterKnife.bind(this, view);

        if (!StringUtils.strIsEmpty(goodsPic)) {
            Glide.with(getActivity())
                    .load(goodsPic)
                    .error(R.mipmap.img_goods_default)
                    .into(picView);
        }

        return view;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }
}
