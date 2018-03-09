package com.lcshidai.lc.ui.more;

import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.OneKeyShareUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qq.QQ;

/**
 * 最新动态详细信息
 * @author 000853
 *
 */
public class DynamicInfoActivity extends TRJActivity implements OnClickListener {
	
	private final String DYNAMIC_INFO_URL = "Mobile2/Public/getNewsContent";
	private final String URL_GET_DATA = "Mobile2/Share/getNews";
	private String DEVICE_ID = "";
	
	private Context mContext;
	private TextView mTvTitle, mSubTitle;
	private Button  mSaveBtn;
	ImageButton mBackBtn;
	private TextView title_tv;
	private WebView content_wv;
	private Button pre_bt, next_bt, share_bt;
	private String dynamic_id;
	private Dialog loading;
	private String prevId = "0";
	private String nextId = "0";
	private OneKeyShareUtil oneKeyShareUtil;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if(null != getIntent()){
			dynamic_id = getIntent().getStringExtra("dynamic_id");
		}
		initView();
		loadData(dynamic_id);
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
		mContext = this;
		setContentView(R.layout.layout_dynamic_info);
		mBackBtn=(ImageButton) findViewById(R.id.btn_back);
		mBackBtn.setOnClickListener(this);
		mSaveBtn=(Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mTvTitle=(TextView)findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("动态详情");
		mSubTitle=(TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		
		oneKeyShareUtil = new OneKeyShareUtil(mContext);
		
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		DEVICE_ID = tm.getDeviceId();
		
		title_tv = (TextView) findViewById(R.id.dynamic_info_title_tv);
		content_wv = (WebView) findViewById(R.id.dynamic_info_content_wv);
		pre_bt = (Button) findViewById(R.id.dynamic_info_pre_bt);
		next_bt = (Button) findViewById(R.id.dynamic_info_next_bt);
		share_bt = (Button) findViewById(R.id.dynamic_info_share_bt);
		
		pre_bt.setOnClickListener(this);
		next_bt.setOnClickListener(this);
		share_bt.setOnClickListener(this);
		
		loading = createLoadingDialog(mContext, "加载中", true);
		
		pre_bt.setClickable(false);
		pre_bt.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_clickable_false));
		next_bt.setClickable(false);
		next_bt.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_clickable_false));
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		//返回
		case R.id.btn_back:
			DynamicInfoActivity.this.finish();
			break;
		//上一篇
		case R.id.dynamic_info_pre_bt:
//			if(idPosition == idArray.length -1){
//				next_bt.setClickable(true);
//				next_bt.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_submit_bg_xml));
//			}
//			idPosition--;
//			if(idPosition == 0){
//				pre_bt.setClickable(false);
//				pre_bt.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_clickable_false));
//			}
			loadData(prevId);
			break;
		//下一篇
		case R.id.dynamic_info_next_bt:
//			if(idPosition == 0){
//				pre_bt.setClickable(true);
//				pre_bt.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_submit_bg_xml));
//			}
//			idPosition++;
//			if(idPosition == idArray.length - 1){
//				next_bt.setClickable(false);
//				next_bt.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_clickable_false));
//			}
			loadData(nextId);
			break;
		//分享
		case R.id.dynamic_info_share_bt:
			toShare();
			break;
		}
	}
	
	private void loadData(String id){
		if(!loading.isShowing()){
			loading.show();
		}
		RequestParams params = new RequestParams();
		params.put("device_no", DEVICE_ID);
		params.put("news_id", id);
		post(DYNAMIC_INFO_URL, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if(loading.isShowing()){
					loading.dismiss();
				}
				try {
					if(null != response){
						String boolen = response.getString("boolen");
						if("1".equals(boolen)){
							JSONObject dataObj = response.getJSONObject("data");
							String title = dataObj.getString("title");
							String content = dataObj.getString("content");
							JSONObject pageObj = dataObj.getJSONObject("page");
							String type_display = dataObj.getString("type_display");
							prevId = pageObj.getString("prev");
							nextId = pageObj.getString("next");
							
							title_tv.setText(title);
 							content_wv.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
							
							if("0".equals(prevId)){
								pre_bt.setClickable(false);
								pre_bt.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_clickable_false));
							}else{
								pre_bt.setClickable(true);
								pre_bt.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_submit_bg_xml));
							}
							
							if("0".equals(nextId)){
								next_bt.setClickable(false);
								next_bt.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_clickable_false));
							}else{
								next_bt.setClickable(true);
								next_bt.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_submit_bg_xml));
							}
						}
					}
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				showToast("网络不给力");
				if(loading.isShowing()){
					loading.dismiss();
				}
			}
		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		dynamic_id = intent.getStringExtra("dynamic_id");
		loadData(dynamic_id);
		super.onNewIntent(intent);
	}
	
	
	/**
	 * 分享
	 */
	private void toShare(){
		RequestParams rq = new RequestParams();
		rq.put("id", dynamic_id);
		post(URL_GET_DATA, rq, new JsonHttpResponseHandler(this){
        	@Override
        	public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
        		super.onSuccess(statusCode,headers, response);
        		if(loading.isShowing()){
					loading.dismiss();
				}
        		try {
    				if(response != null){
	            	   if(response.getInt("boolen") == 1) {
							String content = response.getString("data");
							if(loading.isShowing()){
								loading.dismiss();
							} 
							oneKeyShareUtil.toShare(content, 
									new PlatformActionListener() {
										
										@Override
										public void onError(Platform arg0, int arg1, Throwable arg2) {
											showToast("分享失败");
										}
										
										@Override
										public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
											if(!arg0.getName().equals(QQ.NAME)){
												showToast("分享成功");
											}
										}
										
										@Override
										public void onCancel(Platform arg0, int arg1) {
											
										}
									});	
						}else{
//							showToast(message);
						}
    			   }
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
        	}
        	
        	@Override
        	public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
        		if(loading.isShowing()){
        			loading.dismiss();
				}
        	}
        });
	}
}
