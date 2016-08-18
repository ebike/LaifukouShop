package com.sdjy.sdjymall.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IRecyclerviewLoadMoreFooterView extends FrameLayout {
    /**
     * 动画插值
     */
    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    /**
     * 旋转动画的时间
     */
    static final int ROTATION_ANIMATION_DURATION = 1200;
    /**
     * 旋转的动画
     */
    private Animation mRotateAnimation;
    @Bind(R.id.iv_rotate)
    ImageView rotateView;
    @Bind(R.id.tv_desc)
    TextView descView;

    public IRecyclerviewLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public IRecyclerviewLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IRecyclerviewLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = inflate(context, R.layout.irecyclerview_refresh_load_view, this);
        ButterKnife.bind(view);

        float pivotValue = 0.5f;    // SUPPRESS CHECKSTYLE
        float toDegree = 720.0f;    // SUPPRESS CHECKSTYLE
        mRotateAnimation = new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateAnimation.setFillAfter(true);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);

        descView.setText("数据加载中");
        rotateView.startAnimation(mRotateAnimation);

    }

}
