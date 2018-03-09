package com.lcshidai.lc.ui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.UserMobileAuthImpl;
import com.lcshidai.lc.impl.account.UserMobileAuthSendcodeImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.account.HttpUserMobileAuthSendcodeService;
import com.lcshidai.lc.service.account.HttpUserMobileAuthService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.SmsAutoUtil;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

/**
 * 用户手机认证(手机修改最后一步)
 *
 * @author
 */
public class UserMobileAuthActivity extends TRJActivity implements UserMobileAuthImpl, UserMobileAuthSendcodeImpl {
    HttpUserMobileAuthService humas;
    HttpUserMobileAuthSendcodeService humass;
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    private CustomEditTextLeftIcon edit_mobile, edit_dynamic_code;
    private View mProgressContainer;
    private TimeCount mTime;
    private Button btn_dy_code, btn_submit;
    private int mMode;//1-认证，2-修改
    private TextView tv_show;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mobile_auth);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mMode = bundle.getInt("mode");
        }
        humas = new HttpUserMobileAuthService(this, this);
        humass = new HttpUserMobileAuthSendcodeService(this, this);
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

        Drawable draw_dy_code_icon = getResources().getDrawable(R.drawable.dy_code);
        Drawable draw_mobile_icon = getResources().getDrawable(R.drawable.icon_mobile);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mProgressContainer = findViewById(R.id.progressContainer);
        mProgressContainer.setVisibility(View.GONE);
        tv_show = (TextView) findViewById(R.id.tv_show);
        tv_show.setText("发送中……");
        edit_mobile = (CustomEditTextLeftIcon) findViewById(R.id.edit_mobile);
        edit_dynamic_code = (CustomEditTextLeftIcon) findViewById(R.id.edit_dynamic_code);
        edit_mobile.setIcon(draw_mobile_icon);
        edit_dynamic_code.setIcon(draw_dy_code_icon);
        if (mMode == 1) {
            mTvTitle.setText("手机认证");
            edit_mobile.setHint("手机号码");
            edit_dynamic_code.setHint("手机动态码");
        } else {
            mTvTitle.setText("手机修改");
            edit_mobile.setHint("新手机号码");
            edit_dynamic_code.setHint("手机动态码");
        }
        btn_dy_code = (Button) findViewById(R.id.btn_gain_dn);
        btn_dy_code.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendDyCode();
            }
        });
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_submit.setEnabled(false);
                onSubmitData();
            }
        });
        mTime = new TimeCount(120000, 1000);//构造CountDownTimer对象
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            // mBtnGainDn.setText(" 重新获取 ");
            btn_dy_code.setText("重新发送");
            btn_dy_code.setEnabled(true);
            btn_dy_code.setClickable(true);
//			btn_dy_code.setBackgroundResource(R.drawable.resend_code_bg);
            btn_dy_code.setBackgroundColor(getResources().getColor(R.color.color_3));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            btn_dy_code.setClickable(false);
//			btn_dy_code.setBackgroundResource(R.drawable.bg_button_cancel);
            btn_dy_code.setBackgroundColor(getResources().getColor(R.color.sended_color));
            btn_dy_code.setText(
                    millisUntilFinished / 1000 + "s重新发送");
        }
    }

    private void onSendDyCode() {
        String mobile = edit_mobile.getEdtText().toString().trim();
        if (mobile.equals("")) {
//    		showToast("手机号码不能为空");
            createDialogDismissAuto("手机号码不能为空");
            return;
        }
//		String url = "";
//		if(mMode == 1){
//			url = URL_SEND_AUTH_CODE;
//		}else{
//			url = URL_SEND_EDIT_CODE;
//		}
        humass.gainUserMobileAuthSendcode(mobile, mMode);

//		RequestParams rq =new RequestParams();
//		rq.put("mobile", mobile);
//		post(url, rq, new JsonHttpResponseHandler(){
//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//					JSONObject response) {
//        		try {
//    				if(response != null){
//    				   String boolen = response.getString("boolen");
//    				   String message = "";
//    	               if(boolen.equals("1")){
//    	            	   message = "发送成功";
//    	            	   mTime.start();//开始计时
//    	               }else{
//    	            	   message = response.getString("message");
//    	               }
//    	               showToast(message);
//    			   }
//    			} catch (JSONException e) {
//    				e.printStackTrace();
//    			}
//        	}
//		});

        try {
            SmsAutoUtil.getInstance().startWork(
                    UserMobileAuthActivity.this, new Handler(), edit_dynamic_code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSubmitData() {
        String mobile = edit_mobile.getEdtText().toString().trim();
        String dy_code = edit_dynamic_code.getEdtText().toString().trim();
        if (mobile.equals("")) {
//    		showToast("手机号码不能为空");
            createDialogDismissAuto("手机号码不能为空");
            return;
        }

        if (dy_code.equals("")) {
//    		showToast("动态码不能为空");
            createDialogDismissAuto("动态码不能为空");
            return;
        }
        mProgressContainer.setVisibility(View.VISIBLE);
        humas.gainUserMobileAuth(mobile, dy_code, mMode);
    }

    @Override
    protected void onDestroy() {
        try {
            SmsAutoUtil.getInstance().stopWork(UserMobileAuthActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void gainUserMobileAuthSendcodesuccess(BaseJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                String message = "";
                if (boolen.equals("1")) {
                    message = "发送成功";
                    mTime.start();//开始计时
                } else {
                    message = response.getMessage();
                }
                createDialogDismissAuto(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainUserMobileAuthSendcodefail() {

    }

    @Override
    public void gainUserMobileAuthsuccess(BaseJson response) {
        try {
            mProgressContainer.setVisibility(View.GONE);
            btn_submit.setEnabled(true);
            if (response != null) {
                String boolen = response.getBoolen();
                String message = "";
                if (boolen.equals("1")) {
                    finish();
                    message = "绑定成功";
                    showToast(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainUserMobileAuthfail() {

    }
}