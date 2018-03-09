package com.lcshidai.lc.ui.account.cashout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.account.AccountCheckPayPassImp;
import com.lcshidai.lc.impl.account.AccountEcwCashoutMoneyImp;
import com.lcshidai.lc.impl.account.AccountUserEscrowInfoImp;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.UserEscrowInfoData;
import com.lcshidai.lc.model.account.UserEscrowInfoJson;
import com.lcshidai.lc.service.account.HttpCheckPayPassService;
import com.lcshidai.lc.service.account.HttpEcwCashoutMoneyService;
import com.lcshidai.lc.service.account.HttpUserEscrowInfoService;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.account.pwdmanage.UserPwdManageActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;

public class CashOutActivity extends TRJActivity implements View.OnClickListener, AccountUserEscrowInfoImp, AccountEcwCashoutMoneyImp, AccountCheckPayPassImp {
    private View mProgressBar;
    private ImageButton top_back_btn;
    private TextView top_title_text;
    private ImageView iv_bank_icon;
    private TextView tv_bank_name;
    private TextView tv_most_amount;
    private TextView tv_recharge_detail;
    private EditText et_cashout_amount;
    private EditText et_pay_password;
    private TextView tv_forget_psw;
    private Button btn_cashout_submit;
    private Dialog cashOutDialog = null;

    private HttpUserEscrowInfoService mHttpUserEscrowInfoService;
    private HttpEcwCashoutMoneyService mHttpEcwCashoutMoneyService;
    private HttpCheckPayPassService mHttpCheckPayPassService;

    private String ecw_mobile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariables();
        initView(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_most_amount.setText("");
        et_cashout_amount.setText("");
        et_cashout_amount.setHint("请输入提现金额");
        et_pay_password.setText("");
        et_pay_password.setHint("请输入支付密码");
        btn_cashout_submit.setBackgroundResource(R.drawable.bg_button_clickable_false);
        btn_cashout_submit.setClickable(false);
        refreshData();
    }

    private void initVariables(){
    	mHttpUserEscrowInfoService = new HttpUserEscrowInfoService(this, this);
        mHttpEcwCashoutMoneyService = new HttpEcwCashoutMoneyService(this, this);
        mHttpCheckPayPassService = new HttpCheckPayPassService(this, this);
    }

    private void initView(Bundle savedInstanceState){
        setContentView(R.layout.activity_cash_out);

        mProgressBar = findViewById(R.id.progressContainer);
        mProgressBar.setVisibility(View.GONE);

        top_back_btn = (ImageButton) findViewById(R.id.ib_top_bar_back);
        top_back_btn.setOnClickListener(this);

        top_title_text = (TextView) findViewById(R.id.tv_top_bar_title);
        top_title_text.setText("提现");

        iv_bank_icon = (ImageView) findViewById(R.id.iv_bank_icon);

        tv_bank_name = (TextView) findViewById(R.id.tv_bank_name);

        tv_most_amount = (TextView) findViewById(R.id.tv_most_amount);

        tv_recharge_detail = (TextView) findViewById(R.id.tv_recharge_detail);
        SpannableString ss = new SpannableString(tv_recharge_detail.getText());
        ss.setSpan(new UnderlineSpan(), 0, tv_recharge_detail.getText().length(), Spanned.SPAN_MARK_MARK);
        tv_recharge_detail.setText(ss);
        tv_recharge_detail.setOnClickListener(this);

        et_cashout_amount = (EditText) findViewById(R.id.et_cashout_amount);
        et_cashout_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    checkCashoutAmount();
                }
            }
        });

        et_pay_password = (EditText) findViewById(R.id.et_pay_password);
        
        tv_forget_psw = (TextView) findViewById(R.id.tv_forget_psw);
        tv_forget_psw.setOnClickListener(this);

        btn_cashout_submit = (Button) findViewById(R.id.btn_cashout_submit);
        btn_cashout_submit.setOnClickListener(this);

    }

    private void refreshData(){
        mProgressBar.setVisibility(View.VISIBLE);
    	mHttpUserEscrowInfoService.gainUserEscrowInfo();
        mHttpEcwCashoutMoneyService.getEcwCashoutMoney();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_top_bar_back:
                finish();
                break;
            case R.id.tv_recharge_detail:
                Intent new_recharge_intent = new Intent();
                new_recharge_intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/moneyRecord/50");
                new_recharge_intent.putExtra("title", "充值明细");
                new_recharge_intent.setClass(this, MainWebActivity.class);
                startActivity(new_recharge_intent);
                break;
            case R.id.tv_forget_psw:
            	Intent intent = new Intent(this, UserPwdManageActivity.class);
            	startActivity(intent);
            	break;
            case R.id.btn_cashout_submit:
                checkCashOut();
                break;
            default:
                break;
        }
    }

    private void checkCashOut(){
        String cashOut = et_cashout_amount.getText().toString();
        cashOut = cashOut.replaceAll("[^(0-9)]", "");
        if(TextUtils.isEmpty(cashOut)){
            Toast.makeText(this, "请输入提现金额", Toast.LENGTH_SHORT).show();
            et_cashout_amount.requestFocus();
            return;
        }
        checkPassword();
    }

    private void checkCashoutAmount(){
        String most = tv_most_amount.getText().toString();
        if(most == null || most.equals("")){
            return;
        }

        String cashOut = et_cashout_amount.getText().toString();
        most = most.replaceAll("[^(0-9).]", "");
        if(cashOut != null && !cashOut.equals("")){
            cashOut = cashOut.replaceAll("[^(0-9).]", "");
        }else{
            return;
        }

        double mostAmount = Double.valueOf(most);
        double cashOutAmount = Double.valueOf(cashOut);
        if(cashOutAmount > mostAmount){
            Toast.makeText(this, "您输入的金额大于可提现金额，请重新输入", Toast.LENGTH_SHORT).show();
            et_cashout_amount.setText("");
            return;
        }else if(cashOutAmount > 50000){
            showCashoutDialog();
            return;
        }else if(cashOutAmount <= 0){
            Toast.makeText(this, "提现金额不能小于0，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void checkPassword(){
        String password = et_pay_password.getText().toString().replace("请输入支付密码", "");
        if(password == null || password.equals("")){
            Toast.makeText(this, "请输入支付密码", Toast.LENGTH_SHORT).show();
            et_pay_password.requestFocus();
            return;
        }
        mHttpCheckPayPassService.gainEcwSmsCode(password);
    }

    private void showCashoutDialog(){
        if(cashOutDialog == null){
            cashOutDialog = createDialog("5万以上的大额提现到帐时间比小额晚1-2天，建议分多次提现", "大额提现", "先提5万",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(cashOutDialog.isShowing()){
                                cashOutDialog.dismiss();
                            }
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(cashOutDialog.isShowing()){
                                cashOutDialog.dismiss();
                            }
                            et_cashout_amount.setText("50000");
                        }
                    }
            );
            TextView t = (TextView)cashOutDialog.findViewById(R.id.dialog_nomessage_bt_negative);
            t.setTextColor(getResources().getColor(R.color.color_finance_child_yellow));
        }

        if(!cashOutDialog.isShowing()){
            cashOutDialog.show();
        }
    }

    private void goCashOut(){
        String cashOutAmount = et_cashout_amount.getText().toString();
        cashOutAmount = cashOutAmount.replaceAll("[^(0-9).]", "");
        String pay_wd = et_pay_password.getText().toString();
        Intent intent = new Intent(this, EcwSmsActivity.class);
        intent.putExtra("amount", cashOutAmount);
        intent.putExtra("pay_wd", pay_wd);
        intent.putExtra("ecw_mobile", ecw_mobile);
        startActivity(intent);
        finish();
    }
    
    @Override
	public void gainUserEscrowInfoSuccess(UserEscrowInfoJson response) {
        if(response != null){
            if(response.getBoolen().equals("1")){
                UserEscrowInfoData data = response.getData();

                ecw_mobile = data.getEcw_mobile();

                String bankName = data.getBankname();
                int index = bankName.indexOf("银行");
                if(index != -1){
                    bankName = bankName.substring(0, index + 2);
                }
                String bankcardNumber = data.getBankcard_no();
                int length = bankcardNumber.length();
                bankcardNumber = bankcardNumber.substring(length - 4);
                tv_bank_name.setText(String.format("%s(尾号%s)", bankName, bankcardNumber));
                
                if(data.getBank_icon() != null){
                    Glide.with(mContext).load(data.getBank_icon()).into(iv_bank_icon);
                }
            }
        }
	}

	@Override
	public void gainUserEscrowInfoFail() {

	}

    @Override
    public void gainEcwCashoutMoneySuccess(BaseJson response) {
        mProgressBar.setVisibility(View.GONE);
        if(response != null){
            if(response.getBoolen().equals("1")){
                String mostAmount = response.getMessage();
                if (!CommonUtil.isNullOrEmpty(mostAmount)) {
                    mostAmount = mostAmount.replaceAll("[^(0-9).]", "");
                    double most = Double.valueOf(mostAmount);
                    most /= 100d;
//                    mostAmount = String.valueOf(most);
                    tv_most_amount.setText(String.format("%.2f元", most));
                    btn_cashout_submit.setBackgroundResource(R.drawable.feedback_submit_bg_xml);
                    btn_cashout_submit.setClickable(true);
                }
            }
        }
    }

    @Override
    public void gainEcwCashoutMoneyFail() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void checkPayPassSuccess(BaseJson response) {
        if(response != null){
            if(response.getBoolen().equals("1")){
                goCashOut();
            }else{
                Toast.makeText(this, "支付密码不正确", Toast.LENGTH_SHORT).show();
                et_pay_password.setText("");
                et_pay_password.requestFocus();
            }
        }
    }

    @Override
    public void checkPayPassFail() {

    }

}
