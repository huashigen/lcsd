package com.lcshidai.lc.ui.account.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;

/**
 * 多途径重置密码
 * @author 000853
 *
 */
public class ForgetPasswordChannelActivity extends TRJActivity implements OnClickListener {
	
	private String TELE_NUMBER = "400-900-1000";
	
	private TextView mTvTitle, mSubTitle;
	private Button  mSaveBtn;
	ImageButton mBackBtn;
	private TextView telephone_tv2;
	private LinearLayout userauth_ll, bankcard_ll, safe_ll, tele_ll;
	private String is_id_auth, is_sqa, is_binding_bank;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if(null != getIntent()){
			is_id_auth = getIntent().getStringExtra("is_id_auth");
			is_sqa = getIntent().getStringExtra("is_sqa");
			is_binding_bank = getIntent().getStringExtra("is_binding_bank");
		}
		initView();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
		setContentView(R.layout.activity_forget_pwd_channel);
		
		mBackBtn=(ImageButton)this.findViewById(R.id.btn_back);
		mBackBtn.setVisibility(View.VISIBLE);
		mSaveBtn=(Button)this.findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mTvTitle=(TextView)this.findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("重置登录密码");
		mSubTitle=(TextView)this.findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		
		mBackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		telephone_tv2 = (TextView) findViewById(R.id.forget_pwd_channel_telenumber_tv2);
		telephone_tv2.getPaint().setFakeBoldText(true);
		telephone_tv2.setText(TELE_NUMBER);
		
		userauth_ll = (LinearLayout) findViewById(R.id.forget_pwd_channel_ll_userauth);
		bankcard_ll = (LinearLayout) findViewById(R.id.forget_pwd_channel_ll_bankcard);
		safe_ll = (LinearLayout) findViewById(R.id.forget_pwd_channel_ll_safe);
		tele_ll = (LinearLayout) findViewById(R.id.forget_pwd_channel_ll_tele);
		
		userauth_ll.setOnClickListener(this);
		bankcard_ll.setOnClickListener(this);
		safe_ll.setOnClickListener(this);
		tele_ll.setOnClickListener(this);
		
		if("1".equals(is_id_auth)){
			userauth_ll.setVisibility(View.VISIBLE);
		}
		if("1".equals(is_sqa)){
			safe_ll.setVisibility(View.VISIBLE);
		}
//		if("1".equals(is_binding_bank)){
//			bankcard_ll.setVisibility(View.VISIBLE);
//		}
		tele_ll.setVisibility(View.VISIBLE);
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(ForgetPasswordChannelActivity.this, ForgetPasswordChannelVerifyActivity.class);
		switch(v.getId()){
		//实名信息校验
		case R.id.forget_pwd_channel_ll_userauth:
			intent.putExtra("intent_type", 0);
			break;
		//绑定银行卡信息校验
		case R.id.forget_pwd_channel_ll_bankcard:
			intent.putExtra("intent_type", 1);
			break;
		//安保问题校验
		case R.id.forget_pwd_channel_ll_safe:
			intent.putExtra("intent_type", 2);
			break;
		//联系客服
		case R.id.forget_pwd_channel_ll_tele:
			Intent phoneIntent = new Intent(Intent.ACTION_CALL, 
					Uri.parse("tel:" + TELE_NUMBER.replaceAll("-", "")));
			startActivity(phoneIntent);
			return ;
		}
		startActivityForResult(intent, 10);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 10 && resultCode == 11){
			Intent intent = new Intent(ForgetPasswordChannelActivity.this, ForgetPasswordFirstActivity.class);
			startActivity(intent);
			finish();
		}
		if(requestCode == 10 && resultCode == 12){
			finish();
		}
	}
	
}
