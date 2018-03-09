package com.lcshidai.lc.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.socks.library.KLog;
import com.lcshidai.lc.R;
import com.lcshidai.lc.alarm.AlarmXmlManager;
import com.lcshidai.lc.impl.account.AccountLoginoutImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.account.HttpLoginoutService;
import com.lcshidai.lc.ui.MainActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.ToastUtil;

/**
 * 账户设置
 */
public class AccountSettingActivity extends TRJActivity implements OnClickListener, AccountLoginoutImpl {
    private ImageButton top_back_btn;
    private TextView top_title_text;
    private ToggleButton sbtn_message;
    private LinearLayout ll_exit;
    private int bdpush_is_open = 1;
    private HttpLoginoutService hls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        hls = new HttpLoginoutService(this, this);
        top_back_btn = (ImageButton) findViewById(R.id.ib_top_bar_back);
        top_title_text = (TextView) findViewById(R.id.tv_top_bar_title);
        top_title_text.setText("设置");

        sbtn_message = (ToggleButton) findViewById(R.id.sbtn_message);
        ll_exit = (LinearLayout) findViewById(R.id.exit);

        bdpush_is_open = SpUtils.getInt(SpUtils.Table.CONFIG, SpUtils.Config.IS_PUSH_OPEN, 1);
        if (bdpush_is_open == 1) {
            sbtn_message.setChecked(true);
        } else {
            sbtn_message.setChecked(false);
        }
        sbtn_message.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 替换成个推开关设置
                    SpUtils.setInt(SpUtils.Table.CONFIG, SpUtils.Config.IS_PUSH_OPEN, 1);
                    com.igexin.sdk.PushManager.getInstance().turnOnPush(mContext);
                } else {
                    SpUtils.setInt(SpUtils.Table.CONFIG, SpUtils.Config.IS_PUSH_OPEN, 0);
                    com.igexin.sdk.PushManager.getInstance().turnOffPush(mContext);
                }
            }
        });

        top_back_btn.setOnClickListener(this);
        ll_exit.setOnClickListener(this);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                finish();
                break;
            case R.id.exit:
                if (MemorySave.MS.mIsLogined && !MemorySave.MS.mIsLoginout) {
                    hls.gainAccountLoginout();
                } else {
                    ToastUtil.showToast(this, "未登录");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void gainAccountLoginoutsuccess(BaseJson response) {
        try {
            if (null == response)
                return;
            String boolen = response.getBoolen();
            if (response != null) {
                if (boolen.equals("1")) {
                    ToastUtil.showToast(this, "退出成功");
                    CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
                        @Override
                        public void onReceiveValue(Boolean value) {
                            if (value) {
                                KLog.e("cookie", "清除成功");
                            }
                        }
                    });
                    // todo 清除Glide缓存
                    GoLoginUtil.clearInfoToLogin(this);
                    // 清空预留手机号、是否开通存管、是否浙商账户
                    GoLoginUtil.saveBankLeftPhone("", AccountSettingActivity.this);
                    GoLoginUtil.saveCunGuanFlag(0, AccountSettingActivity.this);
                    GoLoginUtil.saveZheShangCardFlag(0, AccountSettingActivity.this);
                    GoLoginUtil.setPayPswSetFlag(-1, AccountSettingActivity.this);
                    // 清除accessToken, userToken, mangerName, managerPhone等信息
                    GoLoginUtil.saveAccessToken("", this);
                    GoLoginUtil.saveUserToken("", this);
                    GoLoginUtil.saveUcId("", this);
                    GoLoginUtil.saveManagerName("", this);
                    GoLoginUtil.saveManagerPhone("", this);
                    SpUtils.setString(SpUtils.Table.CONFIG, SpUtils.Config.U_DOMAIN, "");
                    SpUtils.setString(SpUtils.Table.CONFIG, SpUtils.Config.U_APP_KEY, "");
                    SpUtils.setString(SpUtils.Table.CONFIG, SpUtils.Config.U_APP_ID, "");
                    MemorySave.MS.mIsLoginout = true;
                    sendBroadcast(MY_RESON_LOGIN_STATUS);
                    AlarmXmlManager.clearAllAlarm(this);
                    Intent intent = new Intent(this, MainActivity.class);
                    MemorySave.MS.mIsGoFinanceHome = true;
                    startActivity(intent);
                    this.finish();
                } else {
                    String message = response.getMessage();
                    ToastUtil.showToast(this, message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void gainAccountLoginoutfail() {
    }


}
