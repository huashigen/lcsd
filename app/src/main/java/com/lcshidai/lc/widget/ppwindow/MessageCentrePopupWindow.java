package com.lcshidai.lc.widget.ppwindow;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lcshidai.lc.R;

public class MessageCentrePopupWindow extends PopupWindow {
	
	private TextView all , kaibiao , touzi , daoqi , bianxian , huikuan , jiangli , bespeak ; 
	private int mCurrentSelId = 0;
	public interface OnTypeSelectedListener{
		void onSelected(int id, int position);
	}
	
	private Context mContext;
	private OnTypeSelectedListener mListener;

	public MessageCentrePopupWindow(Context context){
		super(context);
		this.mContext = context;
		View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_message_centre_type, null);
		this.setContentView(contentView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		this.setOutsideTouchable(true);
		contentView.findViewById(R.id.pop_message_centre_shandow).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(MessageCentrePopupWindow.this.isShowing()){
					MessageCentrePopupWindow.this.dismiss();
				}
			}
		});
		all = (TextView)contentView.findViewById(R.id.message_centre_pop_ll_all);
		kaibiao = (TextView)contentView.findViewById(R.id.message_centre_pop_ll_kaibiao);
		touzi = (TextView)contentView.findViewById(R.id.message_centre_pop_ll_touzi);
		daoqi = (TextView)contentView.findViewById(R.id.message_centre_pop_ll_daoqi);
		bianxian = (TextView)contentView.findViewById(R.id.message_centre_pop_ll_bianxian);
		huikuan = (TextView)contentView.findViewById(R.id.message_centre_pop_ll_huikuan);
		jiangli = (TextView)contentView.findViewById(R.id.message_centre_pop_ll_jiangli);
		bespeak = (TextView)contentView.findViewById(R.id.message_centre_pop_ll_bespeak);
		// 0 -- 全部
		// 31 -- 新项目提醒
		// 1 -- 投资成功提醒
		// 33 -- 到期提醒
		// 190 -- 变现提醒
		// 34 -- 回款提醒
		// 81 -- 活动奖励提醒
		//	2--- 预约的4种提醒消息
		all.setOnClickListener(new TypeClickListener(0, 0));
		kaibiao.setOnClickListener(new TypeClickListener(31, 1));
		touzi.setOnClickListener(new TypeClickListener(1, 2));
		daoqi.setOnClickListener(new TypeClickListener(33, 3));
//		bianxian.setOnClickListener(new TypeClickListener(190, 4));
		huikuan.setOnClickListener(new TypeClickListener(34, 4));
		jiangli.setOnClickListener(new TypeClickListener(81, 5));
//		bespeak.setOnClickListener(new TypeClickListener(2, 7));

	}
	
	public void setCurrentId(int id){
		iterator(id);
	}
	
	public void setOnTypeSelectedListener(OnTypeSelectedListener listener){
		this.mListener = listener;
	}
	
	public void showAsDown(View v){
		if(!this.isShowing()){
			this.showAsDropDown(v);
		}
	}
	
	class TypeClickListener implements OnClickListener {
		int id;
		int position;
		public TypeClickListener(int id, int positon) {
			this.id = id;
			this.position = positon;
		}

		@Override
		public void onClick(View v) {
			mListener.onSelected(id, position);
			iterator(id);
			MessageCentrePopupWindow.this.dismiss();
		}
		
	}
	

	/**
	 * 根据id 设置当前选中项
	 * @param id
	 */
	public void iterator(int id){
		//all ,  ,  ,  ,  ,  ,  , }
		views.add(all);
		views.add(kaibiao);
		views.add(touzi);
		views.add(daoqi);
		views.add(bianxian);
		views.add(huikuan);
		views.add(jiangli);
		views.add(bespeak);
		
		for(int i=0;i<ids.length;i++){
			if(id == ids[i]){
				setChooseStatus(views.get(i));
			} else {
				setDefStatus(views.get(i));
			}
		}
	}
	int[] ids = {0,31,1,33,190,34, 81,2};
	List<TextView> views = new ArrayList<TextView>();
	
//	TextView[] views = {all , kaibiao , touzi , daoqi , bianxian , huikuan , jiangli , bespeak};
	
	
	/**
	 * 默认状态
	 * @param view
	 */
	public void setDefStatus(TextView view ){
		view.setCompoundDrawablesWithIntrinsicBounds(null , null , null , null);
	}
	
	/**
	 * 选中状态
	 * @param view
	 */
	public void setChooseStatus(TextView view){
		Drawable drawable = mContext.getResources().getDrawable(R.drawable.img_check_press);
		view.setCompoundDrawablesWithIntrinsicBounds(null , null , drawable , null);
	}
	
	
}
