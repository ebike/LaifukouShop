package com.sdjy.sdjymall.view.pullrefresh;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.util.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


public class EmptyViewForList extends RelativeLayout {

    private View rootView;
    @Bind(R.id.tv_desc)
    TextView descView;
    @Bind(R.id.tv_desc1)
    TextView descView1;

    private String content;

    public EmptyViewForList(Context context) {
        this(context, null);
    }

    public EmptyViewForList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyViewForList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.InfoView, defStyle, 0);

        content = a.getString(R.styleable.InfoView__content);
        a.recycle();

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_empty_for_list, this, true);
        ButterKnife.bind(this, rootView);

        if (!StringUtils.strIsEmpty(content)) {
            descView.setText(content);
        }
    }

    public void setContent(String content) {
        this.content = content;
        if (!StringUtils.strIsEmpty(content)) {
            descView.setText(content);
        }
    }

    public void setContent1(String content) {
        if (!StringUtils.strIsEmpty(content)) {
            descView1.setVisibility(View.VISIBLE);
            descView1.setText(content);
        }
    }
}
