package com.lcshidai.lc.ui.fragment.finance;

import java.util.ArrayList;
import java.util.List;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.PageSelectedImpl;
import com.lcshidai.lc.impl.finance.FinanceInfoImpl;
import com.lcshidai.lc.model.finance.FinanceInfoJson;
import com.lcshidai.lc.service.finance.GetFinanceProjectDetailService;
import com.lcshidai.lc.widget.tabstrip.PagerSlidingTabStripManageFinance;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.ToastUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;

/**
 * 理财项目详情-第二部分
 */
@SuppressLint("HandlerLeak")
public class FinanceProjectDetailSecondFragment extends TRJFragment implements FinanceInfoImpl,
        OnTouchListener, OnGestureListener, PageSelectedImpl {
    GetFinanceProjectDetailService getFinanceProjectDetailService;
    private String mPrjId;
    private String mPrjType;
    private int isCollection;
    private boolean isFU = false;// 判断是否是速对通
    boolean isReg = false;

    public String limitMoney;
    public String canInvest;

    private Dialog mDialog;
    private MyAdapter mAdapter;
    private ViewPager vPager;
    private PagerSlidingTabStripManageFinance tabs;
    private List<String> mTitleList = new ArrayList<String>(); // 存储标题数据
    private List<TRJFragment> mFragments = new ArrayList<>();// 存储Fragments

    private View view;
    private GestureDetector detector;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPrjId = bundle.getString(Constants.Project.PROJECT_ID);
            isCollection = bundle.getInt(Constants.Project.IS_COLLECTION);
            isFU = bundle.getBoolean("isSu", false);
            isReg = bundle.getBoolean("isReg", false);
            limitMoney = bundle.getString(Constants.Project.LIMIT_MONEY);
            canInvest = bundle.getString(Constants.Project.CAN_INVEST);
        }
        getFinanceProjectDetailService = new GetFinanceProjectDetailService((TRJActivity) getActivity(), this);
        detector = new GestureDetector(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_manage_finance_detail_1, container, false);

//        initView();
        loadData();
        return view;
    }

    public void initView() {
        initData();
        mAdapter = new MyAdapter(getActivity().getSupportFragmentManager());

        vPager = (ViewPager) view.findViewById(R.id.pager);
        tabs = (PagerSlidingTabStripManageFinance) view.findViewById(R.id.tabs);
        if (mAdapter != null) {
            vPager.setAdapter(mAdapter);
            tabs.setViewPager(vPager, this);
        }
    }

    private void initData() {
        // 处理额外传值
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Project.PROJECT_ID, mPrjId);
        bundle.putString(Constants.Project.PROJECT_TYPE, mPrjType);
        if (limitMoney != null) {
            bundle.putString("limitMoney", limitMoney);
        }
        if (canInvest != null) {
            bundle.putString("canInvest", canInvest);
        }
        bundle.putInt(Constants.Project.IS_COLLECTION, isCollection);

        BaseInfoFragment baseInfoFragment;// 普通项目详情
        CollectionProBaseInfoFragment collectionProBaseInfoFragment;// 集合项目详情
        SecurityInfoFragment safeguardFragment;// 安全保障
        RepaymentPlanFragment repaymentPlanFragment;// 回款计划
        InvestRecordFragment investRecordFragment;// 投资记录
        if (isCollection == 1) {
            mTitleList.clear();
            mTitleList.add(getString(R.string.collection_prj_detail));
            mTitleList.add(getString(R.string.risk_control_instruction));
            mTitleList.add(getString(R.string.invest_record));
            collectionProBaseInfoFragment = new CollectionProBaseInfoFragment();
            collectionProBaseInfoFragment.setArguments(bundle);
            safeguardFragment = new SecurityInfoFragment();
            safeguardFragment.setArguments(bundle);
            investRecordFragment = new InvestRecordFragment();
            investRecordFragment.setArguments(bundle);
            mFragments.clear();
            mFragments.add(collectionProBaseInfoFragment);
            mFragments.add(safeguardFragment);
            mFragments.add(investRecordFragment);
        } else {
            mTitleList.clear();
            mTitleList.add(getString(R.string.normal_prj_detail));
            mTitleList.add(getString(R.string.risk_control_instruction));
            mTitleList.add(getString(R.string.received_payments_plan));
            mTitleList.add(getString(R.string.invest_record));
            baseInfoFragment = new BaseInfoFragment();
            baseInfoFragment.setArguments(bundle);
            safeguardFragment = new SecurityInfoFragment();
            safeguardFragment.setArguments(bundle);
            repaymentPlanFragment = new RepaymentPlanFragment();
            repaymentPlanFragment.setArguments(bundle);
            investRecordFragment = new InvestRecordFragment();
            investRecordFragment.setArguments(bundle);
            mFragments.clear();
            mFragments.add(baseInfoFragment);
            mFragments.add(safeguardFragment);
            mFragments.add(repaymentPlanFragment);
            mFragments.add(investRecordFragment);
        }
    }

    /**
     * 在项目详情页滑动到第二页的时候发送消息调用了这里的handlerMessage方法
     */
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            loadData();
        }

    };

    /**
     * 加载数据
     */
    public void loadData() {
        mDialog = TRJActivity.createLoadingDialog(getActivity(), "加载中...", true);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        getFinanceProjectDetailService.getFinanceProjectDetail(mPrjId, isCollection);
    }

    @Override
    public void getFinanceProjectDetailSuccess(FinanceInfoJson response) {
        mDialog.dismiss();
        mPrjType = response.getData().getPrj_type();
        initView();
    }

    @Override
    public void getFinanceProjectDetailFail() {
        ToastUtil.showToast(getActivity(), "网络不给力");
        mDialog.dismiss();
    }

    @Override
    public void selectedPage(int args) {

    }

    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTitleList.size();
        }

        @Override
        public Fragment getItem(int pos) {
            return mFragments.get(pos);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position % mTitleList.size()).toUpperCase();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);
        return true;
    }

}