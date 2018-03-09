package com.lcshidai.lc.ui.newfinan;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 会员资格认证
 * @author 000853
 *
 */
public class QualificationsAuthenticationActivity extends TRJActivity implements OnClickListener {

	private TextView mTvTitle, mSubTitle;
	private Button  mSaveBtn;
	ImageButton mBackBtn;
	private Context mContext;
	
	private Button submit_bt;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initView();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
		mContext = this;
		setContentView(R.layout.activity_qualifications_authentication);
		mBackBtn=(ImageButton) findViewById(R.id.btn_back);
		mBackBtn.setVisibility(View.VISIBLE);
		mBackBtn.setOnClickListener(this);
		mSaveBtn=(Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mTvTitle=(TextView)findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("会员资格认证");
		mSubTitle=(TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		
		submit_bt = (Button) findViewById(R.id.qualifications_authentication_bt_submit);
		submit_bt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.qualifications_authentication_bt_submit:
			Intent intent = new Intent(mContext, InvestorAuthenticationActivity.class);
			startActivity(intent);
			QualificationsAuthenticationActivity.this.finish();
			break;
		case R.id.btn_back:
			QualificationsAuthenticationActivity.this.finish();
			break;
		}
	}
	
}
