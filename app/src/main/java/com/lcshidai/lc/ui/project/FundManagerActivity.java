package com.lcshidai.lc.ui.project;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.PageSelectedImpl;
import com.lcshidai.lc.model.licai.FundDetailInfo;
import com.lcshidai.lc.model.licai.FundManagerBean;
import com.lcshidai.lc.widget.tabstrip.PagerSlidingTabStrip;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 基金经理 Activity
 */
public class FundManagerActivity extends TRJActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, PageSelectedImpl {

    @Bind(R.id.ib_top_bar_back)
    ImageButton topBackBtn;
    @Bind(R.id.tv_top_bar_title)
    TextView topTitleText;
    @Bind(R.id.tv_top_bar_right_action)
    TextView topActionText;
    @Bind(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @Bind(R.id.ll_content_container)
    LinearLayout llContentContainer;
    @Bind(R.id.pagerSlidingTabStrip)
    PagerSlidingTabStrip pagerSlidingTabStrip;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tv_empty)
    TextView tvEmpty;

    private List<FundManagerBean> fundManagerBeanList;
    private List<Fragment> fragmentList = new ArrayList<>();
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foud_manager);
        ButterKnife.bind(this);

        initViewsAndEvents();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initViewsAndEvents() {
        topBackBtn.setOnClickListener(this);
        topTitleText.setText("基金经理");
        FundDetailInfo fundDetailInfo = (FundDetailInfo) getIntent().getSerializableExtra(Constants.FUND_DETAIL);
        if (null != fundDetailInfo) {
            fundManagerBeanList = fundDetailInfo.getFundManager();
            if (null != fundManagerBeanList && fundManagerBeanList.size() > 0) {
                llContentContainer.setVisibility(View.VISIBLE);
                tvEmpty.setVisibility(View.GONE);
                viewPager.setOnPageChangeListener(this);
                for (int i = 0; i < fundManagerBeanList.size(); i++) {
                    fragmentList.add(FundManagerFragment.newInstance(fundManagerBeanList.get(i)));
                }
                adapter = new MyPagerAdapter(getSupportFragmentManager(), fundManagerBeanList, fragmentList);
                viewPager.setAdapter(adapter);
                pagerSlidingTabStrip.setViewPager(viewPager, this);
                pagerSlidingTabStrip.setTextColor(Color.parseColor("#848382"));
                pagerSlidingTabStrip.setTabTextSelectedColor(getResources().getColor(R.color.theme_color));
                pagerSlidingTabStrip.setTextSize(DensityUtil.sp2px(mContext, 14f));
            } else {
                llContentContainer.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;

            default:

                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void selectedPage(int args) {
        onPageSelected(args);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private List<FundManagerBean> list;
        private List<Fragment> fragmentList;

        public MyPagerAdapter(FragmentManager fm, List<FundManagerBean> list, List<Fragment> fragmentList) {
            super(fm);
            this.list = list;
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            if (null != fragmentList && fragmentList.size() > 0) {
                return fragmentList.get(position);
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getXname();
        }

    }
}
