package com.lcshidai.lc.ui.account;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 银行卡列表
 * @author
 *
 */
public class BankListActivity extends TRJActivity{
	private static final String URL_GET_LIST = "Mobile2/User/bank_list";
	private ImageButton mBackBtn;
	private TextView mTvTitle,mSubTitle;
	private Button mSaveBtn;
	private ListView mListView;
	private ItemAdapter mAdapter;
	private View mProgressContainer;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        mAdapter = new ItemAdapter(this);
        mAdapter.clear();
        initView();
        loadData();
    }

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
    	mBackBtn=(ImageButton) findViewById(R.id.btn_back);
    	mBackBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTvTitle=(TextView)findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("银行卡管理");
		mSubTitle=(TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		mSaveBtn=(Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.VISIBLE);
		mSaveBtn.setBackgroundDrawable(null);
		mSaveBtn.setText(" 确定　");
		mSaveBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String name = "",code = "";
				for(Item it : mAdapter.mList){
					if(it.isSelected){
						name = it.mName;
						code = it.mCode;
					}
				}
				Intent intent = new Intent(BankListActivity.this,UserBankAddActivity.class);
				intent.putExtra("name", name);
				intent.putExtra("code", code);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		mListView = (ListView) findViewById(R.id.listView);
		mProgressContainer = findViewById(R.id.progressContainer);
    }
    
   
    
    public void loadData(){
    	RequestParams rq = new RequestParams();
        post(URL_GET_LIST, rq, new JsonHttpResponseHandler(){
        	@Override
        	public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
        		try {
        			mProgressContainer.setVisibility(View.GONE);
    				if(response != null){
	            	   if(response.getInt("boolen") == 1) {
	            		   JSONArray array = response.optJSONArray("data");
	            		   if(array != null){
		            		   int size = array.length();
		            		   for(int i = 0; i < size; i++) {
		   						   JSONObject obj = array.getJSONObject(i);
		   						   Item item = new Item();
		   						   item.mName = obj.getString("name");
		   						   item.mCode = obj.getString("code");
		                           mAdapter.mList.add(item);
		   					   }
	            		   }
                           mListView.setAdapter(mAdapter);
						}
    			   }
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}

        	}
        	@Override
        	public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
        		mProgressContainer.setVisibility(View.GONE);
        	}
        	
        });
    }
    
    private class Item{
    	private String mName;//
    	private String mCode;
    	private boolean isSelected;
	}
    
    public class ItemAdapter extends ArrayAdapter<Item> {
		private Context mContext;
		private ArrayList<Item> mList = new ArrayList<Item>();

		public ItemAdapter(Activity context) {
			super(context, 0);
			mContext = context;
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Item getItem(int position) {
			return mList.get(position);
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			ViewHolder vh = new ViewHolder();
			if(convertView == null || convertView.getTag() == null){ 
				convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_bank_list_item, null);
				vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				vh.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			final Item item = getItem(position);
			vh.tv_name.setText(item.mName);
			if(item.isSelected){
				vh.iv_select.setVisibility(View.VISIBLE);
			}else{
				vh.iv_select.setVisibility(View.GONE);
			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					for(Item it : mAdapter.mList){
						it.isSelected = false;
					}
					ViewHolder vh = (ViewHolder) view.getTag();
					if(vh.iv_select.getVisibility() == RelativeLayout.VISIBLE){
						vh.iv_select.setVisibility(RelativeLayout.GONE);
						item.isSelected = false;
					}else{
						vh.iv_select.setVisibility(RelativeLayout.VISIBLE);
						item.isSelected = true;
					}
					notifyDataSetChanged();
				}
			});
			return convertView;
		}

		public class ViewHolder {
			TextView tv_name;
			ImageView iv_select;
		}
	}
}
