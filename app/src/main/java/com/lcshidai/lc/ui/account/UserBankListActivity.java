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
import com.lcshidai.lc.utils.Thumbnail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 银行卡管理
 * @author
 *
 */
public class UserBankListActivity extends TRJActivity{
	private static final String URL_GET_LIST = "Mobile2/User/user_banks";
	private Context mContext;
	private ImageButton mBackBtn;
	private TextView mTvTitle,mSubTitle;
	private Button mSaveBtn;
	private ListView mListView;
	private ItemAdapter mAdapter;
	private View mProgressContainer;
	private Button btn_submit;
	private String mRealName;
	private RelativeLayout emptyRL;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_user_bank_list);
        mAdapter = new ItemAdapter(this);
        initView();
        
    }

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	@Override
	public void onResume() {
	    super.onResume();
	    mAdapter.clear();
	    mAdapter.mList.clear();
	    mProgressContainer.setVisibility(View.VISIBLE);
	    loadData();
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
		mSaveBtn.setVisibility(View.INVISIBLE);
		mListView = (ListView) findViewById(R.id.listView);
		mProgressContainer = findViewById(R.id.progressContainer);
		emptyRL = (RelativeLayout) findViewById(R.id.rl_empty);
		btn_submit = (Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserBankListActivity.this,UserBankAddActivity.class);
				intent.putExtra("real_name", mRealName);
				intent.putExtra("id", "");
				startActivity(intent);
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				showLongPressDialog(position);
			}
			
		});
    }
    
    private void loadData(){
    	RequestParams rq = new RequestParams();
        post(URL_GET_LIST, rq, new JsonHttpResponseHandler(this){
        	@Override
        	public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
        		super.onSuccess(statusCode,headers, response);
        		try {
        			mProgressContainer.setVisibility(View.GONE);
    				if(response != null){
            		   JSONObject jo = response.optJSONObject("data");
            		   if(jo != null){
	            		   mRealName = !jo.isNull("real_name")?jo.getString("real_name"):"";
	            		   if(response.getInt("boolen") == 1) {
		            		   JSONArray array = jo.optJSONArray("banklist");
		            		   if(array != null){
			            		   int size = array.length();
			            		   for(int i = 0; i < size; i++) {
			   						   JSONObject obj = array.getJSONObject(i);
			   						   Item item = new Item();
			   						   item.mId = obj.getString("id");
			   						   item.mName = obj.getString("bank_name");
			   						   item.mNo = obj.getString("account_no");//卡号
			   						   item.mNoTail = obj.getString("account_no_tail");//四位尾号
			   						   item.mBank = obj.getString("bank");//银行简称
			                           mAdapter.mList.add(item);
			   					   }
		            		   }
		            		   emptyRL.setVisibility(View.GONE);
							   mListView.setVisibility(View.VISIBLE);
                               mListView.setAdapter(mAdapter);
					       }else{
					    	   if(mAdapter.mList.size() == 0){
									emptyRL.setVisibility(View.VISIBLE);
									mListView.setVisibility(View.GONE);
								}
					       }
		            	   if(mAdapter.mList.size() >= 3){
		            		  btn_submit.setVisibility(View.GONE);
		            	   }else{
		            		  btn_submit.setVisibility(View.VISIBLE);
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
        		mProgressContainer.setVisibility(View.GONE);
        	}
        	
        });
    }
    
    /**
	 * 显示长按的弹出框菜单
	 */
	private void showLongPressDialog(final int position){
		LayoutInflater inflater = LayoutInflater.from(mContext);  
        View view = inflater.inflate(R.layout.dialog_bank_list, null);
        TextView titleTV = (TextView) view.findViewById(R.id.tv_top_bar_title);
        titleTV.setText("操作");
        final TextView tv_edit = (TextView) view.findViewById(R.id.tv_edit);
        final TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        final TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final RelativeLayout rl_edit = (RelativeLayout) view.findViewById(R.id.rl_edit);
        final RelativeLayout rl_delete = (RelativeLayout) view.findViewById(R.id.rl_delete);
        final RelativeLayout rl_cancel = (RelativeLayout) view.findViewById(R.id.rl_cancel);
       
        tv_edit.setTextColor(Color.parseColor("#464646"));
        tv_delete.setTextColor(Color.parseColor("#464646"));
        tv_cancel.setTextColor(Color.parseColor("#464646"));
        rl_edit.setBackgroundColor(Color.TRANSPARENT);
        rl_delete.setBackgroundColor(Color.TRANSPARENT);
        rl_cancel.setBackgroundColor(Color.TRANSPARENT);
        
        final Dialog dialog = new Dialog(mContext, R.style.style_loading_dialog);// 创建自定义样式dialog
        dialog.setContentView(view);
        dialog.setCancelable(true);// 不可以用“返回键”取消  
        dialog.show();
        
        rl_edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tv_edit.setTextColor(Color.parseColor("#4B95F8"));
				rl_edit.setBackgroundColor(Color.parseColor("#F6F6F6"));
				onEdit(position);
				dialog.dismiss();
			}
		});
        
        rl_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv_delete.setTextColor(Color.parseColor("#4B95F8"));
				rl_delete.setBackgroundColor(Color.parseColor("#F6F6F6"));
				onCancel(position);
				dialog.dismiss();
			}
		});
        rl_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tv_cancel.setTextColor(Color.parseColor("#4B95F8"));
				rl_cancel.setBackgroundColor(Color.parseColor("#F6F6F6"));
				dialog.dismiss();
			}
		});
	}
	
	private void onCancel(int pos){
		Intent intent = new Intent(UserBankListActivity.this,UserCancelBindingActivity.class);
		intent.putExtra("id", mAdapter.mList.get(pos).mId);
		startActivity(intent);
	}
	
	private void onEdit(int pos){
		Intent intent = new Intent(UserBankListActivity.this,UserBankAddActivity.class);
		intent.putExtra("id", mAdapter.mList.get(pos).mId);
		startActivity(intent);
	}
    
    private class Item{
    	private String mId;
    	private String mName;//名称
    	private String mNo;//卡号
    	private String mNoTail;//四位尾号
    	private String mBank;//银行简称
	}
    
    public class ItemAdapter extends ArrayAdapter<Item> {
		private Context mContext;
		private ArrayList<Item> mList = new ArrayList<Item>();
		private Thumbnail thumbnail = null;

		public ItemAdapter(Activity context) {
			super(context, 0);
			mContext = context;
			thumbnail = Thumbnail.init(mContext);
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_user_bank_list_item, null);
				vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				vh.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				vh.tv_no_tail = (TextView) convertView.findViewById(R.id.tv_no_tail);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			final Item item = getItem(position);
			vh.tv_name.setText(item.mName);
			vh.tv_no_tail.setText("("+ item.mNoTail + ")");
			vh.iv_icon.setImageBitmap(thumbnail.parse(item.mBank));
			return convertView;
		}

		public class ViewHolder {
			TextView tv_name;
			ImageView iv_icon;
			TextView tv_no_tail;
		}
	}
}
