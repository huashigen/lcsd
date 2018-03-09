package com.lcshidai.lc.ui.account.user;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.CheckIdentityImpl;
import com.lcshidai.lc.impl.account.GetQuestionImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.CheckIdentityJson;
import com.lcshidai.lc.model.account.GetQuestionJson;
import com.lcshidai.lc.model.account.GetQuestionStrJson;
import com.lcshidai.lc.service.account.HttpCheckIdentityService;
import com.lcshidai.lc.service.account.HttpGetQuestionService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 各途径重置密码
 * intent_type:	0-实名信息	1-银行卡		2-安保问题
 * @author 000853
 *
 */
public class ForgetPasswordChannelVerifyActivity extends TRJActivity implements CheckIdentityImpl,GetQuestionImpl{
	HttpCheckIdentityService hcis;
	HttpGetQuestionService hgqs;
	
	private int intentType = -1;
	
	private TextView mTvTitle, mSubTitle;
	private Button  mSaveBtn;
	ImageButton mBackBtn;
	private LinearLayout type_ll_1, type_ll_2;
	private CustomEditTextLeftIcon et_1, et_2, et_answer;
	private RelativeLayout rl_next;
	private LinearLayout question_selecte_ll;
	private TextView question_tv;
	private String selectQuestionCodeNo = "";
	private String questionName = "";
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if(getIntent() != null){
			intentType = getIntent().getIntExtra("intent_type", -1);
		}
		hcis = new HttpCheckIdentityService(this, this);
		hgqs = new HttpGetQuestionService(this, this);
		initView();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
		setContentView(R.layout.activity_forget_pwd_channel_verify);
		
		mBackBtn=(ImageButton)this.findViewById(R.id.btn_back);
		mBackBtn.setVisibility(View.VISIBLE);
		mSaveBtn=(Button)this.findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mTvTitle=(TextView)this.findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("忘记密码");
		mSubTitle=(TextView)this.findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);

		type_ll_1 = (LinearLayout) findViewById(R.id.forget_pwd_channel_verity_ll1);
		type_ll_2 = (LinearLayout) findViewById(R.id.forget_pwd_channel_verity_ll2);
		rl_next = (RelativeLayout) findViewById(R.id.rl_next);
		
		mBackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(13);
				finish();
			}
		});
		
		switch(intentType){
		case 0:
			rl_next.setVisibility(View.VISIBLE);
			type_ll_1.setVisibility(View.VISIBLE);
			initAutoVerify();
//			request_url = AUTH_VERIFY_URL;
			break;
		case 1:
			rl_next.setVisibility(View.VISIBLE);
			type_ll_1.setVisibility(View.VISIBLE);
			initBankCardVerify();
//			request_url = BANKCARD_VERIFY_URL;
			break;
		case 2:
			rl_next.setVisibility(View.VISIBLE);
			type_ll_2.setVisibility(View.VISIBLE);
			initSafeVerify();
//			request_url = SAFE_VERIFY_URL;
			break;
		}

		rl_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadData();
			}
		});
	}

	/**
	 * 通过实名信息修改密码验证
	 */
	private void initAutoVerify(){
		et_1 = (CustomEditTextLeftIcon) findViewById(R.id.forget_pwd_channel_verity_et1);
		et_2 = (CustomEditTextLeftIcon) findViewById(R.id.forget_pwd_channel_verity_et2);
		et_1.setHint("姓名");
		et_1.setIcon(getResources().getDrawable(R.drawable.icon_account));
		et_2.setHint("证件号码");
		et_2.setIcon(getResources().getDrawable(R.drawable.user_card));
	}
	
	/**
	 * 通过绑定银行卡信息修改密码验证
	 */
	private void initBankCardVerify(){
		et_1 = (CustomEditTextLeftIcon) findViewById(R.id.forget_pwd_channel_verity_et1);
		et_2 = (CustomEditTextLeftIcon) findViewById(R.id.forget_pwd_channel_verity_et2);
		et_1.setHint("银行卡号");
		et_1.setIcon(getResources().getDrawable(R.drawable.bank_card));
		et_2.setHint("持卡人证件号码");
		et_2.setIcon(getResources().getDrawable(R.drawable.user_card));
	}
	
	/**
	 * 通过安保问题修改密码验证
	 */
	private void initSafeVerify(){
		et_answer = (CustomEditTextLeftIcon) findViewById(R.id.forget_pwd_channel_verity_et_answer);
		et_answer.setHint("答案");
		question_selecte_ll = (LinearLayout) findViewById(R.id.safe_question_ll_select);
		question_tv = (TextView) findViewById(R.id.safe_question_tv_question);
		loadQuestionData();
	}
	
	private void loadData(){
		hcis.gainCheckIdentity(intentType,et_1,et_2,selectQuestionCodeNo,et_answer);
	}
	
	private void loadQuestionData(){
		hgqs.gainGetQuestion();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setResult(13);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//失效
		if(requestCode == 20 && resultCode == 21){
			setResult(11);
			finish();
		}
		//返回或者修改成功
		if(requestCode == 20 && resultCode == 22){
			setResult(12);
			finish();
		}
		//选择密码保护问题
		if(requestCode == 10 && resultCode == 20){
			selectQuestionCodeNo = data.getStringExtra("select_quetion_code_no");
			if(!"".equals(selectQuestionCodeNo)){
				questionName = data.getStringExtra("question_name");
				question_tv.setText(questionName);
				question_tv.setTextColor(Color.parseColor("#333333"));
			}
		}
	}
	
	@Override
	public void doFinish() {
		setResult(13);
		super.doFinish();
	}

	@Override
	public void gainGetQuestionsuccess(BaseJson response) {
		try {
			if(response != null){
			   String boolen = response.getBoolen();
               if(boolen.equals("1")){
            	   GetQuestionJson dataObj = (GetQuestionJson)response;
            	   selectQuestionCodeNo = dataObj.getData().getCode_no();
            	   questionName = dataObj.getData().getCode_name();
            	   
            	   question_tv.setText("安保问题：" + questionName);
               }else{
            	   GetQuestionStrJson dataObj = (GetQuestionStrJson)response;
            	   if("-1".equals(dataObj.getData())){
            		   ToastUtil.showToast(ForgetPasswordChannelVerifyActivity.this, "手机确认超时，请完成手机确认");
            		   setResult(11);
            		   finish();
            	   }else{
	            	   String message = response.getMessage();
	            	   ToastUtil.showToast(ForgetPasswordChannelVerifyActivity.this, message);
            	   }
               }
		   }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gainGetQuestionfail() {
		ToastUtil.showToast(ForgetPasswordChannelVerifyActivity.this, "网络不给力");
	}


	@Override
	public void gainCheckIdentitysuccess(CheckIdentityJson response) {
		try {
			if(response != null){
			   String boolen = response.getBoolen();
               if(boolen.equals("1")){
            	   Intent intent = new Intent(ForgetPasswordChannelVerifyActivity.this, ForgetPasswordGeneralActivity.class);
            	   if(intentType == 0){
            		   intent.putExtra("set_type", "identity");
            	   }else if(intentType == 1){
            		   intent.putExtra("set_type", "bank");
            	   }else if(intentType == 2){
            		   intent.putExtra("set_type", "sqa");
            	   }
            	   startActivityForResult(intent, 20);
               }else{
            	   if("-1".equals(response.getData())){
            		   ToastUtil.showToast(ForgetPasswordChannelVerifyActivity.this, "手机确认超时，请完成手机确认");
            		   setResult(11);
            		   finish();
            	   }else{
	            	   String message = response.getMessage();
	            	   ToastUtil.showToast(ForgetPasswordChannelVerifyActivity.this, message);
            	   }
               }
		   }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gainCheckIdentityfail() {
		ToastUtil.showToast(ForgetPasswordChannelVerifyActivity.this, "网络不给力");
	}
}
