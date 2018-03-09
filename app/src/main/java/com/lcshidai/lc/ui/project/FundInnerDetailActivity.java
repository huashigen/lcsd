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
import butterknife.OnClick;

public class FundInnerDetailActivity extends TRJActivity {

    @Bind(R.id.ib_top_bar_back)
    ImageButton topBackBtn;
    @Bind(R.id.tv_top_bar_title)
    TextView topTitleText;
    @Bind(R.id.iv_fund_detail_img)
    ImageView ivFundDetailImg;
    @Bind(R.id.tv_fund_inner_detail)
    TextView tvFund_inner_detail;

    FundDetailInfo fundDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_inner_detail);
        ButterKnife.bind(this);

        initViewsAndEvents();
    }

    private void initViewsAndEvents() {
        topTitleText.setText("项目详情");
        fundDetailInfo = (FundDetailInfo) getIntent().getSerializableExtra(Constants.FUND_DETAIL);
        if (null != fundDetailInfo) {
            if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getProject_detail())) {
                tvFund_inner_detail.setText(fundDetailInfo.getProject_detail());
                tvFund_inner_detail.setVisibility(View.VISIBLE);
            } else {
                tvFund_inner_detail.setVisibility(View.GONE);
            }
            if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getPro_detail_attach())) {
                ivFundDetailImg.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(fundDetailInfo.getPro_detail_attach()).into(ivFundDetailImg);
            } else {
                ivFundDetailImg.setVisibility(View.GONE);
            }
        }
        ivFundDetailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PublicImageViewActivity.class);
                intent.putExtra("IMG_URL", fundDetailInfo.getPro_detail_attach());
                startActivity(intent);
            }
        });
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @OnClick(R.id.ib_top_bar_back)
    public void onClick() {
        onBackPressed();
    }
}
