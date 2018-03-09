package com.lcshidai.lc.ui.account;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.PrjCountImpl;
import com.lcshidai.lc.model.account.ManageFinanceListData;
import com.lcshidai.lc.ui.fragment.account.ManageFinanceListInfoItemFragment;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.utils.MemorySave;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 理财记录详细信息
 * 
 * @author
 * 
 */
public class ManageFinanceListDetailsActivity extends ManageFinanceListInfoServiceActivity  implements PrjCountImpl{
	
	private ImageButton mBackBtn;
	private TextView mTvTitle, mSubTitle;
	private Button mSaveBtn;


	ManageFinanceListInfoItemFragment mFragment = new ManageFinanceListInfoItemFragment();
	int index,mCurrentP;
	boolean isRe;
	ImageView iv_top_content_flag, iv_biding0, iv_biding1, iv_biding2,
			iv_biding3, iv_biding00;
	// MyScrollView sv;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);
		setContentView(R.layout.activity_account_invest_records_details);
		if (getIntent() != null) {
			index = getIntent().getIntExtra("index", 0);// .getBoolean("isTransfer");
			mCurrentP = getIntent().getIntExtra("mCurrentP", 0);
			isRe = getIntent().getBooleanExtra("isRe", false);
			isTransfer = getIntent().getBooleanExtra("isTransfer", false);
			jobj = (ManageFinanceListData) getIntent().getSerializableExtra("jobj");
		}
		initView();
		mFragment.prjCi=this;
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
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
	
	private void initView() {
		mBackBtn = (ImageButton) findViewById(R.id.btn_back);
		mBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ManageFinanceListDetailsActivity.this.finish();
			}
		});
		mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
		mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		mSaveBtn = (Button) findViewById(R.id.btn_option);
		mSaveBtn.setBackgroundDrawable(null);
		mSaveBtn.setPadding(0, 0, DensityUtil.dip2px(this, 10), 0);
		initValue();
	
		mFragment.mStatus = index + "";
		mFragment.mType = mCurrentP + "";
		mFragment.isRepay = index == 5;
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			mFragment.isTransfer = isTransfer;
			fragmentTransaction.add(R.id.ll_1, mFragment);
			fragmentTransaction.commit();
		findViewById(R.id.ll_1).setVisibility(View.VISIBLE);
	}

	private void initValue() {
		SpannableStringBuilder style;
		String text;
		if (isTransfer) {
			switch (index) {
			case 1:
				mTvTitle.setText("可变现");
				text = "项目数:"+jobj.getCan_cash();
				style=new SpannableStringBuilder(text); 
				style.setSpan(new ForegroundColorSpan(Color.parseColor("#F4A460")),4,text.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
				mSaveBtn.setText(style);
				break;
			case 2:
				mTvTitle.setText("变现中");
				text = "项目数:"+jobj.getCashing();
				style=new SpannableStringBuilder(text); 
				style.setSpan(new ForegroundColorSpan(Color.parseColor("#F4A460")),4,text.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
				mSaveBtn.setText(style);
				break;
			case 3:
				mTvTitle.setText("已变现");
				text = "项目数:"+jobj.getCashed();
				style=new SpannableStringBuilder(text); 
				style.setSpan(new ForegroundColorSpan(Color.GREEN),4,text.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
				mSaveBtn.setText(style);
				break;
			default:
				break;
			}
		}else {
			switch (index) {
			case 1:
				mTvTitle.setText("投资中");
				text = "项目数:"+jobj.getStatus_1();
				style=new SpannableStringBuilder(text); 
				style.setSpan(new ForegroundColorSpan(Color.parseColor("#F4A460")),4,text.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
				mSaveBtn.setText(style);
				break;
			case 2:
				mTvTitle.setText("投资结束");
				text = "项目数:"+jobj.getStatus_2();
				style=new SpannableStringBuilder(text); 
				style.setSpan(new ForegroundColorSpan(Color.parseColor("#F4A460")),4,text.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
				mSaveBtn.setText(style);
				break;
			case 3:
				mTvTitle.setText("待回款");
				text = "项目数:"+jobj.getStatus_3();
				style=new SpannableStringBuilder(text); 
				style.setSpan(new ForegroundColorSpan(Color.GREEN),4,text.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
				mSaveBtn.setText(style);
				break;
			case 4:
				mTvTitle.setText("已回款结束");
				text = "项目数:"+jobj.getStatus_4();
				style=new SpannableStringBuilder(text); 
				style.setSpan(new ForegroundColorSpan(Color.parseColor("#F4A460")),4,text.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
				mSaveBtn.setText(style);
				break;
			case 5:
				mTvTitle.setText("预售投资");
				text = "项目数:"+jobj.getStatus_5();
				style=new SpannableStringBuilder(text); 
				style.setSpan(new ForegroundColorSpan(Color.parseColor("#F4A460")),4,text.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
				mSaveBtn.setText(style);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void setItemView() {
		initValue();
	}

	@Override
	public void setItemCashView() {
		initValue();
	}

	@Override
	public void prjCount() {
		hmflis.gainManageFinanceListInfo(isTransfer, mCurrentP,"1");
	}


}
