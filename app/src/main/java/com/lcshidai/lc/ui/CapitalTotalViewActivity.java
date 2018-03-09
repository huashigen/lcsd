package com.lcshidai.lc.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.model.account.AccountData;
import com.lcshidai.lc.ui.account.AccountActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;

/**
 * 资金总览页面
 * Created by RandyZhang on 16/7/7.
 */
public class CapitalTotalViewActivity extends TRJActivity implements View.OnClickListener {

    // 净资产
    private RelativeLayout rlNetAssetsLabelContainer;
    private TextView tvNetAssetsNum;
    private TextView tvUnderInvest;
    private TextView tvAvailableBalance;
    private TextView tvUnderBankApply;
    private TextView tvBankReturn;

    private TextView tvInstructionTotal, tvIntructionNetAssets, tvIntructionToCollectProfit;
    // 累计收益
    private TextView tvAccumulatedIncomeNum;
    private TextView tvAccumulatedCollectedInterest;
    private TextView tvAccumulatedToCollectInterest;
    private TextView tvCollectedReward;
    private TextView tvExperienceCash;

    private Dialog underBankApplyDialog = null;
    private Dialog bankReturnDialog = null;
    private String openEscrowUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital_total_view);
        openEscrowUrl = getIntent().getStringExtra("openEscrowUrl");
        AccountData accountData = (AccountData) getIntent().getSerializableExtra(AccountActivity.ACCOUNT_DATA);

        initViews(accountData);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initViews(AccountData accountData) {

        ImageButton topBackBtn = (ImageButton) findViewById(R.id.ib_top_bar_back);
        TextView topTitleText = (TextView) findViewById(R.id.tv_top_bar_title);
        rlNetAssetsLabelContainer = (RelativeLayout) findViewById(R.id.rl_net_assets_label_container);
        tvNetAssetsNum = (TextView) findViewById(R.id.tv_net_assets_num);
        tvUnderInvest = (TextView) findViewById(R.id.tv_under_invest);
        tvAvailableBalance = (TextView) findViewById(R.id.tv_available_balance);

        tvInstructionTotal = (TextView) findViewById(R.id.tv_instruction_total);
        tvIntructionNetAssets = (TextView) findViewById(R.id.tv_instruction_net_asserts);
        tvIntructionToCollectProfit = (TextView) findViewById(R.id.tv_instruction_to_collect_profit);

//        RelativeLayout rlUnderBankApplyContainer = (RelativeLayout) findViewById(R.id.rl_under_bank_apply_container);
        tvUnderBankApply = (TextView) findViewById(R.id.tv_under_bank_apply);
        tvAccumulatedIncomeNum = (TextView) findViewById(R.id.tv_accumulated_income_num);
//        RelativeLayout rlBankReturnApplyContainer = (RelativeLayout) findViewById(R.id.rl_bank_return_container);
        tvBankReturn = (TextView) findViewById(R.id.tv_bank_return);
        tvAccumulatedCollectedInterest = (TextView) findViewById(R.id.tv_accumulated_collected_interest);
        tvAccumulatedToCollectInterest = (TextView) findViewById(R.id.tv_accumulated_to_collect_interest);
        tvCollectedReward = (TextView) findViewById(R.id.tv_collected_reward);

        topBackBtn.setOnClickListener(this);
        rlNetAssetsLabelContainer.setOnClickListener(this);
//        rlUnderBankApplyContainer.setOnClickListener(this);
//        rlBankReturnApplyContainer.setOnClickListener(this);
        topTitleText.setText("资金总览");
        if (accountData != null) {
            // 净资产
            tvNetAssetsNum.setText(accountData.getTotalAccountView() + "元");
            // 投资中
            tvUnderInvest.setText(accountData.getMoney_invest() + "元");
            // 可用余额
            tvAvailableBalance.setText(accountData.getAmount_view() + "元");
            // 银行处理中
            if (!CommonUtil.isNullOrEmpty(accountData.getPendingAmountView())) {
                String pendingAmountView = accountData.getPendingAmountView().replace(",", "");
                if (Float.parseFloat(pendingAmountView) > 0) {
//                    rlUnderBankApplyContainer.setVisibility(View.VISIBLE);
                    tvUnderBankApply.setText(accountData.getPendingAmountView() + "元");
                } else {
//                    rlUnderBankApplyContainer.setVisibility(View.GONE);
                }
            }
            if (!CommonUtil.isNullOrEmpty(accountData.getRepayFreezeMoneyView())) {
                String repayFreezeMoneyView = accountData.getRepayFreezeMoneyView().replace(",", "");
                if (Float.parseFloat(repayFreezeMoneyView) > 0) {
//                    rlBankReturnApplyContainer.setVisibility(View.VISIBLE);
                    tvBankReturn.setText(accountData.getRepayFreezeMoneyView() + "元");
                } else {
//                    rlBankReturnApplyContainer.setVisibility(View.GONE);
                }
            }
            tvInstructionTotal.setText(accountData.getTotalPropertyView());
            tvIntructionNetAssets.setText(accountData.getTotalAccountView());
            tvIntructionToCollectProfit.setText(accountData.getWill_profit_view());

            // 预计总收益
            tvAccumulatedIncomeNum.setText(accountData.getTotal_profit() + "元");
            // 已赚收益
            tvAccumulatedCollectedInterest.setText(accountData.getProfit_view() + "元");
            // 预期带收益
            tvAccumulatedToCollectInterest.setText(accountData.getWill_profit_view() + "元");
            // 已收奖励（券／红包）
            tvCollectedReward.setText(accountData.getReward_sum() + "元");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                //
                onBackPressed();
                break;
//            暂无银行存管
//            case R.id.rl_net_assets_label_container:
//                // 跳转到存管页面
//                Intent intent = new Intent(this, CunGuanAccountActivity.class);
//                intent.putExtra("openEscrowUrl", openEscrowUrl);
//                startActivity(intent);
//                break;
//            case R.id.rl_under_bank_apply_container:
//                if (underBankApplyDialog == null) {
//                    initBankApplyDialog();
//                    underBankApplyDialog.show();
//                } else {
//                    underBankApplyDialog.show();
//                }
//                break;
//            case R.id.rl_bank_return_container:
//                if (bankReturnDialog == null) {
//                    initBankReturnDialog();
//                    bankReturnDialog.show();
//                } else {
//                    bankReturnDialog.show();
//                }
//                break;
            default:

                break;
        }
    }

    /**
     * 初始化处理中弹出框
     */
    private void initBankApplyDialog() {
        underBankApplyDialog = new Dialog(this, R.style.style_loading_dialog);// 创建自定义样式dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_under_bank_apply, null);// 得到加载view
        TextView textView = (TextView) view.findViewById(R.id.tv_content);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        String introduction = textView.getText().toString();
        ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.GREEN);
        int start = introduction.indexOf("T");
        int end = introduction.indexOf("1") + 1;
        builder.setSpan(greenSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                underBankApplyDialog.dismiss();
            }
        });// 设置加载信息
        underBankApplyDialog.setContentView(view);
        underBankApplyDialog.setCancelable(true);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = underBankApplyDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.9); //设置宽度
        underBankApplyDialog.getWindow().setAttributes(lp);
    }

    /**
     * 银行处理中(还款)
     */
    private void initBankReturnDialog() {
        bankReturnDialog = new Dialog(this, R.style.style_loading_dialog);// 创建自定义样式dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_bank_return, null);// 得到加载view
        TextView textView = (TextView) view.findViewById(R.id.tv_content);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        String introduction = textView.getText().toString();
        ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.GREEN);
        int start = introduction.indexOf("T");
        int end = introduction.indexOf("1") + 1;
        builder.setSpan(greenSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankReturnDialog.dismiss();
            }
        });// 设置加载信息
        bankReturnDialog.setContentView(view);
        bankReturnDialog.setCancelable(true);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = bankReturnDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.9); //设置宽度
        bankReturnDialog.getWindow().setAttributes(lp);
    }
}
