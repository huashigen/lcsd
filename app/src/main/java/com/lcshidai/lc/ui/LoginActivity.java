package com.lcshidai.lc.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.lcshidai.lc.impl.MessageImpl;
import com.lcshidai.lc.impl.account.BindUidCidImpl;
import com.lcshidai.lc.impl.account.LoginImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.MessageJson;
import com.lcshidai.lc.model.MessageLocalData;
import com.lcshidai.lc.model.account.BaseLoginJson;
import com.lcshidai.lc.model.account.BaseLoginStrJson;
import com.lcshidai.lc.model.account.BindUidCidJson;
import com.lcshidai.lc.model.account.MobileCheckData;
import com.lcshidai.lc.model.account.MobileCheckJson;
import com.lcshidai.lc.service.ApiService;
import com.lcshidai.lc.service.HttpMsgService;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.service.account.HttpBindUidCidService;
import com.lcshidai.lc.service.account.HttpLoginService;
import com.lcshidai.lc.ui.account.user.ForgetPasswordFirstActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.CookieUtil;
import com.lcshidai.lc.utils.DataBuriedManager;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.MsgUtil;
import com.lcshidai.lc.utils.NetUtils;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.socks.library.KLog;
import com.tendcloud.tenddata.TCAgent;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends TRJActivity implements LoginImpl, MessageImpl, BindUidCidImpl,
        OnClickListener {
    HttpLoginService hbls;
    HttpMsgService hms;
    private HttpBindUidCidService bindUidCidService;

    private Dialog netDisconnectedDialog;
    private int mLoginCount = 0;// 登录次数
    private String uid;
    private String mUserName = "";
    private String mPwd = "";
    private String mDyCode = "";

    private ImageButton mBackBtn;
    private LinearLayout llUsernameContainer, llPwdContainer;
    private EditText etUserName, etPwd;
    private ImageView ivUserDelete, ivPwdDelete;
    private RelativeLayout rlDynamicCodeContainer;
    private EditText etDynamicCode;
    private Button btnDynamicCode;
    private LinearLayout llForgetPswAndRegisterContainer;
    private TextView tvForgetPwd;
    private View vDivider;
    private TextView tvRegisterRightNow;
    private CheckBox ckRememberUsername;
    private TextView tvLoginRegisterHongbao;
    private TextView tvLogin;

    private String gestureLockUserName = "";
    private String gestureLockUserId = "";
    private int mainWebFlag = 0; // 是否是从MainWebActivity跳转过来，20表示是
    private String newIntent;
    private int isForgetIntent = -1; // 1:忘记密码跳转 0:其他账户登陆 默认-1
    private boolean isBackToMain = false; // 是否返回跳转到主页面：true-是 false-否
    private Dialog gesturePwdSettingDialog;
    private boolean isSet = false;
    private CookieUtil cookieUtil;
    public static final int LOGIN_SUCCESS = 101;

    String goClass;
    String web_url;

    public enum Step {
        LOGIN, PWD, DEF
    }

    // 步骤
    private Step step = Step.LOGIN;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bundle bundle = getIntent().getExtras();
        isForgetIntent = getIntent().getIntExtra("isForgetIntent", -1);
        isBackToMain = getIntent().getBooleanExtra("is_back_to_main", false);
        if (bundle != null) {
            mainWebFlag = bundle.getInt("main_web_flag");
            newIntent = bundle.getString("fromSubActivity");
            goClass = bundle.getString("goClass");
            web_url = bundle.getString("web_url");
        }
        gestureLockUserName = SpUtils.getString(SpUtils.Table.CONFIG, SpUtils.Config.USER_NAME);
        gestureLockUserId = SpUtils.getString(SpUtils.Table.CONFIG, SpUtils.Config.UID);
        uid = SpUtils.getString(SpUtils.Table.USER, SpUtils.User.UID);
        hbls = new HttpLoginService((TRJActivity) mContext, this);
        hms = new HttpMsgService(mContext, this);
//        hacs = new HttpAccountCheckService<>(mContext, this);
        bindUidCidService = new HttpBindUidCidService(mContext, this);
        cookieUtil = new CookieUtil(this);

        getViews();
        setViews();
        initNetConnectedDialog();
        initGesturePwdSettingDialog();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    /**
     * 初始化网络连接状态检查Dialog
     */
    private void initNetConnectedDialog() {
        netDisconnectedDialog = createDialog(
                "网络连接失败，是否重新连接网络", "是", "取消", new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (netDisconnectedDialog.isShowing()) {
                            netDisconnectedDialog.dismiss();
                        }
                        if (!NetUtils.isNetworkConnected(mContext)) {
                            if (!netDisconnectedDialog.isShowing()) {
                                netDisconnectedDialog.show();
                            }
                        } else {
                            gainDynamicCode();
                        }
                    }
                }, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (netDisconnectedDialog.isShowing()) {
                            netDisconnectedDialog.dismiss();
                        }
                    }
                });
        netDisconnectedDialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    MemorySave.MS.mIsLogined = false;
                }
                return false;
            }
        });
    }

    /**
     * 初始化手势密码设置Dialog
     */
    private void initGesturePwdSettingDialog() {
        gesturePwdSettingDialog = createDialog("设置手势密码", "立即设置", "下次再说", new OnClickListener() {

            @Override
            public void onClick(View v) {
                isSet = true;
                if (gesturePwdSettingDialog.isShowing()) {
                    gesturePwdSettingDialog.dismiss();
                }
            }
        }, new OnClickListener() {

            @Override
            public void onClick(View v) {
                isSet = false;
                if (gesturePwdSettingDialog.isShowing()) {
                    gesturePwdSettingDialog.dismiss();
                }
            }
        });
        gesturePwdSettingDialog.setCancelable(false);
        gesturePwdSettingDialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_SEARCH;
            }
        });
        gesturePwdSettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                if (isSet) {
                    // 进入手势设置页面
                    startActivity(getIntent().setClass(mContext, SettingGestureLockActivity.class));
                } else {
                    SpUtils.setBoolean(SpUtils.Table.CONFIG, SpUtils.Config.IS_GESTURE_OPEN, false);
                    try {
                        if (getIntent() != null) {
                            String nameStr = getIntent().getStringExtra("goClass");
                            if (nameStr != null && !nameStr.equals("")) {
                                Class clazz;
                                Intent intent = getIntent();
                                clazz = Class.forName(nameStr);
                                intent.setClass(mContext, clazz);
                                if (!StringUtils.isEmpty(web_url)) {
                                    intent.putExtra("web_url", web_url);
                                }
                                if (MemorySave.MS.args != null)
                                    intent.putExtras(MemorySave.MS.args);
                                startActivity(intent);
                            }
                            getIntent().removeExtra("goClass");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }
        });
    }

    public void getViews() {
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        llUsernameContainer = (LinearLayout) findViewById(R.id.ll_username_container);
        etUserName = (EditText) findViewById(R.id.edit_username);
        ivUserDelete = (ImageView) findViewById(R.id.iv_user_delete);
        llPwdContainer = (LinearLayout) findViewById(R.id.ll_pwd_container);
        etPwd = (EditText) findViewById(R.id.edit_pwd);
        ivPwdDelete = (ImageView) findViewById(R.id.iv_pwd_delete);
        etDynamicCode = (EditText) findViewById(R.id.edit_dynamic_code);
        llForgetPswAndRegisterContainer = (LinearLayout) findViewById(R.id.ll_forget_pwd_and_register_container);
        tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        vDivider = findViewById(R.id.v_divider);
        tvRegisterRightNow = (TextView) findViewById(R.id.tv_register_right_now);
        rlDynamicCodeContainer = (RelativeLayout) findViewById(R.id.rl_dynamic_code_container);
        btnDynamicCode = (Button) findViewById(R.id.btn_dynamic_code);
        ckRememberUsername = (CheckBox) findViewById(R.id.ck_remember_username);
        tvLoginRegisterHongbao = (TextView) findViewById(R.id.tv_login_register_hongbao);
        tvLoginRegisterHongbao.setOnClickListener(registerClickListener);
        tvLogin = (TextView) findViewById(R.id.tv_login);

        mBackBtn.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
        tvRegisterRightNow.setOnClickListener(this);
        btnDynamicCode.setOnClickListener(this);
        ivUserDelete.setOnClickListener(this);
        ivPwdDelete.setOnClickListener(this);

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etUserName.getText().toString().length() > 0) {
                    ivUserDelete.setVisibility(View.VISIBLE);
                } else {
                    ivUserDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPwd.getText().toString().length() > 0) {
                    ivPwdDelete.setVisibility(View.VISIBLE);
                } else {
                    ivPwdDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvLogin.setOnClickListener(this);

    }

    OnClickListener registerClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (MemorySave.MS.mFinanceToLoginLock) {
                MemorySave.MS.mLoginToRegLock = true;
            }
            Intent intent = new Intent(mContext, UserRegisterFirActivity.class);
            if (goClass != null && !goClass.equals("")) {
                intent.putExtra("goClass", goClass);
            }
            startActivity(intent);
            finish();
        }
    };

    public void setViews() {
//        tvLogin.setBackgroundResource(R.drawable.login_btn_bg_next);
        tvLogin.setText("下一步");
        etUserName.setTextSize(16);
        etUserName.setHint("请输入手机号登录或注册");
        etUserName.setTextColor(Color.BLACK);
        etUserName.setInputType(InputType.TYPE_CLASS_NUMBER);
//        etUserName.setRightTextColor(Color.GRAY);
        String username = GoLoginUtil.getUserName(mContext);
        if (isForgetIntent != 0)
            etUserName.setText(username);
        if (!TextUtils.isEmpty(username)) {
//            调用已有登录账户的登录方法
            showLoginDef();
        }
        etPwd.setHint("登录密码");
        etPwd.setTextSize(16);
        etDynamicCode.setHint("请输入验证码");
        etDynamicCode.setTextColor(Color.BLACK);
        etPwd.setTextColor(Color.BLACK);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (step == Step.PWD) {
                    showUsername();
                }
//                else if (!MemorySave.MS.isGoLast) {
//                    MemorySave.MS.mIsGoHome = true;
//                    MemorySave.MS.mIsLogined = false;
//                    startActivity(new Intent(mContext, MainActivity.class));
//                }
                else {
                    finish();
                }
                break;
            case R.id.iv_user_delete:
                etUserName.setText("");
                break;
            case R.id.iv_pwd_delete:
                etPwd.setText("");
                break;
            case R.id.btn_dynamic_code:
                // 获取图形验证码
                gainDynamicCode();
                break;
            case R.id.tv_forget_pwd:
                // 跳转忘记密码页面
                startActivity(new Intent(mContext, ForgetPasswordFirstActivity.class));
                break;
            case R.id.tv_register_right_now:
                showUsername();
                break;
            case R.id.tv_login:
                performLoginBtnClick();
                break;
            default:

                break;
        }
    }

    /**
     * 执行login btn click事件
     */
    private void performLoginBtnClick() {
        mUserName = etUserName.getText().toString();
        mPwd = etPwd.getText().toString();
        mDyCode = etDynamicCode.getText().toString();
        switch (step) {
            case LOGIN:
                if (mUserName.equals("")) {
                    ToastUtil.showToast(mContext, "用戶名不能为空");
                    return;
                }
                if (etUserName.length() != 11) {
                    ToastUtil.showToast(mContext, "手机号位数不够");
                } else if (!isMobileNO(mUserName)) {
                    ToastUtil.showToast(mContext, "请输入正确的手机号");
                }
                String tag = CommonUtil.getDeviceId(mContext) + "-" + System.currentTimeMillis();
                SpUtils.setString(SpUtils.Table.CONFIG, SpUtils.Config.REG_FLOW, tag);
                isMobileRegister(mUserName);
                break;
            case PWD:
            case DEF:
                if (mPwd.equals("")) {
                    ToastUtil.showToast(mContext, "密码不能为空");
                    return;
                }
                if (mLoginCount >= 2) {
                    if (mDyCode.equals("")) {
                        ToastUtil.showToast(mContext, "请填写验证码");
                        return;
                    }
                }
                login(mUserName, mPwd, mDyCode);
                break;
            default:
                break;
        }
    }

    //    判断手机号是否正确
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^(13[0-9]|14[5-9]|15[0-35-9]|16[6]|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 手机号是否注册
     *
     * @param userMobile 用户手机号
     */
    private void isMobileRegister(String userMobile) {
        ApiService.isMobileRegister(new ProJsonHandler<>(new BaseCallback<MobileCheckJson>() {
            @Override
            protected void onRightData(MobileCheckJson response) {
                MobileCheckData data = response.getData();
                int status = data.getStatus();
                int isVCode = data.getVCode();// 0 不显示，1显示
                if (status == 1) {
//                    判断出手机号已注册，进入输入密码阶段
                    showPwd();
                } else {
                    gotoRegister(status, isVCode);
                }
            }

            @Override
            protected void onWrongData(MobileCheckJson response) {
                super.onWrongData(response);
                ToastUtil.showShortToast(mContext, response.getMessage());
            }
        }, mContext), mContext, userMobile);
    }

    /**
     * 获取动态图形验证码
     */
    public void gainDynamicCode() {
        ApiService.getBitmapData(new BinaryHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                Drawable drawable = new BitmapDrawable(bitmap);
                btnDynamicCode.setBackground(drawable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {

            }
        }, mContext, HttpServiceURL.VERIFY);
    }

    @Override
    public void onResume() {
        super.onResume();
        DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_START_IN);
        if (!netDisconnectedDialog.isShowing() && !NetUtils.isNetworkConnected(mContext)) {
            netDisconnectedDialog.show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_START_OUT);
    }

    /**
     * 输入密码登录
     */
    private void showPwd() {
        step = Step.PWD;
//        tvLogin.setBackgroundResource(R.drawable.login_btn_bg_next);
        tvLogin.setText("登录");
        llUsernameContainer.setVisibility(View.GONE);
        llPwdContainer.setVisibility(View.VISIBLE);
//		etPwd.setText("");
        etPwd.setFocusable(true);
        etPwd.requestFocus();
        etDynamicCode.setText("");
        // 只显示忘记密码
        llForgetPswAndRegisterContainer.setVisibility(View.VISIBLE);
        tvForgetPwd.setVisibility(View.VISIBLE);
        vDivider.setVisibility(View.GONE);
        tvRegisterRightNow.setVisibility(View.GONE);
    }

    /**
     * 显示输入账户
     */
    private void showUsername() {
        step = Step.LOGIN;
        mLoginCount = 0;
//        etUserName.showBtn();
        etUserName.setHint("请输入手机号登录或注册");
//        tvLogin.setBackgroundResource(R.drawable.login_btn_bg_next);
        tvLogin.setText("下一步");
        etUserName.setFocusable(true);
        rlDynamicCodeContainer.setVisibility(View.GONE);
        llUsernameContainer.setVisibility(View.VISIBLE);
        llPwdContainer.setVisibility(View.GONE);
        // 隐藏忘记密码及注册容器
        llForgetPswAndRegisterContainer.setVisibility(View.GONE);
        tvForgetPwd.setVisibility(View.GONE);
        vDivider.setVisibility(View.GONE);
        tvRegisterRightNow.setVisibility(View.GONE);
    }

    /**
     * 已有登录账户
     */
    private void showLoginDef() {
        step = Step.DEF;
        mLoginCount = 0;
        String user_name = etUserName.getText().toString();
        etUserName.setSelection(user_name.length());
        etUserName.setHint("用户名/手机号码/邮箱");
//        tvLogin.setBackgroundResource(R.drawable.login_btn_bg_login);
        tvLogin.setText("登录");
//		etUserName.setFocusable(false);
        llUsernameContainer.setVisibility(View.VISIBLE);
        llPwdContainer.setVisibility(View.VISIBLE);
        llForgetPswAndRegisterContainer.setVisibility(View.VISIBLE);
        tvForgetPwd.setVisibility(View.VISIBLE);
        vDivider.setVisibility(View.VISIBLE);
        tvRegisterRightNow.setVisibility(View.VISIBLE);
    }

    public void login(final String account, final String pwd, String code) {
        showLoadingDialog(mContext, "加载中...", true);
        tvLogin.setClickable(false);
        // 清除之前的cookie
        LCHttpClient.clearCookie();
        hbls.gainBaseLogin(account, pwd, code);

    }

    @Override
    public void loginSuccess(BaseJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    BaseLoginJson blj = (BaseLoginJson) response;
                    if (blj.getData() != null) {
                        GoLoginUtil.saveUserModel(blj, (TRJActivity) mContext);
                        String uid = blj.getData().getUid();
                        String uc_id = blj.getData().getUc_id();
                        // 保存uid
                        SpUtils.setString(SpUtils.Table.USER, SpUtils.User.UID, uid);
                        GoLoginUtil.saveUcId(uc_id, (TRJActivity) mContext);
                        GoLoginUtil.upP(uid, MemorySave.MS.userInfo.uname, mPwd, (TRJActivity) mContext);
                        if (ckRememberUsername.isChecked()) {
                            GoLoginUtil.saveUserName(mUserName, mContext);
                        }
                        // 获取消息
                        gainMessage(uid);
                        TCAgent.onEvent(mContext, Constants.AGENT_LOGIN);
                        String cid = com.igexin.sdk.PushManager.getInstance().getClientid(mContext);
                        // 将个推cid与uid绑定
                        bindUidCidService.bindUidCid(uid, cid, "android");
                    }
                } else {
                    BaseLoginStrJson bsj = (BaseLoginStrJson) response;
                    String data = bsj.getData();
                    String message = response.getMessage();
                    if (message.contains("验证码")) {
                        rlDynamicCodeContainer.setVisibility(View.VISIBLE);
                        gainDynamicCode();
                    } else if (StringUtils.isInteger(data)) {
                        mLoginCount = Integer.valueOf(data);
                        if (mLoginCount >= 2) {
                            rlDynamicCodeContainer.setVisibility(View.VISIBLE);
                            gainDynamicCode();
                        }
                    }
                    ToastUtil.showToast(mContext, message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            loginReset();
        }
    }

    @Override
    public void loginFailed() {
        loginReset();
    }

    /**
     * 登录重置
     */
    private void loginReset() {
        tvLogin.setClickable(true);
        hideLoadingDialog();
    }

    /**
     * 发送获取消息请求
     *
     * @param uid
     */
    public void gainMessage(String uid) {
        int account_sequence = 0;
        if (this.uid.equals(uid)) {
            account_sequence = MsgUtil.getInt(mContext, MsgUtil.ACCOUNT_SEQUENCE);
        }
        int invest_sequence = MsgUtil.getInt(mContext, MsgUtil.INVEST_SEQUENCE);
        int discovery_sequence = MsgUtil.getInt(mContext, MsgUtil.DISCOVERY_SEQUENCE);
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

    @Override
    public void gainMessageSuccess(MessageJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                List<MessageLocalData> list = MsgUtil.convertList(response.getData());
                if (list != null) {
                    MsgUtil.initData(mContext, list);
                }
            }
            goBackSuccess();
        }
    }

    @Override
    public void gainMessageFail() {
        goBackSuccess();
    }

    @Override
    public void bindUidCidSuccess(BindUidCidJson response) {
        if (null != response) {
            if (response.getBoolen().equals("1")) {
                android.util.Log.d("bindUidCidSuccess", "绑定成功");
            }
        }
    }

    @Override
    public void bindUidCidFailed() {

    }

    /**
     * 跳转到注册二级页面
     */
    private void gotoRegister(int status, int isVcode) {
        DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_NEXT_CLICK);
        Intent intent = new Intent();
        intent.setClass(mContext, UserRegisterSecActivity.class);
        intent.putExtra("mobile", mUserName);
        intent.putExtra("status", status);
        // 进行进一步调试
        intent.putExtra("isShowDyCode", isVcode);
        intent.putExtra("res_pop", 1);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        MemorySave.MS.mIsLogined = false;
        if (keyCode == KeyEvent.KEYCODE_BACK && isBackToMain) {
            // getIntent().putExtra("goClass", "");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            LoginActivity.this.finish();
        } else if (keyCode == KeyEvent.KEYCODE_BACK && !GoLoginUtil.isShowGestureLogin(LoginActivity.this)) {
            MemorySave.clearMomery();
            MemorySave.MS.mIsGoHome = true;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void goBackSuccess() {
        MemorySave.MS.isGestureEnd = true;
        SpUtils.setInt(SpUtils.Table.CONFIG, SpUtils.Config.TOTAL_TRY_TIMES, 5);
        try {
            // 保存cookie
            List<Cookie> cookies = LCHttpClient.getCookie();
            if (!cookies.isEmpty()) {
                String strCookie = "";
                Cookie cookie = null;
                for (int i = 0; i < cookies.size(); i++) {
                    cookie = cookies.get(i);
                    if ("PHPSESSID".equals(cookie.getName())) {
                        strCookie = cookie.getName() + "=" + cookie.getValue();
                    }
                }
                cookieUtil.saveCookie(strCookie);
                KLog.i("cookie", strCookie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // MemorySave.MS.mIsLoginMark=true;
            MemorySave.MS.mGestureAudoLoginFlag = false;
            GoLoginUtil.loginSuccess();
            sendBroadcast(MY_RESON_LOGIN_STATUS);
            MemorySave.MS.isLoginSendBroadcast = true;
            setResult(LOGIN_SUCCESS);
            boolean isGestureOpen = SpUtils.getBoolean(SpUtils.Table.CONFIG,
                    SpUtils.Config.IS_GESTURE_OPEN, false);
            if (isForgetIntent == -1) {
                if (MemorySave.MS.userInfo.uid.equals(gestureLockUserId) && isGestureOpen) {
                    if (getIntent() != null) {
                        String nameStr = getIntent().getStringExtra("goClass");
                        if (nameStr != null && !nameStr.equals("")) {
                            Class clazz;
                            Intent intent = getIntent();
                            clazz = Class.forName(nameStr);
                            intent.setClass(this, clazz);
                            if (!StringUtils.isEmpty(web_url)) {
                                intent.putExtra("web_url", web_url);
                            }
                            if (MemorySave.MS.args != null)
                                intent.putExtras(MemorySave.MS.args);
                            startActivity(intent);
                        }
                        getIntent().removeExtra("goClass");
                        finish();
                    }
                } else {
                    isSet = false;
                    if (!isGestureOpen) {
                        if (!gesturePwdSettingDialog.isShowing()) {
                            gesturePwdSettingDialog.show();
                        }
                    } else {
                        if (getIntent() != null) {
                            String nameStr = getIntent().getStringExtra("goClass");
                            if (nameStr != null && !nameStr.equals("")) {
                                Class clazz;
                                Intent intent = getIntent();
                                clazz = Class.forName(nameStr);
                                if (!StringUtils.isEmpty(web_url)) {
                                    intent.putExtra("web_url", web_url);
                                }
                                intent.setClass(this, clazz);
                                if (MemorySave.MS.args != null)
                                    intent.putExtras(MemorySave.MS.args);
                                startActivity(intent);
                            }
                            getIntent().removeExtra("goClass");
                            finish();
                        }
                    }
                }
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mainWebFlag == 20) {
                setResult(20);
            }
        }

    }
}
