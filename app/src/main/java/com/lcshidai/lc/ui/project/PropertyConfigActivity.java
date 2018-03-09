package com.lcshidai.lc.ui.project;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.model.licai.FundLoginInfo;
import com.lcshidai.lc.model.licai.OrderAmountDetailBean;
import com.lcshidai.lc.ui.adapter.base.ListViewDataAdapter;
import com.lcshidai.lc.ui.adapter.base.ViewHolderBase;
import com.lcshidai.lc.ui.adapter.base.ViewHolderCreator;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PropertyConfigActivity extends TRJActivity {

    @Bind(R.id.ib_top_bar_back)
    ImageButton topBackBtn;
    @Bind(R.id.tv_top_bar_title)
    TextView topTitleText;
    @Bind(R.id.tv_under_invest_assets)
    TextView tvUnderInvestAssets;
    @Bind(R.id.lv_property_detail)
    ListView lvPropertyDetail;
    private FundLoginInfo fundLoginInfo;
    private List<OrderAmountDetailBean> orderAmountDetailBeanList;
    private ListViewDataAdapter<OrderAmountDetailBean> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_config);
        ButterKnife.bind(this);

        initViewsAndEvents();
    }

    private void initViewsAndEvents() {
        topTitleText.setText("资产配置");
        fundLoginInfo = (FundLoginInfo) getIntent().getSerializableExtra(Constants.FUND_LOGIN_INFO);
        if (null != fundLoginInfo) {
            orderAmountDetailBeanList = fundLoginInfo.getOrder_amount_detail();
            int colorStart = Color.parseColor("#FFE31C");
            int colorEnd = Color.parseColor("#FF811C");
            Shader shader = new LinearGradient(0, 0, 0, 80, colorStart, colorEnd, Shader.TileMode.CLAMP);
            tvUnderInvestAssets.getPaint().setShader(shader);
            tvUnderInvestAssets.setText(String.format("¥%s", fundLoginInfo.getOrder_amount_total()));
            mAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<OrderAmountDetailBean>() {
                @Override
                public ViewHolderBase<OrderAmountDetailBean> createViewHolder(int position) {
                    return new ViewHolderBase<OrderAmountDetailBean>() {

                        TextView tvLeftLabel;
                        TextView tvRightValue;
                        View vDivider;

                        @Override
                        public View createView(LayoutInflater layoutInflater) {
                            View convertView = layoutInflater.inflate(R.layout.poperty_config_list_item, null);
                            tvLeftLabel = (TextView) convertView.findViewById(R.id.tv_left_label);
                            tvRightValue = (TextView) convertView.findViewById(R.id.tv_right_value);
                            vDivider = convertView.findViewById(R.id.v_divider);
                            return convertView;
                        }

                        @Override
                        public void showData(int position, OrderAmountDetailBean itemData) {
                            if (null != itemData) {
                                tvLeftLabel.setText(itemData.getTitle());
                                tvRightValue.setText(String.format("%s万", String.format("¥%s", itemData.getAmount())));
                            }
                            if (position == orderAmountDetailBeanList.size() - 1) {
                                vDivider.setVisibility(View.GONE);
                            } else {
                                vDivider.setVisibility(View.VISIBLE);
                            }
                        }
                    };
                }
            });

            lvPropertyDetail.setAdapter(mAdapter);
            mAdapter.getDataList().addAll(orderAmountDetailBeanList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return true;
    }

    @OnClick(R.id.ib_top_bar_back)
    public void onClick() {
        onBackPressed();
    }
}
