package com.sdjy.sdjymall.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.common.util.DensityUtils;
import com.sdjy.sdjymall.util.CommonUtils;
import com.sdjy.sdjymall.util.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 横向的条目组件
 * 包含：左侧标题、右测内容、上下横线（0：无线；1：长线；2：短线）
 */
public class RowLabelValueView extends RelativeLayout {
    private View rootView;
    @Bind(R.id.v_top_line)
    View topLineView;
    @Bind(R.id.tv_label)
    TextView labelTextView;
    @Bind(R.id.tv_value)
    TextView valueTextView;
    @Bind(R.id.iv_arrow)
    ImageView arrowImageView;
    @Bind(R.id.v_bottom_line)
    View bottomLineView;
    //顶部横线
    private int topLine;
    //左侧文字
    private String label;
    //右侧文字
    private String value;
    //含右侧箭头
    private boolean hasRightArrow;
    //底部横线
    private int bottomLine;
    //可点击
    private boolean canClick;
    //点击事件回调接口
    private OnClickCallback onClickCallback;

    public interface OnClickCallback {
        void onClick(View view);
    }

    public RowLabelValueView(Context context) {
        this(context, null);
    }

    public RowLabelValueView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowLabelValueView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取字段信息
        TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.RowLabelValueView, defStyleAttr, 0);
        topLine = typedArray.getInt(R.styleable.RowLabelValueView_row_label_value_top_line, 0);
        label = typedArray.getString(R.styleable.RowLabelValueView_row_label_value_label);
        value = typedArray.getString(R.styleable.RowLabelValueView_row_label_value_value);
        hasRightArrow = typedArray.getBoolean(R.styleable.RowLabelValueView_row_label_value_has_right_arrow, false);
        bottomLine = typedArray.getInt(R.styleable.RowLabelValueView_row_label_value_bottom_line, 0);
        canClick = typedArray.getBoolean(R.styleable.RowLabelValueView_row_label_value_can_click, false);
        typedArray.recycle();

        //获取布局文件
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.view_row_label_value, this, true);
        ButterKnife.bind(this, rootView);
        //初始化组件
        //显示顶部横线
        if (topLine == 1) {
            topLineView.setVisibility(View.VISIBLE);
        } else if (topLine == 2) {
            topLineView.setVisibility(View.VISIBLE);
            CommonUtils.setMargins(topLineView, DensityUtils.dp2px(context, 16), 0, 0, 0);
        } else {
            topLineView.setVisibility(View.GONE);
        }
        //左侧文字
        if (!StringUtils.strIsEmpty(label)) {
            labelTextView.setText(label);
        }
        //右侧文字
        if (!StringUtils.strIsEmpty(value)) {
            valueTextView.setText(value);
        }
        //右侧箭头显示
        if (hasRightArrow) {
            arrowImageView.setVisibility(View.VISIBLE);
        } else {
            arrowImageView.setVisibility(View.GONE);
        }
        //显示底部横线
        if (bottomLine == 1) {
            bottomLineView.setVisibility(View.VISIBLE);
        } else if (bottomLine == 2) {
            bottomLineView.setVisibility(View.VISIBLE);
            CommonUtils.setMargins(bottomLineView, DensityUtils.dp2px(context, 16), 0, 0, 0);
        } else {
            bottomLineView.setVisibility(View.GONE);
        }
        //是否可点击
        if (canClick) {
            rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickCallback != null) {
                        onClickCallback.onClick(view);
                    }
                }
            });
        }
    }

    public void setOnClickCallback(OnClickCallback onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    public void setLabel(String label) {
        labelTextView.setText(label);
    }

    public void setValue(String value) {
        if (!StringUtils.strIsEmpty(value)) {
            valueTextView.setVisibility(View.VISIBLE);
            valueTextView.setText(value);
        } else {
            valueTextView.setVisibility(View.GONE);
            valueTextView.setText("");
        }
    }

    public void setValueColor(int color) {
        valueTextView.setTextColor(getResources().getColor(color));
    }

    public String getValue() {
        return valueTextView.getText().toString();
    }

    public void setRightImage(int resource) {
        arrowImageView.setImageResource(resource);
    }
}
