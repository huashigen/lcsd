package com.lcshidai.lc.ui.account.pwdmanage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.PayPwdQuestionImpl;
import com.lcshidai.lc.impl.account.PayPwdVerifyImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.PayPwdQuestionJson;
import com.lcshidai.lc.model.account.PayPwdQuestionStrJson;
import com.lcshidai.lc.model.account.PayPwdVerifyJson;
import com.lcshidai.lc.service.account.HttpPayPwdQuestionService;
import com.lcshidai.lc.service.account.HttpPayPwdVerifyService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

/**
 * 各途径重置手机支付密码
 * intent_type:0-实名信息,1-银行卡,2-安保问题
 *
 * @author 000853
 */
public class PayPwdChannelVerifyActivity extends TRJActivity implements PayPwdVerifyImpl, PayPwdQuestionImpl {
    HttpPayPwdVerifyService hppvs;
    HttpPayPwdQuestionService hppqs;

    private int intentType = -1;

    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    ImageButton mBackBtn;
    private LinearLayout type_ll_1, type_ll_2;
    private CustomEditTextLeftIcon et_1, et_2, et_answer;
    private RelativeLayout rl_next;
    private TextView question_tv, tv_pwd_show;
    private String selectQuestionCodeNo = "";
    private String questionName = "";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (getIntent() != null) {
            intentType = getIntent().getIntExtra("intent_type", -1);
        }
        hppvs = new HttpPayPwdVerifyService(this, this);
        hppqs = new HttpPayPwdQuestionService(this, this);
        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        setContentView(R.layout.activity_pay_pwd_channel_verify);
        mBackBtn = (ImageButton) this.findViewById(R.id.btn_back);
        mBackBtn.setVisibility(View.VISIBLE);
        mSaveBtn = (Button) this.findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) this.findViewById(R.id.tv_top_bar_title);
        mSubTitle = (TextView) this.findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);

        tv_pwd_show = (TextView) this.findViewById(R.id.tv_pwd_show);
        type_ll_1 = (LinearLayout) findViewById(R.id.forget_pwd_channel_verity_ll1);
        type_ll_2 = (LinearLayout) findViewById(R.id.forget_pwd_channel_verity_ll2);
        rl_next = (RelativeLayout) findViewById(R.id.rl_next);

        mBackBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(13);
                finish();
            }
        });

        switch (intentType) {
            case 0:
                mTvTitle.setText("实名校验");
                rl_next.setVisibility(View.VISIBLE);
                type_ll_1.setVisibility(View.VISIBLE);
                initAutoVerify();
//			request_url = AUTH_VERIFY_URL;
                break;
            case 1:
                rl_next.setVisibility(View.VISIBLE);
                type_ll_1.setVisibility(View.VISIBLE);
                initBankCardVerify();
//			request_url = BANKCARD_VERIFY_URL;
                break;
            case 2:
                mTvTitle.setText("安全保护问题校验");
                rl_next.setVisibility(View.VISIBLE);
                type_ll_2.setVisibility(View.VISIBLE);
                initSafeVerify();
//			request_url = SAFE_VERIFY_URL;
                break;
        }

        rl_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    /**
     * 通过实名信息修改密码验证
     */
    private void initAutoVerify() {
        Drawable draw_uname_icon = getResources().getDrawable(R.drawable.icon_account);
        Drawable draw_user_card_icon = getResources().getDrawable(R.drawable.user_card);
        tv_pwd_show.setText("请输入实名信息来验证身份");
        et_1 = (CustomEditTextLeftIcon) findViewById(R.id.forget_pwd_channel_verity_et1);
        et_2 = (CustomEditTextLeftIcon) findViewById(R.id.forget_pwd_channel_verity_et2);
        et_1.setHint("姓名");
        et_2.setHint("二代身份证号码");
        et_1.setTextSize(14);
        et_2.setTextSize(14);
        et_1.setIcon(draw_uname_icon);
        et_2.setIcon(draw_user_card_icon);
    }

    /**
     * 通过绑定银行卡信息修改密码验证
     */
    private void initBankCardVerify() {
        Drawable draw_bank_card_icon = getResources().getDrawable(R.drawable.bank_card);
        Drawable draw_user_card_icon = getResources().getDrawable(R.drawable.user_card);
        tv_pwd_show.setText("请输入绑定银行卡号来验证身份");
        et_1 = (CustomEditTextLeftIcon) findViewById(R.id.forget_pwd_channel_verity_et1);
        et_2 = (CustomEditTextLeftIcon) findViewById(R.id.forget_pwd_channel_verity_et2);
        et_1.setHint("银行卡号");
        et_2.setHint("持卡人证件号码");
        et_1.setIcon(draw_bank_card_icon);
        et_2.setIcon(draw_user_card_icon);
    }

    /**
     * 通过安保问题修改密码验证
     */
    private void initSafeVerify() {
        Drawable draw_user_answer_icon = getResources().getDrawable(R.drawable.img_saft_edit);
        et_answer = (CustomEditTextLeftIcon) findViewById(R.id.forget_pwd_channel_verity_et_answer);
        et_answer.setHint("答案");
        et_answer.setTextSize(14);
        et_answer.setIcon(draw_user_answer_icon);
//		question_selecte_ll = (LinearLayout) findViewById(R.id.safe_question_ll_select);
        question_tv = (TextView) findViewById(R.id.safe_question_tv_question);
//		question_selecte_ll.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(PayPwdChannelVerifyActivity.this, SafeQuestionSelectActivity.class);
//				intent.putExtra("select_quetion_code_no", selectQuestionCodeNo);
//				intent.putExtra("question_name", questionName);
//				startActivityForResult(intent, 10);
//			}
//		});
        loadQuestionData();
    }

    private void loadQuestionData() {
        hppqs.gainPayPwdQuestion();
        /*post(GET_QUESTION_URL, new RequestParams(), new JsonHttpResponseHandler(){
            @Override
			public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
				try {
    				if(response != null){
    				   String boolen = response.getString("boolen");
    	               if(boolen.equals("1")){
    	            	   JSONObject dataObj = response.getJSONObject("data");
    	            	   selectQuestionCodeNo = dataObj.getString("code_no");
    	            	   questionName = dataObj.getString("code_name");
    	            	   question_tv.setText("安保问题：" + questionName);
    	               }else{
    	            	   if("-1".equals(response.getString("data"))){
    	            		   ToastUtil.showToast(PayPwdChannelVerifyActivity.this, "手机确认超时，请完成手机确认");
    	            		   setResult(11);
    	            		   finish();
    	            	   }else{
	    	            	   String message = response.getString("message");
	    	            	   ToastUtil.showToast(PayPwdChannelVerifyActivity.this, message);
    	            	   }
    	               }
    			   }
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				ToastUtil.showToast(PayPwdChannelVerifyActivity.this, "网络不给力");
			}
		});*/
    }

    private void loadData() {
        hppvs.gainPayPwdVerify(intentType, et_1, et_2, selectQuestionCodeNo, et_answer);
    /*	RequestParams params = new RequestParams();
		if(intentType == 0){
			params.put("real_name", et_1.getEdtText());
			params.put("person_id", et_2.getEdtText());
		}else if(intentType == 1){
			params.put("account_no", et_1.getEdtText());
			params.put("person_id", et_2.getEdtText());
		}else if(intentType == 2){
			params.put("sqa_key", selectQuestionCodeNo);
			params.put("sqa_value", et_answer.getEdtText());
		}
		post(request_url, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
				try {
    				if(response != null){
    				   String boolen = response.getString("boolen");
    	               if(boolen.equals("1")){
    	            	   Intent intent = new Intent(PayPwdChannelVerifyActivity.this, PayPwdGeneralActivity.class);
    	            	   if(intentType == 0){
    	            		   intent.putExtra("set_type", "identity");
    	            	   }else if(intentType == 1){
    	            		   intent.putExtra("set_type", "bank");
    	            	   }else if(intentType == 2){
    	            		   intent.putExtra("set_type", "sqa");
    	            	   }
    	            	   startActivityForResult(intent, 20);
    	               }else{
    	            	   if("-1".equals(response.getString("data"))){
    	            		   ToastUtil.showToast(PayPwdChannelVerifyActivity.this, "手机确认超时，请完成手机确认");
    	            		   setResult(11);
    	            		   finish();
    	            	   }else{
	    	            	   String message = response.getString("message");
	    	            	   ToastUtil.showToast(PayPwdChannelVerifyActivity.this, message);
    	            	   }
    	               }
    			   }
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				ToastUtil.showToast(PayPwdChannelVerifyActivity.this, "网络不给力");
			}
		});*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(13);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //失效
        if (requestCode == 20 && resultCode == 21) {
            setResult(11);
            finish();
        }
        //返回或者修改成功
        if (requestCode == 20 && resultCode == 22) {
            setResult(12);
            finish();
        }
        //选择密码保护问题
        if (requestCode == 10 && resultCode == 20) {
            selectQuestionCodeNo = data.getStringExtra("select_quetion_code_no");
            if (!"".equals(selectQuestionCodeNo)) {
                questionName = data.getStringExtra("question_name");
                question_tv.setText(questionName);
                question_tv.setTextColor(Color.parseColor("#333333"));
            }
        }
    }

    @Override
    public void doFinish() {
        setResult(13);
        super.doFinish();
    }

    @Override
    public void gainPayPwdQuestionsuccess(BaseJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    PayPwdQuestionJson dataObj = (PayPwdQuestionJson) response;
                    selectQuestionCodeNo = dataObj.getData().getCode_no();
                    questionName = dataObj.getData().getCode_name();
                    question_tv.setText(questionName);
                } else {
                    PayPwdQuestionStrJson dataObj = (PayPwdQuestionStrJson) response;
                    if ("-1".equals(dataObj.getData())) {
                        ToastUtil.showToast(PayPwdChannelVerifyActivity.this, "手机确认超时，请完成手机确认");
                        setResult(11);
                        finish();
                    } else {
                        String message = response.getMessage();
                        ToastUtil.showToast(PayPwdChannelVerifyActivity.this, message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainPayPwdQuestionfail() {
        ToastUtil.showToast(PayPwdChannelVerifyActivity.this, "网络不给力");
    }

    @Override
    public void gainPayPwdVerifysuccess(PayPwdVerifyJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    Intent intent = new Intent(mContext, PayPwdGeneralActivity.class);
                    if (intentType == 0) {
                        intent.putExtra("set_type", "identity");
                    } else if (intentType == 1) {
                        intent.putExtra("set_type", "bank");
                    } else if (intentType == 2) {
                        intent.putExtra("set_type", "sqa");
                    }
                    startActivityForResult(intent, 20);
                } else {
                    if ("-1".equals(response.getData())) {
                        ToastUtil.showToast(PayPwdChannelVerifyActivity.this, "手机确认超时，请完成手机确认");
                        setResult(11);
                        finish();
                    } else {
                        String message = response.getMessage();
//	            	   ToastUtil.showToast(PayPwdChannelVerifyActivity.this, message);
                        createDialogDismissAuto(message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainPayPwdVerifyfail() {
        ToastUtil.showToast(PayPwdChannelVerifyActivity.this, "网络不给力");
    }

}
