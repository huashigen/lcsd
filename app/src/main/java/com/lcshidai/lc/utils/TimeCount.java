package com.lcshidai.lc.utils;

import android.os.CountDownTimer;
import android.widget.Button;

public class TimeCount extends CountDownTimer {
	
	Button mBtnGainDn;
	
	public TimeCount(long millisInFuture,long countDownInterval){
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		
	}
	
	public static TimeCount newInstance(Button btn,long millisInFuture,long countDownInterval){
		TimeCount tc=new TimeCount(millisInFuture,countDownInterval);
		tc.mBtnGainDn=btn;
		return tc;
	}
	
	@Override
	public void onFinish() {//计时完毕时触发
		mBtnGainDn.setText(" 重新获取 ");
		mBtnGainDn.setClickable(true);
	}
	@Override
	public void onTick(long millisUntilFinished){//计时过程显示
		mBtnGainDn.setClickable(false);
		mBtnGainDn.setText(" 动态码("+millisUntilFinished /1000+")");
	}
}

