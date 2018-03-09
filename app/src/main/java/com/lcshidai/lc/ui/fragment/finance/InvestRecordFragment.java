package com.lcshidai.lc.ui.fragment.finance;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.finance.GetFinanceProjectInvestRecordImpl;
import com.lcshidai.lc.model.finance.FinanceProjectInvestRecordData;
import com.lcshidai.lc.model.finance.FinanceProjectInvestRecordItem;
import com.lcshidai.lc.model.finance.FinanceProjectInvestRecordListJson;
import com.lcshidai.lc.model.finance.InvestRecordTopRankItem;
import com.lcshidai.lc.service.finance.GetFinanceProjectInvestRecordService;
import com.lcshidai.lc.ui.adapter.base.ListViewDataAdapter;
import com.lcshidai.lc.ui.adapter.base.ViewHolderBase;
import com.lcshidai.lc.ui.adapter.base.ViewHolderCreator;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.widget.MyListView;
import com.lcshidai.lc.widget.xlvfresh.XListView;
import com.lcshidai.lc.widget.xlvfresh.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 投资记录
 */
public class InvestRecordFragment extends TRJFragment implements GetFinanceProjectInvestRecordImpl,
        IXListViewListener {

    public View mContentView;

    private String mPrjId;
    public View mStateView;
    private XListView lv_record;
    private int mPageSize = 10;
    private int mP = 1;
    boolean hasMore;
    GetFinanceProjectInvestRecordService getInvestRecordService;
    private View headerView;
    private ListViewDataAdapter<InvestRecordTopRankItem> investRecordTopRankAdapter;

    private int totalPages = 0;
    volatile boolean isLoading = false; // 是否正在加载数据
    private TextView tvRankLabel;
    private MyListView lvTopRank;
    private LinearLayout llInvestDesContainer;
    private int isCollection = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPrjId = bundle.getString(Constants.Project.PROJECT_ID);
            isCollection = bundle.getInt(Constants.Project.IS_COLLECTION);
        }
        getInvestRecordService = new GetFinanceProjectInvestRecordService(
                (FinanceProjectDetailActivity) getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        内容布局
        mContentView = inflater.inflate(R.layout.fragment_finance_invest_record, container, false);
//        XListView头布局
        headerView = inflater.inflate(R.layout.fragment_finance_invest_record_lv_head, null);
        tvRankLabel = (TextView) headerView.findViewById(R.id.tv_rank_label);
        lvTopRank = (MyListView) headerView.findViewById(R.id.lv_rank);
        llInvestDesContainer = (LinearLayout) headerView.findViewById(R.id.ll_invest_des_container);

        lv_record = (XListView) mContentView.findViewById(R.id.lv_record);
        mStateView = mContentView.findViewById(R.id.iv_state);
        lv_record.setCacheColorHint(Color.TRANSPARENT);
        lv_record.setPullRefreshEnable(false);    // 禁用下拉刷新
        lv_record.setPullLoadEnable(true);    // 启用加载更多
        lv_record.setAdapter(iAdapter);
        investRecordTopRankAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<InvestRecordTopRankItem>() {
            @Override
            public ViewHolderBase<InvestRecordTopRankItem> createViewHolder(int position) {
                return new ViewHolderBase<InvestRecordTopRankItem>() {
                    ImageView ivRankIcon;
                    TextView tvInvestor;
                    TextView tvInvestAmount;
                    TextView tvInvestTime;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
//                        投资排行榜布局
                        View convertView = layoutInflater.inflate(R.layout.fragment_finance_invest_record_lv_rank_item, null);
                        ivRankIcon = (ImageView) convertView.findViewById(R.id.iv_rank_icon);
                        tvInvestor = (TextView) convertView.findViewById(R.id.tv_investor);
                        tvInvestAmount = (TextView) convertView.findViewById(R.id.tv_invest_amount);
                        tvInvestTime = (TextView) convertView.findViewById(R.id.tv_invest_time);
                        return convertView;
                    }

                    @Override
                    public void showData(int position, InvestRecordTopRankItem itemData) {
                        if (null != itemData) {
//                            排行榜图片加载
                            Glide.with(mContext).load(itemData.getRanking_img()).into(ivRankIcon);
                            if (null != itemData.getData()) {
                                tvInvestor.setText(itemData.getData().getUname());
                                tvInvestAmount.setText(itemData.getData().getMoney());
                                tvInvestTime.setText(itemData.getData().getCtime());
                            }
                        }
                    }
                };
            }
        });
        lv_record.addHeaderView(headerView);
        lv_record.setXListViewListener(this);
//		lv_record.setOnScrollListener(this);
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    void loadMore() {
        getInvestRecordService.getFinanceProjectInvestRecord(mPrjId, String.valueOf(mP++), mPageSize, isCollection);
    }

    public void loadData() {
        mP = 1;
        hasMore = false;
        iAdapter.mItems.clear();
        iAdapter.notifyDataSetChanged();
        if (investRecordTopRankAdapter != null) {
            investRecordTopRankAdapter.getDataList().clear();
        }
        loadMore();
    }

    ItemAdapter iAdapter = new ItemAdapter();

    /**
     * 处理数据
     *
     * @param data FinanceProjectInvestRecordData
     */
    private void dealWithInvestRecordData(FinanceProjectInvestRecordData data) {
        if (null != data) {
            List<FinanceProjectInvestRecordItem> financeProjectInvestRecordItems = data.getList();
            totalPages = data.getPage().getTotalPages();
            if (null != financeProjectInvestRecordItems) {//jaitems 不为空的话jobd.getTop_Ranks()一定不为空
                hasMore = mP <= totalPages;
                if (!hasMore) {
                    hasMore = false;
                    lv_record.setPullLoadEnable(false);
                }
                iAdapter.mItems.addAll(financeProjectInvestRecordItems);
                tvRankLabel.setVisibility(View.VISIBLE);
                lvTopRank.setVisibility(View.VISIBLE);
                llInvestDesContainer.setVisibility(View.VISIBLE);
                if (investRecordTopRankAdapter != null && data.getTop_ranks() != null) {
                    investRecordTopRankAdapter.getDataList().addAll(data.getTop_ranks());
                    lvTopRank.setAdapter(investRecordTopRankAdapter);
                }
            }
        }
        if (isLoading) {
            lv_record.stopLoadMore();
            isLoading = false;
        }
    }

    class ItemAdapter extends BaseAdapter {

        public ArrayList<FinanceProjectInvestRecordItem> mItems = new ArrayList<FinanceProjectInvestRecordItem>();

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (position < mItems.size()) {
                FinanceProjectInvestRecordItem item = mItems.get(position);
                if (convertView == null || convertView.getTag() == null
                        || !(convertView.getTag() instanceof ViewHolder)) {
                    holder = new ViewHolder();
//                    投标人  投标金额  投标时间 布局
                    convertView = LayoutInflater.from(getActivity()).inflate(
                            R.layout.fragment_finance_invest_record_lvitem, null);
                    holder.tv_bidder = (TextView) convertView.findViewById(R.id.tv_bidder);
                    holder.tv_bid_amount = (TextView) convertView.findViewById(R.id.tv_bid_amount);
                    holder.tv_bid_time = (TextView) convertView.findViewById(R.id.tv_bid_time);
                    holder.rl_bespeak = (RelativeLayout) convertView.findViewById(R.id.rl_bespeak);
                    holder.rl_pre_sale = (RelativeLayout) convertView.findViewById(R.id.rl_pre_sale);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tv_bidder.setText(item.uname);
                holder.tv_bid_amount.setText(item.money);
                holder.tv_bid_time.setText(item.ctime);
                if (!TextUtils.isEmpty(item.is_appoint) && item.is_appoint.equals("1")) {
                    holder.rl_bespeak.setVisibility(View.VISIBLE);
                } else {
                    holder.rl_bespeak.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(item.is_pre_sale) && item.is_pre_sale.equals("1")) {
                    holder.rl_pre_sale.setVisibility(View.VISIBLE);
                } else {
                    holder.rl_pre_sale.setVisibility(View.GONE);
                }
                if (holder.rl_pre_sale.getVisibility() == View.GONE && holder.rl_bespeak.getVisibility() == View.GONE) {
                    holder.rl_bespeak.setVisibility(View.INVISIBLE);
                }
            } else {
                View view = View.inflate(getActivity(), R.layout.loading_item, null);
                if (MemorySave.MS.mIsLogin)
                    loadMore();
                return view;
            }
            return convertView;
        }

        @Override
        public boolean isEnabled(int position) {
            return super.isEnabled(position);
        }
    }

    private class ViewHolder {
        RelativeLayout rl_bespeak;
        RelativeLayout rl_pre_sale;
        TextView tv_bidder;
        TextView tv_bid_amount;
        TextView tv_bid_time;
    }

    class Item {
        public String current_page;// -> 当前页
        public String total_page;// -> 总页数
        public String total;// -> 数据总条数
    }

    @Override
    public void getFinanceProjectInvestRecordSuccess(FinanceProjectInvestRecordListJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    FinanceProjectInvestRecordData data = response.getData();
                    dealWithInvestRecordData(data);
//					setView();
                } else {
                    hasMore = false;
                }
            } else {
                hasMore = false;
            }
        } catch (Exception e) {
            hasMore = false;
            e.printStackTrace();
        } finally {
            iAdapter.notifyDataSetChanged();
            if (iAdapter.mItems.size() > 0) {
                mContentView.findViewById(R.id.iv_state).setVisibility(View.GONE);
            } else {
                mContentView.findViewById(R.id.iv_state).setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void getFinanceProjectInvestRecordFail() {
        hasMore = false;
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
        isLoading = true;
        loadMore();
    }
}
