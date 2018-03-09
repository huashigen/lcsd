package com.lcshidai.lc.ui.account.bespeak;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 预约申请成功
 * @author 000853
 *
 */
public class BespeakApplySuccessActivity extends TRJActivity implements OnClickListener {

	private Context mContext;
	private TextView mTvTitle, mSubTitle;
	private Button  mSaveBtn;
	ImageButton mBackBtn;
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
		setContentView(R.layout.activity_bespeak_apply_success);
		mBackBtn=(ImageButton) findViewById(R.id.btn_back);
		mBackBtn.setOnClickListener(this);
		mSaveBtn=(Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mTvTitle=(TextView)findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("预约抢标");
		mSubTitle=(TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		
		submit_bt = (Button) findViewById(R.id.bespeak_apply_success_bt_submit);
		submit_bt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			setResult(3);
			BespeakApplySuccessActivity.this.finish();
			break;
		case R.id.bespeak_apply_success_bt_submit:
			setResult(3);
			BespeakApplySuccessActivity.this.finish();
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setResult(3);
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
