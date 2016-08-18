package com.sdjy.sdjymall.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.irecyclerview.RefreshTrigger;
import com.sdjy.sdjymall.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IRecyclerviewRefreshHeaderView extends RelativeLayout implements RefreshTrigger {
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

    private int mHeight;

    public IRecyclerviewRefreshHeaderView(Context context) {
        this(context, null);
    }

    public IRecyclerviewRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IRecyclerviewRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    }

    @Override
    public void onStart(boolean automatic, int headerHeight, int finalHeight) {
        this.mHeight = headerHeight;
    }

    @Override
    public void onMove(boolean isComplete, boolean automatic, int moved) {
        if (!isComplete) {
            float angle = moved / mHeight * 180f; // SUPPRESS CHECKSTYLE
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                rotateView.setRotation(angle);
            }
            if (moved <= mHeight) {
                descView.setText("下拉刷新");
            } else {
                descView.setText("松开后刷新");
            }
        }
    }

    @Override
    public void onRefresh() {
        resetRotation();
        rotateView.startAnimation(mRotateAnimation);
        descView.setText("数据加载中");
    }

    @Override
    public void onRelease() {
        descView.setText("松开后刷新");
    }

    @Override
    public void onComplete() {
        descView.setText("加载完成");
    }

    @Override
    public void onReset() {
        resetRotation();
        descView.setText("下拉刷新");
    }

    /**
     * 重置动画
     */
    private void resetRotation() {
        rotateView.clearAnimation();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            rotateView.setRotation(0);
        }
    }
}
