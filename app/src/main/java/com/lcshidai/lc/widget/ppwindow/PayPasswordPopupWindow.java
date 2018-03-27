package com.lcshidai.lc.widget.ppwindow;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.ui.AgreementTextActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tendcloud.tenddata.TCAgent;
import com.lcshidai.lc.R;
import com.lcshidai.lc.entity.BankCardInfo;
import com.lcshidai.lc.impl.PIPWCallBack;
import com.lcshidai.lc.impl.finance.FinancePayImpl;
import com.lcshidai.lc.impl.finance.RewardCheckImpl;
import com.lcshidai.lc.impl.finance.UpdateJxqIncomeImpl;
import com.lcshidai.lc.model.LocalInvestPayModel;
import com.lcshidai.lc.model.finance.FinanceApplyCashoutJson;
import com.lcshidai.lc.model.finance.FinanceCheckPayPasswordJson;
import com.lcshidai.lc.model.finance.FinanceDoCashJson;
import com.lcshidai.lc.model.finance.FinancePayResultJson;
import com.lcshidai.lc.model.finance.FinanceWithdrawalsMoneyJson;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckData;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckJson;
import com.lcshidai.lc.model.finance.reward.RewardChooseInfo;
import com.lcshidai.lc.model.finance.reward.RewardInfoModel;
import com.lcshidai.lc.service.finance.HttpPayService;
import com.lcshidai.lc.service.finance.HttpUpdateJxqIncomeService;
import com.lcshidai.lc.ui.AgreementActivity;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.account.WithdrawSuccessActivity;
import com.lcshidai.lc.ui.account.pwdmanage.UserMobilePayPwdActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.finance.FinanceHongBaoActivity;
import com.lcshidai.lc.ui.finance.InvestSmsConfirmActivity;
import com.lcshidai.lc.ui.finance.InvestSuccessActivity;
import com.lcshidai.lc.ui.finance.JxqActivity;
import com.lcshidai.lc.ui.transfer.CashSuccessActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.RewardCheckUtil;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付密码输入窗口
 */
public class PayPasswordPopupWindow extends PopupWindow implements UpdateJxqIncomeImpl,
        FinancePayImpl, OnClickListener, RewardCheckImpl {
    private ImageView iv_xyb, iv_qfx, iv_sdt, iv_rys;
    private View mMenuView;
    TRJActivity context;
    private TextView ib_hidden, ib_delete, ib_0, ib_1, ib_2, ib_3, ib_4, ib_5, ib_6, ib_7, ib_8, ib_9;
    private TextView agree_tv;// , agree_tv_cash;// ,
    // bespeak_agree_tv,bespeak_agree_tv_x;
    private TextView tv_loan_prjname, tv_repay_way, tv_cash_money,
            tv_loan_rate, pay_pwd_tv_agree_and, tv_cfd_protocol, pay_pwd_tv_agree_repay, pay_pwd_tv_note0, pay_pwd_tv_note1, pay_pwd_tv_note2;
    private TextView withdrawals_tv;
    PIPWCallBack mCallBack;

    private DismissListener mDismissListener;

    private View viewBottom;
    public String serverProtocol;
    private BankCardInfo[] bankArray;
    private BankCardInfo bankCardInfo;
    private int bankSelectedPosition = 0;
    private float originalFloatRate;        // 原始浮点型利率（去掉了百分号）

    String purl;
    Dialog mLoadDialog;
    private boolean isPwdRight = false;
    RewardChooseInfo chooseInfo = new RewardChooseInfo();

    String payProtocol = "";

    private HttpPayService httpPayService;
    private HttpUpdateJxqIncomeService updateJxqIncomeService;//投资之前检查
    public boolean isXXB = false;
    private boolean isEcwAccount = false;// 是否存管用户
    private boolean isAutoInvestOpen = false;// 自动投标是否开通
    private boolean isCollection = false;// 是否是集合标
    private View viewTop;
    private ImageView iv_close;    // 关闭当前窗口
    private TextView tvInvestMoney;                 // 投资金额
    private LinearLayout llAppointPayContainer; // 预约支付容器
    // 预约时间、年化、金额
    private TextView bp_date_tv, bp_rate_tv, bp_money_tv;// bp_product_type_tv,

    private LinearLayout llFinancePayContainer; // 理财支付容器
    private LinearLayout llHbMjContainer;       // 红包与券容器
    // 红包与券label、数量、使用情况
    private TextView tvHbMjLabel, tvHbMjNum, tvHbMjUsed;
    // 加息券容器
    private LinearLayout llJxqContainer;
    // 加息券label、加息券数量、加息券使用情况
    private TextView tvJxqLabel, tvJxqNum, tvJxqUsed;
    // 实际使用容器
    private LinearLayout llActuallyUsedContainer;
    private TextView tvActuallyUsed;            // 实际使用金额
    // 年化、本息
    private TextView tvYearRate, tvBenXi;
    // 加息券收益容器
    private RelativeLayout rlJxqRewardContainer;
    private TextView tvJxqReward;

    // 密码输入部分只是界面上的模拟
    private int inputPwdLength = 0;                     // 已输入密码的长度
    private String inputPwd = "";                       // 真正记录密码
    private ImageView[] pwdIVArray = new ImageView[6];  // 密码框对应的视图
    private Drawable pwdDrawable;                       // 对应的drawable

    private LinearLayout llPayPswWithdrawalsContainer;
    // 实际到账金额、手续费、服务费
    private TextView tvActualIncomeMoney, tvSxFee, tvFwFee;
    private LinearLayout llPayPswCashContainer;

    private LinearLayout llProtocolContainer;
    private RelativeLayout rlPayInfoContainer;

    private LinearLayout ll_option_btn;    // 两个操作按钮的布局
    private Button bt_submit, bt_cancel;

    private TextView tvForgetPsw;

    private int mDuration = 400;

    private String _prj_id;                 // 项目ID
    private String _invest_amount;          // 投资金额
    private String _reward_type = "-1";     // 奖励类型
    private String _bonus_rate = null;      //
    private String _bonus_prj_term = null;  //
    private String _mjq_id = null;          // 满减券ID
    private String _jxq_id = null;          // 加息券ID
    private String _jx_rate = null;         // 加息百分比
    private String _reward_amount = null;   // 奖励金额

    private String protocolId = "0";        //  协议id
    private String protocolName = "--";     //  协议名称

    private String cfdName;                 //
    private String cfdProtocolName = "";    // 长富贷协议

    public int scrWidth;
    public int scrHeight;

    public PayPasswordPopupWindow(final TRJActivity context, PIPWCallBack callBack) {
        super(context);
        this.context = context;
        httpPayService = new HttpPayService(this, context);
        updateJxqIncomeService = new HttpUpdateJxqIncomeService(context, this);
        payProtocol = context.getResources().getString(
                R.string.protocolpayandrepay);
        mCallBack = callBack;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        支付密码输入窗口布局
        mMenuView = inflater.inflate(R.layout.pay_password_pp, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        scrWidth = dm.widthPixels;// 宽度
        scrHeight = dm.heightPixels;// 高度
        mLoadDialog = TRJActivity.createLoadingDialog(context, "加载中...", false);
        mLoadDialog.setCanceledOnTouchOutside(false);
        initViews(dm);

        bankCardInfo = new BankCardInfo();
        pwdDrawable = context.getResources().getDrawable(R.drawable.pay_pwd_dot);

        WeakReference<RewardCheckImpl> reference = new WeakReference<RewardCheckImpl>(this);
        RewardCheckUtil.getInstance().setHBCheckReference(reference);
    }

    /**
     * view and event bind
     */
    private void initViews(DisplayMetrics dm) {
        viewTop = mMenuView.findViewById(R.id.pay_pwd_ll_pay);
        iv_close = (ImageView) mMenuView.findViewById(R.id.iv_close);
        tvInvestMoney = (TextView) mMenuView.findViewById(R.id.tv_invest_money);
        // 预约控件实例化
        llAppointPayContainer = (LinearLayout) mMenuView.findViewById(R.id.ll_appoint_pay_container);
        iv_xyb = (ImageView) mMenuView.findViewById(R.id.bespeak_product_iv_xyb);
        iv_qfx = (ImageView) mMenuView.findViewById(R.id.bespeak_product_iv_qfx);
        iv_sdt = (ImageView) mMenuView.findViewById(R.id.bespeak_product_iv_sdt);
        iv_rys = (ImageView) mMenuView.findViewById(R.id.bespeak_product_iv_rys);
        bp_rate_tv = (TextView) mMenuView.findViewById(R.id.bespeak_interest_rate_tv);// 预约收益
        bp_date_tv = (TextView) mMenuView.findViewById(R.id.bespeak_date_tv);// 预约期限
        bp_money_tv = (TextView) mMenuView.findViewById(R.id.bespeak_money_tv);// 预约金额

        llFinancePayContainer = (LinearLayout) mMenuView.findViewById(R.id.ll_finance_pay_container);
        llHbMjContainer = (LinearLayout) mMenuView.findViewById(R.id.ll_hb_mj_container);
        tvHbMjLabel = (TextView) mMenuView.findViewById(R.id.tv_hb_mj_label);
        tvHbMjNum = (TextView) mMenuView.findViewById(R.id.tv_hb_mj_num);
        tvHbMjUsed = (TextView) mMenuView.findViewById(R.id.tv_hb_mj_used);
        llJxqContainer = (LinearLayout) mMenuView.findViewById(R.id.ll_jxq_container);
        tvJxqLabel = (TextView) mMenuView.findViewById(R.id.tv_jxq_label);
        tvJxqNum = (TextView) mMenuView.findViewById(R.id.tv_jxq_num);
        tvJxqUsed = (TextView) mMenuView.findViewById(R.id.tv_jxq_used);

        llActuallyUsedContainer = (LinearLayout) mMenuView.findViewById(R.id.ll_actually_used_container);
        tvActuallyUsed = (TextView) mMenuView.findViewById(R.id.tv_actually_used);

        llPayPswWithdrawalsContainer = (LinearLayout) mMenuView.findViewById(R.id.ll_pay_psw_withdrawals_container);
        tvActualIncomeMoney = (TextView) mMenuView.findViewById(R.id.withdrawals_actual_money_tv);
        tvSxFee = (TextView) mMenuView.findViewById(R.id.withdrawals_shouxu_money_tv);
        tvFwFee = (TextView) mMenuView.findViewById(R.id.withdrawals_fuwu_money_tv);

        llPayPswCashContainer = (LinearLayout) mMenuView.findViewById(R.id.ll_pay_psw_cash_container);
        llProtocolContainer = (LinearLayout) mMenuView.findViewById(R.id.ll_protocol_container);
        rlPayInfoContainer = (RelativeLayout) mMenuView.findViewById(R.id.rl_pay_info_container);

        tv_loan_prjname = (TextView) mMenuView.findViewById(R.id.tv_loan_prjname);
        tv_repay_way = (TextView) mMenuView.findViewById(R.id.tv_repay_way);
        tv_cash_money = (TextView) mMenuView.findViewById(R.id.tv_cash_money);
        tv_loan_rate = (TextView) mMenuView.findViewById(R.id.tv_loan_rate);
        pay_pwd_tv_agree_and = (TextView) mMenuView.findViewById(R.id.pay_pwd_tv_agree_and);
        tv_cfd_protocol = (TextView) mMenuView.findViewById(R.id.tv_cfd_protocol);
        pay_pwd_tv_note0 = (TextView) mMenuView.findViewById(R.id.pay_pwd_tv_note0);
        pay_pwd_tv_note1 = (TextView) mMenuView.findViewById(R.id.pay_pwd_tv_note1);
        pay_pwd_tv_note2 = (TextView) mMenuView.findViewById(R.id.pay_pwd_tv_note2);
        pay_pwd_tv_agree_repay = (TextView) mMenuView.findViewById(R.id.pay_pwd_tv_agree_repay);
        withdrawals_tv = (TextView) mMenuView.findViewById(R.id.withdrawals_money_tv);
        tvYearRate = (TextView) mMenuView.findViewById(R.id.tv_yearrate);
        tvBenXi = (TextView) mMenuView.findViewById(R.id.tv_benxi);
        rlJxqRewardContainer = (RelativeLayout) mMenuView.findViewById(R.id.rl_jxq_reward_container);
        tvJxqReward = (TextView) mMenuView.findViewById(R.id.tv_jxq_reward);
        agree_tv = (TextView) mMenuView.findViewById(R.id.pay_pwd_tv_agree);

        ll_option_btn = (LinearLayout) mMenuView.findViewById(R.id.ll_option_btn);
        bt_cancel = (Button) mMenuView.findViewById(R.id.pay_pwd_pay_bt_cancel);
        bt_submit = (Button) mMenuView.findViewById(R.id.pay_pwd_pay_bt_submit);
        bt_cancel.setOnClickListener(cancelClickListener);

        tvForgetPsw = (TextView) mMenuView.findViewById(R.id.tv_foregive_psw);
        tvForgetPsw.setOnClickListener(this);
        pay_pwd_tv_note0.setOnClickListener(this);
        pay_pwd_tv_note1.setOnClickListener(this);
        pay_pwd_tv_note2.setOnClickListener(this);

        initKeyboardAndPsw(dm);
    }

    /**
     * 初始化键盘及密码控件
     */
    private void initKeyboardAndPsw(DisplayMetrics dm) {
        pwdIVArray[0] = (ImageView) mMenuView.findViewById(R.id.pay_pwd_iv_0);
        pwdIVArray[1] = (ImageView) mMenuView.findViewById(R.id.pay_pwd_iv_1);
        pwdIVArray[2] = (ImageView) mMenuView.findViewById(R.id.pay_pwd_iv_2);
        pwdIVArray[3] = (ImageView) mMenuView.findViewById(R.id.pay_pwd_iv_3);
        pwdIVArray[4] = (ImageView) mMenuView.findViewById(R.id.pay_pwd_iv_4);
        pwdIVArray[5] = (ImageView) mMenuView.findViewById(R.id.pay_pwd_iv_5);

        viewBottom = mMenuView.findViewById(R.id.pay_pwd_ll_keyboard);
        ib_hidden = (TextView) mMenuView.findViewById(R.id.pay_pwd_keyboard_ib_hidden);
        ib_delete = (TextView) mMenuView.findViewById(R.id.pay_pwd_keyboard_ib_delete);
        ib_0 = (TextView) mMenuView.findViewById(R.id.pay_pwd_keyboard_ib_0);
        ib_1 = (TextView) mMenuView.findViewById(R.id.pay_pwd_keyboard_ib_1);
        ib_2 = (TextView) mMenuView.findViewById(R.id.pay_pwd_keyboard_ib_2);
        ib_3 = (TextView) mMenuView.findViewById(R.id.pay_pwd_keyboard_ib_3);
        ib_4 = (TextView) mMenuView.findViewById(R.id.pay_pwd_keyboard_ib_4);
        ib_5 = (TextView) mMenuView.findViewById(R.id.pay_pwd_keyboard_ib_5);
        ib_6 = (TextView) mMenuView.findViewById(R.id.pay_pwd_keyboard_ib_6);
        ib_7 = (TextView) mMenuView.findViewById(R.id.pay_pwd_keyboard_ib_7);
        ib_8 = (TextView) mMenuView.findViewById(R.id.pay_pwd_keyboard_ib_8);
        ib_9 = (TextView) mMenuView.findViewById(R.id.pay_pwd_keyboard_ib_9);

        ib_hidden.setOnClickListener(cancelClickListener);
        ib_delete.setOnClickListener(new BtnClick(-1));
        ib_0.setOnClickListener(new BtnClick(0));
        ib_1.setOnClickListener(new BtnClick(1));
        ib_2.setOnClickListener(new BtnClick(2));
        ib_3.setOnClickListener(new BtnClick(3));
        ib_4.setOnClickListener(new BtnClick(4));
        ib_5.setOnClickListener(new BtnClick(5));
        ib_6.setOnClickListener(new BtnClick(6));
        ib_7.setOnClickListener(new BtnClick(7));
        ib_8.setOnClickListener(new BtnClick(8));
        ib_9.setOnClickListener(new BtnClick(9));

        if (dm.densityDpi <= 320 && dm.densityDpi >= 240
                && ((dm.heightPixels == 960 && dm.widthPixels == 640) || dm.heightPixels < 800)) {
            ib_0.setBackgroundResource(R.drawable.circle_kb_bg_xm);
            ib_1.setBackgroundResource(R.drawable.circle_kb_bg_xm);
            ib_2.setBackgroundResource(R.drawable.circle_kb_bg_xm);
            ib_3.setBackgroundResource(R.drawable.circle_kb_bg_xm);
            ib_4.setBackgroundResource(R.drawable.circle_kb_bg_xm);
            ib_5.setBackgroundResource(R.drawable.circle_kb_bg_xm);
            ib_6.setBackgroundResource(R.drawable.circle_kb_bg_xm);
            ib_7.setBackgroundResource(R.drawable.circle_kb_bg_xm);
            ib_8.setBackgroundResource(R.drawable.circle_kb_bg_xm);
            ib_9.setBackgroundResource(R.drawable.circle_kb_bg_xm);
            ib_hidden.setBackgroundResource(R.drawable.c_hide_mx);
            ib_delete.setBackgroundResource(R.drawable.c_x_mx);
        }
    }

    private OnClickListener cancelClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            windowDismiss();
        }
    };

    private void windowDismiss() {
        if (android.os.Build.VERSION.SDK_INT < 12) {
            PayPasswordPopupWindow.this.dismiss();
            return;
        }
        ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(viewBottom, "YFraction", 0, (float) 1.5);
        movingFragmentRotator.setDuration(400);
        movingFragmentRotator.start();

        ObjectAnimator movingFragmentRotators = ObjectAnimator.ofFloat(viewTop, "YFraction", 0, (float) -1.5);
        movingFragmentRotators.setStartDelay(400);
        movingFragmentRotators.setDuration(400);
        movingFragmentRotators.start();
        movingFragmentRotators.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                PayPasswordPopupWindow.this.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
            }
        });
    }

    private class BtnClick implements OnClickListener {

        int value;

        public BtnClick(int value) {
            this.value = value;
        }

        @Override
        public void onClick(View v) {
            if (inputPwdLength == 6 && value != -1) {
                return;
            }
            switch (value) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    inputPwdLength++;
                    inputPwd += String.valueOf(value);
                    pwdIVArray[inputPwdLength - 1].setImageDrawable(pwdDrawable);

                    if (inputPwdLength == 6) {
                        loadPayPwdCheck();
                    }
                    break;
                case -1:
                    if (inputPwdLength == 0) {
                        return;
                    } else {
                        inputPwdLength--;
                        inputPwd = inputPwd.substring(0, inputPwd.length() - 1);
                        pwdIVArray[inputPwdLength].setImageDrawable(null);
                    }
                    if (isPwdRight) {
                        isPwdRight = false;
                        if (mFlag != 2) {
                            bt_submit.setEnabled(false);
                            bt_submit.setBackgroundDrawable(context.getResources()
                                    .getDrawable(R.drawable.bg_button_clickable_false));
                        }
                    }
                    break;
            }
        }
    }

    public boolean isEcwAccount() {
        return isEcwAccount;
    }

    public void setEcwAccount(boolean ecwAccount) {
        isEcwAccount = ecwAccount;
    }

    public boolean isAutoInvestOpen() {
        return isAutoInvestOpen;
    }

    public void setAutoInvestOpen(boolean autoInvestOpen) {
        isAutoInvestOpen = autoInvestOpen;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    /**
     * 普通支付调用 用于界面数据初始化
     *
     * @param mContainer 窗口依附容器
     * @param model      FinanceInvestPBuyCheckData对象
     */
    public void goAnimPlusIncome(final View mContainer, final FinanceInvestPBuyCheckData model) {
        RewardInfoModel rewardInfoModel = model.getRewardInfo();
        if (rewardInfoModel != null) {
            chooseInfo.rewardType = rewardInfoModel.getReward_type();
            chooseInfo.rewardId = rewardInfoModel.getReward_id();
        }
        bt_submit.setText("确认提交");
        protocolId = model.getProtocol_id();
        protocolName = model.getProtocol_name();
        cfdName = model.getCfd_name();
        cfdProtocolName = model.getCfd_protocol();
        originalFloatRate = Float.valueOf(model.getYearrate().replace("%", ""));
        tvYearRate.setText(model.getYearrate());

        tvHbMjLabel.setText("红包与券");
        tvHbMjNum.setVisibility(View.INVISIBLE);

        _prj_id = model.getPrjid();
        // 最优加息券显示处理
        dealWithJxq(model);
        // 最优红包或满减券处理
        dealWithHbOrMjq(model);

        llHbMjContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FinanceHongBaoActivity.class);
                intent.putExtra("prj_id", _prj_id);
                intent.putExtra("amount", _invest_amount);
                intent.putExtra("reward_type", _reward_type);
                intent.putExtra("bouns_rate", _bonus_rate);
                intent.putExtra("bouns_prj_term", _bonus_prj_term);
                intent.putExtra("mjq_id", _mjq_id);
                if (isCollection()) {
                    intent.putExtra(Constants.Project.IS_COLLECTION, 1);
                } else {
                    intent.putExtra(Constants.Project.IS_COLLECTION, 0);
                }
                context.startActivity(intent);
            }
        });

        llJxqContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, JxqActivity.class);
                intent.putExtra("prj_id", _prj_id);
                intent.putExtra("amount", _invest_amount);
                intent.putExtra("reward_type", _reward_type);
                intent.putExtra("bouns_rate", _bonus_rate);
                intent.putExtra("bouns_prj_term", _bonus_prj_term);
                intent.putExtra("jxq_id", _jxq_id);
                if (isCollection()) {
                    intent.putExtra(Constants.Project.IS_COLLECTION, 1);
                } else {
                    intent.putExtra(Constants.Project.IS_COLLECTION, 0);
                }
                context.startActivity(intent);
            }
        });

        goAnim(model.getMoney(), mContainer, 0, model.getPrjid(), model.isRepay());
    }

    /**
     * 处理加息券显示
     *
     * @param model FinanceInvestPBuyCheckData对象
     */
    private void dealWithJxq(FinanceInvestPBuyCheckData model) {
        _jxq_id = model.getAddInterestId();
        RewardCheckUtil.getInstance().setJxq_id(model.getAddInterestId());//更新内存中加息券ID
        RewardCheckUtil.getInstance().setJx_rate(model.getAddInterestView());
        tvJxqLabel.setText("加息券");
        tvBenXi.setText(model.getBenxi() + "元");
        tvJxqNum.setVisibility(View.GONE);
        if (!CommonUtil.isNullOrEmpty(model.getAddInterestCount())
                && Integer.valueOf(model.getAddInterestCount()) > 0) {// 有加息券
            tvJxqNum.setVisibility(View.VISIBLE);
            tvJxqNum.setText(model.getAddInterestCount() + "张可用");
            tvJxqNum.setSelected(true);
            tvJxqUsed.setText(String.format("+%s%%", model.getAddInterestView()));
            if (!CommonUtil.isNullOrEmpty(model.getAddInterestView())) {//使用加息券
                float totalRate = originalFloatRate + Float.valueOf(model.getAddInterestView().replace("%", ""));
                tvYearRate.setText(String.format("%s%%", String.format("%.2f", totalRate)));
            } else {// 没有使用加息券
                tvYearRate.setText(String.format("%s%%", String.format("%.2f", originalFloatRate)));
                tvJxqUsed.setText("未使用");
            }
        } else {// 没有加息券
            tvJxqNum.setVisibility(View.GONE);
            tvJxqUsed.setText("无可用");
            tvJxqReward.setText("0");
            tvYearRate.setText(model.getYearrate());
        }
        tvJxqReward.setText(model.getAddInterestAmount() + "元");
    }

    /**
     * 处理 红包满减券显示
     *
     * @param model FinanceInvestPBuyCheckData对象
     */
    private void dealWithHbOrMjq(FinanceInvestPBuyCheckData model) {
        if (model.getRewardInfo() != null) {
            // 实际使用金额与红包满减券有关
            tvActuallyUsed.setText(model.getUsemoney());
            _reward_type = model.getRewardInfo().getReward_type();
            _bonus_rate = model.getRewardInfo().getBouns_rate();
            _bonus_prj_term = model.getRewardInfo().getBouns_prj_term();
            int reward_num = model.getRewardInfo().getReward_num();
            RewardCheckUtil.getInstance().setReward_type(model.getRewardInfo().getReward_type());//更新内存中信息
            RewardCheckUtil.getInstance().setBouns_rate(model.getRewardInfo().getBouns_rate());
            RewardCheckUtil.getInstance().setBouns_prj_term(model.getRewardInfo().getBouns_prj_term());
            if (reward_num > 0) {
                tvHbMjNum.setVisibility(View.VISIBLE);
                tvHbMjNum.setText(String.format("%d张可用", reward_num));
                tvHbMjNum.setSelected(true);
                if (_reward_type != null && _reward_type.equals("1")) {
                    String red_amount = model.getRewardInfo().getHongbao().getAmount();
                    double red = Double.valueOf(red_amount);
                    tvHbMjUsed.setText(String.format("-%.2f元", red));
                } else if (_reward_type != null && _reward_type.equals("2")) {
                    String red_amount = model.getRewardInfo().getManjianTicket().getAmount();
                    double red = Double.valueOf(red_amount);
                    tvHbMjUsed.setText(String.format("-%.2f元", red));
                    _mjq_id = model.getRewardInfo().getManjianTicket().getReward_id();
                    RewardCheckUtil.getInstance().setMjq_id(model.getRewardInfo().getManjianTicket().getReward_id());
                } else {
                    tvHbMjUsed.setText("未使用");
                }
            } else {
                tvHbMjUsed.setText("无可用");
            }
        } else {
            tvHbMjUsed.setText("无可用");
        }
    }

    /**
     * @param mContainer
     * @param id                 orderid
     * @param prj_id             项目id
     * @param tv_loan_prjnamestr 借款产品名称
     * @param tv_repay_waystr    回款方式
     * @param tv_cash_moneystr   借款金额
     * @param tv_loan_ratestr    借款利率
     * @param pnameStr           协议名称
     * @param purlstr            协议地址
     */
    public void goAnimCash(View mContainer, String id, String prj_id,
                           String tv_loan_prjnamestr, String tv_repay_waystr,
                           String tv_cash_moneystr, String tv_loan_ratestr, String pnameStr,
                           String purlstr) {

        loan_prjnamestr = tv_loan_prjnamestr;
        cash_moneystr = tv_cash_moneystr;
        loan_ratestr = tv_loan_ratestr;
        orderID = id;
        cashPrjid = prj_id;

        tv_loan_prjname.setText(tv_loan_prjnamestr);
        tv_repay_way.setText(tv_repay_waystr);
        tv_cash_money.setText(tv_cash_moneystr);
        tv_loan_rate.setText(tv_loan_ratestr + "%");
        protocolName = pnameStr;
        purl = purlstr;
        goAnim("", mContainer, 1, prj_id, false);
        // buyPiFCheck(id, mContainer, money,prj_id);
    }

    String loan_prjnamestr;
    String cash_moneystr;
    String loan_ratestr;
    String orderID;
    String cashPrjid;
    int mFlag = -1;
    boolean mIsRepay;

    /**
     * @param investMoney 投资金额
     * @param mContainer  弹框所依赖的View
     * @param prj_id      项目id
     * @param flag        0 理财支付;1 变现;2 提现;3;4;5预约
     */
    public void goAnim(final String investMoney, View mContainer, final int flag, final String prj_id, final boolean isRepay) {
        mFlag = flag;
        if (flag != 2) {
            bt_submit.setEnabled(false);
            bt_submit.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_button_clickable_false));
        }
        final String money;
        if (flag == 2) {
            money = investMoney;
            loadCashoutFeeData(money);
        } else if (flag != 1) {
            if (!investMoney.contains(".")) {
                money = investMoney + ".00";
            } else {
                money = investMoney;
            }
        } else {
            money = "";
        }
        inputPwdLength = 0;
        inputPwd = "";
        _invest_amount = money;
        _prj_id = prj_id;
        mIsRepay = isRepay;
        for (ImageView aPwdIVArray : pwdIVArray) {
            aPwdIVArray.setImageDrawable(null);
        }
        llFinancePayContainer.setVisibility(View.GONE);
        llPayPswWithdrawalsContainer.setVisibility(View.GONE);
        llPayPswCashContainer.setVisibility(View.GONE);
        llAppointPayContainer.setVisibility(View.GONE);
        llProtocolContainer.setVisibility(View.VISIBLE);
        rlPayInfoContainer.setVisibility(View.VISIBLE);
        if (flag == 0) {
            ll_option_btn.setVisibility(View.GONE);
//            ib_hidden.setVisibility(View.INVISIBLE);
            tvInvestMoney.setText(money);
            _invest_amount = money;
            llFinancePayContainer.setVisibility(View.VISIBLE);
            llActuallyUsedContainer.setVisibility(View.VISIBLE);
            if (isRepay) {
                // bt_submit.setText("确认");
                pay_pwd_tv_agree_and.setVisibility(View.VISIBLE);
                pay_pwd_tv_agree_repay.setVisibility(View.VISIBLE);
                pay_pwd_tv_agree_repay.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AgreementActivity.class);
                        intent.putExtra("title", "服务协议");
                        intent.putExtra("url", serverProtocol);
                        context.startActivity(intent);
                    }
                });
            } else {
                pay_pwd_tv_agree_and.setVisibility(View.GONE);
                pay_pwd_tv_agree_repay.setVisibility(View.GONE);
            }
            StringBuffer sbred = new StringBuffer();
            sbred.append("支付成功表示同意并接受");
            String str = sbred.toString();
            sbred.append("《" + protocolName + "》");
            SpannableString ss = new SpannableString(sbred);
            ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_protocol)),
                    str.length(), sbred.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            agree_tv.setText(ss);
            agree_tv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PayPasswordPopupWindow.this.context, AgreementActivity.class);
                    intent.putExtra("title", protocolName);
                    intent.putExtra("url", "/Index/Protocol/view?id=" + protocolId + "&prj_id=" + prj_id);
                    PayPasswordPopupWindow.this.context.startActivity(intent);
                }
            });
            if (CommonUtil.isNullOrEmpty(cfdName)) {
                tv_cfd_protocol.setVisibility(View.GONE);
            } else {
                String tempCfdName = "《" + cfdName + "》";
                SpannableString cfdSs = new SpannableString(tempCfdName);
                cfdSs.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_3)),
                        0, tempCfdName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_cfd_protocol.setText(cfdSs);
                tv_cfd_protocol.setVisibility(View.VISIBLE);
                tv_cfd_protocol.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MainWebActivity.class);
                        intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + cfdProtocolName);
                        context.startActivity(intent);
                    }
                });
            }
            iv_close.setOnClickListener(cancelClickListener);
        } else if (flag == 1) {
            llProtocolContainer.setVisibility(View.VISIBLE);
            rlPayInfoContainer.setVisibility(View.GONE);
            bt_submit.setText("确定");
            tvInvestMoney.setText(money);
            llPayPswCashContainer.setVisibility(View.VISIBLE);
            pay_pwd_tv_agree_and.setVisibility(View.GONE);
            pay_pwd_tv_agree_repay.setVisibility(View.GONE);
            agree_tv.setText(protocolName);
            agree_tv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AgreementActivity.class);
                    intent.putExtra("title", protocolName);
                    if (!TextUtils.isEmpty(purl)) {
                        String url = "/".equals(purl.substring(0, 1)) ? purl.substring(1) : purl;
                        intent.putExtra("url", url);
                    } else {
                        intent.putExtra("url", purl);
                    }
                    context.startActivity(intent);
                }
            });
        } else if (flag == 2) {
            llPayPswWithdrawalsContainer.setVisibility(View.VISIBLE);
            llProtocolContainer.setVisibility(View.GONE);
            rlPayInfoContainer.setVisibility(View.GONE);

            bt_submit.setText("提现");
            // 禁止科学计数法
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(false);
            numberFormat.setMinimumFractionDigits(2);
            withdrawals_tv.setText(makeMoneyString(money));
        } else if (flag == 5) {// 预约
            rlPayInfoContainer.setVisibility(View.GONE);
            bt_submit.setText("确定");
            llAppointPayContainer.setVisibility(View.VISIBLE);
            agree_tv.setText("《预约规则管理》");
            agree_tv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PayPasswordPopupWindow.this.context, AgreementActivity.class);
                    intent.putExtra("title", "预约规则管理");
                    intent.putExtra("url", "Index/Protocol/view?id=37");
                    PayPasswordPopupWindow.this.context.startActivity(intent);
                }
            });
            pay_pwd_tv_agree_repay.setVisibility(View.VISIBLE);
            pay_pwd_tv_agree_repay.setText("《应收账款转让及回购合同》");
            pay_pwd_tv_agree_repay.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PayPasswordPopupWindow.this.context, AgreementActivity.class);
                    intent.putExtra("title", "应收账款转让及回购合同");
                    intent.putExtra("url", "Index/Protocol/view?id=17");
                    PayPasswordPopupWindow.this.context.startActivity(intent);
                }
            });

            // bp_product_type_tv.setText(bp_product_type);
            bp_date_tv.setText(bp_date);
            bp_rate_tv.setText(bp_rate);
            bp_money_tv.setText(bp_money);
            if (bp_iv_check_data != null && bp_iv_check_data.length > 0) {
                if (bp_iv_check_data[0].equals("1")) {
                    iv_xyb.setVisibility(View.VISIBLE);
                } else {
                    iv_xyb.setVisibility(View.GONE);
                }
                if (bp_iv_check_data[1].equals("1")) {
                    iv_qfx.setVisibility(View.VISIBLE);
                } else {
                    iv_qfx.setVisibility(View.GONE);
                }
                if (bp_iv_check_data[2].equals("1")) {
                    iv_sdt.setVisibility(View.VISIBLE);
                } else {
                    iv_sdt.setVisibility(View.GONE);
                }
                if (bp_iv_check_data[3].equals("1")) {
                    iv_rys.setVisibility(View.VISIBLE);
                } else {
                    iv_rys.setVisibility(View.GONE);
                }
            }
        }

        if (DensityUtil.getDecorViewHeight(context) > DensityUtil.getScreenHeight(context)) {
            showAtLocation(mContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
                    -DensityUtil.getNavigationBarHeight(context)); // 设置layout在PopupWindow中显示的位置
        } else {
            showAtLocation(mContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        bt_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    _invest_amount = money;
                    // 携带信息进入验证页面
                    investFlow();
                } else if (flag == 1) {
                    buyPiF(orderID, prj_id, _jxq_id, loan_prjnamestr, loan_ratestr, cash_moneystr, inputPwd);
                } else if (flag == 2) {
                    if (isPwdRight) {
                        loadWithdrawalsData();
                    }
                } else if (flag == 3) {
                } else if (flag == 5) {
                    bespeakInterface.doBespeak(inputPwd, bp_applyflag);
                }
            }
        });
        if (android.os.Build.VERSION.SDK_INT < 12)
            return;
        final ViewPropertyAnimator animate = mContainer.animate();
        animate.setDuration(mDuration);
        animate.alpha(0.7f);
        viewTop.setVisibility(View.INVISIBLE);
        viewBottom.setVisibility(View.INVISIBLE);
        ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(viewBottom, "YFraction", 1, 0);
        movingFragmentRotator.setDuration(400);
        movingFragmentRotator.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                viewBottom.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {

            }

            @Override
            public void onAnimationEnd(Animator arg0) {

            }

            @Override
            public void onAnimationCancel(Animator arg0) {

            }
        });
        movingFragmentRotator.start();
        ObjectAnimator movingFragmentRotators = ObjectAnimator.ofFloat(viewTop, "YFraction", -1, 0);
        movingFragmentRotators.setStartDelay(390);
        movingFragmentRotators.setDuration(400);
        movingFragmentRotators.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                viewTop.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {

            }

            @Override
            public void onAnimationEnd(Animator arg0) {

            }

            @Override
            public void onAnimationCancel(Animator arg0) {

            }
        });
        movingFragmentRotators.start();

        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                animate.alpha(1f);

            }
        });
    }

    /**
     * 申请变现
     *
     * @param prj_order_id 订单号
     * @param prj_id       项目id
     * @param sdt_prj_name sdt_prj_name
     * @param cash_rate    cash_rate
     * @param cash_money   cash_money
     * @param cash_pwd     密码
     */
    public void buyPiF(String prj_order_id, String prj_id, String jxq_id, String sdt_prj_name,
                       String cash_rate, String cash_money, String cash_pwd) {
        bt_submit.setEnabled(false);
        if (mLoadDialog != null && !mLoadDialog.isShowing()) {
            mLoadDialog.show();
        }
        httpPayService.buyPiF(prj_order_id, prj_id, jxq_id, sdt_prj_name, cash_rate, cash_money, cash_pwd);
    }

    public void loadBankData() {
        String BANK_CARD_LIST_URL = "Mobile2/PayAccount/getMyBindBanks";
        //获取银行卡列表
        context.post(BANK_CARD_LIST_URL, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response != null) {
                        if (response.getInt("boolen") == 1) {
                            JSONArray dataArray = response.getJSONArray("data");
                            int size = dataArray.length();
                            bankArray = new BankCardInfo[size];
                            for (int i = 0; i < size; i++) {
                                JSONObject obj = dataArray.getJSONObject(i);
                                BankCardInfo info = new BankCardInfo();
                                info.setId(obj.getString("id"));
                                info.setBank_name(obj.getString("bank_name"));
                                info.setBank(obj.getString("bank"));
                                info.setAcount_name(obj.getString("acount_name"));
                                info.setSub_bank(obj.getString("sub_bank"));
                                if ("1".equals(obj.getString("is_default"))) {
                                    bankSelectedPosition = i;
                                }
                                bankArray[i] = info;
                            }
                            bankCardInfo = bankArray[bankSelectedPosition];
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                context.showToast("网络不给力");
            }
        });
    }

    /**
     * 选择银行卡后刷新界面
     *
     * @param info bankCardInfo
     */
    public void refreshBankCard(BankCardInfo info) {
        bankCardInfo = info;
    }

    /**
     * 加载提现服务费
     *
     * @param money 提现金额
     */
    public void loadCashoutFeeData(String money) {
        httpPayService.loadCashoutFeeData(money);
    }

    private String makeMoneyString(String str) {
        int len = str.length();
        int index = str.lastIndexOf(".");
        if (index == -1) {
            return str + ".00";
        } else if (index == (len - 1)) {
            return str + "00";
        } else if (index == (len - 2)) {
            return str + "0";
        } else {
            return str;
        }
    }

    // w_flag: 0-新增 1-修改 2-普通提现
    private int w_flag;
    private String w_money, w_bank, w_sub_bank, w_out_account_no, w_out_account_id, w_code,
            w_temp_pcode, w_bank_name, w_sub_bank_id, w_is_save_fund;

    /**
     * @param flag           0-新增 1-修改 2-普通提现
     * @param money          金额 单位元
     * @param bank           银行Code
     * @param sub_bank       支行信息
     * @param out_account_no 银行账户
     * @param out_account_id 银行账户id
     * @param code           城市code
     * @param temp_pcode     省会code
     * @param bank_name      银行名称
     * @param sub_bank_id    支行id
     * @param is_save_fund   是否编辑或新增银行卡 0-否 1-是
     */
    public void setWithdrawalsParams(int flag, String money, String bank,
                                     String sub_bank, String out_account_no, String out_account_id,
                                     String code, String temp_pcode, String bank_name,
                                     String sub_bank_id, String is_save_fund) {
        this.w_flag = flag;
        this.w_money = money;
        this.w_bank = bank;
        this.w_sub_bank = sub_bank;
        this.w_out_account_no = out_account_no;
        this.w_out_account_id = out_account_id;
        this.w_code = code;
        this.w_temp_pcode = temp_pcode;
        this.w_bank_name = bank_name;
        this.w_sub_bank_id = sub_bank_id;
        this.w_is_save_fund = is_save_fund;
    }

    /**
     * 提现
     */
    private void loadWithdrawalsData() {
        bt_submit.setEnabled(false);
        if (!mLoadDialog.isShowing())
            mLoadDialog.show();
        httpPayService.loadWithdrawalsData(w_money, w_bank, w_sub_bank, w_out_account_no, inputPwd, w_flag,
                w_out_account_id, w_code, w_temp_pcode, w_bank_name, w_sub_bank_id, w_is_save_fund);
    }

    public void setDismissListener(DismissListener listener) {
        mDismissListener = listener;
    }

    public interface DismissListener {
        void onDismiss();
    }

    /**
     * 手机支付密码验证
     */
    private void loadPayPwdCheck() {
        httpPayService.checkPayPwd(MemorySave.MS.userInfo.uid, inputPwd);
    }

    /**
     * 预约回调接口
     */
    public interface PayPPForBespeakInterface {
        void doBespeak(String pwd, int bp_applyflag);
    }

    private String bp_product_type, bp_date, bp_rate, bp_money;
    private int bp_applyflag;
    private PayPPForBespeakInterface bespeakInterface;
    private String[] bp_iv_check_data;

    /**
     * 设置预约信息
     *
     * @param type             预约产品类型
     * @param date             时间
     * @param rate             年化
     * @param money            金额
     * @param applyflag        applyFlag
     * @param iv_check_data    ivCheckData
     * @param bespeakInterface 预约回调接口
     */
    public void setBespeakData(String type, String date, String rate,
                               String money, int applyflag, String[] iv_check_data,
                               PayPPForBespeakInterface bespeakInterface) {
        this.bp_product_type = type;
        this.bp_date = date;
        this.bp_rate = rate;
        this.bp_money = money;
        this.bespeakInterface = bespeakInterface;
        this.bp_applyflag = applyflag;
        this.bp_iv_check_data = iv_check_data;
    }

    @Override
    public void buyPiSuccess(FinancePayResultJson result) {
        if (result != null) {
            if (result.getBoolen() != null && result.getBoolen().equals("1")) {
                Intent intent = new Intent();
                intent.putExtra("FinancePayResultData", result.getData());
//				intent.setClass(context, InvestSuccessActivity.class);
                intent.setClass(context, InvestSuccessActivity.class);
                intent.putExtra(Constants.Project.IS_AUTO_INVEST_OPEN, isAutoInvestOpen());
                intent.putExtra(Constants.Project.IS_CG, isEcwAccount());
                context.startActivity(intent);
                Map<String, String> map = new HashMap<String, String>();
                map.put("userName", MemorySave.MS.userInfo.uname);
                map.put("money", _invest_amount);
                TCAgent.onEvent(context, Constants.AGENT_PAYBUY, MemorySave.MS.userInfo.uname, map);
                PayPasswordPopupWindow.this.dismiss();
            } else {
                if (result.getBoolen() != null && !StringUtils.isEmpty(result.getMessage())) {
                    ToastUtil.showToast(context, result.getMessage());
                }
            }
        }
        bt_submit.setEnabled(true);
        if (mLoadDialog != null)
            mLoadDialog.dismiss();
    }

    @Override
    public void buyPiFail(String message) {
        if (!StringUtils.isEmpty(message)) {
            ToastUtil.showToast(context, message);
        }
        bt_submit.setEnabled(true);
        if (mLoadDialog != null)
            mLoadDialog.dismiss();
        this.dismiss();
    }

    @Override
    public void buyPiFSuccess(FinanceDoCashJson result) {
        if (result != null) {
            if (result.getBoolen() != null && result.getBoolen().equals("1")) {
                String sdt_prj_name = result.getData().getPrj_name();
                String title = "";
                String subTitle = "";
                try {
                    if (sdt_prj_name.contains("-")) {
                        title = sdt_prj_name.split("-")[0];
                        subTitle = sdt_prj_name.split("-")[1];
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                PayPasswordPopupWindow.this.dismiss();
                mCallBack.doInvestSuccess(true);
                Intent intent = new Intent();
                intent.putExtra("prj_order_id", result.getData().getPrj_order_id());
                intent.putExtra("title", title);
                intent.putExtra("subTitle", subTitle);
                intent.putExtra("msg", result.getData().getMsg());
                intent.setClass(context, CashSuccessActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        }
        bt_submit.setEnabled(true);
        if (mLoadDialog != null)
            mLoadDialog.dismiss();
    }

    @Override
    public void buyPiFFail() {
        bt_submit.setEnabled(true);
        if (mLoadDialog != null)
            mLoadDialog.dismiss();
    }

    @Override
    public void loadCashoutFeeDataSuccess(FinanceWithdrawalsMoneyJson result) {
        if (null != result) {
            if (result.getBoolen() != null && result.getBoolen().equals("1")) {
                tvActualIncomeMoney.setText(makeMoneyString(result.getMoney()));
                tvSxFee.setText(makeMoneyString(result.getCash_fee()));
                tvFwFee.setText(makeMoneyString(result.getFee()));
            }
        }
    }

    @Override
    public void loadCashoutFeeDataFail() {

    }

    @Override
    public void applyCashoutSuccess(FinanceApplyCashoutJson response) {
        if (mLoadDialog != null && mLoadDialog.isShowing())
            mLoadDialog.dismiss();
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                String message = response.getMessage();
                if ("1".equals(boolen)) {
                    context.showToast(message);
                    Intent intent = new Intent(context, WithdrawSuccessActivity.class);
                    intent.putExtra("remark", response.getRemark());
                    intent.putExtra("cashoutId", response.getCashoutId());
                    context.startActivity(intent);
                    // 统计提现 用户，金额
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("userName", MemorySave.MS.userInfo.uname);
                    map.put("money", w_money);
                    TCAgent.onEvent(context, Constants.AGENT_WITHDRAW, MemorySave.MS.userInfo.uname, map);
                    PayPasswordPopupWindow.this.dismiss();
                    mDismissListener.onDismiss();
                    // windowDismiss();
                } else {
                    context.showToast(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bt_submit.setEnabled(true);
        }
    }

    @Override
    public void applyCashoutFail() {
        bt_submit.setEnabled(true);
        if (mLoadDialog.isShowing())
            mLoadDialog.dismiss();
    }

    /**
     * 携带信息进入验证页面
     */
    private void navToInvestSmsConfirm(LocalInvestPayModel localInvestPayModel) {
        Intent intent = new Intent(context, InvestSmsConfirmActivity.class);
        intent.putExtra(InvestSmsConfirmActivity.LOCAL_INVEST_PAY_INFO, localInvestPayModel);
        int collectionInt = isCollection() ? 1 : 0;
        intent.putExtra(Constants.Project.IS_COLLECTION, collectionInt);
        intent.putExtra(Constants.Project.IS_AUTO_INVEST_OPEN, isAutoInvestOpen());
        intent.putExtra(Constants.Project.IS_CG, isEcwAccount());
        context.startActivity(intent);
    }

    /**
     * 获取支付信息实体
     *
     * @return 支付信息实体对象
     */
    private LocalInvestPayModel getLocalInvestPayModel() {
        LocalInvestPayModel localInvestPayModel = new LocalInvestPayModel();
        localInvestPayModel.setInvestAmount(_invest_amount);
        localInvestPayModel.setInvestPrjId(_prj_id);
        localInvestPayModel.setInvestJxqId(_jxq_id);
        localInvestPayModel.setPayPwd(inputPwd);
        localInvestPayModel.setRepay(mIsRepay);
        localInvestPayModel.setXXB(isXXB);
        localInvestPayModel.setInvestUsedRewardType(_reward_type);
        localInvestPayModel.setInvestHbBonusRate(_bonus_rate);
        localInvestPayModel.setInvestHbBonusRateTimeLimit(_bonus_prj_term);
        localInvestPayModel.setInvestHbOrMjqId(_mjq_id);

        return localInvestPayModel;
    }

    @Override
    public void payPswCheckSuccess(FinanceCheckPayPasswordJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    // keyboard_title_tv.setText("密码输入正确");
                    if (mFlag == 0) {
                        investFlow();
                        // 跳转完成后关闭弹出框
                        dismiss();
                    }
                    isPwdRight = true;
                    bt_submit.setEnabled(true);
                    bt_submit.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.feedback_submit_bg_xml));

                } else {
                    context.createDialogDismissAuto(response.getMessage(), R.drawable.icon_pwd_err);
                    while (inputPwdLength > 0) {
                        inputPwdLength--;
                        inputPwd = inputPwd.substring(0, inputPwd.length() - 1);
                        pwdIVArray[inputPwdLength].setImageDrawable(null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void payPswCheckFailed() {
        context.showToast("网络不给力");
    }

    /**
     * 投资逻辑处理：
     * 1.普通用户:
     * --1.1 集合标：集合标投机接口
     * --1.2 非集合标：非集合标投机接口
     * 2.存管用户：
     * --2.1 已开通自动投标：
     * ------2.1.1 集合标：无需认证，集合标投机接口
     * ------2.1.2 非集合标：无需认证，非集合标投机接口
     * --2.2 未开通自动投标：携带是否集合标字段，进入短信确认页面
     */
    private void investFlow() {
        // 携带信息进入验证页面
        if (isEcwAccount()) {
            if (isAutoInvestOpen()) {
                if (isCollection()) {
                    httpPayService.doCollectionPrjInvest(getLocalInvestPayModel());
                } else {
                    httpPayService.doInvest(getLocalInvestPayModel());
                }
            } else {
                navToInvestSmsConfirm(getLocalInvestPayModel());
            }
        } else {
            if (isCollection()) {
                httpPayService.doCollectionPrjInvest(getLocalInvestPayModel());
            } else {
                httpPayService.doInvest(getLocalInvestPayModel());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_foregive_psw:
                Intent intent = new Intent(context, UserMobilePayPwdActivity.class);
                context.startActivity(intent);
                break;
            case R.id.pay_pwd_tv_note0:
            case R.id.pay_pwd_tv_note1:
                Intent intent1 = new Intent(context, MainWebActivity.class);
                intent1.putExtra("title", "出借风险提示及禁止性行为说明书");
                intent1.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/lendProtocol");
                context.startActivity(intent1);
                break;
            case R.id.pay_pwd_tv_note2:
                Intent intent2 = new Intent(context, MainWebActivity.class);
                intent2.putExtra("title", "资金来源合法承诺书");
                intent2.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/capitalProtocol");
                context.startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    public void updateHbMjq() {
        getHBCheckUtilInfo();
        double money = 0.00;
        DecimalFormat df = new DecimalFormat("#,###.00");
        if (!CommonUtil.isNullOrEmpty(_invest_amount)) {
            money = Double.valueOf(_invest_amount.replaceAll(",", ""));
        }
        if (!CommonUtil.isNullOrEmpty(_reward_amount)) {
            double amount = Double.valueOf(_reward_amount);
            if (_reward_type.equals("1") || _reward_type.equals("2")) {
                tvHbMjUsed.setText(String.format("-%.2f元", amount));
                tvActuallyUsed.setText(String.format("%s元", df.format(money - amount)));
            } else {
                tvHbMjUsed.setText("未使用");
                tvActuallyUsed.setText(String.format("%s元", df.format(money)));
            }
        } else {
            tvHbMjUsed.setText("未使用");
            tvActuallyUsed.setText(String.format("%s元", df.format(money)));
        }
    }

    @Override
    public void updateJxq(boolean isUpdateIncome) {
        getHBCheckUtilInfo();
        if (isUpdateIncome) {
            updateJxqIncomeService.updateJxqIncome(_invest_amount, _prj_id, _jxq_id, mIsRepay);
        }
        if (!CommonUtil.isNullOrEmpty(_jx_rate)) {
            tvJxqUsed.setText(String.format("+%s", _jx_rate));
            float totalRate = originalFloatRate + Float.valueOf(_jx_rate.replace("%", ""));
            tvYearRate.setText(String.format("%s%%", String.format("%.2f", totalRate)));
        } else {
            tvYearRate.setText(String.format("%s%%", String.format("%.2f", originalFloatRate)));
            tvJxqUsed.setText("未使用");
        }
    }

    /**
     * 获取奖励检查信息（红包、满减券、加息券）
     */
    private void getHBCheckUtilInfo() {
        _reward_type = RewardCheckUtil.getInstance().getReward_type();
        _bonus_rate = RewardCheckUtil.getInstance().getBouns_rate();
        _bonus_prj_term = RewardCheckUtil.getInstance().getBouns_prj_term();
        _mjq_id = RewardCheckUtil.getInstance().getMjq_id();
        _reward_amount = RewardCheckUtil.getInstance().getAmount();
        _jxq_id = RewardCheckUtil.getInstance().getJxq_id();
        _jx_rate = RewardCheckUtil.getInstance().getJx_rate();
    }

    @Override
    public void updateDateJxqIncomeSuccess(FinanceInvestPBuyCheckJson result) {
        if (null != result && null != result.getData()) {
            FinanceInvestPBuyCheckData data = result.getData();
            dealWithJxq(data);
        }
    }

    @Override
    public void updateDateJxqIncomeFail(String message) {

    }
}