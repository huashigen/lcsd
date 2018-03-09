package com.lcshidai.lc.ui.account;

import java.util.ArrayList;
import java.util.List;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.SafeQuestionSelectImpl;
import com.lcshidai.lc.model.account.SafeQuestionSelectData;
import com.lcshidai.lc.model.account.SafeQuestionSelectJson;
import com.lcshidai.lc.service.account.HttpSafeQuestionSelectService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.DensityUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 选择安全保护问题
 * @author 000853
 *
 */
public class SafeQuestionSelectActivity extends TRJActivity implements OnClickListener,SafeQuestionSelectImpl {
	HttpSafeQuestionSelectService hsqss;
	private Context mContext;
	private ImageButton mBackBtn;
	private TextView mTvTitle,mSubTitle;
	private Button mSaveBtn;
	
	private ListView lv_qustion_list;
	private Dialog loading;
	private List<Item> itemList;
	
	private String selectCodeNo = "";
	private String questionName = "";
	private QuestionListAdapter questionAdapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		selectCodeNo = getIntent().getStringExtra("select_quetion_code_no");
		questionName = getIntent().getStringExtra("question_name");
		hsqss = new HttpSafeQuestionSelectService(this, this);
		initView();
		loadData();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
		mContext = this;
		setContentView(R.layout.activity_question_select);
		
		mBackBtn=(ImageButton) findViewById(R.id.btn_back);
    	mBackBtn.setOnClickListener(this);
		mTvTitle=(TextView)findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("安全保护问题");
		mSubTitle=(TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		mSaveBtn=(Button)findViewById(R.id.btn_option);
		mSaveBtn.setText("确定");
		mSaveBtn.setTextColor(getResources().getColor(R.color.black));
		mSaveBtn.setVisibility(View.GONE);
		mSaveBtn.setBackgroundColor(Color.TRANSPARENT);
		mSaveBtn.setPadding(0, 0, DensityUtil.dip2px(mContext, 10), 0);
		mSaveBtn.setOnClickListener(this);
		
		itemList = new ArrayList<Item>();
		lv_qustion_list = (ListView) findViewById(R.id.question_select_lv_list);
		questionAdapter = new QuestionListAdapter();
		lv_qustion_list.setAdapter(questionAdapter);
		
		loading = createLoadingDialog(mContext, "数据加载中", true);
		loading.show();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			SafeQuestionSelectActivity.this.finish();
			break;
			
//		case R.id.btn_option:
//			if(!"".equals(selectCodeNo)){
//				Intent intent = new Intent();
//				intent.putExtra("select_quetion_code_no", selectCodeNo);
//				intent.putExtra("question_name", questionName);
//				setResult(20, intent);
//				SafeQuestionSelectActivity.this.finish();
//			}else{
//				showToast("请选择安全保护问题！");
//			}
//			break;
		}
	}

//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		selectCodeNo = itemList.get(arg2).code_no;
//		questionName = itemList.get(arg2).code_name;
//		questionAdapter.notifyDataSetChanged();
//	}
	
	
	class Item{
		String id;
		String code_key;
		String code_no;
		String code_name;
		String lang_type;
		String order;
		String is_public;
	}
	
	class ViewHolder{
		TextView tv;
		ImageView iv;
	}
	
	class QuestionListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Item getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.activity_question_select_list_item, null);
				holder.tv = (TextView) convertView.findViewById(R.id.question_list_item_tv);
				holder.iv = (ImageView) convertView.findViewById(R.id.question_list_item_iv);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final Item item = getItem(position);
			holder.tv.setText(item.code_name);
			if(selectCodeNo.equals(itemList.get(position).code_no)){
				holder.iv.setVisibility(View.VISIBLE);
			}else{
				holder.iv.setVisibility(View.GONE);
			}
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.putExtra("select_quetion_code_no", item.code_no);
					intent.putExtra("question_name", item.code_name);
					setResult(20, intent);
					SafeQuestionSelectActivity.this.finish();
				}
			});
			
			return convertView;
		}
		
	}
	
	
	private void loadData(){
		hsqss.gainSafeQuestionSelect();
	}

	@Override
	public void gainSafeQuestionSelectsuccess(SafeQuestionSelectJson response) {
		try{
			if(null != response){
				String boolen = response.getBoolen();
				if("1".equals(boolen)){
					List<SafeQuestionSelectData> dataArray = response.getData();
					for(int i=0; i<dataArray.size(); i++){
						Item item = new Item();
						SafeQuestionSelectData obj = dataArray.get(i);
						item.id = obj.getId();
						item.code_key = obj.getCode_key();
						item.code_no = obj.getCode_no();
						item.code_name = obj.getCode_name();
						item.lang_type = obj.getLang_type();
						item.order = obj.getOrder();
						item.is_public = obj.getIs_public();
						itemList.add(item);
					}
					questionAdapter.notifyDataSetChanged();
				}
				if(loading.isShowing()){
					loading.dismiss();
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void gainSafeQuestionSelectfail() {
		showToast("网络不给力！");
	}

}
