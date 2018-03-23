package com.lcshidai.lc.ui.finance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.http.BaseCallback;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.http.ProJsonHandler;
import com.lcshidai.lc.impl.GainOpenAccountImpl;
import com.lcshidai.lc.impl.PIPWCallBack;
import com.lcshidai.lc.impl.account.AccountHasEscrowedImp;
import com.lcshidai.lc.impl.account.AccountIsZheshangCardImp;
import com.lcshidai.lc.impl.account.AccountSettingImpl;
import com.lcshidai.lc.impl.account.GetEscrowRemindImpl;
import com.lcshidai.lc.impl.account.SetEscrowRemindImpl;
import com.lcshidai.lc.impl.finance.FinanceInvestPBuyCheckImpl;
import com.lcshidai.lc.impl.onKeyInvest.IsOpenAutoInvestImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.OpenAccountData;
import com.lcshidai.lc.model.OpenAccountJson;
import com.lcshidai.lc.model.account.AccountHasEscrowedData;
import com.lcshidai.lc.model.account.AccountHasEscrowedJson;
import com.lcshidai.lc.model.account.AccountIsZheshangCardData;
import com.lcshidai.lc.model.account.AccountIsZheshangCardJson;
import com.lcshidai.lc.model.account.AccountSettingData;
import com.lcshidai.lc.model.account.AccountSettingJson;
import com.lcshidai.lc.model.account.GetEscrowRemindData;
import com.lcshidai.lc.model.account.GetEscrowRemindJson;
import com.lcshidai.lc.model.finance.FinanceInfoData;
import com.lcshidai.lc.model.finance.FinanceInfoJson;
import com.lcshidai.lc.model.finance.FinanceMaxInvestMoneyJson;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckData;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckJson;
import com.lcshidai.lc.model.oneKeyInvest.IsOpenAutoInvestJson;
import com.lcshidai.lc.service.ApiService;
import com.lcshidai.lc.service.HttpGainOpenAccountService;
import com.lcshidai.lc.service.account.HttpAccountSettingService;
import com.lcshidai.lc.service.account.HttpGetEscrowRemindService;
import com.lcshidai.lc.service.account.HttpHasEscrowedService;
import com.lcshidai.lc.service.account.HttpIsZheshangCardService;
import com.lcshidai.lc.service.account.HttpSetEscrowRemindService;
import com.lcshidai.lc.service.finance.HttpFinanceInvestCheckService;
import com.lcshidai.lc.service.oneKeyInvest.IsOpenAutoInvestService;
import com.lcshidai.lc.ui.GestureLoginActivity;
import com.lcshidai.lc.ui.LoginActivity;
import com.lcshidai.lc.ui.MainActivity;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.account.UserPayPwdFirstSetActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.fragment.finance.ChargeDialogFragment;
import com.lcshidai.lc.ui.fragment.finance.FinanceProjectDetailFirstFragment;
import com.lcshidai.lc.ui.fragment.finance.FinanceProjectDetailSecondFragment;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow;
import com.lcshidai.lc.widget.verticalviewpager.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 投资项目详情页面
 */
@SuppressLint("HandlerLeak")
public class FinanceProjectDetailActivity extends TRJActivity implements FinanceInvestPBuyCheckImpl,
        PIPWCallBack, GetEscrowRemindImpl, SetEscrowRemindImpl, AccountSettingImpl,
        AccountHasEscrowedImp, AccountIsZheshangCardImp, GainOpenAccountImpl, IsOpenAutoInvestImpl {
    private TextView mTvTitle;
    private ImageButton mBackBtn;
    private VerticalViewPager viewPager;
    protected View mProgressContainer;

    private LinearLayout llForbiddenContainer;
    private TextView tv_state, tv_msg;
    private ImageView img_project_detail;
    private Button btn_see_other;

    private LinearLayout llDetailContainer;
    // 未登录状态
    private LinearLayout bottom_ll, ll_invest_money;
    private Button btnInvestStatusTips;
    // 正常投资
    private RelativeLayout rlInvestAmountContainer;
    private EditText etInvestAmount;  // 投资金额
    private TextView tvEdit, tvAmountLabel;// 修改按钮，投资金额标签
    private View vDivider;
    private Button btnInvestRightNow;// 投资按钮

    // 新秀表投资
    private RelativeLayout rlXxbContainer;
    private TextView tvXxbInfo;// 展示新秀标信息
    private Button btnXxbInvestRightNow;
    // 余额不足
    private RelativeLayout rlBalanceLessContainer;// 金额不够容器
    private Button btnRechargeRightNow;
    // 待开标
    private LinearLayout llDkbContainer;// 待开标容器
    private TextView tvDkbLeftTime;// 待开标剩余时间

    // 自定义键盘布局
    private LinearLayout keyboard_main;
    private TranslateAnimation showAnim, dismissAnim;// 自定义键盘动画
    private Button btnKeyboard1, btnKeyboard2, btnKeyboard3, btnKeyboard4, btnKeyboard5, btnKeyboard6,
            btnKeyboard7, btnKeyboard8, btnKeyboard9, btnKeyboard0, btnKeyboard00, btnKeyboardPoint;// 自定义键盘子控件
    private TextView btnKeyboardPlusNum, btnKeyboardMinusNum;                                     // 加减按钮
    private View btnKeyboardPlusContainer, btnKeyboardMinusContainer, btnKeyboardDelete, btnKeyboardHide;

    private PayPasswordPopupWindow payPasswordPopupWindow;
    private FinanceInfoData financeInfoData;

    private String mPrjId;
    private String mPrjName;
    private int isCollection;
    private boolean isAutoInvestOpen = false;
    boolean isXXB = false;// 是否為新秀標
    private boolean isLogin = false;
    private boolean isKeyboardShowing = false;
    private boolean isKeyboardDismiss = true;
    private float maxMoney;
    public String limitMoney;
    public String canInvest;
    private MyPageAdapter mAdapter;

    int plusValue = 1000;
    TimeCount mTimeCount;// 针对尚未开始的项目显示倒计时
    //    项目信息
    FinanceProjectDetailFirstFragment mFirstFragment = new FinanceProjectDetailFirstFragment();
    FinanceProjectDetailSecondFragment mSecondFragment = new FinanceProjectDetailSecondFragment();

    private List<TRJFragment> mFragments = new ArrayList<>();
    private HttpAccountSettingService accountSettingService; // 请求是否设置手机支付密码
    private HttpHasEscrowedService mHttpHasEscrowedService = null;
    private HttpIsZheshangCardService httpIsZheshangCardService = null;
    private HttpGainOpenAccountService httpGainOpenAccountService = null;
    private HttpGetEscrowRemindService mHttpGetEscrowRemindService;
    private HttpSetEscrowRemindService mHttpSetEscrowRemindService;
    private IsOpenAutoInvestService isOpenAutoInvestService;

    private HttpFinanceInvestCheckService investCheckService;
    private int mHasEscrowedFlag = 0;// 是否开户
    private int mIsZheshangCardFlag = 0;// 是否浙商卡
    private Dialog mRzDialog;// 实名认证
    private Dialog setPayPswDialog = null;// 设置支付密码
    private Dialog openEcwDialog = null;
    private Dialog remindDialog = null;

    private boolean isInvestFlag = true;// 为解决（投资和充值）公用判断是否开户接口而设置的flag
    private String isSetPayPsw = "1";                // 是否设置支付密码
    private boolean isOldInvestFlow = false;
    private boolean isOldRechargeFlow = false;
    private boolean isEcwAccount = false;
    private String openEscrowUrl;                   // wap开通存管的地址

    private static final String TAG = "FinanceProjectDetailAct";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mHttpHasEscrowedService = new HttpHasEscrowedService(this, this);
        httpIsZheshangCardService = new HttpIsZheshangCardService(this, this);
        httpGainOpenAccountService = new HttpGainOpenAccountService(this, this);
        accountSettingService = new HttpAccountSettingService(this, this);
        mHttpGetEscrowRemindService = new HttpGetEscrowRemindService(this, this);
        mHttpSetEscrowRemindService = new HttpSetEscrowRemindService(this, this);
        isOpenAutoInvestService = new IsOpenAutoInvestService(this, this);
        investCheckService = new HttpFinanceInvestCheckService(this, this);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mPrjId = args.getString(Constants.Project.PROJECT_ID);
            isCollection = args.getInt(Constants.Project.IS_COLLECTION);
            mPrjName = args.getString(Constants.Project.PROJECT_NAME);
            limitMoney = args.getString(Constants.Project.LIMIT_MONEY);
            canInvest = args.getString(Constants.Project.CAN_INVEST);
            mFirstFragment.setArguments(args);
            mSecondFragment.setArguments(args);
        }
        setContentView(R.layout.activity_finance_project_detail);
        initView();

        isLogin();

    }

    /**
     * 判断是否登录
     */
    private void isLogin() {
        ApiService.isLogin(new ProJsonHandler<>(new BaseCallback<BaseJson>() {
            @Override
            protected void onRightData(BaseJson response) {
                Log.e(TAG, "isLogin: 已登录");
                isLogin = true;
                loadIsEscrowAccount();
                loadSettingData();
//                loadIsShowOpenEcwDialog();
                getMaxAvaInvestAmount(mPrjId, isCollection);
                getProjectDetail();
            }

            @Override
            protected void onWrongData(BaseJson response) {
                super.onWrongData(response);
                Log.e(TAG, "onWrongData: 未登录");
                isLogin = false;
                getProjectDetail();
            }
        }, mContext), mContext);

    }

    /**
     * 获取项目详情数据
     */
    private void getProjectDetail() {
        ApiService.getProjectDetail(new ProJsonHandler<>(new BaseCallback<FinanceInfoJson>() {
            @Override
            protected void onRightData(FinanceInfoJson response) {
                FinanceInfoData financeInfoData = response.getData();
                String message = response.getMessage();
                if (null != financeInfoData) {
                    String can_read = financeInfoData.getCan_read();
                    String can_read_msg = financeInfoData.getCan_read_msg();
                    if (can_read != null && can_read.equals("0")) {
                        isProjectDetailVisible(true, message, can_read_msg);
                    } else {
                        isProjectDetailVisible(false, null, null);
                        dealWithPrjDetailData(financeInfoData);
                    }
                }
            }

            @Override
            protected void onWrongData(FinanceInfoJson response) {
                FinanceInfoData financeInfoData = response.getData();
                String message = response.getMessage();
                if (null != financeInfoData) {
                    String can_read = financeInfoData.getCan_read();
                    String can_read_msg = financeInfoData.getCan_read_msg();
                    if (can_read != null && can_read.equals("0")) {
                        isProjectDetailVisible(true, message, can_read_msg);
                    } else {
                        isProjectDetailVisible(false, null, null);
                    }
                }
            }
        }, mContext), mContext, mPrjId, isCollection);
    }

    private void loadIsEscrowAccount() {
        mHttpHasEscrowedService.hasEscrowed();
        showLoadingDialog(mContext, "正在加载...", false);
    }

    private void loadSettingData() {
        accountSettingService.gainAccountSetting("");
        showLoadingDialog(mContext, "正在加载...", false);
    }

    private void loadIsShowOpenEcwDialog() {
        mHttpGetEscrowRemindService.getEscrowRemind();
        showLoadingDialog(mContext, "正在加载...", false);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    public void initView() {
        mAdapter = new MyPageAdapter(getSupportFragmentManager());
        payPasswordPopupWindow = new PayPasswordPopupWindow(this, this);
        payPasswordPopupWindow.isXXB = isXXB;

        initSetPswDialog();
        // bar
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mBackBtn = (ImageButton) findViewById(R.id.ib_top_bar_back);
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // forbidden
        llForbiddenContainer = (LinearLayout) findViewById(R.id.ll_forbidden_container);
        tv_state = (TextView) findViewById(R.id.tv_1);
        img_project_detail = (ImageView) findViewById(R.id.img_project_detail);
        tv_msg = (TextView) findViewById(R.id.tv_2);
        btn_see_other = (Button) findViewById(R.id.btn_see_other);
        btn_see_other.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llDetailContainer = (LinearLayout) findViewById(R.id.finance_detail);
        llDetailContainer.setVisibility(View.INVISIBLE);
        viewPager = (VerticalViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(mAdapter);
        mFirstFragment.setVerticalViewPager(viewPager);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        break;
                    case 1:
                        mSecondFragment.mHandler.sendEmptyMessage(0);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        viewPager.setOffscreenPageLimit(1);

        btnInvestStatusTips = (Button) findViewById(R.id.btn_invest_status_tips);

        rlInvestAmountContainer = (RelativeLayout) findViewById(R.id.rl_invest_amount_container);
        tvAmountLabel = (TextView) findViewById(R.id.tv_account_label);
        vDivider = findViewById(R.id.v_divider);
        etInvestAmount = (EditText) findViewById(R.id.et_invest_amount);
        tvEdit = (TextView) findViewById(R.id.tv_edit);
        tvEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isKeyboardShowing && isKeyboardDismiss) {
                    isKeyboardShowing = true;
                    bottom_ll.startAnimation(showAnim);
                    isShowAmountLabel(false);
                }
            }
        });
        isShowAmountLabel(true);
        btnInvestRightNow = (Button) findViewById(R.id.btn_invest_right_now);

        rlBalanceLessContainer = (RelativeLayout) findViewById(R.id.rl_balance_less_container);
        btnRechargeRightNow = (Button) findViewById(R.id.btn_recharge_right_now);

        rlXxbContainer = (RelativeLayout) findViewById(R.id.rl_xxb_container);
        btnXxbInvestRightNow = (Button) findViewById(R.id.btn_xxb_invest_right_now);
        tvXxbInfo = (TextView) findViewById(R.id.tv_xxb_info);

        llDkbContainer = (LinearLayout) findViewById(R.id.ll_dkb_container);
        tvDkbLeftTime = (TextView) findViewById(R.id.tv_dkb_left_time);

        if (limitMoney != null && !limitMoney.equals("")) {
            etInvestAmount.setText(limitMoney);
            etInvestAmount.setEnabled(false);
            rlInvestAmountContainer.setVisibility(View.INVISIBLE);
            etInvestAmount.setOnFocusChangeListener(null);
        } else {
            etInvestAmount.clearFocus();
            etInvestAmount.setInputType(InputType.TYPE_NULL);
            etInvestAmount.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (!isKeyboardShowing && isKeyboardDismiss) {
                            isKeyboardShowing = true;
                            bottom_ll.startAnimation(showAnim);
                            isShowAmountLabel(false);
                        }
                    }
                    return false;
                }
            });
        }

        keyboard_main = (LinearLayout) findViewById(R.id.keyboard_main);
        ll_invest_money = (LinearLayout) findViewById(R.id.ll_invest_money);
        bottom_ll = (LinearLayout) findViewById(R.id.ll_bottom);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        keyboard_main.measure(0, h);
        final int height = keyboard_main.getMeasuredHeight();
        if (canInvest != null && canInvest.equals("1")) {
            bottom_ll.setVisibility(View.GONE);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bottom_ll.getLayoutParams();
            lp.setMargins(0, 0, 0, -height);
            bottom_ll.setLayoutParams(lp);
            bottom_ll.requestLayout();
            bottom_ll.measure(0, h);
            bottom_ll.setVisibility(View.VISIBLE);
        }
        ll_invest_money.measure(0, h);

        showAnim = new TranslateAnimation(0, 0, 0, -height);
        showAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // gray_view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bottom_ll.getLayoutParams();
                lp.setMargins(0, 0, 0, 0);
                bottom_ll.setLayoutParams(lp);
                bottom_ll.clearAnimation();
                isKeyboardDismiss = false;
            }
        });
        showAnim.setDuration(500);

        dismissAnim = new TranslateAnimation(0, 0, 0, height);
        dismissAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isKeyboardDismiss = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bottom_ll.getLayoutParams();
                lp.setMargins(0, 0, 0, -height);
                bottom_ll.setLayoutParams(lp);
                bottom_ll.clearAnimation();
                isKeyboardShowing = false;
            }
        });
        dismissAnim.setDuration(500);

        initKeyboard();
        flag = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFragments != null) {
            mFragments.clear();
            if (mFirstFragment != null) {
                mFragments.add(mFirstFragment);
            }
            if (mSecondFragment != null) {
                mFragments.add(mSecondFragment);
            }
        }
        mAdapter.notifyDataSetChanged();
        viewPager.setAdapter(mAdapter);
        // todo 待优化
        isOpenAutoInvestService.isOpenAutoInvest();
    }

    /**
     * 是否显示投资金额label
     *
     * @param show show
     */
    private void isShowAmountLabel(boolean show) {
        if (show) {
            tvAmountLabel.setVisibility(View.VISIBLE);
            vDivider.setVisibility(View.VISIBLE);
            tvEdit.setVisibility(View.VISIBLE);
        } else {
            tvAmountLabel.setVisibility(View.GONE);
            vDivider.setVisibility(View.GONE);
            tvEdit.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化键盘
     */
    private void initKeyboard() {
        btnKeyboard1 = (Button) findViewById(R.id.b1);
        btnKeyboard2 = (Button) findViewById(R.id.b2);
        btnKeyboard3 = (Button) findViewById(R.id.b3);
        btnKeyboard4 = (Button) findViewById(R.id.b4);
        btnKeyboard5 = (Button) findViewById(R.id.b5);
        btnKeyboard6 = (Button) findViewById(R.id.b6);
        btnKeyboard7 = (Button) findViewById(R.id.b7);
        btnKeyboard8 = (Button) findViewById(R.id.b8);
        btnKeyboard9 = (Button) findViewById(R.id.b9);
        btnKeyboard0 = (Button) findViewById(R.id.b0);
        btnKeyboard00 = (Button) findViewById(R.id.b00);
        btnKeyboardPoint = (Button) findViewById(R.id.bpoint);
        btnKeyboardPlusContainer = findViewById(R.id.view_plus);
        btnKeyboardPlusNum = (TextView) findViewById(R.id.bplus);
        btnKeyboardMinusContainer = findViewById(R.id.view_minus);
        btnKeyboardMinusNum = (TextView) findViewById(R.id.bminus);
        btnKeyboardDelete = findViewById(R.id.bx);
        btnKeyboardHide = findViewById(R.id.bhide);

        btnKeyboard1.setOnClickListener(new KeyboardClickListener(1));
        btnKeyboard2.setOnClickListener(new KeyboardClickListener(2));
        btnKeyboard3.setOnClickListener(new KeyboardClickListener(3));
        btnKeyboard4.setOnClickListener(new KeyboardClickListener(4));
        btnKeyboard5.setOnClickListener(new KeyboardClickListener(5));
        btnKeyboard6.setOnClickListener(new KeyboardClickListener(6));
        btnKeyboard7.setOnClickListener(new KeyboardClickListener(7));
        btnKeyboard8.setOnClickListener(new KeyboardClickListener(8));
        btnKeyboard9.setOnClickListener(new KeyboardClickListener(9));
        btnKeyboard0.setOnClickListener(new KeyboardClickListener(0));
        btnKeyboard00.setOnClickListener(new KeyboardClickListener(10));
        btnKeyboardPoint.setOnClickListener(new KeyboardClickListener(111));
        btnKeyboardPlusContainer.setOnClickListener(new KeyboardClickListener(11));
        btnKeyboardMinusContainer.setOnClickListener(new KeyboardClickListener(12));
        btnKeyboardDelete.setOnClickListener(new KeyboardClickListener(13));
        btnKeyboardHide.setOnClickListener(new KeyboardClickListener(14));
    }

    /**
     * 投资流程
     */
    private void investFlow() {
        /**
         * 立即投资判断是否开户：
         *      已开户，正常流程；
         *      未开户，提醒去开户；
         */
        isOldInvestFlow = false;
        payPasswordPopupWindow.setEcwAccount(true);
        if (GoLoginUtil.getCunGuanFlag(FinanceProjectDetailActivity.this) == 1) {// 已开通，已实名认证
            // 判断是否设置支付密码
            investPayPswFlow();
        } else if (GoLoginUtil.getCunGuanFlag(FinanceProjectDetailActivity.this) == 0) { // 未设置，说明登录之后没有请求是否开通存管账户接口
            mHttpHasEscrowedService.hasEscrowed();
            showLoadingDialog(mContext, "数据加载中", false);
        } else if (GoLoginUtil.getCunGuanFlag(FinanceProjectDetailActivity.this) == 2) {// 未开通，去开通
//            showOpenEcwDialog(openEscrowUrl);
        }
    }

    /**
     * 旧的投资流程，走旧的流程时肯定已经提示过用户去开户了
     */
    private void oldInvestFlow() {
        isOldInvestFlow = true;
        payPasswordPopupWindow.setEcwAccount(false);
        if ("1".equals(MemorySave.MS.userInfo.is_id_auth)) {// 已实名认证
            // 判断是否设置支付密码
            investPayPswFlow();
        } else {// 未实名认证
            accountSettingService.gainAccountSetting("");
        }
    }

    /**
     * 投资支付密码验证流程
     */
    private void investPayPswFlow() {
        if (GoLoginUtil.getPayPswSetFlag(mContext) == 1) {
            doInvestCheck();
        } else if (GoLoginUtil.getPayPswSetFlag(mContext) == 0) {
            setPayPswDialog.show();
        } else if (GoLoginUtil.getPayPswSetFlag(mContext) == -1) {
            accountSettingService.gainAccountSetting("");
            showLoadingDialog(mContext, "正在加载...", false);
        }
    }

    /**
     * 投资检查操作（检查完成后弹出密码框）
     */
    private void doInvestCheck() {
        float investAmountFloat;
        String investAmountStr = etInvestAmount.getText().toString();
        try {
            investAmountFloat = Float.parseFloat(investAmountStr);
        } catch (Exception e) {
            investAmountFloat = 0;
        }
        if (maxMoney != 0)
            if (investAmountFloat > maxMoney) {
                //弹出充值框
                ChargeDialogFragment dialogFragment = new ChargeDialogFragment();
                dialogFragment.setOnActionBtnClickListener(new ChargeDialogFragment.OnActionBtnClickListener() {
                    @Override
                    public void onLick(String content) {
                        isInvestFlag = false;
                        // 充值流程
                        if (isEcwAccount) {// 是存管账户
                            rechargeFlow();
                        } else {// 不是存管账户
                            oldRechargeFlow();
                        }
                    }
                });
                dialogFragment.show(getSupportFragmentManager(), "charge");
                return;
            }
        if (null != financeInfoData) {
            if (CommonUtil.isNullOrEmpty(financeInfoData.getIs_pre_sale())) {
                financeInfoData.setIs_pre_sale("0");
            }
            boolean isRepay = financeInfoData.getIs_pre_sale().equals("1") && financeInfoData.getBid_status().equals("1");
            doInvestCheckInner(investAmountFloat, financeInfoData.getPrj_id(), "", isRepay, financeInfoData.getYear_rate() + "%");
        }
    }

    /**
     * 真正的投资检查内部处理函数
     *
     * @param investMoney 投资金额
     * @param investPrjId 投资项目id
     * @param jxqId       加息券ID
     * @param isRepay     是否回款
     * @param yearRate    年华收益率
     */
    private void doInvestCheckInner(float investMoney, String investPrjId, String jxqId,
                                    boolean isRepay, String yearRate) {
        showLoadingDialog(mContext, "数据加载中", true);
        btnInvestRightNow.setEnabled(false);
        if (isCollection == 0) {
            investCheckService.doInvestCheck(investMoney, investPrjId, jxqId, isRepay, yearRate);
        } else {
            if (isAutoInvestOpen) {
                investCheckService.doCollectionInvestCheck(investMoney, investPrjId, jxqId, isRepay, yearRate);
            } else {
                btnInvestRightNow.setEnabled(true);
                Intent intent;
                String loadUrl;
                //  去开通
                loadUrl = LCHttpClient.BASE_WAP_HEAD + "/#/openAutoBiding?phase=one ";
                intent = new Intent(mContext, MainWebActivity.class);
                intent.putExtra("web_url", loadUrl);
                intent.putExtra("title", "自动投标授权");
                startActivity(intent);
            }
        }
    }

    /**
     * 充值流程
     */
    private void rechargeFlow() {
        /**
         *  立即充值判断是否后开户:
         *      已开户
         *      未开户
         *  立即充值判断是否浙商卡
         *      是浙商卡
         *      非浙商卡
         */
        isOldRechargeFlow = false;
        if (GoLoginUtil.getCunGuanFlag(FinanceProjectDetailActivity.this) == 1) {// 已开通，一定实名认证了
            rechargePayPswFlow();
        } else if (GoLoginUtil.getCunGuanFlag(FinanceProjectDetailActivity.this) == 0) { // 未设置，说明登录之后没有请求是否开通存管账户接口
            mHttpHasEscrowedService.hasEscrowed();
            showLoadingDialog(mContext, "数据加载中", false);
        } else if (GoLoginUtil.getCunGuanFlag(FinanceProjectDetailActivity.this) == 2) {// 未开通，去开通
//            showOpenEcwDialog(openEscrowUrl);
        }
    }

    /**
     * 旧的充值流程
     */
    private void oldRechargeFlow() {
        isOldRechargeFlow = true;
        rechargePayPswFlow();
    }

    /**
     * 充值支付密码验证流程
     */
    private void rechargePayPswFlow() {
        // 判断是否设置支付密码
        if (GoLoginUtil.getPayPswSetFlag(mContext) == 1) {// 设置了支付密码
            if (isOldRechargeFlow) {// 旧的充值流程，无需考虑是否是浙商卡
                doRecharge(true);
            } else {// 新的充值流程，要考虑是否浙商卡
                if (GoLoginUtil.getZsCardFlag(FinanceProjectDetailActivity.this) == 1) {// 是浙商卡
                    ToastUtil.showToast(this, "浙商卡用户无需充值，卡内资金可直接投资，请保证卡内资金充足");
                } else if (GoLoginUtil.getZsCardFlag(FinanceProjectDetailActivity.this) == 0) {
                    // 是否浙商卡Flag未设置，说明登录之后未请求是否浙商卡接口
                    httpIsZheshangCardService.isZheshangCard();
                } else if (GoLoginUtil.getZsCardFlag(FinanceProjectDetailActivity.this) == 2) {
                    // 非浙商卡
                    doRecharge(false);
                }
            }
        } else if (GoLoginUtil.getPayPswSetFlag(mContext) == 0) { // 没有设置支付密码
            // 弹框处理
            setPayPswDialog.show();
        } else if (GoLoginUtil.getPayPswSetFlag(mContext) == -1) {
            accountSettingService.gainAccountSetting("");
            showLoadingDialog(mContext, "正在加载...", false);
        }
    }

    /**
     * 真正的充值
     *
     * @param isOld 是否是旧的充值
     */
    private void doRecharge(boolean isOld) {
        if (isOld) {
            Intent new_recharge_intent = new Intent();
            new_recharge_intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "#/addBank");// 跳转到旧的充值地址
            new_recharge_intent.putExtra("title", "充值");
            new_recharge_intent.putExtra("goBack", true);
            new_recharge_intent.setClass(mContext, MainWebActivity.class);
            startActivity(new_recharge_intent);
        } else {
            Intent new_recharge_intent = new Intent();
            new_recharge_intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "#/submitPay");
            new_recharge_intent.putExtra("title", "充值");
            new_recharge_intent.setClass(mContext, MainWebActivity.class);
            startActivity(new_recharge_intent);
        }
    }

    @Override
    public void gainHasEscrowedSuccess(AccountHasEscrowedJson response) {
        if (response != null) {
            hideLoadingDialog();
            if (response.getBoolen().equals("1")) {
                AccountHasEscrowedData data = response.getData();
                mHasEscrowedFlag = data.getFlag();
                GoLoginUtil.saveCunGuanFlag(mHasEscrowedFlag, FinanceProjectDetailActivity.this);
                if (data.getFlag() == 0) {// 未开通
                    isEcwAccount = false;
                    payPasswordPopupWindow.setEcwAccount(false);
                } else {// 已开通
                    payPasswordPopupWindow.setEcwAccount(true);
                    isEcwAccount = true;
                    if (isInvestFlag) {// 投资
                        // 走投资流程
                        investFlow();
                    } else {// 充值
                        // 走充值流程
                        rechargeFlow();
                    }
                }
            }
        }
    }

    @Override
    public void gainHasEscrowedFail() {
        hideLoadingDialog();
    }

    @Override
    public void gainIsZheshangCardSuccess(AccountIsZheshangCardJson response) {
        // 是浙商卡
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                AccountIsZheshangCardData data = response.getData();
                mIsZheshangCardFlag = data.getFlag();
                if (mIsZheshangCardFlag == 1) {// 浙商卡
                    GoLoginUtil.saveZheShangCardFlag(1, FinanceProjectDetailActivity.this);
                } else {// 他行卡
                    GoLoginUtil.saveZheShangCardFlag(2, FinanceProjectDetailActivity.this);
                }
                rechargeFlow();
            }
        }
    }

    @Override
    public void gainIsZheshangCardFail() {
    }

    /**
     * 显示开通存管账户对话框
     */
    private void showOpenEcwDialog(String openEscrowUrl) {
        if (openEcwDialog == null) {
            initOpenEcwDialog(openEscrowUrl);
            openEcwDialog.show();
        } else {
            openEcwDialog.show();
        }
    }

    /**
     * 设置手机支付密码弹框
     */
    private void initSetPswDialog() {
        if (setPayPswDialog == null) {
            setPayPswDialog = createDialog("您未设置手机支付密码", "设置", "取消", new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (setPayPswDialog.isShowing()) {
                                setPayPswDialog.dismiss();
                            }
//                            手机支付密码设置
                            Intent intent = new Intent(mContext, UserPayPwdFirstSetActivity.class);
                            intent.putExtra("from_activity", 1);// 显示返回按钮
                            intent.putExtra("intent_from_withdrawals", 1);
                            startActivityForResult(intent, 2);
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

    /**
     * 初始化开户提醒dialog
     */
    private void initOpenEcwDialog(final String openEscrowUrl) {
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
        tvGettingToKnowMore.setVisibility(View.VISIBLE);// 暂时隐藏
        tvGettingToKnowMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // 替换成了解更多url
                intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "#/process");
                intent.putExtra("title", "了解更多");
                intent.setClass(mContext, MainWebActivity.class);
                startActivity(intent);
            }
        });

        tvNegative.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openEcwDialog.dismiss();
                if (remindDialog == null) {
                    initRemindDialog();
                    remindDialog.show();
                } else {
                    remindDialog.show();
                }

            }
        });
        tvPositive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.isNullOrEmpty(openEscrowUrl)) {
                    httpGainOpenAccountService.getOpenAccountInfo();
                } else {
                    Intent intent = new Intent(mContext, MainWebActivity.class);
                    intent.putExtra("web_url", openEscrowUrl);
                    intent.putExtra("need_header", 0);
                    intent.putExtra("title", "开通存管");
                    startActivity(intent);
                    if (openEcwDialog != null && openEcwDialog.isShowing()) {
                        openEcwDialog.dismiss();
                        openEcwDialog = null;
                    }
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

    /**
     * 初始化暂不开户提醒dialog
     */
    private void initRemindDialog() {
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

//        WindowManager windowManager = getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = remindDialog.getWindow().getAttributes();
//        lp.width = (int) (display.getWidth() * 0.9); //设置宽度
//        remindDialog.getWindow().setAttributes(lp);
    }

    @Override
    public void getOpenAccountInfoSuccess(OpenAccountJson response) {
        if (response != null)
            Log.i(tag, response.getMessage());

        if (response != null) {
            if (response.getBoolen().equals("1")) {
                // 获取存管开户信息成功
                OpenAccountData openAccountData = response.getData();
                if (openAccountData != null) {
                    CommonUtil.openAccount(FinanceProjectDetailActivity.this, openAccountData);
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
            hideLoadingDialog();
            if (response.getBoolen().equals("1")) {
                GetEscrowRemindData data = response.getData();
                openEscrowUrl = data.getOpenEscrowUrl();
                if (data != null) {
                    if (data.getFlag() == 1
                            && data.getIs_allow_escrow() == 1) {//未显示过开户弹框
//                        showOpenEcwDialog(openEscrowUrl);
                    }
                }
            }
        }
    }

    @Override
    public void getEscrowRemindFail() {
        hideLoadingDialog();
    }

    @Override
    public void setEscrowRemindSuccess(BaseJson response) {

    }

    @Override
    public void setEscrowRemindFail() {

    }

    @Override
    public void getIsOpenAutoInvestSuccess(IsOpenAutoInvestJson response) {
        if (null != response) {
            if (response.getBoolen().equals("1")) {
                if (response.getData() != null) {
                    isAutoInvestOpen = response.getData().getIs_auto_bid() == 1;
                }
            }
        }
    }

    @Override
    public void getIsOpenAutoInvestFailed() {

    }

    class KeyboardClickListener implements OnClickListener {
        int value;

        KeyboardClickListener(int flag) {
            this.value = flag;
        }

        @Override
        public void onClick(View v) {
            String str = etInvestAmount.getText().toString();
            try {
                float va = Float.parseFloat(str.equals("") ? "0" : str);
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
                        if (str.length() < 9) {
                            if (va == 0) {
                                str = value + "";
                            } else {
                                str = str + value;
                            }
                        }
                        break;
                    case 10:
                        if (va < 1) {
                            return;
                        }
                        if (str.length() < 8)
                            str = str + "00";
                        break;
                    case 11:
                        String s1;
                        int s;
                        if (str.contains(".")) {
                            s = Integer.parseInt(str.substring(0, str.indexOf(".")));
                            s1 = str.substring(str.indexOf("."), str.length());
                            str = (s + plusValue) + s1;
                        } else {
                            va += plusValue;
                            str = va + "";
                        }
                        break;
                    case 12:
                        if (va < plusValue) {
                            str = va + "";
                        } else {
                            // str = va - plusValue + "";
                            String s2;
                            int h;
                            if (str.contains(".")) {
                                h = Integer.parseInt(str.substring(0, str.indexOf(".")));
                                s2 = str.substring(str.indexOf("."), str.length());
                                str = (h - plusValue) + s2;
                            } else {
                                str = (va - plusValue) + "";
                            }
                        }
                        break;
                    case 13:
                        if (str.length() > 1) {
                            str = str.substring(0, str.length() - 1);
                        } else {
                            str = "0";
                        }
                        break;
                    case 111:
                        if (str.indexOf(".") == -1) {
                            str += ".";
                        }
                        break;
                    case 14:
                        if (isKeyboardShowing && !isKeyboardDismiss) {
                            bottom_ll.startAnimation(dismissAnim);
                            isShowAmountLabel(true);
                        }
                        break;
                    default:
                        str = "0";
                        break;
                }
            } catch (Exception e) {
                str = "0";
            }
            etInvestAmount.setText(str);
        }

    }

    /**
     * 新秀标处理
     *
     * @param financeInfoData financeInfoData
     */
    private void dealWithXxb(final FinanceInfoData financeInfoData) {
        rlXxbContainer.setVisibility(View.VISIBLE);
        tvXxbInfo.setText("理财金额:  " + financeInfoData.getPractice_money() + "元");
        btnXxbInvestRightNow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                float va = Float.parseFloat(etInvestAmount.getText().toString().equals("") ? "0"
                        : etInvestAmount.getText().toString());
                payPasswordPopupWindow.goAnim(va + "", findViewById(R.id.main), 0,
                        financeInfoData.getPrj_id(), false);
            }
        });
    }

    /**
     * 待开标处理
     * bid_status = "1"
     *
     * @param financeInfoData financeInfoData
     */
    private void dealWithPreSale(FinanceInfoData financeInfoData) {
        rlInvestAmountContainer.setVisibility(View.GONE);
        llDkbContainer.setVisibility(View.VISIBLE);
        Long remaning_time = Long.parseLong(financeInfoData.getStart_bid_time_diff());
        if (remaning_time > 0) {
            if (mTimeCount != null) {
                mTimeCount.cancel();
            }
            mTimeCount = new TimeCount(Math.abs(remaning_time) * 1000 + 1000, 1000,
                    financeInfoData.bid_status);
            mTimeCount.start();
        } else if (remaning_time == 0) {
            financeInfoData.bid_status = "2";
            dealWithOnSale(financeInfoData);
        }
    }

    /**
     * bid_status = "2"
     *
     * @param financeInfoData financeInfoData
     */
    private void dealWithOnSale(FinanceInfoData financeInfoData) {
        llDkbContainer.setVisibility(View.GONE);
        rlInvestAmountContainer.setVisibility(View.VISIBLE);
        if (isCollection == 1) {
            btnInvestRightNow.setText("一键投资");
        } else {
            btnInvestRightNow.setText("立即投资");
        }
        btnInvestRightNow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 投资流程
                isInvestFlag = true;
                if (isEcwAccount) {// 是存管账户
                    investFlow();
                } else {// 不是存管账户
                    oldInvestFlow();
                }
            }
        });
    }

    /**
     * 处理余额不足
     * is_balance_less = "1"
     *
     * @param financeInfoData financeInfoData
     */
    private void dealWithBalanceLess(FinanceInfoData financeInfoData) {
        rlBalanceLessContainer.setVisibility(View.VISIBLE);
        btnRechargeRightNow.setText("立即充值");
        btnRechargeRightNow.setBackgroundColor(getResources().getColor(R.color.color_finance_child_yellow));
        btnRechargeRightNow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isInvestFlag = false;
                // 充值流程
                if (isEcwAccount) {// 是存管账户
                    rechargeFlow();
                } else {// 不是存管账户
                    oldRechargeFlow();
                }

            }
        });
    }

    /**
     * 处理项目详情数据
     *
     * @param pi 项目详情接口对应data对象
     */
    public void dealWithPrjDetailData(final FinanceInfoData pi) {
        // 初始化视图状态，全部隐藏
        btnInvestStatusTips.setVisibility(View.GONE);
        rlBalanceLessContainer.setVisibility(View.GONE);
        llDkbContainer.setVisibility(View.GONE);
        rlInvestAmountContainer.setVisibility(View.GONE);
        rlXxbContainer.setVisibility(View.GONE);

        financeInfoData = pi;
        String display = pi.getPrj_type_display();
        String name = pi.getPrj_name();
        String title;
        if (!CommonUtil.isNullOrEmpty(pi.getPrj_name_show())) {
            mTvTitle.setText(pi.getPrj_name_show());
        } else {
            if (StringUtils.isEmpty(display)) {
                title = "";
            } else {
                title = display + "-";
            }
            mTvTitle.setText(String.format("%s%s", title, name));
        }
        if (!isLogin) {
            Log.e(TAG, "dealWithPrjDetailData: 未登录");
            btnInvestStatusTips.setVisibility(View.VISIBLE);
            btnInvestStatusTips.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int gestureTimes = SpUtils.getInt(SpUtils.Table.CONFIG, SpUtils.Config.TOTAL_TRY_TIMES, 5);
                    Intent intent = new Intent();
                    if (GoLoginUtil.isShowGestureLogin(mContext) && gestureTimes > 0) {
                        // 如果手势登陆开关打开且手势登陆密码输入次数小于5，则进入手势登陆页面
                        intent.setClass(mContext, GestureLoginActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
                        intent = new Intent(mContext, LoginActivity.class);
                        startActivityForResult(intent, 10000);
                    }
                }
            });
            return;
        }

        // 是否余额不足
        if (pi.is_balance_less.equals("1")) {
            dealWithBalanceLess(financeInfoData);
            return;
        }

        // 处理新秀标
        if (isXXB) {
            dealWithXxb(financeInfoData);
            return;
        }

        btnKeyboardMinusNum.setText(R.string.number_1000);
        btnKeyboardPlusNum.setText(R.string.number_1000);

        if (!CommonUtil.isNullOrEmpty(pi.bid_status) && pi.bid_status.equals("1")) {
            Log.e(TAG, "dealWithPrjDetailData: dealWithPreSale");
            dealWithPreSale(financeInfoData);
            return;
        } else if (pi.bid_status.equals("2")) {// #FFA200
            Log.e(TAG, "dealWithPrjDetailData: dealWithOnSale");
            dealWithOnSale(pi);
        } else {
            Log.e(TAG, "dealWithPrjDetailData: 其他");
            btnInvestStatusTips.setVisibility(View.VISIBLE);
//            btnInvestStatusTips.setBackgroundResource(R.color.color_6);
            btnInvestStatusTips.setText(pi.bid_status_display);
            btnInvestStatusTips.setEnabled(false);
        }
    }

    /**
     * 获取最大可投资金额
     *
     * @param id           项目id
     * @param isCollection 是否集合标
     */
    public void getMaxAvaInvestAmount(String id, int isCollection) {
        if (limitMoney != null && !limitMoney.equals("")) {
            return;
        }
        showLoadingDialog(mContext, "数据加载中", true);
        investCheckService.getMaxAvaInvestAmount(id, isCollection);
    }

    @Override
    protected void onDestroy() {
        hideLoadingDialog();
        MemorySave.MS.mAlarmLoginFlag = false;
        if (mTimeCount != null) {
            mTimeCount.isFinish = true;
            mTimeCount.cancel();
        }
        cancelAllRequest();
        super.onDestroy();
    }

    @Override
    public void getMaxAvaInvestAmountSuccess(FinanceMaxInvestMoneyJson result) {
        if (result != null) {
            if (result.getBoolen() != null && result.getBoolen().equals("1")) {
                if (result.getData() != null && StringUtils.isFloat(result.getData())) {
                    maxMoney = Float.parseFloat(result.getData());
                    if (etInvestAmount != null) {
                        etInvestAmount.setText(CommonUtil.customFormat("0.00", maxMoney));
                    }
                }
            } else {
                if (result.getMessage() != null) {
                    if (result.getMessage().contains("余额不足")) {
                        etInvestAmount.setText("余额不足");
//                        etInvestAmount.setText(CommonUtil.customFormat("0.00", 0));
                        tvAmountLabel.setVisibility(View.GONE);
                        vDivider.setVisibility(View.GONE);
                        tvEdit.setVisibility(View.GONE);
                        etInvestAmount.setEnabled(false);
                        etInvestAmount.setTextColor(getResources().getColor(R.color.color_finance_child_yellow));
                    } else {
//                        ToastUtil.showToast(this, result.getMessage());
                    }
                }
            }
        }
        hideLoadingDialog();

    }

    @Override
    public void getMaxAvaInvestAmountFail() {
        hideLoadingDialog();
    }

    @Override
    public void doInvestCheckSuccess(FinanceInvestPBuyCheckJson result, float va, String id,
                                     boolean isRepay, String yearrate) {
        hideLoadingDialog();
        if (result != null) {
            if (result.getBoolen() != null && result.getBoolen().equals("1") && result.getData() != null) {
                result.getData().setMoney(va + "");
                result.getData().setRepay(isRepay);
                result.getData().setPrjid(id);
                result.getData().setYearrate(yearrate);
                if (isCollection == 1) {
                    payPasswordPopupWindow.setCollection(true);
                } else {
                    payPasswordPopupWindow.setCollection(false);
                }
                payPasswordPopupWindow.setAutoInvestOpen(isAutoInvestOpen);
                if (isKeyboardShowing && !isKeyboardDismiss) {
                    bottom_ll.startAnimation(dismissAnim);
                }
                if (result.getData().isRepay()) {
                    payPasswordPopupWindow.serverProtocol = result.getData().getServer_protocol();
                }
                if (result.getData().getIs_tips().equals("1")) {
                    dialogPopupWindow.showWithMessage(result.getData().getTips_error(), "7");
                } else {
                    payPasswordPopupWindow.goAnimPlusIncome(this.findViewById(R.id.main), result.getData());
                }
            } else {
                if (result.getMessage() != null && result.getMessage().contains("支付密码")) {
                    Intent intent = new Intent(mContext, UserPayPwdFirstSetActivity.class);
                    startActivity(intent);
                } else {
                    if (result.getMessage() != null && result.getMessage().contains("余额不足"))
                        dialogPopupWindow.showWithMessage(result.getMessage(), 0 + "");
                }
            }
        }
        btnInvestRightNow.setEnabled(true);
    }

    @Override
    public void doInvestCheckFail(String response) {
        btnInvestRightNow.setEnabled(true);
        hideLoadingDialog();
        if (!CommonUtil.isNullOrEmpty(response)) {
            ToastUtil.showToast(this, response);
        }
    }

    @Override
    public void doInvestCheckSuccess(boolean success, FinanceInvestPBuyCheckData model) {
        if (success) {
            // mymodel = model;
            if (isKeyboardShowing && !isKeyboardDismiss) {
                bottom_ll.startAnimation(dismissAnim);
            }
            if (model.isRepay()) {
                payPasswordPopupWindow.serverProtocol = model.getServer_protocol();
            }
            if (model.getIs_tips().equals("1")) {
                dialogPopupWindow.showWithMessage(model.getTips_error(), "7");
            } else {
                payPasswordPopupWindow.goAnimPlusIncome(this.findViewById(R.id.main), model);
            }
        }
    }

    @Override
    public void callPayCheckBack(boolean success) {

    }

    @Override
    public void doInvestSuccess(boolean success) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mFirstFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "mContent", mFirstFragment); // Exception made here
        }
    }

    @Override
    public void gainAccountSettingSuccess(AccountSettingJson response) {
        try {
            hideLoadingDialog();
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    AccountSettingData json = response.getData();
                    isSetPayPsw = json.getIs_paypwd_mobile_set();
                    if (isSetPayPsw.equals("1")) {
                        GoLoginUtil.setPayPswSetFlag(1, mContext);
                    } else {
                        GoLoginUtil.setPayPswSetFlag(0, mContext);
                    }
                    // 走旧的流程
                    if (isOldInvestFlow) {
                        if ("1".equals(json.getIs_id_auth())) {
                            MemorySave.MS.userInfo.is_id_auth = json.getIs_id_auth();
                            investPayPswFlow();
                        } else {//引导充值自动实名认证
                            String title = "温馨提示";
                            String msg = "充值成功后系统自动进行实名认证！";
                            String absBtn = "现在去充值";
                            String negBtn = "取消";
                            mRzDialog = createDialog1(title, msg, absBtn, negBtn,
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(View arg0) {
                                            doRecharge(true);
                                            mRzDialog.dismiss();
                                        }
                                    }, new OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {
                                            // createDialogDismissAuto("取消");
                                            mRzDialog.dismiss();
                                        }
                                    });
                            mRzDialog.show();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainAccountSettingFail() {
        hideLoadingDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        isLogin();
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == 1) {
            Intent intent = new Intent(this, MainActivity.class);
            MemorySave.MS.mIsGoFinanceHome = true;
            startActivity(intent);
            this.finish();
        } else if (requestCode == 2) {//设置完成支付密码后回来进入充值页面
            isInvestFlag = false;
            // 充值流程
            if (isEcwAccount) {// 是存管账户
                rechargeFlow();
            } else {// 不是存管账户
                oldRechargeFlow();
            }
        }
    }

    /**
     * 项目详情是否可见
     *
     * @param ivVisible visible if false
     * @param state     state
     * @param msg       message for the reason of invisible
     */
    private void isProjectDetailVisible(boolean ivVisible, String state, String msg) {
        if (ivVisible) {
            llForbiddenContainer.setVisibility(View.VISIBLE);
            llDetailContainer.setVisibility(View.GONE);
//            if (state.equals("项目截止投标")) {
//                img_project_detail.setImageDrawable(getDrawable(R.drawable.icon_project_detail_jiebiao));
//            } else if (state.equals("项目已满标")) {
//                img_project_detail.setImageDrawable(getDrawable(R.drawable.icon_project_detail_manbiao));
//            } else if (state.equals("项目待回款")) {
//                img_project_detail.setImageDrawable(getDrawable(R.drawable.icon_project_detail_daihuikuan));
//            } else if (state.equals("项目已回款")) {
//                img_project_detail.setImageDrawable(getDrawable(R.drawable.icon_project_detail_huikuan));
//            }
            mTvTitle.setText(mPrjName);
            tv_state.setText(state);
            tv_msg.setText(msg);
        } else {
            llForbiddenContainer.setVisibility(View.GONE);
            llDetailContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 倒计时工具类
     */
    public class TimeCount extends CountDownTimer {

        boolean isFinish = false;

        public TimeCount(long millisInFuture, long countDownInterval, String bidState) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔

        }

        @Override
        public void onFinish() {// 计时完毕时触发
            try {
                if (!isFinish) {
                    new TimeCountFinishHandler().sendEmptyMessage(0);
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            try {
                StringBuffer str = new StringBuffer();
                long second = millisUntilFinished / 1000;
                long s = second % 60; // 秒
                long mi = (second - s) / 60 % 60; // 分钟
                long h = ((second - s) / 60 - mi) / 60 % 24; // 小时
                long d = (((second - s) / 60 - mi) / 60 - h) / 24; // 天
                if (d > 0) {
                    str.append(d + "天");
                }
                String h1 = h > 10 ? h + "" : "0" + h;
                String mi1 = mi > 10 ? mi + "" : "0" + mi;
                String s1 = s > 10 ? s + "" : "0" + s;
                str.append(h1 + "小时");
                str.append(mi1 + "分");
                str.append(s1 + "秒");
                tvDkbLeftTime.setText(str + "后开始投资");// +item.ra
            } catch (Exception e) {
                isFinish = true;
                // cancel();
            }
        }
    }

    /**
     * 倒计时处理handler
     */
    class TimeCountFinishHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            financeInfoData.setBid_status("2");
            dealWithPrjDetailData(financeInfoData);
        }
    }

    /**
     * 详情页ViewPager适配器
     */
    class MyPageAdapter extends FragmentStatePagerAdapter {

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int pos) {
            return mFragments.get(pos);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}