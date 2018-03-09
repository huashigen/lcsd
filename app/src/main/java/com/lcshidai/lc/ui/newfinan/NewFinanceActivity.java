package com.lcshidai.lc.ui.newfinan;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.LoginActivity;
import com.lcshidai.lc.ui.account.AccountActivity;
import com.lcshidai.lc.ui.base.AbsActivityGroup;
import com.lcshidai.lc.ui.base.AbsSubActivity;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.ui.fragment.newfinan.NewFinanceListFragment;
import com.lcshidai.lc.utils.MemorySave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 我要理财（消湖理财）
 * @author 000853
 *
 */
public class NewFinanceActivity extends AbsSubActivity implements OnClickListener {

	private TextView mTvTitle, mSubTitle;
	private Button  mSaveBtn;
	ImageButton mBackBtn;
	private Context mContext;
	
	private Button submit_bt;
	private int pageFlag = 0;	//0:未登录	1：未验证		2：已验证
	private LinearLayout authentication_ll, list_ll;
	private NewFinanceListFragment newFinanceListFragment;
	private boolean isAddedListFragment = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);
		initView();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
		mContext = this;
		setContentView(R.layout.activity_newfinance);
		mBackBtn=(ImageButton) findViewById(R.id.btn_back);
		mBackBtn.setVisibility(View.INVISIBLE);
		mSaveBtn=(Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mTvTitle=(TextView)findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("我要理财");
		mSubTitle=(TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		
		authentication_ll = (LinearLayout) findViewById(R.id.newfinance_ll_authentication);
		list_ll = (LinearLayout) findViewById(R.id.newfinance_ll_list);
		submit_bt = (Button) findViewById(R.id.newfinance_submit_bt);
		submit_bt.setOnClickListener(this);
	}
	
	public boolean getIsAddedListFragment(){
		return isAddedListFragment;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!MemorySave.MS.mIsLogin){
			pageFlag = 0;
			list_ll.setVisibility(View.GONE);
			authentication_ll.setVisibility(View.VISIBLE);
			if(isAddedListFragment && null != newFinanceListFragment){
				isAddedListFragment = false;
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.remove(newFinanceListFragment);
				ft.commit();
			}
			submit_bt.setText("登陆即送理财礼包");
		}else{
			if(null != MemorySave.MS.userInfo && "0".equals(MemorySave.MS.userInfo.is_active)){
				pageFlag = 1;
				list_ll.setVisibility(View.GONE);
				authentication_ll.setVisibility(View.VISIBLE);
				if(isAddedListFragment && null != newFinanceListFragment){
					isAddedListFragment = false;
					FragmentManager fm = getSupportFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					ft.remove(newFinanceListFragment);
					ft.commit();
				}
				submit_bt.setText("立即认证");
			}else{
				pageFlag = 2;
				authentication_ll.setVisibility(View.GONE);
				list_ll.setVisibility(View.VISIBLE);
				if(!isAddedListFragment){
					isAddedListFragment = true;
					newFinanceListFragment = new NewFinanceListFragment();
					FragmentManager fm = getSupportFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					ft.add(R.id.newfinance_ll_list, newFinanceListFragment);
					ft.commit();
				}
			}
		}
		
		MemorySave.MS.mIsFinanceInfoFinish=false;
		
		if(MemorySave.MS.mIsGoHome){
    		((AbsActivityGroup)getParent()).goFinanceTemp();
			MemorySave.MS.mIsGoHome = false;
			return;
    	}
		
		if(MemorySave.MS.mIsGoFinance){
    		((AbsActivityGroup)getParent()).goFinanceTemp();
			MemorySave.MS.mIsGoFinance = false;
			return;
    	}
		
		if(MemorySave.MS.mIsGoAccount){
    		((AbsActivityGroup)getParent()).goAccountTemp();
			MemorySave.MS.mIsGoAccount = false;
			return;
    	}
		
		if (MemorySave.MS.mIsGoFinanceRecord) {
			((AbsActivityGroup) getParent()).goAccount(new Intent(
					NewFinanceActivity.this, AccountActivity.class));
			MemorySave.MS.mIsGoFinanceRecord=false;
			return;
		}
		
		if(MemorySave.MS.mIsGoFinanceInfo){
    		MemorySave.MS.mIsGoFinanceInfo = false;
    		MemorySave.MS.mAlarmLoginFlag = true;
    		Intent intent = new Intent();
    		intent.setClass(NewFinanceActivity.this, FinanceProjectDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("prj_id", MemorySave.MS.mAlarmPrjId);
			intent.putExtras(bundle);
			startActivity(intent);
			return;
    	}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.newfinance_submit_bt:
			if(pageFlag == 0){
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
			}else if(pageFlag == 1){
				Intent intent = new Intent(mContext, QualificationsAuthenticationActivity.class);
				startActivity(intent);
			}
			break;
		}
	}
	
}
