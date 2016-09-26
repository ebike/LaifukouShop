package com.sdjy.sdjymall.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.MyTeamActivity;
import com.sdjy.sdjymall.activity.TeamGoodsInfoActivity;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.constants.FinalValues;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.OrderInfoModel;
import com.sdjy.sdjymall.model.PayResult;
import com.sdjy.sdjymall.model.TeamModel;
import com.sdjy.sdjymall.subscribers.NoProgressSubscriber;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.CommonUtils;
import com.sdjy.sdjymall.util.StringUtils;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 创建或加入团队
 */
public class CreateOrAddTeamDialog {

    @Bind(R.id.tv_add_team)
    TextView addTeamView;
    @Bind(R.id.tv_create_team)
    TextView createTeamView;
    @Bind(R.id.ll_add_team)
    LinearLayout addTeamLayout;
    @Bind(R.id.ll_create_team)
    LinearLayout createTeamLayout;
    @Bind(R.id.et_phone)
    EditText phoneText;
    @Bind(R.id.tv_team_name)
    TextView teamNameView;
    @Bind(R.id.et_team_name)
    EditText teamNameText;

    private Context context;
    private LayoutInflater inflate;
    private Display display;
    private Dialog dialog;
    private String goodsId;
    private TeamModel teamModel;
    private String addOrCreate;
    private OrderInfoModel infoModel;

    public CreateOrAddTeamDialog(Context context, String goodsId) {
        this.context = context;
        inflate = LayoutInflater.from(context);
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        this.goodsId = goodsId;
    }

    public CreateOrAddTeamDialog builder() {
        //初始化显示的view
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_create_or_add_team, null);
        ButterKnife.bind(this, view);

        phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = phoneText.getText().toString();
                if (!StringUtils.strIsEmpty(phone)
                        && phone.length() == 11
                        && CommonUtils.isPhoneNumber(phone)) {
                    HttpMethods.getInstance().checkRefereeUser(new NoProgressSubscriber<TeamModel>(new SubscriberOnNextListener<TeamModel>() {
                        @Override
                        public void onNext(TeamModel model) {
                            teamModel = model;
                            teamNameView.setText(teamModel.teamName);
                        }
                    }, context), goodsId, phone);
                }
            }
        });

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

    @OnClick(R.id.tv_add_team)
    public void addTeam() {
        addTeamView.setBackgroundColor(context.getResources().getColor(R.color.red1));
        createTeamView.setBackgroundColor(context.getResources().getColor(R.color.white));
        addTeamView.setTextColor(context.getResources().getColor(R.color.white));
        createTeamView.setTextColor(context.getResources().getColor(R.color.red1));
        addTeamLayout.setVisibility(View.VISIBLE);
        createTeamLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_create_team)
    public void createTeam() {
        addTeamView.setBackgroundColor(context.getResources().getColor(R.color.white));
        createTeamView.setBackgroundColor(context.getResources().getColor(R.color.red1));
        addTeamView.setTextColor(context.getResources().getColor(R.color.red1));
        createTeamView.setTextColor(context.getResources().getColor(R.color.white));
        addTeamLayout.setVisibility(View.GONE);
        createTeamLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_add)
    public void add() {
        String phone = phoneText.getText().toString();
        if (!StringUtils.strIsEmpty(phone)
                && phone.length() == 11
                && CommonUtils.isPhoneNumber(phone)
                && teamModel != null) {
            HttpMethods.getInstance().payJoinTeam(new ProgressSubscriber<OrderInfoModel>(new SubscriberOnNextListener<OrderInfoModel>() {
                @Override
                public void onNext(OrderInfoModel model) {
                    addOrCreate = "add";
                    infoModel = model;
                    pay(model.orderInfo);
                }
            }, context), teamModel.refereeId, goodsId, "alipay");
        } else {
            T.showShort(context, "您输入的手机号有误");
        }
    }

    @OnClick(R.id.btn_create)
    public void create() {
        String teamName = teamNameText.getText().toString();
        if (!StringUtils.strIsEmpty(teamName)) {
            HttpMethods.getInstance().payCreateTeam(new ProgressSubscriber<OrderInfoModel>(new SubscriberOnNextListener<OrderInfoModel>() {
                @Override
                public void onNext(OrderInfoModel model) {
                    addOrCreate = "create";
                    infoModel = model;
                    pay(model.orderInfo);
                }
            }, context), teamName, goodsId, "alipay");
        } else {
            T.showShort(context, "请给团队起个名吧");
        }
    }

    private void pay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) context);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FinalValues.SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (!StringUtils.strIsEmpty(addOrCreate) && "add".equals(addOrCreate)) {
                            HttpMethods.getInstance().appNotifyJoinTeam(new ProgressSubscriber(new SubscriberOnNextListener() {
                                @Override
                                public void onNext(Object o) {
                                    dialog.dismiss();
                                    T.showShort(context, "成功加入团队");
                                    EventBus.getDefault().post(new RefreshEvent(TeamGoodsInfoActivity.class.getSimpleName()));
                                    context.startActivity(new Intent(context, MyTeamActivity.class));
                                }
                            }, context), infoModel.orderId);
                        } else if (!StringUtils.strIsEmpty(addOrCreate) && "create".equals(addOrCreate)) {
                            HttpMethods.getInstance().appNotifyCreateTeam(new ProgressSubscriber(new SubscriberOnNextListener() {
                                @Override
                                public void onNext(Object o) {
                                    dialog.dismiss();
                                    T.showShort(context, "成功创建团队");
                                    EventBus.getDefault().post(new RefreshEvent(TeamGoodsInfoActivity.class.getSimpleName()));
                                    context.startActivity(new Intent(context, MyTeamActivity.class));
                                }
                            }, context), infoModel.orderId);
                        }
                    } else {
                        T.showShort(context, "支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
