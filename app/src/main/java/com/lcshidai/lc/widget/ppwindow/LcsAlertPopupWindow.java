package com.lcshidai.lc.widget.ppwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.finance.LcsUserCheckInImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.finance.HttpLcsUserCheckInService;
import com.lcshidai.lc.ui.base.TRJActivity;


public class LcsAlertPopupWindow extends PopupWindow implements OnClickListener, LcsUserCheckInImpl {
	private Button btn_i_know;

	private HttpLcsUserCheckInService mHttpLcsUserCheckInService;

	public LcsAlertPopupWindow(TRJActivity context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.pp_lcs_alert, null);

		mHttpLcsUserCheckInService = new HttpLcsUserCheckInService(context, this);

		btn_i_know = (Button) contentView.findViewById(R.id.btn_i_know);
		btn_i_know.setOnClickListener(this);

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
			case R.id.btn_i_know:
				mHttpLcsUserCheckInService.userCheckIn();
				dismiss();
				break;
			default:
				break;
		}
	}

	@Override
	public void userCheckInSuccess(BaseJson response) {

	}

	@Override
	public void userCheckInFail() {

	}

}
