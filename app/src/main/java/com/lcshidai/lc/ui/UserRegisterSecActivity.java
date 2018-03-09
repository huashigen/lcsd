package com.lcshidai.lc.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tendcloud.tenddata.TCAgent;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.MessageImpl;
import com.lcshidai.lc.impl.account.GainCodeImpl;
import com.lcshidai.lc.impl.account.UserRegisterSecImpl;
import com.lcshidai.lc.impl.account.UserRegisterSendcodeImpl;
import com.lcshidai.lc.impl.account.UserSetPasswordImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.MessageJson;
import com.lcshidai.lc.model.MessageLocalData;
import com.lcshidai.lc.model.account.UserRegisterSecData;
import com.lcshidai.lc.model.account.UserRegisterSecJson;
import com.lcshidai.lc.service.HttpMsgService;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.service.account.HttpGainCodeService;
import com.lcshidai.lc.service.account.HttpSendRegisterSmsCodeService;
import com.lcshidai.lc.service.account.HttpUserRegisterSecService;
import com.lcshidai.lc.service.account.HttpUserRegisterSendcodeService;
import com.lcshidai.lc.service.account.HttpUserSetPasswordNewService;
import com.lcshidai.lc.ui.account.RegisterSuccessActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.DataBuriedManager;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.MsgUtil;
import com.lcshidai.lc.utils.SmsAutoUtil;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.ToastUtil;

import java.util.List;

/**
 * 注册第二步
 * Created by Randy on 2016/5/5.
 */
public class UserRegisterSecActivity extends TRJActivity implements UserRegisterSendcodeImpl, UserRegisterSecImpl, GainCodeImpl, MessageImpl,
        UserSetPasswordImpl, View.OnClickListener {
    // 网络请求
    HttpSendRegisterSmsCodeService hurss;
    HttpUserRegisterSecService hurs;
    HttpUserSetPasswordNewService mHttpUserSetPasswordService;
    HttpGainCodeService hgcs;
    HttpMsgService hms;

    private Button btnOption;
    private ImageButton btnBack;
    private TextView tvTitle, tvSubtitle;
    private TimeCount timeCounter;
    private String uid;
    private String password = "", passwordAgain = "", mobileNumber = "", smsCode = "", recommendCode = "";

    private LinearLayout llDyCodeContainer;
    private RelativeLayout rlSmsCodeContainer;
    private EditText etDyCode, etSmsCode, etPassword;
    private TextView tvResendSmsCode, tvRecommendCode, tvRegister;
    private ImageView ivDyCodeDel, ivSmsCodeDel, ivPasswordDel, ivPasswordReview;
    private Button btnGainDyCode;

    private CheckBox cbAgreement;
    private TextView tvAgreement;

    private PopupWindow recommendCodePopWin;// 填写推荐人代码的弹出框；
    private PopupWindow seekBarVerifyPopWin;// 滑动验证弹出框
    private boolean isPswHide = true;
    private int res_pop = 0;
    private int status = 0;
    private int isShowDyCode = 0;// 0不显示，1显示
    private boolean isSet = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_sec);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mobileNumber = bundle.getString("mobile");
            status = bundle.getInt("status", 0);
            // 是否显示图形动态验证码
            isShowDyCode = bundle.getInt("isShowDyCode", 0);
            res_pop = bundle.getInt("res_pop", 0);
        }
        hurss = new HttpSendRegisterSmsCodeService(this, this);
        hurs = new HttpUserRegisterSecService(this, this);
        hms = new HttpMsgService(this, this);
        mHttpUserSetPasswordService = new HttpUserSetPasswordNewService(this, this);
        timeCounter = new TimeCount(120000, 1000);// 构造CountDownTimer对象

        getViews();

        // event binding
        btnBack.setOnClickListener(this);
        tvResendSmsCode.setOnClickListener(this);
        ivDyCodeDel.setOnClickListener(this);
        btnGainDyCode.setOnClickListener(this);
        ivSmsCodeDel.setOnClickListener(this);
        ivPasswordDel.setOnClickListener(this);
        ivPasswordReview.setOnClickListener(this);
        tvRecommendCode.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        etDyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etDyCode.getText().toString().length() > 0) {
                    ivDyCodeDel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSmsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etSmsCode.getText().toString().length() > 0) {
                    ivSmsCodeDel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPassword.getText().length() > 0) {
                    ivPasswordDel.setVisibility(View.VISIBLE);
                    tvRegister.setBackgroundResource(R.drawable.bg_button_clickable_true);
                } else {
                    ivPasswordDel.setVisibility(View.GONE);
                    tvRegister.setBackgroundResource(R.drawable.bg_button_clickable_false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 导航栏处理
        tvTitle.setText("注册");
        tvRegister.setText("注册");
        tvSubtitle.setVisibility(View.GONE);
        btnOption.setVisibility(View.INVISIBLE);
        if (status == 2) {
            // 活动注册，隐藏短信验证码输入框，显示是否同意协议输入框
            rlSmsCodeContainer.setVisibility(View.GONE);
            tvRecommendCode.setVisibility(View.GONE);
        } else {
            //正常注册
            rlSmsCodeContainer.setVisibility(View.VISIBLE);
            tvRecommendCode.setVisibility(View.VISIBLE);
            tvRecommendCode.setText("推荐人代码（选填）");
            tvRecommendCode.setTextColor(getResources().getColor(R.color.theme_color));
        }
        //  处理协议内容
        dealWithAgreement();

        hgcs = new HttpGainCodeService(this, this);
        if (isShowDyCode == 1) {
            llDyCodeContainer.setVisibility(View.VISIBLE);
            hgcs.getBitmapData(HttpServiceURL.VERIFY, "", "", "");
        } else {
            llDyCodeContainer.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_SECOND_IN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_SECOND_OUT);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                        SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_BACK_CLICK);
                setResult(20);
                finish();
                break;
            case R.id.iv_dy_code_del:
                etDyCode.setText("");
                ivDyCodeDel.setVisibility(View.GONE);
                break;
            case R.id.btn_gain_dy_code:
                hgcs.getBitmapData(HttpServiceURL.VERIFY, "", "", "");
                break;
            case R.id.tv_send_verify_code:
                if (status == 2) {
                    //设置为活动用户
                    hurss.isActivity();
                }
                if (isShowDyCode == 0) {//不显示图形验证码，显示验证框
                    if (seekBarVerifyPopWin == null) {
                        initSeekBarPopWin();
                        seekBarVerifyPopWin.showAtLocation(tvRecommendCode, Gravity.CENTER, 0, 0);
                    } else {
                        seekBarVerifyPopWin.showAtLocation(tvRecommendCode, Gravity.CENTER, 0, 0);
                    }
                    DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                            SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_SMS_CODE_CLICK);
                } else {// 显示图形验证码，不现实验证框
                    hurss.getRegisterSmsCode(mobileNumber, etDyCode.getText().toString(),
                            isShowDyCode, HttpUserRegisterSendcodeService.REGISTER_PWD_URL_FLAG);
                }

                break;
            case R.id.iv_verify_code_del:
                etSmsCode.setText("");
                ivSmsCodeDel.setVisibility(View.GONE);
                break;
            case R.id.iv_psw_del:
                etPassword.setText("");
                ivPasswordDel.setVisibility(View.GONE);
                break;
            case R.id.iv_psw_view:
                if (isPswHide) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivPasswordReview.setImageDrawable(getResources().getDrawable(R.drawable.pwd_visible));
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivPasswordReview.setImageDrawable(getResources().getDrawable(R.drawable.pwd_invisible));
                }
                isPswHide = !isPswHide;
                etPassword.setSelection(etPassword.getText().toString().length());
                break;
            case R.id.tv_recommend_code:
                if (recommendCodePopWin == null) {
                    initRecommendWin();
                    recommendCodePopWin.showAtLocation(tvRecommendCode, Gravity.CENTER, 0, 0);
                } else {
                    recommendCodePopWin.showAtLocation(tvRecommendCode, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tv_register:
                // 注册操作
                if (validCheck()) {
                    DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                            SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_START);
                    onRegister();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始推荐代码弹出框
     */
    private void initRecommendWin() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_win_recommend_code, null);
        final EditText etRecommendCode = (EditText) contentView.findViewById(R.id.et_recommend_code);
        TextView tvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendCodePopWin.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                String code = etRecommendCode.getText().toString();
                recommendCode = code;
                tvRecommendCode.setText("推荐人代码：" + code);
                tvRecommendCode.setTextColor(getResources().getColor(R.color.theme_color));
                recommendCodePopWin.dismiss();
            }
        });
        recommendCodePopWin = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recommendCodePopWin.setFocusable(true);

    }

    /**
     * 初始化滑动验证弹出框
     */
    private void initSeekBarPopWin() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_win_seekbar_verify, null);
        final SeekBar seekBar = (SeekBar) contentView.findViewById(R.id.sb_dy);
        TextView tvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        final TextView tvTip = (TextView) contentView.findViewById(R.id.tv_tip);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarVerifyPopWin.dismiss();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 100) {
                    // 开始获取短信验证码
                    seekBarVerifyPopWin.dismiss();
                    DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                            SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_SECURITY_VERIFY);
                    hurss.getRegisterSmsCode(mobileNumber, "", isShowDyCode, HttpUserRegisterSendcodeService.REGISTER_PWD_URL_FLAG);
                }
                if (progress == 0) {
                    tvTip.setVisibility(View.VISIBLE);
                } else {
                    tvTip.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() != 100) {
                    seekBar.setProgress(0);
                }
            }
        });
        seekBarVerifyPopWin = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        seekBarVerifyPopWin.setFocusable(true);

        seekBarVerifyPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                seekBar.setProgress(0);
            }
        });
    }

    /**
     * 完整性检查
     */
    private boolean validCheck() {
        // 短信验证码还要另外再作判断
        smsCode = etSmsCode.getText().toString();
        password = etPassword.getText().toString();
        passwordAgain = password;
        if (status != 2 && CommonUtil.isNullOrEmpty(smsCode)) {
            createDialogDismissAuto("短信验证码不能为空");
            return false;
        }
        if (password.equals("")) {
            createDialogDismissAuto("登录密码不能为空");
            return false;
        } else if (mobileNumber.equals("")) {
            createDialogDismissAuto("手机号不能为空");
            return false;
        }
        // 添加是否同意协议代码，只有通过活动注册的才有协议的显示
        if (!(cbAgreement.isChecked())) {
            createDialogDismissAuto("请阅读并同意接受<<理财时代用户注册协议>>");
            return false;
        }

        return true;
    }

    /**
     * 通过活动注册的用户，需要显示协议，要做相关处理
     **/
    private void dealWithAgreement() {
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tvAgreement.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) tvAgreement.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans(); // should clear old spans
            for (URLSpan url : urls) {
                MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
                style.setSpan(myURLSpan, sp.getSpanStart(url),
                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new ForegroundColorSpan(Color.parseColor("#007AFF")), sp.getSpanStart(url),
                        sp.getSpanEnd(url),
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            }
            tvAgreement.setText(style);
        }
    }

    /**
     * 获取相应控件
     */
    private void getViews() {
        // find views
        // title bar
        tvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        tvSubtitle = (TextView) findViewById(R.id.tv_subtitle);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnOption = (Button) findViewById(R.id.btn_option);

        llDyCodeContainer = (LinearLayout) findViewById(R.id.ll_dy_code_container);
        etDyCode = (EditText) findViewById(R.id.et_dy_code);
        ivDyCodeDel = (ImageView) findViewById(R.id.iv_dy_code_del);
        btnGainDyCode = (Button) findViewById(R.id.btn_gain_dy_code);
        rlSmsCodeContainer = (RelativeLayout) findViewById(R.id.rl_sms_code_container);
        etSmsCode = (EditText) findViewById(R.id.et_verify_code);
        ivSmsCodeDel = (ImageView) findViewById(R.id.iv_verify_code_del);
        tvResendSmsCode = (TextView) findViewById(R.id.tv_send_verify_code);

        etPassword = (EditText) findViewById(R.id.et_psw);
        ivPasswordDel = (ImageView) findViewById(R.id.iv_psw_del);
        ivPasswordReview = (ImageView) findViewById(R.id.iv_psw_view);

        tvRecommendCode = (TextView) findViewById(R.id.tv_recommend_code);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        cbAgreement = (CheckBox) findViewById(R.id.cb_register_agreement);
        tvAgreement = (TextView) findViewById(R.id.tv_register_agreement);

    }

    /**
     * 提交注册
     */
    public void onRegister() {
        showLoadingDialog(mContext, "数据加载中...", true);
        tvRegister.setClickable(false);
        tvRegister.setBackgroundColor(Color.rgb(0xd1, 0xcf, 0xcc));

        if (status == 2) {
            mHttpUserSetPasswordService.setUserPassword(password, passwordAgain, mobileNumber);
        } else {
            hurs.gainUserRegisterSec(password, passwordAgain, mobileNumber, smsCode, recommendCode);
        }
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            // mBtnGainDn.setText(" 重新获取 ");
//            hgcs.getBitmapData(HttpServiceURL.VERIFY, false, "", "", "");
            etSmsCode.setText("");
            tvResendSmsCode.setText("重发验证码");
            tvResendSmsCode.setEnabled(true);
            tvResendSmsCode.setClickable(true);
            //bt_gain_code.setBackgroundResource(R.drawable.resend_code_bg);
            tvResendSmsCode.setTextColor(getResources().getColor(R.color.theme_color));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            tvResendSmsCode.setClickable(false);
            //	bt_gain_code.setBackgroundResource(R.drawable.bg_button_cancel);
            tvResendSmsCode.setTextColor(getResources().getColor(R.color.sended_color));
            tvResendSmsCode.setText("重发验证码" + "(" + millisUntilFinished / 1000 + ")");
        }
    }

    @Override
    protected void onDestroy() {
        MemorySave.MS.mLoginToRegLock = false;
        try {
            SmsAutoUtil.getInstance().stopWork(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private class MyURLSpan extends ClickableSpan {
        private String mUrl;

        MyURLSpan(String url) {
            mUrl = url;
        }

        @Override
        public void onClick(View widget) {
            if (mUrl.equals("1")) {
                Intent intent = new Intent(mContext, AgreementActivity.class);
                intent.putExtra("title", "理财时代注册协议");
                intent.putExtra("url", "/Index/Protocol/view?id=1");
                startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, AgreementActivity.class);
                intent.putExtra("title", "理财时代资金管理服务协议");
                intent.putExtra("url", "/Index/Protocol/view?id=2");
                startActivity(intent);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#2B7EC2"));
            ds.setUnderlineText(false); // 去掉下划线
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(20);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void doFinish() {
        setResult(20);
        super.doFinish();
    }

    @Override
    public void getRegisterSmsCodeSuccess(BaseJson response) {
        try {
            tvResendSmsCode.setEnabled(true);
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    tvResendSmsCode.setEnabled(false);
                    tvResendSmsCode.setTextColor(getResources().getColor(R.color.sended_color));
                    // 自动填充
                    try {
                        SmsAutoUtil.getInstance().startWork(mContext, new Handler(), etSmsCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    timeCounter.start();// 开始计时
                    createDialogDismissAuto("验证完成，动态码已发送");
                    // 隐藏系统键盘
                    hideSystemKeyboard((TRJActivity) mContext);
                } else {
                    String message = response.getMessage();
                    createDialogDismissAuto(message);
                    hgcs.getBitmapData(HttpServiceURL.VERIFY, "", "", "");
                    // 隐藏系统键盘
                    hideSystemKeyboard((TRJActivity) mContext);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏系统键盘
     */
    private void hideSystemKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
        View currentFocusView = activity.getCurrentFocus();
        if (null != currentFocusView) {
            inputMethodManager.hideSoftInputFromInputMethod(currentFocusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void getRegisterSmsCodeFailed() {

    }

    private String money = "", comment = "", rule = "";

    @Override
    public void gainUserRegisterSecsuccess(UserRegisterSecJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                UserRegisterSecData json = null;
                if (boolen.equals("1")) {
                    // ToastUtil.showToast(UserRegisterFirActivity.this,
                    // "注册成功");
                    // GoLoginUtil.saveUserModel(response);
                    DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                            SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_FINISH);
                    json = response.getData();
                    if (json != null) {
                        uid = json.getUid();
                        String uname = json.getUname();
                        String money = json.getMoney();
                        String comment = json.getComment();
                        String rule = json.getRule();
                        MemorySave.MS.mGestureAudoLoginFlag = false;
                        MemorySave.MS.mIsLogin = true;
                        MemorySave.MS.mIsLogined = true;
                        MemorySave.MS.mIsFristrec = true;
                        if (res_pop == 1) {
                            MemorySave.MS.mIsRecpop = true;
                        }
                        GoLoginUtil.saveUserName(mobileNumber, mContext);
                        SpUtils.setString(SpUtils.Table.USER, SpUtils.User.UID, uid);
                        GoLoginUtil.upP(uid, uname, password, (TRJActivity) mContext);
                        GoLoginUtil.autoLogin((TRJActivity) mContext);
                        isSet = false;
                        if (MemorySave.MS.mLoginToRegLock) {
                            MemorySave.MS.mRegLockSuccessBack = true;
                        }
                        this.money = money;
                        this.comment = comment;
                        this.rule = rule;
                        gainMessage(uid);
                        TCAgent.onEvent(this, Constants.AGENT_REGISTER);
                    }
                } else {
                    String message = response.getMessage();
                    createDialogDismissAuto(message);
                    tvRegister.setClickable(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tvRegister.setClickable(true);
            tvRegister.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_clickable_true));
            hideLoadingDialog();
        }
    }

    /**
     * 发送获取消息请求
     *
     * @param uid
     */
    public void gainMessage(String uid) {
        int invest_sequence = MsgUtil.getInt(this, MsgUtil.INVEST_SEQUENCE);
        int discovery_sequence = MsgUtil.getInt(this, MsgUtil.DISCOVERY_SEQUENCE);
        if (TextUtils.isEmpty(uid)) {
            uid = "0";
        }
        if (invest_sequence == -1) {
            invest_sequence = 0;
        }
        if (discovery_sequence == -1) {
            discovery_sequence = 0;
        }
        hms.getAllMsg(uid, invest_sequence, discovery_sequence, 0);
    }

    @Override
    public void gainUserRegisterSecfail() {
        ToastUtil.showToast((TRJActivity) mContext, "网络不给力!");
        tvRegister.setClickable(true);
        tvRegister.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_clickable_true));
        hideLoadingDialog();
    }

    @Override
    public void gainBitmapDatasuccess(byte[] binaryData, String url, String content, String title, String qrcode) {
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
        Drawable drawable = new BitmapDrawable(bitmap);
        btnGainDyCode.setBackground(drawable);
    }

    @Override
    public void gainBitmapDatafail() {

    }

    @Override
    public void setPasswordSuccess(UserRegisterSecJson response) {
        gainUserRegisterSecsuccess(response);
    }

    @Override
    public void setPasswordFail() {
        ToastUtil.showToast((TRJActivity) mContext, "网络不给力!");
        tvRegister.setClickable(true);
        tvRegister.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_clickable_true));
        hideLoadingDialog();
    }

    @Override
    public void gainMessageSuccess(MessageJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                List<MessageLocalData> list = MsgUtil.convertList(response.getData());
                if (list != null) {
                    MsgUtil.initData(this, list);
                }
                Intent intent = new Intent();
//                跳转注册成功页面
                intent.setClass(mContext, RegisterSuccessActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("money", money);
                bundle.putString("comment", comment);
                bundle.putString("rule", rule);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    @Override
    public void gainMessageFail() {

    }

}