package com.sdjy.sdjymall.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.model.GoodsPricesModel;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择商品规格弹出框
 */
public class ChooseStandardDialog {

    @Bind(R.id.tag_flow)
    TagFlowLayout flowLayout;
    @Bind(R.id.tv_count)
    TextView countView;
    @Bind(R.id.tv_into_car)
    TextView intoCarView;

    private Context context;
    private LayoutInflater inflate;
    private Display display;
    private Dialog dialog;
    private List<GoodsPricesModel> goodsPricesList;
    private ChooseStandardCallback callback;
    private ChangeCountCallback countCallback;
    private View.OnClickListener intoCarCallback;
    private int selectedPos = 0;
    private int count = 1;
    private int state;

    public ChooseStandardDialog(Context context, List<GoodsPricesModel> goodsPricesList, int state) {
        this.context = context;
        inflate = LayoutInflater.from(context);
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        this.goodsPricesList = goodsPricesList;
        this.state = state;
    }

    public ChooseStandardDialog builder() {
        //初始化显示的view
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_choose_standard, null);
        ButterKnife.bind(this, view);

        showBtnState(selectedPos);

        TagAdapter adapter = new TagAdapter<GoodsPricesModel>(goodsPricesList) {
            @Override
            public View getView(FlowLayout parent, int position, GoodsPricesModel pricesModel) {
                TextView tv = (TextView) inflate.inflate(R.layout.view_standard,
                        flowLayout, false);
                tv.setText(pricesModel.standard);
                return tv;
            }

            @Override
            public boolean setSelected(int position, GoodsPricesModel pricesModel) {
                return position == selectedPos;
            }
        };
        flowLayout.setAdapter(adapter);

        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (selectedPos != position) {
                    selectedPos = position;
                    showBtnState(selectedPos);
                }
                return true;
            }
        });

        countView.setText(count + "");

        //创建dialog
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = display.getWidth();
        dialogWindow.setAttributes(lp);
        return this;
    }

    public ChooseStandardDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ChooseStandardDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    private void showBtnState(int position) {
        GoodsPricesModel model = goodsPricesList.get(position);
        if (state != 1) {
            intoCarView.setBackgroundColor(context.getResources().getColor(R.color.yellow1));
            intoCarView.setText("商品已下架");
        } else if (model.stock <= 0) {
            intoCarView.setBackgroundColor(context.getResources().getColor(R.color.yellow1));
            intoCarView.setText("暂时缺货");
        } else {
            intoCarView.setBackgroundColor(context.getResources().getColor(R.color.red));
            intoCarView.setText("加入购物车");
        }
        if (callback != null) {
            callback.callback(model);
        }
    }

    @OnClick(R.id.tv_minus)
    public void minus() {
        if (count > 1) {
            count--;
            countView.setText(count + "");
            if (countCallback != null) {
                countCallback.changed(count);
            }
        }
    }

    @OnClick(R.id.tv_plus)
    public void plus() {
        count++;
        countView.setText(count + "");
        if (countCallback != null) {
            countCallback.changed(count);
        }
    }

    @OnClick(R.id.tv_into_car)
    public void intoCar(View v) {
        if (state != 1 || goodsPricesList.get(selectedPos).stock <= 0) {
            return;
        }

        dialog.dismiss();
        if (intoCarCallback != null) {
            intoCarCallback.onClick(v);
        }
    }

    public interface ChooseStandardCallback {
        void callback(GoodsPricesModel selectedPricesModel);
    }

    public void setCallback(ChooseStandardCallback callback) {
        this.callback = callback;
    }

    public interface ChangeCountCallback {
        void changed(int count);
    }

    public void setCountCallback(ChangeCountCallback countCallback) {
        this.countCallback = countCallback;
    }

    public void setIntoCarCallback(View.OnClickListener intoCarCallback) {
        this.intoCarCallback = intoCarCallback;
    }
}
