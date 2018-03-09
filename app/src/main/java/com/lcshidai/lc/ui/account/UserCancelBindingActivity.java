package com.lcshidai.lc.ui.account;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.SmsAutoUtil;
import com.lcshidai.lc.utils.TimeCount;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 银行卡取消绑定
 *
 */
public class UserCancelBindingActivity extends TRJActivity{
	private static final String URL_SEND_DATA = "Mobile2/PayAccount/delFundAccount";//解除/删除银行卡信息
	private static final String URL_SEND_EDIT_CODE = "/Mobile2/PayAccount/sendDelFundAuthCode";//取消绑定发送动态码
	private ImageButton mBackBtn;
	private TextView mTvTitle,mSubTitle;
	private Button mSaveBtn;
	private CustomEditTextLeftIcon edit_pay_pwd,edit_dynamic_code;
	private View mProgressContainer;
	private TimeCount mTime;
	private Button btn_dy_code,btn_submit;
	private TextView tv_show;
	private String mId;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cancel_binding);
        Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mId = bundle.getString("id");
		}
        initView();
    }

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
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
		mProgressContainer = findViewById(R.id.progressContainer);
		mProgressContainer.setVisibility(View.GONE);
		tv_show = (TextView)findViewById(R.id.tv_show);
		tv_show.setText("发送中……");
		edit_pay_pwd = (CustomEditTextLeftIcon)findViewById(R.id.edit_pay_pwd);
		edit_dynamic_code = (CustomEditTextLeftIcon)findViewById(R.id.edit_dynamic_code);
		mTvTitle.setText("银行卡管理");
		edit_pay_pwd.setHint("手机支付密码");
		edit_dynamic_code.setHint("手机动态码");
		edit_pay_pwd.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
		btn_dy_code = (Button)findViewById(R.id.btn_gain_dn);
		btn_dy_code.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onSendDyCode();
				try {
					SmsAutoUtil.getInstance().startWork(
							UserCancelBindingActivity.this, new Handler(), edit_dynamic_code);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btn_submit = (Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				btn_submit.setEnabled(false);
				onSubmitData();
			}
		});
		mTime = TimeCount.newInstance(btn_dy_code, 120000, 1000);//构造CountDownTimer对象
    }
    
    private void onSendDyCode(){
		RequestParams rq =new RequestParams();
		post(URL_SEND_EDIT_CODE, rq, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
        		try {
    				if(response != null){
    				   String boolen = response.getString("boolen");
    	               String message = "";
    	               if(boolen.equals("1")){
    	            	   message = "发送成功";
    	            	   mTime.start();//开始计时
    	               }else{
    	            	   message = response.getString("message");
    	               }
    	               showToast(message);
    			   }
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
        	}
		});
	}
    
    private void onSubmitData(){
    	String pay_pwd = edit_pay_pwd.getEdtText().toString().trim();
    	String dy_code = edit_dynamic_code.getEdtText().toString().trim();
    	if(pay_pwd.equals("")){
    		showToast("支付密码不能为空");
    		btn_submit.setEnabled(true);
    		return;
    	}
    	if(dy_code.equals("")){
    		showToast("动态码不能为空");
    		btn_submit.setEnabled(true);
    		return;
    	}
    	mProgressContainer.setVisibility(View.VISIBLE);
    	RequestParams rq = new RequestParams();
        rq.put("id",mId);
    	rq.put("pwd",pay_pwd);
    	rq.put("code",dy_code);
        post(URL_SEND_DATA, rq, new JsonHttpResponseHandler(this){
        	@Override
        	public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
        		super.onSuccess(statusCode,headers, response);
        		try {
        			mProgressContainer.setVisibility(View.GONE);
        			btn_submit.setEnabled(true);
        			if(response != null){
     				   String boolen = response.getString("boolen");
     				   String message = "";
     	               if(boolen.equals("1")){
     	            	   message = "删除成功";
     	 				   finish();
     	 				   showToast(message);
     	               }
//     	               else{
//     	            	   message = response.getString("message");
//     	               }
     	               
     			   }
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}

        	}
        	@Override
        	public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
        		mProgressContainer.setVisibility(View.GONE);
        	}
        	
        });
    }
    
    @Override
    protected void onDestroy() {
    	try {
			SmsAutoUtil.getInstance().stopWork(UserCancelBindingActivity.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	super.onDestroy();
    }
}
