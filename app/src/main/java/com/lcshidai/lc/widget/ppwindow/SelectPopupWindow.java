package com.lcshidai.lc.widget.ppwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;

public class SelectPopupWindow extends PopupWindow {
	protected TextView btn_take_photo, btn_pick_photo, btn_cancel,btn_change_uname;
	protected View mMenuView;
	protected TRJActivity mContext;

	public SelectPopupWindow(TRJActivity context) {
		super(context);
		initContext(context);
	}

	private void initContext(TRJActivity context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.select_dialog, null);
		btn_take_photo = (TextView) mMenuView.findViewById(R.id.btn_take_photo);
		btn_pick_photo = (TextView) mMenuView.findViewById(R.id.btn_pick_photo);
		btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel);
		btn_change_uname= (TextView) mMenuView.findViewById(R.id.btn_change_uname);
		// 取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});

		if (android.os.Build.VERSION.SDK_INT <= 10) {
			// btn_take_photo.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_normal);
			// btn_pick_photo.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_normal);
			// btn_cancel.setBackgroundResource(R.drawable.btn_style_alert_dialog_cancel_normal);

			btn_take_photo.setOnTouchListener(touchListenerA);
			btn_pick_photo.setOnTouchListener(touchListenerA);
			btn_cancel.setOnTouchListener(touchListenerB);
		}

		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	public SelectPopupWindow(TRJActivity context, OnClickListener itemsOnClick) {
		super(context);
		initContext(context);
		// 设置按钮监听
		btn_pick_photo.setOnClickListener(itemsOnClick);
		btn_take_photo.setOnClickListener(itemsOnClick);
	}


	OnTouchListener touchListenerA = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// v.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_pressed);
				break;
			case MotionEvent.ACTION_UP:
				// v.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_normal);
				break;
			case MotionEvent.ACTION_CANCEL:
				// v.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_normal);
				break;
			}
			return false;
		}
	};

	OnTouchListener touchListenerB = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// v.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_pressed);
				break;
			case MotionEvent.ACTION_UP:
				// v.setBackgroundResource(R.drawable.btn_style_alert_dialog_cancel_normal);
				break;
			case MotionEvent.ACTION_CANCEL:
				// v.setBackgroundResource(R.drawable.btn_style_alert_dialog_cancel_normal);
				break;
			}
			return false;
		}
	};

}
