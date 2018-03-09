package com.lcshidai.lc.ui.project;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.licai.GetFundDetailInfoImpl;
import com.lcshidai.lc.model.licai.FundDetailInfo;
import com.lcshidai.lc.model.licai.FundDetailInfoJson;
import com.lcshidai.lc.model.licai.FundItemsBean;
import com.lcshidai.lc.model.licai.NetDataBean;
import com.lcshidai.lc.model.licai.Shsz300DataBean;
import com.lcshidai.lc.model.licai.YieldBean;
import com.lcshidai.lc.model.licai.YieldSubBean;
import com.lcshidai.lc.service.licai.HttpGetFundDetailInfoService;
import com.lcshidai.lc.ui.adapter.base.ListViewDataAdapter;
import com.lcshidai.lc.ui.adapter.base.ViewHolderBase;
import com.lcshidai.lc.ui.adapter.base.ViewHolderCreator;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.MyListView;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 基金理财详情页面
 */
public class FundProjectDetailActivity extends TRJActivity implements View.OnClickListener, OnChartGestureListener,
        OnChartValueSelectedListener, UniversalVideoView.VideoViewCallback, GetFundDetailInfoImpl {
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";

    @Bind(R.id.ib_top_bar_back)
    ImageButton topBackBtn;
    @Bind(R.id.tv_top_bar_title)
    TextView topTitleText;
    @Bind(R.id.tv_top_bar_right_action)
    TextView topActionText;
    @Bind(R.id.ll_head_container)
    LinearLayout llHeadContainer;
    @Bind(R.id.tv_head_value)
    TextView tvHeadValue;
    @Bind(R.id.tv_head_label)
    TextView tvHeadLabel;
    @Bind(R.id.tv_project_limit)
    TextView tvProjectLimit;// 产品期限
    @Bind(R.id.tv_project_limit_label)
    TextView tvProjectLimitLabel;
    @Bind(R.id.ll_product_limit_container)
    LinearLayout llProductLimitContainer;
    @Bind(R.id.tv_subscription_starting_point)
    TextView tvSubscriptionStartingPoint;// 认购起点
    @Bind(R.id.tv_subscription_starting_point_label)
    TextView tvSubscriptionStartingPointLabel;
    @Bind(R.id.ll_subscription_starting_point_container)
    LinearLayout llSubscriptionStartingPointContainer;
    @Bind(R.id.rl_about_purchase_container)
    RelativeLayout rlAboutPurchaseContainer;// 认购须知
    @Bind(R.id.tabs_container)
    LinearLayout tabsContainer;// 投资期限tabs容器（一个月、两个月等）
    @Bind(R.id.hsv_time_container)
    HorizontalScrollView hsvTimeContainer;
    @Bind(R.id.ll_purchase_item_container)
    LinearLayout llPurchaseItemContainer;//
    @Bind(R.id.lc_net_trend_chart)
    LineChart lcNetTrendChart;
    @Bind(R.id.ll_trend_chart_container)
    LinearLayout llTrendChartContainer;
    @Bind(R.id.lv_base_info)
    MyListView lvBaseInfo;
    @Bind(R.id.tv_fund_highlight)
    TextView tvFundHighlight;// 产品亮点
    @Bind(R.id.rl_project_detail_container)
    RelativeLayout rlProjectDetailContainer;
    @Bind(R.id.tv_manager_team)
    TextView tvManagerTeam;// 管理团队
    @Bind(R.id.rl_fund_manager_container)
    RelativeLayout rlFundManagerContainer;
    @Bind(R.id.rl_risk_control_container)
    RelativeLayout rlRiskControlContainer;
    @Bind(R.id.rl_public_show_material_container)
    RelativeLayout rlPublicShowMaterialContainer;
    @Bind(R.id.rl_invest_about_container)
    RelativeLayout rlInvestAboutContainer;
    @Bind(R.id.videoView)
    UniversalVideoView videoView;
    @Bind(R.id.media_controller)
    UniversalMediaController mediaController;
    @Bind(R.id.iv_video_preview_image)
    ImageView ivVideoPreviewImage;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.fl_video_container)
    FrameLayout flVideoContainer;
    @Bind(R.id.rl_video_label_container)
    RelativeLayout rlVideoLabelContainer;
    @Bind(R.id.tv_instruction)
    TextView tvInstruction;
    @Bind(R.id.sv_container)
    ScrollView svContainer;
    @Bind(R.id.tv_contact_financial_planner)
    TextView tvContactFinancialPlanner;
    @Bind(R.id.tv_appointment_invest)
    TextView tvAppointmentInvest;
    @Bind(R.id.ll_footer_container)
    LinearLayout llFooterContainer;
    @Bind(R.id.v_purchase_need_known_divider)
    View vPurchaseNeedKnownDivider;

    private int mSeekPosition;
    private boolean isFullscreen;
    private int cachedHeight;

    private String fund_id = "";
    private String jsonStr = "{\"status\":\"1\",\"boolen\":\"1\",\"message\":\"添加成功！\",\"data\":{\"info\":{\"fund_name\":\"覃小婧\",\"net_worth\":\"1.2232\",\"net_update_time\":\"2016-04-18\",\"time_limit\":\"3+2年\",\"min_buy_amount\":\"100\",\"sub_fee_rule\":\"融资方国企背景，实力雄厚资金安全，基金开设托管专户，资金由第三方进行托管，保证专款专用。多重还款保障，融资方应收账款信息、交易材料真实且披露透明。债务方还款实力强。多重还款来源给用户切实有力的安全保障。\",\"apply_fee_rule\":\"融资方国企背景，实力雄厚资金安全，基金开设托管专户，资金由第三方进行托管，保证专款专用。多重还款保障，融资方应收账款信息、交易材料真实且披露透明。债务方还款实力强。多重还款来源给用户切实有力的安全保障。\",\"manage_fee_rule\":\"基金管理费2%/年\",\"redeem_fee_rule\":\"每年3月，6月，9月最后一个工作日开放申购赎回，份额持有人持有基金份额不满180天的不得赎回。\",\"reward_rule\":\"收益低于20%，不提取业绩报酬；收益高于（20%）包含，提取收益部分20%\",\"account_holder\":\"中融国际信托有限公司\",\"bank_account\":\"1000101010\",\"keep_bank\":\"中国民生银行北京西客站支行\",\"remark\":\"xx认购中融-泽鑫1号xx万\",\"yield\":[{\"time_limit\":\"3个月\",\"yield_sub\":[{\"invest_from\":\"100\",\"invest_to\":\"300\",\"min_buy_amount\":\"100\",\"annual_rate\":\"8.00\",\"rake_back_rate\":\"0.8\"},{\"invest_from\":\"300\",\"invest_to\":\"500\",\"min_buy_amount\":\"100\",\"annual_rate\":\"8.50\",\"rake_back_rate\":\"0.9\"}]},{\"time_limit\":\"六个月\",\"yield_sub\":[{\"invest_from\":\"101\",\"invest_to\":\"301\",\"min_buy_amount\":\"100\",\"annual_rate\":\"8.00\",\"rake_back_rate\":\"0.8\"},{\"invest_from\":\"305\",\"invest_to\":\"505\",\"min_buy_amount\":\"100\",\"annual_rate\":\"8.00\",\"rake_back_rate\":\"0.9\"}]}],\"net_data\":[{\"date\":\"2016-08-29\",\"value\":\"0.8\"},{\"date\":\"2016-08-30\",\"value\":\"0.8\"},{\"date\":\"2016-09-01\",\"value\":\"0.8\"},{\"date\":\"2016-09-02\",\"value\":\"0.7\"},{\"date\":\"2016-09-03\",\"value\":\"0.9\"}],\"shsz300_data\":[{\"date\":\"2016-08-29\",\"value\":\"0.8\"},{\"date\":\"2016-08-30\",\"value\":\"0.9\"},{\"date\":\"2016-09-01\",\"value\":\"1.1\"},{\"date\":\"2016-09-02\",\"value\":\"1.2\"},{\"date\":\"2016-09-03\",\"value\":\"0.9\"}],\"fund_type\":\"1\",\"fund_type_str\":\"固收\",\"is_fixed\":\"1\",\"fund_status\":\"1\",\"fund_status_str\":\"募集中\",\"scale_amount\":\"50.00\",\"collect_start_time\":\"2016-06-13\",\"collect_end_time\":\"2017-02-13\",\"rate\":\"\",\"size_ratio\":\"小额畅打\",\"yield_type\":\"固定收益\",\"issue_org\":\"投融普华资产管理公司\",\"publish_channel\":\"\",\"pay_way\":\"1\",\"pay_way_str\":\"按季付息\",\"invest_district\":\"不限\",\"invest_field\":\"1\",\"invest_field_str\":\"工商企业类\",\"structuring_flag\":\"0\",\"manager\":\"投融普华资产管理公司\",\"fund_manager_xname\":\"齐伟、XX\",\"total_net_value\":\"1.2345\",\"step_buy_amount\":\"10\",\"closing_period\":\"12个月\",\"pre_close_period\":\"6个月\",\"build_time\":\"2016-02-23\",\"fund_highlight\":\"1.融资方国企背景，实力雄厚<br/>2.资金安全，基金开设托管专户，资金由第三方进行托管，保证专款专用。<br/>3.多重还款保障，融资方应收账款信息、交易材料真实且披露透明。债务方还款实力强。多重还款来源给用户切实有力的安全保障。\",\"project_detail\":\"xxx\",\"fund_manager\":[{\"xname\":\"李祖义\",\"work_year\":\"5年\",\"graduate_college\":\"牛津大学\",\"describes\":\"融资方国企背景，实力雄厚资金安全，基金开设托管专户，资金由第三方进行托管，保证专款专用。多重还款保障，融资方应收账款信息、交易材料真实且披露透明。债务方还款实力强。多重还款来源给用户切实有力的安全保障。\"},{\"xname\":\"张晓雅\",\"work_year\":\"2年\",\"graduate_college\":\"哈佛大学\",\"describes\":\"融资方国企背景，实力雄厚资金安全，基金开设托管专户，资金由第三方进行托管，保证专款专用。多重还款保障，融资方应收账款信息、交易材料真实且披露透明。债务方还款实力强。多重还款来源给用户切实有力的安全保障。\"}],\"invest_risk_ctrl\":\"预警线为0.85，止损线为0.8，融资方国企背景，实力雄厚资金安全，基金开设托管专户，资金由第三方进行托管，保证专款专用。多重还款保障，融资方应收账款信息、交易材料真实且披露透明。债务方还款实力强。多重还款来源给用户切实有力的安全保障。\",\"other_comments\":\"预警线为0.85，止损线为0.8，融资方国企背景，实力雄厚资金安全，基金开设托管专户，资金由第三方进行托管，保证专款专用。多重还款保障，融资方应收账款信息、交易材料真实且披露透明。债务方还款实力强。多重还款来源给用户切实有力的安全保障。\",\"invest_scope\":\"融资方国企背景，实力雄厚资金安全，基金开设托管专户，资金由第三方进行托管，保证专款专用。多重还款保障，融资方应收账款信息、交易材料真实且披露透明。债务方还款实力强。多重还款来源给用户切实有力的安全保障。\",\"issuer_description\":\"融资方国企背景，实力雄厚资金安全，基金开设托管专户，资金由第三方进行托管，保证专款专用。多重还款保障，融资方应收账款信息、交易材料真实且披露透明。债务方还款实力强。多重还款来源给用户切实有力的安全保障。\",\"invest_philosophy\":\"融资方国企背景，实力雄厚资金安全，基金开设托管专户，资金由第三方进行托管，保证专款专用。多重还款保障，融资方应收账款信息、交易材料真实且披露透明。债务方还款实力强。多重还款来源给用户切实有力的安全保障。\",\"ltv_ratio\":\"100\",\"ltv_body\":\"投融谱华互联网金融公司\",\"fund_invest_at\":\"用于远洋国际办公场所的开发和建设支出\",\"return_source\":\"1. 办公场地出租。<br/>2. 持有互联网金融业务创收。\",\"luyan_pic\":\"http://img5.imgtn.bdimg.com/it/u=1433906797,1905437080&fm=21&gp=0.jpg\",\"luyan_name\":\"http://www.iqiyi.com/w_19rskn1jpd.html\",\"attach\":[{\"name\":\"理财时代1号PPT\",\"url\":\"https://www.tourongjia.com/file/1.ppg\"},{\"name\":\"理财时代2号PPT\",\"url\":\"https://www.tourongjia.com/file/2.ppg\"}]},\"favor_flag\":\"1\"}}";

    // 趋势表格中日期对应的list是否被初始化Flag
    boolean dateListInitFlag = false;

    private HttpGetFundDetailInfoService getFundDetailInfoService = null;

    private List<String> dates = new ArrayList<>();// 记录走势图日期
    private List<Entry> netChartData = new ArrayList<>();// 记录净值曲线数据
    private List<Entry> shszChartData = new ArrayList<>();// 记录沪深300曲线数据
    private FundDetailInfo fundDetailInfo = null;
    private Dialog cfpDialog;
    private ListViewDataAdapter<FundItemsBean> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_project_detail);
        ButterKnife.bind(this);
        getFundDetailInfoService = new HttpGetFundDetailInfoService(this, this);
        initViewsAndEvents();
    }

    /**
     * 初始化视图和事件
     */
    private void initViewsAndEvents() {
        fund_id = getIntent().getStringExtra(Constants.FUND_ID);
        adapter = new ListViewDataAdapter<FundItemsBean>(new ViewHolderCreator<FundItemsBean>() {
            @Override
            public ViewHolderBase<FundItemsBean> createViewHolder(int position) {
                return new ViewHolderBase<FundItemsBean>() {
                    RelativeLayout rlBaseInfoItemContainer;
                    TextView tvLabel;
                    TextView tvValue;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View convertView = layoutInflater.inflate(R.layout.base_info_list_item, null);
                        rlBaseInfoItemContainer = (RelativeLayout) convertView.findViewById(R.id.rl_base_info_item_container);
                        tvLabel = (TextView) convertView.findViewById(R.id.tv_label);
                        tvValue = (TextView) convertView.findViewById(R.id.tv_value);
                        return convertView;
                    }

                    @Override
                    public void showData(int position, FundItemsBean itemData) {
                        if (null != itemData) {
                            if (position % 2 == 0) {
                                rlBaseInfoItemContainer.setBackgroundColor(getResources().getColor(R.color.white));
                            } else {
                                rlBaseInfoItemContainer.setBackgroundColor(getResources().getColor(R.color.fund_pro_detail_base_gray));
                            }
                            tvLabel.setText(itemData.getName());
                            tvValue.setText(itemData.getValue());
                        }
                    }
                };
            }
        });
        topBackBtn.setOnClickListener(this);
        rlAboutPurchaseContainer.setOnClickListener(this);
        rlProjectDetailContainer.setOnClickListener(this);
        rlFundManagerContainer.setOnClickListener(this);
        rlRiskControlContainer.setOnClickListener(this);
//        rlAddedIntroContainer.setOnClickListener(this);
        rlPublicShowMaterialContainer.setOnClickListener(this);
        rlInvestAboutContainer.setOnClickListener(this);
        tvContactFinancialPlanner.setOnClickListener(this);
        tvAppointmentInvest.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivPlay.setVisibility(View.VISIBLE);
                ivVideoPreviewImage.setVisibility(View.VISIBLE);
            }
        });
        videoView.setMediaController(mediaController);

        initLineChartView();

        getFundDetailInfoService.getFundDetailInfo(GoLoginUtil.getAccessToken(this),
                GoLoginUtil.getUserToken(this), fund_id);
        showLoadingDialog(mContext, "正在加载", false);
    }

    /**
     * 数据填充
     */
    @SuppressLint("DefaultLocale")
    private void initViewsWithData(FundDetailInfo fundDetailInfo) {
        if (null != fundDetailInfo) {
            topTitleText.setText(fundDetailInfo.getFund_name());
            tvProjectLimit.setText(fundDetailInfo.getTime_limit());//产品期限
            tvSubscriptionStartingPoint.setText(String.format("%s万", fundDetailInfo.getMin_buy_amount()));// 认购起点
//                产品类型，1：固收，2：证券基金，3：股权基金，4：信托产品，5：资管计划，6：债权基金
            String fundType = fundDetailInfo.getFund_type();
            switch (Integer.valueOf(fundType)) {
                case 1:
                    // 固收
                    try {
                        tvHeadValue.setText(fundDetailInfo.getRate());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        tvHeadValue.setText("数据格式错误");
                    }
                    tvHeadValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32.0f);
                    tvHeadLabel.setText("基准收益");
                    rlFundManagerContainer.setVisibility(View.GONE);
                    break;
                case 2:
                    // 证券基金
                    tvHeadValue.setText(fundDetailInfo.getNet_worth());
                    if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getNet_update_time())) {
                        tvHeadLabel.setText("最新净值" + "(" + fundDetailInfo.getNet_update_time() + ")");
                    } else {
                        tvHeadLabel.setText("最新净值");
                    }
                    tvHeadValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32.0f);
                    rlFundManagerContainer.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    // 股权基金
                    tvHeadValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f);
                    tvHeadValue.setText(fundDetailInfo.getFund_invest_at());
                    tvHeadLabel.setText("资金投向");
                    rlFundManagerContainer.setVisibility(View.GONE);
                    break;
                case 4:
                    // 信托产品
                    tvHeadValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f);
                    tvHeadValue.setText(fundDetailInfo.getInvest_field_str());
                    tvHeadLabel.setText("投资领域");
                    rlFundManagerContainer.setVisibility(View.GONE);
                    break;
                case 5:
                    // 债权基金
                    tvHeadValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f);
                    tvHeadValue.setText(fundDetailInfo.getItem_fund_type_str());
                    tvHeadLabel.setText("基金类型");
                    rlFundManagerContainer.setVisibility(View.GONE);
                    break;
                case 6:
                    // 资管计划
                    tvHeadValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f);
                    tvHeadValue.setText(fundDetailInfo.getInvest_field_str());
                    tvHeadLabel.setText("投资领域");
                    rlFundManagerContainer.setVisibility(View.GONE);
                    break;

            }
            // 只显示k-v pair中v不为空的
            adapter.getDataList().addAll(getUsefulFundItems(fundDetailInfo.getFund_items()));
            lvBaseInfo.setAdapter(adapter);
            if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getIssuer())) {
                tvInstruction.setText(String.format("销售机构：%s", fundDetailInfo.getIssuer()));
            } else {
                tvInstruction.setVisibility(View.GONE);
            }

            tvFundHighlight.setText(fundDetailInfo.getFund_highlight());
            tvManagerTeam.setText(fundDetailInfo.getFund_manager_xname());

            // 路演视频预览图片
            // 加入视频播放
            if (!CommonUtil.isNullOrEmpty(fundDetailInfo.getLuyan_thumb())) {
                Glide.with(mContext).load(fundDetailInfo.getLuyan_thumb()).into(ivVideoPreviewImage);
            }
            setVideoAreaSize(fundDetailInfo.getLuyan_name());
            videoView.setVideoViewCallback(this);

            List<YieldBean> yieldBeanList = fundDetailInfo.getYield();
            if (null != yieldBeanList && yieldBeanList.size() > 0) {
                dealWithYieldList(yieldBeanList);
            }
            List<NetDataBean> netDataBeanList = fundDetailInfo.getNet_data();
            List<Shsz300DataBean> shsz300DataBeenList = fundDetailInfo.getShsz300_data();
            if (netDataBeanList.size() > 0 && shsz300DataBeenList.size() > 0 && netDataBeanList.size() == shsz300DataBeenList.size()) {
                LineData data = getChartData(netDataBeanList, shsz300DataBeenList);
                lcNetTrendChart.setData(data);
                lcNetTrendChart.invalidate();
            }
//            if (netDataBeanList.size() == 0 && shsz300DataBeenList.size() == 0) {
//                llTrendChartContainer.setVisibility(View.GONE);
//            } else {
//                llTrendChartContainer.setVisibility(View.VISIBLE);
//            }
            llTrendChartContainer.setVisibility(View.GONE);
            // 项目详情页是否显示
            if (CommonUtil.isNullOrEmpty(fundDetailInfo.getProject_detail()) &&
                    CommonUtil.isNullOrEmpty(fundDetailInfo.getPro_detail_attach())) {
                rlProjectDetailContainer.setVisibility(View.GONE);
            } else {
                rlProjectDetailContainer.setVisibility(View.VISIBLE);
            }
            // 风险控制是否显示
            if (CommonUtil.isNullOrEmpty(fundDetailInfo.getInvest_risk_ctrl())
                    && CommonUtil.isNullOrEmpty(fundDetailInfo.getFund_manage_attach())) {
                rlRiskControlContainer.setVisibility(View.GONE);
            } else {
                rlRiskControlContainer.setVisibility(View.VISIBLE);
            }
            // 项目材料是否显示
            if (fundDetailInfo.getAttach().size() == 0) {
                rlPublicShowMaterialContainer.setVisibility(View.GONE);
            } else {
                rlPublicShowMaterialContainer.setVisibility(View.VISIBLE);
            }
            svContainer.scrollTo(0, 0);
            if (hsvTimeContainer.getVisibility() == View.VISIBLE) {
                vPurchaseNeedKnownDivider.setVisibility(View.GONE);
            } else {
                vPurchaseNeedKnownDivider.setVisibility(View.VISIBLE);
            }
            if (CommonUtil.isNullOrEmpty(fundDetailInfo.getLuyan_name())) {
                flVideoContainer.setVisibility(View.GONE);
                rlVideoLabelContainer.setVisibility(View.GONE);
            } else {
                flVideoContainer.setVisibility(View.VISIBLE);
                rlVideoLabelContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    private List<FundItemsBean> getUsefulFundItems(List<FundItemsBean> fundItemsBeanList) {
        List<FundItemsBean> list = new ArrayList<>();
        for (int i = 0; i < fundItemsBeanList.size(); i++) {
            FundItemsBean fundItemsBean = fundItemsBeanList.get(i);
            if (!CommonUtil.isNullOrEmpty(fundItemsBean.getValue())) {
                list.add(fundItemsBean);
            }
        }

        return list;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize(final String path) {
        videoView.post(new Runnable() {
            @Override
            public void run() {
                int width = flVideoContainer.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = flVideoContainer.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                flVideoContainer.setLayoutParams(videoLayoutParams);
                videoView.setVideoPath(path);
                videoView.requestFocus();
            }
        });
    }

    // 数据处理
    private void dealWithYieldList(List<YieldBean> yieldList) {
        LinearLayout.LayoutParams timeTabLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        timeTabLayoutParams.setMargins(DensityUtil.dip2px(mContext, 20), DensityUtil.dip2px(mContext, 5), 0, DensityUtil.dip2px(mContext, 5));
        tabsContainer.removeAllViews();
        if (yieldList.size() == 1) {
            hsvTimeContainer.setVisibility(View.GONE);
        } else {
            hsvTimeContainer.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < yieldList.size(); i++) {
            final YieldBean yieldData = yieldList.get(i);
            TextView timeTab = new TextView(mContext);
            timeTab.setBackgroundResource(R.drawable.time_limit_drawable_selected);
            timeTab.setPadding(45, 10, 45, 10);
            timeTab.setLayoutParams(timeTabLayoutParams);
            timeTab.setTextColor(getResources().getColor(R.color.black));
            if (!CommonUtil.isNullOrEmpty(yieldData.getTime_limit())) {
                timeTab.setText(yieldData.getTime_limit());
                timeTab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 点击时采用点击时的对应的数据
                        dataBind(yieldData.getYield_sub());
                        updateTabState(tabsContainer, v);
                    }
                });
                tabsContainer.addView(timeTab);
            }

        }
        // 初始化用列表的第一个数据
        if (yieldList.size() > 0) {
            updateTabState(tabsContainer, tabsContainer.getChildAt(0));
            List<YieldSubBean> yieldSubBeanList = yieldList.get(0).getYield_sub();
            dataBind(yieldSubBeanList);
        }
    }

    /**
     * 更新tab状态
     *
     * @param tabsContainer tab 容器
     * @param selectedTab   选择的tab
     */
    private void updateTabState(LinearLayout tabsContainer, View selectedTab) {
        for (int i = 0; i < tabsContainer.getChildCount(); i++) {
            TextView currentTab = (TextView) tabsContainer.getChildAt(i);
            if (currentTab == selectedTab) {
                currentTab.setTextColor(getResources().getColor(R.color.theme_color));
                currentTab.setBackgroundResource(R.drawable.time_limit_drawable_selected);
            } else {
                currentTab.setTextColor(getResources().getColor(R.color.bg_float_gray));
                currentTab.setBackgroundResource(R.drawable.time_limit_drawable_normal);
            }
        }
    }

    private void dataBind(List<YieldSubBean> yieldSubBeanList) {
        llPurchaseItemContainer.removeAllViews();
        for (int j = 0; j < yieldSubBeanList.size(); j++) {
            YieldSubBean yieldSubBean = yieldSubBeanList.get(j);
            String text;
            if (CommonUtil.isNullOrEmpty(yieldSubBean.getInvest_to())) {
                // 如果最大值为空yieldData.getInvestFrom
                text = String.format("%s%s%s", yieldSubBean.getInvest_from(), "万", " ≤ X ");
            } else {
                text = String.format("%s%s%s%s", yieldSubBean.getInvest_from(), "万", " ≤ X < " + yieldSubBean.getInvest_to(), "万");
            }

            LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.fund_detail_profit_des_item, llPurchaseItemContainer, false);
            TextView tvInvestAmount = (TextView) item.findViewById(R.id.tv_invest_amount);
            TextView tvPreProfit = (TextView) item.findViewById(R.id.tv_pre_profit);
            tvInvestAmount.setText(text);
            if (!CommonUtil.isNullOrEmpty(yieldSubBean.getAnnual_rate())) {
                tvPreProfit.setText(String.format("%s", yieldSubBean.getAnnual_rate()));
            } else {
                tvPreProfit.setText("");
            }
            llPurchaseItemContainer.addView(item);
        }
    }

    /**
     * 获取 chart数据
     *
     * @param netDataBeanList     累计净值
     * @param shsz300DataBeenList 沪深300
     * @return 表格数据
     */
    private LineData getChartData(List<NetDataBean> netDataBeanList, List<Shsz300DataBean> shsz300DataBeenList) {
        List<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet dataSetA, dataSetB;
        if (netDataBeanList != null) {
            for (int i = 0; i < netDataBeanList.size(); i++) {
                netChartData.add(new Entry(i, Float.valueOf(netDataBeanList.get(i).getValue())));
            }
        }
        if (shsz300DataBeenList != null) {
            for (int i = 0; i < netDataBeanList.size(); i++) {
                shszChartData.add(new Entry(i, Float.valueOf(shsz300DataBeenList.get(i).getValue())));
            }
        }

        if (lcNetTrendChart.getData() != null && lcNetTrendChart.getData().getDataSetCount() > 0) {
            if (netDataBeanList != null) {
                dataSetA = (LineDataSet) lcNetTrendChart.getData().getDataSetByIndex(0);
                dataSetA.setValues(netChartData);
            }
            if (shsz300DataBeenList != null) {
                dataSetB = (LineDataSet) lcNetTrendChart.getData().getDataSetByIndex(1);
                dataSetB.setValues(shszChartData);
            }
            lcNetTrendChart.getData().notifyDataChanged();
            lcNetTrendChart.notifyDataSetChanged();
        } else {
            if (null != netDataBeanList) {
                dataSetA = new LineDataSet(netChartData, "A");
                dataSetA.setColor(Color.rgb(255, 0, 0));
                dataSetA.setAxisDependency(YAxis.AxisDependency.LEFT);
                dataSets.add(dataSetA);
            }

            if (null != shsz300DataBeenList) {
                dataSetB = new LineDataSet(shszChartData, "B");
                dataSetB.setColor(Color.rgb(0, 0, 255));
                dataSetB.setAxisDependency(YAxis.AxisDependency.LEFT);
                dataSets.add(dataSetB);
            }
        }

        if (null != netDataBeanList) {
            if (!dateListInitFlag) {
                dateListInitFlag = true;
                for (int i = 0; i < netDataBeanList.size(); i++) {
                    dates.add(i, netDataBeanList.get(i).getDate());
                }
            }
        }
        if (null != shsz300DataBeenList) {
            if (!dateListInitFlag) {
                dateListInitFlag = true;
                for (int i = 0; i < shsz300DataBeenList.size(); i++) {
                    dates.add(i, netDataBeanList.get(i).getDate());
                }
            }
        }
        return new LineData(dataSets);
    }

    /**
     * 初始化图表控件
     */
    private void initLineChartView() {
        lcNetTrendChart.setOnChartGestureListener(this);
        lcNetTrendChart.setOnChartValueSelectedListener(this);
        lcNetTrendChart.setDrawGridBackground(false);

        lcNetTrendChart.setDescription("");
        lcNetTrendChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        lcNetTrendChart.setTouchEnabled(true);

        // enable scaling and dragging
        lcNetTrendChart.setDragEnabled(true);
        lcNetTrendChart.setScaleEnabled(true);
        lcNetTrendChart.setPinchZoom(true);

        XAxis xAxis = lcNetTrendChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0.f);

        xAxis.setAvoidFirstLastClipping(true);// 避免X轴标签超出边界
        xAxis.setLabelRotationAngle(30f);// 设置X轴标签旋转角度（顺时针）
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        lcNetTrendChart.setExtraBottomOffset(10);
        YAxis leftAxis = lcNetTrendChart.getAxisLeft();
        lcNetTrendChart.getAxisRight().setEnabled(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawTopYLabelEntry(true);// 设置是否显示Y轴最大的Label

        Legend legend = lcNetTrendChart.getLegend();
        List<LegendEntry> legendEntryList = new ArrayList<>();
        LegendEntry firstLegendEntry = new LegendEntry("累计净值", Legend.LegendForm.CIRCLE, 10f, 2f, null, Color.rgb(255, 0, 0));
        LegendEntry secondLegendEntry = new LegendEntry("沪深300", Legend.LegendForm.CIRCLE, 10f, 2f, null, Color.rgb(0, 0, 255));
        legendEntryList.add(firstLegendEntry);
        legendEntryList.add(secondLegendEntry);
        legend.setCustom(legendEntryList);
        legend.setXEntrySpace(20f);
        legend.setYEntrySpace(20f);

        MyMarkerView mv = new MyMarkerView(this, R.layout.chart_marker_view);
        mv.setChartView(lcNetTrendChart);
        lcNetTrendChart.setMarker(mv);
        lcNetTrendChart.setDrawMarkers(true);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null && videoView.isPlaying()) {
            mSeekPosition = videoView.getCurrentPosition();
            videoView.pause();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;
            case R.id.rl_about_purchase_container:
                intent = new Intent(mContext, PurchaseNeedKnowActivity.class);
                intent.putExtra(Constants.FUND_DETAIL, fundDetailInfo);
                startActivity(intent);
                break;
            case R.id.rl_project_detail_container:
                intent = new Intent(mContext, FundInnerDetailActivity.class);
                intent.putExtra(Constants.FUND_DETAIL, fundDetailInfo);
                startActivity(intent);
                break;
            case R.id.rl_risk_control_container:
                intent = new Intent(mContext, RiskControlActivity.class);
                intent.putExtra(Constants.FUND_DETAIL, fundDetailInfo);
                startActivity(intent);
                break;
            case R.id.rl_public_show_material_container:
                intent = new Intent(mContext, PublicShowMaterialActivity.class);
                intent.putExtra(Constants.FUND_DETAIL, fundDetailInfo);
                startActivity(intent);
                break;
            case R.id.rl_fund_manager_container:
                intent = new Intent(mContext, FundManagerActivity.class);
                intent.putExtra(Constants.FUND_DETAIL, fundDetailInfo);
                startActivity(intent);
                break;
            case R.id.rl_invest_about_container:
                intent = new Intent(mContext, InvestAboutActivity.class);
                intent.putExtra(Constants.FUND_DETAIL, fundDetailInfo);
                startActivity(intent);
                break;
            case R.id.iv_play:
                if (mSeekPosition > 0) {
                    videoView.seekTo(mSeekPosition);
                }
                videoView.start();
                mediaController.setTitle("路演视频");
                ivPlay.setVisibility(View.GONE);
                ivVideoPreviewImage.setVisibility(View.GONE);
                break;
            case R.id.tv_contact_financial_planner:
                if (null == cfpDialog) {
                    initCfpDialog();
                    cfpDialog.show();
                } else {
                    cfpDialog.show();
                }
                break;
            case R.id.tv_appointment_invest:
                String fundStatus = fundDetailInfo.getFund_status();
                if (fundStatus.equals("1") || fundStatus.equals("2") || fundStatus.equals("3") || fundStatus.equals("4")) {
                    intent = new Intent(mContext, InvestAppointmentActivity.class);
                    intent.putExtra(Constants.FUND_DETAIL, fundDetailInfo);
                    intent.putExtra(Constants.FUND_ID, fund_id);
                    startActivity(intent);
                } else {
                    ToastUtil.showLongToast(mContext, "该产品募集期已结束");
                }
                break;
        }
    }

    /**
     * 初始化联系我的理财师弹窗
     */
    private void initCfpDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_contact_cfp, null);
        ImageView ivImgCfp = (ImageView) view.findViewById(R.id.iv_img_cfp);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        TextView tvNotContactNow = (TextView) view.findViewById(R.id.tv_not_contact_now);
        TextView tvContactNow = (TextView) view.findViewById(R.id.tv_contact_now);
        int width = (int) (DensityUtil.getScreenWidth(mContext) * 0.8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width / 2);
        ivImgCfp.setLayoutParams(params);

        tvContent.setText(String.format("您的专属理财师：%s", GoLoginUtil.getManagerName((TRJActivity) mContext)));

        tvNotContactNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfpDialog.dismiss();
            }
        });

        tvContactNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +
                        GoLoginUtil.getManagerPhone((TRJActivity) mContext)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                cfpDialog.dismiss();
            }
        });

        cfpDialog = new Dialog(this, R.style.style_loading_dialog);
        cfpDialog.setContentView(view);
        cfpDialog.setCancelable(true);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = cfpDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.8); //设置宽度
        cfpDialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onBackPressed() {
        if (isFullscreen) {
            videoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = flVideoContainer.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            flVideoContainer.setLayoutParams(layoutParams);

        } else {
            ViewGroup.LayoutParams layoutParams = flVideoContainer.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            flVideoContainer.setLayoutParams(layoutParams);
        }

        switchTopBottomBar(!isFullscreen);
    }

    private void switchTopBottomBar(boolean show) {
        if (show) {
            findViewById(R.id.rl_top_bar).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_instruction).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_footer_container).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.rl_top_bar).setVisibility(View.GONE);
            findViewById(R.id.tv_instruction).setVisibility(View.GONE);
            findViewById(R.id.ll_footer_container).setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        ivPlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        ivPlay.setVisibility(View.GONE);
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {

    }

    @Override
    public void getFundDetailInfoSuccess(FundDetailInfoJson response) {
        hideLoadingDialog();
        if (null != response) {
            if (null != response.getData()) {
                if (null != response.getData().getInfo()) {
                    fundDetailInfo = response.getData().getInfo();
//                    response = new GsonBuilder().create().fromJson(jsonStr, FundDetailInfoJson.class);
//                    fundDetailInfo = response.getData().getInfo();
                    initViewsWithData(fundDetailInfo);
                }
            }
        }
    }

    @Override
    public void getFundDetailInfoFailed() {
        hideLoadingDialog();
    }

    public class MyMarkerView extends MarkerView {

        private TextView tvDate;
        private TextView tvTotalNet;
        private TextView tvHsValue;

        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvDate = (TextView) findViewById(R.id.tv_date);
            tvTotalNet = (TextView) findViewById(R.id.tv_total_net_value);
            tvHsValue = (TextView) findViewById(R.id.tv_hs_value);
        }

        /* 每次畫 MakerView 時都會觸發這個 Callback 方法，通常會在此方法內更新 View 的內容 */
        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            tvDate.setText(dates.get(((int) e.getX()) % dates.size()));
            tvTotalNet.setText(String.format("%s%s", getString(R.string.total_net), netChartData.get((int) e.getX()).getY()));
            tvHsValue.setText(String.format("%s%s", getString(R.string.shsz_300), shszChartData.get((int) e.getX()).getY()));
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }

    }

}