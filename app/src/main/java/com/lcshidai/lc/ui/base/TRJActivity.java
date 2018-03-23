package com.lcshidai.lc.ui.base;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tendcloud.tenddata.TCAgent;
import com.lcshidai.lc.AtrApplication;
import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.BroadCastImpl;
import com.lcshidai.lc.ui.AgreementActivity;
import com.lcshidai.lc.ui.GestureLoginActivity;
import com.lcshidai.lc.ui.ImageVerifyCodeRegisterActivity;
import com.lcshidai.lc.ui.LoadingActivity;
import com.lcshidai.lc.ui.LoginActivity;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.PublicImageViewActivity;
import com.lcshidai.lc.ui.SettingGestureLockActivity;
import com.lcshidai.lc.ui.WelcomeGuideActivity;
import com.lcshidai.lc.ui.account.BalancePaymentsActivity;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.ui.finance.InvestSuccessActivity;
import com.lcshidai.lc.ui.finance.MaterialActivity;
import com.lcshidai.lc.ui.more.DynamicInfoActivity;
import com.lcshidai.lc.ui.project.FundManagerActivity;
import com.lcshidai.lc.utils.BaseAppManager;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.NetUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.utils.UpdateManager;
import com.lcshidai.lc.widget.SlideFinishOnGestureListener;
import com.lcshidai.lc.widget.SlideFinishOnGestureListener.SlideDirection;
import com.lcshidai.lc.widget.ppwindow.DialogPopupWindow;

import org.json.JSONObject;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public abstract class TRJActivity extends FragmentActivity implements BroadCastImpl {

    public DialogPopupWindow dialogPopupWindow;
    private MyReceiver myReceiver;
    protected GestureDetector detector; // 触摸监听实例
    protected SlideFinishOnGestureListener gestureListener;
    protected SlideDirection slideDirection;
    protected Dialog loadingDialog = null;
    protected Context mContext;
    protected String tag = null;

    public boolean flag = false;
    // HomeKeyEventBroadCastReceiver receiver;
    protected AtrApplication mApplication;
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (MemorySave.MS.mIsFristrec) {
                        MemorySave.MS.mIsFristrec = false;
                        SharedPreferences xml = getSharedPreferences("res_info", 0);
                        String res_money = xml.getString("res_money", "");
                        String res_comment = xml.getString("res_comment", "");
                        String res_rule = xml.getString("res_rule", "");
                        dialogPopupWindow.setresValue(res_money, res_comment,
                                res_rule);
                    }
            }
        }
    };

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = this;
        tag = getClass().getSimpleName();
        BaseAppManager.getInstance().addActivity(this);
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MY_ACTION);
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(myReceiver, intentFilter);
        if (!isNotApplyTranslucent()) {
            applyTranslucent();
        }
    }

    protected abstract boolean isNotApplyTranslucent();


    protected void applyTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTheme(android.R.style.Theme_Holo_NoActionBar_TranslucentDecor);
        } else {
            setTheme(android.R.style.Theme_NoTitleBar);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageEnd(this, getPackageName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgent.onPageStart(this, getPackageName());

        MemorySave.MS.isrunResume = true;
        if (MemorySave.MS.goLockActFromPower) {
            MemorySave.MS.goLockActFromPower = false;
            GoLoginUtil.goGestureLoginActivity(TRJActivity.this, "");
        } else if (MemorySave.MS.goLockActFromHome) {
            MemorySave.MS.goLockActFromHome = false;
            long nowTime = new Date().getTime();
            if (nowTime - MemorySave.MS.locktime > 600000) {
                GoLoginUtil.goGestureLoginActivity(TRJActivity.this, "");
                // startActivity(new Intent(TRJActivity.this,
                // GestureLoginActivity.class));
            } else {
                MemorySave.MS.isGestureEnd = true;
            }
        }
        if (MemorySave.MS.isGestureEnd) {
            doGestureEnd();
        }
        MemorySave.MS.isGestureEnd = false;
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        //得到一个配置
        config.setToDefaults();
        //给这个配置设置成预设值，也就是fontScale的值(这里取值为1）
        res.updateConfiguration(config, res.getDisplayMetrics());
        //更新资源里面的配置
        return res;
    }

    protected void doGestureEnd() {

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        dialogPopupWindow = new DialogPopupWindow(this,
                ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), null);
        // mHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(myReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /**
     * post request to server for current activity
     *
     * @param url             the request url
     * @param params          the parameters wanted to submit to the server
     * @param responseHandler the response handler
     */
    public void post(String url, RequestParams params,
                     AsyncHttpResponseHandler responseHandler) {
        if (params == null)
            throw new IllegalArgumentException("RequestParams cannot be empty!");
        if (!NetUtils.isNetworkConnected(this)) {
            String message = "网络连接异常,请检查网络";
            ToastUtil.showToast((TRJActivity) mContext, message);
        }
        PackageManager manager = this.getPackageManager();
        ApplicationInfo app_info = null;
        try {
            app_info = manager.getApplicationInfo(this.getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String brand = android.os.Build.BRAND;// 手机品牌
        String model = android.os.Build.MODEL;// 手机型号
        String channel = app_info.metaData.getString("TD_CHANNEL_ID");
        params.put("hmsr", channel);
        params.put("brand", brand);
        params.put("model", model);
        LCHttpClient.post(this, url, params, responseHandler);
    }

    /**
     * show toast notification message
     *
     * @param message
     */
    public void showToast(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String goClass = getIntent().getStringExtra("goClass");
        if (requestCode == Constants.REQUEST_CODE) {
            if (resultCode == LoginActivity.LOGIN_SUCCESS) {
            } else {
                if (CommonUtil.isNullOrEmpty(goClass))
                    finish();
            }
            getIntent().removeExtra("goClass");
        }
    }

    /**
     * 显示加载dialog
     */
    protected void showLoadingDialog(Context context, String message, boolean cancelAble) {
        if (loadingDialog == null) {
            loadingDialog = createLoadingDialog(context, message, cancelAble);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    /**
     * 隐藏加载dialog
     */
    protected void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg,
                                             boolean cancelAble) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_custmor_loading, null);// 得到加载view
        TextView tipTextView = (TextView) view
                .findViewById(R.id.custmor_loadding_tv_message);// 提示文字
        tipTextView.setText(msg);// 设置加载信息
        Dialog loadingDialog = new Dialog(context, R.style.style_loading_dialog);// 创建自定义样式dialog
        loadingDialog.setContentView(view);
        loadingDialog.setCancelable(cancelAble);
        return loadingDialog;
    }

    /**
     * 自定义弹出框
     *
     * @return
     */
    public Dialog createDialog(CharSequence message, String absoluteStr,
                               String negativeStr, OnClickListener absoluteListener,
                               OnClickListener negativeListener) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_dialog_no_message,
                null);
        TextView titleTV = (TextView) view
                .findViewById(R.id.dialog_nomessage_tv_title);
        TextView absoluteBT = (TextView) view
                .findViewById(R.id.dialog_nomessage_bt_absolute);
        TextView negativeBT = (TextView) view
                .findViewById(R.id.dialog_nomessage_bt_negative);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        lp.width = (int) (screenWidth * 0.88);
        titleTV.setLayoutParams(lp);

        if (android.os.Build.VERSION.SDK_INT < 12) {
            absoluteBT
                    .setBackgroundResource(R.drawable.bg_dialog_button_absolute_12);
            negativeBT
                    .setBackgroundResource(R.drawable.bg_dialog_button_negative_12);
        } else {
            absoluteBT
                    .setBackgroundResource(R.drawable.bg_dialog_button_absolute);
            negativeBT
                    .setBackgroundResource(R.drawable.bg_dialog_button_negative);
        }

        titleTV.setText(message);
        absoluteBT.setText(absoluteStr);
        negativeBT.setText(negativeStr);

        absoluteBT.setOnClickListener(absoluteListener);
        negativeBT.setOnClickListener(negativeListener);

        Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
        dialog.setContentView(view);
        return dialog;
    }

    /**
     * 自定义弹出框(带标题)
     *
     * @return
     */
    public Dialog createDialog(String title, String message,
                               String absoluteStr, String negativeStr,
                               OnClickListener absoluteListener, OnClickListener negativeListener) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_dialog_have_message,
                null);
        TextView titleTV = (TextView) view
                .findViewById(R.id.dialog_message_tv_title);
        TextView messageTV = (TextView) view
                .findViewById(R.id.dialog_message_tv_message);
        Button absoluteBT = (Button) view
                .findViewById(R.id.dialog_message_bt_absolute);
        Button negativeBT = (Button) view
                .findViewById(R.id.dialog_message_bt_negative);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        lp.width = (int) (screenWidth * 0.88);
        titleTV.setLayoutParams(lp);

        if (android.os.Build.VERSION.SDK_INT < 12) {
            absoluteBT
                    .setBackgroundResource(R.drawable.bg_dialog_button_absolute_12);
            negativeBT
                    .setBackgroundResource(R.drawable.bg_dialog_button_negative_12);
        } else {
            absoluteBT
                    .setBackgroundResource(R.drawable.bg_dialog_button_absolute);
            negativeBT
                    .setBackgroundResource(R.drawable.bg_dialog_button_negative);
        }

        titleTV.setText(title);
        messageTV.setText(message);
        absoluteBT.setText(absoluteStr);
        negativeBT.setText(negativeStr);

        absoluteBT.setOnClickListener(absoluteListener);
        negativeBT.setOnClickListener(negativeListener);

        Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }

    /**
     * 自定义弹出框(带标题)
     * 认证
     *
     * @param title            标题
     * @param message          内容
     * @param absoluteStr      左边按钮
     * @param negativeStr      右边按钮
     * @param absoluteListener 左边监听
     * @param negativeListener 右边监听
     * @return
     */
    public Dialog createDialog1(String title, String message,
                                String absoluteStr, String negativeStr,
                                OnClickListener absoluteListener, OnClickListener negativeListener) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_dialog_renzheng,
                null);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.dialog_renzheng_ll);

        TextView titleTV = (TextView) view
                .findViewById(R.id.dialog_message_tv_title);
        TextView messageTV = (TextView) view
                .findViewById(R.id.dialog_message_tv_message);
        Button absoluteBT = (Button) view
                .findViewById(R.id.dialog_message_bt_absolute);
        Button negativeBT = (Button) view
                .findViewById(R.id.dialog_message_bt_negative);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        lp.width = (int) (screenWidth * 0.88);
        ll.setLayoutParams(lp);

		/*if (android.os.Build.VERSION.SDK_INT < 12) {
            absoluteBT
					.setBackgroundResource(R.drawable.bg_dialog_button_absolute_12);
			negativeBT
					.setBackgroundResource(R.drawable.bg_dialog_button_negative_12);
		} else {
			absoluteBT
					.setBackgroundResource(R.drawable.bg_dialog_button_absolute);
			negativeBT
					.setBackgroundResource(R.drawable.bg_dialog_button_negative);
		}*/

        titleTV.setText(title);
        messageTV.setText(message);
        absoluteBT.setText(absoluteStr);
        negativeBT.setText(negativeStr);

        absoluteBT.setOnClickListener(absoluteListener);
        negativeBT.setOnClickListener(negativeListener);

        Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }

    /**
     * 自定义弹出框(带标题一个按钮)
     *
     * @return
     */
    public Dialog createDialog(String title, String message,
                               String absoluteStr, OnClickListener absoluteListener) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_dialog_one_button,
                null);
        TextView titleTV = (TextView) view
                .findViewById(R.id.dialog_onebtn_tv_title);
        TextView messageTV = (TextView) view
                .findViewById(R.id.dialog_onebtn_tv_message);
        Button absoluteBT = (Button) view
                .findViewById(R.id.dialog_onebtn_bt_absolute);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        lp.width = (int) (screenWidth * 0.88);
        titleTV.setLayoutParams(lp);

        absoluteBT.setBackgroundResource(R.drawable.bg_dialog_one_button);

        // if(android.os.Build.VERSION.SDK_INT < 12){
        // absoluteBT.setBackgroundResource(R.drawable.bg_dialog_button_absolute_12);
        // }else{
        // absoluteBT.setBackgroundResource(R.drawable.bg_dialog_button_absolute);
        // }
        //
        titleTV.setText(title);
        messageTV.setText(message);
        absoluteBT.setText(absoluteStr);
        messageTV.setMovementMethod(ScrollingMovementMethod.getInstance());
        absoluteBT.setOnClickListener(absoluteListener);

        Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
        dialog.setContentView(view);
        return dialog;
    }

    /**
     * 自定义弹出框(两个button)
     *
     * @return
     */
    public Dialog createDialogTwoButton(String totle, String text1,
                                        SpannableString text2, String text3, String text4, String text5,
                                        OnClickListener absoluteListener, OnClickListener negativeListener) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(
                R.layout.layout_dialog_two_button_qifuxin, null);

        TextView titleTV = (TextView) view.findViewById(R.id.titleTV);
        TextView tvTotleMoneny = (TextView) view
                .findViewById(R.id.tvTotleMoneny);
        TextView text_1 = (TextView) view.findViewById(R.id.text_1);
        TextView text_2 = (TextView) view.findViewById(R.id.text_2);
        TextView text_3 = (TextView) view.findViewById(R.id.text_3);
        TextView text_4 = (TextView) view.findViewById(R.id.text_4);
        TextView text_5 = (TextView) view.findViewById(R.id.text_5);

        Button btOK = (Button) view.findViewById(R.id.btOK);
        Button btCancel = (Button) view.findViewById(R.id.btCancel);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        lp.width = (int) (screenWidth * 0.8);
        titleTV.setLayoutParams(lp);
        // titleTV.setText(title);

        tvTotleMoneny.setText(totle);
        text_1.setText(text1);
        text_2.setText(text2);
        text_3.setText(text3);
        text_4.setText(text4);
        text_5.setText(text5);

        final Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        btCancel.setOnClickListener(negativeListener);
        btOK.setOnClickListener(absoluteListener);
        return dialog;
    }

    /**
     * 自定义弹出框(带标题一个按钮,自动消失)
     *
     * @return
     */
    public void createDialogDismissAuto(String title, int drawable) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View toastView = layoutInflater.inflate(R.layout.toast_feedback_layout,
                null);
        TextView titleTV = (TextView) toastView
                .findViewById(R.id.toast_feedback_tv_message);
        ImageView imgToast = (ImageView) toastView.findViewById(R.id.imgToast);
        imgToast.setImageResource(drawable);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        lp.width = (int) (screenWidth * 0.88);
        titleTV.setLayoutParams(lp);

        titleTV.setText(title);

        final Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
        dialog.setContentView(toastView);
        dialog.show();

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // cancel the timer, otherwise, you may receive a
                // crash
            }
        }, 3000);

    }

    public void createDialogDismissAuto(String title) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View toastView = layoutInflater.inflate(R.layout.toast_dialog,
                null);
        TextView titleTV = (TextView) toastView
                .findViewById(R.id.toast_feedback_tv_message);
        ImageView imgToast = (ImageView) toastView.findViewById(R.id.imgToast);
        imgToast.setVisibility(View.GONE);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        lp.width = (int) (screenWidth * 0.7);
        lp.height = (int) (screenWidth * 0.4);
        titleTV.setLayoutParams(lp);

        titleTV.setText(title);

        final Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
        dialog.setContentView(toastView);
        dialog.show();

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // cancel the timer, otherwise, you may receive a
                // crash
            }
        }, 2000);

    }

    /**
     * 发送广播
     *
     * @param reson 广播来源说明
     */
    public void sendBroadcast(String reson) {
        Intent intent = new Intent();
        intent.setAction(MY_ACTION);
        intent.putExtra(MY_RESON, reson);
        sendBroadcast(intent);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            doReceive(context, intent);
        }
    }

    /**
     * 接收广播
     *
     * @param context
     * @param intent  广播来源
     */
    protected void doReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MY_ACTION)) {
            if (intent.getStringExtra(MY_RESON).equals(EXIT_EXTRA)) {
                TRJActivity.this.finish();
            }
        } else {
            // if(!(context instanceof TRJActivity))return;
            String action = intent.getAction();
            if (GoLoginUtil.isShowGestureLogin(TRJActivity.this)) {
                if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                    String reason = intent.getStringExtra(SYSTEM_REASON);
                    if (GoLoginUtil.isBackground(getApplicationContext())
                            && !MemorySave.MS.isrunResume)// 判断该程序是否在后台运行
                        return;
                    MemorySave.MS.isrunResume = false;
                    if (reason != null) {
                        if (reason.equals(SYSTEM_HOME_KEY)
                                || reason.equals(SYSTEM_RECENT_APPS)) {
                            MemorySave.MS.goLockActFromHome = true;
                            MemorySave.MS.locktime = new Date().getTime();
                        }
                    }
                } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                    if (!MemorySave.MS.isrunResume)// 判断该程序是否在后台运行
                        return;
                    MemorySave.MS.isrunResume = false;
                    MemorySave.MS.goLockActFromPower = true;
                }
            } else {//如果没设置手势只记录刷新
                if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                    String reason = intent.getStringExtra(SYSTEM_REASON);
                    if (reason != null) {
                        if (reason.equals(SYSTEM_HOME_KEY)
                                || reason.equals(SYSTEM_RECENT_APPS)) {
                            MemorySave.MS.isGestureEnd = true;
                        }
                    }
                } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                    MemorySave.MS.isGestureEnd = true;
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev == null) {
            return true;
        }
        if (flag) {// 过滤右划关闭监听时间
            return super.dispatchTouchEvent(ev);
        }
        if (this instanceof AbsSubActivity
                || this instanceof GestureLoginActivity
                || this instanceof InvestSuccessActivity
                || this instanceof AgreementActivity
                || this instanceof MainWebActivity
                || this instanceof DynamicInfoActivity
                || this instanceof WelcomeGuideActivity
                || this instanceof FinanceProjectDetailActivity
                || this instanceof MaterialActivity
                || this instanceof LoadingActivity
                || this instanceof BalancePaymentsActivity
                || this instanceof ImageVerifyCodeRegisterActivity
                || this instanceof FundManagerActivity
                || this instanceof PublicImageViewActivity
                || this instanceof SettingGestureLockActivity) {
            return super.dispatchTouchEvent(ev);
        }// 过滤右划关闭监听时间
        boolean isGesture = false;
        // if (GlobalVars.IS_ENABLE_GESTURE) {
        if (slideDirection == null) {
            slideDirection = SlideDirection.RIGHT;
        }
        if (detector == null) {
            gestureListener = new SlideFinishOnGestureListener(this,
                    slideDirection);
            detector = new GestureDetector(this, gestureListener);
        }
        isGesture = detector.onTouchEvent(ev);
        // }
        if (isGesture) {
            return isGesture;
        } else {
            try {
                return super.dispatchTouchEvent(ev);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    public SlideDirection getSlideDirection() {
        return slideDirection;
    }

    public void setSlideDirection(SlideDirection slideDirection) {
        this.slideDirection = slideDirection;
        if (gestureListener != null) {
            gestureListener.setSlideDirection(slideDirection);
        }
    }

    /**
     * 右划关闭回调
     */
    public void doFinish() {
        this.finish();
    }

    /**
     * 解析String类型的Json数据
     */
    public String resolveJsonToString(JSONObject obj, String key,
                                      String ifNull, String danwei) {
        if (!obj.isNull(key)) {
            if (!TextUtils.isEmpty(obj.optString(key))) {
                return obj.optString(key) + danwei;
            }
        }
        return ifNull;

    }

    public String resolveString(String obj, String key, String ifNull,
                                String danwei) {
        if (obj != null) {
            if (!TextUtils.isEmpty(obj)) {
                return obj + danwei;
            }
        }
        return ifNull;

    }

    /**
     * 判断是否有新的版本
     */
    protected void checkAppUpdate() {
        UpdateManager.reset();
        UpdateManager.getInstance(this).checkUpdateInfo(this, 0);
    }

    /**
     * 取消Activity的所有请求
     */
    public void cancelAllRequest() {
        LCHttpClient.cancelRequest();
    }
}
