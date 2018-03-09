package com.lcshidai.lc.ui.fragment.finance;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.GetHomePopImpl;
import com.lcshidai.lc.impl.finance.GuaranteeIconImpl;
import com.lcshidai.lc.impl.finance.NavigationImpl;
import com.lcshidai.lc.impl.finance.RecommendInfoImpl;
import com.lcshidai.lc.model.GetHomePopData;
import com.lcshidai.lc.model.GetHomePopJson;
import com.lcshidai.lc.model.MessageLocalData;
import com.lcshidai.lc.model.MessageTypeNew;
import com.lcshidai.lc.model.MsgNew;
import com.lcshidai.lc.model.finance.FinaceHomeJson;
import com.lcshidai.lc.model.finance.FinaceHomeNewbieItem;
import com.lcshidai.lc.model.finance.FinaceNavigationData;
import com.lcshidai.lc.model.finance.FinaceNavigationJson;
import com.lcshidai.lc.model.finance.GuaranteeIconData;
import com.lcshidai.lc.model.finance.GuaranteeIconJson;
import com.lcshidai.lc.service.HttpGetHomePopService;
import com.lcshidai.lc.service.finance.HttpGuaranteeIconService;
import com.lcshidai.lc.service.finance.HttpNavigationService;
import com.lcshidai.lc.service.finance.HttpRecommendService;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.finance.FinaceMovementActivity;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.ui.fragment.BannerViewPagerFragment;
import com.lcshidai.lc.ui.more.InviteActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.utils.GoClassUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.MsgUtil;
import com.lcshidai.lc.widget.progress.HalfCircleProgressIndicator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;


/**
 * 首页
 *
 * @author
 */
public class FragmentHome extends TRJFragment implements OnClickListener, RecommendInfoImpl,
        NavigationImpl, GuaranteeIconImpl, GetHomePopImpl {

    private BannerViewPagerFragment bannerFragment = new BannerViewPagerFragment();
    //	private View mProgressBar;
    private ScrollView scroll_home;
    private View rlempty;
    private View mProgressBar;
    private LinearLayout ll_navigation;
    private TextView seeMoreBtn;
    private RelativeLayout rl_notice;
    private TextView tv_notice;
    private ImageView iv_notice_close;
    private ImageView iv_notice_home;
    private AnimationDrawable animationDrawable;
    private RelativeLayout rl1;
    private HalfCircleProgressIndicator roundpiIn;
    private HalfCircleProgressIndicator roundpiOut;
    private ImageView iv_activity;
    private ImageView iv_new;
    private TextView tv_prj_flag;
    private TextView tv_prj_type;
    private TextView tv_year_rate;
    private TextView tv_year_rate_add;
    private TextView tv_year_rate_award;
    private TextView tv_time_limit;
    private TextView tv_min_bid_amount;
    private TextView tv_invest_count;
    private TextView tv_finance_now;
    private TextView tv_to_invest;
    private TextView tv_safe_text_1, tv_safe_text_2, tv_safe_text_3;

    private HttpRecommendService hmrs;
    private HttpNavigationService httpNavigationService;
    private HttpGuaranteeIconService httpGuaranteeIconService;
    private HttpGetHomePopService httpGetHomePopService;

    private Dialog homeActivityDialog = null;

    private boolean HIDE_NOTICE;
    private String prj_id;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hmrs = new HttpRecommendService((TRJActivity) getActivity(), this);
        httpNavigationService = new HttpNavigationService((TRJActivity) getActivity(), this);
        httpGuaranteeIconService = new HttpGuaranteeIconService((TRJActivity) getActivity(), this);
        httpGetHomePopService = new HttpGetHomePopService((TRJActivity) getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        填充首页布局
        View contentView = inflater.inflate(R.layout.fragment_home_france, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkAppUpdate();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if (bannerFragment.isAdded()) {
            getActivity().getSupportFragmentManager().putFragment(
                    savedInstanceState, "bannerFragment", bannerFragment);
        } else {
            fragmentTransaction.add(R.id.banner_fragment, bannerFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();

        scroll_home = (ScrollView) view.findViewById(R.id.scroll_home);

        rlempty = view.findViewById(R.id.rl_empty);
        mProgressBar = view.findViewById(R.id.progressContainer);
        mProgressBar.setVisibility(View.GONE);

        ll_navigation = (LinearLayout) view.findViewById(R.id.ll_navigation);

        seeMoreBtn = (TextView) view.findViewById(R.id.btn_see_more);

        rl_notice = (RelativeLayout) view.findViewById(R.id.rl_notice);
        iv_notice_close = (ImageView) view.findViewById(R.id.iv_notice_home_close);
        tv_notice = (TextView) view.findViewById(R.id.tv_notice);
        rl_notice.setVisibility(View.GONE);
        iv_notice_home = (ImageView) view.findViewById(R.id.iv_notice_home);
        animationDrawable = (AnimationDrawable) iv_notice_home.getDrawable();

        rl1 = (RelativeLayout) view.findViewById(R.id.rl1);
        roundpiIn = (HalfCircleProgressIndicator) view.findViewById(R.id.rpis_in);
        roundpiIn.setProgresColor(Color.rgb(0xdd, 0xdd, 0xdd));// #DDDDDD
        roundpiIn.setProgress(100);
        //进度条线宽设置，注意dp与px的切换
        roundpiIn.setProgressStrokeWidth(DensityUtil.dip2px(getActivity(),5));
        roundpiOut = (HalfCircleProgressIndicator) view.findViewById(R.id.rpis_out);
        roundpiOut.setProgresColor(Color.rgb(0xdd, 0xdd, 0xdd));
        //进度条线宽设置，注意dp与px的切换
        roundpiOut.setProgressStrokeWidth(DensityUtil.dip2px(getActivity(),5));
        //测试用数据
//		roundpiOut.setProgressMovie(82);
        iv_activity = (ImageView) view.findViewById(R.id.iv_activity);
        iv_new = (ImageView) view.findViewById(R.id.iv_new);
        tv_prj_flag = (TextView) view.findViewById(R.id.tv_prj_flag);
        tv_prj_type = (TextView) view.findViewById(R.id.tv_prj_type);
        tv_year_rate = (TextView) view.findViewById(R.id.tv_year_rate);
        tv_year_rate_add = (TextView) view.findViewById(R.id.tv_year_rate_add);
        tv_year_rate_award = (TextView) view.findViewById(R.id.tv_year_rate_award);
        tv_time_limit = (TextView) view.findViewById(R.id.tv_time_limit);
        tv_min_bid_amount = (TextView) view.findViewById(R.id.tv_min_bid_amount);
        tv_invest_count = (TextView) view.findViewById(R.id.tv_invest_count);
        tv_finance_now = (TextView) view.findViewById(R.id.tv_finance_now);
        tv_to_invest = (TextView) view.findViewById(R.id.tv_to_invest);
        tv_safe_text_1 = (TextView) view.findViewById(R.id.tv_safe_text_1);
        tv_safe_text_2 = (TextView) view.findViewById(R.id.tv_safe_text_2);
        tv_safe_text_3 = (TextView) view.findViewById(R.id.tv_safe_text_3);

        int startColor = Color.parseColor("#FFC33C");
        int endColor = Color.parseColor("#FF811C");

        Shader shader = new LinearGradient(0, 0, 0, 20, startColor, endColor, Shader.TileMode.CLAMP);
        tv_year_rate.getPaint().setShader(shader);
        tv_year_rate_add.getPaint().setShader(shader);
        tv_finance_now.getPaint().setShader(shader);

        seeMoreBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_see_more:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainWebActivity.class);
                intent.putExtra("title", "关于理财时代");
                String href = "#/aboutTab/2";
                if (href.startsWith("http")) {
                    intent.putExtra("web_url", href);
                } else {
                    intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/" + href);
                }
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scroll_home.scrollTo(0, 0);
        loadDada();
        initNotice();
    }

    public void resetData(boolean notRe) {

    }

    private void loadDada() {
        hmrs.gainRecommendInfo();
        httpNavigationService.gainNavigation();
        httpGetHomePopService.getHomePopMessage(CommonUtil.getDeviceId(getActivity()));
    }

    /**
     * 初始化首页活动弹框
     *
     * @param data 首页弹框数据信息
     */
    private void initHomeActivityDialog(final GetHomePopData data) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//        填充初始化首页时弹框布局
        View view = layoutInflater.inflate(R.layout.layout_home_activity_pop, null);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
        ImageView ivActivityBanner = (ImageView) view.findViewById(R.id.iv_activity_banner);
        TextView tvActivityButton = (TextView) view.findViewById(R.id.tv_activity_button);

        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivityDialog.dismiss();

            }
        });
        if (!CommonUtil.isNullOrEmpty(data.getImg())) {
            Glide.with(mContext).load(data.getImg()).into(ivActivityBanner);
        } else {
            ivActivityBanner.setBackgroundResource(R.drawable.banner_default);
        }
        if (!CommonUtil.isNullOrEmpty(data.getButton_text())) {
            //  当返回的图片中不包含文字按钮是，显示文字
            tvActivityButton.setVisibility(View.VISIBLE);
            tvActivityButton.setText(data.getButton_text());
        } else {
            //  当返回的图片中包含文字按钮是，不显示文字按钮
            tvActivityButton.setVisibility(View.GONE);
        }

        ivActivityBanner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isNullOrEmpty(data.getUrl())) {
                    Intent intent = new Intent(mContext, MainWebActivity.class);
                    intent.putExtra("web_url", data.getUrl());
                    intent.putExtra("need_header", 0);
                    intent.putExtra("title", "周年庆典");
                    startActivity(intent);
                    homeActivityDialog.dismiss();
                }
            }
        });
        tvActivityButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isNullOrEmpty(data.getUrl())) {
                    Intent intent = new Intent(mContext, MainWebActivity.class);
                    intent.putExtra("web_url", data.getUrl());
                    intent.putExtra("need_header", 0);
                    intent.putExtra("title", "周年庆典");
                    startActivity(intent);
                    homeActivityDialog.dismiss();
                }
            }
        });

        homeActivityDialog = new Dialog(getActivity(), R.style.style_loading_dialog);
        homeActivityDialog.setContentView(view);
        homeActivityDialog.setCancelable(true);
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = homeActivityDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.8); //设置宽度
        homeActivityDialog.getWindow().setAttributes(lp);
    }

//    初始化滚动通知
    private void initNotice() {
        if (HIDE_NOTICE) {
            rl_notice.setVisibility(View.GONE);
        }
        String title = null;
        String url = null;
        final MessageLocalData data = (MessageLocalData) MsgUtil.getObj(getActivity(), MsgUtil.INVEST);
        if (data != null && data.getMap() != null) {
            final MessageTypeNew type = data.getMap().get(MsgUtil.TYPE_INVEST_NOTICE);
            if (type != null && type.getMessages().size() > 0) {
                String msg = type.getMessages().get(0).getMsg();
                boolean isDirty = type.getMessages().get(0).isDirty();
                if (!isDirty) {
                    //产品全新蜕变，畅享全新移动互联体验|www.rty.com
                    title = msg.split("\\|")[0];
                    url = msg.split("\\|")[1];
                }
            }
            if (title == null || "".equals(title)) {
                return;
            }
            rl_notice.setVisibility(View.VISIBLE);
            animationDrawable.start();
            tv_notice.setText(title);
            tv_notice.setTag(url);
            tv_notice.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String url = (String) tv_notice.getTag();
                    if (url != null && !"".equals(url)) {
                        if (data.getMap().containsKey(MsgUtil.TYPE_INVEST_NOTICE)) {
                            MessageTypeNew messageTypeNew = data.getMap().get(MsgUtil.TYPE_INVEST_NOTICE);
                            MsgNew msgNew = messageTypeNew.getMessages().get(0);
                            msgNew.setDirty(true);
                            MsgUtil.setObj(getActivity(), MsgUtil.INVEST, data);
                            Intent in = new Intent();
                            in.setAction(MsgUtil.MSG_ACTION_REFRESH);
                            in.putExtra("flag", 0);
                            getActivity().sendBroadcast(in);
                        }
                        Intent intent = new Intent(getActivity(), MainWebActivity.class);
                        intent.putExtra("title", " ");
                        intent.putExtra("web_url", url);
                        getActivity().startActivity(intent);
                        HIDE_NOTICE = true;
                    }
                }
            });
            iv_notice_close.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Animation textDismissAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.text_dismiss);
                    tv_notice.startAnimation(textDismissAnim);
                    Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.notice_dismiss);
                    anim.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (data.getMap().containsKey(MsgUtil.TYPE_INVEST_NOTICE)) {
                                MessageTypeNew messageTypeNew = data.getMap().get(MsgUtil.TYPE_INVEST_NOTICE);
                                MsgNew msgNew = messageTypeNew.getMessages().get(0);
                                msgNew.setDirty(true);
                                MsgUtil.setObj(getActivity(), MsgUtil.INVEST, data);
                                Intent in = new Intent();
                                in.setAction(MsgUtil.MSG_ACTION_REFRESH);
                                in.putExtra("flag", 0);
                                getActivity().sendBroadcast(in);
                            }
                            rl_notice.setVisibility(View.GONE);
                        }
                    });
                    rl_notice.startAnimation(anim);

                }
            });
        }
    }

    @Override
    public void gainRecommendsuccess(FinaceHomeJson response) {
        try {
            if (response != null) {
                if (response.getBoolen().equals("1")) {
                    FinaceHomeNewbieItem recommend = response.getData().getPrjList().getRecommend();
                    if (recommend != null) {
                        FinaceHomeNewbieItem item = recommend;
                        prj_id = recommend.getId();
                        httpGuaranteeIconService.gainGuaranteeIcons(prj_id);
                        // 这断设置颜色的代码位置要选择正确
                        int mStartColor = Color.parseColor("#FF811C");
                        int mEndColor = Color.parseColor("#FFC33C");
                        Shader mShader = new LinearGradient(0, 0, 0, 20, mStartColor, mEndColor, Shader.TileMode.CLAMP);
                        roundpiOut.getPaint().setShader(mShader);

                        roundpiOut.setProgressMovie(Integer.parseInt(item.getSchedule()));
                        String flag = item.getPrj_series();
                        if ("1".equals(flag)) {
                            tv_prj_flag.setBackground(getResources().getDrawable(R.drawable.icon_shishizhuan));
                        } else if ("2".equals(flag)) {
                            tv_prj_flag.setBackground(getResources().getDrawable(R.drawable.icon_xiaoshidai));
                        } else if ("3".equals(flag)) {
                            tv_prj_flag.setBackground(getResources().getDrawable(R.drawable.icon_qiedai));
                        }
                        if(!CommonUtil.isNullOrEmpty(item.getPrj_name_show())) {
                            tv_prj_type.setText(item.getPrj_name_show());
                        } else {
                            tv_prj_type.setText(item.getPrj_type_name() + "-" + item.getPrj_name());
                        }
                        String year_rate = item.getYear_rate();
                        String left;
                        String right;
                        if (year_rate.contains("+")) {
                            int index = year_rate.indexOf("+");
                            if (index != -1) {
                                left = year_rate.substring(0, index);
                                right = year_rate.substring(index + 1);
                                float l = Float.valueOf(left);
                                float r = Float.valueOf(right);
                                left = String.format("%.0f", l);
                                right = String.format("%.2f", r);
                                tv_year_rate.setText(left);
                                tv_year_rate_add.setText(".00" + "+" + right + "%");
                                tv_year_rate_award.setVisibility(View.VISIBLE);
                            }
                        } else if (year_rate.contains(".")) {
                            int index = year_rate.indexOf(".");
                            if (index != -1) {
                                left = year_rate.substring(0, index);
                                right = year_rate.substring(index);
                                tv_year_rate.setText(left);
                                tv_year_rate_add.setText(right + "%");
                                tv_year_rate_award.setVisibility(View.INVISIBLE);
                            }
                        }

                        tv_time_limit.setText(item.getTime_limit() + item.getTime_limit_unit_view());
                        DecimalFormat f = new DecimalFormat("0,000.00");
                        Double d = Double.parseDouble(f.parseObject(item.getMin_bid_amount_name()).toString());
                        BigDecimal bd = new BigDecimal(d);
                        BigDecimal bd2 = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
                        tv_min_bid_amount.setText(bd2 + "元");
                        tv_invest_count.setText("已有" + item.getInvest_count() + "笔认购");

                        if ("1".equals(item.getIs_new())) {
                            iv_new.setImageResource(R.drawable.icon_newcus);
                        } else {
                            iv_new.setImageResource(R.drawable.icon_suggest);
                        }


                        final FinaceHomeNewbieItem itemt = item;
                        tv_to_invest.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                String prj_name = tv_prj_type.getText().toString();
                                Bundle args = new Bundle();
                                args.putString("prj_id", itemt.getId());
                                args.putString("prj_name", prj_name);
//								getActivity().getIntent().putExtra("goClass", FinanceProjectDetailActivity.class.getName());
                                GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
                                GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
                                MemorySave.MS.args = args;
                                Intent intent = new Intent();
                                intent.putExtras(args);
                                intent.setClass(mContext, FinanceProjectDetailActivity.class);
                                startActivity(intent);
                            }
                        });
                        rl1.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                String prj_name = tv_prj_type.getText().toString();
                                Bundle args = new Bundle();
                                args.putString(Constants.Project.PROJECT_ID, itemt.getId());
                                args.putString(Constants.Project.PROJECT_NAME, prj_name);
                                args.putInt(Constants.Project.IS_COLLECTION, itemt.getIs_collection());
//								getActivity().getIntent().putExtra("goClass", FinanceProjectDetailActivity.class.getName());
                                GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
                                GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
                                MemorySave.MS.args = args;
                                Intent intent = new Intent();
                                intent.putExtras(args);
                                intent.setClass(mContext, FinanceProjectDetailActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mProgressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void gainRecommendfail() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void gainNavigationSuccess(FinaceNavigationJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                List<FinaceNavigationData> dataList = response.getData();
                int size = dataList.size();
                int width = ll_navigation.getWidth() / size;
                ll_navigation.removeAllViews();
                for (int i = 0; i < dataList.size(); i++) {
                    FinaceNavigationData finaceNavigationData = dataList.get(i);
//                    填充首页四个图标的布局
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.finance_navigation_item, null);
                    ImageView iv_navigation = (ImageView) view.findViewById(R.id.iv_navigation);
                    TextView tv_navigation = (TextView) view.findViewById(R.id.tv_navigation);
                    ImageView iv_dot = (ImageView) view.findViewById(R.id.iv_dot);
                    Glide.with(mContext).load(finaceNavigationData.getPic()).into(iv_navigation);
                    tv_navigation.setText(finaceNavigationData.getName());

                    //活动的小红点
                    if (i == 0) {
                        MessageLocalData invest_data = (MessageLocalData) MsgUtil.getObj(getActivity(), MsgUtil.INVEST);
                        if (invest_data != null) {
                            for (Map.Entry<String, MessageTypeNew> map : invest_data.getMap().entrySet()) {

                                if (map.getKey().equals(MsgUtil.TYPE_INVEST_HOT) && map.getValue().getMessages().size() > 0) {
                                    List<MsgNew> msgNews = map.getValue().getMessages();
                                    for (MsgNew msgNew : msgNews) {
                                        if (!msgNew.isDirty()) {
                                            iv_dot.setVisibility(View.VISIBLE);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    ll_navigation.addView(view);
                    view.getLayoutParams().width = width;
                    view.requestLayout();

                    final String name = finaceNavigationData.getName();
                    final String hrefType = finaceNavigationData.getHrefType();
                    final String href = finaceNavigationData.getHref();
                    final String need_header = finaceNavigationData.getNeed_header();
//                    导航点击事件
                    view.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (hrefType != null) {
                                if (hrefType.equals("wap")) {
                                    if (href != null && !href.isEmpty()) {
                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(), MainWebActivity.class);
                                        intent.putExtra("title", name);
                                        if (href.startsWith("http")) {
                                            intent.putExtra("web_url", href);
                                        } else {
                                            intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/" + href);
                                        }
                                        intent.putExtra("need_header", need_header);
                                        startActivity(intent);
                                    }
                                } else {
                                    if ("rmhd".equals(hrefType)) {
//                                        跳转热门活动页面
                                        Intent intent = new Intent(getActivity(), FinaceMovementActivity.class);
                                        startActivity(intent);
                                    }
                                    if ("yqhy".equals(hrefType)) {
//                                        跳转邀请好友页面
                                        Intent intent = new Intent(mContext, InviteActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void gainNavigationFail() {

    }

    @Override
    public void gainGuaranteeIconSuccess(GuaranteeIconJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                tv_safe_text_1.setVisibility(View.GONE);
                tv_safe_text_2.setVisibility(View.GONE);
                tv_safe_text_3.setVisibility(View.GONE);
                List<GuaranteeIconData> data = response.getData();
                for (int i = 0; i < data.size(); i++) {
                    String name_view_1 = data.get(i).getName_view_1();
                    String name_view_2 = data.get(i).getName_view_2();
                    String name = name_view_1 + "\n" + name_view_2;
                    if (i == 0) {
                        tv_safe_text_1.setText(name);
                        tv_safe_text_1.setVisibility(View.VISIBLE);
                        if (name_view_1 != null && name_view_1.length() <= 2
                                && name_view_2 != null && name_view_2.length() <= 2) {
                            tv_safe_text_1.setBackground(getResources().getDrawable(R.drawable.shape_home_safe));
                        } else {
                            tv_safe_text_1.setBackground(getResources().getDrawable(R.drawable.shape_home_safe2));
                        }
                    } else if (i == 1) {
                        tv_safe_text_2.setText(name);
                        tv_safe_text_2.setVisibility(View.VISIBLE);
                        if (name_view_1 != null && name_view_1.length() <= 2
                                && name_view_2 != null && name_view_2.length() <= 2) {
                            tv_safe_text_2.setBackground(getResources().getDrawable(R.drawable.shape_home_safe));
                        } else {
                            tv_safe_text_2.setBackground(getResources().getDrawable(R.drawable.shape_home_safe2));
                        }
                    } else if (i == 2) {
                        tv_safe_text_3.setText(name);
                        tv_safe_text_3.setVisibility(View.VISIBLE);
                        if (name_view_1 != null && name_view_1.length() <= 2
                                && name_view_2 != null && name_view_2.length() <= 2) {
                            tv_safe_text_3.setBackground(getResources().getDrawable(R.drawable.shape_home_safe));
                        } else {
                            tv_safe_text_3.setBackground(getResources().getDrawable(R.drawable.shape_home_safe2));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void gainGuaranteeIconFail() {

    }

    @Override
    public void getHomePopSuccess(GetHomePopJson response) {
        if (null != response) {
            if (response.getBoolen().equals("1")) {
                // show pop win
                if (null != response.getData()) {
                    if (response.getData().getIs_open() == 1) {
                        if (null != homeActivityDialog) {
                            homeActivityDialog.show();
                        } else {
                            initHomeActivityDialog(response.getData());
                            homeActivityDialog.show();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void getHomePopFail() {

    }
}
