package com.lcshidai.lc.ui.more;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 客服
 * 
 * @author
 * 
 */
public class HelpActivity extends TRJActivity implements
	  OnClickListener {

	private Dialog telephoneDialog;
	private Button mSaveBtn;
	ImageButton mBackBtn;
	private TextView mTvTitle,mSubTitle;
	private LinearLayout linear_phone;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);
		setContentView(R.layout.activity_question_help);
		initView();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView() {
 

		 
		mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("联系客服");
		mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		mBackBtn = (ImageButton) findViewById(R.id.btn_back);
		mBackBtn.setOnClickListener(this);

		mSaveBtn = (Button) findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.GONE);
		linear_phone = (LinearLayout) findViewById(R.id.linear_phone);
		linear_phone.setOnClickListener(this);
		telephoneDialog = createDialog("拨打" + getResources().getString(R.string.help_phone) + "？", "拨打", "取消",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (telephoneDialog.isShowing())
							telephoneDialog.dismiss();
						Intent phoneIntent = new Intent(Intent.ACTION_CALL,
								Uri.parse("tel:"
										+ getResources().getString(R.string.help_phone).replaceAll("-", "")));
						startActivity(phoneIntent);
					}
				}, new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (telephoneDialog.isShowing())
							telephoneDialog.dismiss();
					}
				});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			 HelpActivity.this.finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	 
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			HelpActivity.this.finish();
			break;
		case R.id.linear_phone:
			if (!telephoneDialog.isShowing())
				telephoneDialog.show();
			break;
		}

	}

}