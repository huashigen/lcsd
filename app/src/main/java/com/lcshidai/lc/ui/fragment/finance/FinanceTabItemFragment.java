package com.lcshidai.lc.ui.fragment.finance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.R.color;
import com.lcshidai.lc.impl.BroadCastImpl;
import com.lcshidai.lc.impl.PIPWCallBack;
import com.lcshidai.lc.impl.finance.FinanceTabItemImpl;
import com.lcshidai.lc.model.finance.FinanceTabItemJson;
import com.lcshidai.lc.model.finance.FinanceTabItemProductItem;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckData;
import com.lcshidai.lc.service.finance.HttpFiananceTabItemService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.GoClassUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.NetUtils;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.ppwindow.DialogPopupWindow;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow;
import com.lcshidai.lc.widget.ppwindow.ProfileInvestPopupWindow;
import com.lcshidai.lc.widget.progress.RoundProgressIndicator;
import com.lcshidai.lc.widget.xlvfresh.XListView;
import com.lcshidai.lc.widget.xlvfresh.XListView.IXListViewListener;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  投资Item Fragment
 */
public class FinanceTabItemFragment extends TRJFragment implements
        PIPWCallBack, IXListViewListener, DialogPopupWindow.ChooseListener,
        FinanceTabItemImpl {

    private XListView mListView;
    private ItemAdapter mAdapter;
    public static final String P_TYPE_ALL = "0";
    public static final String P_TYPE_1 = "1";//bo
    public static final String P_TYPE_2 = "2";//dun
    public static final String P_TYPE_3 = "3";//ying
    private View mContent;
    private int mPagesize = 10;
    private int mP = 1;
    boolean hasMore;
    public boolean isSu = false;// 是否为速转让项目
    public boolean isVinvest = false;// 是否为新秀理财项目

    public boolean isRundata = false;
    public String searchStr = "";
    public String mCurrentPType = "0";
    public String mMyorder = "0";
    public String filter = "";
    public String switch_wap = "0";
    ProfileInvestPopupWindow pipw;
    PayPasswordPopupWindow pw;
    public String homeFlag;// home界面传过来
    TimeCount mTc;

    public HashMap<String, TVIDTime> startTVMap = new HashMap<String, TVIDTime>();
    public ArrayList<String> idList = new ArrayList<String>();
    View mProgressBar;

    private DialogPopupWindow dialogPopupWindow;
    private View mainView;
    private FinanceInvestPBuyCheckData mymodel;

    HttpFiananceTabItemService mHttpService;
    private String search_param = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (null != data) {
            homeFlag = data.getString("homeFlag");
            search_param = data.getString("search_param");
        }
        mHttpService = new HttpFiananceTabItemService(
                (TRJActivity) getActivity(), this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContent = inflater.inflate(R.layout.fragment_lisvt_view, container, false);
        mAdapter = new ItemAdapter(getActivity());
        mProgressBar = mContent.findViewById(R.id.progressContainer);
        mProgressBar.setVisibility(View.GONE);
        mListView = (XListView) mContent.findViewById(R.id.listView);
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.finance_list_head, null);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.iv_type);
        imageView.setOnClickListener(null);
        if (mCurrentPType.equals(P_TYPE_1)) {
            imageView.setImageResource(R.drawable.finance_type_shishi);
            mListView.addHeaderView(headerView);
        } else if (mCurrentPType.equals(P_TYPE_2)) {
            imageView.setImageResource(R.drawable.finance_type_xiaoshi);
            mListView.addHeaderView(headerView);
        } else if (mCurrentPType.equals(P_TYPE_3)) {
            imageView.setImageResource(R.drawable.finance_type_qie);
            mListView.addHeaderView(headerView);
        }
        mListView.setAdapter(mAdapter);
        mListView.setPullLoadEnable(false);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                FinanceTabItemProductItem item = mAdapter.getItem(position - 1);
                String prj_name = item.getPrj_type_name() + "-" + item.getPrj_name();
                String id = mAdapter.getItem(position - 1).getId();
                final Bundle args = new Bundle();
                args.putString(Constants.Project.PROJECT_ID, id);
                args.putString(Constants.Project.PROJECT_NAME, prj_name);
                // isCollection
                args.putInt(Constants.Project.IS_COLLECTION, item.getIs_collection());
                GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
                GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
                MemorySave.MS.args = args;
                Intent intent = new Intent();
                intent.putExtras(args);
                intent.setClass(mContext, FinanceProjectDetailActivity.class);
                startActivity(intent);
//                new TestLogin(getActivity()).testIt((TRJActivity) getActivity());
            }
        });
        pipw = new ProfileInvestPopupWindow((TRJActivity) getActivity(), this);
        pw = new PayPasswordPopupWindow((TRJActivity) getActivity(), this);
        mainView = mContent.findViewById(R.id.ll_main);
        dialogPopupWindow = new DialogPopupWindow((TRJActivity) getActivity(), mainView, this);
        reLoadData();
        return mContent;
    }

    public synchronized void reLoadData() {
        if (mAdapter != null && !isRundata) {
            startTVMap.clear();
            idList.clear();
            if (mTc == null) {
                mTc = new TimeCount(Long.MAX_VALUE, 1000);
                mTc.start();
            }
            hasMore = false;
            mAdapter.clear();
            mListView.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            mP = 1;
            mContent.findViewById(R.id.rl_empty).setVisibility(View.GONE);
            loadData();
        }
    }

    public synchronized void reLoadDataDown() {
        if (mAdapter != null && !isRundata) {
            startTVMap.clear();
            idList.clear();
            if (mTc == null) {
                mTc = new TimeCount(Long.MAX_VALUE, 1000);
                mTc.start();
            }
            isDown = true;
            hasMore = false;
            mP = 1;
            loadData();
        }
    }

    boolean isDown = false;

    private void loadData() {

        if (getActivity() == null || !NetUtils.isNetworkConnected(getActivity())) {
            mContent.findViewById(R.id.listView).setVisibility(View.GONE);
            mContent.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
            return;
        }
        isRundata = true;
        mHttpService.gainPrjList(isSu, homeFlag, isVinvest, mCurrentPType,
                mMyorder, searchStr, filter, mP++, mPagesize, search_param);

    }

    @Override
    public void onStop() {
        super.onStop();
        isRundata = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MemorySave.MS.mGoFinancetype != -1) {
            MemorySave.MS.mGoFinancetype = -1;
            return;
        }
        if (MemorySave.MS.mRegLockSuccessBack) {
            MemorySave.MS.mRegLockSuccessBack = false;
            if (MemorySave.MS.lockArgs != null) {
                try {
                    MemorySave.MS.mRegLockSuccessBackMoney = true;
                    FinanceTabItemProductItem pi = (FinanceTabItemProductItem) MemorySave.MS.lockArgs
                            .getSerializable("pi");
                    pipw.goAnim(getActivity().findViewById(R.id.main), pi, false);
                } catch (Exception e) {
                }
            }
        }
    }

    public class ItemAdapter extends ArrayAdapter<FinanceTabItemProductItem> {
        private Context mContext;
        public List<FinanceTabItemProductItem> mfilelist = new ArrayList<FinanceTabItemProductItem>();
        public int size;

        public ItemAdapter(Activity context) {
            super(context, 0);
            mContext = context;
        }

        @Override
        public int getCount() {
            size = mfilelist.size();
            return size + (hasMore ? 1 : 0);
        }

        @Override
        public void add(FinanceTabItemProductItem object) {
            mfilelist.add(object);
        }

        @Override
        public void clear() {
            mfilelist.clear();
            super.clear();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public FinanceTabItemProductItem getItem(int position) {
            if (mCurrentPType.equals(P_TYPE_1) || mCurrentPType.equals(P_TYPE_2)
                    || mCurrentPType.equals(P_TYPE_3)) {
                return mfilelist.get(position - 1);
            } else {
                return mfilelist.get(position);
            }
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            try {
                final ViewHolders vh;
                if (position < size) {
                    vh = new ViewHolders();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.finance_item, null);
                    vh.tv_prj_type = (TextView) convertView.findViewById(R.id.tv_prj_type);
                    vh.iv_activity = (ImageView) convertView.findViewById(R.id.iv_activity);
                    vh.title_tv_1 = (TextView) convertView.findViewById(R.id.title_tv_1);
                    vh.title_tv_2 = (TextView) convertView.findViewById(R.id.title_tv_2);
                    vh.title_tv_3 = (TextView) convertView.findViewById(R.id.title_tv_3);
                    vh.content_tv_1 = (TextView) convertView.findViewById(R.id.content_tv_1);
                    vh.content_tv_2 = (TextView) convertView.findViewById(R.id.content_tv_2);
                    vh.content_tv_3 = (TextView) convertView.findViewById(R.id.content_tv_3);
                    vh.tv_down_right = (TextView) convertView.findViewById(R.id.tv_down_right);
                    vh.roundpi = (RoundProgressIndicator) convertView.findViewById(R.id.rpis);
                    vh.rpisout = (RoundProgressIndicator) convertView.findViewById(R.id.rpisout);
                    vh.tv_state_info = (TextView) convertView.findViewById(R.id.tv_state_info);
                    vh.iv_new = (ImageView) convertView.findViewById(R.id.iv_new);
                    vh.iv_prj_type = (ImageView) convertView.findViewById(R.id.iv_prj_type);
                    vh.v_prj_line = convertView.findViewById(R.id.v_prj_line);
                    vh.v_place_holder = convertView.findViewById(R.id.v_place_holder);
                    convertView.setTag(vh);
                    final FinanceTabItemProductItem pi = mfilelist.get(position);

                    if (mCurrentPType.equals(P_TYPE_1) || mCurrentPType.equals(P_TYPE_2)
                            || mCurrentPType.equals(P_TYPE_3)) {
                        vh.iv_prj_type.setVisibility(View.GONE);
                        vh.v_prj_line.setVisibility(View.GONE);
                        vh.v_place_holder.setVisibility(View.VISIBLE);
                    } else {
                        vh.iv_prj_type.setVisibility(View.VISIBLE);
                        vh.v_prj_line.setVisibility(View.VISIBLE);
                        vh.v_place_holder.setVisibility(View.GONE);
                        if ("1".equals(pi.getPrj_series())) {
                            vh.iv_prj_type.setImageResource(R.drawable.finance_item_type_shishi);
                        } else if ("2".equals(pi.getPrj_series())) {
                            vh.iv_prj_type.setImageResource(R.drawable.finance_item_type_xiaoshi);
                        } else if ("3".equals(pi.getPrj_series())) {
                            vh.iv_prj_type.setImageResource(R.drawable.finance_item_type_qie);
                        }
                    }
                    vh.roundpi.setProgress(100);
                    vh.roundpi.setProgressStrokeWidth(4);
                    vh.roundpi.setProgresColor(Color.rgb(0xdd, 0xdd, 0xdd));// #DDDDDD
                    vh.rpisout.setProgressStrokeWidth(4);
                    vh.rpisout.setProgresColor(Color.rgb(0xff, 0x93, 0x72));// #FF9372

                    String fir = "期限";
                    String fend = "";
                    String extendplus = "";
                    String extendvalue = "";
                    String extendunit = "";

                    if (pi.getActivity_id() != null
                            && !pi.getActivity_id().equals("")
                            && !pi.getActivity_id().equals("0")) {
                        vh.iv_activity.setVisibility(View.VISIBLE);
                        if (pi.getActivity_ext_info() == null
                                || pi.getActivity_ext_info().getBig_icon() == null
                                || pi.getActivity_ext_info().getBig_icon()
                                .equals("")) {
                            vh.iv_activity.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.activity_icon));
                        } else {
                            vh.iv_activity.setBackgroundDrawable(null);
                            Glide.with(mContext).load(pi.getActivity_ext_info().getBig_icon()).into(vh.iv_activity);
                        }
                    } else {
                        vh.iv_activity.setVisibility(View.GONE);
                    }

                    if (pi.getIs_new().equals("1")) {
                        vh.iv_new.setVisibility(View.VISIBLE);
                        if ("5".equals(pi.getBid_status())
                                || "4".equals(pi.getBid_status())
                                || "3".equals(pi.getBid_status())
                                || "7".equals(pi.getBid_status())) {
                            vh.iv_new.setImageResource(R.drawable.icon_newcus_gray);
                        } else {
                            vh.iv_new.setImageResource(R.drawable.icon_newcus);
                        }
                    } else {
                        vh.iv_new.setVisibility(View.INVISIBLE);
                    }
                    fend = pi.getTime_limit_unit_view();
                    if (pi.getIs_extend() != null && pi.getIs_extend().equals("1")) {
                        extendplus = " " + pi.getUni_symbol();
                        extendvalue = pi.getTime_limit_extend();
                        extendunit = pi.getTime_limit_extend_unit();
                    }
                    String str = fir + pi.getTime_limit() + fend + extendplus;
                    String strre = str + extendvalue + extendunit;
                    SpannableString ss = new SpannableString(strre);
                    if (!CommonUtil.isNullOrEmpty(pi.getBid_status())
                            && (pi.getBid_status().equals("1")
                            && Math.abs(Long.parseLong(pi.getStart_bid_time_diff())) > 0)
                            || (pi.getBid_status().equals("2"))) {
                        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)),
                                fir.length(), pi.getTime_limit().length() + fir.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        if (pi.getIs_extend() != null && pi.getIs_extend().equals("1")) {
                            ss.setSpan(new ForegroundColorSpan(getResources()
                                            .getColor(R.color.blue)), str.length(),
                                    extendvalue.length() + str.length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    } else {
                        ss.setSpan(new ForegroundColorSpan(Color.rgb(0xc0, 0xc0, 0xc0)), 0,
                                strre.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (!CommonUtil.isNullOrEmpty(pi.getPrj_name_show())) {
                        vh.tv_prj_type.setText(pi.getPrj_name_show());
                    } else {
                        vh.tv_prj_type.setText(pi.getPrj_type_name() + "-" + pi.getPrj_name());
                    }

                    String year_rate = null;
                    SpannableString ssr = null;
                    String strr = null;

                    year_rate = pi.getYear_rate();
                    int i = year_rate.indexOf("+");
                    if (i != -1) {
                        String left = year_rate.substring(0, i);
                        String right = year_rate.substring(i + 1, year_rate.length());
                        float l = Float.valueOf(left);
                        float r = Float.valueOf(right);
                        year_rate = String.format("%.2f", l) + "+" + String.format("%.2f", r);
                    }

                    strr = year_rate + "%";
                    ssr = new SpannableString(strr);
                    int index = strr.indexOf(".");
                    ssr.setSpan(new AbsoluteSizeSpan(20, true), 0, index,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    SpannableString sst = null;
                    String strt = pi.getTime_limit() + pi.getTime_limit_unit_view();
                    sst = new SpannableString(strt);
                    sst.setSpan(new AbsoluteSizeSpan(12, true), pi.getTime_limit().length(),
                            strt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    DecimalFormat f = new DecimalFormat("0,000.00");
                    Double d = Double.parseDouble(f.parseObject(
                            pi.getMin_bid_amount_name()).toString());
                    BigDecimal bd = new BigDecimal(d);
                    BigDecimal bd2 = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
                    SpannableString ssm = null;
                    String strm = bd2 + "元起投";
                    ssm = new SpannableString(strm);
                    vh.content_tv_1.setText(ssr);

                    if (isVinvest) {
                        vh.title_tv_2.setText("理财金限额(元)");// practice_money
                        vh.content_tv_2.setText(pi.getPractice_money());
                    } else {
                        vh.title_tv_2.setText(pi.getRepay_way_view());
                        if (!StringUtils.isEmpty(homeFlag)) {
                            if (pi.getMax_bid_amount_view() == null
                                    || pi.getMax_bid_amount_view().equals("")
                                    || pi.getMax_bid_amount_view().equals("0.00")) {
                                vh.content_tv_2.setText(pi.getMin_bid_amount_name() + "+");
                            } else {
                                vh.content_tv_2.setText(pi.getMin_bid_amount_name() + "-"
                                        + pi.getMax_bid_amount_view());
                            }
                        } else {
                            if (pi.getMax_bid_amount_name().equals("0.00")) {
                                vh.content_tv_2.setText(pi.getMin_bid_amount_raw() + "+");
                            } else {
                                vh.content_tv_2.setText(pi.getMin_bid_amount_raw() + "-"
                                        + pi.getMax_bid_amount_raw());
                            }
                        }
                    }
                    vh.content_tv_2.setText(sst);
                    if (!"".equals(pi.getMin_bid_amount_name())) {
                        vh.content_tv_3.setText(ssm);
                    }

                    String remaining_prefix = pi.getRemaining_prefix();
                    String remaining_middle = pi.getRemaining_middle();
                    String remaining_suffix = pi.getRemaining_suffix();
                    String remaining = remaining_prefix + remaining_middle + remaining_suffix;
                    SpannableString remaind = new SpannableString(remaining);
                    remaind.setSpan(new AbsoluteSizeSpan(12, true), 0, remaining_prefix.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    remaind.setSpan(new AbsoluteSizeSpan(12, true),
                            remaining.length() - remaining_suffix.length(), remaining.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if (pi.pre_end != null && pi.pre_end.equals("1")) {

                    } else if (pi.getBid_status().equals("1")
                            && Math.abs(Long.parseLong(pi.getStart_bid_time())) != 0) {
                        TVIDTime itime = startTVMap.get(pi.getId());
                        if (itime != null) {
                            itime.tv = vh.tv_down_right;
                            itime.pi = pi;
                        }
                        if (!CommonUtil.isNullOrEmpty(pi.getIs_pre_sale()) &&
                                pi.getIs_pre_sale().equals("1")) {
                            vh.tv_state_info.setText("预售");
                            vh.tv_state_info.setTextColor(Color.rgb(0x30, 0x9b, 0xfc));
                            vh.rpisout.setProgresColor(Color.rgb(0x30, 0x9b, 0xfc));
                            vh.tv_down_right.setText(remaind);
                        } else {
                            vh.tv_down_right.setTextColor(getResources().getColor(color.color_3));
                            vh.tv_state_info.setText("待开标");
                            vh.tv_state_info.setTextColor(getResources().getColor(color.color_3));
                            vh.roundpi.setProgresColor(Color.rgb(0x68, 0x90, 0x56));
                            vh.rpisout.setProgresColor(Color.rgb(0x68, 0x90, 0x56));
                            vh.roundpi.setProgressStrokeWidth(2);
                            vh.rpisout.setProgressStrokeWidth(2);
                        }
                    } else if (pi.getBid_status().equals("2") || (Math.abs(Long.parseLong(pi
                            .getStart_bid_time_diff())) == 0 && pi
                            .getBid_status().equals("1"))) {
                        if (isVinvest) {
                            vh.tv_state_info.setText("抢标认购");
                        } else {
                            if (!StringUtils.isEmpty(homeFlag)) {
                                vh.tv_state_info.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                                vh.tv_state_info.setText("抢");
                                vh.tv_down_right.setText(remaind);
                            } else {
                                vh.tv_state_info.setText(pi.getSchedule() + "%");
                                vh.tv_down_right.setText(remaind);

                            }

                            vh.tv_state_info.setTextColor(Color.rgb(0xff, 0x93, 0x72));
                            vh.rpisout.setProgresColor(Color.rgb(0xff, 0x93, 0x72));
                        }
                    } else if ("5".equals(pi.getBid_status())) {
                        pi.setSchedule("0");
                        vh.tv_state_info.setText("已回款");
                        vh.tv_state_info.setTextColor(getResources().getColor(color.tv_default));
                        vh.tv_down_right.setTextColor(Color.rgb(0x99, 0x99, 0x99));// #999999
                        vh.tv_down_right.setText(pi.getDemand_amount_view());
                        vh.tv_prj_type.setTextColor(Color.rgb(0x99, 0x99, 0x99));
                        vh.content_tv_2.setTextColor(Color.rgb(0x99, 0x99, 0x99));
                        vh.roundpi.setVisibility(View.VISIBLE);
                        vh.rpisout.setVisibility(View.GONE);
                    } else if ("4".equals(pi.getBid_status())) {
                        pi.setSchedule("0");
                        vh.tv_state_info.setText("待回款");
                        vh.tv_state_info.setTextColor(getResources().getColor(color.tv_default));
                        vh.tv_down_right.setTextColor(Color.rgb(0x99, 0x99, 0x99));// #999999
                        vh.tv_down_right.setText(pi.getDemand_amount_view());
                        vh.tv_prj_type.setTextColor(Color.rgb(0x99, 0x99, 0x99));
                        vh.content_tv_2.setTextColor(Color.rgb(0x99, 0x99, 0x99));
                        vh.roundpi.setVisibility(View.VISIBLE);
                        vh.rpisout.setVisibility(View.GONE);
                    } else if ("3".equals(pi.getBid_status())) {
                        pi.setSchedule("0");
                        vh.tv_state_info.setText("已满标");
                        vh.tv_state_info.setTextColor(getResources().getColor(color.tv_default));
                        vh.tv_down_right.setTextColor(Color.rgb(0x99, 0x99, 0x99));// #999999
                        vh.tv_down_right.setText(pi.getDemand_amount_view());
                        vh.tv_prj_type.setTextColor(Color.rgb(0x99, 0x99, 0x99));
                        vh.content_tv_2.setTextColor(Color.rgb(0x99, 0x99, 0x99));
                        vh.roundpi.setVisibility(View.VISIBLE);
                        vh.rpisout.setVisibility(View.GONE);
                    } else if ("31".equals(pi.getBid_status())) {
                        pi.setSchedule("0");
                        vh.tv_state_info.setText("支付中");
                        vh.tv_down_right.setText(remaind);
                        vh.tv_state_info.setTextColor(getResources().getColor(color.tv_default));
                        vh.roundpi.setVisibility(View.VISIBLE);
                        vh.rpisout.setVisibility(View.GONE);
                    } else {
                        pi.setSchedule("0");
                        vh.tv_state_info.setText("已截标");
                        vh.tv_state_info.setTextColor(getResources().getColor(color.tv_default));

                        vh.tv_down_right.setTextColor(Color.rgb(0x99, 0x99, 0x99));// #999999
                        vh.tv_down_right.setText(pi.getDemand_amount_view());
                        vh.tv_prj_type.setTextColor(Color.rgb(0x99, 0x99, 0x99));
                        vh.content_tv_2.setTextColor(Color.rgb(0x99, 0x99, 0x99));
                        vh.roundpi.setVisibility(View.VISIBLE);
                        vh.rpisout.setVisibility(View.GONE);
                    }
                    if (pi.getBid_status().equals("1") && pi.getIs_pre_sale().equals("1")
                            && pi.getIs_sale_over().equals("1")) {
                        vh.rpisout.setProgressMovie(0);
                    } else if (pi.getBid_status().equals("1")
                            && pi.getIs_pre_sale().equals("1")) {
                        if (!StringUtils.isInteger(pi.getPre_sale_schedule()))
                            pi.setPre_sale_schedule("0");
                        vh.rpisout.setProgressMovie(Integer.parseInt(pi
                                .getPre_sale_schedule()));
                    } else {
                        if (!StringUtils.isInteger(pi.getSchedule()))
                            pi.setSchedule("0");
                        vh.rpisout.setProgressMovie(Integer.parseInt(pi.getSchedule()));
                    }
                } else {
                    View view = View.inflate(getActivity(), R.layout.loading_item, null);
                    loadData();
                    return view;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        public class ViewHolders {

            public TextView content_tv_3;
            public TextView title_tv_3;
            TextView tv_prj_type;
            ImageView iv_activity;
            TextView title_tv_1;
            TextView title_tv_2;
            TextView tv_state_info;
            TextView content_tv_1;
            TextView content_tv_2;
            ImageView iv_new;
            ImageView iv_prj_type;
            View v_prj_line;
            View v_place_holder;
            TextView tv_down_right;

            RoundProgressIndicator roundpi;
            RoundProgressIndicator rpisout;
        }
    }

    public class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onTick(long arg0) {
            try {
                TVIDTime iTime;
                for (String string : idList) {
                    iTime = startTVMap.get(string);

                    if (iTime == null)
                        continue;
                    if (iTime.diffTime > 0)
                        iTime.diffTime = iTime.diffTime - 1;
                    if (iTime.diffTime <= 0) {
                        try {
                            changeBidState(iTime);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        idList.remove(string);
                        startTVMap.remove(string);
                        continue;
                    }
                    try {
                        if (iTime.tv != null) {
                            iTime.tv.setText("倒计时:" + StringUtils.formatLong(iTime.diffTime));
                            iTime.tv.setTextSize(12);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    /**
     * 倒计时走完改变状态
     */
    public void changeBidState(TVIDTime iTime) {
        if (!iTime.isRepay) {
            iTime.pi.setBid_status("2");
        } else {
            iTime.pi.setIs_sale_over("1");
            iTime.pi.pre_end = "1";
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void callPayCheckBack(boolean success) {

    }

    @Override
    public void doInvestSuccess(boolean success) {

    }

    @Override
    public void onRefresh() {
        reLoadDataDown();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    public static class TVIDTime {
        long diffTime = 0;
        TextView tv;
        String prjid;
        FinanceTabItemProductItem pi;
        boolean isRepay = false;
    }

    @Override
    public void doInvestCheckSuccess(boolean success, FinanceInvestPBuyCheckData model) {
        if (success) {
            mymodel = model;
            pipw.dismiss();
            if (model.isRepay()) {
                pw.serverProtocol = model.getServer_protocol();
            }
            if (model.getIs_tips().equals("1")) {
                dialogPopupWindow.showWithMessage(model.getTips_error(), "7");
            } else {
                pw.goAnimPlusIncome(getActivity().findViewById(R.id.main), model);
            }
        }
    }

    @Override
    public void chooseItem(int markItem) {
        switch (markItem) {
            case 0:
                pw.goAnimPlusIncome(getActivity().findViewById(R.id.main), mymodel);
                break;
            default:
                break;
        }
    }

    @Override
    public void gainFinanceTabItemSuccess(FinanceTabItemJson response) {
        try {
            if (response != null) {
                if (response.getBoolen().equals("1")) {
                    if (isDown) {
                        isDown = false;
                        mAdapter.clear();
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();
                    }
                    switch_wap = response.getData().getSwitch_wap();
                    List<FinanceTabItemProductItem> nowList = StringUtils
                            .isEmpty(homeFlag) ? response.getData().getList()
                            : response.getData().getPrjList();
                    FinanceTabItemProductItem item = null;
                    hasMore = nowList.size() >= mPagesize;
                    if (hasMore)
                        if (StringUtils.isInteger(response.getData().getCurrentPage())) {
                            if (Integer.parseInt(response.getData()
                                    .getCurrentPage()) >= response.getData()
                                    .getTotalPages()) {
                                hasMore = false;
                            }
                        }
                    for (int j = 0; j < nowList.size(); j++) {
                        try {
                            item = nowList.get(j);
                            if (!StringUtils.isEmpty(homeFlag)) item.setHomeFlag("1");
                            mAdapter.add(item);
                            TVIDTime tvalue;
                            if (!CommonUtil.isNullOrEmpty(item.getBid_status()) && item.getBid_status().equals("1")) {
                                tvalue = new TVIDTime();
                                idList.add(item.getId());
                                tvalue.prjid = item.getId();
                                tvalue.diffTime = Long.parseLong(item.getStart_bid_time_diff());
                                if (!CommonUtil.isNullOrEmpty(item.getIs_pre_sale()) &&
                                        item.getIs_pre_sale().equals("1")) {
                                    tvalue.isRepay = true;
                                }
                                startTVMap.put(item.getId(), tvalue);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    hasMore = false;
                    if (response.getBoolen().trim().equals("0")) {
                        GoLoginUtil.BaseToLoginActivity(getActivity());
                    }
                    ToastUtil.showToast(getActivity(), response.getMessage());
                }
                if (mAdapter.mfilelist.size() <= 0 && !(response.getBoolen() != null
                        && response.getBoolen().equals("0"))) {
                    mContent.findViewById(R.id.listView).setVisibility(View.GONE);
                    mContent.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
                    Intent intent = new Intent(BroadCastImpl.ACTION_FINANCE_SEARCH_NO_DATA);
                    getActivity().sendBroadcast(intent);
                } else {
                    mContent.findViewById(R.id.rl_empty).setVisibility(View.GONE);
                    mContent.findViewById(R.id.listView).setVisibility(View.VISIBLE);
                }
            } else {
                hasMore = false;
            }
        } catch (Exception e) {
            hasMore = false;
            e.printStackTrace();
        } finally {
            mAdapter.notifyDataSetChanged();
            isRundata = false;
        }
    }

    @Override
    public void gainFinanceTabItemFail() {
        ToastUtil.showToast(FinanceTabItemFragment.this.getActivity(), "网络不给力!");
        isRundata = false;
    }
}