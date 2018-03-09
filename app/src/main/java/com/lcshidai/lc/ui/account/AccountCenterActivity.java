package com.lcshidai.lc.ui.account;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.BroadCastImpl;
import com.lcshidai.lc.impl.GainOpenAccountImpl;
import com.lcshidai.lc.impl.ResultImpl;
import com.lcshidai.lc.impl.account.AccountSettingImpl;
import com.lcshidai.lc.impl.onKeyInvest.IsOpenAutoInvestImpl;
import com.lcshidai.lc.model.OpenAccountData;
import com.lcshidai.lc.model.OpenAccountJson;
import com.lcshidai.lc.model.account.AccountSettingData;
import com.lcshidai.lc.model.account.AccountSettingImg;
import com.lcshidai.lc.model.account.AccountSettingJson;
import com.lcshidai.lc.model.oneKeyInvest.IsOpenAutoInvestJson;
import com.lcshidai.lc.service.HttpGainOpenAccountService;
import com.lcshidai.lc.service.account.HttpAccountSettingService;
import com.lcshidai.lc.service.oneKeyInvest.IsOpenAutoInvestService;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.account.pwdmanage.UserPwdManageActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.Thumbnail;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.CircularImage;
import com.lcshidai.lc.widget.ppwindow.CameraOptionPopupWindow;
import com.lcshidai.lc.widget.ppwindow.SafeLevelPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 个人中心
 */
public class AccountCenterActivity extends TRJActivity implements View.OnClickListener, ResultImpl,
        AccountSettingImpl, GainOpenAccountImpl, IsOpenAutoInvestImpl {
    @Bind(R.id.ib_top_bar_back)
    ImageButton ibTopBarBack;
    @Bind(R.id.tv_top_bar_title)
    TextView tvTopBarTitle;
    @Bind(R.id.iv_head)
    CircularImage ivHead;
    @Bind(R.id.tv_telephone)
    TextView tvTelephone;
    @Bind(R.id.tv_uname)
    TextView tvUname;
    @Bind(R.id.tv_safe)
    TextView tvSafe;
    @Bind(R.id.iv_photo1)
    ImageView ivPhoto1;
    @Bind(R.id.iv_photo2)
    ImageView ivPhoto2;
    @Bind(R.id.iv_photo3)
    ImageView ivPhoto3;
    @Bind(R.id.iv_photo4)
    ImageView ivPhoto4;
    @Bind(R.id.iv_photo5)
    ImageView ivPhoto5;
    @Bind(R.id.ll_safe)
    LinearLayout llSafe;
    @Bind(R.id.tv_mobile_auth)
    TextView tvMobileAuth;
    @Bind(R.id.rl_mobile_auth_container)
    RelativeLayout rlMobileAuthContainer;
    @Bind(R.id.tv_real_auth)
    TextView tvRealAuth;
    @Bind(R.id.rl_real_name_auth_container)
    RelativeLayout rlRealNameAuthContainer;
    @Bind(R.id.tv_mail_auth)
    TextView tvMailAuth;
    @Bind(R.id.rl_mail_auth_container)
    RelativeLayout rlMailAuthContainer;
    @Bind(R.id.rl_pwd_management_container)
    RelativeLayout rlPwdManagementContainer;
    @Bind(R.id.rl_setting_container)
    RelativeLayout rlSettingContainer;
    @Bind(R.id.main)
    FrameLayout main;
    @Bind(R.id.progressContainer)
    LinearLayout mProgressContainer;
//    @Bind(R.id.tv_current_version)
//    TextView tvCurrentVersion;
    @Bind(R.id.tv_auto_invest_status)
    TextView tvAutoInvestStatus;
    @Bind(R.id.rl_auto_invest_container)
    RelativeLayout rlAutoInvestContainer;
    private CameraOptionPopupWindow menuWindow;
    private String is_id_auth = "", is_email_auth = "", is_mobile_auth = "",
            is_paypwd_mobile_set = "", is_binding_bank = "", is_set_sqa = "";// 0-未验证，1-已认证
    private String real_name = null;
    private Thumbnail thumbnail;

    HttpAccountSettingService hass;
    private int screenWidth;

    private int starNum = 0;
    private String safe = null;

    private int mHasEscrowedFlag;
    private double mRealValue;

    private Dialog openEcwDialog = null;
    private HttpGainOpenAccountService httpGainOpenAccountService;

    private IsOpenAutoInvestService isOpenAutoInvestService;
    private OpenEcwAccountReceiver mOpenEcwAccountReceiver;
    private String openEscrowUrl;
    // 是否已开通一键投资，默认关闭
    private boolean isAutoShowDialog = false;
    // 是否开启自动投资
    private boolean isAutoInvestOpen = false;
    // 是否需要重新开启
    private boolean isNeedOpenAgain = false;
    private CountDownTimer timer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_center);
        ButterKnife.bind(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        mHasEscrowedFlag = getIntent().getIntExtra("hasEscrowedFlag", -1);
        mRealValue = getIntent().getDoubleExtra("realValue", 0.00);
        openEscrowUrl = getIntent().getStringExtra("openEscrowUrl");
        thumbnail = Thumbnail.init(AccountCenterActivity.this);
        hass = new HttpAccountSettingService(this, this);
        httpGainOpenAccountService = new HttpGainOpenAccountService(this, this);
        isOpenAutoInvestService = new IsOpenAutoInvestService(this, this);
        initView();

        mOpenEcwAccountReceiver = new OpenEcwAccountReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastImpl.ACTION_OPEN_ECW_ACCOUNT);
        registerReceiver(mOpenEcwAccountReceiver, filter);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return flag;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mOpenEcwAccountReceiver);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void initView() {
        tvTopBarTitle.setText("个人中心");
//        String versionStr = String.format("理财时代版本号%s", CommonUtil.getVersionName(mContext,
//                mContext.getPackageName()));
//        tvCurrentVersion.setText(versionStr);
        ibTopBarBack.setOnClickListener(this);
        ivHead.setOnClickListener(this);
        rlMobileAuthContainer.setOnClickListener(this);
        rlRealNameAuthContainer.setOnClickListener(this);
        rlMailAuthContainer.setOnClickListener(this);
        rlPwdManagementContainer.setOnClickListener(this);
        rlSettingContainer.setOnClickListener(this);
        llSafe.setOnClickListener(this);
        rlAutoInvestContainer.setOnClickListener(this);
    }

    private void loadData() {
        hass.gainAccountSetting("");
        // 获取一键投资开启状态
        isOpenAutoInvestService.isOpenAutoInvest();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        try {
            menuWindow.reback(requestCode, resultCode, data, false);
        } catch (Exception e) {
        }
    }

    @Override
    public void goBackSuccess() {
        loadData();
    }

    @Override
    public void gainAccountSettingSuccess(AccountSettingJson response) {
        try {
            mProgressContainer.setVisibility(View.GONE);
            if (null == response)
                return;
            String boolen = response.getBoolen();
            if (response != null) {
                if (boolen.equals("1")) {
                    AccountSettingData json = response.getData();
                    tvUname.setText("理财时代ID: " + StringUtils.onShowForEmpty(json.getUname()));

                    String str_safe = json.getSafe_level_label();
                    String value;
                    if (str_safe.equals("a")) {
                        value = "低";
                    } else if (str_safe.equals("b")) {
                        value = "中";
                    } else if (str_safe.equals("c")) {
                        value = "高";
                    } else {
                        value = "-";
                    }
                    this.safe = value;
                    tvSafe.setText(value);
                    is_id_auth = json.getIs_id_auth();
                    is_email_auth = json.getIs_email_auth();
                    is_mobile_auth = json.getIs_mobile_auth();
                    is_paypwd_mobile_set = json.getIs_paypwd_mobile_set();
                    is_binding_bank = json.getIs_binding_bank();
                    is_set_sqa = json.getIs_set_sqa();
                    if (is_id_auth.equals("1")) {
                        real_name = json.getReal_name();
                        tvRealAuth.setText(real_name);
                        tvRealAuth.setTextColor(this.getResources().getColor(R.color.saft));
                    } else {
                        tvRealAuth.setText(R.string.account_center_auth_right_now);
                    }
                    if (is_email_auth.equals("1")) {
                        String email = json.getEmail();
                        tvMailAuth.setText(email);
                        tvMailAuth.setTextColor(this.getResources().getColor(R.color.saft));
                    } else {
                        tvMailAuth.setText(R.string.account_center_auth_right_now);
                    }
                    if (is_mobile_auth.equals("1")) {
                        String mobile = json.getMobile();
                        tvMobileAuth.setText(mobile);
                        tvMobileAuth.setTextColor(this.getResources().getColor(R.color.saft));
                    } else {
                        tvMobileAuth.setText(R.string.account_center_auth_right_now);
                    }
                    int starNum = json.getSafe_level();
                    this.starNum = starNum;
                    float safeLevel = (starNum / 100f) * 5;
                    int num = (int) safeLevel;
                    boolean isHalf = false;
                    if (safeLevel > num) {
                        isHalf = true;
                    }
                    iteratorStar(num, isHalf);
                    String mobile = json.getMobile();
                    tvTelephone.setText(mobile);
                    AccountSettingImg obj = json.getAva();
                    String url = "";
                    if (screenWidth < 720) {
                        url = obj.getUrl_s100();
                    } else {
                        url = obj.getUrl_s300();
                    }
                    Glide.with(mContext)
                            .load(url)
                            .placeholder(R.drawable.gesture_lock_head)
                            .error(R.drawable.gesture_lock_head)
                            .into(ivHead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * rate 绘制
     *
     * @param num    数量
     * @param isHalf 是否有半星
     */
    public void iteratorStar(int num, boolean isHalf) {
        List<ImageView> views = new ArrayList<ImageView>();
        views.add(ivPhoto1);
        views.add(ivPhoto2);
        views.add(ivPhoto3);
        views.add(ivPhoto4);
        views.add(ivPhoto5);
        for (ImageView view : views) {
            view.setImageResource(R.drawable.img_star_gray);
        }
        for (int i = 0; i < num; i++) {
            views.get(i).setImageBitmap(thumbnail.readBitMap(mContext, R.drawable.img_star_theme));
        }
        if (isHalf) {
            if (num < views.size()) {
                views.get(num).setImageBitmap(thumbnail.readBitMap(mContext, R.drawable.img_star_half));
            }
        }
    }

    @Override
    public void gainAccountSettingFail() {
        mProgressContainer.setVisibility(View.GONE);
    }

    @Override
    public void getOpenAccountInfoSuccess(OpenAccountJson response) {
        if (response != null) {
            // if loading is shown, hide it
            if (response.getBoolen().equals("1")) {
                // 获取存管开户信息成功
                OpenAccountData openAccountData = response.getData();
                if (openAccountData != null) {
                    CommonUtil.openAccount(mContext, openAccountData);
                }
            } else {
                String message = response.getMessage();
                ToastUtil.showToast(this, message);
            }
        }
    }

    @Override
    public void getOpenAccountInfoFailed() {

    }

    private void showOpenEcwDialog() {
        final String title = getString(R.string.ecw_dialog_open_ecw_account_instruction_auth);
        SpannableString ssTitle;
        if (mRealValue > 0) {
            String s = title + getString(R.string.ecw_dialog_open_ecw_account_sub_instruction);
            ssTitle = new SpannableString(s);
            ssTitle.setSpan(new AbsoluteSizeSpan(12, true),
                    title.length(), s.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssTitle = new SpannableString(title);
        }
        if (openEcwDialog == null) {
            openEcwDialog = createDialog("温馨提示", ssTitle.toString(), getString(R.string.ecw_dialog_not_open_ecw_account_now), getString(R.string.ecw_dialog_open_ecw_account_now),
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (openEcwDialog.isShowing()) {
                                openEcwDialog.dismiss();
                                openEcwDialog = null;
                            }
                        }
                    },
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (openEcwDialog.isShowing()) {
                                openEcwDialog.dismiss();
                                openEcwDialog = null;
                            }
                            if (CommonUtil.isNullOrEmpty(openEscrowUrl)) {
//							startActivity(new Intent(AccountActivity.this, CunGuanAccountActivity.class));
                                // 获取开户信息
                                httpGainOpenAccountService.getOpenAccountInfo();
                            } else {
                                Intent intent = new Intent(mContext, MainWebActivity.class);
                                intent.putExtra("web_url", openEscrowUrl);
                                intent.putExtra("need_header", 0);
                                intent.putExtra("title", "开通存管");
                                startActivity(intent);
                            }
                        }
                    }
            );
            Button btn = (Button) openEcwDialog.findViewById(R.id.dialog_message_bt_absolute);
            Button t = (Button) openEcwDialog.findViewById(R.id.dialog_message_bt_negative);
            btn.setTextColor(mContext.getResources().getColor(R.color.text_default));
            t.setTextColor(mContext.getResources().getColor(R.color.color_finance_child_yellow));
        }
        if (!openEcwDialog.isShowing()) {
            openEcwDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                finish();
                break;
            case R.id.iv_head:
                // 实例化SelectPicPopupWindow
                menuWindow = new CameraOptionPopupWindow((TRJActivity) mContext, this);
                // 显示窗口 设置layout在PopupWindow中显示的位置
                menuWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ll_safe:
                dealWithSafeLevelClicked();
                break;
            case R.id.rl_mobile_auth_container:
                if (is_mobile_auth.equals("1")) {
                    intent = new Intent(mContext, UserMobileUpdaterActivity.class);
                } else {
                    intent = new Intent(mContext, UserMobileAuthActivity.class);
                    intent.putExtra("mode", 1);
                }
                startActivity(intent);
                break;
            case R.id.rl_real_name_auth_container:
                if (is_id_auth.equals("0")) {
//                    showOpenEcwDialog();
                    goRechargeDialog();
                }
                break;
            case R.id.rl_mail_auth_container:
                if (is_email_auth.equals("1")) {
                    intent = new Intent(mContext, UserMailUpdaterActivity.class);
                } else {
                    intent = new Intent(mContext, UserMailAuthActivity.class);
                    intent.putExtra("mode", 1);
                }
                startActivity(intent);
                break;

            case R.id.rl_pwd_management_container:
                intent = new Intent();
                intent.setClass(mContext, UserPwdManageActivity.class);
                intent.putExtra("is_paypwd_mobile_set", is_paypwd_mobile_set);
                intent.putExtra("is_set_sqa", is_set_sqa);
                startActivity(intent);
                break;
            case R.id.rl_auto_invest_container:
                String loadUrl, title;
                if (isAutoInvestOpen) {
                    //  去关闭
                    loadUrl = LCHttpClient.BASE_WAP_HEAD + "/#/openAutoBiding?phase=opened ";
                } else {
                    //  去开通
                    loadUrl = LCHttpClient.BASE_WAP_HEAD + "/#/openAutoBiding?phase=one ";
                }
                intent = new Intent(mContext, MainWebActivity.class);
                intent.putExtra("web_url", loadUrl);
                intent.putExtra("title", "自动投标授权");
                startActivity(intent);
                break;
            case R.id.rl_setting_container:
                intent = new Intent();
                intent.setClass(mContext, AccountSettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    //实名认证跳转充值提示Dialog
    private Dialog mRzDialog = null;
    private void goRechargeDialog(){
        String title = "温馨提示";
        String msg = "充值成功后系统自动进行实名认证！";
        String absBtn = "现在去充值";
        String negBtn = "取消";
        mRzDialog = createDialog1(title, msg, absBtn, negBtn, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent new_recharge_intent = new Intent();
                new_recharge_intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/addBank");
                new_recharge_intent.setClass(AccountCenterActivity.this,
                        MainWebActivity.class);
                startActivity(new_recharge_intent);
                mRzDialog.dismiss();
            }
        }, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mRzDialog.dismiss();
            }
        });
        mRzDialog.show();
    }

    private void dealWithSafeLevelClicked() {
        Bundle bundle = new Bundle();
        bundle.putString("safeLevel", safe);
        bundle.putInt("starNum", starNum);
        boolean is_mobile = is_mobile_auth.equals("1");
        bundle.putBoolean("is_mobile_auth", is_mobile);
        boolean is_login_psw = true;
        bundle.putBoolean("is_login_psw", is_login_psw);
        boolean is_email = is_email_auth.equals("1");
        bundle.putBoolean("is_email_auth", is_email);
        boolean is_real_name = real_name != null;
        bundle.putBoolean("is_real_name", is_real_name);
        boolean isGestureOpen = SpUtils.getBoolean(SpUtils.Table.CONFIG,
                SpUtils.Config.IS_GESTURE_OPEN, false);
        bundle.putBoolean("is_gesture_lock", isGestureOpen);
        boolean is_paypwd = is_paypwd_mobile_set.equals("1");
        bundle.putBoolean("is_paypwd", is_paypwd);
        boolean is_sqa = is_set_sqa.equals("1");
        bundle.putBoolean("is_sqa", is_sqa);
        bundle.putString("openEscrowUrl", openEscrowUrl);
//        点击安全等级弹出popwindow
        SafeLevelPopupWindow safeLevelPopupWindow = new SafeLevelPopupWindow(AccountCenterActivity.this, bundle);
        safeLevelPopupWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private class OpenEcwAccountReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取开户信息
            httpGainOpenAccountService.getOpenAccountInfo();
        }
    }

    @Override
    public void getIsOpenAutoInvestSuccess(IsOpenAutoInvestJson response) {
        if (null != response) {
            if (response.getBoolen().equals("1")) {
                if (response.getData() != null) {
                    if (response.getData().getIs_auto_bid() == 1) {
                        tvAutoInvestStatus.setText("已开通");
                        tvAutoInvestStatus.setTextColor(getResources().getColor(R.color.text_default));
                    } else {
                        tvAutoInvestStatus.setText("未开通");
                        tvAutoInvestStatus.setTextColor(getResources().getColor(R.color.theme_color));
                    }
                    isAutoInvestOpen = response.getData().getIs_auto_bid() == 1;
                    isNeedOpenAgain = response.getData().getOpen_again() == 1;
                }
            }
        }
    }

    @Override
    public void getIsOpenAutoInvestFailed() {

    }
}
