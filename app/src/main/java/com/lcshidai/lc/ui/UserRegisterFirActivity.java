package com.lcshidai.lc.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.RegistHongbaoImpl;
import com.lcshidai.lc.impl.account.UserRegisterFirImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.RegistHongbaoData;
import com.lcshidai.lc.model.account.RegistHongbaoJson;
import com.lcshidai.lc.service.account.HttpRegistHongbaoService;
import com.lcshidai.lc.service.account.HttpUserRegisterFirService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.AnieLayout;
import com.lcshidai.lc.widget.text.CustomEditTextMark;

/**
 * 注册页面
 */
public class UserRegisterFirActivity extends TRJActivity implements UserRegisterFirImpl, RegistHongbaoImpl {
    HttpUserRegisterFirService hurfs;
    HttpRegistHongbaoService hrhs;
    private CustomEditTextMark mEditMobile;
    private Button mBtnOption;
    ImageButton mBtnBack;
    private TextView mTvTitle, mTvSubtitle, tologin;
    private View rl_submit;
    private AnieLayout ll_pay_pwd_keyboard;
    private TextView ib_hidden, ib_delete, ib_0, ib_1, ib_2, ib_3, ib_4, ib_5, ib_6, ib_7, ib_8, ib_9;
    private TextView tv_show1, tv_reg_red;
    private int pwdLenth = 0;
    private int res_pop = 0;
    private String inputPwd = "";
    private String mobile = "";

    private boolean isToMain = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_fir);
        Intent args = getIntent();
        if (null != args) {
            res_pop = args.getIntExtra("res_pop", 0);
            isToMain = args.getBooleanExtra("is_back_to_main", false);
        }
        hurfs = new HttpUserRegisterFirService(this, this);
        hrhs = new HttpRegistHongbaoService(this, this);
        getViews();
        setViews();
        getRegistHongbao();
        showKeyboard();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    public void getViews() {
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvSubtitle = (TextView) findViewById(R.id.tv_subtitle);
        mEditMobile = (CustomEditTextMark) findViewById(R.id.edit_mobile);
        rl_submit = mEditMobile.getrl_submit();
        tologin = mEditMobile.gettologin();
        rl_submit.setVisibility(View.VISIBLE);
        mBtnBack = (ImageButton) findViewById(R.id.btn_back);
        mBtnOption = (Button) findViewById(R.id.btn_option);

        tv_show1 = (TextView) findViewById(R.id.tv_show1);
        tv_reg_red = (TextView) findViewById(R.id.tv_reg_red);
        ib_hidden = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_hidden);
        ib_delete = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_delete);
        ib_0 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_0);
        ib_1 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_1);
        ib_2 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_2);
        ib_3 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_3);
        ib_4 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_4);
        ib_5 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_5);
        ib_6 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_6);
        ib_7 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_7);
        ib_8 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_8);
        ib_9 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_9);
        ll_pay_pwd_keyboard = (AnieLayout) findViewById(R.id.pay_pwd_ll_keyboard);

    }

    public void setViews() {
        mTvTitle.setText("注册");
        mTvSubtitle.setVisibility(View.GONE);
        mBtnOption.setVisibility(View.INVISIBLE);
        mEditMobile.setText("手机号码");
        mEditMobile.setHint("请输入11位手机号码");
        mEditMobile.setInputType(InputType.TYPE_NULL);
        String value = "注册即送";
        //mEditMobile.setIcon(draw_mobile_icon);
        SpannableString tran_ss = new SpannableString(value);
        tran_ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.home_text)), 2, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_show1.setText(tran_ss);
        mBtnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isToMain) {
                    startActivity(new Intent(mContext, MainActivity.class));
                } else {
                    setResult(20);
                }
                finish();
            }
        });

        rl_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = mEditMobile.getEdtText();
                if (!mobile.equals("")) {
                    IsMobileReg(mobile);
                } else {
                    createDialogDismissAuto("手机号不能为空");
                }
            }
        });

        tologin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });
        ib_hidden.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//				ll_pay_pwd_keyboard.setVisibility(View.GONE);
                if (android.os.Build.VERSION.SDK_INT < 12) {
                    ll_pay_pwd_keyboard.setVisibility(View.GONE);
                    return;
                }

                final ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(
                        ll_pay_pwd_keyboard, "YFraction", 0, (float) 1.5);
                movingFragmentRotator.setDuration(200);
                movingFragmentRotator.start();
                movingFragmentRotator.addListener(new AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animator arg0) {
                        ll_pay_pwd_keyboard.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator arg0) {
                    }
                });
            }
        });

        mEditMobile.getDeleteBT().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //	mEditMobile.setEdtText("");
                mEditMobile.setTextET("");
                inputPwd = "";
                pwdLenth = 0;
            }
        });

        mEditMobile.getEditTextET().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard();
            }
        });
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
    }

    class BtnClick implements OnClickListener {

        int value;

        public BtnClick(int value) {
            this.value = value;
        }

        @Override
        public void onClick(View v) {
            if (pwdLenth == 11 && value != -1) {
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
                    pwdLenth++;
                    inputPwd += String.valueOf(value);
                    mEditMobile.setEdtText(inputPwd);
                    break;
                case -1:
                    if (pwdLenth == 0) {
                        return;
                    } else {
                        pwdLenth--;
                        inputPwd = inputPwd.substring(0, inputPwd.length() - 1);
                        mEditMobile.setEdtText(inputPwd);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isToMain) {
                startActivity(new Intent(mContext, MainActivity.class));
            } else {
                setResult(20);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void doFinish() {
        setResult(20);
        super.doFinish();
    }

    private void showKeyboard() {
        int visibility = ll_pay_pwd_keyboard.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            if (android.os.Build.VERSION.SDK_INT < 12) {
                ll_pay_pwd_keyboard.setVisibility(View.VISIBLE);
                return;
            }
            ll_pay_pwd_keyboard.setVisibility(View.INVISIBLE);
            final ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(ll_pay_pwd_keyboard, "YFraction", 1, 0);
            movingFragmentRotator.setDuration(200);
            movingFragmentRotator.addListener(new AnimatorListener() {
                @Override
                public void onAnimationStart(Animator arg0) {
                    ll_pay_pwd_keyboard.setVisibility(View.VISIBLE);
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
        }
    }

    /**
     * 手机号码是否注册
     */
    public void IsMobileReg(final String mobile) {
        showLoadingDialog(mContext, "数据加载中...", true);
        rl_submit.setEnabled(false);
        hurfs.gainUserRegisterFir(mobile);
    }

    private void getRegistHongbao() {
        hrhs.gainRegistHongbao();
    }

    @Override
    public void gainRegistHongbaosuccess(RegistHongbaoJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                RegistHongbaoData dataObj = null;
                if ("1".equals(boolen)) {
                    dataObj = response.getData();
                    tv_reg_red.setText(TextUtils.isEmpty(dataObj.getNum()) ? "0" : dataObj.getNum());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainRegistHongbaofail() {

    }

    @Override
    public void gainUserRegisterFirsuccess(BaseJson response) {
        try {
            rl_submit.setEnabled(true);
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    Intent intent = new Intent(mContext, UserRegisterSecActivity.class);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("res_pop", res_pop);
                    String goClass = "";
                    if (getIntent() != null) {
                        goClass = getIntent().getStringExtra("goClass");
                    }
                    if (goClass != null && !goClass.equals("")) {
                        intent.putExtra("goClass", goClass);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    String message = response.getMessage();
                    createDialogDismissAuto(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            hideLoadingDialog();
        }
    }

    @Override
    public void gainUserRegisterFirfail() {
        rl_submit.setEnabled(true);
        ToastUtil.showToast((TRJActivity) mContext, "网络不给力!");
        hideLoadingDialog();
    }
}