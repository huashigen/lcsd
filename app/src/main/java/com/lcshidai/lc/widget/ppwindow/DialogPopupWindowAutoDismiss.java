package com.lcshidai.lc.widget.ppwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;

public class DialogPopupWindowAutoDismiss extends PopupWindow {
	private TRJActivity mContext;
	private View mMenuView;
 
 
	private TextView tv_Title;
	private View mcontainer;
	private String msetPayPWDGoClass = "";
	private String mpwd = "";
	private RelativeLayout re_main;
	private LinearLayout re_main_res,bespeak_info_bottom_ll;
	private ImageView imgsleep;
	private RelativeLayout reImgTitle;
	private ChooseListener mChooseListener;
	private MyChooseListener mListener;
	public DialogPopupWindowAutoDismiss(TRJActivity context, View container,ChooseListener chooseListener) {
		super(context);
		mChooseListener=chooseListener;
		initContext(context,container);
	}

	private void initContext(TRJActivity context, View container) {
		mListener=new MyChooseListener();
		mContext = context;
		mcontainer = container;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.dialog_pp_dismiss, null);
		 
		tv_Title = (TextView) mMenuView.findViewById(R.id.tv_Title);
		 
		re_main  = (RelativeLayout) mMenuView.findViewById(R.id.re_main);
		re_main_res  = (LinearLayout) mMenuView.findViewById(R.id.re_main_res);
		bespeak_info_bottom_ll  = (LinearLayout) mMenuView.findViewById(R.id.bespeak_info_bottom_ll);
		imgsleep = (ImageView) mMenuView.findViewById(R.id.imgsleep);
		reImgTitle= (RelativeLayout) mMenuView.findViewById(R.id.reImgTitle);
		 
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimDialogPopupWindow);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//		mMenuView.setOnTouchListener(new OnTouchListener() {
//
//			public boolean onTouch(View v, MotionEvent event) {
//
//				int height = mMenuView.findViewById(R.id.ll_dpop).getTop();
//				int bottom=mMenuView.findViewById(R.id.ll_dpop).getBottom();
//				int y=(int) event.getY();
//				if(event.getAction()==MotionEvent.ACTION_UP){
//					if(y<height){
//						dismiss();
//					}else if(y>bottom){
//						dismiss();
//					}
//				}				
//				return true;
//
//			}
//		});
	}
	
	public void showWithMessage(String message ){
		re_main.setVisibility(View.VISIBLE);
		 
	 
		 
		 
			tv_Title.setVisibility(View.VISIBLE);
			tv_Title.setText(message);
	 
	 
		showAtLocation(mcontainer, Gravity.CENTER,
				0, 0);
		if (android.os.Build.VERSION.SDK_INT < 12)
			return;
		final ViewPropertyAnimator animate = mcontainer.animate();
		animate.setDuration(500);
		animate.alpha(0.5f);
		

		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				animate.alpha(1f);

			}
		});
	}

	public void setDismissPoP(){
		final Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				 dismiss(); // when the task active then close the dialog
				t.cancel(); // cancel the timer, otherwise, you may receive a
							// crash
			}
		}, 1000);
	}

	public void setintentactivity(String setPayPWDGoClass, String recharge) {
		// TODO Auto-generated method stub
		msetPayPWDGoClass = setPayPWDGoClass;
		 
		mpwd = recharge;
		this.setBackgroundDrawable(null);
	}

	public void setrechargeactivity(String pwd) {
		// TODO Auto-generated method stub
		 
		mpwd = pwd;
	}
	
	public  void setresValue(String res_money, String res_comment, String res_rule) {
		// TODO Auto-generated method stub
	 
		re_main.setVisibility(View.GONE);
		re_main_res.setVisibility(View.VISIBLE);
//		btn_dpop_res.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//					dismiss();
//			}
//		});
		showAtLocation(mcontainer, Gravity.CENTER,
				0, 0);
		if (android.os.Build.VERSION.SDK_INT < 12)
			return;
		final ViewPropertyAnimator animate = mcontainer.animate();
		animate.setDuration(500);
		animate.alpha(0.5f);
		
		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				animate.alpha(1f);

			}
		});
	}
	
	class MyChooseListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			
			//mNewCusMark.setVisibility(View.GONE);
			 
			
			dismiss();
		}
		
	}
	
	public interface ChooseListener{
		void chooseItem(int markItem);
	}
}
