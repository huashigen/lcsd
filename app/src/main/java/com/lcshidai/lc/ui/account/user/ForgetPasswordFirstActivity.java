package com.lcshidai.lc.ui.account.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.CheckPwdMobileCodeImpl;
import com.lcshidai.lc.impl.account.GainCodeImpl;
import com.lcshidai.lc.impl.account.UserRegisterSendcodeImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.CheckPwdMobileCodeData;
import com.lcshidai.lc.model.account.CheckPwdMobileCodeJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.service.account.HttpCheckPwdMobileCodeService;
import com.lcshidai.lc.service.account.HttpGainCodeService;
import com.lcshidai.lc.service.account.HttpUserRegisterSendcodeService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.SmsAutoUtil;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.ppwindow.DialogPopupWindow;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

/**
 * 忘记密码
 *
 * @author 000853
 */
public class ForgetPasswordFirstActivity extends TRJActivity implements UserRegisterSendcodeImpl, CheckPwdMobileCodeImpl, GainCodeImpl {
    HttpUserRegisterSendcodeService hurss;
    HttpCheckPwdMobileCodeService hcpmcs;

    private CustomEditTextLeftIcon edit_account, edit_dynamic_code;
    private View rl_next, btn_option, tv_subtitle;

    private TextView tv_title;
    private Button btn_gain_dn;
    private Button iv_test;

    private TimeCount time;
    com.lcshidai.lc.widget.text.CustomEditTextLeftIcon edit_code;
    private DialogPopupWindow dialogPopupWindow;
    private View mainView;
    HttpGainCodeService hgcs;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_forget_pwd_first);
        hcpmcs = new HttpCheckPwdMobileCodeService(this, this);
        hurss = new HttpUserRegisterSendcodeService(this, this);
        hgcs = new HttpGainCodeService(this, this);
        mainView = findViewById(R.id.absmain);
        dialogPopupWindow = new DialogPopupWindow(this, mainView, null);
        edit_code = (CustomEditTextLeftIcon) findViewById(R.id.edit_code);
        edit_account = (CustomEditTextLeftIcon) findViewById(R.id.edit_account);
        edit_dynamic_code = (CustomEditTextLeftIcon) findViewById(R.id.edit_dynamic_code);
        Drawable draw_mobile_icon = getResources().getDrawable(R.drawable.icon_mobile);
        Drawable draw_dtm_icon = getResources().getDrawable(R.drawable.icon_dtm);
        Drawable draw_pwd_icon = getResources().getDrawable(R.drawable.icon_pwd_me);
        edit_account.setIcon(draw_mobile_icon);
        edit_code.setIcon(draw_dtm_icon);
        edit_dynamic_code.setIcon(draw_pwd_icon);
        rl_next = findViewById(R.id.rl_next);
        btn_gain_dn = (Button) findViewById(R.id.btn_gain_dn);
        iv_test = (Button) findViewById(R.id.iv_test);
        btn_option = findViewById(R.id.btn_option);
        tv_subtitle = findViewById(R.id.tv_subtitle);
        tv_title = (TextView) findViewById(R.id.tv_top_bar_title);

        btn_option.setVisibility(View.INVISIBLE);
        tv_subtitle.setVisibility(View.GONE);

        edit_account.setHint("手机号码");
        edit_code.getET().setHint("验证码");
        edit_dynamic_code.setHint("动态码");
        time = new TimeCount(120000, 1000);//构造CountDownTimer对象

        tv_title.setText("忘记密码");
        rl_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                next();
            }
        });
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_gain_dn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sendDyCode();
                try {
                    SmsAutoUtil.getInstance().startWork(
                            ForgetPasswordFirstActivity.this, new Handler(), edit_dynamic_code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        hgcs.getBitmapData(HttpServiceURL.VERIFY, "", "", "");
        findViewById(R.id.iv_test).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hgcs.getBitmapData(HttpServiceURL.VERIFY, "", "", "");
            }
        });
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    /**
     * 发送动态码请求
     */
    public void sendDyCode() {
        if (edit_code.getEdtText().equals("")) {
            createDialogDismissAuto("请输入验证码");
            return;
        } else if (edit_account.getEdtText().equals("")) {
            createDialogDismissAuto("请输入手机号");
            return;
        }
        hurss.gainUserRegisterSendcode(edit_account.getEdtText(), edit_code.getEdtText(), HttpUserRegisterSendcodeService.FORGET_PWD_URL_FLAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        try {
            SmsAutoUtil.getInstance().stopWork(ForgetPasswordFirstActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void next() {
        hcpmcs.gainCheckPwdMobileCode(edit_account.getEdtText(), edit_dynamic_code.getEdtText());
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            // mBtnGainDn.setText(" 重新获取 ");
            hgcs.getBitmapData(HttpServiceURL.VERIFY, "", "", "");
            edit_code.setText("");
            btn_gain_dn.setText("免费获取动态码");
            btn_gain_dn.setEnabled(true);
            btn_gain_dn.setClickable(true);
            //btn_gain_dn.setBackgroundResource(R.drawable.resend_code_bg);
            btn_gain_dn.setBackgroundColor(getResources().getColor(R.color.color_3));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            btn_gain_dn.setClickable(false);
            //	btn_gain_dn.setBackgroundResource(R.drawable.bg_button_cancel);
            btn_gain_dn.setBackgroundColor(getResources().getColor(R.color.sended_color));
            android.view.ViewGroup.LayoutParams para;
            para = btn_gain_dn.getLayoutParams();
            para.width = iv_test.getWidth();
            para.height = edit_dynamic_code.getHeight();
            btn_gain_dn.setLayoutParams(para);
            btn_gain_dn.setText("(" + millisUntilFinished / 1000 + "秒)重新发送");
        }
    }

    @Override
    public void gainCheckPwdMobileCodesuccess(CheckPwdMobileCodeJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    CheckPwdMobileCodeData jdata = response.getData();
                    //0:未设置		1：已设置
                    String is_id_auth = jdata.getIs_id_auth();
                    String is_sqa = jdata.getIs_sqa();
                    String is_binding_bank = jdata.getIs_binding_bank();

                    Intent intent = new Intent();
                    if ("0".equals(is_id_auth) && "0".equals(is_sqa) && "0".equals(is_binding_bank)) {
                        intent.setClass(ForgetPasswordFirstActivity.this, ForgetPasswordGeneralActivity.class);
                        intent.putExtra("set_type", "direct");
                    } else {
                        intent.setClass(ForgetPasswordFirstActivity.this, ForgetPasswordChannelActivity.class);
                        intent.putExtra("is_id_auth", is_id_auth);
                        intent.putExtra("is_sqa", is_sqa);
                        intent.putExtra("is_binding_bank", is_binding_bank);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    String message = response.getMessage();
                    ToastUtil.showToast(ForgetPasswordFirstActivity.this, message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainCheckPwdMobileCodefail() {

    }


    @Override
    public void getRegisterSmsCodeSuccess(BaseJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    btn_gain_dn.setEnabled(false);
                    time.start();// 开始计时
                    createDialogDismissAuto("动态码已发送");
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    ForgetPasswordFirstActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    createDialogDismissAuto(response.getMessage());
                    hgcs.getBitmapData(HttpServiceURL.VERIFY, "", "", "");
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    ForgetPasswordFirstActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getRegisterSmsCodeFailed() {

    }

    @Override
    public void gainBitmapDatasuccess(byte[] binaryData, String url,
                                      String content, String title, String qrcode) {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
        Drawable drawable = new BitmapDrawable(bitmap);
        findViewById(R.id.iv_test).setBackground(drawable);
    }

    @Override
    public void gainBitmapDatafail() {

    }

}
