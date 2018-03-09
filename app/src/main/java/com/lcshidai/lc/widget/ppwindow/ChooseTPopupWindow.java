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

public class ChooseTPopupWindow extends PopupWindow {


	private View mMenuView;

	private View mView0;
	private View mView1;
	private View mView2;
	private View mView3;
	private View mView4;
	//private View mNewCus;
	
//	private View mView0Mark;
//	private View mView1Mark;
//	private View mView2Mark;
//	private View mView3Mark;
//	private View mNewCusMark;
	
	private ChooseListener mChooseListener;
	private MyChooseListener mListener;
	
	public ChooseTPopupWindow(Activity context,ChooseListener chooseListener) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mChooseListener=chooseListener;
		mMenuView = inflater.inflate(R.layout.pp_top_bar_list, null);
		
		mView0=mMenuView.findViewById(R.id.rl_all);
		mView1=mMenuView.findViewById(R.id.rl_gaofushuai);
		mView2=mMenuView.findViewById(R.id.rl_baifumei);
		mView3=mMenuView.findViewById(R.id.rl_fuerdai);
		mView4=mMenuView.findViewById(R.id.rl_dudt);
		//mNewCus=mMenuView.findViewById(R.id.rl_newcus);
		
//		mView0Mark=mMenuView.findViewById(R.id.iv_all);
//		mView1Mark=mMenuView.findViewById(R.id.iv_gaofushuai);
//		mView2Mark=mMenuView.findViewById(R.id.iv_baifumei);
//		mView3Mark=mMenuView.findViewById(R.id.iv_fuerdai);
		//mNewCusMark=mMenuView.findViewById(R.id.iv_new_cus);
		
		
		mListener=new MyChooseListener();
		
		mView0.setOnClickListener(mListener);
		mView1.setOnClickListener(mListener);
		mView2.setOnClickListener(mListener);
		mView3.setOnClickListener(mListener);
		mView4.setOnClickListener(mListener);
		//mNewCus.setOnClickListener(mListener);
		
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
	
	public void goAnim(View view,int x,int y,int currentT){
		
		mView0.setVisibility(View.VISIBLE);
		mView1.setVisibility(View.VISIBLE);
		mView2.setVisibility(View.VISIBLE);
		mView3.setVisibility(View.VISIBLE);
		mView4.setVisibility(View.VISIBLE);
//		if(currentT==0)mView0.setVisibility(View.GONE);
//		if(currentT==1)mView1.setVisibility(View.GONE);
//		if(currentT==2)mView2.setVisibility(View.GONE);
//		if(currentT==3)mView3.setVisibility(View.GONE);
//		if(currentT==4)mView4.setVisibility(View.GONE);
		showAsDropDown(view, x, y);
		//if(chooseMark!=4)mNewCusMark.setVisibility(View.GONE);
	}

	class MyChooseListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			
			//mNewCusMark.setVisibility(View.GONE);
			
			if(arg0==mView0){
				mChooseListener.chooseItem(0);
			}else if(arg0==mView1){
				mChooseListener.chooseItem(1);
			}else if(arg0==mView2){
				mChooseListener.chooseItem(3);
			}else if(arg0==mView3){
				mChooseListener.chooseItem(2);
			}else if(arg0==mView4){
				mChooseListener.chooseItem(4);
			}
			dismiss();
		}
		
	}
	
//	public void chooseIt(int i){
//		mView0Mark.setVisibility(View.GONE);
//		mView1Mark.setVisibility(View.GONE);
//		mView2Mark.setVisibility(View.GONE);
//		mView3Mark.setVisibility(View.GONE);
////		mNewCusMark.setVisibility(View.GONE);
//		
////		if(i==0){
////			mViewAllMark.setVisibility(View.VISIBLE);
////		}else if(i==1){
////			mViewGaofushuaiMark.setVisibility(View.VISIBLE);
////		}else if(i==2){
////			mViewBaifumeiMark.setVisibility(View.VISIBLE);
////		}else if(i==3){
////			mViewFuerdaiMark.setVisibility(View.VISIBLE);
////		}else if(i==4){
////			mNewCusMark.setVisibility(View.VISIBLE);
////		}
//	}
	
	public interface ChooseListener{
		void chooseItem(int markItem);
	}
	
}
