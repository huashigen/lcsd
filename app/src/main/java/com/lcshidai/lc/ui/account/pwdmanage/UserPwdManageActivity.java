package com.lcshidai.lc.ui.account.pwdmanage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.AccountSettingImpl;
import com.lcshidai.lc.model.account.AccountSettingData;
import com.lcshidai.lc.model.account.AccountSettingJson;
import com.lcshidai.lc.service.account.HttpAccountSettingService;
import com.lcshidai.lc.ui.account.SafeQuestionActivity;
import com.lcshidai.lc.ui.account.UserPayPwdFirstSetActivity;
import com.lcshidai.lc.ui.account.UserPwdUpdaterActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.SpUtils;

/**
 * 用户密码管理
 */
public class UserPwdManageActivity extends TRJActivity implements AccountSettingImpl {
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    private LinearLayout ll_login_pwd, ll_pay_pwd, ll_gesture_pwd, ll_set_sqa;
    private TextView tv_pay_pwd, tv_gesture_pwd, tv_question_pwd;
    private String is_paypwd_mobile_set, is_set_sqa;
    private HttpAccountSettingService accountSettingService;
    private boolean isNeedRequest = false;// 是否需要请求网络

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pwd_manage);
//        个人中心密码管理  修改
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {// bundle 不为空，有数据过来，无需刷新
            is_paypwd_mobile_set = bundle.getString("is_paypwd_mobile_set");
            is_set_sqa = bundle.getString("is_set_sqa");
            isNeedRequest = false;
        } else {// 为空，无数据，要刷新
            isNeedRequest = true;
        }
        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(isNeedRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isNeedRequest = true;// 进入后台后，重新唤醒是需要重新刷新，放置其它地方有更改导致数据不同步
    }

    /**
     * 加载数据
     */
    private void loadData(boolean isNeedRequest) {
        if (isNeedRequest) {
            if (accountSettingService != null) {
                accountSettingService.gainAccountSetting("");
            } else {
                accountSettingService = new HttpAccountSettingService(this, this);
                accountSettingService.gainAccountSetting("");
            }
            showLoadingDialog(this, "正在加载", false);
        }
    }

    private void initView() {
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("密码管理");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        ll_login_pwd = (LinearLayout) findViewById(R.id.ll_login_pwd);
        ll_pay_pwd = (LinearLayout) findViewById(R.id.ll_pay_pwd);
        ll_gesture_pwd = (LinearLayout) findViewById(R.id.ll_gesture_pwd);
        ll_set_sqa = (LinearLayout) findViewById(R.id.ll_set_sqa);
        tv_pay_pwd = (TextView) findViewById(R.id.tv_pay_pwd);
        tv_gesture_pwd = (TextView) findViewById(R.id.tv_gesture_pwd);
        tv_question_pwd = (TextView) findViewById(R.id.tv_question_pwd);

        ll_login_pwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(mContext, UserPwdUpdaterActivity.class);
                intent.putExtra("mode", 1);
                intent.putExtra("title", "修改登录密码");
                startActivity(intent);
            }
        });
        ll_pay_pwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (is_paypwd_mobile_set.equals("1")) {
                    intent.setClass(mContext, UserMobilePayPwdActivity.class);
                } else {
                    intent.setClass(mContext, UserPayPwdFirstSetActivity.class);
                    intent.putExtra("from_activity", 1);
                }
                startActivity(intent);
            }
        });
        ll_gesture_pwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GesturePwdActivity.class);
                startActivity(intent);
            }
        });

        ll_set_sqa.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SafeQuestionActivity.class);
                startActivityForResult(intent, 17);
            }
        });

        textSet();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 17 && resultCode == 18) {
            is_set_sqa = "1";
            if (!TextUtils.isEmpty(is_set_sqa) && "1".endsWith(is_set_sqa)) {
                tv_question_pwd.setText("已设置");
                tv_question_pwd.setTextColor(getResources().getColor(R.color.theme_color));
            } else {
                tv_question_pwd.setText("未设置");
                tv_question_pwd.setTextColor(getResources().getColor(R.color.saft));
            }
        }
    }

    @Override
    public void gainAccountSettingSuccess(AccountSettingJson response) {
        if (null != response) {
            hideLoadingDialog();
            if (response.getBoolen().equals("1")) {
                AccountSettingData accountSettingData = response.getData();
                is_paypwd_mobile_set = accountSettingData.getIs_paypwd_mobile_set();
                is_set_sqa = accountSettingData.getIs_set_sqa();
//                if (is_paypwd_mobile_set.equals("1")) {
//                    tv_pay_pwd.setText("已设置");
//                    tv_pay_pwd.setTextColor(this.getResources().getColor(R.color.saft));
//                    GoLoginUtil.setPayPswSetFlag(1, mContext);
//                } else {
//                    tv_pay_pwd.setText("未设置");
//                    tv_pay_pwd.setTextColor(this.getResources().getColor(R.color.login_orange));
//                    GoLoginUtil.setPayPswSetFlag(0, mContext);
//                }
//                boolean isGestureOpen = SpUtils.getBoolean(SpUtils.Table.CONFIG,
//                        SpUtils.Config.IS_GESTURE_OPEN, false);
//                if (isGestureOpen) {
//                    tv_gesture_pwd.setText("已开启");
//                    tv_gesture_pwd.setTextColor(this.getResources().getColor(R.color.saft));
//                } else {
//                    tv_gesture_pwd.setText("未开启");
//                    tv_gesture_pwd.setTextColor(this.getResources().getColor(R.color.login_orange));
//                }
//                if (!TextUtils.isEmpty(is_set_sqa) && "1".endsWith(is_set_sqa)) {
//                    tv_question_pwd.setText("已设置");
//                    tv_question_pwd.setTextColor(this.getResources().getColor(R.color.saft));
//                } else {
//                    tv_question_pwd.setText("未设置");
//                    tv_question_pwd.setTextColor(this.getResources().getColor(R.color.login_orange));
//                }

                textSet();
            }
        }
    }
    //        个人中心密码管理  设置与未设置
    private void textSet(){
        if (is_paypwd_mobile_set.equals("1")) {
            tv_pay_pwd.setText("已设置");
            tv_pay_pwd.setTextColor(this.getResources().getColor(R.color.saft));
            GoLoginUtil.setPayPswSetFlag(1, mContext);
        } else {
            tv_pay_pwd.setText("未设置");
            tv_pay_pwd.setTextColor(this.getResources().getColor(R.color.login_orange));
            GoLoginUtil.setPayPswSetFlag(0, mContext);
        }
        boolean isGestureOpen = SpUtils.getBoolean(SpUtils.Table.CONFIG,
                SpUtils.Config.IS_GESTURE_OPEN, false);
        if (isGestureOpen) {
            tv_gesture_pwd.setText("已开启");
            tv_gesture_pwd.setTextColor(this.getResources().getColor(R.color.saft));
        } else {
            tv_gesture_pwd.setText("未开启");
            tv_gesture_pwd.setTextColor(this.getResources().getColor(R.color.login_orange));
        }
        if (!TextUtils.isEmpty(is_set_sqa) && "1".endsWith(is_set_sqa)) {
            tv_question_pwd.setText("已设置");
            tv_question_pwd.setTextColor(this.getResources().getColor(R.color.saft));
        } else {
            tv_question_pwd.setText("未设置");
            tv_question_pwd.setTextColor(this.getResources().getColor(R.color.login_orange));
        }
    }

    @Override
    public void gainAccountSettingFail() {
        hideLoadingDialog();
    }
}
