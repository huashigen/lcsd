package com.lcshidai.lc.ui.project;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.model.licai.FundDetailInfo;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddedIntroActivity extends TRJActivity {

    @Bind(R.id.ib_top_bar_back)
    ImageButton topBackBtn;
    @Bind(R.id.tv_top_bar_title)
    TextView topTitleText;
    @Bind(R.id.tv_added_intro)
    TextView tvAddedIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_intro);
        ButterKnife.bind(this);

        initViewsAndEvents();
    }

    private void initViewsAndEvents() {
        topTitleText.setText("补充说明");
        FundDetailInfo fundDetailInfo = (FundDetailInfo) getIntent().getSerializableExtra(Constants.FUND_DETAIL);
        if (null != fundDetailInfo) {
            if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getOther_comments()))
                tvAddedIntro.setText(fundDetailInfo.getOther_comments());
        }
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
