package com.lcshidai.lc.ui.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.licai.IsShowCfyImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.licai.IsShowCfyJson;
import com.lcshidai.lc.service.licai.HttpIsShowCfyService;
import com.lcshidai.lc.ui.LoginActivity;
import com.lcshidai.lc.ui.base.AbsSubActivity;
import com.lcshidai.lc.ui.finance.FinanceFilterActivity;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.GoLoginUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 投资
 *
 * @author
 */
public class ProjectActivity extends AbsSubActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, IsShowCfyImpl {
    @Bind(R.id.btn_invest)
    RadioButton btnInvest;
    @Bind(R.id.btn_licai)
    RadioButton btnLicai;
    @Bind(R.id.radio_group_project)
    RadioGroup radioGroupProject;
    @Bind(R.id.tv_top_bar_title)
    TextView topTitleText;
    @Bind(R.id.tv_finance_filter)
    TextView tvFinanceFilter;
    @Bind(R.id.iv_fund_center)
    ImageView ivFundCenter;
    @Bind(R.id.fl_radio_group_container)
    FrameLayout flRadioGroupContainer;
    @Bind(R.id.fl_fragment)
    FrameLayout flFragment;
    @Bind(R.id.main)
    LinearLayout main;

    private Fragment mProInvestFragment = null;
    private Fragment mProLicaiFragment = null;
    private Fragment mCurrentFragment = null;

    private HttpIsShowCfyService isShowCfyService = null;

    private enum Type {
        Invest, Licai
    }

    private Type currentType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        ButterKnife.bind(this);
        isShowCfyService = new HttpIsShowCfyService(this, this);
        currentType = Type.Invest;
        initVariables();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentType == Type.Invest) {
            ivFundCenter.setVisibility(View.GONE);
        } else {
            ivFundCenter.setVisibility(View.VISIBLE);
        }
        isShowCfyService.isShowCfy();
    }

    private void initVariables() {
        mProInvestFragment = new ProInvestFragment();
        mProLicaiFragment = new ProLicaiFragmentNew();
    }

    private void initView() {
        radioGroupProject.setOnCheckedChangeListener(this);
        tvFinanceFilter.setOnClickListener(this);
        ivFundCenter.setOnClickListener(this);
        tvFinanceFilter.setText("筛选");
        topTitleText.setText("投资");
        radioGroupProject.check(R.id.btn_invest);
        switchFragment(currentType);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.btn_invest:
                currentType = Type.Invest;
                switchFragment(currentType);
//                tvFinanceFilter.setVisibility(View.VISIBLE);
                ivFundCenter.setVisibility(View.GONE);
                break;
            case R.id.btn_licai:
                currentType = Type.Licai;
                switchFragment(currentType);
//                tvFinanceFilter.setVisibility(View.GONE);
                ivFundCenter.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_finance_filter:
                intent = new Intent(this, FinanceFilterActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_fund_center:
                intent = new Intent(this, FundAccountActivity.class);
                String access_token = GoLoginUtil.getAccessToken(this);
                intent.putExtra(Constants.ACCESS_TOKEN_KEY, access_token);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void switchFragment(Type type) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        mCurrentFragment = fragmentManager.findFragmentById(R.id.fl_fragment);
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        mCurrentFragment = fragmentManager.findFragmentByTag(type.toString());
        switch (type) {
            case Invest:
                if (mCurrentFragment == null) {
                    transaction.add(R.id.fl_fragment, mProInvestFragment, type.toString()).commitAllowingStateLoss();
                } else {
                    transaction.show(mProInvestFragment).commitAllowingStateLoss();
                }
                break;
            case Licai:
                if (mCurrentFragment == null) {
                    transaction.add(R.id.fl_fragment, mProLicaiFragment, type.toString()).commitAllowingStateLoss();
                } else {
                    transaction.show(mProLicaiFragment).commitAllowingStateLoss();
                }
//                tvFinanceFilter.setVisibility(View.GONE);
                ivFundCenter.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE) {
            if (resultCode == LoginActivity.LOGIN_SUCCESS) {
            } else {
//				if (getIntent().getStringExtra("goClass") == null
//						|| getIntent().getStringExtra("goClass").equals(""))
//					finish();
                radioGroupProject.check(R.id.btn_invest);
                ((ProLicaiFragmentNew) mProLicaiFragment).setFlag(1);
            }
            getIntent().removeExtra("goClass");
        }
    }

    @Override
    public void isShowCfySuccess(IsShowCfyJson response) {
        if (null != response) {
            if (null != response.getData()) {
                if (response.getData().getCfy_show() == 1) {
                    // 显示cfy
                    radioGroupProject.setVisibility(View.VISIBLE);
                    topTitleText.setVisibility(View.GONE);
                } else {
                    // 不显示
                    radioGroupProject.setVisibility(View.GONE);
                    topTitleText.setVisibility(View.VISIBLE);
                }
                if (currentType == Type.Invest) {
//                    tvFinanceFilter.setVisibility(View.VISIBLE);
                }
                flFragment.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void isShowCfyFailed(BaseJson errorResponse) {
        // 不显示
        radioGroupProject.setVisibility(View.GONE);
//        tvFinanceFilter.setVisibility(View.VISIBLE);
        topTitleText.setVisibility(View.VISIBLE);
        flFragment.setVisibility(View.VISIBLE);
    }
}
