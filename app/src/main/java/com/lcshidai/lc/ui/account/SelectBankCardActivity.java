package com.lcshidai.lc.ui.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.R;
import com.lcshidai.lc.entity.BankCardInfo;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.utils.Thumbnail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 选择银行卡页面
 * @author 000853
 *
 */
public class SelectBankCardActivity extends TRJActivity implements OnClickListener, OnItemClickListener {
	
	private final String BANK_CARD_LIST_URL = "Mobile2/PayAccount/getMyBindBanks";	//提现绑定银行卡列表
	private final String RECHARGE_BANK_LIST_URL = "Mobile2/PayAccount/paybanklist";	//提现绑定银行卡列表
	
	private TextView mTvTitle, mSubTitle;
	private Button  mSaveBtn;
	ImageButton mBackBtn;
	private Context mContext;
	
	private ListView bankcard_lv;
	private TextView enough_tv;
	private Button add_bt;
	
	private List<Parcelable> bankInfoList;
	private String selectId = "";
	private BankListAdapter bankListAdapter;
	private int selectPosition = -1;
	private int intentPosition = -1;
	
	private int intentFlag = 0;	//0-提现绑定银行卡列表 	1-充值支持的银行列表	
	
	private Dialog loading;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if(null != getIntent()){
			selectId = getIntent().getStringExtra("bank_card_id");
			intentFlag = getIntent().getIntExtra("intent_flag", 0);
		}
		initView();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
		mContext = this;
		setContentView(R.layout.activity_select_bank_cark);
		mBackBtn=(ImageButton) findViewById(R.id.btn_back);
		mBackBtn.setOnClickListener(this);
		mSaveBtn=(Button)findViewById(R.id.btn_option);
		mSaveBtn.setPadding(0, 0, DensityUtil.dip2px(mContext, 10), 0);
		mSaveBtn.setBackgroundColor(Color.TRANSPARENT);
		mSaveBtn.setText("完成");
		mSaveBtn.setVisibility(View.INVISIBLE);
		mSaveBtn.setOnClickListener(this);
		mTvTitle=(TextView)findViewById(R.id.tv_top_bar_title);
		mSubTitle=(TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		
		bankcard_lv = (ListView) findViewById(R.id.select_bank_card_list_lv);
		enough_tv = (TextView) findViewById(R.id.select_bank_card_enough_tv);
		add_bt = (Button) findViewById(R.id.select_bank_card_add_bt);
		

		bankInfoList = new ArrayList<Parcelable>();
		bankListAdapter = new BankListAdapter();
		bankcard_lv.setAdapter(bankListAdapter);
		bankcard_lv.setOnItemClickListener(this);
		
		loading = createLoadingDialog(mContext, "数据加载中", true);
		loading.show();
		if(intentFlag == 0){
			initWithdrawalsView();
		}else{
			mTvTitle.setText("选择充值的银行");
			loadSupportBank();
		}
	}
	
	private void initWithdrawalsView(){
		mTvTitle.setText("选择银行卡");
		String enough = enough_tv.getText().toString();
		SpannableString ss = new SpannableString(enough);
		ss.setSpan(new ForegroundColorSpan(
    			Color.parseColor("#138AE8")), 7, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		enough_tv.setText(ss);
		add_bt.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		if(intentFlag == 0){
			bankInfoList.clear();
			loadBankData();
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			SelectBankCardActivity.this.finish();
			break;
		case R.id.btn_option:
			if(null != bankInfoList && intentPosition != selectPosition){
				Intent intent = new Intent();
				if(intentFlag == 0){
					intent.putExtra("select_bank_card_info", bankInfoList.get(selectPosition));
				}else{
					intent.putExtra("bank_icon", ((BankCardInfo)bankInfoList.get(selectPosition)).getBank());
					intent.putExtra("bank_name", ((BankCardInfo)bankInfoList.get(selectPosition)).getBank_name());
					intent.putExtra("bank_channel", ((BankCardInfo)bankInfoList.get(selectPosition)).getChannel());
					intent.putExtra("bank_code", ((BankCardInfo)bankInfoList.get(selectPosition)).getCode());
				}
				setResult(11, intent);
			}
			SelectBankCardActivity.this.finish();
			break;
		case R.id.select_bank_card_add_bt:
			Intent intent = new Intent(mContext, UserBankAddActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	class BankListAdapter extends BaseAdapter{
		
		Thumbnail thumbnail = null;
		
		BankListAdapter (){
			this.thumbnail = Thumbnail.init(mContext);
		}

		@Override
		public int getCount() {
			return bankInfoList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return bankInfoList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			BankCardInfo bankInfo = (BankCardInfo) bankInfoList.get(arg0);
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.layout_select_bank_card_list_item, null);
			ImageView icon_iv = (ImageView) arg1.findViewById(R.id.select_bank_card_item_icon_iv);
			TextView name_tv = (TextView) arg1.findViewById(R.id.select_bank_card_item_name_tv);
			ImageView right_iv = (ImageView) arg1.findViewById(R.id.select_bank_card_item_right_iv);
			
			icon_iv.setImageBitmap(thumbnail.parse(bankInfo.getBank()));
			if(intentFlag == 0){
				if(!TextUtils.isEmpty(bankInfo.getAcount_name())){
					String bankcardnum = bankInfo.getAcount_name().replaceAll(" ", "");
					name_tv.setText(bankInfo.getBank_name() + "（" + 
							bankcardnum.substring(bankcardnum.length() - 4, bankcardnum.length()) + "）");
				}else{
					name_tv.setText(bankInfo.getBank_name());
				}
			}else{
				name_tv.setText(bankInfo.getBank_name());
			}
			if(arg0 == selectPosition)
				right_iv.setVisibility(View.VISIBLE);
			else
				right_iv.setVisibility(View.GONE);
			return arg1;
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		selectPosition = arg2;
		bankListAdapter.notifyDataSetChanged();
		if(null != bankInfoList && intentPosition != selectPosition){
			Intent intent = new Intent();
			if(intentFlag == 0){
				intent.putExtra("select_bank_card_info", bankInfoList.get(selectPosition));
			}else{
				intent.putExtra("bank_icon", ((BankCardInfo)bankInfoList.get(selectPosition)).getBank());
				intent.putExtra("bank_name", ((BankCardInfo)bankInfoList.get(selectPosition)).getBank_name());
				intent.putExtra("bank_channel", ((BankCardInfo)bankInfoList.get(selectPosition)).getChannel());
				intent.putExtra("bank_code", ((BankCardInfo)bankInfoList.get(selectPosition)).getCode());
			}
			setResult(11, intent);
		}
		SelectBankCardActivity.this.finish();
	}
	
	/**
	 * 获取银行卡列表
	 */
	private void loadBankData(){
		post(BANK_CARD_LIST_URL, new RequestParams(), new JsonHttpResponseHandler(SelectBankCardActivity.this){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if(loading.isShowing()){
					loading.dismiss();
				}
				try {
    				if(response != null){
	            	   if(response.getInt("boolen") == 1) {
							JSONArray dataArray = response.getJSONArray("data");
							int size = dataArray.length();
							for(int i=0; i<size; i++){
								JSONObject obj = dataArray.getJSONObject(i);
								BankCardInfo info = new BankCardInfo();
								info.setId(obj.getString("id"));
								info.setBank_name(obj.getString("bank_name"));
								info.setBank(obj.getString("bank"));
								info.setAcount_name(obj.getString("acount_name"));
								info.setSub_bank(obj.getString("sub_bank"));
								
								if(selectId.equals(info.getId())){
									selectPosition = i;
									intentPosition = i;
								}
								bankInfoList.add(info);
							}
							bankListAdapter.notifyDataSetChanged();
//							if(bankInfoList.size() < 3){
//								enough_tv.setVisibility(View.GONE);
//								add_bt.setVisibility(View.VISIBLE);
//							}else{
//								add_bt.setVisibility(View.GONE);
//								enough_tv.setVisibility(View.VISIBLE);
//							}
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
				showToast("网络不给力");
			}
		});
	}
	
	/**
	 * 获取充值支持的银行列表
	 */
	private void loadSupportBank(){
		post(RECHARGE_BANK_LIST_URL, new RequestParams(), new JsonHttpResponseHandler(SelectBankCardActivity.this){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if(loading.isShowing()){
					loading.dismiss();
				}
				try {
    				if(response != null){
	            	   if(response.getInt("boolen") == 1) {
							JSONArray dataArray = response.getJSONArray("data");
							int size = dataArray.length();
							for(int i=0; i<size; i++){
								JSONObject obj = dataArray.getJSONObject(i);
								BankCardInfo info = new BankCardInfo();
								info.setBank_name(obj.getString("name"));
								info.setBank(obj.getString("myCode"));
								info.setCode(obj.getString("code"));
								info.setChannel(obj.getString("channel"));
								
								if(selectId.equals(info.getBank())){
									selectPosition = i;
									intentPosition = i;
								}
								bankInfoList.add(info);
							}
							bankListAdapter.notifyDataSetChanged();
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
				showToast("网络不给力");
			}
		});
	}
}
