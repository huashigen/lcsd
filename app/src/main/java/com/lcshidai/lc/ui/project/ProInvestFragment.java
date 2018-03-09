package com.lcshidai.lc.ui.project;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.finance.FinancePosterImpl;
import com.lcshidai.lc.model.finance.FinancePosterJson;
import com.lcshidai.lc.service.finance.HttpFinancePosterService;
import com.lcshidai.lc.ui.adapter.FinanceFragmentAdapter;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.finance.FinanceFilterActivity;
import com.lcshidai.lc.ui.fragment.finance.FinanceTabItemFragment;
import com.lcshidai.lc.utils.LoginStatusHelper;
import com.lcshidai.lc.widget.ppwindow.FinancePosterPopupWindow;

/**
 * Created by Allin on 2016/7/18.
 *  投资页主页
 */
public class ProInvestFragment extends TRJFragment implements View.OnClickListener, FinancePosterImpl {
    Resources resources;
    private TextView tv_tab_1, tv_tab_2, tv_tab_3, tv_tab_4;
    private TextView[] tv_tabs;

    FinanceTabItemFragment[] fragments;
    FinanceTabItemFragment mfisrtFragment;
    FinanceTabItemFragment mSecondFragment;
    FinanceTabItemFragment mThirdFragment;
    FinanceTabItemFragment mFourthFragment;

//    private ImageButton top_back_btn;
//    private TextView top_title_text;
//    private TextView top_action_text;

    //	private ImageView iv_type;
    private ViewPager mViewPager;

    private HttpFinancePosterService mHttpFinancePosterService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invest_project_list, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resources = getResources();
        InitTextView(view);

        mHttpFinancePosterService = new HttpFinancePosterService((TRJActivity)getActivity(), this);
        mHttpFinancePosterService.gainPoster();
    }

    @Override
    public void onResume() {
        super.onResume();
        super.onResume();
//		tv_tab_1.performClick();
        if(LoginStatusHelper.LoginStatusChange){
            LoginStatusHelper.LoginStatusChange = false;
            for (FinanceTabItemFragment financeTabItemFragment : fragments) {
                financeTabItemFragment.reLoadData();
            }
        }
    }

    private void InitTextView(View view) {

        tv_tab_1 = (TextView) view.findViewById(R.id.tv_tab_1);
        tv_tab_2 = (TextView) view.findViewById(R.id.tv_tab_2);
        tv_tab_3 = (TextView) view.findViewById(R.id.tv_tab_3);
        tv_tab_4 = (TextView) view.findViewById(R.id.tv_tab_4);
        tv_tabs = new TextView[]{tv_tab_1, tv_tab_2, tv_tab_3, tv_tab_4};
        tv_tab_1.setOnClickListener(this);
        tv_tab_2.setOnClickListener(this);
        tv_tab_3.setOnClickListener(this);
        tv_tab_4.setOnClickListener(this);

//		iv_type = (ImageView) findViewById(R.id.iv_type);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);

        mfisrtFragment = new FinanceTabItemFragment();
        mSecondFragment = new FinanceTabItemFragment();
        mThirdFragment = new FinanceTabItemFragment();
        mFourthFragment = new FinanceTabItemFragment();

        mfisrtFragment.mCurrentPType = FinanceTabItemFragment.P_TYPE_ALL;
        mSecondFragment.mCurrentPType = FinanceTabItemFragment.P_TYPE_2;
        mThirdFragment.mCurrentPType = FinanceTabItemFragment.P_TYPE_3;
        mFourthFragment.mCurrentPType = FinanceTabItemFragment.P_TYPE_1;

//		switchFragment(FIR_FRAGMENT, true);

        tv_tab_1.setSelected(true);

        fragments = new FinanceTabItemFragment[]{mfisrtFragment, mSecondFragment, mThirdFragment, mFourthFragment};
        FinanceFragmentAdapter adapter = new FinanceFragmentAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                for (TextView tv : tv_tabs) {
                    tv.setSelected(false);
                }
                tv_tabs[arg0].setSelected(true);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    private void setPager(int position){
        mViewPager.setCurrentItem(position, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tab_1:
                setPager(0);
                tv_tab_1.setSelected(true);
                tv_tab_2.setSelected(false);
                tv_tab_3.setSelected(false);
                tv_tab_4.setSelected(false);
                break;
            case R.id.tv_tab_2:
                setPager(1);
                tv_tab_1.setSelected(false);
                tv_tab_2.setSelected(true);
                tv_tab_3.setSelected(false);
                tv_tab_4.setSelected(false);
                break;
            case R.id.tv_tab_3:
                setPager(2);
                tv_tab_1.setSelected(false);
                tv_tab_2.setSelected(false);
                tv_tab_3.setSelected(true);
                tv_tab_4.setSelected(false);
                break;
            case R.id.tv_tab_4:
                setPager(3);
                tv_tab_1.setSelected(false);
                tv_tab_2.setSelected(false);
                tv_tab_3.setSelected(false);
                tv_tab_4.setSelected(true);
//			iv_type.setVisibility(View.VISIBLE);
//			iv_type.setImageResource(R.drawable.finance_type_ying);
                break;
            case R.id.ib_top_bar_back:
                getActivity().finish();
                break;
            case R.id.tv_top_bar_right_action:
                Intent intent = new Intent(getActivity(), FinanceFilterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    public void gainFinancePostersuccess(FinancePosterJson response) {
        if(response != null){
            if(response.getBoolen().equals("1") && response.getData() != null){
                Bundle bundle = new Bundle();
                bundle.putString("pic", response.getData().getPic());
//				bundle.putString("pic", "https://ywuat.tourongjia.com/public/images/mobile/haibao.png");
                FinancePosterPopupWindow financePosterPopupWindow = new FinancePosterPopupWindow((TRJActivity)getActivity(), bundle);
                financePosterPopupWindow.showAtLocation(getView().findViewById(R.id.main), Gravity.CENTER, 0, 0);
            }
        }
    }

    @Override
    public void gainFinancePosterfail() {

    }

}
