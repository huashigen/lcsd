package com.lcshidai.lc.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcshidai.lc.R;

import java.util.Timer;
import java.util.TimerTask;

public class ToastUtil {
    private static Toast mToast;

    public static void showToast(final Activity activity, final String message) {
        if (null != activity) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
                    } else {
                        mToast.setText(message);
                        mToast.setDuration(Toast.LENGTH_SHORT);
                    }
                    mToast.show();
                }
            });
        }
    }

    public static void showToast(final Context context, final String message) {
        if (null != context) {
            ((Activity) context).runOnUiThread(new Runnable() {
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    } else {
                        mToast.setText(message);
                        mToast.setDuration(Toast.LENGTH_SHORT);
                    }
                    mToast.show();
                }
            });
        }
    }

    public static void showLongToast(Context context, String message) {
        if (null != context) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            } else {
                mToast.setText(message);
                mToast.setDuration(Toast.LENGTH_LONG);
            }
            mToast.show();
        }
    }

    public static void showLongToast(Context context, int strResId) {
        if (null != context) {
            if (mToast == null) {
                mToast = Toast.makeText(context, context.getString(strResId), Toast.LENGTH_LONG);
            } else {
                mToast.setText(context.getString(strResId));
                mToast.setDuration(Toast.LENGTH_LONG);
            }
            mToast.show();
        }
    }

    public static void showShortToast(Context context, String message) {
        if (null != context) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.show();
        }
    }

    public static void showShortToast(Context context, int strResId) {
        if (null != context) {
            if (mToast == null) {
                mToast = Toast.makeText(context, context.getString(strResId), Toast.LENGTH_SHORT);
            } else {
                mToast.setText(context.getString(strResId));
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.show();
        }
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 自定义弹出框(带标题，自动消失，可设置背景)
     *
     * @param context  上下文对象
     * @param title    标题
     * @param drawable 背景图片
     */
    public static void createDialogDismissAuto(Context context, String title, int drawable) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View toastView = layoutInflater.inflate(R.layout.toast_feedback_layout, null);
        TextView titleTV = (TextView) toastView.findViewById(R.id.toast_feedback_tv_message);
        ImageView imgToast = (ImageView) toastView.findViewById(R.id.imgToast);
        imgToast.setImageResource(drawable);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.width = (int) (screenWidth * 0.88);
        titleTV.setLayoutParams(lp);
        titleTV.setText(title);

        final Dialog dialog = new Dialog(context, R.style.style_loading_dialog);
        dialog.setContentView(toastView);
        dialog.show();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();   // when the task active then close the dialog
                timer.cancel();     // cancel the timer, otherwise, you may receive a crash
            }
        }, 3000);

    }

    /**
     * 自定义弹出框（带标题，自动消失）
     *
     * @param context 上下文
     * @param title   标题
     */
    public static void createDialogDismissAuto(Context context, String title) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View toastView = layoutInflater.inflate(R.layout.toast_dialog, null);
        TextView titleTV = (TextView) toastView.findViewById(R.id.toast_feedback_tv_message);
        ImageView imgToast = (ImageView) toastView.findViewById(R.id.imgToast);
        imgToast.setVisibility(View.GONE);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.width = (int) (screenWidth * 0.7);
        lp.height = (int) (screenWidth * 0.4);
        titleTV.setLayoutParams(lp);

        titleTV.setText(title);

        final Dialog dialog = new Dialog(context, R.style.style_loading_dialog);
        dialog.setContentView(toastView);
        dialog.show();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();   // when the task active then close the dialog
                timer.cancel();     // cancel the timer, otherwise, you may receive a crash
            }
        }, 2000);

    }
}
