package com.lcshidai.lc.ui.account.pwdmanage;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.GesturePwdImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.account.HttpGesturePwdService;
import com.lcshidai.lc.ui.SettingGestureLockActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.LockPatternUtils;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIconPwd;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 手势密码
 */
public class GesturePwdActivity extends TRJActivity implements GesturePwdImpl {
    HttpGesturePwdService hgps;
    private static final int GESTURE_ON = 0;          //开启手势
    private static final int GESTURE_OFF = 1;        //关闭手势
    private static final int GESTURE_UPDATER = 2;       //修改手势密码
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    private LinearLayout ll_gesture;
    private ToggleButton sbtn_gesture_setting;
    private Dialog mExitDialog;
    private CustomEditTextLeftIconPwd et_pwd;
    private CustomEditTextLeftIcon edit_uname;
    private boolean isGestureLockOpen = false;    //true：手势密码打开	else手势密码关闭
    private int gesture_status = -1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_pwd);
        hgps = new HttpGesturePwdService(this, this);
        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        mExitDialog = createDialog();
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("手势密码");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        ll_gesture = (LinearLayout) findViewById(R.id.ll_gesture);
        sbtn_gesture_setting = (ToggleButton) findViewById(R.id.sbtn_gesture_setting);
        sbtn_gesture_setting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gesture_status = GESTURE_ON;
                } else {
                    gesture_status = GESTURE_OFF;
                }
                if (et_pwd != null) et_pwd.setText("");
                if (mExitDialog != null) mExitDialog.show();
            }
        });
        ll_gesture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_pwd != null) et_pwd.setText("");
                if (mExitDialog != null) mExitDialog.show();
                gesture_status = GESTURE_UPDATER;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGestureLockOpen = SpUtils.getBoolean(SpUtils.Table.CONFIG,
                SpUtils.Config.IS_GESTURE_OPEN, false);
        if (isGestureLockOpen) {
            ll_gesture.setVisibility(View.VISIBLE);
            sbtn_gesture_setting.setChecked(true);
        } else {
            ll_gesture.setVisibility(View.GONE);
            sbtn_gesture_setting.setChecked(false);
        }

        if (mExitDialog != null && mExitDialog.isShowing()) {
            mExitDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mExitDialog != null && mExitDialog.isShowing()) {
            mExitDialog.dismiss();
        }
    }

    private LinearLayout ll_gesture_login;

    private Dialog createDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_dialog_login, null);
        edit_uname = (CustomEditTextLeftIcon) view.findViewById(R.id.edit_uname);
        ll_gesture_login = (LinearLayout) view.findViewById(R.id.dialog_gesture_login_ll);
        et_pwd = (CustomEditTextLeftIconPwd) view.findViewById(R.id.edit_pwd);
        Button absoluteBT = (Button) view.findViewById(R.id.dialog_nomessage_bt_absolute);
        Button negativeBT = (Button) view.findViewById(R.id.dialog_nomessage_bt_negative);

        Drawable icon_pwd = getResources().getDrawable(R.drawable.icon_pwd);
        Drawable icon_user = getResources().getDrawable(R.drawable.icon_user);
        edit_uname.setHint("用户名、手机号、邮箱");
        edit_uname.setTextSize(14);
        et_pwd.setTextSize(14);
        et_pwd.setHint("请输入登录密码");
        edit_uname.setIcon(icon_user);
        et_pwd.setIcon(icon_pwd);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.width = (int) (screenWidth * 0.88);
        ll_gesture_login.setLayoutParams(lp);
        absoluteBT.setText("确定");
        negativeBT.setText("取消");

        absoluteBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = MemorySave.MS.userInfo.uname;
                final String pwd = et_pwd.getEdtText().trim();
                if (!StringUtils.isEmpty(name)) {
                    hgps.gainGesturePwd(name, pwd);
                }

            }
        });

        negativeBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gesture_status == GESTURE_ON) {
                    sbtn_gesture_setting.setChecked(false);
                } else if (gesture_status == GESTURE_OFF) {
                    sbtn_gesture_setting.setChecked(true);
                }
                if (mExitDialog != null && mExitDialog.isShowing()) {
                    mExitDialog.dismiss();
                }
            }
        });

        Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void gainGesturePwdsuccess(BaseJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    if (mExitDialog != null && mExitDialog.isShowing()) {
                        mExitDialog.dismiss();
                    }
                    if (gesture_status == GESTURE_OFF) {
                        SpUtils.setBoolean(SpUtils.Table.CONFIG, SpUtils.Config.IS_GESTURE_OPEN, false);
                        LockPatternUtils.getInstance(GesturePwdActivity.this).clearLock();
                        ll_gesture.setVisibility(View.GONE);
                    } else if (gesture_status == GESTURE_ON || gesture_status == GESTURE_UPDATER) {
                        startActivity(new Intent(mContext, SettingGestureLockActivity.class));
                    }
                } else {
                    String message = response.getMessage();
                    createDialogDismissAuto(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainGesturePwdfail() {

    }
}
