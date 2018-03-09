package com.lcshidai.lc.ui.finance;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.PageSelectedImpl;
import com.lcshidai.lc.impl.PrjCountImpl;
import com.lcshidai.lc.model.account.ManageFinanceListData;
import com.lcshidai.lc.widget.tabstrip.PagerSlidingTabStripManageFinance;
import com.lcshidai.lc.ui.account.ManageFinanceListInfoServiceActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.fragment.account.ManageFinanceListInfoItemFragment_Re;
import com.lcshidai.lc.utils.MemorySave;

/**
 * 投资记录页面
 * 
 * @author
 * 
 */
public class ManageFinaceInfoListReActivity extends ManageFinanceListInfoServiceActivity implements PageSelectedImpl,  PrjCountImpl,
		OnClickListener {

	private String[] CONTENT;
	private ManageFinanceListInfoItemFragment_Re mFinanceZCFragmentD = new ManageFinanceListInfoItemFragment_Re();
	private ManageFinanceListInfoItemFragment_Re mFinanceCZFragmentH = new ManageFinanceListInfoItemFragment_Re();
 
	private PagerSlidingTabStripManageFinance tabs;
	private MyAdapter adapter;
	private ViewPager vPager;

	private Button mSaveBtn;
	ImageButton mBackBtn;
	private TextView mTvTitle, tv_subtitle;
	private RelativeLayout reHelp;
	int index,mCurrentP;
	TextView tv_text1,tvYuQi;
	boolean isRe; 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);
		setContentView(R.layout.activity_manage_finace_list);
		if (getIntent() != null) {
			index = getIntent().getIntExtra("index", 0);// .getBoolean("isTransfer");
			mCurrentP = getIntent().getIntExtra("mCurrentP", 0);
			isRe = getIntent().getBooleanExtra("isRe", false);
			isTransfer = getIntent().getBooleanExtra("isTransfer", false);
			jobj = (ManageFinanceListData) getIntent().getSerializableExtra("jobj");
		}
		initView();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView() {
		CONTENT = getResources().getStringArray(R.array.manage_finace_title);
		vPager = (ViewPager) findViewById(R.id.pager);
		mFinanceZCFragmentD.mCurrentPType="1";
		mFinanceCZFragmentH.mCurrentPType="2";
		tabs = (PagerSlidingTabStripManageFinance) findViewById(R.id.tabs);
		tvYuQi = (TextView)findViewById(R.id.tvYuQi);
		tv_text1= (TextView)findViewById(R.id.tv_text1);
		String tvYuQiStr=getIntent().getStringExtra("tvYuQi");
		String daiShouBenJinStr=getIntent().getStringExtra("daiShouBenJinStr");
		tvYuQi.setText(tvYuQiStr);
		tv_text1.setText(daiShouBenJinStr);
		flag = true;
		adapter = new MyAdapter(getSupportFragmentManager());

		if (adapter != null) {
			vPager.setAdapter(adapter);
			tabs.setViewPager(vPager, ManageFinaceInfoListReActivity.this);
		}

		mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("投资记录");
		tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
		tv_subtitle.setVisibility(View.GONE);
		mBackBtn = (ImageButton) findViewById(R.id.btn_back);
		mBackBtn.setOnClickListener(this);

		mSaveBtn = (Button) findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.GONE);
		reHelp = (RelativeLayout) findViewById(R.id.reHelp);
		reHelp.setOnClickListener(this);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ManageFinaceInfoListReActivity.this.finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	class MyAdapter extends FragmentStatePagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}

		@Override
		public Fragment getItem(int pos) {
			TRJFragment newFragment = null;
			// mFinanceZCFragment mFinanceCZFragment mFinanceTZFragment
			// mFinanceYYFragment
			if (pos == 0) {
				newFragment = mFinanceZCFragmentD;
				// mFinanceZCFragment.initData(0);
			} else if (pos == 1) {
				newFragment = mFinanceCZFragmentH;
			}   
			return newFragment;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}
	}

	@Override
	public void selectedPage(int args) {
		switch (args) {
		case 0:
			hmflis.gainManageFinanceListInfo(isTransfer, mCurrentP,"1");
			break;
		case 1:
			hmflis.gainManageFinanceListInfo(isTransfer, mCurrentP,"2");
			break;
//		case 2:
//			// mFinanceTZFragment.initData(2);
//			break;
//		case 3:
//			// mFinanceYYFragment.initData(3);
//			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			ManageFinaceInfoListReActivity.this.finish();
			break;
		 
		}

	}
	@Override
	protected void onResume() {
		super.onResume();
		if (MemorySave.MS.mIsGoFinanceRecord
				|| MemorySave.MS.mIsFinanceInfoFinish) {// 投标成功去账户概括整个逻辑结束
			finish();
			return;
		}
	}
	@Override
	public void prjCount() {
		hmflis.gainManageFinanceListInfo(isTransfer, mCurrentP,"1");
	}
	@Override
	public void setItemView() {
	 
	}

	@Override
	public void setItemCashView() {
		 
	}

}