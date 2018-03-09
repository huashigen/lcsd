package com.lcshidai.lc.ui.account.pwdmanage;
import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.account.AccountCenterActivity;
import com.lcshidai.lc.ui.base.TRJActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 重置支付密码成功界面
 *
 *
 */
public class PayPwdResetShowActivity extends TRJActivity{
	private View rl_next,btn_option,tv_subtitle;
	private TextView tv_title;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_pay_pwd_reset_show);
		initView();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
		btn_option=findViewById(R.id.btn_option);
		tv_subtitle=findViewById(R.id.tv_subtitle);
		tv_title=(TextView) findViewById(R.id.tv_top_bar_title);
		rl_next = findViewById(R.id.rl_next);
		
		btn_option.setVisibility(View.INVISIBLE);
		tv_subtitle.setVisibility(View.GONE);
		tv_title.setText("设置手机支付密码");
		rl_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PayPwdResetShowActivity.this, AccountCenterActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		ImageButton btn_back=(ImageButton) findViewById(R.id.btn_back);
	     btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PayPwdResetShowActivity.this, UserMobilePayPwdActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
}