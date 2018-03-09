package com.lcshidai.lc.ui.account;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.RechargeInfoImpl;
import com.lcshidai.lc.model.account.RechargeInfoData;
import com.lcshidai.lc.model.account.RechargeInfoJson;
import com.lcshidai.lc.service.account.HttpRechargeInfoService;
import com.lcshidai.lc.ui.base.TRJActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 充值信息页面
 * @author 000853
 *
 */
public class RechargeInfoActivity extends TRJActivity implements RechargeInfoImpl{
	HttpRechargeInfoService hris;
	private Context mContext;
	private TextView mTvTitle, mSubTitle;
	private Button  mSaveBtn;
	ImageButton mBackBtn;
	private String id;
	private String status = "2";
	
	private LinearLayout actualmoney_ll, type_ll, bank_ll, shenqingbeckup_ll, liushui_ll;
	private View actualmoney_line, type_line, bank_line, shenqingbeckup_line, liushui_line;
	private TextView time_tv, status_tv, money_tv, actualmoney_tv, type_tv, bank_tv, shenqingbeckup_tv, chltime_tv, liushui_tv, chulibeckup_tv;
	private Button submit_bt;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if(null != getIntent()){
			id = getIntent().getStringExtra("id");
			status = getIntent().getStringExtra("status");
		}
		hris = new HttpRechargeInfoService(this, this);
		initView();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
		mContext = this;
		setContentView(R.layout.activity_recharge_info);
		
		mBackBtn=(ImageButton) findViewById(R.id.btn_back);
		mBackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RechargeInfoActivity.this.finish();
			}
		});
		mSaveBtn=(Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mTvTitle=(TextView)findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("充值");
		mSubTitle=(TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		
		actualmoney_ll = (LinearLayout) findViewById(R.id.recharge_info_ll_actualmoney);
		type_ll = (LinearLayout) findViewById(R.id.recharge_info_ll_type);
		bank_ll = (LinearLayout) findViewById(R.id.recharge_info_ll_bank);
		shenqingbeckup_ll = (LinearLayout) findViewById(R.id.recharge_info_ll_shenqingbeckup);
		liushui_ll = (LinearLayout) findViewById(R.id.recharge_info_ll_liushui);
		
		actualmoney_line = findViewById(R.id.recharge_info_line_actualmoney);
		type_line = findViewById(R.id.recharge_info_line_type);
		bank_line = findViewById(R.id.recharge_info_line_bank);
		shenqingbeckup_line = findViewById(R.id.recharge_info_line_shenqingbeckup);
		liushui_line = findViewById(R.id.recharge_info_line_liushui);
		
		time_tv = (TextView) findViewById(R.id.recharge_info_vlaue_time);
		status_tv = (TextView) findViewById(R.id.recharge_info_vlaue_status);
		money_tv = (TextView) findViewById(R.id.recharge_info_vlaue_money);
		actualmoney_tv = (TextView) findViewById(R.id.recharge_info_vlaue_actualmoney);
		type_tv = (TextView) findViewById(R.id.recharge_info_vlaue_type);
		bank_tv = (TextView) findViewById(R.id.recharge_info_vlaue_bank);
		shenqingbeckup_tv = (TextView) findViewById(R.id.recharge_info_vlaue_shenqingbeckup);
		chltime_tv = (TextView) findViewById(R.id.recharge_info_vlaue_chltime);
		liushui_tv = (TextView) findViewById(R.id.recharge_info_vlaue_liushui);
		chulibeckup_tv = (TextView) findViewById(R.id.recharge_info_vlaue_chulibeckup);
		
		submit_bt = (Button) findViewById(R.id.recharge_info_button);
		submit_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		
		if("2".equals(status)){
			actualmoney_ll.setVisibility(View.VISIBLE);
			actualmoney_line.setVisibility(View.VISIBLE);
			liushui_ll.setVisibility(View.VISIBLE);
			liushui_line.setVisibility(View.VISIBLE);
			
			type_ll.setVisibility(View.GONE);
			type_line.setVisibility(View.GONE);
			bank_ll.setVisibility(View.GONE);
			bank_line.setVisibility(View.GONE);
			shenqingbeckup_ll.setVisibility(View.GONE);
			shenqingbeckup_line.setVisibility(View.GONE);
			submit_bt.setVisibility(View.GONE);
		}else{
			actualmoney_ll.setVisibility(View.GONE);
			actualmoney_line.setVisibility(View.GONE);
			liushui_ll.setVisibility(View.GONE);
			liushui_line.setVisibility(View.GONE);
			submit_bt.setVisibility(View.GONE);
			
			type_ll.setVisibility(View.VISIBLE);
			type_line.setVisibility(View.VISIBLE);
			bank_ll.setVisibility(View.VISIBLE);
			bank_line.setVisibility(View.VISIBLE);
			shenqingbeckup_ll.setVisibility(View.VISIBLE);
			shenqingbeckup_line.setVisibility(View.VISIBLE);
		}
		loadData();
	}
	
	private void loadData(){
		hris.gainRechargeInfo(id);
		/*RequestParams params = new RequestParams();
		params.put("id", id);
		post(RECHARGE_INFO_URL, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if(null != response){
						String boolen = response.getString("boolen");
						if("1".equals(boolen)){
							JSONObject dataObj = response.getJSONObject("data");
							String amount = dataObj.getString("amount");	//充值金额（元）
							String real_amount = dataObj.getString("real_amount");	//实际到账金额
							String bank_name = dataObj.getString("bank_name");	//转账银行
							String ctime = dataObj.getString("ctime");	//申请时间
							String ticket_no = dataObj.getString("ticket_no");	//流水号
							String status = dataObj.getString("status");	//状态 1-待处理 2-成功 3-失败
							String channel = dataObj.getString("channel");	// 充值方式 xianxia-线下转账 shenfut-盛付通
							String bak = dataObj.getString("bak");	//申请备注
							String deal_bak = dataObj.getString("deal_bak");	//处理备注
							String verifytime = dataObj.getString("verifytime");	//处理备注
							
							time_tv.setText(setTextToTextView(ctime));
							status_tv.setText("2".equals(status)?"成功":"失败");
							money_tv.setText(setTextToTextView(amount));
							actualmoney_tv.setText(setTextToTextView(real_amount));
							if(null != channel && !"".equals(channel)){
								if("xianxia".equals(channel)){
									type_tv.setText(setTextToTextView("线下转账"));
								}else if("shenfut".equals(channel)){
									type_tv.setText(setTextToTextView("盛付通"));
								}else if("yilian".equals(channel)){
									type_tv.setText(setTextToTextView("易联支付"));
								}else if("lianlian".equals(channel)){
									type_tv.setText(setTextToTextView("连连支付"));
								}else{
									type_tv.setText(setTextToTextView("--"));
								}
							}
							bank_tv.setText(setTextToTextView(bank_name));
							shenqingbeckup_tv.setText(setTextToTextView(bak));
							chltime_tv.setText(setTextToTextView(verifytime));
							liushui_tv.setText(setTextToTextView(ticket_no));
							chulibeckup_tv.setText(setTextToTextView(deal_bak));
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
			}
		});*/
	}
	
	private String setTextToTextView(String str){
		if(null == str || "".equals(str) || "null".equals(str)){
			return "--";
		}
		return str;
	}

	@Override
	public void gainRechargeInfosuccess(RechargeInfoJson response) {
		try {
			if(null != response){
				String boolen = response.getBoolen();
				if("1".equals(boolen)){
					RechargeInfoData dataObj = response.getData();
					String amount = dataObj.getAmount();	//充值金额（元）
					String real_amount = dataObj.getReal_amount();	//实际到账金额
					String bank_name = dataObj.getBank_name();	//转账银行
					String ctime = dataObj.getCtime();	//申请时间
					String ticket_no = dataObj.getTicket_no();	//流水号
					String status = dataObj.getStatus();	//状态 1-待处理 2-成功 3-失败
					String channel = dataObj.getChannel();	// 充值方式 xianxia-线下转账 shenfut-盛付通
					String bak = dataObj.getBak();	//申请备注
					String deal_bak = dataObj.getDeal_bak();	//处理备注
					String verifytime = dataObj.getVerifytime();	//处理备注
					
					time_tv.setText(setTextToTextView(ctime));
					status_tv.setText("2".equals(status)?"成功":"失败");
					money_tv.setText(setTextToTextView(amount));
					actualmoney_tv.setText(setTextToTextView(real_amount));
					if(null != channel && !"".equals(channel)){
						if("xianxia".equals(channel)){
							type_tv.setText(setTextToTextView("线下转账"));
						}else if("shenfut".equals(channel)){
							type_tv.setText(setTextToTextView("盛付通"));
						}else if("yilian".equals(channel)){
							type_tv.setText(setTextToTextView("易联支付"));
						}else if("lianlian".equals(channel)){
							type_tv.setText(setTextToTextView("连连支付"));
						}else{
							type_tv.setText(setTextToTextView("--"));
						}
					}
					bank_tv.setText(setTextToTextView(bank_name));
					shenqingbeckup_tv.setText(setTextToTextView(bak));
					chltime_tv.setText(setTextToTextView(verifytime));
					liushui_tv.setText(setTextToTextView(ticket_no));
					chulibeckup_tv.setText(setTextToTextView(deal_bak));
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gainRechargeInfofail() {
		showToast("网络不给力");
	}
	
}
