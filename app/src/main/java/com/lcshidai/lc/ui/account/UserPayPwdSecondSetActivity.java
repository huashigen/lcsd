package com.lcshidai.lc.ui.account;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.UserPayPwdSecondSetImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.account.HttpUserPayPwdSecondSetService;
import com.lcshidai.lc.ui.MainActivity;
import com.lcshidai.lc.ui.account.pwdmanage.UserPwdManageActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.widget.AnieLayout;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 支付密码设置
 *
 */
public class UserPayPwdSecondSetActivity extends TRJActivity implements UserPayPwdSecondSetImpl{
	HttpUserPayPwdSecondSetService huppsss;
	private ImageButton mBackBtn;
	private TextView mTvTitle,mSubTitle;
	private Button mSaveBtn;
	private Drawable pwdDrawable;
	private ImageView[] pwdIVArray = new ImageView[6];
	private TextView ib_hidden, ib_delete, ib_0, ib_1, ib_2, ib_3, ib_4, ib_5, ib_6, ib_7, ib_8, ib_9;
	private Button btn_reset,btn_commit;
	private int pwdLenth = 0;
	private String inputPwd = "",firstPwd = "";
	private AnieLayout ll_pay_pwd_keyboard;
	private Dialog dialog;
	private int mFrom;
	private LinearLayout mLayoutInput;
	private int intent_from_withdrawals = 0;
	private String go_class = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag = true;
        setContentView(R.layout.activity_user_pay_pwd_second_set);
        pwdDrawable = getResources().getDrawable(R.drawable.pay_pwd_dot);
        dialog = createLoadingDialog(UserPayPwdSecondSetActivity.this, "加载中...", true);
        if(getIntent() != null){
        	firstPwd = getIntent().getStringExtra("first_pwd");
        	mFrom = getIntent().getIntExtra("from_activity",0);
        	intent_from_withdrawals = getIntent().getIntExtra("intent_from_withdrawals", 0);
        	go_class = getIntent().getStringExtra("go_class");
		}
        huppsss = new HttpUserPayPwdSecondSetService(this, this);
        initView();
        showKeyboard();
    }

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
    	mBackBtn = (ImageButton) findViewById(R.id.btn_back);
		mTvTitle = (TextView)findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("设置手机支付密码");
		mSubTitle = (TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		mSaveBtn = (Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mLayoutInput = (LinearLayout) findViewById(R.id.layout_input);
		mLayoutInput.setClickable(true);
		pwdIVArray[0] = (ImageView) findViewById(R.id.pay_pwd_iv_0);
		pwdIVArray[1] = (ImageView) findViewById(R.id.pay_pwd_iv_1);
		pwdIVArray[2] = (ImageView) findViewById(R.id.pay_pwd_iv_2);
		pwdIVArray[3] = (ImageView) findViewById(R.id.pay_pwd_iv_3);
		pwdIVArray[4] = (ImageView) findViewById(R.id.pay_pwd_iv_4);
		pwdIVArray[5] = (ImageView) findViewById(R.id.pay_pwd_iv_5);

		btn_reset = (Button) findViewById(R.id.btn_reset);
		btn_commit = (Button) findViewById(R.id.btn_commit);

		ib_hidden = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_hidden);
        ib_delete = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_delete);
        ib_0 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_0);
        ib_1 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_1);
        ib_2 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_2);
        ib_3 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_3);
        ib_4 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_4);
        ib_5 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_5);
        ib_6 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_6);
        ib_7 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_7);
        ib_8 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_8);
        ib_9 = (TextView) findViewById(R.id.pay_pwd_keyboard_ib_9);
		ll_pay_pwd_keyboard = (AnieLayout)findViewById(R.id.pay_pwd_ll_keyboard);

		mBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSendPayPwd();
			}
		});

		btn_reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserPayPwdSecondSetActivity.this, UserPayPwdFirstSetActivity.class);
				intent.putExtra("from_activity", 1);
				startActivity(intent);
				UserPayPwdSecondSetActivity.this.finish();
			}
		});
		ib_hidden.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (android.os.Build.VERSION.SDK_INT < 12) {
					ll_pay_pwd_keyboard.setVisibility(View.GONE);
					return;
				}

				final ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(
						ll_pay_pwd_keyboard, "YFraction", 0, (float) 1.5);
				movingFragmentRotator.setDuration(200);
				movingFragmentRotator.start();
				movingFragmentRotator.addListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator arg0) {
					}
					@Override
					public void onAnimationRepeat(Animator arg0) {
					}
					@Override
					public void onAnimationEnd(Animator arg0) {
						ll_pay_pwd_keyboard.setVisibility(View.GONE);
					}
					@Override
					public void onAnimationCancel(Animator arg0) {
					}
				});
			}
		});
		mLayoutInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showKeyboard();
			}
		});
		ib_delete.setOnClickListener(new BtnClick(-1));
		ib_0.setOnClickListener(new BtnClick(0));
		ib_1.setOnClickListener(new BtnClick(1));
		ib_2.setOnClickListener(new BtnClick(2));
		ib_3.setOnClickListener(new BtnClick(3));
		ib_4.setOnClickListener(new BtnClick(4));
		ib_5.setOnClickListener(new BtnClick(5));
		ib_6.setOnClickListener(new BtnClick(6));
		ib_7.setOnClickListener(new BtnClick(7));
		ib_8.setOnClickListener(new BtnClick(8));
		ib_9.setOnClickListener(new BtnClick(9));
    }

    private void onSendPayPwd(){
    	if(inputPwd.length() != 6){
    		showToast("请输入6位数字手机支付密码");
    		return;
    	}
    	if(!firstPwd.equals(inputPwd)){
    		showToast("输入密码与确认密码不一致");
    		return;
    	}
    	btn_commit.setEnabled(false);
		btn_reset.setEnabled(false);
		if(!dialog.isShowing()){
			dialog.show();
		}
		huppsss.gainUserPayPwdSecondSet(firstPwd,inputPwd);

//    	RequestParams rq = new RequestParams();
//    	rq.put("set_type", "first");
//        rq.put("password", firstPwd);
//        rq.put("password_repeat", inputPwd);
//        post(URL_PAY_PWD_UPDATE, rq, new JsonHttpResponseHandler(this){
//        	@Override
//        	public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
//        		super.onSuccess(statusCode,headers, response);
//        		try {
//        			dialog.dismiss();
//        			btn_commit.setEnabled(true);
//        			btn_reset.setEnabled(true);
//        			if(response != null){
//     				   String boolen = response.getString("boolen");
//     				   String message = "";
//						if (boolen.equals("1")) {
//							if(null != MemorySave.MS.userInfo) MemorySave.MS.userInfo.is_paypwd_mobile_set = "1";
//							if (intent_from_withdrawals == 1) {
//								try{
//									Intent intent = new Intent(UserPayPwdSecondSetActivity.this,
//											Class.forName(go_class));
//									intent.putExtra("intent_flag", "AREA");
//									intent.putExtra("set_pay_pwd", 1);
//									startActivity(intent);
//									message = "手机支付密码设置成功";
//									showToast(message);
//									UserPayPwdSecondSetActivity.this.finish();
//								}
//								catch(Exception e){
//									e.printStackTrace();
//								}
//								return ;
//							}
//							if (mFrom == 1) {
//								Intent intent = new Intent();
//								intent.setClass(
//										UserPayPwdSecondSetActivity.this,
//										UserPwdManageActivity.class);
//								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//								intent.putExtra("is_paypwd_mobile_set", "1");
//								startActivity(intent);
//							}
//							message = "手机支付密码设置成功";
//							showToast(message);
//							finish();
//						}
//     			   }
//    			} catch (JSONException e) {
//    				e.printStackTrace();
//    			}
//
//        	}
//        	@Override
//        	public void onFailure(int statusCode, Header[] headers,
//					String responseString, Throwable throwable) {
//        		dialog.dismiss();
//        		btn_commit.setEnabled(true);
//        		btn_reset.setEnabled(true);
//        	}
//        	
//        });
    }

    class BtnClick implements OnClickListener{

		int value;

		public BtnClick(int value){
			this.value=value;
		}

		@Override
		public void onClick(View v) {
			if(pwdLenth == 6 && value != -1){
				return ;
			}
			switch(value){
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				pwdLenth++;
				inputPwd += String.valueOf(value);
				pwdIVArray[pwdLenth - 1].setImageDrawable(pwdDrawable);
				break;
			case -1:
				if(pwdLenth == 0){
					return ;
				}else{
					pwdLenth--;
					inputPwd = inputPwd.substring(0, inputPwd.length() - 1);
					pwdIVArray[pwdLenth].setImageDrawable(null);
				}
				break;
			}
		}
	}

    private void showKeyboard(){
    	int visibility = ll_pay_pwd_keyboard.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			if (android.os.Build.VERSION.SDK_INT < 12){
				ll_pay_pwd_keyboard.setVisibility(View.VISIBLE);
				return;
			}
			ll_pay_pwd_keyboard.setVisibility(View.INVISIBLE);
			final ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(ll_pay_pwd_keyboard, "YFraction", 1, 0);
			movingFragmentRotator.setDuration(200);
			movingFragmentRotator.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator arg0) {
					ll_pay_pwd_keyboard.setVisibility(View.VISIBLE);
				}
				@Override
				public void onAnimationRepeat(Animator arg0) {
				}
				@Override
				public void onAnimationEnd(Animator arg0) {
				}
				@Override
				public void onAnimationCancel(Animator arg0) {
				}
			});
			movingFragmentRotator.start();
		}
    }

	@Override
	public void gainUserPayPwdSecondSetsuccess(BaseJson response) {
		try {
			dialog.dismiss();
			btn_commit.setEnabled(true);
			btn_reset.setEnabled(true);
			if(response != null){
				   String boolen = response.getBoolen();
				   String message = "";
				if (boolen.equals("1")) {
					if(null != MemorySave.MS.userInfo) {
						MemorySave.MS.userInfo.is_paypwd_mobile_set = "1";
					}
					if (intent_from_withdrawals == 1) {
						try{
                            Intent intent;
                            if (!CommonUtil.isNullOrEmpty(go_class)) {
                                intent = new Intent(UserPayPwdSecondSetActivity.this, Class.forName(go_class));
                            } else {
                                intent = new Intent(UserPayPwdSecondSetActivity.this, MainActivity.class);
                            }
							intent.putExtra("intent_flag", "AREA");
							intent.putExtra("set_pay_pwd", 1);
							startActivity(intent);
							message = "手机支付密码设置成功";
							showToast(message);
							GoLoginUtil.setPayPswSetFlag(1, mContext);
							UserPayPwdSecondSetActivity.this.finish();
						}
						catch(Exception e){
							e.printStackTrace();
						}
						return ;
					}
					if (mFrom == 1) {
						Intent intent = new Intent();
						intent.setClass(
								UserPayPwdSecondSetActivity.this,
								UserPwdManageActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("is_paypwd_mobile_set", "1");
						startActivity(intent);
					}
					message = "手机支付密码设置成功";
					showToast(message);
					GoLoginUtil.setPayPswSetFlag(1, mContext);
					finish();
				} else {
					createDialogDismissAuto(response.getMessage());
				}
			   }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gainUserPayPwdSecondSetfail() {
		dialog.dismiss();
		GoLoginUtil.setPayPswSetFlag(0, mContext);
		btn_commit.setEnabled(true);
		btn_reset.setEnabled(true);
	}
}
