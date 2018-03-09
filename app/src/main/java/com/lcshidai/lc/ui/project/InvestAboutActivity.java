package com.lcshidai.lc.ui.project;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.model.licai.FundDetailInfo;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InvestAboutActivity extends TRJActivity implements View.OnClickListener {

    @Bind(R.id.tv_invest_scope)
    TextView tvInvestScope;
    @Bind(R.id.tv_invest_manage_team)
    TextView tvInvestManageTeam;
    @Bind(R.id.tv_invest_philosophy)
    TextView tvInvestPhilosophy;
    @Bind(R.id.tv_invest_target)
    TextView tvInvestTarget;
    @Bind(R.id.ll_invest_target_container)
    LinearLayout llInvestTargetContainer;
    @Bind(R.id.tv_issuer_introduce)
    TextView tvIssuerIntroduce;
    @Bind(R.id.ll_issuer_introduce_container)
    LinearLayout llIssuerIntroduceContainer;
    @Bind(R.id.ll_manage_team_container)
    LinearLayout llManageTeamContainer;
    @Bind(R.id.ll_invest_philosophy_container)
    LinearLayout llInvestPhilosophyContainer;
    @Bind(R.id.tv_fund_company)
    TextView tvFundCompany;
    @Bind(R.id.ll_fund_company_container)
    LinearLayout llFundCompanyContainer;
    @Bind(R.id.tv_fund_invest_at)
    TextView tvFundInvestAt;
    @Bind(R.id.ll_fund_invest_at_container)
    LinearLayout llFundInvestAtContainer;
    @Bind(R.id.tv_return_source_at)
    TextView tvReturnSourceAt;
    @Bind(R.id.ll_return_source_container)
    LinearLayout llReturnSourceContainer;
    @Bind(R.id.tv_added_intro)
    TextView tvAddedIntro;
    @Bind(R.id.ll_added_intro_container)
    LinearLayout llAddedIntroContainer;
    @Bind(R.id.ib_top_bar_back)
    ImageButton topBackBtn;
    @Bind(R.id.ll_invest_scope_container)
    LinearLayout llInvestScopeContainer;
    @Bind(R.id.tv_ltv_ratio)
    TextView tvLtvRatio;
    @Bind(R.id.ll_ltv_ratio_container)
    LinearLayout llLtvRatioContainer;
    @Bind(R.id.tv_ltv_body)
    TextView tvLtvBody;
    @Bind(R.id.ll_ltv_body_container)
    LinearLayout llLtvBodyContainer;

    private int fund_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_about);
        ButterKnife.bind(this);

        initViewsAndEvents();
    }

    private void initViewsAndEvents() {

        ImageButton btnBack = (ImageButton) findViewById(R.id.ib_top_bar_back);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        btnBack.setOnClickListener(this);

        tvTitle.setText("投资相关");
        FundDetailInfo fundDetailInfo = (FundDetailInfo) getIntent().getSerializableExtra(Constants.FUND_DETAIL);
        if (null != fundDetailInfo) {
            fund_type = Integer.valueOf(fundDetailInfo.getFund_type());
            tvInvestTarget.setText(fundDetailInfo.getInvest_target());
            tvIssuerIntroduce.setText(fundDetailInfo.getIssuer_description());
            tvInvestScope.setText(fundDetailInfo.getInvest_scope());
            tvInvestManageTeam.setText(fundDetailInfo.getManage_org());
            tvInvestPhilosophy.setText(fundDetailInfo.getInvest_philosophy());
            tvFundCompany.setText(fundDetailInfo.getFund_company());
            tvLtvRatio.setText(fundDetailInfo.getLtv_ratio());
            tvLtvBody.setText(fundDetailInfo.getLtv_body());
            tvFundInvestAt.setText(fundDetailInfo.getFund_invest_at());
            tvReturnSourceAt.setText(fundDetailInfo.getReturn_source());
            tvAddedIntro.setText(fundDetailInfo.getOther_comments());
            switch (fund_type) {
//                1：固收，2：证券基金，3：股权基金，4：信托产品，5：债权基金，6：资管计划
                case 1:
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getInvest_target()))
                        llInvestTargetContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getIssuer_description()))
                        llIssuerIntroduceContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getInvest_scope()))
                        llInvestScopeContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getManage_org()))
                        llManageTeamContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getInvest_philosophy()))
                        llInvestPhilosophyContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getFund_company()))
                        llFundCompanyContainer.setVisibility(View.VISIBLE);
                    llLtvRatioContainer.setVisibility(View.GONE);
                    llLtvBodyContainer.setVisibility(View.GONE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getFund_invest_at()))
                        llFundInvestAtContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getReturn_source()))
                        llReturnSourceContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getOther_comments()))
                        llAddedIntroContainer.setVisibility(View.GONE);
                    break;
                case 2:
                case 3:
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getInvest_target()))
                        llInvestTargetContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getIssuer_description()))
                        llIssuerIntroduceContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getInvest_scope()))
                        llInvestScopeContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getManage_org()))
                        llManageTeamContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getInvest_philosophy()))
                        llInvestPhilosophyContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getFund_company()))
                        llFundCompanyContainer.setVisibility(View.VISIBLE);
                    llLtvRatioContainer.setVisibility(View.GONE);
                    llLtvBodyContainer.setVisibility(View.GONE);
                    llFundInvestAtContainer.setVisibility(View.GONE);
                    llReturnSourceContainer.setVisibility(View.GONE);
                    llAddedIntroContainer.setVisibility(View.GONE);
                    break;
                case 4:
                case 5:
                case 6:
                    llInvestTargetContainer.setVisibility(View.GONE);
                    llIssuerIntroduceContainer.setVisibility(View.GONE);
                    llInvestScopeContainer.setVisibility(View.GONE);
                    llManageTeamContainer.setVisibility(View.GONE);
                    llInvestPhilosophyContainer.setVisibility(View.GONE);
                    llFundCompanyContainer.setVisibility(View.GONE);
                    llLtvRatioContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getLtv_body()))
                        llLtvBodyContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getFund_invest_at()))
                        llFundInvestAtContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getReturn_source()))
                        llReturnSourceContainer.setVisibility(View.VISIBLE);
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getOther_comments()))
                        llAddedIntroContainer.setVisibility(View.VISIBLE);
                    break;
            }
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
        }
    }
}
