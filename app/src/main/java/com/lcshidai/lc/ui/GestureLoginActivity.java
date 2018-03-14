package com.lcshidai.lc.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.service.ApiService;
import com.lcshidai.lc.R;
import com.lcshidai.lc.http.BaseCallback;
import com.lcshidai.lc.http.ProJsonHandler;
import com.lcshidai.lc.impl.MessageImpl;
import com.lcshidai.lc.impl.account.BindUidCidImpl;
import com.lcshidai.lc.model.MessageJson;
import com.lcshidai.lc.model.MessageLocalData;
import com.lcshidai.lc.model.account.AccountSettingData;
import com.lcshidai.lc.model.account.AccountSettingImg;
import com.lcshidai.lc.model.account.AccountSettingJson;
import com.lcshidai.lc.model.account.BindUidCidJson;
import com.lcshidai.lc.service.HttpMsgService;
import com.lcshidai.lc.service.account.HttpBindUidCidService;
import com.lcshidai.lc.service.account.HttpGainCodeService;
import com.lcshidai.lc.ui.account.MessageDetailActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CacheUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.LockPatternUtils;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.MsgUtil;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.Thumbnail;
import com.lcshidai.lc.widget.CircularImage;
import com.lcshidai.lc.widget.LockPatternView;
import com.lcshidai.lc.widget.LockPatternView.Cell;
import com.lcshidai.lc.widget.LockPatternView.DisplayMode;

import java.util.List;

public class GestureLoginActivity extends TRJActivity implements LockPatternView.OnPatternListener,
        OnClickListener, MessageImpl, BindUidCidImpl {
    private HttpGainCodeService hgcs;
    private HttpMsgService hms;
    private HttpBindUidCidService bindUidCidService;
    private TextView userName;
    private LockPatternView lockPatternView;
    private int wrongTime;
    private Animation shakeAnim;
    private TextView forgetGesture, changeUser;
    private String uid, mobile, cid;
    private Thumbnail thumbnail;
    private CircularImage userImageView;
    private Dialog forgetDialog;

    private String gestureLockUserName = "";
    private String gestureLockUserId = "";

    private boolean isGoLogined = false;
    private CacheUtil cacheUtil;
    private String message_centre = "", md_title = "", md_content = "", md_ctime = "";
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            message_centre = getIntent().getStringExtra("message_centre");
            md_title = getIntent().getStringExtra("md_title");
            md_content = getIntent().getStringExtra("md_content");
            md_ctime = getIntent().getStringExtra("md_ctime");
        }
        hms = new HttpMsgService(this, this);
        bindUidCidService = new HttpBindUidCidService(this, this);
        initView();
        MemorySave.MS.mIsGestureLoginAlive = true;
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (null != intent) {
            message_centre = intent.getStringExtra("message_centre");
            md_title = intent.getStringExtra("md_title");
            md_content = intent.getStringExtra("md_content");
            md_ctime = intent.getStringExtra("md_ctime");
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 17) {
            if (resultCode == LoginActivity.LOGIN_SUCCESS) {
                Intent intent = new Intent();
                if (MemorySave.MS.mIsFirstOpen)
                    intent.putExtra("goClass", MainActivity.class.getName());
                intent.setClass(mContext, SettingGestureLockActivity.class);
                startActivity(intent);
                GestureLoginActivity.this.finish();
            }
        } else if (requestCode == 18) {
            if (resultCode == LoginActivity.LOGIN_SUCCESS) {
                Intent intent = new Intent();
                if (MemorySave.MS.userInfo.uid.equals(gestureLockUserId)) {
                    if (MemorySave.MS.mIsFirstOpen) {
                        intent.setClass(mContext, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    if (MemorySave.MS.mIsFirstOpen)
                        intent.putExtra("goClass", MainActivity.class.getName());
                    intent.setClass(mContext, SettingGestureLockActivity.class);
                    startActivity(intent);
                }
                GestureLoginActivity.this.finish();
            }
        }
    }

    @Override
    protected void onResume() {
        isGoLogined = false;
        gestureLockUserId = SpUtils.getString(SpUtils.Table.CONFIG, SpUtils.Config.UID);
        MemorySave.MS.goLockActFromPower = false;
        MemorySave.MS.goLockActFromHome = false;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        MemorySave.MS.mIsGestureLoginAlive = false;
        super.onDestroy();
    }

    private void initView() {
        setContentView(R.layout.activity_gesture_login);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        cacheUtil = CacheUtil.get(mContext, 1000 * 1000 * 100, Integer.MAX_VALUE);
        wrongTime = SpUtils.getInt(SpUtils.Table.CONFIG, SpUtils.Config.TOTAL_TRY_TIMES, 5);
        uid = SpUtils.getString(SpUtils.Table.USER, SpUtils.User.UID);
        cid = com.igexin.sdk.PushManager.getInstance().getClientid(mContext);
        mobile = GoLoginUtil.getUserName(this);
        gestureLockUserName = SpUtils.getString(SpUtils.Table.CONFIG, SpUtils.Config.USER_NAME);
        gestureLockUserId = SpUtils.getString(SpUtils.Table.CONFIG, SpUtils.Config.UID);

        userName = (TextView) findViewById(R.id.gesture_login_tv_username);
        forgetGesture = (TextView) findViewById(R.id.gesture_login_tv_forget);
        changeUser = (TextView) findViewById(R.id.gesture_login_tv_change);
        lockPatternView = (LockPatternView) findViewById(R.id.gesture_login_lockview);
        userImageView = (CircularImage) findViewById(R.id.gesture_login_iv_userimg);

        shakeAnim = AnimationUtils.loadAnimation(mContext, R.anim.wrong_alert_shake);

        if (wrongTime == 5) {
            userName.setText(userNamePlusStar(gestureLockUserName));
            String maskNumber = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
            userName.setText(maskNumber);
            userName.setTextColor(Color.parseColor("#C63135"));
        } else {
            userName.setTextColor(Color.parseColor("#C63135"));
            userName.setText("还可以再输入" + wrongTime + "次");
        }

        forgetDialog = createDialog("忘记手势密码，需重新登录", "重新登录", "取消",
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (forgetDialog.isShowing()) {
                            forgetDialog.dismiss();
                        }
                        Intent intent = new Intent();
                        intent.putExtra("goClass", MainActivity.class.getName());
                        intent.putExtra("isForgetIntent", 1);
                        intent.setClass(mContext, LoginActivity.class);
                        startActivityForResult(intent, 17);
                    }
                },
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (forgetDialog.isShowing()) {
                            forgetDialog.dismiss();
                        }
                    }
                });

        forgetGesture.setOnClickListener(this);
        changeUser.setOnClickListener(this);
        lockPatternView.setOnPatternListener(this);

        if (!"".equals(uid)) {
            thumbnail = Thumbnail.init(mContext);
            loadUserImage();
        }
    }

    /**
     * 用户名加星
     */
    private String userNamePlusStar(String name) {
        int length = name.length();
        if (length == 2) {
            name = name.substring(0, 1);
            name = name.concat("*");
        } else if (length > 2) {
            StringBuffer sb = new StringBuffer();
            sb.append(name.charAt(0));
            for (int i = 0; i < (length - 2); i++) {
                sb.append("*");
            }
            sb.append(name.charAt(length - 1));
            name = sb.toString();
        }
        return name;
    }


    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            lockPatternView.clearPattern();
        }
    };

    private void postClearPatternRunnable() {
        lockPatternView.removeCallbacks(mClearPatternRunnable);
        lockPatternView.postDelayed(mClearPatternRunnable, 1000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //忘记密码
            case R.id.gesture_login_tv_forget:
                if (!forgetDialog.isShowing()) {
                    forgetDialog.show();
                }
                break;
            //切换用户
            case R.id.gesture_login_tv_change:
                Intent intent = new Intent();
                intent.putExtra("goClass", MainActivity.class.getName());
                intent.putExtra("isForgetIntent", 0);
                intent.setClass(mContext, LoginActivity.class);
                startActivityForResult(intent, 18);
                break;
        }
    }


    @Override
    public void onPatternStart() {
        lockPatternView.removeCallbacks(mClearPatternRunnable);
    }


    @Override
    public void onPatternCleared() {
        lockPatternView.removeCallbacks(mClearPatternRunnable);
    }


    @Override
    public void onPatternCellAdded(List<Cell> pattern) {

    }


    @Override
    public void onPatternDetected(List<Cell> pattern) {
        SpUtils.setInt(SpUtils.Table.CONFIG, SpUtils.Config.TOTAL_TRY_TIMES, 5);
        MemorySave.MS.mGestureAudoLoginFlag = true;
        MemorySave.MS.lastGestureTime = System.currentTimeMillis();
        //手势判断成功
        if (LockPatternUtils.getInstance(mContext).checkPattern(pattern) && wrongTime > 0) {
            if (!TextUtils.isEmpty(message_centre) && "message_centre".equals(message_centre)) {
                message_centre = "";
                Intent intent = new Intent();
                intent.setClass(GestureLoginActivity.this, MessageDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("message_centre", "message_centre");
                intent.putExtra("md_title", md_title);
                intent.putExtra("md_content", md_content);
                intent.putExtra("md_ctime", md_ctime);
                startActivity(intent);
            } else {
                if (MemorySave.MS.mIsFirstOpen) {
                    gainMessage(uid);
                    bindUidCidService.bindUidCid(uid, cid, "android");
                    GoLoginUtil.gestureAutoLogin(GestureLoginActivity.this, 0, MainActivity.class.getName());
                } else {
                    if (MemorySave.MS.mIsGoFinanceInfo) {
                        Intent intent = new Intent();
                        intent.setClass(GestureLoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (MemorySave.MS.mIsGoFinance) {
                        Intent intent = new Intent();
                        intent.setClass(GestureLoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        if (getIntent() != null) {
                            String nameStr = getIntent().getStringExtra("goClass");
//							String nameStr = GoClassUtil.goClass;
                            if (null != nameStr && !"".equals(nameStr)) {
                                GoLoginUtil.gestureAutoLogin(GestureLoginActivity.this, 0, nameStr);
                            } else {
                                GoLoginUtil.autoLogin(GestureLoginActivity.this);
                            }
                        }
                    }
                }
            }
            getIntent().removeExtra("goClass");
//			GoClassUtil.goClass = null;
            MemorySave.MS.isGestureEnd = true;
            GestureLoginActivity.this.finish();
        } else {
            lockPatternView.setDisplayMode(DisplayMode.Wrong);
            postClearPatternRunnable();
            if (wrongTime > 0) {
                wrongTime--;
            }
            if (wrongTime == 0 && !isGoLogined) {
                isGoLogined = true;
                LockPatternUtils.getInstance(mContext).clearLock();
                GoLoginUtil.clearUser(GestureLoginActivity.this);
                SpUtils.setString(SpUtils.Table.CONFIG, SpUtils.Config.UID, "");
                Intent intent = new Intent();
//				intent.putExtra("goClass", MainActivity.class.getName());
                intent.putExtra("isForgetIntent", 1);
                intent.setClass(mContext, LoginActivity.class);
                startActivityForResult(intent, 17);
            }
            SpUtils.setInt(SpUtils.Table.CONFIG, SpUtils.Config.TOTAL_TRY_TIMES, wrongTime);
            userName.setTextColor(getResources().getColor(R.color.theme_color));
            userName.setText("密码错误，还可以再输入" + wrongTime + "次");
            userName.startAnimation(shakeAnim);
        }
    }

    /**
     * 发送获取消息请求
     *
     * @param uid
     */
    public void gainMessage(String uid) {
        int invest_sequence = MsgUtil.getInt(this, MsgUtil.INVEST_SEQUENCE);
        int account_sequence = MsgUtil.getInt(this, MsgUtil.ACCOUNT_SEQUENCE);
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

    private void loadUserImage() {
        ApiService.getAccountSetting(new ProJsonHandler<>(new BaseCallback<AccountSettingJson>() {
            @Override
            protected void onRightData(AccountSettingJson response) {
                AccountSettingData dataObj = response.getData();
                AccountSettingImg avaObj = dataObj.getAva();
                String url = "";
                if (screenWidth < 720) {
                    url = avaObj.getUrl_s100();
                } else {
                    url = avaObj.getUrl_s300();
                }
                Glide.with(mContext)
                        .load(url)
                        .placeholder(R.drawable.gesture_lock_head)
                        .error(R.drawable.gesture_lock_head)
                        .into(userImageView);
            }
        }, mContext), mContext, uid);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void gainMessageSuccess(MessageJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                List<MessageLocalData> list = MsgUtil.convertList(response.getData());
                if (list != null && list.size() > 0) {
                    for (MessageLocalData data : list) {
                        if (data == null) return;
                        int position = -1;
                        if (MsgUtil.INVEST.equals(data.getType())) {
                            MsgUtil.setObj(this, MsgUtil.INVEST, data);
                            MsgUtil.setInt(this, MsgUtil.INVEST_SEQUENCE, data.getSequence());
                            position = 0;
                        } else if (MsgUtil.ACCOUNT.equals(data.getType())) {
                            MsgUtil.setObj(this, MsgUtil.ACCOUNT, data);
                            MsgUtil.setInt(this, MsgUtil.ACCOUNT_SEQUENCE, data.getSequence());
                            position = 1;
                        } else if (MsgUtil.DISCOVERY.equals(data.getType())) {
                            MsgUtil.setObj(this, MsgUtil.DISCOVERY, data);
                            MsgUtil.setInt(this, MsgUtil.DISCOVERY_SEQUENCE, data.getSequence());
                            position = 2;
                        }
                        Intent intent = new Intent();
                        intent.setAction(MsgUtil.MSG_ACTION_REFRESH);
                        intent.putExtra("flag", position);
                        sendBroadcast(intent);
                    }
                }
            }
        }
    }

    @Override
    public void gainMessageFail() {

    }

    @Override
    public void bindUidCidSuccess(BindUidCidJson response) {
        if (null != response) {
            if (response.getBoolen().equals("1")) {
                Log.d("bindUidCidSuccess", "绑定成功");
            }
        }
    }

    @Override
    public void bindUidCidFailed() {

    }

}
