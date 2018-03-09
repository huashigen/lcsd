package com.lcshidai.lc.widget.flipview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.lcshidai.lc.R;

public class FlipAdapter extends BaseAdapter{
	
	public interface Callback{
		void onPageRequested(int page);
	}
	
	private int m=10;
	
//	static class Item {
//		static long id = 0;
//		
//		long mId;
//		
//		public Item() {
//			mId = id++;
//		}
//		
//		long getId(){
//			return mId;
//		}
//	}
	
	
	private LayoutInflater inflater;
//	private List<Item> items = new ArrayList<Item>();
	int len;
	boolean misLeft;
	Context mContext;
	public FlipAdapter(Context context,int len,int m,boolean isleft) {
		this.m=m;
		mContext=context;
		misLeft=isleft;
		inflater = LayoutInflater.from(context);
		this.len=len;
	}


	@Override
	public int getCount() {
		return len;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return (position+1)%10;
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.page, parent, false);
			
			holder.text = (TextView) convertView.findViewById(R.id.text);
			
			convertView.setTag(holder);
			if(misLeft){
				convertView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.flipview_item_left));
			}else{
				convertView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.flipview_right));
			}
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		//TODO set a text with the id as well
		holder.text.setText(position%m+"");
		
		return convertView;
	}

	static class ViewHolder{
		TextView text;
		Button firstPage;
		Button lastPage;
	}


}
