package com.lcshidai.lc.widget.ppwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;


public class FinancePosterPopupWindow extends PopupWindow implements OnClickListener{
	private TRJActivity mContext;

	private ImageView iv_pp;
	private ImageButton btn_close;

	public FinancePosterPopupWindow(TRJActivity context, Bundle bundle) {
		super(context);
		mContext = context;
		String pic = bundle.getString("pic");

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.pp_finance_poster, null);

		iv_pp = (ImageView) contentView.findViewById(R.id.iv_pp);
		Glide.with(mContext).load(pic).into(iv_pp);

		btn_close = (ImageButton) contentView.findViewById(R.id.btn_close);
		btn_close.setOnClickListener(this);

		// 设置SelectPicPopupWindow的View
		this.setContentView(contentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.PopAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_close:
				dismiss();
				break;
			default:
				break;
		}
	}
	
}
