package com.lcshidai.lc.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.ModifyLeftPhoneImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpModifyLeftPhoneService;
import com.lcshidai.lc.ui.account.cashout.CashOutActivity;
import com.lcshidai.lc.ui.account.cashout.EcwSmsActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.finance.InvestSmsConfirmActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.SmsAutoUtil;
import com.lcshidai.lc.utils.ToastUtil;

/**
 * 修改预留手机号码页面
 * <p/>
 * Created by RandyZhang on 16/7/6.
 */
public class ModifyPreLeftPhoneNumberActivity extends TRJActivity implements View.OnClickListener, ModifyLeftPhoneImpl {
    public static String ENTRANCE = "entrance";
    private EditText etOriginPhone;
    private ImageView ivOriginPhoneDel;
    private EditText etNowPhone;
    private ImageView ivNowPhoneDel;
    private EditText etSmsCode;
    private ImageView ivSmsCodeDel;
    private TextView tvSendSmsCode;
    private TextView tvConfirm;

    private String originPhoneStr = "", nowPhoneStr = "", smsStr = "";

    private TimeCount timeCounter;

    private HttpModifyLeftPhoneService httpModifyLeftPhoneService = null;
    private Dialog successDialog = null;
    private String entrance;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pre_left_phone_number);
        entrance = getIntent().getStringExtra(ENTRANCE);
        timeCounter = new TimeCount(60000, 1000);// 构造CountDownTimer对象        // 发送短信接口
        httpModifyLeftPhoneService = new HttpModifyLeftPhoneService(this, this);
        initViews();

    }

    private void initViews() {
        ImageButton topBackBtn = (ImageButton) findViewById(R.id.ib_top_bar_back);
        TextView topTitleText = (TextView) findViewById(R.id.tv_top_bar_title);
        etOriginPhone = (EditText) findViewById(R.id.et_origin_phone);
        ivOriginPhoneDel = (ImageView) findViewById(R.id.iv_origin_phone_del);
        etNowPhone = (EditText) findViewById(R.id.et_now_phone);
        ivNowPhoneDel = (ImageView) findViewById(R.id.iv_now_phone_del);
        etSmsCode = (EditText) findViewById(R.id.et_sms_code);
        ivSmsCodeDel = (ImageView) findViewById(R.id.iv_sms_code_del);
        tvSendSmsCode = (TextView) findViewById(R.id.tv_send_sms_code);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);

        topBackBtn.setOnClickListener(this);
        topTitleText.setText("修改预留手机号");
        etOriginPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etOriginPhone.getText().toString().length() > 0) {
                    ivOriginPhoneDel.setVisibility(View.VISIBLE);
                } else {
                    ivOriginPhoneDel.setVisibility(View.GONE);
                }
                // 控制确定按钮的背景
                if (isChangeConfirmButtonBg(etOriginPhone.getText().toString(), etNowPhone.getText().toString(), etSmsCode.getText().toString())) {
                    tvConfirm.setBackgroundResource(R.drawable.bg_button_clickable_true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivOriginPhoneDel.setOnClickListener(this);
        etNowPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etNowPhone.getText().toString().length() > 0) {
                    ivNowPhoneDel.setVisibility(View.VISIBLE);
                } else {
                    ivNowPhoneDel.setVisibility(View.GONE);
                }
                // 控制确定按钮的背景
                if (isChangeConfirmButtonBg(etOriginPhone.getText().toString(), etNowPhone.getText().toString(), etSmsCode.getText().toString())) {
                    tvConfirm.setBackgroundResource(R.drawable.bg_button_clickable_true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivNowPhoneDel.setOnClickListener(this);
        etSmsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etSmsCode.getText().toString().length() > 0) {
                    ivSmsCodeDel.setVisibility(View.VISIBLE);
                } else {
                    ivSmsCodeDel.setVisibility(View.GONE);
                }
                // 控制确定按钮的背景
                if (isChangeConfirmButtonBg(etOriginPhone.getText().toString(), etNowPhone.getText().toString(), etSmsCode.getText().toString())) {
                    tvConfirm.setBackgroundResource(R.drawable.btn_gradient_selector);
                } else {
                    tvConfirm.setBackgroundResource(R.drawable.bg_button_clickable_false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivSmsCodeDel.setOnClickListener(this);

        tvSendSmsCode.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvConfirm.setText("确定");
    }

    /**
     * 是否改变确认按钮背景
     *
     * @param originPhone 原手机号
     * @param nowPhone    现手机号
     * @param smsCode     短信验证码
     * @return true if need change
     */
    private boolean isChangeConfirmButtonBg(String originPhone, String nowPhone, String smsCode) {
        return !CommonUtil.isNullOrEmpty(originPhone) && !CommonUtil.isNullOrEmpty(nowPhone) && !CommonUtil.isNullOrEmpty(smsCode);
    }

    @Override
    public void onClick(View v) {
        originPhoneStr = etOriginPhone.getText().toString();
        nowPhoneStr = etNowPhone.getText().toString();
        smsStr = etSmsCode.getText().toString();
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;
            case R.id.iv_origin_phone_del:
                etOriginPhone.setText("");
                break;
            case R.id.iv_now_phone_del:
                etNowPhone.setText("");
                break;
            case R.id.iv_sms_code_del:
                etSmsCode.setText("");
                break;
            case R.id.tv_send_sms_code:
                // 调用发送短信接口发送短信
                if (CommonUtil.isNullOrEmpty(originPhoneStr)) {
                    showToast("原银行预留手机号为空");
                    return;
                }

                if (CommonUtil.isNullOrEmpty(nowPhoneStr)) {
                    showToast("现银行预留手机号为空");
                    return;
                }
                if (originPhoneStr.equals(nowPhoneStr)) {
                    showToast("新旧银行预留手机号相同");
                    return;
                } else {
                    httpModifyLeftPhoneService.gainModifyLeftPhoneSmsCode(originPhoneStr, nowPhoneStr);
                }
                break;
            case R.id.tv_confirm:
                if (validCheck()) {
                    httpModifyLeftPhoneService.modifyLeftPhone(originPhoneStr, nowPhoneStr, smsStr);
                    // 显示加载框
                    showLoadingDialog(this, "正在加载", true);
                }

                break;
            default:

                break;
        }
    }

    private boolean validCheck() {
        return !CommonUtil.isNullOrEmpty(originPhoneStr) && !CommonUtil.isNullOrEmpty(nowPhoneStr) && !CommonUtil.isNullOrEmpty(smsStr);

    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void modifyLeftPhoneSuccess(BaseJson response) {
        // 请求成功，隐藏加载框
        hideLoadingDialog();
        if (null != response) {
            if (response.getBoolen().equals("1")) {

                // 显示修改成功对话框
                if (successDialog == null) {
                    initSuccessDialog(this, true);
                    successDialog.show();
                } else {
                    successDialog.show();
                }
            } else {
                if (!CommonUtil.isNullOrEmpty(response.getMessage())) {
                    ToastUtil.showToast((TRJActivity) mContext, response.getMessage());
                }
            }
        }
    }

    @Override
    public void modifyLeftPhoneFailed(BaseJson errorResponse) {
        // 请求失败，隐藏加载框，显示失败原因 errorResponse.getMessage()
        hideLoadingDialog();
    }

    @Override
    public void gainModifyLeftPhoneSmsCodeSuccess(BaseJson response) {
        try {
            tvSendSmsCode.setEnabled(true);
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    tvSendSmsCode.setEnabled(false);
                    tvSendSmsCode.setTextColor(getResources().getColor(R.color.sended_color));
                    timeCounter.start();// 开始计时
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    if (!CommonUtil.isNullOrEmpty(response.getMessage())) {
                        ToastUtil.showToast((TRJActivity) mContext, response.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainModifyLeftPhoneSmsCodeFailed(BaseJson errorResponse) {

    }

    private void initSuccessDialog(Context context, boolean cancelAble) {
        successDialog = new Dialog(context, R.style.style_loading_dialog);// 创建自定义样式dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_modify_left_phone_success, null);// 得到加载view
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);// 提示文字
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successDialog.dismiss();
                // 进行不同的跳转操作
                Intent intent = new Intent();
                if (!CommonUtil.isNullOrEmpty(entrance)) {
                    if (entrance.equals(CunGuanAccountActivity.class.getSimpleName())) {
                        // 存管账户
                        finish();
                    } else if (entrance.equals(EcwSmsActivity.class.getSimpleName())) {
                        // 提现，提现确认页面
                        intent.setClass(mContext, CashOutActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (entrance.equals(InvestSmsConfirmActivity.class.getSimpleName())) {
                        // 投资详情，投资确认页面
                        finish();
                    }
                }
                // 记录手机号
                GoLoginUtil.saveBankLeftPhone(etNowPhone.getText().toString(), mContext);
                finish();

            }
        });// 设置加载信息
        successDialog.setContentView(view);
        successDialog.setCancelable(cancelAble);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = successDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.8); //设置宽度
        successDialog.getWindow().setAttributes(lp);
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            // mBtnGainDn.setText(" 重新获取 ");
//            hgcs.getBitmapData(HttpServiceURL.VERIFY, false, "", "", "");
            etSmsCode.setText("");
            tvSendSmsCode.setText("重发验证码");
            tvSendSmsCode.setEnabled(true);
            tvSendSmsCode.setClickable(true);
            //bt_gain_code.setBackgroundResource(R.drawable.resend_code_bg);
            tvSendSmsCode.setTextColor(getResources().getColor(R.color.theme_color));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            tvSendSmsCode.setClickable(false);
            //	bt_gain_code.setBackgroundResource(R.drawable.bg_button_cancel);
            tvSendSmsCode.setTextColor(getResources().getColor(R.color.theme_color));
            tvSendSmsCode.setText(millisUntilFinished / 1000 + "s");
        }
    }

    @Override
    protected void onDestroy() {
        MemorySave.MS.mLoginToRegLock = false;
        try {
            SmsAutoUtil.getInstance().stopWork(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}