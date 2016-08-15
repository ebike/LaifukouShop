package com.sdjy.sdjymall.common.util;

import android.content.Context;
import android.view.View;
import com.sdjy.sdjymall.util.StringUtils;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * 弹出框工具类
 */
public class DialogUtils {

    private static MaterialDialog materialDialog;

    public static MaterialDialog showDialog(Context context,
                                            String title,
                                            String message,
                                            View view,
                                            String positiveText,
                                            String negativeText,
                                            final View.OnClickListener positiveListener,
                                            final View.OnClickListener negativeListener) {

        materialDialog = new MaterialDialog(context)
                .setCanceledOnTouchOutside(true);

        if (!StringUtils.strIsEmpty(title)) {
            materialDialog.setTitle(title);
        }
        if (!StringUtils.strIsEmpty(message)) {
            materialDialog.setMessage(message);
        }
        if (view != null) {
            materialDialog.setContentView(view);
        }
        String positive = "确定";
        if (!StringUtils.strIsEmpty(positiveText)) {
            positive = positiveText;
        }
        materialDialog.setPositiveButton(positive, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                if (positiveListener != null) {
                    positiveListener.onClick(v);
                }
            }
        });
        if(!StringUtils.strIsEmpty(negativeText) || negativeListener != null){
            String negative = "取消";
            if (!StringUtils.strIsEmpty(negativeText)) {
                negative = negativeText;
            }
            materialDialog.setNegativeButton(negative, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialDialog.dismiss();
                    if (negativeListener != null) {
                        negativeListener.onClick(v);
                    }
                }
            });
        }
        materialDialog.show();

        return materialDialog;
    }

    public static MaterialDialog showDialog(Context context,
                                            String message) {
        return showDialog(context, null, message, null, null, null, null, null);
    }

    public static MaterialDialog showDialog(Context context,
                                            String title,
                                            String message) {
        return showDialog(context, title, message, null, null, null, null, null);
    }

    public static MaterialDialog showDialog(Context context,
                                            String message,
                                            final View.OnClickListener positiveListener) {
        return showDialog(context, null, message, null, null, null, positiveListener, null);
    }

    public static MaterialDialog showDialog(Context context,
                                            String message,
                                            final View.OnClickListener positiveListener,
                                            String negativeText) {
        return showDialog(context, null, message, null, null, negativeText, positiveListener, null);
    }

    public static MaterialDialog showDialog(Context context,
                                            String title,
                                            String message,
                                            final View.OnClickListener positiveListener) {
        return showDialog(context, title, message, null, null, null, positiveListener, null);
    }

    public static MaterialDialog showDialog(Context context,
                                            String title,
                                            String message,
                                            final View.OnClickListener positiveListener,
                                            String negativeText) {
        return showDialog(context, title, message, null, null, negativeText, positiveListener, null);
    }

    public static MaterialDialog showDialog(Context context,
                                            String message,
                                            String positiveText,
                                            String negativeText,
                                            final View.OnClickListener positiveListener) {
        return showDialog(context, null, message, null, positiveText, negativeText, positiveListener, null);
    }

    public static MaterialDialog showDialog(Context context,
                                            String title,
                                            String message,
                                            String positiveText,
                                            String negativeText,
                                            final View.OnClickListener positiveListener) {
        return showDialog(context, title, message, null, positiveText, negativeText, positiveListener, null);
    }

    public static MaterialDialog showDialog(Context context,
                                            String title,
                                            String message,
                                            String positiveText,
                                            String negativeText,
                                            final View.OnClickListener positiveListener,
                                            final View.OnClickListener negativeListener) {
        return showDialog(context, title, message, null, positiveText, negativeText, positiveListener, negativeListener);
    }

    public static MaterialDialog showDialog(Context context,
                                            String title,
                                            View view,
                                            final View.OnClickListener positiveListener,
                                            String negativeText,
                                            final View.OnClickListener negativeListener) {
        return showDialog(context, title, null, view, null, negativeText, positiveListener, negativeListener);
    }

    public static MaterialDialog showDialog(Context context,
                                            String title,
                                            String message,
                                            View view,
                                            final View.OnClickListener positiveListener,
                                            String negativeText) {
        return showDialog(context, title, message, view, null, negativeText, positiveListener, null);
    }

    public static MaterialDialog showDialog_OneB(Context context,
                                                 String message,
                                                 String positiveText) {
        return showDialog(context, null, message, null, positiveText, null, null, null);
    }

    public static MaterialDialog showDialog_OneB(Context context,
                                                 String message,
                                                 String positiveText,
                                                 View.OnClickListener positiveListener) {
        return showDialog(context, null, message, null, positiveText, null, positiveListener, null);
    }
}
