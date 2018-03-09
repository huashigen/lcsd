package com.lcshidai.lc.ui.transfer;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.PIPWCallBack;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckData;
import com.lcshidai.lc.model.transfer.CashCheckAddData;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow;

/**
 * 变现确认
 *
 * @author 000814
 */
public class CashConfirmActivity extends TRJActivity implements PIPWCallBack {

    PayPasswordPopupWindow pw;

    CashCheckAddData cashItem;

    TextView tv_sdt_show_prj_name, tv_cash_rate, tv_curr_cash_money, tv_repay_way_name, tv_payable_interest,
            tv_service_fee, tv_real_into_account_money, tv_real_income, tv_sdt_left_time_money, tv_cash_money;
    String cash_rate, cash_money;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            cashItem = (CashCheckAddData) args.get("CashCheckItem");
            cash_rate = args.getString("cash_rate");
            cash_money = args.getString("cash_money");
        }

        pw = new PayPasswordPopupWindow(CashConfirmActivity.this, this);
        setContentView(R.layout.activity_cash_confirm);
        findViewById(R.id.btn_option).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_subtitle).setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.tv_top_bar_title)).setText("变现借款确认");
        findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pw.goAnimCash(findViewById(R.id.main), cashItem.prj_order_id, cashItem.prj_id,
                        cashItem.sdt_prj_name, cashItem.repay_way_name, cash_money, cash_rate,
                        cashItem.protocol_name, cashItem.protocol_url);
            }
        });
        tv_sdt_show_prj_name = (TextView) findViewById(R.id.tv_sdt_show_prj_name);
        tv_cash_rate = (TextView) findViewById(R.id.tv_cash_rate);
        tv_curr_cash_money = (TextView) findViewById(R.id.tv_curr_cash_money);
        tv_repay_way_name = (TextView) findViewById(R.id.tv_repay_way_name);
        tv_payable_interest = (TextView) findViewById(R.id.tv_payable_interest);
        tv_service_fee = (TextView) findViewById(R.id.tv_service_fee);
        tv_real_into_account_money = (TextView) findViewById(R.id.tv_real_into_account_money);
        tv_real_income = (TextView) findViewById(R.id.tv_real_income);
        tv_sdt_left_time_money = (TextView) findViewById(R.id.tv_sdt_left_time_money);
        tv_cash_money = (TextView) findViewById(R.id.tv_cash_money);

        tv_sdt_show_prj_name.setText(cashItem.sdt_show_prj_name);
        tv_cash_rate.setText(cash_rate + "%");
        tv_curr_cash_money.setText(cashItem.curr_cash_money + "元");
        tv_repay_way_name.setText(cashItem.repay_way_name);
        tv_payable_interest.setText(cashItem.payable_interest + "元");
        tv_service_fee.setText(cashItem.service_fee + "元");
        tv_real_into_account_money.setText(cashItem.real_into_account_money + "元");
        tv_real_income.setText(cashItem.real_income + "元");
        tv_sdt_left_time_money.setText(cashItem.sdt_left_time_money + "元");
        tv_cash_money.setText(cash_money + "元");

        findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void get(String string) {

    }

    @Override
    public void callPayCheckBack(boolean success) {

    }

    @Override
    public void doInvestSuccess(boolean success) {
        finish();
    }

    @Override
    public void doInvestCheckSuccess(boolean success, FinanceInvestPBuyCheckData model) {

    }

}
