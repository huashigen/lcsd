package com.lcshidai.lc.widget.ppwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.lcshidai.lc.R;

public class RedMoneyPW extends PopupWindow {

	private View mMenuView;
	public RedMoneyPW(final Activity context,final CallBackShare callBack) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.web_que, null);
		
		//设置按钮监听
				//设置SelectPicPopupWindow的View
				this.setContentView(mMenuView);
				//设置SelectPicPopupWindow弹出窗体的宽
				this.setWidth(LayoutParams.MATCH_PARENT);
				//设置SelectPicPopupWindow弹出窗体的高
				this.setHeight(LayoutParams.MATCH_PARENT);
				//设置SelectPicPopupWindow弹出窗体可点击
				this.setFocusable(true);
				//设置SelectPicPopupWindow弹出窗体动画效果
				//this.setAnimationStyle(R.style.AnimBottom);
				//实例化一个ColorDrawable颜色为半透明
				ColorDrawable dw = new ColorDrawable(0xb0000000);
				//设置SelectPicPopupWindow弹出窗体的背景
				this.setBackgroundDrawable(dw);
				//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
				
				mMenuView.findViewById(R.id.view_left).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						RedMoneyPW.this.dismiss();
						context.finish();
					}
				});
				
				mMenuView.findViewById(R.id.view_right).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						RedMoneyPW.this.dismiss();
						callBack.share();
					}
				});
	}
	
	public void show(View view){
		showAtLocation(view, Gravity.CENTER, 0, 0);
	}
	
	public interface CallBackShare{
		void share();
	}
	
}