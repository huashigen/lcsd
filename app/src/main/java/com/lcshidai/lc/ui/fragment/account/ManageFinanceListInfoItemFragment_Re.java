package com.lcshidai.lc.ui.fragment.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.PIPWCallBack;
import com.lcshidai.lc.impl.PrjCountImpl;
import com.lcshidai.lc.impl.account.CancelCashImpl;
import com.lcshidai.lc.impl.account.ManageFinanceListInfoItemImpl;
import com.lcshidai.lc.impl.transfer.CashCheckAddImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.ManageFinanceListInfo;
import com.lcshidai.lc.model.account.ManageFinanceListInfoItem;
import com.lcshidai.lc.model.account.ManageFinanceListInfoItemJson;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckData;
import com.lcshidai.lc.model.transfer.CashCheckAddData;
import com.lcshidai.lc.model.transfer.CashCheckAddJson;
import com.lcshidai.lc.service.account.HttpCancelCashService;
import com.lcshidai.lc.service.account.HttpManageFinanceListInfoItemService;
import com.lcshidai.lc.service.transfer.HttpCashCheckAddService;
import com.lcshidai.lc.ui.AgreementActivity;
import com.lcshidai.lc.ui.account.ManageFinanceListProtocol;
import com.lcshidai.lc.ui.account.RepaymentListActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.ui.transfer.CashActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.GoClassUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.TestLogin;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow;
import com.lcshidai.lc.widget.quickaction.QuickAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 投资记录 变现记录
 *
 * @author 000814
 */
public class ManageFinanceListInfoItemFragment_Re extends TRJFragment implements
        ManageFinanceListInfoItemImpl, PIPWCallBack, CancelCashImpl,
        CashCheckAddImpl {
    HttpManageFinanceListInfoItemService hmfliis;
    HttpCashCheckAddService hccas;
    HttpCancelCashService hcccs;
    public PrjCountImpl prjCi;

    View contextView;
    ListView mListView;
    View mProgressBar;
    ImageView parr, darr;
    private int mPagesize = 10;
    private int mP = 1;
    private int mP1 = 1;
    private int mP2 = 1;

    boolean hasMore;
    private TextView prjAccount;

    public String mType = "0";
    public String mStatus = "0";

    public boolean isTransfer;
    public boolean isFreshdown = false;
    PayPasswordPopupWindow pw;

    public boolean isRepay = false;

    public boolean isPay = true;

    public boolean isDel = true;
    public String mCurrentPType = "0";
    TextView back_time, make_time;
    public String paysc = null;
    public String delsc = null;


    public ItemAdapter mAdapter;
    public ItemTransferAdapter mTransferAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hccas = new HttpCashCheckAddService((TRJActivity) getActivity(), this);
        hcccs = new HttpCancelCashService((TRJActivity) getActivity(), this);
        hmfliis = new HttpManageFinanceListInfoItemService((TRJActivity) getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contextView = inflater.inflate(R.layout.fragment_account_invest_records_info,
                container, false);
        mListView = (ListView) contextView.findViewById(R.id.listView);
        TextView text = new TextView(getActivity());
        text.setHeight(10);
        text.setBackgroundResource(R.color.gray_bg);
        mListView.addHeaderView(text);
        mProgressBar = contextView.findViewById(R.id.progressContainer);
        prjAccount = (TextView) contextView.findViewById(R.id.prjaccount);
        parr = (ImageView) contextView.findViewById(R.id.parr);
        darr = (ImageView) contextView.findViewById(R.id.darr);
        pw = new PayPasswordPopupWindow((TRJActivity) getActivity(), this);
        if (isTransfer) {
            mTransferAdapter = new ItemTransferAdapter(getActivity());
            mListView.setAdapter(mTransferAdapter);
        } else {
            mAdapter = new ItemAdapter(getActivity());
            mListView.setAdapter(mAdapter);
        }
        // mListView.addHeaderView(headerView);
        qa = new QuickAction(getActivity());
        qa.dismissView();
        back_time = (TextView) contextView.findViewById(R.id.back_time);
        make_time = (TextView) contextView.findViewById(R.id.make_time);

        back_time.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                if (isPay) {
                    paysc = "desc";
                    delsc = "asc";
                    isPay = false;
                    mP = 1;
                    parr.setBackground(getResources().getDrawable(R.drawable.img_arrow_bottom));
                    hmfliis.gainManageFinanceListInfoItem(mCurrentPType, mType,
                            mP++, mPagesize, isTransfer, mStatus, paysc, delsc);

                } else {
                    paysc = "asc";
                    delsc = "desc";
                    isPay = true;
                    mP = 1;
                    parr.setBackground(getResources().getDrawable(R.drawable.img_arrow_top));
                    hmfliis.gainManageFinanceListInfoItem(mCurrentPType, mType,
                            mP++, mPagesize, isTransfer, mStatus, paysc, delsc);
                }

            }
        });

        make_time.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                if (isDel) {
                    paysc = "desc";
                    delsc = "asc";
                    isDel = false;
                    darr.setBackground(getResources().getDrawable(R.drawable.img_arrow_bottom));
                    mP = 1;
                    hmfliis.gainManageFinanceListInfoItem(mCurrentPType, mType, mP++, mPagesize,
                            isTransfer, mStatus, paysc, delsc);
                } else {
                    paysc = "asc";
                    delsc = "desc";
                    isDel = true;
                    mP = 1;
                    darr.setBackground(getResources().getDrawable(R.drawable.img_arrow_top));
                    hmfliis.gainManageFinanceListInfoItem(mCurrentPType, mType, mP++, mPagesize,
                            isTransfer, mStatus, paysc, delsc);

                }
            }
        });
        return contextView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        if (MemorySave.MS.isCashSuccessBack) {
            MemorySave.MS.isCashSuccessBack = false;
            prjCi.prjCount();
        }
    }

    public void loadData() {
        hasMore = false;
        mListView.setVisibility(View.GONE);
        if (isTransfer) {
            mTransferAdapter.clear();
            mTransferAdapter.notifyDataSetChanged();
        } else {
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
        }
        mP = 1;
        mProgressBar.setVisibility(View.VISIBLE);
        loadMore();
    }

    private void loadMore() {
        hmfliis.gainManageFinanceListInfoItem(mCurrentPType, mType, mP++, mPagesize,
                isTransfer, mStatus, paysc, delsc);
    }

    QuickAction qa;

    public class ItemAdapter extends ArrayAdapter<ManageFinanceListInfoItem> {
        private Context mContext;
        public List<ManageFinanceListInfoItem> mfilelist = new ArrayList<ManageFinanceListInfoItem>();
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
        public void add(ManageFinanceListInfoItem jobj) {
            mfilelist.add(jobj);
        }

        @Override
        public void addAll(Collection<? extends ManageFinanceListInfoItem> collection) {
            mfilelist.addAll(collection);
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
        public ManageFinanceListInfoItem getItem(int position) {
            return mfilelist.get(position);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolders vh;
            if (position < size) {
                vh = new ViewHolders();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.account_invest_records_item_1, null);
                vh.iv_series = (TextView) convertView.findViewById(R.id.iv_series);
                vh.tv_prj_name = (TextView) convertView.findViewById(R.id.tv_prj_name);
                vh.im_transfer = (ImageView) convertView.findViewById(R.id.im_transfer);
                vh.im_transfer_cn = (ImageView) convertView.findViewById(R.id.im_transfer_cn);
                vh.im_new = (ImageView) convertView.findViewById(R.id.im_new);
                vh.im_bianxian = (ImageView) convertView.findViewById(R.id.im_can_duixian);
                vh.iv_activity = (ImageView) convertView.findViewById(R.id.iv_activity);
                vh.iv_limit = (ImageView) convertView.findViewById(R.id.iv_limit);
                vh.iv_phone_spec = (ImageView) convertView.findViewById(R.id.iv_phone_spec);
                vh.content_tv_1 = (TextView) convertView.findViewById(R.id.content_tv_1);
                vh.content_tv_2 = (TextView) convertView.findViewById(R.id.content_tv_2);
                vh.content_tv_3 = (TextView) convertView.findViewById(R.id.content_tv_3);
                vh.tv_date_next_repayment = (TextView) convertView.findViewById(R.id.tv_date_next_repayment);
                vh.btn_view_arg = (Button) convertView.findViewById(R.id.btn_view_arg);
                vh.btn_repay_list = (Button) convertView.findViewById(R.id.btn_repay_list);
                vh.tv_content = (TextView) convertView.findViewById(R.id.tv_top_content);
                /** 新增的 */
                vh.tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
                vh.tv_time_limit_view = (TextView) convertView.findViewById(R.id.tv_time_limit_view);
                vh.tv_worth_view = (TextView) convertView.findViewById(R.id.tv_worth_view);
                vh.tv_last_worth_view = (TextView) convertView.findViewById(R.id.tv_last_worth_view);
                vh.ll_worth_view = (LinearLayout) convertView.findViewById(R.id.ll_worth_view);
                vh.tv_reward_type_tips = (TextView) convertView.findViewById(R.id.tv_reward_type_tips);
                vh.tv_reward_money_rate = (TextView) convertView.findViewById(R.id.tv_reward_money_rate);
                vh.tv_jxq_reward_tips = (TextView) convertView.findViewById(R.id.tv_jxq_reward_tips);

                convertView.setTag(vh);
                final ManageFinanceListInfoItem item = mfilelist.get(position);

                String typeDis = item.getPrj_type_display();
                if (!StringUtils.isEmpty(typeDis)) {
                    typeDis += "-";
                } else {
                    typeDis = "";
                }
                vh.tv_prj_name.setText(typeDis + item.getPrj_name());
                String rewardTips = item.getReward_type_tips();
                if (!StringUtils.isEmpty(rewardTips)) {
                    vh.tv_reward_type_tips.setVisibility(View.VISIBLE);
                    vh.tv_reward_type_tips.setText("(" + rewardTips + ")");
                } else {
                    vh.tv_reward_type_tips.setVisibility(View.GONE);
                }
                if (item.getJxq_rate_view().equals("%")) {
                    vh.tv_jxq_reward_tips.setVisibility(View.GONE);
                } else {
                    vh.tv_jxq_reward_tips.setVisibility(View.VISIBLE);
                    vh.tv_jxq_reward_tips.setText("(使用" + item.getJxq_rate_view() + "加息券)");
                }
                String series = item.getPrj_series();
                if (!CommonUtil.isNullOrEmpty(series)) {
                    if (series.equals("1")) {
                        vh.iv_series.setText("时时赚");
                    } else if (series.equals("2")) {
                        vh.iv_series.setText("小时贷");
                    } else if (series.equals("3")) {
                        vh.iv_series.setText("企额贷");
                    }
                }

                if (isRepay) {
                    vh.content_tv_1.setText(item.getMoney_show());
                    vh.content_tv_2.setText(item.getTime_limit_show());
                    vh.content_tv_3.setText(item.getProfit_show());
                    if (item.getBuy_time_show() != null) {
                        vh.tv_top_content_right.setText(item.getBuy_time_show());
                    }
                    vh.tv_content.setText(item.getCtime_show());
                    // vh.btn_view_arg.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_green_submit_bg));
                    vh.btn_view_arg.setBackground(getResources().getDrawable(R.drawable.img_plan_sel));
                    vh.btn_view_arg.setTextColor(getResources().getColor(R.color.text_color));
                    vh.btn_view_arg.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (item.getStatus().equals("3")) {
                                Intent intent = new Intent(getActivity(), AgreementActivity.class);
                                intent.putExtra("title", "服务协议");
                                intent.putExtra("url", item.getServer_protocol());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent();
                                intent.putExtra("server_protocol", item.getServer_protocol());
                                intent.putExtra("pre_protocol", item.getPre_protocol());
                                intent.setClass(getActivity(), ManageFinanceListProtocol.class);
                                getActivity().startActivity(intent);
                            }
                        }
                    });
                } else {
                    if (item.getIs_have_contract().equals("1")) {
                        // vh.btn_view_arg.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_green_submit_bg));
                        vh.btn_view_arg.setBackground(getResources().getDrawable(R.drawable.img_plan_sel));
                        vh.btn_view_arg.setTextColor(getResources().getColor(R.color.text_color));
                        // vh.iv_pre_sale.setVisibility(View.INVISIBLE);
                        vh.btn_view_arg.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, AgreementActivity.class);
                                intent.putExtra("title", "借款合同");
                                intent.putExtra("id", item.getOrder_id());
                                intent.putExtra("url", item.getServer_protocol());
                                startActivity(intent);
                            }
                        });
                    } else {
                        if (item.getIs_pre_sale().equals("1")) {
                            // vh.iv_pre_sale.setVisibility(View.VISIBLE);
                            // vh.btn_view_arg.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_green_submit_bg));
                            vh.btn_view_arg.setBackground(getResources().getDrawable(R.drawable.img_plan_sel));
                            vh.btn_view_arg.setTextColor(getResources().getColor(R.color.text_color));
                            vh.btn_view_arg.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, AgreementActivity.class);
                                    intent.putExtra("title", "服务协议");
                                    intent.putExtra("url", item.getServer_protocol());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            // vh.iv_pre_sale.setVisibility(View.INVISIBLE);
                            vh.btn_view_arg.setOnClickListener(null);
                            // vh.btn_view_arg.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_sub_gray));
                            vh.btn_view_arg.setBackground(getResources().getDrawable(R.drawable.img_plan_def));
                            vh.btn_view_arg.setTextColor(getResources().getColor(R.color.text_color_1));
                        }
                    }

                    vh.tv_content.setText(item.getDate_deal());
                    vh.content_tv_3.setText(item.getRepay_way_display());
                    vh.content_tv_2.setText(item.getIncoming());
                    vh.content_tv_1.setText(item.getMoney());
                    String moneyRate = item.getReward_money_rate();
                    if (StringUtils.isEmpty(moneyRate)) {
                        vh.tv_reward_money_rate.setVisibility(View.GONE);
                    } else {
                        String str = "含" + moneyRate + "%奖励";
                        vh.tv_reward_money_rate.setText(str);
                        vh.tv_reward_money_rate.setVisibility(View.VISIBLE);
                    }
                    String rate = item.getRate();
                    vh.tv_rate.setText(rate.substring(0, rate.length() - 1));
                    String timeLimit = item.getTime_limit_view();
                    vh.tv_time_limit_view.setText(timeLimit);
                    if (item.getIcon_activity() != null && item.getIcon_activity().equals("1")) {
                        vh.iv_activity.setVisibility(View.VISIBLE);
                        if (item.getExt() == null || item.getExt().getIcon() == null || item.getExt().getIcon().equals("")) {
                            vh.iv_activity.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_icon));
                        } else {
                            vh.iv_activity.setBackgroundDrawable(null);
                            Glide.with(mContext).load(item.getExt().getIcon()).into(vh.iv_activity);
                        }
                    } else {
                        vh.iv_activity.setVisibility(View.GONE);
                    }
                }
                vh.im_transfer.setVisibility(View.GONE);
                //
                if (item.getCan_bianxian() != null && item.getCan_bianxian().equals("1")) {
                    vh.im_bianxian.setVisibility(View.VISIBLE);
                } else {
                    vh.im_bianxian.setVisibility(View.GONE);
                }

                String repayMent = item.getDate_next_repayment();
                String re = repayMent.substring(0, 10) + " " + repayMent.substring(10, repayMent.length());
                vh.tv_date_next_repayment.setText(re);
                int start = Integer.parseInt(item.getBid_status());
                if (item.getIs_float().equals("1") && start > 3) {
                    vh.ll_worth_view.setVisibility(View.VISIBLE);
                    String worth = item.getWorth_view();
                    String lastWorth = item.getLast_worth_view();
                    if (StringUtils.isEmpty(worth)) {
                        worth = "0";
                    }
                    if (StringUtils.isEmpty(lastWorth)) {
                        lastWorth = "0";
                    }
                    vh.tv_worth_view.setText(worth);
                    vh.tv_last_worth_view.setText(lastWorth);
                } else {
                    vh.ll_worth_view.setVisibility(View.GONE);
                }
                if (item.getIs_have_repayplan() != null && item.getIs_have_repayplan().equals("1")) {
                    // vh.btn_repay_list.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_green_submit_bg));
                    vh.btn_repay_list.setBackground(getResources().getDrawable(R.drawable.img_plan_sel));
                    vh.btn_repay_list.setTextColor(getResources().getColor(R.color.text_color));
                    vh.btn_repay_list.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), RepaymentListActivity.class);
                            intent.putExtra("id", item.getOrder_id());
                            startActivity(intent);
                        }
                    });
                } else {
                    vh.btn_repay_list.setBackground(getResources().getDrawable(R.drawable.img_plan_def));
                    vh.btn_repay_list.setTextColor(getResources().getColor(R.color.text_color_1));
                    vh.btn_repay_list.setOnClickListener(null);
                }

                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();
                        if (isRepay) {
                            args.putString("prj_id", item.getId());
                        } else {
                            args.putString("prj_id", item.getPrj_id());
                        }
                        args.putString("canInvest", item.getCanInvest());
                        GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
                        GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
                        MemorySave.MS.args = args;
                        new TestLogin(getActivity()).testIt((TRJActivity) getActivity());
                    }
                });
            } else {
                View view = View.inflate(getActivity(), R.layout.loading_item, null);
                //isTransfer = true;
                isFreshdown = true;
                loadMore();
                return view;
            }
            return convertView;

        }

    }

    private SpannableString getClickSpan(OnClickListener listener, String str,
                                         int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new MyURLSpan(listener), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private class MyURLSpan extends ClickableSpan {
        private OnClickListener listener;

        MyURLSpan(OnClickListener lis) {
            this.listener = lis;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#666666")); // 改变颜色
            ds.setUnderlineText(false); // 去除下划线
        }

        @Override
        public void onClick(View widget) {
            listener.onClick(widget);
        }
    }

    public class ItemTransferAdapter extends ArrayAdapter<ManageFinanceListInfoItem> {
        private Context mContext;
        public List<ManageFinanceListInfoItem> mfilelist = new ArrayList<ManageFinanceListInfoItem>();
        public int size;

        public ItemTransferAdapter(Activity context) {
            super(context, 0);
            mContext = context;
        }

        @Override
        public int getCount() {
            size = mfilelist.size();
            return size + (hasMore ? 1 : 0);
        }

        @Override
        public void add(ManageFinanceListInfoItem jobj) {
            mfilelist.add(jobj);
        }

        @Override
        public void addAll(
                Collection<? extends ManageFinanceListInfoItem> collection) {
            mfilelist.addAll(collection);
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
        public ManageFinanceListInfoItem getItem(int position) {
            return mfilelist.get(position);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolderps vh;
            if (position < size) {
                // if(convertView==null||convertView.getTag()==null){
                vh = new ViewHolderps();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.account_cash_item, null);
                vh.tv_prj_type = (TextView) convertView.findViewById(R.id.tv_prj_type);
                vh.tv_prj_name = (TextView) convertView.findViewById(R.id.tv_prj_name);
                vh.im_bianxian = (ImageView) convertView.findViewById(R.id.im_can_duixian);
                vh.im_transfer = (ImageView) convertView.findViewById(R.id.im_transfer);
                vh.im_transfer_cn = (ImageView) convertView.findViewById(R.id.im_transfer_cn);
                vh.im_new = (ImageView) convertView.findViewById(R.id.im_new);
                vh.iv_activity = (ImageView) convertView.findViewById(R.id.iv_activity);
                vh.iv_limit = (ImageView) convertView.findViewById(R.id.iv_limit);
                vh.content_tv_1 = (TextView) convertView.findViewById(R.id.content_tv_1);
                vh.content_tv_2 = (TextView) convertView.findViewById(R.id.content_tv_2);
                vh.content_tv_4 = (TextView) convertView.findViewById(R.id.content_tv_4);
                vh.content_tv_5 = (TextView) convertView.findViewById(R.id.content_tv_5);
                vh.iv_phone_spec = (ImageView) convertView.findViewById(R.id.iv_phone_spec);
                vh.tv_date_next_repayment = (TextView) convertView.findViewById(R.id.tv_date_next_repayment);
                vh.btn_view_arg = (Button) convertView.findViewById(R.id.btn_view_arg);
                vh.btn_repay_list = (Button) convertView.findViewById(R.id.btn_repay_list);
                vh.btn_transfer = (Button) convertView.findViewById(R.id.btn_transfer);
                vh.ll_nextview = convertView.findViewById(R.id.ll_nextview);
                vh.ll_content = convertView.findViewById(R.id.ll_topc);
                vh.tv_content = (TextView) convertView.findViewById(R.id.tv_top_content);
                vh.iv_blue_flag = (ImageView) convertView.findViewById(R.id.iv_blue_flag);
                vh.tv_top_1 = (TextView) convertView.findViewById(R.id.tv_top_1);
                vh.title_tv_1 = (TextView) convertView.findViewById(R.id.title_tv_1);
                vh.title_tv_2 = (TextView) convertView.findViewById(R.id.title_tv_2);
                vh.title_tv_4 = (TextView) convertView.findViewById(R.id.title_tv_4);
                vh.title_tv_5 = (TextView) convertView.findViewById(R.id.title_tv_5);
                vh.pb = (ProgressBar) convertView.findViewById(R.id.pb);
                convertView.setTag(vh);
                // }else{
                // vh=(ViewHolderps) convertView.getTag();
                // }
                final ManageFinanceListInfoItem item = mfilelist.get(position);
                if (item.getType().equals("1") || item.getType().equals("3")) {
                    vh.tv_top_1.setText("成交日期:");
                } else {
                    vh.tv_top_1.setText("发布日期:");
                }
                vh.tv_content.setText(item.getOrder_mtime());

                vh.content_tv_1.setText(item.getRate());
                vh.content_tv_2.setText(item.getCurr_time_limit());

                if (item.getType().equals("1")) {
                    vh.title_tv_4.setText("可变现次数");
                    vh.title_tv_5.setText("可变现额度(元)");
                    vh.content_tv_4.setText(item.getLeft_cash_times());
                    vh.content_tv_5.setText(item.getCan_money());
                } else {
                    if (item.getType().equals("3")) {
                        vh.title_tv_4.setText("实际变现金额(元)");
                        vh.title_tv_5.setText("收益额度");
                        vh.content_tv_4.setText(item.getReal_cash_money());
                        vh.content_tv_5.setText(item.getIncome());
                    } else {
                        vh.title_tv_4.setText("变现金额(元)");
                        vh.title_tv_5.setText("实际变现金额(元)");
                        vh.content_tv_4.setText(item.getPlan_cash_money());
                        vh.content_tv_5.setText(item.getReal_cash_money());
                    }
                }

                if (!StringUtils.isInteger(item.getSchedule()))
                    item.getSchedule().equals("0");
                vh.pb.setProgress(Integer.parseInt(item.getSchedule()));
                vh.tv_date_next_repayment.setText(item.getExpect_repay_time());
                vh.tv_prj_name.setText(item.getPrj_name());

                if (item.getIcon_activity() != null && item.getIcon_activity().equals("1")) {
                    vh.iv_activity.setVisibility(View.VISIBLE);
                    if (item.getExt() == null || item.getExt().getIcon() == null || item.getExt().getIcon().equals("")) {
                        vh.iv_activity.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_icon));
                    } else {
                        vh.iv_activity.setBackgroundDrawable(null);
                        Glide.with(mContext).load(item.getExt().getIcon()).into(vh.iv_activity);
                    }
                } else {
                    vh.iv_activity.setVisibility(View.GONE);
                }
                if (item.getIs_view().equals("1")) {
                    // vh.btn_view_arg.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_green_submit_bg));
                    vh.btn_view_arg.setBackground(getResources().getDrawable(R.drawable.img_plan_sel));
                    vh.btn_view_arg.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (item.getProtcol_url() != null) {
                                try {
                                    ManageFinanceListInfo jObj = item.getProtcol_url();
                                    String fun = ((TRJActivity) getActivity()).resolveString(jObj.getFun(), "remaining_amount", "", "");
                                    String name = ((TRJActivity) getActivity()).resolveString(jObj.getName(), "remaining_amount", "", "");
                                    String type = ((TRJActivity) getActivity()).resolveString(jObj.getType(), "remaining_amount", "", "");
                                    String id = ((TRJActivity) getActivity()).resolveString(jObj.getId(), "remaining_amount", "", "");
                                    Intent intent = new Intent(getActivity(), AgreementActivity.class);
                                    intent.putExtra("intent_flag", 1);
                                    intent.putExtra("title", "");
                                    intent.putExtra("fun", fun);
                                    intent.putExtra("name", name);
                                    intent.putExtra("type", type);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                } else {
                    vh.btn_view_arg.setOnClickListener(null);
                    vh.btn_view_arg.setBackground(getResources().getDrawable(R.drawable.img_plan_def));
                    vh.btn_view_arg.setTextColor(getResources().getColor(R.color.text_color_1));
                    // vh.btn_view_arg.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_cancel));
                }

                if (item.getType().equals("1")) {
                    vh.btn_transfer.setText("立即变现");
                    vh.btn_transfer.setVisibility(View.VISIBLE);
                    vh.btn_transfer.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            cashCheck(item.getPrj_order_id(), "0", "0", item.getPrj_name());
                        }
                    });
                } else if (item.getType().equals("2")) {
                    vh.btn_transfer.setText("取消变现");
                    vh.btn_transfer.setVisibility(View.VISIBLE);
                    vh.btn_transfer.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            cancelCheck(item.getFid());
                        }
                    });
                } else {
                    vh.btn_transfer.setVisibility(View.GONE);
                }

                if (item.getIs_have_repayplan().equals("1")) {
                    // vh.btn_repay_list.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_green_submit_bg));
                    vh.btn_repay_list.setBackground(getResources().getDrawable(
                            R.drawable.img_plan_sel));
                    vh.btn_repay_list.setTextColor(getResources().getColor(
                            R.color.text_color));
                    vh.btn_repay_list.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(),
                                    RepaymentListActivity.class);
                            intent.putExtra("id", item.getPrj_order_id());
                            startActivity(intent);
                        }
                    });
                } else {
                    // vh.btn_repay_list.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_cancel));
                    vh.btn_repay_list.setBackground(getResources().getDrawable(
                            R.drawable.img_plan_def));
                    vh.btn_repay_list.setTextColor(getResources().getColor(
                            R.color.text_color_1));
                    vh.btn_repay_list.setOnClickListener(null);
                }

                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();
                        args.putString("prj_id", item.getPrj_id());
                        GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
                        GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
                        MemorySave.MS.args = args;
                        new TestLogin(getActivity()).testIt((TRJActivity) getActivity());
                    }
                });
            } else {
                View view = View.inflate(getActivity(), R.layout.loading_item,
                        null);
                loadMore();
                // new
                // ListViewUtility().setListViewHeightBasedOnChildren(mListView);
                return view;
            }
            return convertView;
        }

    }

    public class ViewHolderps {
        TextView tv_prj_type;
        TextView tv_prj_name;
        TextView tv_date_next_repayment;
        View ll_nextview;

        ImageView im_transfer;
        ImageView im_transfer_cn;
        ImageView im_new;
        ImageView iv_activity;
        ImageView iv_limit;
        ImageView iv_phone_spec;
        ImageView im_bianxian;

        TextView content_tv_1;
        TextView content_tv_2;
        TextView content_tv_4;
        TextView content_tv_5;
        TextView tv_nv;

        Button btn_view_arg;
        Button btn_repay_list;
        Button btn_transfer;

        View ll_content;
        TextView tv_content;
        ImageView iv_blue_flag;
        TextView tv_top_1;

        TextView title_tv_1;
        TextView title_tv_2;
        TextView title_tv_4;
        TextView title_tv_5;

        ProgressBar pb;
    }

    public class ViewHolders {

        TextView iv_series;
        // TextView tv_prj_money;
        TextView tv_worth_view, tv_last_worth_view;
        LinearLayout ll_worth_view;
        TextView tv_top_content_right;
        // TextView title_tv_3;
        // TextView repay_status;
        // TextView tv_prj_type;
        TextView tv_prj_name;
        TextView tv_date_next_repayment;
        View ll_nextview;

        ImageView im_transfer;
        ImageView im_transfer_cn;
        ImageView im_new;
        ImageView iv_activity;
        ImageView iv_limit;
        ImageView iv_phone_spec;
        ImageView im_bianxian;

        TextView content_tv_1;
        TextView content_tv_2;
        TextView content_tv_3;
        TextView content_tv_4;
        TextView content_tv_5;
        TextView content_tv_6;
        // TextView content_tv_7;
        // TextView content_tv_8;
        // TextView tv_nv;

        Button btn_view_arg;
        Button btn_repay_list;
        // Button btn_transfer;

        // View ll_content;
        TextView tv_content;
        // ImageView iv_blue_flag;
        TextView tv_top_1;

        // TextView title_tv_1;
        // TextView title_tv_2;
        TextView title_tv_4;
        TextView title_tv_5;
        TextView title_tv_6;

        // ImageView title_tv_2_img;
        // ImageView iv_pre_sale;
        // ProgressBar pb;

        // 新增
        TextView tv_rate;
        TextView tv_time_limit_view;
        TextView tv_reward_type_tips; // 是否使用满减券
        TextView tv_reward_money_rate; // 包含奖励率
        //added by randy 2017-02-13
        TextView tv_jxq_reward_tips;//加息券使用提示

        TextView back_time;
        TextView make_time;
    }

    @Override
    public void callPayCheckBack(boolean success) {
        if (success) {
            loadData();
        }
    }

    @Override
    public void doInvestSuccess(boolean success) {
        if (success) {
            loadData();
            if (prjCi != null)
                prjCi.prjCount();
        }
    }

    /**
     * 变现check
     *
     * @param prj_order_id 订单的ID
     * @param cash_money   投资的金额
     * @param cash_rate    利率
     */
    private void cashCheck(final String prj_order_id, String cash_money, String cash_rate, final String prjName) {
        // hccs.gainCashCheck(prj_order_id, cash_money,cash_rate,prjName);
        hccas.gainCashCheckAddResult(prj_order_id, cash_money, cash_rate);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 变现check
     *
     * @param prj_order_id 订单的ID
     */
    private void cancelCheck(final String prj_order_id) {
        mProgressBar.setVisibility(View.VISIBLE);
        hcccs.gainCancelCash(prj_order_id);
    }

    @Override
    public void gainManageFinanceListInfoItemsuccess(ManageFinanceListInfoItemJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    List<ManageFinanceListInfoItem> jarray = response.getData().getList();
                    prjAccount.setText(response.getData().getPage().getTotal());
                    // mAdapter.clear();
                    if (jarray != null) {
                        hasMore = jarray.size() >= mPagesize;
                        if (isFreshdown) {
                            if (hasMore)
                                if (StringUtils.isInteger(response.getData().getPage().getTotal_page()) &&
                                        StringUtils.isInteger(response.getData().getPage().getCurrent_page())) {
                                    int count = Integer.parseInt(response.getData().getPage().getTotal_page());
                                    if (Integer.parseInt(response.getData().getPage().getCurrent_page()) >= count) {
                                        hasMore = false;
                                    }
                                } else {
                                    hasMore = false;
                                }

                            mAdapter.addAll(jarray);
                        } else {
                            if (hasMore)
                                if (response.getData().getPage() != null && StringUtils.isInteger(response.getData().getPage().getTotal_page())
                                        && StringUtils.isInteger(response.getData().getPage().getCurrent_page())) {
                                    int count = Integer.parseInt(response.getData().getPage().getTotal_page());
                                    if (Integer.parseInt(response.getData().getPage().getCurrent_page()) >= count) {
                                        hasMore = false;
                                    }
                                } else {
                                    hasMore = false;
                                }
                            mAdapter.clear();
                            mAdapter.addAll(jarray);
                        }
                    }
                } else {
                    hasMore = false;
                    String message = response.getMessage();
                    ToastUtil.showToast(getActivity(), message);
                }
                if (isFreshdown) {
                    if (mAdapter.mfilelist.size() <= 0 && !(response.getLogined() != null && response.getLogined().equals("0"))) {
                        contextView.findViewById(R.id.listView).setVisibility(View.GONE);
                        contextView.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
                    } else {
                        contextView.findViewById(R.id.rl_empty).setVisibility(View.GONE);
                        contextView.findViewById(R.id.listView).setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mAdapter.mfilelist.size() <= 0 && !(response.getLogined() != null && response.getLogined().equals("0"))) {
                        contextView.findViewById(R.id.listView).setVisibility(View.GONE);
                        contextView.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
                    } else {
                        contextView.findViewById(R.id.rl_empty).setVisibility(View.GONE);
                        contextView.findViewById(R.id.listView).setVisibility(View.VISIBLE);
                    }
                }

            } else {
                hasMore = false;
            }
        } catch (Exception e) {
            hasMore = false;
            e.printStackTrace();
        } finally {
            mProgressBar.setVisibility(View.GONE);
            if (isFreshdown) {
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter.notifyDataSetChanged();
            }
            isFreshdown = false;
        }
    }

    @Override
    public void gainManageFinanceListInfoItemfail() {
        ToastUtil.showToast(getActivity(), "网络不给力!");
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void doInvestCheckSuccess(boolean success, FinanceInvestPBuyCheckData model) {

    }

    @Override
    public void gainCancelCashsuccess(BaseJson response) {
        try {
            // JSONObject jobj=response.optJSONObject("data");
            String boolen = response.getBoolen();
            if (boolen.equals("1")) {
                ToastUtil.showToast(getActivity(), "取消成功!");
                doInvestSuccess(true);
            }
            // mAdapter.clear();
            // setView(item);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void gainCancelCashfail() {
        ToastUtil.showToast(getActivity(), "网络不给力!");
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void gainCashCheckAddSuccess(CashCheckAddJson response) {
        try {
            CashCheckAddData jobj = response.getData();
            Intent intent = new Intent();
            // intent.putExtra("ChshItem", item);
            intent.putExtra("prj_name", jobj.getOriginal_show_prj_name());
            intent.putExtra("prj_order_id", jobj.prj_order_id);
            intent.putExtra("prj_id", jobj.getPrj_id());
            intent.putExtra("rate_tips", jobj.getRate_tips());
            intent.putExtra("balance_rate", jobj.getBalance_rate());
            intent.putExtra("max_cash_money", jobj.getMax_cash_money());
            intent.setClass(getActivity(), CashActivity.class);
            getActivity().startActivity(intent);
            // setView(item);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void gainCashCheckAddFail() {
        ToastUtil.showToast(getActivity(), "网络不给力!");
        mProgressBar.setVisibility(View.GONE);
    }

}
