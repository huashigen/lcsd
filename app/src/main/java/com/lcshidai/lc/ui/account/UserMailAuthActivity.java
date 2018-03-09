package com.lcshidai.lc.ui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.UserMailAuthImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.account.HttpUserMailAuthService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;
/**
 * 用户邮箱认证(邮箱修改最后提交)
 *
 */
public class UserMailAuthActivity extends TRJActivity implements UserMailAuthImpl{
	HttpUserMailAuthService humas;
	private ImageButton mBackBtn;
	private TextView mTvTitle,mSubTitle;
	private Button mSaveBtn;
	private CustomEditTextLeftIcon edit_mail;
	private TextView btn_submit;
	private View mProgressContainer;
	private TextView tv_show;
	private int mMode;//1-认证，2-修改
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mail_auth);
        Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mMode = bundle.getInt("mode");
		}
		humas = new HttpUserMailAuthService(this, this);
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
		edit_mail = (CustomEditTextLeftIcon)findViewById(R.id.edit_mail);
		Drawable draw_email_icon = getResources().getDrawable(R.drawable.icon_email);
		edit_mail.setIcon(draw_email_icon);
		AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);
		

		if(mMode == 1){
		    mTvTitle.setText("邮箱认证");
			SpannableString ss_dy_vcode = new SpannableString("邮箱");
			ss_dy_vcode.setSpan(ass, 0, ss_dy_vcode.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			edit_mail.setHint(new SpannedString(ss_dy_vcode));
		}else{
			mTvTitle.setText("邮箱修改");
			SpannableString ss_dy_vcode = new SpannableString("邮箱");
			ss_dy_vcode.setSpan(ass, 0, ss_dy_vcode.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			edit_mail.setHint(new SpannedString(ss_dy_vcode));
			edit_mail.setHint("新邮箱");
		}
		btn_submit = (TextView) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onSubmitData();
			}
		});
    }
    
    private void onSubmitData(){
    	String mail = edit_mail.getEdtText().toString().trim();
    	if(mail.equals("")){
//    		showToast("邮箱不能为空");
    		createDialogDismissAuto("邮箱不能为空");
    		return;
    	}
    	mProgressContainer.setVisibility(View.VISIBLE);
    	humas.gainUserMailAuth(mail, mMode);
    }

	@Override
	public void gainUserMailAuthsuccess(BaseJson response) {
		try {
			mProgressContainer.setVisibility(View.GONE);
			if(response != null){
				   String boolen = response.getBoolen();
				   String message = "";
				   btn_submit.setEnabled(true);
	               if(boolen.equals("1")){
	            	   finish();
	            	   if(mMode == 1){
	            	       message = "提交成功,去激活邮箱";
	            	   }else{
	            		   message = "绑定成功,去激活邮箱";
	            	   }
	            	  showToast(message);
	               }
	               else{
	            	   message = response.getMessage();
	            	   createDialogDismissAuto(message);
	               }
	               
			   }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gainUserMailAuthfail() {
		mProgressContainer.setVisibility(View.GONE);
	}
}
