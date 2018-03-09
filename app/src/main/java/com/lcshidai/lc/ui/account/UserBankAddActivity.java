package com.lcshidai.lc.ui.account;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.widget.text.CustomEditBank;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 银行卡添加
 * @author
 *
 */
public class UserBankAddActivity extends TRJActivity{
	private static final String URL_SEND_DATA = "Mobile2/PayAccount/saveFundAccount";
	private static final String URL_GET_BANK_INFO = "Mobile2/PayAccount/getFundAccount";
	private static final String AUTH_ID_URL = "Mobile2/Auth/isAuthId";
	private ImageButton mBackBtn;
	private TextView mTvTitle,mSubTitle;
	private Button mSaveBtn;
	private View mProgressContainer;
	private Button btn_submit;
	private TextView tv_bank,tv_account_bank,tv_account_city;
	private CustomEditBank edit_account_no;
    private String mCode = "",pcode="",ccode="",mRealName,mBankName,sub_bank="",sub_bank_id;
	private LinearLayout ll_bank,ll_account_bank,ll_account_city;
	private String is_agree = "1";
	private CheckBox cb_agree;
	private TextView tv_real_name;
	private String bid="";//银行卡ID
	private Dialog authDialog;
	private Context mContext;
	
	private boolean isAuthOk = false;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bank_add);
        mContext = this;
        Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mRealName = bundle.getString("real_name");
			bid = bundle.getString("id");
		}
        initView();
        if(bid != null && !bid.equals("")){
        	loadData();
        }
    }

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	@Override
	protected void onResume() {
		if(!isAuthOk){
			if(bid.equals("")){
				mProgressContainer.setVisibility(View.VISIBLE);
				isAuthId();
	        }
		}
		super.onResume();
	}
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			UserBankAddActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
    
    private void initView(){
    	mBackBtn=(ImageButton) findViewById(R.id.btn_back);
    	mBackBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTvTitle = (TextView)findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("银行卡管理");
		mSubTitle = (TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		mSaveBtn = (Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mProgressContainer = findViewById(R.id.progressContainer);
		mProgressContainer.setVisibility(View.GONE);
		tv_bank = (TextView)findViewById(R.id.tv_bank);
		tv_account_bank = (TextView)findViewById(R.id.tv_account_bank);
		tv_account_city = (TextView)findViewById(R.id.tv_account_city);
		edit_account_no = (CustomEditBank)findViewById(R.id.edit_account_no);
		edit_account_no.setHint("卡号(仅限储蓄卡)");
		edit_account_no.setInputType(InputType.TYPE_CLASS_NUMBER);
		tv_real_name = (TextView)findViewById(R.id.tv_real_name);
		tv_real_name.setText(mRealName);
		btn_submit = (Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onSubmitData();
			}
		});
		ll_bank = (LinearLayout)findViewById(R.id.ll_bank);
		ll_bank.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(UserBankAddActivity.this,BankListActivity.class),2);
			}
		});
		ll_account_city = (LinearLayout)findViewById(R.id.ll_account_city);
		ll_account_city.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(UserBankAddActivity.this,CityListActivity.class),4);
			}
		});
		ll_account_bank = (LinearLayout)findViewById(R.id.ll_account_bank);
		ll_account_bank.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(mCode.equals("")){
					createDialogDismissAuto("请选择银行");
		    		return;
		    	}
				if(pcode.equals("")){
					createDialogDismissAuto("请选择开户城市");
		    		return;
				}
				Intent intent = new Intent(UserBankAddActivity.this,AccountBankSearchActivity.class);
				intent.putExtra("code", mCode);
				intent.putExtra("pcode", pcode);
				intent.putExtra("ccode", ccode);
				startActivityForResult(intent,3);
			}
		});
		cb_agree = (CheckBox)findViewById(R.id.cb_agree);
		cb_agree.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if(isChecked){
					is_agree = "1";
				}else{
					is_agree = "0";
				}
			}
		});
		authDialog = createDialog("信息提示", 
				"您的账户还未实名认证，请完成实名认证后再操作", "确定", "取消", 
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(authDialog.isShowing()){
							authDialog.dismiss();
						}
						Intent intent = new Intent();
						intent.setClass(mContext, UserNameAuthActivity.class);
						startActivity(intent);
					}
				}, 
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(authDialog.isShowing()){
							authDialog.dismiss();
						}
						UserBankAddActivity.this.finish();
					}
				});
    }
    
    private void onSubmitData(){
    	String bank_no = edit_account_no.getEdtText().toString().trim();
    	if(mCode.equals("")){
    		createDialogDismissAuto("请选择银行");
    		return;
    	}
    	if(bank_no.equals("")){
    		createDialogDismissAuto("请填写银行卡号");
    		return;
    	}
    	if(pcode.equals("")){
    		createDialogDismissAuto("请选择开户城市");
			return;
	    }
    	if(sub_bank.equals("")){
    		createDialogDismissAuto("请选择开户行");
			return;
	    }
    	btn_submit.setEnabled(false);
    	mProgressContainer.setVisibility(View.VISIBLE);
    	RequestParams rq = new RequestParams();
        rq.put("id",bid);
        rq.put("account_no",bank_no);
    	rq.put("bank",mCode);
    	rq.put("bank_name",mBankName);
    	rq.put("sub_bank",sub_bank);
    	rq.put("sub_bank_id",sub_bank_id);
    	rq.put("temp_pcode",pcode);
    	rq.put("code",ccode);
    	rq.put("is_default",is_agree);
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
     	            	   message = "提交成功";
     	 				   finish();
     	 				 createDialogDismissAuto(message);
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
        		btn_submit.setEnabled(true);
        	}
        	
        });
    }
    
    private void loadData(){
    	mProgressContainer.setVisibility(View.VISIBLE);
    	RequestParams rq = new RequestParams();
        rq.put("id",bid);
        post(URL_GET_BANK_INFO, rq, new JsonHttpResponseHandler(this){
        	@Override
        	public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
        		super.onSuccess(statusCode,headers, response);
        		try {
        			mProgressContainer.setVisibility(View.GONE);
        			if(response != null){
     				   String boolen = response.getString("boolen");
     	               if(boolen.equals("1")){
     	            	  JSONObject obj = response.optJSONObject("data");
     	            	  if(obj != null){
     	            		 tv_real_name.setText(obj.getString("real_name"));
     	            		 JSONObject jo = obj.optJSONObject("data");
     	            		 if(jo != null){
     	            			String account_no = jo.getString("account_no");
     	            			edit_account_no.setEdtText(account_no);
     	            			edit_account_no.setEdtSelection(account_no.length());
     	            			mBankName = jo.getString("bank_name");
     	            			mCode = jo.getString("bank");
     	            			sub_bank = jo.getString("sub_bank");
     	            			sub_bank_id = jo.getString("sub_bank_id");
     	            			pcode = jo.getString("bank_province");
     	            			ccode = jo.getString("bank_city");
     	            			is_agree = jo.getString("is_default");
     	            			String pname = !jo.isNull("province_name")?jo.getString("province_name"):"";
     	            			String cname = !jo.isNull("city_name")?jo.getString("city_name"):"";
     	            			tv_bank.setText(mBankName);
     	            			if(pname.equals("") && cname.equals("")){
     	            				tv_account_city.setText("");
     	            			}else{
     	            			    tv_account_city.setText(pname + " " + cname);
     	            			}
     	            			tv_account_bank.setText(sub_bank);
     	            			if(is_agree.equals("1")){
     	            				cb_agree.setChecked(true);
     	            			}else{
     	            				cb_agree.setChecked(false);
     	            			}
     	            		 }
     	            	  }
     	               } 
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
    
    private void isAuthId(){
    	post(AUTH_ID_URL, new RequestParams(), new JsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
				super.onSuccess(statusCode,headers, response);
				try{
					if(null != response){
						if(response != null){
							mProgressContainer.setVisibility(View.GONE);
							String boolen = response.getString("boolen");
							if(null != boolen && "1".equals(boolen)){
								isAuthOk = true;
								mRealName = response.optString("data");
								tv_real_name.setText(mRealName);
							}else{
								if(!authDialog.isShowing()){
									authDialog.show();
								}
							}
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case 2:
				if(data != null){
					Bundle b = data.getExtras();
					mBankName = b.getString("name");
					mCode =  b.getString("code");
					tv_bank.setText(mBankName);
				}
				break;
			case 3:
				if(data != null){
					Bundle b = data.getExtras();
					sub_bank = b.getString("name");
					sub_bank_id = b.getString("sid");
					tv_account_bank.setText(sub_bank);
				}
				break;
			case 4:
				if(data != null){
					Bundle b = data.getExtras();
					String pname = b.getString("pname");
					pcode =  b.getString("pcode");
					String cname = b.getString("cname");
					ccode =  b.getString("ccode");
					tv_account_city.setText(pname + " " + cname);
					sub_bank = "";
					tv_account_bank.setText(sub_bank);
				}
				break;
			default:
				break;
			}
		}
	}
}
