package com.lcshidai.lc.ui.finance;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tendcloud.tenddata.TCAgent;
import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.GainInvestConfirmSmsCodeImpl;
import com.lcshidai.lc.impl.account.AccountUserEscrowInfoImp;
import com.lcshidai.lc.impl.finance.FinancePayImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.LocalInvestPayModel;
import com.lcshidai.lc.model.account.UserEscrowInfoData;
import com.lcshidai.lc.model.account.UserEscrowInfoJson;
import com.lcshidai.lc.model.finance.FinanceApplyCashoutJson;
import com.lcshidai.lc.model.finance.FinanceCheckPayPasswordJson;
import com.lcshidai.lc.model.finance.FinanceDoCashJson;
import com.lcshidai.lc.model.finance.FinancePayResultJson;
import com.lcshidai.lc.model.finance.FinanceWithdrawalsMoneyJson;
import com.lcshidai.lc.service.HttpGainInvestConfirmSmsCodeService;
import com.lcshidai.lc.service.account.HttpUserEscrowInfoService;
import com.lcshidai.lc.service.finance.HttpPayService;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.SmsAutoUtil;
import com.lcshidai.lc.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 存管用户短信验证码确认页面
 * Created by RandyZhang on 16/7/8.
 */
public class InvestSmsConfirmActivity extends TRJActivity implements View.OnClickListener, FinancePayImpl, GainInvestConfirmSmsCodeImpl, AccountUserEscrowInfoImp {

    public static final String LOCAL_INVEST_PAY_INFO = "local_invest_pay_info";

    private LocalInvestPayModel localInvestPayModel;
    private int isCollection = 0;
    private boolean isAutoInvestOpen = false;
    private boolean isCg = false;

    private TextView tvInvestAmount;
    private EditText etSmsCode;
    private ImageView ivSmsCodeDel;
    private TextView tvSendSmsCode;
    private TextView tvTips;
//    private TextView tvLeftPhoneChanged;
    private TextView tvConfirm;
    private TextView tvSmsCodeTips;

    private String bankLeftPhone;

    private TimeCount timeCounter;
    private HttpGainInvestConfirmSmsCodeService gainInvestConfirmSmsCodeService = null;
    private HttpPayService payService;
    private HttpUserEscrowInfoService httpUserEscrowInfoService = null;

    private Dialog openAutoInvestRemindDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_sms_confirm);

        localInvestPayModel = (LocalInvestPayModel) getIntent().getSerializableExtra(LOCAL_INVEST_PAY_INFO);
        isCollection = getIntent().getIntExtra(Constants.Project.IS_COLLECTION, 0);
        isAutoInvestOpen = getIntent().getBooleanExtra(Constants.Project.IS_AUTO_INVEST_OPEN, false);
        isCg = getIntent().getBooleanExtra(Constants.Project.IS_CG, false);
        initViews();

        timeCounter = new TimeCount(60000, 1000);
        payService = new HttpPayService(this, this);
        gainInvestConfirmSmsCodeService = new HttpGainInvestConfirmSmsCodeService(this, this);
        httpUserEscrowInfoService = new HttpUserEscrowInfoService(this, this);
        if (null != localInvestPayModel) {
            gainInvestConfirmSmsCodeService.gainInvestConfirmSmsCode(localInvestPayModel);
        }
        httpUserEscrowInfoService.gainUserEscrowInfo();
        showLoadingDialog(mContext, "正在加载", false);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return true;
    }

    private void initViews() {
        ImageButton topBackBtn = (ImageButton) findViewById(R.id.ib_top_bar_back);
        TextView topTitleText = (TextView) findViewById(R.id.tv_top_bar_title);
        tvInvestAmount = (TextView) findViewById(R.id.tv_invest_amount);
        etSmsCode = (EditText) findViewById(R.id.et_sms_code);
        ivSmsCodeDel = (ImageView) findViewById(R.id.iv_sms_code_del);
        tvSendSmsCode = (TextView) findViewById(R.id.tv_send_sms_code);
        tvTips = (TextView) findViewById(R.id.tv_tips);
//        tvLeftPhoneChanged = (TextView) findViewById(R.id.tv_left_phone_number_changed);
        tvSmsCodeTips = (TextView) findViewById(R.id.tv_sms_code_tips);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);

        if (null != localInvestPayModel) {
            if (!CommonUtil.isNullOrEmpty(localInvestPayModel.getInvestAmount())) {
                String investMoney = localInvestPayModel.getInvestAmount().replace(",", "");
                double investMoneyValue = Double.valueOf(investMoney);
                tvInvestAmount.setText(String.format("%.2f", investMoneyValue));
            }
        }

        topTitleText.setText("投资");
        tvConfirm.setText("确定投资");
        etSmsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etSmsCode.getText().toString().length() > 0) {
                    ivSmsCodeDel.setVisibility(View.VISIBLE);
                    tvConfirm.setBackgroundResource(R.drawable.bg_button_clickable_true);
                } else {
                    ivSmsCodeDel.setVisibility(View.GONE);
                    tvConfirm.setBackgroundResource(R.drawable.bg_button_clickable_false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        topBackBtn.setOnClickListener(this);
        ivSmsCodeDel.setOnClickListener(this);
        tvSendSmsCode.setOnClickListener(this);
//        tvLeftPhoneChanged.setOnClickListener(this);
        tvSmsCodeTips.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    /**
     * 初始化开通自动投标提醒弹框
     */
    private void initAutoInvestRemindDialog() {
        if (openAutoInvestRemindDialog == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.layout_dialog_auto_invest_remind, null);
            TextView tvAutoInvestLabelOne = (TextView) view.findViewById(R.id.tv_auto_invest_label_one);
            TextView tvAutoInvestLabelTwo = (TextView) view.findViewById(R.id.tv_auto_invest_label_two);
            TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
            TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);

            tvAutoInvestLabelTwo.setText(Html.fromHtml(getString(R.string.dialog_auto_invest_label_two)));

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAutoInvestRemindDialog.dismiss();
                }
            });

            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    String loadUrl;
                    //  去开通
                    loadUrl = LCHttpClient.BASE_WAP_HEAD + "/#/openAutoBiding?phase=one ";
                    intent = new Intent(mContext, MainWebActivity.class);
                    intent.putExtra("web_url", loadUrl);
                    intent.putExtra("title", "自动投标授权");
                    startActivity(intent);
                    openAutoInvestRemindDialog.dismiss();
                    finish();
                }
            });

            openAutoInvestRemindDialog = new Dialog(this, R.style.style_loading_dialog);
            openAutoInvestRemindDialog.setContentView(view);
            openAutoInvestRemindDialog.setCancelable(true);
        }
    }

    @Override
    public void buyPiSuccess(FinancePayResultJson result) {
        hideLoadingDialog();
        if (result != null) {
            if (result.getBoolen() != null && result.getBoolen().equals("1")) {
                Intent intent = new Intent();
                intent.putExtra("FinancePayResultData", result.getData());
                intent.setClass(this, InvestSuccessActivity.class);
                intent.putExtra(Constants.Project.IS_AUTO_INVEST_OPEN, isAutoInvestOpen);
                intent.putExtra(Constants.Project.IS_CG, isCg);
                startActivity(intent);
                Map<String, String> map = new HashMap<String, String>();
                map.put("userName", MemorySave.MS.userInfo.uname);
                if (localInvestPayModel != null)
                    map.put("money", localInvestPayModel.getInvestAmount());
                TCAgent.onEvent(this, Constants.AGENT_PAYBUY, MemorySave.MS.userInfo.uname, map);
                finish();
            }
        }
    }

    @Override
    public void buyPiFail(String message) {
        hideLoadingDialog();
        if (!CommonUtil.isNullOrEmpty(message)) {
            ToastUtil.showToast((TRJActivity) mContext, message);
        } else {
            ToastUtil.showToast(mContext, "投资失败");
        }
    }

    @Override
    public void buyPiFSuccess(FinanceDoCashJson result) {

    }

    @Override
    public void buyPiFFail() {

    }

    @Override
    public void loadCashoutFeeDataSuccess(FinanceWithdrawalsMoneyJson result) {

    }

    @Override
    public void loadCashoutFeeDataFail() {

    }

    @Override
    public void applyCashoutSuccess(FinanceApplyCashoutJson response) {

    }

    @Override
    public void applyCashoutFail() {

    }

    @Override
    public void payPswCheckSuccess(FinanceCheckPayPasswordJson response) {

    }

    @Override
    public void payPswCheckFailed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;

            case R.id.iv_sms_code_del:
                etSmsCode.setText("");
                break;

            case R.id.tv_send_sms_code:
                // 发送短信验证码
                if (null != localInvestPayModel)
                    gainInvestConfirmSmsCodeService.gainInvestConfirmSmsCode(localInvestPayModel);
                break;

//            case R.id.tv_left_phone_number_changed:
//                // 预留手机号已更改处理
//                Intent intent = new Intent(this, ModifyPreLeftPhoneNumberActivity.class);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.tv_sms_code_tips:
                if (openAutoInvestRemindDialog != null) {
                    openAutoInvestRemindDialog.show();
                } else {
                    initAutoInvestRemindDialog();
                    openAutoInvestRemindDialog.show();
                }
                break;
            case R.id.tv_confirm:
                // 带验证码的投资验证
                if (null != localInvestPayModel) {
                    localInvestPayModel.setInvestMobileCode(etSmsCode.getText().toString());
                    if (isCollection == 1) {
                        payService.doCollectionPrjInvest(localInvestPayModel);
                    } else {
                        payService.doInvest(localInvestPayModel);
                    }
                    showLoadingDialog(mContext, "正在加载", true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void gainInvestConfirmSmsCodeSuccess(BaseJson response) {
        // 发送短信验证码成功
        if (response != null) {
            try {
                SmsAutoUtil.getInstance().startWork(this, new Handler(), etSmsCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            timeCounter.start();// 开始计时
            String formatLeftPhone = "";
            // 获取银行预留手机号
            if (!CommonUtil.isNullOrEmpty(bankLeftPhone)) {
                formatLeftPhone = CommonUtil.formatPhone(bankLeftPhone);
            } else {
                formatLeftPhone = CommonUtil.formatPhone(GoLoginUtil.getUserName(mContext));
            }
            tvTips.setText("短信动态码由浙商银行发送至您的预留手机号" + formatLeftPhone + "上，请耐心等待。");
        }
    }

    @Override
    public void gainInvestConfirmSmsCodeFailed(BaseJson errorResponse) {
        // 发送短信验证码失败
    }

    @Override
    public void gainUserEscrowInfoSuccess(UserEscrowInfoJson response) {
        if (response != null) {
            // if loading is shown, hide it
            hideLoadingDialog();
            if (response.getBoolen().equals("1")) {
                UserEscrowInfoData data = response.getData();
                bankLeftPhone = data.getEcw_mobile();
            } else {
                String message = response.getMessage();
                ToastUtil.showToast(this, message);
            }
        }
    }

    @Override
    public void gainUserEscrowInfoFail() {
        hideLoadingDialog();
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
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