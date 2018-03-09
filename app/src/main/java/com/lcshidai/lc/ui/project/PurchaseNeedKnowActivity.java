package com.lcshidai.lc.ui.project;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.model.licai.FundDetailInfo;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 认购须知 Activity
 * Created by RandyZhang on 16/8/23.
 */
public class PurchaseNeedKnowActivity extends TRJActivity implements View.OnClickListener {

    @Bind(R.id.ib_top_bar_back)
    ImageButton topBackBtn;
    @Bind(R.id.tv_top_bar_title)
    TextView topTitleText;
    @Bind(R.id.tv_top_bar_right_action)
    TextView topActionText;
    @Bind(R.id.tv_purchase_start_point)
    TextView tvPurchaseStartPoint;
    @Bind(R.id.tv_purchase_increment)
    TextView tvPurchaseIncrement;
    @Bind(R.id.tv_open_date)
    TextView tvOpenDate;
    @Bind(R.id.tv_sub_fee_rule)
    TextView tvSubFeeRule;
    @Bind(R.id.tv_apply_fee_rule)
    TextView tvApplyFeeRule;
    @Bind(R.id.tv_manage_fee_rule)
    TextView tvManageFeeRule;
    @Bind(R.id.tv_redeem_fee_rule)
    TextView tvRedeemFeeRule;
    @Bind(R.id.tv_reward_rule)
    TextView tvRewardRule;
    @Bind(R.id.tv_other_instruction)
    TextView tvOtherInstruction;
    @Bind(R.id.tv_account_holder)
    TextView tvAccountHolder;
    @Bind(R.id.tv_bank_account)
    TextView tvBankAccount;
    @Bind(R.id.tv_bank)
    TextView tvBank;
    @Bind(R.id.tv_remark)
    TextView tvRemark;
    @Bind(R.id.tv_copy_account_info)
    TextView tvCopyAccountInfo;
    @Bind(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @Bind(R.id.rl_purchase_increment_container)
    RelativeLayout rlPurchaseIncrementContainer;
    @Bind(R.id.rl_purchase_start_point_container)
    RelativeLayout rlPurchaseStartPointContainer;
    @Bind(R.id.rl_open_date_container)
    RelativeLayout rlOpenDateContainer;
    @Bind(R.id.ll_purchase_start_point_container)
    LinearLayout llPurchaseStartPointContainer;
    @Bind(R.id.ll_sub_fee_rule_container)
    LinearLayout llSubFeeRuleContainer;
    @Bind(R.id.ll_apply_fee_rule_container)
    LinearLayout llApplyFeeRuleContainer;
    @Bind(R.id.ll_manage_fee_rule_container)
    LinearLayout llManageFeeRuleContainer;
    @Bind(R.id.ll_redeem_fee_rule_container)
    LinearLayout llRedeemFeeRuleContainer;
    @Bind(R.id.ll_reward_rule_container)
    LinearLayout llRewardRuleContainer;
    @Bind(R.id.ll_other_instruction_container)
    LinearLayout llOtherInstructionContainer;
    @Bind(R.id.ll_collect_account_container)
    LinearLayout llCollectAccountContainer;
    private int fund_type;
    private FundDetailInfo fundDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_need_know);
        ButterKnife.bind(this);

        initViewsAndEvents();
    }

    private void initViewsAndEvents() {
        fundDetailInfo = (FundDetailInfo) getIntent().getSerializableExtra(Constants.FUND_DETAIL);

        ImageButton btnBack = (ImageButton) findViewById(R.id.ib_top_bar_back);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        btnBack.setOnClickListener(this);
        tvCopyAccountInfo.setOnClickListener(this);

        tvTitle.setText(R.string.about_purchase);

        if (null != fundDetailInfo) {

            tvPurchaseStartPoint.setText(String.format("%s万", fundDetailInfo.getMin_buy_amount()));
            tvPurchaseIncrement.setText(String.format("%s万", fundDetailInfo.getStep_buy_amount()));
            tvOpenDate.setText(fundDetailInfo.getOpen_date());
            tvSubFeeRule.setText(fundDetailInfo.getSub_fee_rule());
            tvApplyFeeRule.setText(fundDetailInfo.getApply_fee_rule());
            tvManageFeeRule.setText(fundDetailInfo.getManage_fee_rule());
            tvRedeemFeeRule.setText(fundDetailInfo.getRedeem_fee_rule());
            tvRewardRule.setText(fundDetailInfo.getReward_rule());
            tvOtherInstruction.setText(fundDetailInfo.getOther_comments());
            tvAccountHolder.setText(String.format("%s%s", getString(R.string.account_name), fundDetailInfo.getAccount_holder()));
            tvBankAccount.setText(String.format("%s%s", getString(R.string.account_number), fundDetailInfo.getBank_account()));
            tvBank.setText(String.format("%s%s", getString(R.string.account_open_bank), fundDetailInfo.getKeep_bank()));
            tvRemark.setText(String.format("%s%s", getString(R.string.remark), fundDetailInfo.getRemark()));
            try {
                fund_type = Integer.valueOf(fundDetailInfo.getFund_type());
                switch (fund_type) {
                    case 1:
                    case 2:
                    case 3:
                        if (CommonUtil.isNullOrEmpty(fundDetailInfo.getMin_buy_amount())
                                && CommonUtil.isNullOrEmpty(fundDetailInfo.getStep_buy_amount())
                                && CommonUtil.isNullOrEmpty(fundDetailInfo.getOpen_date())) {
                            llPurchaseStartPointContainer.setVisibility(View.GONE);
                        } else {
                            llPurchaseStartPointContainer.setVisibility(View.VISIBLE);
                        }
                        if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getMin_buy_amount()))
                            rlPurchaseStartPointContainer.setVisibility(View.VISIBLE);
                        if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getStep_buy_amount()))
                            rlPurchaseIncrementContainer.setVisibility(View.VISIBLE);
                        if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getOpen_date()))
                            rlOpenDateContainer.setVisibility(View.VISIBLE);
                        if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getSub_fee_rule()))
                            llSubFeeRuleContainer.setVisibility(View.VISIBLE);
                        if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getApply_fee_rule()))
                            llApplyFeeRuleContainer.setVisibility(View.VISIBLE);
                        if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getManage_fee_rule()))
                            llManageFeeRuleContainer.setVisibility(View.VISIBLE);
                        if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getRedeem_fee_rule()))
                            llRedeemFeeRuleContainer.setVisibility(View.VISIBLE);
                        if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getReward_rule()))
                            llRewardRuleContainer.setVisibility(View.VISIBLE);
                        if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getOther_comments()))
                            llOtherInstructionContainer.setVisibility(View.VISIBLE);
                        llCollectAccountContainer.setVisibility(View.GONE);
                        break;
                    case 4:
                    case 5:
                    case 6:
                        if (CommonUtil.isNullOrEmpty(fundDetailInfo.getMin_buy_amount())
                                && CommonUtil.isNullOrEmpty(fundDetailInfo.getStep_buy_amount())
                                && CommonUtil.isNullOrEmpty(fundDetailInfo.getOpen_date())) {
                            llPurchaseStartPointContainer.setVisibility(View.GONE);
                        } else {
                            llPurchaseStartPointContainer.setVisibility(View.VISIBLE);
                        }
                        if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getMin_buy_amount()))
                            rlPurchaseStartPointContainer.setVisibility(View.VISIBLE);
                        if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getStep_buy_amount()))
                            rlPurchaseIncrementContainer.setVisibility(View.VISIBLE);
                        rlOpenDateContainer.setVisibility(View.GONE);
                        llSubFeeRuleContainer.setVisibility(View.GONE);
                        llApplyFeeRuleContainer.setVisibility(View.GONE);
                        llManageFeeRuleContainer.setVisibility(View.GONE);
                        llRedeemFeeRuleContainer.setVisibility(View.GONE);
                        llRewardRuleContainer.setVisibility(View.GONE);
                        llOtherInstructionContainer.setVisibility(View.GONE);
                        if (CommonUtil.isNullOrEmpty(fundDetailInfo.getAccount_holder())
                                && CommonUtil.isNullOrEmpty(fundDetailInfo.getBank_account())
                                && CommonUtil.isNullOrEmpty(fundDetailInfo.getKeep_bank())
                                && CommonUtil.isNullOrEmpty(fundDetailInfo.getRemark())) {
                            llCollectAccountContainer.setVisibility(View.GONE);
                        } else {
                            llCollectAccountContainer.setVisibility(View.VISIBLE);
                        }

                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
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
            case R.id.tv_copy_account_info:
                // 添加复制功能
                if (null != fundDetailInfo) {
                    tvAccountHolder.setText(String.format("%s%s", getString(R.string.account_name), fundDetailInfo.getAccount_holder()));
                    tvBankAccount.setText(String.format("%s%s", getString(R.string.account_number), fundDetailInfo.getBank_account()));
                    tvBank.setText(String.format("%s%s", getString(R.string.account_open_bank), fundDetailInfo.getKeep_bank()));
                    tvRemark.setText(String.format("%s%s", getString(R.string.remark), fundDetailInfo.getRemark()));
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(getString(R.string.account_name)).append(fundDetailInfo.getAccount_holder()).append("\n")
                            .append(getString(R.string.account_number)).append(fundDetailInfo.getBank_account()).append("\n")
                            .append(getString(R.string.account_open_bank)).append(fundDetailInfo.getKeep_bank()).append("\n")
                            .append(getString(R.string.remark)).append(fundDetailInfo.getRemark());
                    CommonUtil.copyText(stringBuilder.toString(), mContext);
                    ToastUtil.showToast(this, "账户信息已复制到剪贴板");
                }
                break;
            default:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
