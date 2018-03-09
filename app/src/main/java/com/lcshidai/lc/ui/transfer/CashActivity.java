package com.lcshidai.lc.ui.transfer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.transfer.CashCheckAddImpl;
import com.lcshidai.lc.impl.transfer.CashCheckImpl;
import com.lcshidai.lc.model.transfer.CashCheckAddData;
import com.lcshidai.lc.model.transfer.CashCheckAddJson;
import com.lcshidai.lc.model.transfer.CashCheckData;
import com.lcshidai.lc.model.transfer.CashCheckJson;
import com.lcshidai.lc.service.transfer.HttpCashCheckAddService;
import com.lcshidai.lc.service.transfer.HttpCashCheckService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.text.CustomEditTextRightUnit;

/**
 * 变现
 *
 * @author 000814
 */
public class CashActivity extends TRJActivity implements CashCheckImpl, CashCheckAddImpl {

    public String prj_name;
    public String prj_id;// : "项目ID",
    public String rate_tips;// : "请输入变现利率为6.28%-9.42%之间的值!",
    public String balance_rate;// : 平衡利率
    public String max_cash_money;// : 最大可变现
    public String prj_order_id;

    TextView tv_contentinfo, tv_rate_info;
    CustomEditTextRightUnit rateET;
    CustomEditTextRightUnit cuseEt;
    HttpCashCheckService hccs;
    HttpCashCheckAddService hccas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        hccs = new HttpCashCheckService(this, this);
        hccas = new HttpCashCheckAddService(this, this);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            prj_id = args.getString("prj_id");
            rate_tips = args.getString("rate_tips");
            balance_rate = args.getString("balance_rate");
            max_cash_money = args.getString("max_cash_money");
            prj_name = args.getString("prj_name");
            prj_order_id = args.getString("prj_order_id");
        }
        prj_name = prj_name == null ? "" : prj_name;
        setView();
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

    @Override
    protected void onResume() {
        super.onResume();
        MemorySave.MS.isBidSuccessBack = false;
        if (MemorySave.MS.mIsFinanceInfoFinish) {
            finish();
        } else if (MemorySave.MS.mIsGoFinanceRecord) {
            finish();
        }
    }

    private void setView() {
        findViewById(R.id.btn_option).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_subtitle).setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.tv_top_bar_title)).setText("变现借款");
        rateET = (CustomEditTextRightUnit) this.findViewById(R.id.cuse_rateunit);
        cuseEt = (CustomEditTextRightUnit) this.findViewById(R.id.cuse_cashunit);

        // tv_money_info=(TextView) findViewById(R.id.tv_money_info);
        tv_rate_info = (TextView) findViewById(R.id.tv_rate_info);
        tv_rate_info.setText(rate_tips);
        cuseEt.setUnit("元");
        rateET.setUnit("%");
        ((CustomEditTextRightUnit) this.findViewById(R.id.cuse_rateunit)).getET()
                .addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        cashCheckEt(prj_order_id, cuseEt.getEdtText(), rateET.getEdtText());
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                });
        findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cuseEt.getEdtText().equals("")) {
                    ToastUtil.showToast(CashActivity.this, "变现金额不能为空!");
                    return;
                }

                if (rateET.getEdtText().equals("")) {
                    ToastUtil.showToast(CashActivity.this, "变现利率不能为空!");
                    return;
                }

                String rateValue = rateET.getEdtText();
                if (!StringUtils.isFloat(rateValue)) {
                    ToastUtil.showToast(CashActivity.this, "请输入有效数值");
                    return;
                } else if (Float.parseFloat(rateValue) == 0) {
                    ToastUtil.showToast(CashActivity.this, "利率不能为零");
                    return;
                }

                cashCheck(prj_order_id, cuseEt.getEdtText(), rateET.getEdtText());
            }
        });
        String fir = "将您通过理财时代持有的资产（";
        String fend = "）作为权益保障发布借款，从而提前变现!";
        String str = fir + prj_name + fend;
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), fir.length(),
                prj_name.length() + fir.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) findViewById(R.id.tv_contentinfo)).setText(ss);// +item.ra
        // ((TextView)findViewById(R.id.tv_contentinfo)).setText("将您通过理财时代持有的资产（"+prj_name+"）作为权益保障发布借款，从而提前变现!");
        rateET.setText(balance_rate);
        // ((CustomEditTextRightUnit)this.findViewById(R.id.cuse_cashunit)).setText(max_cash_money);
        ((TextView) this.findViewById(R.id.tv_countchashmoney)).setText(max_cash_money);
        cuseEt.setText(max_cash_money);
    }

    /**
     * 变现check
     *
     * @param prj_order_id
     * 订单的ID
     * @param cash_money
     * 投资的金额
     * @param cash_rate
     * 利率
     */
    String mCash_rate;
    String mCash_money;

    private void cashCheck(String prj_order_id, final String cash_money, final String cash_rate) {

        mCash_rate = cash_rate;
        mCash_money = cash_money;
        hccas.gainCashCheckAddResult(prj_order_id, cash_money, cash_rate);

    }

    /**
     * 变现check
     *
     * @param prj_order_id 订单的ID
     * @param cash_money   投资的金额
     * @param cash_rate    利率
     */
    private void cashCheckEt(String prj_order_id, String cash_money, String cash_rate) {
        hccs.gainCashCheckResult(prj_order_id, cash_money, cash_rate);

    }

    @Override
    public void gainCashCheckSuccess(CashCheckJson response) {
        try {
            String boolen = response.getBoolen();
            if (boolen.equals("0")) {
                tv_rate_info.setTextColor(Color.RED);
            } else {
                CashCheckData jobj = response.getData();
                tv_rate_info.setTextColor(Color.rgb(0xae, 0xae, 0xae));// #AEAEAE
                ((TextView) CashActivity.this.findViewById(R.id.tv_countchashmoney)).setText(jobj.getMax_cash_money());
                cuseEt.setText(jobj.getMax_cash_money());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainCashCheckFail() {
        ToastUtil.showToast(CashActivity.this, "网络不给力!");
    }

    @Override
    public void gainCashCheckAddSuccess(CashCheckAddJson response) {
        try {

            String boolen = response.getBoolen();
            if (!boolen.equals("1")) {
                return;
            }
            CashCheckAddData jobj = response.getData();

            Intent intent = new Intent();
            intent.putExtra("CashCheckItem", jobj);
            intent.putExtra("cash_rate", mCash_rate);
            intent.putExtra("cash_money", mCash_money);
            intent.setClass(CashActivity.this, CashConfirmActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainCashCheckAddFail() {
        ToastUtil.showToast(CashActivity.this, "网络不给力!");
    }
}
