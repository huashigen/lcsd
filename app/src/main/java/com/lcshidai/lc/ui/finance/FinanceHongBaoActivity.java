package com.lcshidai.lc.ui.finance;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.adapter.FinanceFragmentAdapter;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.fragment.finance.FinanceHBFragment;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.RewardCheckUtil;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class FinanceHongBaoActivity extends TRJActivity implements OnClickListener {
    private ImageButton top_back_btn;
    private TextView top_title_text;
    private TextView tv_tab_1, tv_tab_2, tv_tab_3;
    private View v_tab_1, v_tab_2;
    private TextView[] tv_tabs;
    private View[] v_tabs;
    private ViewPager mViewPager;

    private FinanceHBFragment[] fragments;
    private FinanceHBFragment mfisrtFragment;
    private FinanceHBFragment mSecondFragment;
    private FinanceHBFragment mThirdFragment;

    private String prj_id, amount, reward_type, bouns_rate, bouns_prj_term, mjq_id;
    private int isCollection = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_finance_hongbao);

        prj_id = getIntent().getStringExtra("prj_id");
        amount = getIntent().getStringExtra("amount");
        reward_type = getIntent().getStringExtra("reward_type");
        bouns_rate = getIntent().getStringExtra("bouns_rate");
        bouns_prj_term = getIntent().getStringExtra("bouns_prj_term");
        mjq_id = getIntent().getStringExtra("mjq_id");
        isCollection = getIntent().getIntExtra(Constants.Project.IS_COLLECTION, 0);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        top_back_btn = (ImageButton) findViewById(R.id.ib_top_bar_back);
        top_back_btn.setOnClickListener(this);
        top_title_text = (TextView) findViewById(R.id.tv_top_bar_title);
        top_title_text.setText("红包与券");

        tv_tab_1 = (TextView) findViewById(R.id.tv_tab_1);
        tv_tab_2 = (TextView) findViewById(R.id.tv_tab_2);
//		tv_tab_3 = (TextView) findViewById(R.id.tv_tab_3);
        tv_tabs = new TextView[]{tv_tab_1, tv_tab_2};
        tv_tab_1.setOnClickListener(this);
        tv_tab_2.setOnClickListener(this);
//		tv_tab_3.setOnClickListener(this);

        v_tab_1 = findViewById(R.id.v_tab_1);
        v_tab_2 = findViewById(R.id.v_tab_2);
        v_tabs = new View[]{v_tab_1, v_tab_2};

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        Bundle args = new Bundle();
        args.putString("prj_id", prj_id);
        args.putString("amount", amount);
        args.putString("reward_type", reward_type);
        args.putString("bouns_rate", bouns_rate);
        args.putString("bouns_prj_term", bouns_prj_term);
        args.putString("mjq_id", mjq_id);
        args.putInt(Constants.Project.IS_COLLECTION, isCollection);

        mfisrtFragment = new FinanceHBFragment();
        mfisrtFragment.TabType = FinanceHBFragment.HB;
        mfisrtFragment.setArguments(args);
        mSecondFragment = new FinanceHBFragment();
        mSecondFragment.TabType = FinanceHBFragment.MJQ;
        mSecondFragment.setArguments(args);
//		mThirdFragment = new FinanceHBFragment();
//		mThirdFragment.TabType = FinanceHBFragment.JXQ;
//		mThirdFragment.setArguments(args);
        fragments = new FinanceHBFragment[]{mfisrtFragment, mSecondFragment};

        FinanceFragmentAdapter adapter = new FinanceFragmentAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
//				for (TextView tv : tv_tabs) {
//					tv.setSelected(false);
//				}
//				tv_tabs[arg0].setSelected(true);
                for (View v : v_tabs) {
                    v.setVisibility(View.INVISIBLE);
                }
                v_tabs[arg0].setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        if (reward_type != null && reward_type.equals("1")) {
//			tv_tab_1.setSelected(true);
            v_tabs[0].setVisibility(View.VISIBLE);
            setPager(0);
        } else if (reward_type != null && reward_type.equals("2")) {
//			tv_tab_2.setSelected(true);
            v_tabs[1].setVisibility(View.VISIBLE);
            setPager(1);
        } else {
//			tv_tab_1.setSelected(true);
            v_tabs[0].setVisibility(View.VISIBLE);
            setPager(0);
        }
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;
            case R.id.tv_tab_1:
                setPager(0);
                break;
            case R.id.tv_tab_2:
                setPager(1);
                break;
            case R.id.tv_tab_3:
                setPager(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        if (RewardCheckUtil.getInstance().is_perform_clicked()) {
            if (RewardCheckUtil.getInstance().getHBCheckReference().get() != null) {
                RewardCheckUtil.getInstance().getHBCheckReference().get().updateHbMjq();
            }
        }
    }

    private void setPager(int position) {
        mViewPager.setCurrentItem(position, true);
    }

}
