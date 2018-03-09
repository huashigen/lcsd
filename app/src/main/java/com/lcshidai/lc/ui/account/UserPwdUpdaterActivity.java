package com.lcshidai.lc.ui.account;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.UserPwdUpdaterImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.account.HttpUserPwdUpdaterService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIconPwdNew;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 用户密码修改
 *
 */
public class UserPwdUpdaterActivity extends TRJActivity implements UserPwdUpdaterImpl{
	HttpUserPwdUpdaterService hupus;
	private ImageButton mBackBtn;
	private TextView mTvTitle,mSubTitle;
	private Button mSaveBtn;
	private CustomEditTextLeftIconPwdNew edit_current_pwd,edit_new_pwd,edit_again_pwd;
	private Button btn_submit;
	private View mProgressContainer;
	private TextView tv_show,tv_note;
	private int mMode;//1-登录密码，2-支付密码
	private String mTitle;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pwd_updater);
        Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mMode = bundle.getInt("mode");
			mTitle = bundle.getString("title");
		}
        initView();
        hupus = new HttpUserPwdUpdaterService(this, this);
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
		Drawable draw_wjmm_icon = getResources().getDrawable(R.drawable.icon_pwd);
		mTvTitle = (TextView)findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText(mTitle);
		mSubTitle = (TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		mSaveBtn = (Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mProgressContainer = findViewById(R.id.progressContainer);
		mProgressContainer.setVisibility(View.GONE);
		tv_show = (TextView)findViewById(R.id.tv_show);
		tv_show.setText("发送中……");
		edit_current_pwd = (CustomEditTextLeftIconPwdNew)findViewById(R.id.edit_current_pwd);
		edit_new_pwd = (CustomEditTextLeftIconPwdNew)findViewById(R.id.edit_new_pwd);
		edit_again_pwd = (CustomEditTextLeftIconPwdNew)findViewById(R.id.edit_again_pwd);
		edit_current_pwd.setIcon(draw_wjmm_icon);
		edit_new_pwd.setIcon(draw_wjmm_icon);
		edit_again_pwd.setIcon(draw_wjmm_icon);
		tv_note = (TextView)findViewById(R.id.tv_note);
		edit_current_pwd.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
		edit_new_pwd.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
		edit_again_pwd.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);

		AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);
		SpannableString ss_dy_vcode = new SpannableString("当前密码");
		SpannableString ss_dy_vcode1 = new SpannableString("新密码");
		SpannableString ss_dy_vcode2 = new SpannableString("确认新密码");
		ss_dy_vcode.setSpan(ass, 0, ss_dy_vcode.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		edit_current_pwd.setHint(new SpannedString(ss_dy_vcode));
		ss_dy_vcode1.setSpan(ass, 0, ss_dy_vcode1.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		edit_current_pwd.setHint(new SpannedString(ss_dy_vcode));
		ss_dy_vcode2.setSpan(ass, 0, ss_dy_vcode2.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		edit_current_pwd.setHint(new SpannedString(ss_dy_vcode));
		edit_new_pwd.setHint(new SpannedString(ss_dy_vcode1));
		edit_again_pwd.setHint(new SpannedString(ss_dy_vcode2));
		Drawable draw_pwd_icon = getResources().getDrawable(R.drawable.icon_pwd);
//		edit_current_pwd.setIcon(draw_pwd_icon);
//		edit_new_pwd.setIcon(draw_pwd_icon);
//		edit_again_pwd.setIcon(draw_pwd_icon);
		if(mMode == 1){
			tv_note.setVisibility(View.GONE);
		}else{
			tv_note.setVisibility(View.VISIBLE);
			tv_note.setText("初始的支付密码与登录密码相同");
		}
		btn_submit = (Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onSubmitData();
			}
		});
    }
    
    private void onSubmitData(){
    	String old_pwd = edit_current_pwd.getEdtText().toString().trim();
    	String new_pwd = edit_new_pwd.getEdtText().toString().trim();
    	String again_pwd = edit_again_pwd.getEdtText().toString().trim();
    	if(old_pwd.equals("")){
//    		showToast("当前密码不能为空");
     	   createDialogDismissAuto("当前密码不能为空");
    		return;
    	}
    	if(new_pwd.equals("")){
//    		showToast("新密码不能为空");
    		createDialogDismissAuto("新密码不能为空");
    		return;
    	}
    	if(again_pwd.equals("")){
//    		showToast("确认新密码不能为空");
    		createDialogDismissAuto("确认新密码不能为空");
    		return;
    	}
    	if(!new_pwd.equals(again_pwd)){
//    		showToast("新密码与确认新密码不一致");
    		createDialogDismissAuto("新密码与确认新密码不一致");
    		return;
    	}
    	btn_submit.setEnabled(false);
    	mProgressContainer.setVisibility(View.VISIBLE);
    	hupus.gainUserPwdUpdater(old_pwd,new_pwd,again_pwd,mMode);
    }

	@Override
	public void gainUserPwdUpdatersuccess(BaseJson response) {
		try {
			mProgressContainer.setVisibility(View.GONE);
			btn_submit.setEnabled(true);
			if(response != null){
				   String boolen = response.getBoolen();
				   String message = "";
	               if(boolen.equals("1")){
	            	   finish();
	            	   message = "提交成功";
	            	   showToast(message);
	               } else {
	            	   createDialogDismissAuto(response.getMessage());
	               }
			   }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gainUserPwdUpdaterfail() {
		mProgressContainer.setVisibility(View.GONE);
	}
}
