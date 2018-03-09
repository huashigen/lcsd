package com.lcshidai.lc.ui.account;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.UserMailUpdaterImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.account.HttpUserMailUpdaterService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

/**
 * 用户邮箱修改(第一步)
 * 
 */
public class UserMailUpdaterActivity extends TRJActivity implements
		UserMailUpdaterImpl {
	HttpUserMailUpdaterService humus;
	private ImageButton mBackBtn;
	private TextView mTvTitle, mSubTitle;
	private Button mSaveBtn;
	private CustomEditTextLeftIcon edit_current_mail, edit_pwd;
	private Button btn_submit;
	private View mProgressContainer;
	private TextView tv_show;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_mail_updater);
		humus = new HttpUserMailUpdaterService(this, this);
		initView();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView() {
		mBackBtn = (ImageButton) findViewById(R.id.btn_back);
		mBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("邮箱修改");
		mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		mSaveBtn = (Button) findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mProgressContainer = findViewById(R.id.progressContainer);
		mProgressContainer.setVisibility(View.GONE);
		tv_show = (TextView) findViewById(R.id.tv_show);
		tv_show.setText("发送中……");
		edit_current_mail = (CustomEditTextLeftIcon) findViewById(R.id.edit_current_mail);
		edit_pwd = (CustomEditTextLeftIcon) findViewById(R.id.edit_pwd);
		edit_pwd.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		edit_current_mail.setHint("当前邮箱");
		edit_pwd.setHint("登录密码");
		Drawable draw_email_icon = getResources().getDrawable(
				R.drawable.icon_email);
		Drawable draw_pwd_icon = getResources()
				.getDrawable(R.drawable.icon_pwd);
		edit_current_mail.setIcon(draw_email_icon);
		edit_pwd.setIcon(draw_pwd_icon);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSubmitData();
			}
		});
	}

	private void onSubmitData() {
		String mail = edit_current_mail.getEdtText().toString().trim();
		String pwd = edit_pwd.getEdtText().toString().trim();
		if (mail.equals("")) {
			// showToast("当前邮箱不能为空");
			createDialogDismissAuto("当前邮箱不能为空");
			return;
		}
		if (pwd.equals("")) {
			// showToast("登录密码不能为空");
			createDialogDismissAuto("登录密码不能为空");
			return;
		}
		mProgressContainer.setVisibility(View.VISIBLE);
		humus.gainUserMailUpdater(mail, pwd);

	}

	@Override
	public void gainUserMailUpdatersuccess(BaseJson response) {
		try {
			mProgressContainer.setVisibility(View.GONE);
			if (response != null) {
				String boolen = response.getBoolen();
				String message = "";
				btn_submit.setEnabled(true);
				if (boolen.equals("1")) {
					Intent intent = new Intent(UserMailUpdaterActivity.this,
							UserMailAuthActivity.class);
					intent.putExtra("mode", 2);
					startActivity(intent);
					finish();
				} else {
					message = response.getMessage();
					createDialogDismissAuto(message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gainUserMailUpdaterfail() {
		mProgressContainer.setVisibility(View.GONE);
	}
}
