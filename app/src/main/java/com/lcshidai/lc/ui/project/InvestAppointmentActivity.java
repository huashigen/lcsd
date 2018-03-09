package com.lcshidai.lc.ui.project;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.licai.AddToBookingOrderImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.licai.FundDetailInfo;
import com.lcshidai.lc.service.licai.HttpAddBookingOrderService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvestAppointmentActivity extends TRJActivity implements AddToBookingOrderImpl {

    @Bind(R.id.ib_top_bar_back)
    ImageButton topBackBtn;
    @Bind(R.id.tv_top_bar_title)
    TextView topTitleText;
    @Bind(R.id.tv_fund_name)
    TextView tvFundName;
    @Bind(R.id.tv_increase_amount_10)
    TextView tvIncreaseAmount10;
    @Bind(R.id.tv_increase_amount_100)
    TextView tvIncreaseAmount100;
    @Bind(R.id.iv_minus_sign)
    ImageView ivMinusSign;
    @Bind(R.id.et_appointment_amount)
    EditText etAppointmentAmount;
    @Bind(R.id.tv_plus_sign)
    ImageView tvPlusSign;
    @Bind(R.id.et_manager_phone)
    EditText etManagerPhone;
    @Bind(R.id.et_investor_remark)
    EditText etInvestorRemark;
    @Bind(R.id.tv_submit_appointment)
    TextView tvSubmitAppointment;

    private String fund_id = "";

    private int min_buy_amount;// 认购起点
    private int increaseUnit = 100;// 增加或减少的单位
    private HttpAddBookingOrderService addBookingOrderService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_appointment);
        ButterKnife.bind(this);
        addBookingOrderService = new HttpAddBookingOrderService(this, this);
        initViewsAndEvents();
    }

    private void initViewsAndEvents() {

        int colorStart = Color.parseColor("#FFE31C");
        int colorEnd = Color.parseColor("#FF811C");
        Shader shader = new LinearGradient(0, 0, 0, 80, colorStart, colorEnd, Shader.TileMode.CLAMP);
        etAppointmentAmount.getPaint().setShader(shader);
        FundDetailInfo fundDetailInfo = (FundDetailInfo) getIntent().getSerializableExtra(Constants.FUND_DETAIL);
        fund_id = getIntent().getStringExtra(Constants.FUND_ID);
        if (null != fundDetailInfo) {
            tvFundName.setText(fundDetailInfo.getFund_name());
            etManagerPhone.setText(GoLoginUtil.getManagerPhone(this));
            try {
                min_buy_amount = Integer.valueOf(fundDetailInfo.getMin_buy_amount());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            etAppointmentAmount.setText(String.valueOf(min_buy_amount));
        }

        topTitleText.setText("投资预约");
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    public void onBackPressed() {
        ToastUtil.cancelToast();
        super.onBackPressed();
    }

    @OnClick({R.id.ib_top_bar_back, R.id.tv_increase_amount_10, R.id.tv_increase_amount_100, R.id.iv_minus_sign,
            R.id.tv_plus_sign, R.id.tv_submit_appointment})
    public void onClick(View view) {
        int amount = 0;
        String amountStr = etAppointmentAmount.getText().toString();
        if (!CommonUtil.isNullOrEmpty(amountStr)) {
            amount = Integer.valueOf(amountStr);
        }
        switch (view.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;
            case R.id.tv_increase_amount_10:
                increaseUnit = 10;
                tvIncreaseAmount10.setTextColor(getResources().getColor(R.color.white));
                tvIncreaseAmount100.setTextColor(getResources().getColor(R.color.black));
                tvIncreaseAmount10.setBackgroundResource(R.drawable.bg_plus_100);
                tvIncreaseAmount100.setBackgroundResource(R.drawable.bg_plus_10);
                break;
            case R.id.tv_increase_amount_100:
                increaseUnit = 100;
                tvIncreaseAmount10.setTextColor(getResources().getColor(R.color.black));
                tvIncreaseAmount100.setTextColor(getResources().getColor(R.color.white));
                tvIncreaseAmount10.setBackgroundResource(R.drawable.bg_plus_10);
                tvIncreaseAmount100.setBackgroundResource(R.drawable.bg_plus_100);
                break;
            case R.id.iv_minus_sign:
                if (amount - increaseUnit >= min_buy_amount) {
                    etAppointmentAmount.setText(String.valueOf(amount - increaseUnit));
                } else {
                    ToastUtil.showShortToast(mContext, String.format("投资金额不能低于认购起点：%d万！", min_buy_amount));
                }
                break;
            case R.id.tv_plus_sign:
                etAppointmentAmount.setText(String.valueOf(amount + increaseUnit));
                break;
            case R.id.tv_submit_appointment:
                if (amount >= min_buy_amount) {
                    addBookingOrderService.addToBookingOrder(GoLoginUtil.getAccessToken(this),
                            GoLoginUtil.getUserToken(this), fund_id, String.valueOf(amount * 10000), etInvestorRemark.getText().toString());
                    showLoadingDialog(mContext, "正在提交", true);
                } else {
                    ToastUtil.showShortToast(mContext, String.format("投资金额不能低于认购起点：%d万！", min_buy_amount));
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void addToBookingOrderSuccess(BaseJson response) {
        hideLoadingDialog();
        showToast(response.getMessage());
        finish();
    }

    @Override
    public void addToBookingOrderFailed(BaseJson errorResponse) {
        hideLoadingDialog();
        showToast(errorResponse.getMessage());
    }
}
