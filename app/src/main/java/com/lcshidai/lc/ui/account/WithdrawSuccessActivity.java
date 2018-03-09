package com.lcshidai.lc.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.AdsImpl;
import com.lcshidai.lc.model.AdsData;
import com.lcshidai.lc.model.AdsJson;
import com.lcshidai.lc.service.HttpAdsService;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.StringUtils;

/**
 * 提现成功界面
 *
 * @author Administrator
 */
public class WithdrawSuccessActivity extends TRJActivity implements OnClickListener, AdsImpl {

    private ImageView mBackImg;
    private TextView mOption, mSubmit, mTime;
    private RelativeLayout rlAccountInsuranceTipContainer;

    private String remark = "", cashoutId = "";

    private ImageView iv_ads;
    private HttpAdsService mHttpAdsService;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        remark = getIntent().getStringExtra("remark");
        cashoutId = getIntent().getStringExtra("cashoutId");
        initView();

        mHttpAdsService = new HttpAdsService(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHttpAdsService.getAdsByUid();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    public void initView() {
        setContentView(R.layout.activity_withdraw_success);

        iv_ads = (ImageView) findViewById(R.id.iv_ad);

        rlAccountInsuranceTipContainer = (RelativeLayout) findViewById(R.id.rl_account_insurance_tip_container);
        mBackImg = (ImageView) findViewById(R.id.withdraw_succ_back);
        mOption = (TextView) findViewById(R.id.withdraw_succ_option);
        mSubmit = (TextView) findViewById(R.id.withdraw_succ_submit);
        mTime = (TextView) findViewById(R.id.withdraw_succ_time);

        if (!StringUtils.isEmpty(remark)) {
            mTime.setText(remark);
        }

        mBackImg.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        rlAccountInsuranceTipContainer.setOnClickListener(this);
        dealWithAccountInsurance();
    }

    /**
     * 处理账户资金安全险逻辑
     */
    private void dealWithAccountInsurance() {
        String accountInsuranceState = SpUtils.getString(SpUtils.Table.USER,
                SpUtils.User.IS_ACCOUNT_INSURANCE);
        if (!CommonUtil.isNullOrEmpty(accountInsuranceState)) {
            if ("0".equals(accountInsuranceState)) {
                //暂时没有此功能，暂时更改为gone
//                rlAccountInsuranceTipContainer.setVisibility(View.VISIBLE);
                rlAccountInsuranceTipContainer.setVisibility(View.GONE);
            } else {
                rlAccountInsuranceTipContainer.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.withdraw_succ_back:
            case R.id.withdraw_succ_submit:
                this.finish();
                break;
            case R.id.withdraw_succ_option:
                break;
            case R.id.rl_account_insurance_tip_container:
                String url = SpUtils.getString(SpUtils.Table.USER,
                        SpUtils.User.ACCOUNT_INSURANCE_URL);
                if (!CommonUtil.isNullOrEmpty(url)) {
                    Intent intent = new Intent(mContext, MainWebActivity.class);
                    intent.putExtra("title", "银行卡安全保障");
                    intent.putExtra("web_url", url);
                    startActivity(intent);
                }
            default:
                break;
        }
    }

    @Override
    public void getAdsSuccess(AdsJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                AdsData data = response.getData();
                if (data != null) {
                    final String id = data.getId();
                    final String image_url = data.getImg_url();
                    final String link_url = data.getLink_url();
                    if (!CommonUtil.isNullOrEmpty(image_url)) {
                        Glide.with(mContext).load(image_url).into(iv_ads);
                        iv_ads.setVisibility(View.VISIBLE);
                        iv_ads.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("web_url", link_url);
                                intent.putExtra("title", "");
                                if (link_url.contains("com.toubaojia.tbj") || link_url.contains("toubaojia.com")) {
                                    intent.putExtra("need_header", "0");
                                }
                                intent.setClass(mContext, MainWebActivity.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        iv_ads.setVisibility(View.GONE);
                    }
                } else {
                    iv_ads.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void getAdsFail() {

    }


}
