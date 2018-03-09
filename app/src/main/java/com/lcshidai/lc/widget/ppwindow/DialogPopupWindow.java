package com.lcshidai.lc.widget.ppwindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.account.UserPayPwdFirstSetActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.MemorySave;

public class DialogPopupWindow extends PopupWindow {
	private TRJActivity mContext;
	private View mMenuView;
	private Button btn_dpop,btn_dpop_res,bespeak_info_bt_cancle,bespeak_info_bt_back;
	private ImageView iv_dpop,img_gesture;
	private TextView tv_dpop,tv_Title,tv_dpop_res,tv_give_res,tv_rule_res;
	private View mcontainer;
	private String msetPayPWDGoClass = "";
	private String mpwd = "";
	private RelativeLayout re_main;
	private LinearLayout re_main_res,bespeak_info_bottom_ll;
	private ImageView imgsleep;
	private RelativeLayout reImgTitle;
	private ChooseListener mChooseListener;
	private MyChooseListener mListener;
	public DialogPopupWindow(TRJActivity context, View container,ChooseListener chooseListener) {
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
		mMenuView = inflater.inflate(R.layout.dialog_pp, null);
		iv_dpop = (ImageView) mMenuView.findViewById(R.id.iv_dpop);
		img_gesture = (ImageView) mMenuView.findViewById(R.id.img_gesture);
		tv_dpop = (TextView) mMenuView.findViewById(R.id.tv_dpop);
		tv_Title = (TextView) mMenuView.findViewById(R.id.tv_Title);
		btn_dpop_res = (Button) mMenuView.findViewById(R.id.btn_dpop_res);
		btn_dpop = (Button) mMenuView.findViewById(R.id.btn_dpop);
		bespeak_info_bt_cancle = (Button) mMenuView.findViewById(R.id.bespeak_info_bt_cancle);
		bespeak_info_bt_back = (Button) mMenuView.findViewById(R.id.bespeak_info_bt_back);
		re_main  = (RelativeLayout) mMenuView.findViewById(R.id.re_main);
		re_main_res  = (LinearLayout) mMenuView.findViewById(R.id.re_main_res);
		bespeak_info_bottom_ll  = (LinearLayout) mMenuView.findViewById(R.id.bespeak_info_bottom_ll);
		imgsleep = (ImageView) mMenuView.findViewById(R.id.imgsleep);
		reImgTitle= (RelativeLayout) mMenuView.findViewById(R.id.reImgTitle);
		tv_dpop_res = (TextView) mMenuView.findViewById(R.id.tv_dpop_res);
		tv_give_res = (TextView) mMenuView.findViewById(R.id.tv_give_res);
		tv_rule_res = (TextView) mMenuView.findViewById(R.id.tv_rule_res);
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
	}
	
	public void showWithMessage(String message, String imgflag ){
		re_main.setVisibility(View.VISIBLE);
		re_main_res.setVisibility(View.GONE);
		tv_Title.setVisibility(View.GONE);
		if(null!=iv_dpop){
			if (imgflag!=null&&imgflag.equals("1")) {
				iv_dpop.setBackgroundResource(R.drawable.pop_err);
			}else if (imgflag!=null&&imgflag.equals("0")) {
				iv_dpop.setBackgroundResource(R.drawable.pop_warn);
			}
			else if (imgflag!=null&&imgflag.equals("2")) {
				iv_dpop.setBackgroundResource(R.drawable.pop_succ);
			}else if (imgflag!=null&&imgflag.equals("3")) {
				reImgTitle.setVisibility(View.GONE);
				tv_dpop.setPadding(0, 40, 0, 0);
				btn_dpop.setText("我知道了");
				imgsleep.setVisibility(View.VISIBLE);
			}else if (imgflag!=null&&imgflag.equals("4")) {
				iv_dpop.setBackgroundResource(R.drawable.clock_xzd);
				btn_dpop.setText("我知道了");
				tv_Title.setVisibility(View.VISIBLE);
				tv_Title.setText("yu整cc倒计时中");
			}else if (imgflag!=null&&imgflag.equals("5")) {
			 if(null!=tv_dpop&&null!=btn_dpop&&null!=img_gesture&&null!=mMenuView){
				 iv_dpop.setBackgroundResource(R.drawable.img_gesture);
					tv_dpop.setVisibility(View.GONE);
					btn_dpop.setVisibility(View.GONE);
					img_gesture.setVisibility(View.VISIBLE);
					img_gesture.setBackgroundResource(R.drawable.btn_gesture);
					mMenuView.findViewById(R.id.ll_dpop).setBackgroundResource(0x00000000);
					mMenuView.setOnTouchListener(new OnTouchListener() {
					
								public boolean onTouch(View v, MotionEvent event) {
					
									int y=(int) event.getY();
									if(event.getAction()==MotionEvent.ACTION_UP){
											dismiss();
									}				
									return true;
					
								}
							});
			 }
				
			}else if (imgflag!=null&&imgflag.equals("6")) {
				iv_dpop.setBackgroundResource(R.drawable.pop_warn);
				bespeak_info_bottom_ll.setVisibility(View.VISIBLE);
				btn_dpop.setVisibility(View.GONE);
				bespeak_info_bt_cancle.setOnClickListener(mListener);
				bespeak_info_bt_back.setOnClickListener(mListener);
			}else if (imgflag!=null&&imgflag.equals("7")) {
				iv_dpop.setBackgroundResource(R.drawable.pop_warn);
				bespeak_info_bottom_ll.setVisibility(View.VISIBLE);
				btn_dpop.setVisibility(View.GONE);
				bespeak_info_bt_cancle.setText("确认");
				bespeak_info_bt_back.setText("取消");
				bespeak_info_bt_cancle.setOnClickListener(mListener);
				bespeak_info_bt_back.setOnClickListener(mListener);
			}
			tv_dpop.setText(message);
			// 取消按钮
					btn_dpop.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// 销毁弹出框
							if(mpwd!=null&&mpwd.equals("pwd")){
								Intent intent = new Intent(mContext, UserPayPwdFirstSetActivity.class);
								intent.putExtra("intent_from_withdrawals", 1);
								intent.putExtra("go_class", msetPayPWDGoClass);
								mContext.startActivity(intent);
								dismiss();
							}else if (mpwd!=null&&mpwd.equals("recharge")) {
								Intent intent = new Intent();
								if(MemorySave.MS.mRegLockSuccessBackMoney){
									intent.putExtra("mRegGest", true);
									MemorySave.MS.mRegLockSuccessBackMoney=false;
								}
								intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/addBank");
								intent.setClass(mContext,
										MainWebActivity.class);
								mContext.startActivity(intent);
								dismiss();
							}else {
								dismiss();
							}
						}
					});
		}
		
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
				animate.alpha(1f);

			}
		});
	}

	

	public void setintentactivity(String setPayPWDGoClass, String recharge) {
		msetPayPWDGoClass = setPayPWDGoClass;
		btn_dpop.setText("马上设置");
		mpwd = recharge;
		this.setBackgroundDrawable(null);
	}

	public void setrechargeactivity(String pwd) {
		btn_dpop.setText("去充值");
		mpwd = pwd;
	}
	
	public  void setresValue(String res_money, String res_comment, String res_rule) {
		tv_dpop_res.setText("获得"+res_money+"元理财金");
		tv_give_res.setText(res_comment);
		tv_rule_res.setText(res_rule);
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
				animate.alpha(1f);

			}
		});
	}
	
	class MyChooseListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			
			//mNewCusMark.setVisibility(View.GONE);
			if(arg0==bespeak_info_bt_cancle){
				mChooseListener.chooseItem(0);
			}else if(arg0==bespeak_info_bt_back){
				mChooseListener.chooseItem(1);
			}
			
			dismiss();
		}
		
	}
	
	public interface ChooseListener{
		void chooseItem(int markItem);
	}
}
