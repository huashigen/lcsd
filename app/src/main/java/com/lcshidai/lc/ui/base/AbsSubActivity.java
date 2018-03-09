package com.lcshidai.lc.ui.base;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.utils.BaseAppManager;
import com.lcshidai.lc.utils.MemorySave;

/** 继承该类即可实现子Activity的功能 */
public abstract class AbsSubActivity extends TRJActivity{
		
	private AbsSubActivity requestSubActivity;
	private Dialog mExitDialog;
	private boolean dialogShowing = false;
	
	private ArrayList<String> mClass = null;
	
	public static final String TAB1="com.trj.hp.ui.project.ProjectActivity";
	public static final String TAB2="com.trj.hp.ui.finance.ManageFinanceActivity";
	public static final String TAB3="com.trj.hp.ui.account.AccountActivity";
	public static final String TAB4="com.trj.hp.ui.DiscoveryActivity";
//	public static final String TAB5="com.trj.hp.ui.transfer.ManageTransferActivity";
	public static final String TAB5="com.trj.hp.ui.newfinan.NewFinanceActivity";
	
	public AbsSubActivity getRequestSubActivity() {
		return requestSubActivity;
	}

	public void setRequestSubActivity(AbsSubActivity requestSubActivity) {
		this.requestSubActivity = requestSubActivity;
	}
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mClass = new ArrayList<String>();
	   
	    mClass.add(TAB2);
	    mClass.add(TAB3);
	    mClass.add(TAB4);
	    mClass.add(TAB5);
	    mClass.add(TAB1);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Intent it = getIntent();
		if(mClass.contains(it.getComponent().getClassName())){
			mExitDialog = createDialog("您确定要退出吗？", "确定", "取消", 
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//解除当前用户推送绑定
//							new BDPushUitl(AbsSubActivity.this).deletePushInfoToServer();
							LCHttpClient.clearCookie();
							MemorySave.MS.mIsLogin = false;
							MemorySave.MS.mIsLogined=false;
							MemorySave.MS.userInfo = null;
							MemorySave.MS.mIsGoFinance = false;
							MemorySave.MS.isLoginSendBroadcast = false;
							finish();
							BaseAppManager.getInstance().clear();
							if(mExitDialog.isShowing()){
								mExitDialog.dismiss(); 
							}
							System.exit(0);
						}
					}, 
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(mExitDialog.isShowing()){
								mExitDialog.dismiss(); 
							}
						}
					});
			if(dialogShowing) mExitDialog.show();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(mExitDialog != null && mExitDialog.isShowing()) {
			dialogShowing = mExitDialog.isShowing();
			mExitDialog.dismiss();
		} else {
			dialogShowing = false;
		}
	}

	private Class getTargetClass(Intent intent){
		Class clazz = null;
		try {
			if(intent.getComponent() != null)
			clazz = Class.forName(intent.getComponent().getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}
	
	// 重写了startActivity()方法，
	// 这样调用该方法时会根据目标Activity是否是子Activity而调用不同的方法
	@Override
	public void startActivity(Intent intent) {
		if( getTargetClass(intent) != null && AbsSubActivity.class.isAssignableFrom(getTargetClass(intent)) ){
			if(this.getParent() instanceof AbsActivityGroup){
				intent.putExtra("fromSubActivity", getClass().getName());
				((AbsActivityGroup)this.getParent()).launchNewActivity(intent);
			}
		}else{
			super.startActivity(intent);
		}
	}

	// 重写了startActivity()方法，
	// 这样调用该方法时会根据目标Activity是否是子Activity而调用不同的方法
	public void startActivityNoFrom(Intent intent) {
			super.startActivity(intent);
	}
	
	// 重写了startActivityForResult()方法，
	// 这样调用该方法时会根据目标Activity是否是子Activity而调用不同的方法
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if( getTargetClass(intent) != null && AbsSubActivity.class.isAssignableFrom(getTargetClass(intent)) ){
			if(this.getParent() instanceof AbsActivityGroup){
				intent.putExtra("fromSubActivity", getClass().getName());
				((AbsActivityGroup) this.getParent())
						.launchNewActivityForResult(this, intent, requestCode);
			}
		}else{
			super.startActivityForResult(intent, requestCode);
		}
	}
		
	public void goHome(){
		 ((AbsActivityGroup)this.getParent()).goFinanceTemp();
	}
	
	/** 调用此方法来返回上一个界面 */
	public void goback(String name) {
		Class clazz = null;
		try {
			Intent intent=getIntent();
			String className=intent.getStringExtra("fromSubActivity");
			clazz = Class.forName(className);
		} catch (Exception e) {
			if(name.equals("com.trj.hp.ui.finance.ManageFinanceInfoActivity_2")){
				exit();
			}
			return;
		}
		Intent intent = new Intent(this,clazz);
		((AbsActivityGroup)this.getParent()).launchActivity(intent);
	}
	
	/** 调用此方法来返回上一个界面并返回数据 */
	public void gobackWithResult(int resultCode, Intent data) {
		Class clazz = null;
		try {
			clazz = Class.forName(getIntent().getStringExtra("fromSubActivity"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		data.setClass(this, clazz);
		if( requestSubActivity != null){
			requestSubActivity.onActivityResult(
					data.getIntExtra("requestCode", 0), resultCode, data);
		}
		((AbsActivityGroup)this.getParent()).launchActivity(data);	
	}
	
	protected void exit() {
		dialogShowing = true;
		if(mExitDialog != null) mExitDialog.show();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent it = getIntent();
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	String className=it.getComponent().getClassName();
	    	if(mClass.contains(className)){
				exit();
			}else{
				goback(className);
			}
	        return true;
	    } else {
	        return super.onKeyDown(keyCode, event);
	    }
	}
	
}
