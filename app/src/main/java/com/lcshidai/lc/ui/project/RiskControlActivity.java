package com.lcshidai.lc.ui.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.model.licai.FundDetailInfo;
import com.lcshidai.lc.ui.PublicImageViewActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RiskControlActivity extends TRJActivity implements View.OnClickListener {

    @Bind(R.id.ib_top_bar_back)
    ImageButton topBackBtn;
    @Bind(R.id.tv_top_bar_title)
    TextView topTitleText;
    @Bind(R.id.tv_risk_control)
    TextView tvRiskControl;
    @Bind(R.id.iv_risk_control)
    ImageView ivRiskControl;
    private FundDetailInfo fundDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_control);
        ButterKnife.bind(this);

        initViewsAndEvents();
    }

    private void initViewsAndEvents() {
        fundDetailInfo = (FundDetailInfo) getIntent().getSerializableExtra(Constants.FUND_DETAIL);

        topBackBtn.setOnClickListener(this);
        topTitleText.setText("风险控制");
        if (null != fundDetailInfo) {
            if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getFund_manage_attach())) {
                ivRiskControl.setVisibility(View.VISIBLE);
                try {
                    Glide.with(mContext).load(fundDetailInfo.getFund_manage_attach()).into(ivRiskControl);
                } catch (Exception e) {
                    e.printStackTrace();
                    ivRiskControl.setVisibility(View.GONE);
                }
            } else {
                ivRiskControl.setVisibility(View.GONE);
            }
            if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getInvest_risk_ctrl())) {
                tvRiskControl.setText(fundDetailInfo.getInvest_risk_ctrl());
                tvRiskControl.setVisibility(View.VISIBLE);
            } else {
                tvRiskControl.setVisibility(View.GONE);
            }
        }

        ivRiskControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PublicImageViewActivity.class);
                intent.putExtra("IMG_URL", fundDetailInfo.getFund_manage_attach());
                startActivity(intent);
            }
        });
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
