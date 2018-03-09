package com.lcshidai.lc.ui.account;

import java.util.ArrayList;
import java.util.List;

import com.lcshidai.lc.R;
import com.lcshidai.lc.entity.MessageInfo;
import com.lcshidai.lc.ui.MainActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.TimeUtil;
import com.lcshidai.lc.widget.text.HelpCenterTextView;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 消息详情
 * @author 000853
 *
 */
public class MessageDetailActivity extends TRJActivity implements OnClickListener {

	private TextView mTvTitle, mSubTitle;
	private Button  mSaveBtn;
	ImageButton mBackBtn;
	private Context mContext;
	private MessageInfo msgInfo;
	private TextView type_tv, time_tv;
	private HelpCenterTextView info_tv;
	private Button submit_bt;
	private String message_centre = "", md_title = "", md_content = "", md_ctime = "";
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if(null != getIntent()){
			msgInfo = getIntent().getParcelableExtra("message_info");
			if(!TextUtils.isEmpty(getIntent().getStringExtra("message_centre"))){
				message_centre = getIntent().getStringExtra("message_centre");
				md_title = getIntent().getStringExtra("md_title");
				md_content = getIntent().getStringExtra("md_content");
				md_ctime = getIntent().getStringExtra("md_ctime");
			}
		}
		initView();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
		mContext = this;
		setContentView(R.layout.activity_message_detail);
		mBackBtn=(ImageButton) findViewById(R.id.btn_back);
		mBackBtn.setOnClickListener(this);
		mSaveBtn=(Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mTvTitle=(TextView)findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("消息详情");
		mSubTitle=(TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		
		type_tv = (TextView) findViewById(R.id.message_detail_tv_type);
		time_tv = (TextView) findViewById(R.id.message_detail_tv_time);
		info_tv = (HelpCenterTextView) findViewById(R.id.message_detail_tv_info);
		submit_bt = (Button) findViewById(R.id.message_detail_bt_submit);
		submit_bt.setOnClickListener(this);
		
		if(!TextUtils.isEmpty(message_centre)){
			type_tv.setText(md_title);
			time_tv.setText(md_ctime);
			info_tv.setText(md_content);
		}
		else{
			if(null != msgInfo){
				type_tv.setText(msgInfo.remind_title);
				time_tv.setText(TimeUtil.dateShowStyleConvert(msgInfo.show_time, "yyyy/MM/dd HH:mm","yy/MM/dd HH:mm"));
				info_tv.setText(msgInfo.remind_message);
				//新项目提醒
				if("31".equals(msgInfo.remind_type) && !TextUtils.isEmpty(msgInfo.prj_id) && !TextUtils.isEmpty(msgInfo.prj_type)){
					submit_bt.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			MessageDetailActivity.this.finish();
			break;
		case R.id.message_detail_bt_submit:
			Intent intent = new Intent();
			Bundle args=new Bundle();
			args.putString("prj_id", msgInfo.prj_id);
			args.putString("mTransferId", "");
			intent.putExtras(args);
			intent.setClass(mContext, FinanceProjectDetailActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		if(intent != null){
			md_title = intent.getStringExtra("md_title");
			md_content = intent.getStringExtra("md_content");
			md_ctime = intent.getStringExtra("md_ctime");
			type_tv.setText(md_title);
			time_tv.setText(md_ctime);
			info_tv.setText(md_content);
		}
		if(submit_bt.getVisibility() == View.VISIBLE){
			submit_bt.setVisibility(View.GONE);
		}
		super.onNewIntent(intent);
	}
	
	@Override
	public void doFinish() {
		finish();
	}
	
	@Override
	public void finish() {
		if(!isRunedMain()){
			GoLoginUtil.gestureAutoLogin(MessageDetailActivity.this, 0, MainActivity.class.getName());
		}
		super.finish();
	}
	
	/**
	 * 是否是点击通知启动的APP进入的消息详细页面
	 * @return
	 */
	private final String PACKAGE_NAME = "com.trj.hp";
	private final String MAIN_ACTIVITY_NAME = "com.trj.hp.ui.MainActivity";
	private boolean isRunedMain(){
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> running = am.getRunningTasks(100);
		List<ComponentName> main = new ArrayList<ComponentName>();
		for(RunningTaskInfo task : running){
			main.add(task.baseActivity);
		}
		ComponentName component = new ComponentName(PACKAGE_NAME, MAIN_ACTIVITY_NAME);
		return main.contains(component);
	}
}
