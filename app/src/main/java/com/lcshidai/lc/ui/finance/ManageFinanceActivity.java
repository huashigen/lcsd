package com.lcshidai.lc.ui.finance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.LoginActivity;
import com.lcshidai.lc.ui.account.UserPayPwdFirstSetActivity;
import com.lcshidai.lc.ui.base.AbsActivityGroup;
import com.lcshidai.lc.ui.base.AbsSubActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.fragment.finance.FragmentHome;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.widget.FixedSpeedScroller;
import com.lcshidai.lc.widget.HomeViewPager;

import java.lang.reflect.Field;

/**
 * 理财   与首页相关
 *
 * @author 000814
 */
public class ManageFinanceActivity extends AbsSubActivity {

//    首页
    private FragmentHome mFinanceOneFragment = new FragmentHome();

    private TRJFragment[] mFragments = {
            mFinanceOneFragment,    //首页
    };

    private Fragment nowFragment = mFinanceOneFragment;

    private MyAdapter adapter;
    private HomeViewPager vPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_manage_finance);
        flag = true;
        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        vPager = (HomeViewPager) findViewById(R.id.pager);
        vPager.setOffscreenPageLimit(mFragments.length);
        //禁止滑动
        vPager.setScrollble(false);

        adapter = new MyAdapter(getSupportFragmentManager());

        if (adapter != null) {
            vPager.setAdapter(adapter);// getChildFragmentManager())
            setViewPagerSpeed(0);
        }

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MemorySave.MS.mIsGoAccount) {
            ((AbsActivityGroup) getParent()).goAccountTemp();
            MemorySave.MS.mIsGoAccount = false;
            return;
        }
        if (MemorySave.MS.mIsGoFinanceHome) {
            setSelectPageHome(1);
            MemorySave.MS.mIsGoFinanceHome = false;
            return;
        }

        if (MemorySave.MS.isBidSuccessBack
                || MemorySave.MS.mIsFinanceInfoFinish
                || MemorySave.MS.goToFinanceAll
                || MemorySave.MS.isNeedRefreshItemList) {
            MemorySave.MS.mIsFinanceInfoFinish = false;
            MemorySave.MS.isBidSuccessBack = false;
            MemorySave.MS.goToFinanceAll = false;
            MemorySave.MS.isNeedRefreshItemList = false;
            setSelectPageHome(0);
            moarkreChoose(false);
        }

        if (MemorySave.MS.isRechargeSuccessToFinance) {
            if (MemorySave.MS.userInfo.is_paypwd_mobile_set == null
                    || !MemorySave.MS.userInfo.is_paypwd_mobile_set.equals("1")) {
                Intent intent = new Intent(this,
                        UserPayPwdFirstSetActivity.class);
                startActivity(intent);
                return;
            }
            MemorySave.MS.isRechargeSuccessToFinance = false;
        }

    }

    private void moarkreChoose(boolean notRe) {
        if (nowFragment == mFinanceOneFragment) {
            mFinanceOneFragment.resetData(notRe);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE) {
            if (resultCode == LoginActivity.LOGIN_SUCCESS) {
            } else {
//                if (getIntent().getStringExtra("goClass") == null || getIntent().getStringExtra("goClass").equals(""))
//                    finish();
            }
            getIntent().removeExtra("goClass");
        }
    }

    private void setSelectPageHome(int item) {
        vPager.setCurrentItem(item);
    }

    private class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return mFragments[pos];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }

    @Override
    protected void doGestureEnd() {
        super.doGestureEnd();
        moarkreChoose(false);
    }

    private void setViewPagerSpeed(int speed) {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(vPager.getContext());
            scroller.setmDuration(speed);
            mField.set(vPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
