package com.lcshidai.lc.ui.account;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.http.BaseCallback;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.http.ProJsonHandler;
import com.lcshidai.lc.impl.AdsImpl;
import com.lcshidai.lc.impl.BroadCastImpl;
import com.lcshidai.lc.impl.GainOpenAccountImpl;
import com.lcshidai.lc.impl.MessageImpl;
import com.lcshidai.lc.impl.account.AccountHasEscrowedImp;
import com.lcshidai.lc.impl.account.AccountIsZheshangCardImp;
import com.lcshidai.lc.impl.account.AccountSettingImpl;
import com.lcshidai.lc.impl.account.GetEscrowRemindImpl;
import com.lcshidai.lc.impl.account.SetEscrowRemindImpl;
import com.lcshidai.lc.impl.account.SignImpl;
import com.lcshidai.lc.impl.account.SignUserInfoImpl;
import com.lcshidai.lc.impl.licai.AccessTokenImpl;
import com.lcshidai.lc.impl.licai.CheckFundRegisterImpl;
import com.lcshidai.lc.impl.licai.IsShowCfyImpl;
import com.lcshidai.lc.model.AdsData;
import com.lcshidai.lc.model.AdsJson;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.MessageJson;
import com.lcshidai.lc.model.MessageLocalData;
import com.lcshidai.lc.model.MessageTypeNew;
import com.lcshidai.lc.model.MsgNew;
import com.lcshidai.lc.model.OpenAccountData;
import com.lcshidai.lc.model.OpenAccountJson;
import com.lcshidai.lc.model.account.AccountData;
import com.lcshidai.lc.model.account.AccountHasEscrowedData;
import com.lcshidai.lc.model.account.AccountHasEscrowedJson;
import com.lcshidai.lc.model.account.AccountIsZheshangCardData;
import com.lcshidai.lc.model.account.AccountIsZheshangCardJson;
import com.lcshidai.lc.model.account.AccountJson;
import com.lcshidai.lc.model.account.AccountSettingData;
import com.lcshidai.lc.model.account.AccountSettingJson;
import com.lcshidai.lc.model.account.GetEscrowRemindData;
import com.lcshidai.lc.model.account.GetEscrowRemindJson;
import com.lcshidai.lc.model.account.SignJson;
import com.lcshidai.lc.model.account.SignUserInfoJson;
import com.lcshidai.lc.model.licai.AccessTokenJson;
import com.lcshidai.lc.model.licai.CheckFundRegisterJson;
import com.lcshidai.lc.model.licai.IsShowCfyJson;
import com.lcshidai.lc.service.ApiService;
import com.lcshidai.lc.service.HttpAdsService;
import com.lcshidai.lc.service.HttpGainOpenAccountService;
import com.lcshidai.lc.service.HttpMsgService;
import com.lcshidai.lc.service.account.HttpAccountSettingService;
import com.lcshidai.lc.service.account.HttpGetEscrowRemindService;
import com.lcshidai.lc.service.account.HttpHasEscrowedService;
import com.lcshidai.lc.service.account.HttpIsZheshangCardService;
import com.lcshidai.lc.service.account.HttpSetEscrowRemindService;
import com.lcshidai.lc.service.account.HttpSignService;
import com.lcshidai.lc.service.account.HttpSignUserInfoService;
import com.lcshidai.lc.service.licai.HttpCheckFundRegisterService;
import com.lcshidai.lc.service.licai.HttpGetAccessTokenService;
import com.lcshidai.lc.service.licai.HttpIsShowCfyService;
import com.lcshidai.lc.ui.CapitalTotalViewActivity;
import com.lcshidai.lc.ui.CunGuanAccountActivity;
import com.lcshidai.lc.ui.LoginActivity;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.account.cashout.CashOutActivity;
import com.lcshidai.lc.ui.base.AbsActivityGroup;
import com.lcshidai.lc.ui.base.AbsSubActivity;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.ui.finance.ManageFinaceInfoListReActivity;
import com.lcshidai.lc.ui.more.InviteActivity;
import com.lcshidai.lc.ui.project.FundAccountActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.MsgUtil;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.rytong.czfinancial.view.ZSBank;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 账户首页
 */
public final class AccountActivity extends AbsSubActivity implements AccountSettingImpl,
        AccountHasEscrowedImp, AccountIsZheshangCardImp, GainOpenAccountImpl, GetEscrowRemindImpl,
        SetEscrowRemindImpl, OnClickListener, MessageImpl, AccessTokenImpl, SignImpl,
        SignUserInfoImpl, AdsImpl, CheckFundRegisterImpl, IsShowCfyImpl {
    public static final String ACCOUNT_DATA = "account_data";
    @Bind(R.id.ll_account_brief_view_container)
    LinearLayout llAccountBriefViewContainer;
    @Bind(R.id.iv_view_net_asset_toggle)
    ImageView ivViewNetAssetToggle;
    @Bind(R.id.tv_net_asset)
    TextView tvNetAsset;
    //    @Bind(R.id.tv_open_ecw_account_right_now)
//    TextView tvOpenEcwAccountRightNow;
    @Bind(R.id.tv_ecw_tips_label)
    TextView tvEcwTipsLabel;
    @Bind(R.id.tv_available_balance)
    TextView tvAvailableBalance;
    @Bind(R.id.tv_available_balance_label)
    TextView tvAvailableBalanceLabel;
    @Bind(R.id.ll_available_balance_container)
    LinearLayout llAvailableBalanceContainer;
    @Bind(R.id.tv_expected_total_return)
    TextView tvExpectedTotalReturn;
    @Bind(R.id.tv_expected_total_return_label)
    TextView tvExpectedTotalReturnLabel;
    @Bind(R.id.ll_expected_total_return_container)
    LinearLayout llExpectedTotalReturnContainer;
    @Bind(R.id.ll_net_asset_container)
    LinearLayout llNetAssetContainer;
    @Bind(R.id.tv_withdrawals)
    TextView tvWithdrawals;
    @Bind(R.id.tv_recharge)
    TextView tvRecharge;
    @Bind(R.id.iv_invest_record)
    ImageView ivInvestRecord;
    @Bind(R.id.tv_invest_record_describe)
    TextView tvInvestRecordDescribe;
    @Bind(R.id.ll_invest_record_container)
    LinearLayout llInvestRecordContainer;
    @Bind(R.id.iv_fund_record_icon)
    ImageView ivFundRecordIcon;
    @Bind(R.id.ll_fund_record_container)
    LinearLayout llFundRecordContainer;
    @Bind(R.id.iv_esw_account_icon)
    ImageView ivEswAccountIcon;
    @Bind(R.id.tv_esw_account_account)
    TextView tvEswAccountAccount;
    @Bind(R.id.tv_esw_account_describe)
    TextView tvEswAccountDescribe;
    @Bind(R.id.ll_esw_account_container)
    LinearLayout llEswAccountContainer;
    @Bind(R.id.ll_fund_divider_container)
    LinearLayout llFundDividerContainer;
    @Bind(R.id.iv_fund_account_icon)
    ImageView ivFundAccountIcon;
    @Bind(R.id.tv_fund_account_open_state)
    TextView tvFundAccountOpenState;
    @Bind(R.id.ll_fund_account_container)
    LinearLayout llFundAccountContainer;
    @Bind(R.id.iv_ad)
    ImageView ivAd;
    @Bind(R.id.iv_invite_friend_icon)
    ImageView ivInviteFriendIcon;
    @Bind(R.id.iv_invite_friend_badge)
    ImageView ivInviteFriendBadge;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.ll_invite_friend_container)
    LinearLayout llInviteFriendContainer;
    @Bind(R.id.iv_my_reward_icon)
    ImageView ivMyRewardIcon;
    @Bind(R.id.iv_my_reward_badge)
    ImageView ivMyRewardBadge;
    @Bind(R.id.ll_my_reward_container)
    LinearLayout llMyRewardContainer;
    @Bind(R.id.iv_financial_cash_icon)
    ImageView ivFinancialCashIcon;
    @Bind(R.id.iv_financial_cash_badge)
    ImageView ivFinancialCashBadge;
    @Bind(R.id.ll_financial_cash_container)
    LinearLayout llFinancialCashContainer;
    @Bind(R.id.iv_my_score_icon)
    ImageView ivMyScoreIcon;
    @Bind(R.id.tv_my_score)
    TextView tvMyScore;
    @Bind(R.id.iv_my_score_badge)
    ImageView ivMyScoreBadge;
    @Bind(R.id.tv_my_score_describe)
    TextView tvMyScoreDescribe;
    @Bind(R.id.ll_my_score_container)
    LinearLayout llMyScoreContainer;
    @Bind(R.id.iv_my_public_service_icon)
    ImageView ivMyPublicServiceIcon;
    @Bind(R.id.tv_my_public_service)
    TextView tvMyPublicService;
    @Bind(R.id.iv_my_public_service_badge)
    ImageView ivMyPublicServiceBadge;
    @Bind(R.id.tv_my_public_service_describe)
    TextView tvMyPublicServiceDescribe;
    @Bind(R.id.ll_my_public_service_container)
    LinearLayout llMyPublicServiceContainer;
    @Bind(R.id.iv_account_center)
    ImageButton ivAccountCenter;
    @Bind(R.id.iv_msg)
    ImageView ivMsg;
    @Bind(R.id.iv_account_message)
    ImageView ivAccountMessage;
    @Bind(R.id.fl_msg_container)
    FrameLayout flMsgContainer;
    @Bind(R.id.iv_wxld)
    ImageView ivWxld;
    @Bind(R.id.iv_sign)
    ImageView ivSign;
    @Bind(R.id.tv_show)
    TextView tvShow;
    @Bind(R.id.progressContainer)
    LinearLayout progressContainer;
    @Bind(R.id.fl_main_container)
    FrameLayout flMainContainer;
    @Bind(R.id.tv_400_customer_service)
    TextView tv400CustomerService;
    @Bind(R.id.tv_online_customer_service)
    TextView tvOnlineCustomerService;
    //    @Bind(R.id.ll_account_insurance_container)
//    LinearLayout llAccountInsuranceContainer;
    @Bind(R.id.iv_account_insurance_icon)
    ImageView ivAccountInsuranceIcon;
    @Bind(R.id.tv_account_insurance)
    TextView tvAccountInsurance;

    private Timer timer = null;
    private Double mRealValue = (Double) 0.00, mRealValue1 = (Double) 0.00, mRealValue2 = (Double) 0.00,
            mRealValue3 = (Double) 0.00, mRealValue4 = (Double) 0.00;

    public static int MI = 50;

    private TimerTask mTimerTask;
    private MessageLocalData data;
    private AccountData accountData;
    private LoginReceiver mLoginReceiver;

    private Dialog openEcwDialog = null;                            // 开通存管弹窗
    private Dialog remindDialog = null;                             // 点击暂不开通后的弹窗
    private Dialog signDialog = null;                               // 签到弹窗
    private Dialog setPayPswDialog = null;                          // 设置支付密码弹窗
    private Dialog mRzDialog = null;                                // 充值后自动进行实名认证弹窗提醒
    private Dialog hotLineDialog;

    private HttpMsgService hms;
    private HttpAccountSettingService hass;    // 请求是否设置手机支付密码
    private HttpHasEscrowedService mHttpHasEscrowedService;
    private HttpIsZheshangCardService mHttpIsZheshangCardService;
    private HttpGainOpenAccountService httpGainOpenAccountService;
    private HttpGetEscrowRemindService mHttpGetEscrowRemindService;
    private HttpSetEscrowRemindService mHttpSetEscrowRemindService;
    private HttpSignService mHttpSignService;
    private HttpSignUserInfoService mHttpSignUserInfoService;
    private HttpCheckFundRegisterService checkFundRegisterService;
    private HttpGetAccessTokenService getAccessTokenService;
    private HttpAdsService mHttpAdsService;
    private HttpIsShowCfyService isShowCfyService = null;

    private String totalScore, todayScore;
    private String uid;
    private String tvYuQiQiDaiShouYiStr, daiShouBenJinStr;
    private boolean isAccountVisible;

    private final int InitFlag = -1;
    private int mHasEscrowedFlag = InitFlag;
    private int mIsZheshangCardFlag = InitFlag;
    private int EcwType = 0;
    private Map<String, String> mOpenEcwMap = null;
    private boolean GoOpenEcw;
    private boolean mIsAllowEscrow;                             // 是否允许开通存管

    private String netAssetStr = "--";
    private String availableBalanceStr = "--";
    private String expectedTotalReturnStr = "--";

    private String openEscrowUrl;                               // wap开通存管的地址

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        hass = new HttpAccountSettingService(this, this);
        hms = new HttpMsgService(this, this);
        mHttpHasEscrowedService = new HttpHasEscrowedService(this, this);
        mHttpIsZheshangCardService = new HttpIsZheshangCardService(this, this);
        httpGainOpenAccountService = new HttpGainOpenAccountService(this, this);
        mHttpGetEscrowRemindService = new HttpGetEscrowRemindService(this, this);
        mHttpSetEscrowRemindService = new HttpSetEscrowRemindService(this, this);
        mHttpSignService = new HttpSignService(this, this);
        mHttpSignUserInfoService = new HttpSignUserInfoService(this, this);
        mHttpAdsService = new HttpAdsService(this, this);
        isShowCfyService = new HttpIsShowCfyService(this, this);
        checkFundRegisterService = new HttpCheckFundRegisterService(this, this);
        getAccessTokenService = new HttpGetAccessTokenService(this, this);
        mLoginReceiver = new LoginReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastImpl.ACTION_LOGIN_SUCCEED_OR_FAILED);
        registerReceiver(mLoginReceiver, filter);

        initView();
        initSetPayPswDialog();
        // loadData();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        ivAccountCenter.setOnClickListener(this);
        ivMsg.setOnClickListener(this);
        ivSign.setOnClickListener(this);
        ivViewNetAssetToggle.setOnClickListener(this);
        llAccountBriefViewContainer.setOnClickListener(this);
//        tvOpenEcwAccountRightNow.setOnClickListener(this);
        llNetAssetContainer.setOnClickListener(this);
        tvRecharge.setOnClickListener(this);
        tvWithdrawals.setOnClickListener(this);
//        llAccountInsuranceContainer.setOnClickListener(this);
        llInvestRecordContainer.setOnClickListener(this);
        llFundRecordContainer.setOnClickListener(this);
        llEswAccountContainer.setOnClickListener(this);
        llFundAccountContainer.setOnClickListener(this);
        llInviteFriendContainer.setOnClickListener(this);
        llMyRewardContainer.setOnClickListener(this);
        llFinancialCashContainer.setOnClickListener(this);
        llMyScoreContainer.setOnClickListener(this);
        llMyPublicServiceContainer.setOnClickListener(this);
        tv400CustomerService.setOnClickListener(this);
        tvOnlineCustomerService.setOnClickListener(this);

//        SpannableString ecw_tips_0_ss = new SpannableString(tvOpenEcwAccountRightNow.getText());
//        ecw_tips_0_ss.setSpan(new UnderlineSpan(), 0, tvOpenEcwAccountRightNow.getText().length(),Spanned.SPAN_MARK_MARK);
//        tvOpenEcwAccountRightNow.setText(ecw_tips_0_ss);

//        int colorStart = Color.parseColor("#FCECB5");
//        int colorEnd = Color.parseColor("#F7971C");
//        Shader shader = new LinearGradient(0, 0, 0, 80, colorStart, colorEnd, TileMode.CLAMP);
//        tvNetAsset.getPaint().setShader(shader);

        timer = new Timer(true);
    }

    private void initSetPayPswDialog() {
        if (setPayPswDialog == null) {
            setPayPswDialog = createDialog("您未设置手机支付密码", "设置", "取消", new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (setPayPswDialog.isShowing()) {
                                setPayPswDialog.dismiss();
                            }
                            Intent intent = new Intent(mContext, UserPayPwdFirstSetActivity.class);
                            intent.putExtra("from_activity", 1);// 显示返回按钮
                            intent.putExtra("intent_from_withdrawals", 1);
                            startActivity(intent);
                        }
                    },
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (setPayPswDialog.isShowing()) {
                                setPayPswDialog.dismiss();
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_account_center:// 个人中心
                if (userInfoIsNull())
                    return;
                intent = new Intent(mContext, AccountCenterActivity.class);
                intent.putExtra("hasEscrowedFlag", mHasEscrowedFlag);
                intent.putExtra("realvalue", mRealValue);
                intent.putExtra("openEscrowUrl", openEscrowUrl);
                startActivity(intent);
                break;
            case R.id.iv_msg:// 消息中心
                intent = new Intent();
                intent.setClass(mContext, MessageCentreActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_sign:// 签到
                // goto sign activity, now we use web
                intent = new Intent(mContext, MainWebActivity.class);
                intent.putExtra("title", "签到");
                intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "#/dormitory");
                startActivity(intent);
                break;
            case R.id.iv_view_net_asset_toggle:
                SpUtils.setBoolean(SpUtils.Table.CONFIG, SpUtils.Config.IS_SHOW_ACCOUNT, !isAccountVisible);
                toggleBriefViewVisible();
                break;
//            银行存管功能暂无
//            case R.id.tv_open_ecw_account_right_now:
//                if (mIsAllowEscrow) {
//                    showOpenEcwDialog(openEscrowUrl);
//                } else {
//                    httpGainOpenAccountService.getOpenAccountInfo();
//                }
//                break;
            case R.id.tv_recharge:// 充值
                EcwType = 1;
                checkAccountType();
                break;
            case R.id.tv_withdrawals: // 提现
                EcwType = 2;
                checkAccountType();
                break;
//            case R.id.ll_account_insurance_container:
//                if (!CommonUtil.isNullOrEmpty(accountData.getSafety_url())) {
//                    intent = new Intent(mContext, MainWebActivity.class);
//                    intent.putExtra("title", "银行卡安全保障");
//                    intent.putExtra("web_url", accountData.getSafety_url());
//                    startActivity(intent);
//                }
//                break;
            case R.id.ll_net_asset_container:
            case R.id.ll_account_brief_view_container:
                intent = new Intent(mContext, CapitalTotalViewActivity.class);
                intent.putExtra(ACCOUNT_DATA, accountData);
                intent.putExtra("openEscrowUrl", openEscrowUrl);
                startActivity(intent);
                break;
            case R.id.ll_invest_record_container:// 投资记录
                if (userInfoIsNull())
                    return;
                intent = new Intent();
                intent.setClass(mContext, ManageFinaceInfoListReActivity.class);
                intent.putExtra("isTransfer", false);
                intent.putExtra("tvYuQi", tvYuQiQiDaiShouYiStr);
                intent.putExtra("daiShouBenJinStr", daiShouBenJinStr);
                startActivity(intent);
                break;
            case R.id.ll_fund_record_container:// 资金记录
                if (userInfoIsNull())
                    return;
                EcwType = 3;
                checkAccountType();
                break;
            case R.id.ll_esw_account_container:// 银行存管
                if (userInfoIsNull())
                    return;
                startActivity(new Intent(mContext, CunGuanAccountActivity.class)
                        .putExtra("openEscrowUrl", openEscrowUrl));
                break;
            case R.id.ll_fund_account_container:// 基金账户
                String access_token = GoLoginUtil.getAccessToken(this);// 获取access token
                intent = new Intent(mContext, FundAccountActivity.class);
                intent.putExtra(Constants.ACCESS_TOKEN_KEY, access_token);
                startActivity(intent);
                break;
            case R.id.ll_invite_friend_container:// 邀请好友
                if (data != null && null != data.getMap()) {
                    MessageTypeNew type = data.getMap().get(MsgUtil.TYPE_ACCOUNT_USERRECOMMED);
                    if (type != null) {
                        ivInviteFriendBadge.setVisibility(View.GONE);
                        List<MsgNew> msgNews = type.getMessages();
                        for (MsgNew msgNew : msgNews) {
                            msgNew.setDirty(true);
                            MemorySave.MS.oldAccountBadgeNum--;
                        }
                        MsgUtil.setObj(this, MsgUtil.ACCOUNT, data);

                        intent = new Intent();
                        intent.setAction(MsgUtil.MSG_ACTION_REFRESH);
                        intent.putExtra("flag", 1);
                        sendBroadcast(intent);
                    }
                }
                Intent intent_invite = new Intent();
                if (!MemorySave.MS.mIsLogin) {
                    intent_invite.setClass(mContext, LoginActivity.class);
                    intent_invite.putExtra("goClass", InviteActivity.class.getName());
                } else {
                    intent_invite.setClass(mContext, InviteActivity.class);
                }
                startActivity(intent_invite);
                break;
            case R.id.ll_my_reward_container:// 我的奖励
                if (data != null && null != data.getMap()) {
                    ivMyRewardBadge.setVisibility(View.GONE);
                    MessageTypeNew type1 = data.getMap().get(MsgUtil.TYPE_ACCOUNT_AWARDS_BONUS);
                    MessageTypeNew type2 = data.getMap().get(MsgUtil.TYPE_ACCOUNT_AWARDS_RATETICKET);
                    MessageTypeNew type3 = data.getMap().get(MsgUtil.TYPE_ACCOUNT_AWARDS_REDUCETICKET);
                    MessageTypeNew type4 = data.getMap().get(MsgUtil.TYPE_ACCOUNT_AWARDS_OTHERS);
                    if (type1 != null) {
                        List<MsgNew> msgNews = type1.getMessages();
                        for (MsgNew msgNew : msgNews) {
                            msgNew.setDirty(true);
                            MemorySave.MS.oldAccountBadgeNum--;
                        }
                    }
                    if (type2 != null) {
                        List<MsgNew> msgNews = type2.getMessages();
                        for (MsgNew msgNew : msgNews) {
                            msgNew.setDirty(true);
                            MemorySave.MS.oldAccountBadgeNum--;
                        }
                    }
                    if (type3 != null) {
                        List<MsgNew> msgNews = type3.getMessages();
                        for (MsgNew msgNew : msgNews) {
                            msgNew.setDirty(true);
                            MemorySave.MS.oldAccountBadgeNum--;
                        }
                    }
                    if (type4 != null) {
                        List<MsgNew> msgNews = type4.getMessages();
                        for (MsgNew msgNew : msgNews) {
                            msgNew.setDirty(true);
                            MemorySave.MS.oldAccountBadgeNum--;
                        }
                    }

                    MsgUtil.setObj(this, MsgUtil.ACCOUNT, data);
                    intent = new Intent();
                    intent.setAction(MsgUtil.MSG_ACTION_REFRESH);
                    intent.putExtra("flag", 1);
                    sendBroadcast(intent);
                }

                intent = new Intent();
                intent.putExtra("switch_wap", "1");
                intent.putExtra("switch_title", false);
                if (!MemorySave.MS.mIsLogin) {
                    intent.setClass(mContext, LoginActivity.class);
                    intent.putExtra("goClass", MyRewardActivity.class.getName());
                } else {
                    intent.setClass(AccountActivity.this, MyRewardActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.ll_financial_cash_container:// 理财金
                if (data != null && null != data.getMap()) {
                    MessageTypeNew type = data.getMap().get(MsgUtil.TYPE_ACCOUNT_TYJ);
                    if (type != null) {
                        ivFinancialCashBadge.setVisibility(View.GONE);

                        List<MsgNew> msgNews = type.getMessages();
                        for (MsgNew msgNew : msgNews) {
                            msgNew.setDirty(true);
                            MemorySave.MS.oldAccountBadgeNum--;
                        }
                        MsgUtil.setObj(this, MsgUtil.ACCOUNT, data);

                        intent = new Intent();
                        intent.setAction(MsgUtil.MSG_ACTION_REFRESH);
                        intent.putExtra("flag", 1);
                        sendBroadcast(intent);

                    }
                }
                Intent goldIntent = new Intent(mContext, FinancialCashActivity.class);
                goldIntent.putExtra("hasEscrowedFlag", mHasEscrowedFlag);
                startActivity(goldIntent);
                break;
            case R.id.ll_my_score_container:// 我的积分
                // go to score record activity
                intent = new Intent(mContext, MyScoreActivity.class);
                intent.putExtra("totalScore", totalScore);
                intent.putExtra("todayScore", todayScore);
                startActivity(intent);
                break;
            case R.id.ll_my_public_service_container:// 我的公益
//                暂无此功能
//                intent = new Intent(mContext, MainWebActivity.class);
//                intent.putExtra("title", "我的公益");
//                intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "#/publicWelfare");
//                startActivity(intent);
                break;
            case R.id.tv_online_customer_service:// 在线客服
                String userName = GoLoginUtil.getUserName(mContext);
                String onlineUrl;

                intent = new Intent(mContext, MainWebActivity.class);
                if (userName != null) {
                    onlineUrl = "https://webchat.7moor.com/wapchat.html?accessId=00060720-e6df-11e7-89a7-4151a0fa6525&fromUrl=m.lcshdai.com&clientId=" + userName;
                } else {
                    String result = getResult();
                    onlineUrl = "https://webchat.7moor.com/wapchat.html?accessId=00060720-e6df-11e7-89a7-4151a0fa6525&fromUrl=m.lcshdai.com&clientId=" + result;
                }
                intent.putExtra("title", "在线客服");
                intent.putExtra("web_url", onlineUrl);
                startActivity(intent);

//                String domain = "trj.udesk.cn";
//                String app_key = "923b05ba3579961081d9aa152e468fc2";
//                String app_id = "e732a6951c8e09db";
//                if (!TextUtils.isEmpty(domain) && !TextUtils.isEmpty(app_key)) {
//                    UdeskSDKManager.getInstance().initApiKey(getApplicationContext(), domain, app_key, app_id);
//                    String sdkToken = SpUtils.getString(SpUtils.Table.CONFIG, SpUtils.Config.U_SDK_TOKEN);
//                    if (TextUtils.isEmpty(sdkToken)) {
//                        sdkToken = UUID.randomUUID().toString();
//                    }
//                    Map<String, String> info = new HashMap<String, String>();
//                    info.put(UdeskConst.UdeskUserInfo.USER_SDK_TOKEN, sdkToken);
//                    info.put(UdeskConst.UdeskUserInfo.NICK_NAME, sdkToken);
//                    UdeskSDKManager.getInstance().setUserInfo(getApplicationContext(), sdkToken, info);
//                    saveDomainAndKey(domain, app_key, app_id);
//                    SpUtils.setString(SpUtils.Table.CONFIG, SpUtils.Config.U_SDK_TOKEN, sdkToken);
//                    UdeskSDKManager.getInstance().entryChat(mContext);
//                }
                break;
            case R.id.tv_400_customer_service:// 客服
                // 显示弹窗
                if (null == hotLineDialog) {
                    initHotLineDialog();
                    hotLineDialog.show();
                } else {
                    hotLineDialog.show();
                }
                break;
            default:
                break;
        }
    }

    //    获取6位随机数
    @NonNull
    private String getResult() {
        java.util.Random random = new java.util.Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        return result;
    }

    //    客服热线Dialog
    private void initHotLineDialog() {
        if (hotLineDialog == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.layout_dialog_hotline, null);
            TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
            TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);

            tvCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hotLineDialog.dismiss();
                }
            });

            tvConfirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hotLine = getResources().getString(R.string.customer_service_hot_line);
                    Intent phoneIntent = new Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:" + hotLine.replaceAll("-", "")));
                    startActivity(phoneIntent);
                }
            });

            hotLineDialog = new Dialog(this, R.style.style_loading_dialog);
            hotLineDialog.setContentView(view);
            hotLineDialog.setCancelable(true);
        }
    }

//    private void saveDomainAndKey(String domain, String app_key, String app_id) {
//        SpUtils.setString(SpUtils.Table.CONFIG, SpUtils.Config.U_DOMAIN, domain);
//        SpUtils.setString(SpUtils.Table.CONFIG, SpUtils.Config.U_APP_KEY, app_key);
//        SpUtils.setString(SpUtils.Table.CONFIG, SpUtils.Config.U_APP_ID, app_id);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        uid = SpUtils.getString(SpUtils.Table.USER, SpUtils.User.UID);

        MemorySave.MS.mIsGoFinanceRecord = false;
        if (MemorySave.MS.mIsGoHome) {
            ((AbsActivityGroup) getParent()).goFinanceTemp();
            MemorySave.MS.mIsGoHome = false;
            return;
        }

        if (MemorySave.MS.mIsGoFinance || MemorySave.MS.mIsFinanceInfoFinish) {
            ((AbsActivityGroup) getParent()).goFinanceTemp();
            MemorySave.MS.mIsGoFinance = false;
            return;
        }

        if (MemorySave.MS.mIsGoFinanceInfo) {
            MemorySave.MS.mIsGoFinanceInfo = false;
            MemorySave.MS.mAlarmLoginFlag = true;
            Intent intent = new Intent();
            intent.setClass(AccountActivity.this, FinanceProjectDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("prj_id", MemorySave.MS.mAlarmPrjId);
            bundle.putString("mTransferId", "0");
            intent.putExtras(bundle);
            startActivity(intent);
            return;
        }

        if (MemorySave.MS.mIsLogined && !MemorySave.MS.mIsLoginout) {
            ivAccountCenter.setVisibility(View.VISIBLE);
            if (!MemorySave.MS.mIsLogining) {
                loadData();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        if (MemorySave.MS.mIsGoAccount) {
            MemorySave.MS.mIsGoAccount = false;
            return;
        }

        if (!MemorySave.MS.mIsLogin) {
            ivMyRewardBadge.setVisibility(View.GONE);
            ivFinancialCashBadge.setVisibility(View.GONE);
            ivInviteFriendBadge.setVisibility(View.GONE);
        } else {
            data = (MessageLocalData) MsgUtil.getObj(this, MsgUtil.ACCOUNT);
            if (data != null) {
                initDot(data);
            }
        }
    }

    /**
     * 处理账户页面小红点展示
     *
     * @param data 本地存储的消息数据
     */
    public void initDot(MessageLocalData data) {
        if (data.getMap().size() > 0) {
            for (String key : data.getMap().keySet()) {
                if (key.equals(MsgUtil.TYPE_ACCOUNT_AWARDS_BONUS)
                        || key.equals(MsgUtil.TYPE_ACCOUNT_AWARDS_RATETICKET)
                        || key.equals(MsgUtil.TYPE_ACCOUNT_AWARDS_REDUCETICKET)
                        || key.equals(MsgUtil.TYPE_ACCOUNT_AWARDS_OTHERS)) {
                    MessageTypeNew type = data.getMap().get(key);
                    List<MsgNew> msgNews = type.getMessages();
                    for (MsgNew msgNew : msgNews) {
                        if (!msgNew.isDirty()) {
                            ivMyRewardBadge.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }
                if (key.equals(MsgUtil.TYPE_ACCOUNT_TYJ)) {
                    MessageTypeNew type = data.getMap().get(key);
                    List<MsgNew> msgNews = type.getMessages();
                    for (MsgNew msgNew : msgNews) {
                        if (!msgNew.isDirty()) {
                            ivFinancialCashBadge.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }
                if (key.equals(MsgUtil.TYPE_ACCOUNT_USERRECOMMED)) {
                    MessageTypeNew type = data.getMap().get(key);
                    List<MsgNew> msgNews = type.getMessages();
                    for (MsgNew msgNew : msgNews) {
                        if (!msgNew.isDirty()) {
                            ivInviteFriendBadge.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void loadData() {
        mHasEscrowedFlag = InitFlag;
        mIsZheshangCardFlag = InitFlag;
        EcwType = 0;
        mOpenEcwMap = null;
        GoOpenEcw = false;
        progressContainer.setVisibility(View.VISIBLE);
        mHttpHasEscrowedService.hasEscrowed();
        mHttpIsZheshangCardService.isZheshangCard();
        mHttpGetEscrowRemindService.getEscrowRemind();
        ApiService.getAccountInfo(new ProJsonHandler<AccountJson>(new BaseCallback<AccountJson>() {
            @Override
            protected void onRightData(AccountJson response) {
                progressContainer.setVisibility(View.GONE);
                accountData = response.getData();
                mIndext = 0;
                setView(accountData);
                if (!CommonUtil.isNullOrEmpty(accountData.getIs_safety_ins())) {
                    SpUtils.setString(SpUtils.Table.USER, SpUtils.User.IS_ACCOUNT_INSURANCE,
                            accountData.getIs_safety_ins());
                    SpUtils.setString(SpUtils.Table.USER, SpUtils.User.ACCOUNT_INSURANCE_URL,
                            accountData.getSafety_url());
                    if ("0".equals(accountData.getIs_safety_ins())) {
                        tvAccountInsurance.setText("开启银行卡安全保障");
                        ivAccountInsuranceIcon.setImageResource(R.drawable.icon_account_insurance_state0);
                    } else if ("1".equals(accountData.getIs_safety_ins())) {
                        tvAccountInsurance.setText("银行卡安全由中国人保保障中");
                        ivAccountInsuranceIcon.setImageResource(R.drawable.icon_account_insurance_state1);
                    } else if ("2".equals(accountData.getIs_safety_ins())) {
                        tvAccountInsurance.setText("银行卡安全由中国人保保障中");
                        ivAccountInsuranceIcon.setImageResource(R.drawable.icon_account_insurance_state2);
                    }
                } else {
//                    llAccountInsuranceContainer.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(String message) {
                super.onError(message);
                progressContainer.setVisibility(View.GONE);
                ToastUtil.showToast(mContext, message);

            }
        }, mContext), mContext);
        gainMessage(uid);
        mHttpSignService.sign();
        mHttpSignUserInfoService.getSignUserInfo();
        mHttpAdsService.getAdsByUid();
        getAccessTokenService.getAccessToken("android", CommonUtil.getDeviceId(mContext));
        isShowCfyService.isShowCfy();
    }

    /**
     * 发送获取消息请求
     *
     * @param uid
     */
    public void gainMessage(String uid) {
        int account_sequence = MsgUtil.getInt(this, MsgUtil.ACCOUNT_SEQUENCE);
        int invest_sequence = MsgUtil.getInt(this, MsgUtil.INVEST_SEQUENCE);
        int discovery_sequence = MsgUtil.getInt(this, MsgUtil.DISCOVERY_SEQUENCE);
        if ("".equals(uid)) {
            uid = "0";
        }
        if (invest_sequence == -1) {
            invest_sequence = 0;
        }
        if (account_sequence == -1) {
            account_sequence = 0;
        }
        if (discovery_sequence == -1) {
            discovery_sequence = 0;
        }
        hms.getAllMsg(uid, invest_sequence, discovery_sequence, account_sequence);
    }

    private void setView(AccountData accountData) {
        if (accountData.getSource() != null && accountData.getSource().equals("scwangxinld")) {
            ivWxld.setVisibility(View.VISIBLE);
        }
        mRealValue = Double.parseDouble(accountData.getTotalAccountView().replace(",", ""));
        mRealValue1 = Double.parseDouble(accountData.getMoney_invest().replace(",", ""));
        mRealValue2 = Double.parseDouble(accountData.getMoney_debt().replace(",", ""));
        mRealValue3 = Double.parseDouble(accountData.getAmount_view().replace(",", ""));
        mRealValue4 = Double.parseDouble(accountData.getTotal_profit().replace(",", ""));
        move();
        tvYuQiQiDaiShouYiStr = accountData.getWill_profit_view();
        daiShouBenJinStr = accountData.getMoney_invest();
    }

    @Override
    public void getAccessTokenSuccess(AccessTokenJson response) {
        if (null != response) {
            String access_token = response.getData().getAccess_token();
            GoLoginUtil.saveAccessToken(access_token, this);
            checkFundRegisterService.checkFundRegister(access_token, GoLoginUtil.getUcId(this));
        }
    }

    @Override
    public void getAccessTokenFailed() {
        Log.e("getAccessTokenFailed", "获取理财AccessToken失败");
    }

    @Override
    public void signSuccess(SignJson response) {
        if (null != response) {
            if (response.getBoolen().equals("1")) {
                if (response.getData() != null) {
                    if (response.getData().getFlag() == 0) {
                        if (!CommonUtil.isNullOrEmpty(response.getData().getScore())) {
                            showSignDialog(response.getData().getScore());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void signFailed() {

    }

    @Override
    public void getSignUserInfoSuccess(SignUserInfoJson response) {
        if (null != response) {
            if (response.getBoolen().equals("1")) {
                if (response.getData() != null) {
                    if (!CommonUtil.isNullOrEmpty(response.getData().getTotalScore())) {
                        totalScore = response.getData().getTotalScore();
                        todayScore = response.getData().getTotayScore();
                        tvMyScoreDescribe.setText(totalScore + "积分");
                    }
                }
            }
        }
    }

    @Override
    public void getSignUserInfoFailed() {

    }

    @Override
    public void checkFundRegisterSuccess(CheckFundRegisterJson response) {
        if (null != response) {
            if (response.getBoolen().equals("1")) {
                if (response.getData().getIs_register().equals("0")) {
                    tvFundAccountOpenState.setText("立即开通");
                } else {
                    tvFundAccountOpenState.setText("");
                }
            }
        }
    }

    @Override
    public void checkFundRegisterFailed(BaseJson errorResponse) {

    }

    @Override
    public void isShowCfySuccess(IsShowCfyJson response) {
        if (null != response) {
            if (null != response.getData()) {
                if (response.getData().getCfy_show() == 1) {
                    llFundAccountContainer.setVisibility(View.VISIBLE);
                    llFundDividerContainer.setVisibility(View.VISIBLE);
                } else {
                    llFundAccountContainer.setVisibility(View.GONE);
                    llFundDividerContainer.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void isShowCfyFailed(BaseJson errorResponse) {
        llFundAccountContainer.setVisibility(View.GONE);
        llFundDividerContainer.setVisibility(View.GONE);
    }

    private class MyTimerTaskT extends TimerTask {

        public void run() {

            int cont = 3;
            if (mIndext < 10) {
                mIndext++;
                cont = 3;
            } else {
                cont = 0;
            }
            handler.sendMessageDelayed(handler.obtainMessage(cont, mIndext + ""), MI);
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 3) {
                mIndext = Integer.parseInt(String.valueOf(msg.obj));
                Double value1 = mRealValue1 * mIndext / 10;
                Double value2 = mRealValue2 * mIndext / 10;
                Double value3 = mRealValue3 * mIndext / 10;
                Double value4 = mRealValue4 * mIndext / 10;
                Double value = mRealValue * mIndext / 10;
                if (mIndext == 10) {
                    value1 = mRealValue1;
                    value2 = mRealValue2;
                    value3 = mRealValue3;
                    value4 = mRealValue4;
                    value = mRealValue;
                }
                netAssetStr = StringUtils.m2D((value));
                availableBalanceStr = StringUtils.m2D((value3));
                expectedTotalReturnStr = StringUtils.m2D((value4));
                toggleBriefViewVisible();
                int currentapiVersion = Build.VERSION.SDK_INT;
                if (currentapiVersion >= 11) {
                    tvAvailableBalance.setAlpha((float) (mIndext == 10 ? 1
                            : mIndext == 0 || mIndext == 1 ? 0.5
                            : mIndext == 2 || mIndext == 3 ? 0.6
                            : mIndext == 4 || mIndext == 5 ? 0.7
                            : mIndext == 6 || mIndext == 7 ? 0.8 : 0.9));
                    tvExpectedTotalReturn.setAlpha((float) (mIndext == 10 ? 1
                            : mIndext == 0 || mIndext == 1 ? 0.5
                            : mIndext == 2 || mIndext == 3 ? 0.6
                            : mIndext == 4 || mIndext == 5 ? 0.7
                            : mIndext == 6 || mIndext == 7 ? 0.8 : 0.9));
                    tvNetAsset.setAlpha((float) (mIndext == 10 ? 1
                            : mIndext == 0 || mIndext == 1 ? 0.5
                            : mIndext == 2 || mIndext == 3 ? 0.6
                            : mIndext == 4 || mIndext == 5 ? 0.7
                            : mIndext == 6 || mIndext == 7 ? 0.8 : 0.9));
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mLoginReceiver);
    }

    int mIndext = 0;

    private void move() {
        if (mIndext != 10) {
            if (timer != null) {
                if (mTimerTask != null)
                    mTimerTask.cancel(); // 将原任务从队列中移除
                mTimerTask = new MyTimerTaskT(); // 新建
                timer.schedule(mTimerTask, 10, MI);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE) {
            if (resultCode == LoginActivity.LOGIN_SUCCESS) {
                loadData();
            } else {
                // finish();
            }
        }
        getIntent().removeExtra("goClass");
    }

    public boolean userInfoIsNull() {
        if (MemorySave.MS.userInfo == null) {
            MemorySave.MS.isSetUnamego = false;
            ((AbsActivityGroup) getParent()).goFinanceTemp();
            return true;
        }
        return false;
    }

    @Override
    public void gainAccountSettingSuccess(AccountSettingJson response) {
        try {
            progressContainer.setVisibility(View.GONE);
            if (null == response)
                return;
            String boolen = response.getBoolen();
            if (response != null) {
                if (boolen.equals("1")) {
                    AccountSettingData json = response.getData();
                    if ("1".equals(json.getIs_id_auth())) {
                        MemorySave.MS.userInfo.is_id_auth = json.getIs_id_auth();
                        if ("1".equals(json.getIs_paypwd_mobile_set())) {
                            MemorySave.MS.userInfo.is_paypwd_mobile_set = json.getIs_paypwd_mobile_set();
                            goCashOut();
                        } else {
                            if (setPayPswDialog == null) {
                                initSetPayPswDialog();
                                setPayPswDialog.show();
                            } else {
                                setPayPswDialog.show();
                            }
                        }
                    } else {
                        String title = "温馨提示";
                        String msg = "充值成功后系统自动进行实名认证！";
                        String absBtn = "现在去充值";
                        String negBtn = "取消";
                        mRzDialog = createDialog1(title, msg, absBtn, negBtn, new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                Intent new_recharge_intent = new Intent();
                                new_recharge_intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/addBank");
                                new_recharge_intent.setClass(AccountActivity.this,
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainAccountSettingFail() {
        progressContainer.setVisibility(View.GONE);
    }

    @Override
    public void gainMessageSuccess(MessageJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                // 先保存之前的account值
                data = (MessageLocalData) MsgUtil.getObj(this, MsgUtil.ACCOUNT);
                if (null != data) {
                    MemorySave.MS.oldAccountBadgeNum = data.getCount();
                }
                List<MessageLocalData> list = MsgUtil.convertList(response.getData());
                if (list != null) {
                    MsgUtil.initData(this, list);
                }
                data = (MessageLocalData) MsgUtil.getObj(this, MsgUtil.ACCOUNT);
                if (data != null) {
                    initDot(data);
                }
            }
        }
    }

    @Override
    public void gainMessageFail() {

    }

    private class LoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean loginSucceed = intent.getBooleanExtra("loginSucceed", false);
            if (loginSucceed) {
                loadData();
            } else {
                ToastUtil.showShortToast(mContext, "登录失败，请检查网络连接重试");
            }
        }

    }

    private void goRecharge() {
        if (mHasEscrowedFlag == 0) {
            Intent new_recharge_intent = new Intent();
            new_recharge_intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/addBank");
            new_recharge_intent.setClass(AccountActivity.this, MainWebActivity.class);
            startActivity(new_recharge_intent);
        } else if (mHasEscrowedFlag == 1) {
            Intent new_recharge_intent = new Intent();
            new_recharge_intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/submitPay");
            new_recharge_intent.putExtra("title", "充值");
            new_recharge_intent.setClass(AccountActivity.this, MainWebActivity.class);
            startActivity(new_recharge_intent);
        }
    }

    private void goCashOut() {
        if ("1".equals(MemorySave.MS.userInfo.is_id_auth)) {
            if ("1".equals(MemorySave.MS.userInfo.is_paypwd_mobile_set)) {
                if (mHasEscrowedFlag == 0) {
//                    提现
                    Intent intent = new Intent(mContext, WithdrawalsActivity.class);
                    startActivityForResult(intent, 7);
                } else if (mHasEscrowedFlag == 1) {
                    Intent intent = new Intent(mContext, CashOutActivity.class);
                    startActivityForResult(intent, 7);
                }
            } else {
                hass.gainAccountSetting("");
                progressContainer.setVisibility(View.VISIBLE);
            }
        } else {
            hass.gainAccountSetting("");
            progressContainer.setVisibility(View.VISIBLE);
        }
    }

    private void goFinancialRecords() {
        startActivity(new Intent(AccountActivity.this, BalancePaymentsActivity.class));
    }

    private synchronized void checkAccountType() {
        if (mHasEscrowedFlag == InitFlag) {
            mHttpHasEscrowedService.hasEscrowed();
            if (mIsZheshangCardFlag == InitFlag) {
                mHttpIsZheshangCardService.isZheshangCard();
            }
        } else if (mHasEscrowedFlag == 0) {
            if (EcwType == 0) {
//                httpGainOpenAccountService.getOpenAccountInfo();
            } else if (EcwType == 1) {
                goRecharge();
            } else if (EcwType == 2) {
                goCashOut();
            } else if (EcwType == 3) {
                goFinancialRecords();
            }
            EcwType = 0;
        } else if (mHasEscrowedFlag == 1) {
            if (mIsZheshangCardFlag == InitFlag) {
                mHttpIsZheshangCardService.isZheshangCard();
            } else {
                if (mIsZheshangCardFlag == 1) {    //浙商卡
                    if (EcwType != 0) {
                        showZheshangToast(EcwType);
                    }
                } else {
                    if (EcwType == 1) {
                        goRecharge();
                    } else if (EcwType == 2) {
                        goCashOut();
                    } else if (EcwType == 3) {
                        goFinancialRecords();
                    }
                }
                EcwType = 0;
            }
        }
    }

    //    立即开通银行存管Dialog
    private void showOpenEcwDialog(final String openEscrowUrl) {
        if (openEcwDialog == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.layout_dialog_ecw, null);
            TextView tvPositive = (TextView) view.findViewById(R.id.tv_positive);
            TextView tvNegative = (TextView) view.findViewById(R.id.tv_negative);
            TextView tvOpenAccountLabel = (TextView) view.findViewById(R.id.tv_how_to_open_account_label);
            TextView tvOpenAccountContent = (TextView) view.findViewById(R.id.tv_open_account_content);
            TextView tvGettingToKnowMore = (TextView) view.findViewById(R.id.tv_how_to_open_account_know_more);

            if (CommonUtil.isNullOrEmpty(openEscrowUrl)) {
                tvGettingToKnowMore.setVisibility(View.VISIBLE);// 显示
                tvOpenAccountLabel.setText(R.string.ecw_dialog_open_ecw_account_how_old);
                tvOpenAccountContent.setText(R.string.ecw_dialog_open_ecw_account_instruction_how_old);
            } else {
                tvGettingToKnowMore.setVisibility(View.GONE);// 隐藏
                tvOpenAccountLabel.setText(R.string.ecw_dialog_open_ecw_account_how_new);
                tvOpenAccountContent.setText(R.string.ecw_dialog_open_ecw_account_instruction_how_new);
            }
            tvGettingToKnowMore.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "#/process");// 替换成了解更多url
                    intent.putExtra("title", "了解更多");
                    intent.setClass(mContext, MainWebActivity.class);
                    startActivity(intent);
                }
            });

            tvNegative.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (openEcwDialog != null && openEcwDialog.isShowing()) {
                        openEcwDialog.dismiss();
                        openEcwDialog = null;
                    }
                    showRemindDialog();
                }
            });
            tvPositive.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonUtil.isNullOrEmpty(openEscrowUrl)) {
                        if (mOpenEcwMap == null) {
                            GoOpenEcw = true;
                            // 获取开户信息
                            httpGainOpenAccountService.getOpenAccountInfo();
                        } else {
                            for (String key : mOpenEcwMap.keySet()) {
                                Log.e(tag, key + " : " + mOpenEcwMap.get(key));
                            }
                            boolean result = ZSBank.onEvent(AccountActivity.this, "0001", mOpenEcwMap);
                            System.out.println("是否安装浙商理财App: " + result);
                        }
                    } else {
                        Intent intent = new Intent(mContext, MainWebActivity.class);
                        intent.putExtra("web_url", openEscrowUrl);
                        intent.putExtra("need_header", 0);
                        intent.putExtra("title", "开通存管");
                        startActivity(intent);
                    }
                    if (openEcwDialog != null && openEcwDialog.isShowing()) {
                        openEcwDialog.dismiss();
                        openEcwDialog = null;
                    }
                }
            });

            openEcwDialog = new Dialog(this, R.style.style_loading_dialog);
            openEcwDialog.setContentView(view);
            openEcwDialog.setCancelable(true);
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = openEcwDialog.getWindow().getAttributes();
            lp.width = (int) (display.getWidth() * 0.8); //设置宽度
            openEcwDialog.getWindow().setAttributes(lp);
        }
        if (!openEcwDialog.isShowing()) {
            openEcwDialog.show();
        }
    }

    //    签到Dialog
    private void showSignDialog(String score) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_dialog_sign, null);
        TextView tvScore = (TextView) view.findViewById(R.id.tv_score);
        tvScore.setText(score);
        signDialog = new Dialog(this, R.style.style_loading_dialog);
        signDialog.setContentView(view);
        signDialog.setCancelable(true);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = signDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.8); //设置宽度
        signDialog.getWindow().setAttributes(lp);
        signDialog.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                signDialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // cancel the timer, otherwise, you may receive a
                // crash
            }
        }, 2000);
    }

    //    暂不开通银行存管Dialog
    private void showRemindDialog() {
        if (remindDialog == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.layout_dialog_remind, null);
            TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);

            tvConfirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    remindDialog.dismiss();
                    mHttpSetEscrowRemindService.setEscrowRemind();
                }
            });

            remindDialog = new Dialog(this, R.style.style_loading_dialog);
            remindDialog.setContentView(view);
            remindDialog.setCancelable(true);
        }
        if (!remindDialog.isShowing()) {
            remindDialog.show();
        }
    }

    private void showZheshangToast(int type) {
        if (mIsZheshangCardFlag == 1) {
            if (type == 1) {
                ToastUtil.showShortToast(mContext, "浙商卡用户无需充值，卡内资金可直接投资，请保证卡内资金充足");
            } else if (type == 2) {
                ToastUtil.showShortToast(mContext, "浙商卡无需提现，资金直接回到卡内");
            } else if (type == 3) {
                ToastUtil.showShortToast(mContext, "资金记录请登录浙商银行直销银行查询");
            }
        }
    }

    /**
     * 账户简览是否可见
     */
    private void toggleBriefViewVisible() {
        isAccountVisible = SpUtils.getBoolean(SpUtils.Table.CONFIG, SpUtils.Config.IS_SHOW_ACCOUNT, false);
        if (!isAccountVisible) {
            tvNetAsset.setText("******");
            tvAvailableBalance.setText("******");
            tvExpectedTotalReturn.setText("******");
            ivViewNetAssetToggle.setImageResource(R.drawable.icon_account_hide);
        } else {
            tvNetAsset.setText(netAssetStr);
            tvAvailableBalance.setText(availableBalanceStr);
            tvExpectedTotalReturn.setText(expectedTotalReturnStr);
            ivViewNetAssetToggle.setImageResource(R.drawable.icon_account_open);
        }
    }

    @Override
    public void gainHasEscrowedSuccess(AccountHasEscrowedJson response) {
        if (response != null) {
            AccountHasEscrowedData data = response.getData();
            if (data != null) {
                mHasEscrowedFlag = data.getFlag();
                if (data.getFlag() != 1) {
//                    tvOpenEcwAccountRightNow.setVisibility(View.VISIBLE);
                    tvEcwTipsLabel.setVisibility(View.GONE);
                    tvEswAccountDescribe.setText("未开通");
                } else {
//                    tvOpenEcwAccountRightNow.setVisibility(View.GONE);
                    tvEcwTipsLabel.setVisibility(View.VISIBLE);
                    tvEswAccountDescribe.setText("已开通");
                }
                checkAccountType();
            }
        }
    }

    @Override
    public void gainHasEscrowedFail() {

    }

    @Override
    public void gainIsZheshangCardSuccess(AccountIsZheshangCardJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                AccountIsZheshangCardData data = response.getData();
                mIsZheshangCardFlag = data.getFlag();
                checkAccountType();
            }
        }
    }

    @Override
    public void gainIsZheshangCardFail() {

    }

    @Override
    public void getOpenAccountInfoSuccess(OpenAccountJson response) {
        if (response != null)
            Log.i(tag, response.getMessage());

        if (response != null) {
            // too if loading is shown, hide it
            if (response.getBoolen().equals("1")) {
                if (mIsAllowEscrow) {
                    OpenAccountData openAccountData = response.getData();
                    if (openAccountData != null) {
                        Map<String, String> map = CommonUtil.convertAccountData2Map(openAccountData);
                        if (mOpenEcwMap == null) {
                            mOpenEcwMap = map;
                            if (GoOpenEcw) {
                                GoOpenEcw = false;
                                for (String key : mOpenEcwMap.keySet()) {
                                    Log.e(tag, key + " : " + mOpenEcwMap.get(key));
                                }
                                boolean result = ZSBank.onEvent(this, "0001", map);
                                System.out.println("是否安装浙商理财App: " + result);
                            }
                        }
                    }
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

    @Override
    public void getEscrowRemindSuccess(GetEscrowRemindJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                GetEscrowRemindData data = response.getData();
                if (data != null) {
                    openEscrowUrl = data.getOpenEscrowUrl();
                    if (data.getFlag() == 1
                            && data.getIs_allow_escrow() == 1) {
//                        showOpenEcwDialog(openEscrowUrl);
                    }
                    mIsAllowEscrow = data.getIs_allow_escrow() == 1;
                }
            }
        }

    }

    @Override
    public void getEscrowRemindFail() {
    }

    @Override
    public void setEscrowRemindSuccess(BaseJson response) {
    }

    @Override
    public void setEscrowRemindFail() {
    }

    @Override
    public void getAdsSuccess(AdsJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                AdsData data = response.getData();
                if (data != null) {
                    final String id = data.getId();
                    final String image_url = data.getImg_url();
                    final String link_url = data.getLink_url();
                    if (!CommonUtil.isNullOrEmpty(image_url)) {
                        Glide.with(mContext).load(image_url).into(ivAd);
                        ivAd.setVisibility(View.VISIBLE);
                        ivAd.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("web_url", link_url);
                                intent.putExtra("title", "");
                                if (link_url.contains("com.toubaojia.tbj") || link_url.contains("toubaojia.com")) {
                                    intent.putExtra("need_header", "0");
                                }
                                intent.setClass(mContext, MainWebActivity.class);
                                startActivity(intent);

                            }
                        });
                    } else {
                        ivAd.setVisibility(View.GONE);
                    }
                } else {
                    ivAd.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void getAdsFail() {

    }

}
