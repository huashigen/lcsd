package com.lcshidai.lc.ui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * 用户实名认证
 *
 */
public class UserNameAuthActivity extends TRJActivity{
	private static final String URL_SEND_DATA = "Mobile2/Auth/authId";
	private ImageButton mBackBtn;
	private TextView mTvTitle,mSubTitle;
	private Button mSaveBtn;
	private CustomEditTextLeftIcon edit_name,edit_card;
	private Button btn_submit;
	private View mProgressContainer;
	private TextView tv_show;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name_auth);
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
		mTvTitle.setText("实名认证");
		mSubTitle = (TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		mSaveBtn = (Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mProgressContainer = findViewById(R.id.progressContainer);
		mProgressContainer.setVisibility(View.GONE);
		tv_show = (TextView)findViewById(R.id.tv_show);
		tv_show.setText("发送中……");
		edit_name = (CustomEditTextLeftIcon)findViewById(R.id.edit_name);
		edit_card = (CustomEditTextLeftIcon)findViewById(R.id.edit_card);
		edit_name.setHint("姓名");
		edit_card.setHint("身份证号码");
		Drawable draw_uname_icon = getResources().getDrawable(R.drawable.dy_code);
		Drawable draw_mobile_num_icon = getResources().getDrawable(R.drawable.icon_mobile);
		edit_name.setIcon(draw_uname_icon);
		edit_card.setIcon(draw_mobile_num_icon);
		btn_submit = (Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onSubmitData();
			}
		});
    }
    
    private void onSubmitData(){
    	String name = edit_name.getEdtText().toString().trim();
    	String card = edit_card.getEdtText().toString().trim();
    	if(name.equals("")){
    		showToast("姓名不能为空");
    		return;
    	}
    	if(card.equals("")){
    		showToast("身份证号码不能为空");
    		return;
    	}
    	btn_submit.setEnabled(false);
    	mProgressContainer.setVisibility(View.VISIBLE);
    	RequestParams rq = new RequestParams();
        rq.put("real_name", name);
        rq.put("person_id", card);
        post(URL_SEND_DATA, rq, new JsonHttpResponseHandler(this){
        	@Override
        	public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
        		super.onSuccess(statusCode,headers, response);
        		try {
        			mProgressContainer.setVisibility(View.GONE);
        			btn_submit.setEnabled(true);
        			if(response != null){
     				   String boolen = response.getString("boolen");
     				   String message = "";
     	               if(boolen.equals("1")){
     	            	   finish();
     	            	   message = "提交成功";
     	            	   MemorySave.MS.userInfo.is_id_auth = "1";
     	            	   showToast(message);
     	               }
//     	               else{
//     	            	   message = response.getString("message");
//     	               }
     	               
     			   }
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}

        	}
        	@Override
        	public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
        		mProgressContainer.setVisibility(View.GONE);
        		btn_submit.setEnabled(true);
        	}
        	
        });
    }
}
