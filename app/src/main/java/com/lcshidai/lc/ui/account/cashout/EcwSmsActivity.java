package com.lcshidai.lc.ui.account.cashout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.AccountCashOutConfirmImp;
import com.lcshidai.lc.impl.account.AccountEcwSmsImp;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.account.HttpCashOutConfirmService;
import com.lcshidai.lc.service.account.HttpEcwSmsService;
import com.lcshidai.lc.ui.base.TRJActivity;

public class EcwSmsActivity extends TRJActivity implements View.OnClickListener, AccountEcwSmsImp, AccountCashOutConfirmImp {
    private ImageButton top_back_btn;
    private TextView top_title_text;
    private TextView tv_invest_amount;
    private EditText et_sms_code;
    private TextView tv_send_sms_code;
    private TextView tv_tips;
    private TextView tv_confirm;
//    private TextView tv_left_phone_number_changed;
    private View mProgressBar;

    private HttpEcwSmsService mHttpEcwSmsService;
    private HttpCashOutConfirmService mHttpCashOutConfirmService;

    private String amount;
    private String pay_wd;
    private String ecw_mobile;

    private TimeCount mTimeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecw_sms);

        amount = getIntent().getStringExtra("amount");
        pay_wd = getIntent().getStringExtra("pay_wd");
        ecw_mobile = getIntent().getStringExtra("ecw_mobile");

        mTimeCount = new TimeCount(60000, 1000);

        mProgressBar = findViewById(R.id.progressContainer);
        mProgressBar.setVisibility(View.GONE);

        top_back_btn = (ImageButton) findViewById(R.id.ib_top_bar_back);
        top_back_btn.setOnClickListener(this);
        top_title_text = (TextView) findViewById(R.id.tv_top_bar_title);
        top_title_text.setText("提现");

        tv_invest_amount = (TextView) findViewById(R.id.tv_invest_amount);
        double amountValue = Double.valueOf(amount);
        tv_invest_amount.setText(String.format("%.2f", amountValue));

        et_sms_code = (EditText) findViewById(R.id.et_sms_code);
        et_sms_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et_sms_code.getText() != null){
                    if(et_sms_code.getText().length() > 0){
                        tv_confirm.setBackgroundResource(R.drawable.feedback_submit_bg_xml);
                        tv_confirm.setClickable(true);
                    }else{
                        tv_confirm.setBackgroundResource(R.drawable.bg_button_clickable_false);
                        tv_confirm.setClickable(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 获取编辑框焦点
        et_sms_code.requestFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //打开软键盘
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_sms_code, 0);
            }
        }, 500);

        tv_send_sms_code = (TextView) findViewById(R.id.tv_send_sms_code);
        tv_send_sms_code.setOnClickListener(this);

        tv_tips = (TextView) findViewById(R.id.tv_tips);
        tv_tips.setText(String.format("短信动态码由浙商银行发送至您的预留手机号\n%s上，请耐心等待。", ecw_mobile));

        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(this);
        tv_confirm.setClickable(false);

//        tv_left_phone_number_changed = (TextView) findViewById(tv_left_phone_number_changed);
//        tv_left_phone_number_changed.setOnClickListener(this);

        mHttpEcwSmsService = new HttpEcwSmsService(this, this);
        mHttpCashOutConfirmService = new HttpCashOutConfirmService(this, this);

        mHttpEcwSmsService.gainEcwSmsCode(amount,"1");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_top_bar_back:
                finish();
                break;
            case R.id.tv_send_sms_code:
                mHttpEcwSmsService.gainEcwSmsCode(amount,"1");
                tv_send_sms_code.setClickable(false);
                break;
//            case R.id.tv_left_phone_number_changed:
//                Intent intent = new Intent(this, ModifyPreLeftPhoneNumberActivity.class);
//                intent.putExtra(ModifyPreLeftPhoneNumberActivity.ENTRANCE, EcwSmsActivity.class.getSimpleName());
//                startActivity(intent);
//                break;
            case R.id.tv_confirm:
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                cashOut();
                break;
            default:
                break;
        }
    }

    private void cashOut(){
        String mobileCode = et_sms_code.getText().toString();
        if(mobileCode != null){
            mHttpCashOutConfirmService.cashOutConfirm(amount, mobileCode, pay_wd);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return true;
    }

    @Override
    public void gainEcwSmsCodeSuccess(BaseJson response) {
        if(response != null){
            if (response.getBoolen().equals("1")){
//                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                mTimeCount.start();// 开始计时
            }
        }
    }

    @Override
    public void gainEcwSmsCodeFail() {

    }

    @Override
    public void cashOutSuccess(BaseJson response) {
        mProgressBar.setVisibility(View.GONE);
        if(response != null){
            if(response.getBoolen().equals("1")){
//                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CashOutSuccessActivity.class);
                intent.putExtra("amount", amount);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void cashOutFail(int statusCode) {
        mProgressBar.setVisibility(View.GONE);
        if(statusCode == 0){
            Toast.makeText(this, "网络不佳", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this, CashOutFailActivity.class);
            intent.putExtra("code", statusCode);
            startActivity(intent);
            finish();
        }
    }

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * Callback fired on regular interval.
         *
         * @param millisUntilFinished The amount of time until finished.
         */
        @Override
        public void onTick(long millisUntilFinished) {
            tv_send_sms_code.setText(millisUntilFinished / 1000 + "s");
            tv_send_sms_code.setClickable(false);
        }

        /**
         * Callback fired when the time is up.
         */
        @Override
        public void onFinish() {
            tv_send_sms_code.setText("重新发送");
            tv_send_sms_code.setClickable(true);
        }
    }
}
