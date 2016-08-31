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
 * 创建或加入团队
 */
public class CreateOrAddTeamDialog {

    @Bind(R.id.tag_flow)
    TagFlowLayout flowLayout;
    @Bind(R.id.tv_count)
    TextView countView;

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

    public CreateOrAddTeamDialog(Context context, List<GoodsPricesModel> goodsPricesList) {
        this.context = context;
        inflate = LayoutInflater.from(context);
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        this.goodsPricesList = goodsPricesList;
    }

    public CreateOrAddTeamDialog builder() {
        //初始化显示的view
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_create_or_add_team, null);
        ButterKnife.bind(this, view);

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
                    if (callback != null) {
                        callback.callback(goodsPricesList.get(position));
                    }
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

    public CreateOrAddTeamDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public CreateOrAddTeamDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        dialog.show();
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
