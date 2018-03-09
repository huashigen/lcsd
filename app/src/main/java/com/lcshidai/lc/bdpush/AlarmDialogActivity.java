package com.lcshidai.lc.bdpush;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.MainActivity;
import com.lcshidai.lc.utils.MemorySave;

public class AlarmDialogActivity extends Activity implements OnClickListener {
	
	private final String PACKAGE_NAME = "com.trj.hp";
	private final String LOADING_ACTIVITY_NAME = "com.trj.hp.ui.LoadingActivity";
	private final String MAIN_ACTIVITY_NAME = "com.trj.hp.ui.MainActivity";
	private final String DETAILED_ACTIVITY_NAME = "com.trj.hp.bdpush.AlarmDialogActivity";
	private final String GESTURE_LOGIN_NAME = "com.trj.hp.ui.GestureLoginActivity";
	
	private LinearLayout main_ll;
	private Button submit_bt, cancel_bt;
	private MediaPlayer mediaPlayer;
	private Vibrator vibrator;
	private AudioManager audioManager;
	private String prjTypeName;
	private String nowTime;
	private String prjId;
	private TextView type_tv, time_tv;
	private WakeLock mWakelock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Window win = getWindow();
	    WindowManager.LayoutParams winParams = win.getAttributes();
	    winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
	            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
	            | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
	            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		if(null != getIntent()){
			prjTypeName = getIntent().getStringExtra("prj_type_name");
			nowTime = getIntent().getStringExtra("now_time");
			prjId = getIntent().getStringExtra("prj_id");
		}
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		acquireWakeLock();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		releaseWakeLock();
	}
	
	private void acquireWakeLock() {
        if (mWakelock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakelock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());
        }
            mWakelock.acquire();
    }
        
    private void releaseWakeLock() {
        if (mWakelock != null && mWakelock.isHeld()) {
            mWakelock.release();
            mWakelock = null;
        }
     }
        
	
	private void initView(){
		setContentView(R.layout.activity_alarm_dialog);
		main_ll = (LinearLayout) findViewById(R.id.alarm_dialog_ll_main);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		LayoutParams lp = (LayoutParams) main_ll.getLayoutParams();
		lp.width = (int) (dm.widthPixels * 0.85);
		main_ll.setLayoutParams(lp);
		
		submit_bt = (Button) findViewById(R.id.alarm_dialog_bt_submit);
		cancel_bt = (Button) findViewById(R.id.alarm_dialog_bt_cancel);
		type_tv = (TextView) findViewById(R.id.alarm_dialog_tv_prj_type);
		time_tv = (TextView) findViewById(R.id.alarm_dialog_tv_time);
		
		type_tv.setText(prjTypeName);
		time_tv.setText(nowTime);
		submit_bt.setOnClickListener(this);
		cancel_bt.setOnClickListener(this);
		
		mediaPlayer = new MediaPlayer();
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		try {
			mediaPlayer.setDataSource(AlarmDialogActivity.this, uri);
			audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//			mediaPlayer.setVolume(audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION), audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
			mediaPlayer.setLooping(false);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		//非静音模式
		if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
			vibrator.vibrate(2000);
		}
		
	}

	@Override
	public void onClick(View v) {
		if(mediaPlayer.isPlaying()){
			mediaPlayer.stop();
		}
		if(android.os.Build.VERSION.SDK_INT >= 11 && vibrator.hasVibrator()){
			vibrator.cancel();
		}
		switch(v.getId()){
		case R.id.alarm_dialog_bt_submit:
			int flag = getRunningProcess(AlarmDialogActivity.this);
			//程序已经运行
			if(flag == 1){
				Intent intent = new Intent();
				intent.setClass(AlarmDialogActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    		MemorySave.MS.mIsGoFinanceInfo=true;
	    		MemorySave.MS.mAlarmPrjId = prjId;
				startActivity(intent);
				AlarmDialogActivity.this.finish();
			}
			//3-启动APP时手势解锁页面		4-APP已经启动过的锁屏页面
			else if(flag == 3 || flag == 4){
				MemorySave.MS.mIsGoFinanceInfo=true;
	    		MemorySave.MS.mAlarmPrjId = prjId;
				AlarmDialogActivity.this.finish();
			}
			else{
				Intent intent = new Intent(Intent.ACTION_MAIN);
				ComponentName cn = new ComponentName(PACKAGE_NAME, LOADING_ACTIVITY_NAME);
				intent.setComponent(cn);
				intent.putExtra("alarm_to_finance", 1);
				intent.putExtra("prj_id", prjId);
				startActivity(intent);
				AlarmDialogActivity.this.finish();
			}
			break;
		case R.id.alarm_dialog_bt_cancel:
			AlarmDialogActivity.this.finish();
			break;
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		prjTypeName = getIntent().getStringExtra("prj_type_name");
		nowTime = getIntent().getStringExtra("now_time");
		prjId = getIntent().getStringExtra("prj_id");
		type_tv.setText(prjTypeName);
		time_tv.setText(nowTime);
		if(mediaPlayer.isPlaying()){
			mediaPlayer.stop();
		}
		if(android.os.Build.VERSION.SDK_INT >= 11 && vibrator.hasVibrator()){
			vibrator.cancel();
		}
		mediaPlayer.start();
		//非静音模式
		if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
			vibrator.vibrate(2000);
		}
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onDestroy() {
		if(mediaPlayer.isPlaying()){
			mediaPlayer.stop();
		}
		if(android.os.Build.VERSION.SDK_INT >= 11 && vibrator.hasVibrator()){
			vibrator.cancel();
		}
		vibrator = null;
		mediaPlayer.release();
		mediaPlayer = null;
		super.onDestroy();
	}
	
	/**
	 * 判断当前程序是否登陆正运行
	 * @param context
	 * @return	0:当前程序没有运行	1:当前程序有运行Main		2：当前程序只运行了AlarmDetailed
	 */
	public int getRunningProcess(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(
				Context.ACTIVITY_SERVICE);
		//获取当前系统正在运行的任务
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		//创建一个程序名和主activity的对象集合(将当前所有运行程序的此信息放入集合)
		List<ComponentName> cnList = new ArrayList<ComponentName>();
		for(RunningTaskInfo rapi : list){
			cnList.add(rapi.baseActivity);
		}
		
		//创建一个MainActivity的一个信息对象
		ComponentName main = new ComponentName(PACKAGE_NAME, MAIN_ACTIVITY_NAME);
		//已经有过运行MainActivity
		if(cnList.contains(main)){
			if(MemorySave.MS.mIsGestureLoginAlive){
				return 4;
			}
			return 1;
		}
		
		//创建一个AlarmDetailedActivity的一个信息对象
		ComponentName detailed = new ComponentName(PACKAGE_NAME, DETAILED_ACTIVITY_NAME);
		//正在运行AlarmDetailedActivity
		if(cnList.contains(detailed)){
			return 2;
		}
		
		//创建一个GestureLoginActivity的一个信息对象
		ComponentName gesture = new ComponentName(PACKAGE_NAME, GESTURE_LOGIN_NAME);
		//正在运行GestureLoginActivity
		if(cnList.contains(gesture)){
			return 3;
		}
		return 0;
	}
	
}
