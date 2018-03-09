package com.lcshidai.lc.widget.ppwindow;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.lcshidai.lc.R;

public class SelectPicPopupWindow extends PopupWindow {


	private View mMenuView;

	private View mViewAll;
	private View mViewGaofushuai;
	private View mViewBaifumei;
	private View mViewFuerdai;
	private View mNewCus;
	
	private View mViewAllMark;
	private View mViewGaofushuaiMark;
	private View mViewBaifumeiMark;
	private View mViewFuerdaiMark;
	private View mNewCusMark;
	
	private ChooseListener mChooseListener;
	private MyChooseListener mListener;
	
	public SelectPicPopupWindow(Activity context,ChooseListener chooseListener,int chooseMark) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mChooseListener=chooseListener;
		mMenuView = inflater.inflate(R.layout.pp_top_bar_list_old, null);
		
		mViewAll=mMenuView.findViewById(R.id.rl_all);
		mViewGaofushuai=mMenuView.findViewById(R.id.rl_gaofushuai);
		mViewBaifumei=mMenuView.findViewById(R.id.rl_baifumei);
		mViewFuerdai=mMenuView.findViewById(R.id.rl_fuerdai);
		mNewCus=mMenuView.findViewById(R.id.rl_newcus);
		
		mViewAllMark=mMenuView.findViewById(R.id.iv_all);
		mViewGaofushuaiMark=mMenuView.findViewById(R.id.iv_gaofushuai);
		mViewBaifumeiMark=mMenuView.findViewById(R.id.iv_baifumei);
		mViewFuerdaiMark=mMenuView.findViewById(R.id.iv_fuerdai);
		mNewCusMark=mMenuView.findViewById(R.id.iv_new_cus);
		
		if(chooseMark!=0)mViewAllMark.setVisibility(View.GONE);
		if(chooseMark!=1)mViewGaofushuaiMark.setVisibility(View.GONE);
		if(chooseMark!=2)mViewBaifumeiMark.setVisibility(View.GONE);
		if(chooseMark!=3)mViewFuerdaiMark.setVisibility(View.GONE);
		if(chooseMark!=4)mNewCusMark.setVisibility(View.GONE);
		
		mListener=new MyChooseListener();
		
		mViewAll.setOnClickListener(mListener);
		mViewGaofushuai.setOnClickListener(mListener);
		mViewBaifumei.setOnClickListener(mListener);
		mViewFuerdai.setOnClickListener(mListener);
		mNewCus.setOnClickListener(mListener);
		
		mMenuView.findViewById(R.id.ll_back).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//销毁弹出框
				dismiss();
			}
		});
		//设置按钮监听
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		//this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}

	class MyChooseListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			
			mViewAllMark.setVisibility(View.GONE);
			mViewGaofushuaiMark.setVisibility(View.GONE);
			mViewBaifumeiMark.setVisibility(View.GONE);
			mViewFuerdaiMark.setVisibility(View.GONE);
			mNewCusMark.setVisibility(View.GONE);
			
			if(arg0==mViewAll){
				mChooseListener.chooseItem(0);
				mViewAllMark.setVisibility(View.VISIBLE);
			}else if(arg0==mViewGaofushuai){
				mChooseListener.chooseItem(1);
				mViewGaofushuaiMark.setVisibility(View.VISIBLE);
			}else if(arg0==mViewBaifumei){
				mChooseListener.chooseItem(2);
				mViewBaifumeiMark.setVisibility(View.VISIBLE);
			}else if(arg0==mViewFuerdai){
				mChooseListener.chooseItem(3);
				mViewFuerdaiMark.setVisibility(View.VISIBLE);
			}else if(arg0==mNewCus){
				mChooseListener.chooseItem(4);
				mNewCusMark.setVisibility(View.VISIBLE);
			}
			dismiss();
		}
		
	}
	
	public void chooseIt(int i){
		mViewAllMark.setVisibility(View.GONE);
		mViewGaofushuaiMark.setVisibility(View.GONE);
		mViewBaifumeiMark.setVisibility(View.GONE);
		mViewFuerdaiMark.setVisibility(View.GONE);
		mNewCusMark.setVisibility(View.GONE);
		
		if(i==0){
			mViewAllMark.setVisibility(View.VISIBLE);
		}else if(i==1){
			mViewGaofushuaiMark.setVisibility(View.VISIBLE);
		}else if(i==2){
			mViewBaifumeiMark.setVisibility(View.VISIBLE);
		}else if(i==3){
			mViewFuerdaiMark.setVisibility(View.VISIBLE);
		}else if(i==4){
			mNewCusMark.setVisibility(View.VISIBLE);
		}
	}
	
	public interface ChooseListener{
		void chooseItem(int markItem);
	}
	
}
