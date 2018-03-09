package com.lcshidai.lc.ui.account.pwdmanage;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.CheckMobileCodeImpl;
import com.lcshidai.lc.impl.account.GetMobileCodeImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.account.HttpCheckMobileCodeService;
import com.lcshidai.lc.service.account.HttpGetMobileCodeService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.SmsAutoUtil;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 手机支付密码获取动态码
 *
 */
public class UserMobileCodeActivity extends TRJActivity implements GetMobileCodeImpl,CheckMobileCodeImpl{
	HttpGetMobileCodeService hgmcs;
	HttpCheckMobileCodeService hcmcs;
	private ImageButton mBackBtn;
	private TextView mTvTitle,mSubTitle;
	private Button mSaveBtn;
	private CustomEditTextLeftIcon edit_dynamic_code;
	private TimeCount mTime;
	private TextView btn_dy_code,btn_submit;
	private String toActivity = "";
//	private DialogPopupWindow dialogPopupWindow;
//	private View mainView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mobile_code);
        Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			toActivity = bundle.getString("to_activity");
		}
		hgmcs = new HttpGetMobileCodeService(this, this);
		hcmcs = new HttpCheckMobileCodeService(this, this);
        initView();
    }

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
    	Drawable draw_dynamic_code_icon = getResources().getDrawable(R.drawable.dy_code);
    	mBackBtn = (ImageButton) findViewById(R.id.btn_back);
    	mBackBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTvTitle = (TextView)findViewById(R.id.tv_top_bar_title);
		mSubTitle = (TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		mSaveBtn = (Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		edit_dynamic_code = (CustomEditTextLeftIcon)findViewById(R.id.edit_dynamic_code);
		edit_dynamic_code.setIcon(draw_dynamic_code_icon);
		mTvTitle.setText("手机校验");
		edit_dynamic_code.setHint("请输入动态码");
		edit_dynamic_code.setTextSize(14);
		btn_dy_code = (TextView)findViewById(R.id.btn_gain_dn);
		btn_dy_code.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onSendDyCode();
			}
		});
		btn_submit = (Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onSubmitData();
			}
		});
		mTime = new TimeCount(120000, 1000);//构造CountDownTimer对象
//		mainView = findViewById(R.id.ll_main);
//		dialogPopupWindow = new DialogPopupWindow(this);
    }
    
	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			// mBtnGainDn.setText(" 重新获取 ");
			btn_dy_code.setText("重新发送");
			btn_dy_code.setEnabled(true);
			btn_dy_code.setClickable(true);
//			btn_dy_code.setBackgroundResource(R.color.color_3);
			btn_dy_code.setBackgroundColor(getResources().getColor(R.color.color_3));
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btn_dy_code.setClickable(false);
//			btn_dy_code.setBackgroundResource(R.drawable.bg_button_cancel);
//			btn_dy_code.setText(
//					"重发(" + millisUntilFinished / 1000 + "秒)");
			btn_dy_code.setBackgroundResource(R.color.sended_color);
			btn_dy_code.setText(millisUntilFinished / 1000 + "s重新发送");
		}
	}
    
    private void onSendDyCode(){
    	hgmcs.gainGetMobileCode();
//		RequestParams rq =new RequestParams();
//		post(URL_SEND_EDIT_CODE, rq, new JsonHttpResponseHandler(){
//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//					JSONObject response) {
//        		try {
//    				if(response != null){
//    				   String boolen = response.getString("boolen");
//    	               String message = "";
//    	               if(boolen.equals("1")){
//    	            	   message = "发送成功";
//    	            	   mTime.start();//开始计时
//    	               }else{
//    	            	   message = response.getString("message");
////    	            	   dialogPopupWindow.setValue(message, "0",mainView);
//    	               }
//    	               showToast(message);
//    			   }
//    			} catch (JSONException e) {
//    				e.printStackTrace();
//    			}
//        	}
//		});

		try {
			SmsAutoUtil.getInstance().startWork(
					UserMobileCodeActivity.this, new Handler(), edit_dynamic_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    private void onSubmitData(){
    	String dy_code = edit_dynamic_code.getEdtText().toString().trim();
    	if(dy_code.equals("")){
//    		dialogPopupWindow.setValue("动态码不能为空", "0",mainView);
//    		showToast("动态码不能为空");
    		createDialogDismissAuto("动态码不能为空");
    		return;
    	}
    	btn_submit.setEnabled(false);
    	hcmcs.gainCheckMobileCode(dy_code);
		showLoadingDialog(mContext, "正在加载...", true);
    	
    }
    
    @Override
    protected void onDestroy() {
    	try {
			SmsAutoUtil.getInstance().stopWork(UserMobileCodeActivity.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	super.onDestroy();
    }

	@Override
	public void gainCheckMobileCodesuccess(BaseJson response) {
		try {
			hideLoadingDialog();
			btn_submit.setEnabled(true);
			if(response != null){
				   String boolen = response.getBoolen();
	               if(boolen.equals("1")){
	            	  Intent intent = new Intent();
	            	  if(toActivity.equals("real")){
	            		 intent.setClass(UserMobileCodeActivity.this, PayPwdChannelVerifyActivity.class);
	            		 intent.putExtra("intent_type", 0);
	            	  }else if(toActivity.equals("bank")){
	            		 intent.setClass(UserMobileCodeActivity.this, PayPwdChannelVerifyActivity.class);
	            		 intent.putExtra("intent_type", 1);
	            	  }else if(toActivity.equals("sqa")){
	            		 intent.setClass(UserMobileCodeActivity.this, PayPwdChannelVerifyActivity.class);
	            		 intent.putExtra("intent_type", 2);
	            	  }
	 				  startActivity(intent);
	 				  finish();
	               } else {
	            	   createDialogDismissAuto(response.getMessage());
	               }
			   }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gainCheckMobileCodefail() {
		hideLoadingDialog();
		ToastUtil.showToast(this, "网络异常");
	}

	@Override
	public void gainGetMobileCodesuccess(BaseJson response) {
		try {
			if(response != null){
			   String boolen = response.getBoolen();
               String message = "";
               if(boolen.equals("1")){
            	   message = "发送成功";
            	   mTime.start();//开始计时
               }else{
            	   message = response.getMessage();
//            	   dialogPopupWindow.setValue(message, "0",mainView);
               }
//               showToast(message);
               createDialogDismissAuto(message);
		   }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gainGetMobileCodefail() {
		createDialogDismissAuto("发送失败");
	}
}
