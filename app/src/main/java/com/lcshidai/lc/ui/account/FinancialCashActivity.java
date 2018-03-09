package com.lcshidai.lc.ui.account;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.PageSelectedImpl;
import com.lcshidai.lc.impl.account.GoldFinanceImpl;
import com.lcshidai.lc.impl.account.GoldFinanceYieldImpl;
import com.lcshidai.lc.model.account.GoldFinanceData;
import com.lcshidai.lc.model.account.GoldFinanceJson;
import com.lcshidai.lc.model.account.GoldFinanceYieldData;
import com.lcshidai.lc.model.account.GoldFinanceYieldJson;
import com.lcshidai.lc.service.account.HttpGoldFinanceService;
import com.lcshidai.lc.service.account.HttpGoldFinanceYieldService;
import com.lcshidai.lc.widget.tabstrip.PagerSlidingTabStripManageFinance;
import com.lcshidai.lc.ui.MainActivity;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.fragment.more.GoldenTabItemFragment;
import com.lcshidai.lc.ui.fragment.more.GoldenTabItemUsedFragment;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.NetUtils;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.ppwindow.DialogPopupWindow;

/**
 * 理财金
 */
public class FinancialCashActivity extends TRJActivity implements OnClickListener, GoldFinanceImpl,
        GoldFinanceYieldImpl, PageSelectedImpl {
    private HttpGoldFinanceService hgfs;
    private HttpGoldFinanceYieldService hgfys;
    private String[] CONTENT;

    GoldenTabItemFragment mFinanceHaveFragment = new GoldenTabItemFragment();
    GoldenTabItemUsedFragment mFinanceUsedFragment = new GoldenTabItemUsedFragment();
    // GoldenTabItemFragment nowFragment =mFinanceHaveFragment;
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn, btnTQSY;
    // bbs交流 动态 摇一摇 邀请 评分 版本 建议 //关于理财时代
    private RelativeLayout relativeLiCaiJin, relativeTZJL, relativeTQSY;
    private TRJActivity mContext;
    private Dialog loading;
    public int mCurrentOrderP = 0;

    private ImageView imgBanner;
    private TextView tvLiCaiJin, tvDaiShou, tvTiQu;
    private PagerSlidingTabStripManageFinance tabs;
    MyAdapter adapter;
    ViewPager vPager;
    private Dialog tiquDialog;
    private DialogPopupWindow dialogPopupWindow;
    private View mainView;
    private Boolean toHomeflag = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_finance);
        mContext = this;
        hgfs = new HttpGoldFinanceService(this, this);
        hgfys = new HttpGoldFinanceYieldService(this, this);
        initView();
        loadData();
        // getBannerInformation();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        CONTENT = getResources().getStringArray(R.array.golden_title);
        flag = true;
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setVisibility(View.VISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("理财金");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setBackgroundColor(Color.TRANSPARENT);
        mSaveBtn.setPadding(0, 0, DensityUtil.dip2px(mContext, 10), 0);
        mSaveBtn.setText("规则");
        imgBanner = (ImageView) findViewById(R.id.imgBanner);
        btnTQSY = (Button) findViewById(R.id.btnTQSY);
        relativeLiCaiJin = (RelativeLayout) findViewById(R.id.relativeLiCaiJin);
        relativeTZJL = (RelativeLayout) findViewById(R.id.relativeTZJL);
        relativeTQSY = (RelativeLayout) findViewById(R.id.relativeTQSY);

        tvLiCaiJin = (TextView) findViewById(R.id.tvLiCaiJin);
        tvDaiShou = (TextView) findViewById(R.id.tvDaiShou);
        tvTiQu = (TextView) findViewById(R.id.tvTiQu);

        loading = createLoadingDialog(mContext, "正在加载", true);
        mainView = findViewById(R.id.ll_main);
        dialogPopupWindow = new DialogPopupWindow(this, mainView, null);
        relativeLiCaiJin.setOnClickListener(this);
        relativeTZJL.setOnClickListener(this);
        relativeTQSY.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        btnTQSY.setOnClickListener(this);
        imgBanner.setOnClickListener(this);
        adapter = new MyAdapter(getSupportFragmentManager());
        // new SetAdapterTask().execute();
        vPager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStripManageFinance) findViewById(R.id.tabs);
        if (adapter != null) {
            vPager.setAdapter(adapter);
            tabs.setViewPager(vPager, FinancialCashActivity.this);
        }
        Intent intent = getIntent();
        toHomeflag = intent.getBooleanExtra("flag", false);
        mFinanceHaveFragment.mCurrentPType = "0";
        mFinanceUsedFragment.mCurrentPType = "1";
    }

    @Override
    public void selectedPage(int args) {
        switch (args) {
            case 0:
                // mFinanceZCFragment.initData(0);
                mFinanceHaveFragment.ld(false);
                break;
            case 1:
                // mFinanceCZFragment.initData(1);
                mFinanceUsedFragment.reLoadDatas();
                break;

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
                newFragment = mFinanceHaveFragment;
            } else if (pos == 1) {
                newFragment = mFinanceUsedFragment;
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
    protected void onResume() {
        // mFinanceHaveFragment.ld(false);
        mFinanceHaveFragment.ld(false);
        // moarkreChoose(false);
        super.onResume();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //
            case R.id.btn_back:
                // FinancialCashActivity.this.finish();
                Intent intent1 = new Intent();
                intent1.setClass(FinancialCashActivity.this, MainActivity.class);
                if (!toHomeflag) {
                    MemorySave.MS.mIsGoAccount = true;

                } else {
                    MemorySave.MS.mIsGoHome = true;

                }
                startActivity(intent1);
                finish();
                break;
            case R.id.imgBanner:
            case R.id.btn_option:
                if (!NetUtils.isNetworkConnected(FinancialCashActivity.this)) {
                    ToastUtil.showToast(FinancialCashActivity.this, getResources()
                            .getString(R.string.tv_network));
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(FinancialCashActivity.this, MainWebActivity.class);
                intent.putExtra("title", "理财金");
                intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/tyjSqa");
                startActivity(intent);

                break;
            case R.id.btnTQSY:// 提取收益
                if (!NetUtils.isNetworkConnected(FinancialCashActivity.this)) {
                    ToastUtil.showToast(FinancialCashActivity.this, getResources()
                            .getString(R.string.tv_network));
                    return;
                }

                if (tvTiQu.getText().toString().equals("0.0")
                        || tvTiQu.getText().toString().equals("0.00")) {
                    String message = "暂无可提取的收益";
                    // dialogPopupWindow.showWithMessage(message, "0");
                    createDialogDismissAuto(message);
                    return;
                }
                loadDataTiQu();
                break;
            // 理财金
            case R.id.relativeLiCaiJin:
                Intent licaijin = new Intent();
                licaijin.putExtra("mTvTitle", "获取记录");
                licaijin.putExtra("sourceMoney", tvLiCaiJin.getText().toString());
                licaijin.putExtra("flagUrl", true);
                licaijin.setClass(mContext,
                        GoldFinanceRecordActivity.class);
                startActivity(licaijin);
                break;

            // 投资记录
            case R.id.relativeTZJL:
                Intent touzijilu = new Intent();
                touzijilu.putExtra("mTvTitle", "投资记录");
                touzijilu.putExtra("sourceMoney", tvDaiShou.getText().toString());
                touzijilu.putExtra("flagUrl", false);
                touzijilu.setClass(mContext, GoldFinanceRecordActivity.class);
                startActivity(touzijilu);
                break;

            // 可提取收益
            case R.id.relativeTQSY:

                break;

        }
    }

    private void loadDataTiQu() {
        hgfys.gainGoldFinanceYield();

    }

    private void loadData() {
        hgfs.gainGoldFinance();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent1 = new Intent();
            intent1.setClass(mContext, MainActivity.class);
            if (!toHomeflag) {
                // FinancialCashActivity.this.finish();
                MemorySave.MS.mIsGoAccount = true;
            } else {
                MemorySave.MS.mIsGoHome = true;

            }
            startActivity(intent1);
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void gainGoldFinanceYieldsuccess(GoldFinanceYieldJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                String message = response.getMessage();
                if (boolen.equals("1")) {
                    GoldFinanceYieldData data = response.getData();
                    if (null != data) {
                        String yield = data.getYield();//
                        createDialogDismissAuto(yield + "提取成功");
                        tvTiQu.setText("0.00");
                        btnTQSY.setBackgroundResource(R.drawable.feedback_submit_bg_gold);
                        btnTQSY.setEnabled(false);
                        mFinanceHaveFragment.ld(false);
                    } else {
                        if (StringUtils.isEmpty(message))
                            message = "返回数据为空，稍后重试";
                        createDialogDismissAuto(message);
                    }

                } else {
                    if (!StringUtils.isEmpty(message)) {
                        createDialogDismissAuto(message);
                    }
                }

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainGoldFinanceYieldfail() {
        ToastUtil.showToast(mContext, "网络不给力!");
    }

    @Override
    public void gainGoldFinancesuccess(GoldFinanceJson response) {
        String myTyj = "";
        String myYield = "";
        String bonus_stay_total = "";
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    GoldFinanceData data = response.getData();
                    myTyj = data.getMyTyj();//
                    myYield = data.getMyYield();//
                    bonus_stay_total = data.getBonus_stay_total();//

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!StringUtils.isEmpty(myTyj)) {
                tvLiCaiJin.setText(myTyj);
            }
            if (!StringUtils.isEmpty(myYield)) {
                tvDaiShou.setText(myYield);
            }
            if (!StringUtils.isEmpty(bonus_stay_total)) {
                tvTiQu.setText(bonus_stay_total);
            }

            if (!StringUtils.isEmpty(bonus_stay_total)
                    && bonus_stay_total.equals("0.00")) {
                btnTQSY.setBackgroundResource(R.drawable.feedback_submit_bg_gold);
                btnTQSY.setEnabled(false);
            } else {
                btnTQSY.setBackgroundResource(R.drawable.bg_button_yellow);
            }

        }
    }

    @Override
    public void gainGoldFinancefail() {
        ToastUtil.showToast(mContext, "网络不给力!");
    }
}
