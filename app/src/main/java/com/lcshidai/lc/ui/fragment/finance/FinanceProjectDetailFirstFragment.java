package com.lcshidai.lc.ui.fragment.finance;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.finance.FinanceInfoImpl;
import com.lcshidai.lc.impl.finance.GuaranteeIconImpl;
import com.lcshidai.lc.model.finance.FinanceInfoData;
import com.lcshidai.lc.model.finance.FinanceInfoJson;
import com.lcshidai.lc.model.finance.GuaranteeIconData;
import com.lcshidai.lc.model.finance.GuaranteeIconJson;
import com.lcshidai.lc.model.finance.RemindData;
import com.lcshidai.lc.service.finance.GetFinanceProjectDetailService;
import com.lcshidai.lc.service.finance.HttpGuaranteeIconService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.widget.ArcTextProgressBar;
import com.lcshidai.lc.widget.verticalviewpager.VerticalViewPager;

import java.util.List;

/**
 * @author Randy
 * @Description: 项目信息第一部分
 */
public class FinanceProjectDetailFirstFragment extends TRJFragment implements FinanceInfoImpl,
        GuaranteeIconImpl, OnTouchListener, OnGestureListener {
    private VerticalViewPager verticalViewPager;

    public View view;
//    private ImageView ivProSeries;                                                      // 项目系
    private ArcTextProgressBar arcTextProgressBar;                                      // 自定义进度条
    private LinearLayout llRateContainer;                                               // 年化收益率
    private TextView tvNormalRate;                                                      // 正常收益率
    private TextView tvNormalRateSymbol;                                                // 正常收益率符号

    private TextView tvIsFloatSymbol;                                                   // 浮动符号

    private LinearLayout llPlatformRewardContainer;                                     // 平台奖励收益百分比容器
    private TextView tvPlatformRewardRate;                                              // 平台奖励收益率
    private TextView tvPlatformRewardPlus;
    private TextView tvPlatformRewardRateSymbol;                                        // 平台奖励收益率符号
    private ImageView ivRateQuestionSymbol;                                             // 平台收益率问号（点击查看说明）

    private TextView tvRemainInvestAmount;                                              // 剩余可购金额

    private TextView tvTenThousandProfit;                                               // 万元收益原始值
    private TextView tvTenThousandProfitView;                                           // 万分收益
    private TextView tvInvestDeadline;                                                  // 投资截止倒计时
    private LinearLayout llFloatContainer;
    private TextView tvRewardFloat;
    private TextView tvInvestLimitTime;                                                 // 投资期限
    private TextView tvInvestLimitTimeUnit;                                             // 投资期限单位
    private TextView tvPaybackWay;                                                      // 收益分配方式
    private TextView tvMaxInvestAmount;                                                 // 最大投资金额
    private TextView tvMinInvestAmount;                                                 // 起购金额
    private LinearLayout llDetailContainer;                                             // 详情容器
    private TextView tvDetailLabel;                                                     // 详情标签

    private LinearLayout llGuaranteeIconsContainer;                                     // 保障图标容器

    TimeCount mTimeCount;
    private PopupWindow popupWindow;

    private GetFinanceProjectDetailService getPrjDetailService;
    private HttpGuaranteeIconService guaranteeIconService;
    public String limitMoney;
    public String canInvest;
    private String mPrjId, strYearRate;
    private int isCollection;
    private FinanceInfoData data;

    private GestureDetector detector;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mPrjId = args.getString(Constants.Project.PROJECT_ID);
            isCollection = args.getInt(Constants.Project.IS_COLLECTION);
            limitMoney = args.getString(Constants.Project.LIMIT_MONEY);
            canInvest = args.getString(Constants.Project.CAN_INVEST);
        }
        detector = new GestureDetector(this);
        getPrjDetailService = new GetFinanceProjectDetailService((TRJActivity) getActivity(), this);
        guaranteeIconService = new HttpGuaranteeIconService((TRJActivity) getActivity(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public void setVerticalViewPager(VerticalViewPager verticalViewPager) {
        this.verticalViewPager = verticalViewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.finance_project_detail_first_fragment, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {
        //
        //view bindings
        //
//        ivProSeries = (ImageView) view.findViewById(R.id.iv_series);
        tvNormalRate = (TextView) view.findViewById(R.id.tv_rate);
        tvNormalRateSymbol = (TextView) view.findViewById(R.id.tv_rate_symbol1);
        tvInvestLimitTime = (TextView) view.findViewById(R.id.tv_time_limit_num);
        tvInvestLimitTimeUnit = (TextView) view.findViewById(R.id.tv_time_limit_unit);
        arcTextProgressBar = (ArcTextProgressBar) view.findViewById(R.id.pb_rate);
        tvPaybackWay = (TextView) view.findViewById(R.id.tv_payback_way);
        tvRemainInvestAmount = (TextView) view.findViewById(R.id.tv_remaining_amount);
        tvMaxInvestAmount = (TextView) view.findViewById(R.id.tv_max_invest_amount);
        tvMinInvestAmount = (TextView) view.findViewById(R.id.tv_min_invest_amount);
        tvTenThousandProfit = (TextView) view.findViewById(R.id.tv_wanyuan_profit);
        tvTenThousandProfitView = (TextView) view.findViewById(R.id.tv_ten_thousand_profit_view);
        tvInvestDeadline = (TextView) view.findViewById(R.id.tv_invest_deadline);
        tvRewardFloat = (TextView) view.findViewById(R.id.tv_reward_float);
        llPlatformRewardContainer = (LinearLayout) view.findViewById(R.id.ll_platform_reward_container);
        tvPlatformRewardRate = (TextView) view.findViewById(R.id.tv_platform_reward_rate);
        tvPlatformRewardPlus = (TextView) view.findViewById(R.id.tv_platform_reward_plus);
        tvPlatformRewardRateSymbol = (TextView) view.findViewById(R.id.tv_platform_reward_rate_symbol);
        ivRateQuestionSymbol = (ImageView) view.findViewById(R.id.iv_rate_question_symbol);
        llFloatContainer = (LinearLayout) view.findViewById(R.id.ll_float_container);
        tvIsFloatSymbol = (TextView) view.findViewById(R.id.tv_is_float_symbol);
        llRateContainer = (LinearLayout) view.findViewById(R.id.ll_rate_container);
        llDetailContainer = (LinearLayout) view.findViewById(R.id.ll_detail_container);
        tvDetailLabel = (TextView) view.findViewById(R.id.tv_detail_label);
        llGuaranteeIconsContainer = (LinearLayout) view.findViewById(R.id.ll_guarantee_icons_container);

        //
        //event bindings
        //
        ivRateQuestionSymbol.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (null != popupWindow && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                showAnswerPop(ivRateQuestionSymbol);

            }
        });

        llDetailContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != verticalViewPager) {
                    verticalViewPager.setCurrentItem(1);
                }
            }
        });
    }

    private void initData(FinanceInfoData data) {
        if (isCollection == 1) {
            tvDetailLabel.setText("集合详情");
        } else {
            tvDetailLabel.setText("详情");
        }
//        int colorStart = Color.parseColor("#FCECB5");
//        int colorEnd = Color.parseColor("#F7971C");
//        Shader shader = new LinearGradient(0, 0, 0, 40, colorStart, colorEnd, Shader.TileMode.CLAMP);
//        String series = data.getPrj_series();
//        if (!CommonUtil.isNullOrEmpty(series)) {
//            if (series.equals("1")) {
//                ivProSeries.setImageDrawable(getResources().getDrawable(R.drawable.icon_detail_series_bo));
//            } else if (series.equals("2")) {
//                ivProSeries.setImageDrawable(getResources().getDrawable(R.drawable.icon_detail_series_dun));
//            } else if (series.equals("3")) {
//                ivProSeries.setImageDrawable(getResources().getDrawable(R.drawable.icon_detail_series_ying));
//            }
//        }
        strYearRate = data.getRate() + data.getRate_symbol();
        SpannableString rateSpannableStr = new SpannableString(data.getRate());
        rateSpannableStr.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(getActivity(), 30)), 0,
                data.getRate().indexOf("."), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvNormalRate.setText(rateSpannableStr);
        tvPlatformRewardRateSymbol.setText(data.getRate_symbol());
        tvNormalRateSymbol.setText(data.getRate_symbol());
        tvInvestLimitTime.setText(data.getTime_limit_num());
        tvInvestLimitTimeUnit.setText(data.getTime_limit_unit());

//        tvNormalRate.getPaint().setShader(shader);
//        tvNormalRateSymbol.getPaint().setShader(shader);
//        tvPlatformRewardRate.getPaint().setShader(shader);
//        tvPlatformRewardPlus.getPaint().setShader(shader);
//        tvPlatformRewardRateSymbol.getPaint().setShader(shader);
//        tvIsFloatSymbol.getPaint().setShader(shader);

        tvPaybackWay.setText(data.getRepay_way());
        if (CommonUtil.isNullOrEmpty(data.getRemaining_amount())) {
            tvRemainInvestAmount.setText("剩余可投" + "0" + "元");
        } else {
            tvRemainInvestAmount.setText("剩余可投" + data.getRemaining_amount() + "元");
        }
        String maxMoney = data.getMax_bid_amount();
        if ("0".equals(maxMoney)) {
            tvMaxInvestAmount.setText("不限");
        } else {
            tvMaxInvestAmount.setText(maxMoney + "元");
        }
        String minBid = data.getMin_bid_amount() + "元，" + data.getStep_bid_amount() + "元递增";
        tvMinInvestAmount.setText(minBid);
        int wanyuan = data.getWanyuan_profit();
        tvTenThousandProfit.setText(wanyuan + "");
        String wanyuan_profit_total_view = data.getWanyuan_profit_total_view();
        tvTenThousandProfitView.setText("" + wanyuan_profit_total_view);
        int progress = Integer.valueOf(data.getSchedule());
        arcTextProgressBar.setCurrentProgress(progress);
        int isFloat = data.getIs_float();
        if (isFloat == 1) {
            llFloatContainer.setVisibility(View.VISIBLE);
            tvIsFloatSymbol.setVisibility(View.VISIBLE);
        } else if (isFloat == 0) {
            llFloatContainer.setVisibility(View.GONE);
            tvIsFloatSymbol.setVisibility(View.GONE);
        }

        if (data.getActivity_id() > 0) {
            String activityRate = data.getReward_money_rate();
            if (StringUtils.isEmpty(activityRate)) {
                activityRate = "0";
            }
            tvPlatformRewardRate.setText(data.getReward_money_rate());
            if ("0".equals(activityRate)) {
                llPlatformRewardContainer.setVisibility(View.GONE);
                ivRateQuestionSymbol.setVisibility(View.GONE);
                tvNormalRateSymbol.setVisibility(View.VISIBLE);
                llFloatContainer.setVisibility(View.GONE);
            } else {
                llFloatContainer.setVisibility(View.VISIBLE);
                ivRateQuestionSymbol.setVisibility(View.VISIBLE);
                llPlatformRewardContainer.setVisibility(View.VISIBLE);
                tvNormalRateSymbol.setVisibility(View.GONE);
            }
        } else {
            llPlatformRewardContainer.setVisibility(View.GONE);
        }
        // 判断当前项目是否已开标
        if (!StringUtils.isEmpty(data.getStart_bid_time_diff())) {
            long startTime = Long.parseLong(data.getStart_bid_time_diff());
            if (startTime == 0) {
                Long remaning_time = 0l;// =Long.parseLong(pi.end_bid_time_diff);
                remaning_time = Long.parseLong(data.getEnd_bid_time_diff());
                if (remaning_time > 0) {
                    if (mTimeCount != null) {
                        mTimeCount.cancel();
                    }
                    mTimeCount = new TimeCount(
                            Math.abs(remaning_time) * 1000 + 1000, 1000,
                            data.bid_status);
                    mTimeCount.start();
                } else if (remaning_time == 0) {
                    tvInvestDeadline.setText("--:--:--");
                    if (mTimeCount != null) {
                        mTimeCount.cancel();
                    }
                }
            }
        }
    }

    private void loadData() {
        if (null == getActivity())
            return;
        getPrjDetailService.getFinanceProjectDetail(mPrjId, isCollection);
        guaranteeIconService.gainGuaranteeIcons(mPrjId);
    }

    @Override
    public void getFinanceProjectDetailSuccess(FinanceInfoJson response) {
        try {
            data = response.getData();
            RemindData remind = data.getRemind();
            if (null != remind) {
                String remindID = remind.getRemind_id();
                String is_available = remind.getIs_available();
                data.setRemind_id(remindID);
                data.setIs_available(is_available);
            }
            // 对应Views的数据处理
            initData(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    public void getFinanceProjectDetailFail() {

    }

    @Override
    public void gainGuaranteeIconSuccess(GuaranteeIconJson response) {
        if (null != response) {
            // 动态添加保障图标
            if (response.getBoolen().equals("1")) {
                List<GuaranteeIconData> list = response.getData();
                if (list == null) {
                    llGuaranteeIconsContainer.setVisibility(View.GONE);
                } else if (list.size() == 0) {
                    llGuaranteeIconsContainer.setVisibility(View.GONE);
                } else {
                    llGuaranteeIconsContainer.removeAllViews();
                    int width = llGuaranteeIconsContainer.getWidth() / list.size();
                    for (GuaranteeIconData data : list) {
                        LinearLayout smallContainer = (LinearLayout) LayoutInflater.from(mContext)
                                .inflate(R.layout.guarantee_icon_item, null);
                        ImageView ivGuaranteeIcon = (ImageView) smallContainer
                                .findViewById(R.id.iv_guarantee_icon);
                        TextView tvGuaranteeIcon = (TextView) smallContainer
                                .findViewById(R.id.tv_guarantee_name);
                        Glide.with(mContext).load(data.getPic()).into(ivGuaranteeIcon);
                        tvGuaranteeIcon.setText(data.getName());
                        llGuaranteeIconsContainer.addView(smallContainer);
                        smallContainer.getLayoutParams().width = width;
                        smallContainer.requestLayout();
                    }
                }
            }
        }
    }


    @Override
    public void gainGuaranteeIconFail() {

    }

    class TimeCountFinishHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            tvInvestDeadline.setText("--:--:--");
            if (mTimeCount != null) {
                mTimeCount.cancel();
            }
        }
    }

    public class TimeCount extends CountDownTimer {

        boolean isFinish = false;

        public TimeCount(long millisInFuture, long countDownInterval, String bidState) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            try {
                if (!isFinish) {
                    new TimeCountFinishHandler().sendEmptyMessage(0);
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            try {
                StringBuffer str = new StringBuffer();
                long second = millisUntilFinished / 1000;
                long s = second % 60; // 秒
                long mi = (second - s) / 60 % 60; // 分钟
                long h = ((second - s) / 60 - mi) / 60 % 24; // 小时
                long d = (((second - s) / 60 - mi) / 60 - h) / 24; // 天
                if (d > 0) {
                    str.append(d + "天");
                }
                String hour = h >= 10 ? h + "" : "0" + h;
                String minute = mi >= 10 ? mi + "" : "0" + mi;
                String mill = s >= 10 ? s + "" : "0" + s;
                str.append(hour + "小时");
                str.append(minute + "分");
                str.append(mill + "秒");
                tvInvestDeadline.setText(str);// +item.ra
            } catch (Exception e) {
                isFinish = true;
                // cancel();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mTimeCount != null) {
            mTimeCount.isFinish = true;
            mTimeCount.cancel();
        }
        super.onDestroy();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 0;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
//		        	 Toast.makeText(getActivity(), "aaa", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);
        return true;
    }

//    借款收益率＋平台奖励收益率de 问题弹窗
    public void showAnswerPop(View parent) {
        View container = LayoutInflater.from(getActivity()).inflate(R.layout.finance_item_first_fragment_question_pop_win, null);
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindow = new PopupWindow(container, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        // 点击其他地方消失
        container.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        int[] location = new int[2];
        parent.getLocationOnScreen(location);

        popupWindow.getContentView().measure(0,0);
        int width = popupWindow.getContentView().getMeasuredWidth();
        int height = popupWindow.getContentView().getMeasuredHeight();
        popupWindow.showAsDropDown(parent,-(width-parent.getWidth()/2)+DensityUtil.dip2px(getActivity(),17),-(parent.getHeight()+height)-DensityUtil.dip2px(getActivity(), 3));

//        popupWindow.showAsDropDown(parent, -DensityUtil.getScreenWidth(getActivity()) + location[0]
//                        - parent.getWidth() - DensityUtil.dip2px(getActivity(), 5),
//                -(llRateContainer.getHeight() + parent.getHeight()) + DensityUtil.dip2px(getActivity(), 5));
    }

}
