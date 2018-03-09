package com.lcshidai.lc.ui.fragment.finance;

import java.util.ArrayList;
import java.util.List;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.BroadCastImpl;
import com.lcshidai.lc.impl.finance.FinanceHBImpl;
import com.lcshidai.lc.impl.finance.FinanceMJQImpl;
import com.lcshidai.lc.model.finance.FinanceHBItemData;
import com.lcshidai.lc.model.finance.FinanceHBJson;
import com.lcshidai.lc.model.finance.FinanceMJQItemData;
import com.lcshidai.lc.model.finance.FinanceMJQJson;
import com.lcshidai.lc.service.finance.HttpFinanceHBService;
import com.lcshidai.lc.service.finance.HttpFinanceMJQService;
import com.lcshidai.lc.ui.adapter.FinanceHBAdapter;
import com.lcshidai.lc.ui.adapter.FinanceMJQAdapter;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.RewardCheckUtil;
import com.lcshidai.lc.widget.xlvfresh.XListViewNew;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FinanceHBFragment extends TRJFragment implements OnItemClickListener, XListViewNew.IXListViewListener,
        AbsListView.OnScrollListener, FinanceHBImpl, FinanceMJQImpl {
    private XListViewNew mListView;
    private LinearLayout ll_empty;
    private TextView tv_empty;

    public static final int HB = 1;
    public static final int MJQ = 2;
    public static final int JXQ = 3;
    public int TabType = 0;

    private int mPage;
    private boolean mHasMore;
    private boolean mIsLastRow;

    private final int REFRESH = 1;
    private final int LOADMORE = 2;
    private int DataChangeType = REFRESH;

    private HttpFinanceHBService mHttpFinanceHBService;
    private HttpFinanceMJQService mHttpFinanceMJQService;

    private String prj_id, amount, reward_type, bouns_rate, bouns_prj_term, mjq_id;
    private int isCollection = 0;

    private List<?> mDataList;
    private BaseAdapter mAdapter;

    private int mCheckedIndex = -1;

    private ClearCheckedHBReceiver mClearCheckedHBReceiver;

    public FinanceHBFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hb, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prj_id = getArguments().getString("prj_id");
        amount = getArguments().getString("amount");
        reward_type = getArguments().getString("reward_type");
        bouns_rate = getArguments().getString("bouns_rate");
        bouns_prj_term = getArguments().getString("bouns_prj_term");
        mjq_id = getArguments().getString("mjq_id");
        isCollection = getArguments().getInt(Constants.Project.IS_COLLECTION);

        mHttpFinanceHBService = new HttpFinanceHBService(this, (TRJActivity) getActivity());
        mHttpFinanceMJQService = new HttpFinanceMJQService(this, (TRJActivity) getActivity());

        initView(view, savedInstanceState);
        initData();

        mClearCheckedHBReceiver = new ClearCheckedHBReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastImpl.ACTION_CLEAR_CHECK_HB);
        getActivity().registerReceiver(mClearCheckedHBReceiver, filter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mClearCheckedHBReceiver);
    }

    private void initView(View view, Bundle savedInstanceState) {
        mListView = (XListViewNew) view.findViewById(R.id.listView);
        ll_empty = (LinearLayout) view.findViewById(R.id.ll_empty);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);

        switch (TabType) {
            case HB:
                mDataList = new ArrayList<FinanceHBItemData>();
                mAdapter = new FinanceHBAdapter((List<FinanceHBItemData>) mDataList);
                mListView.setAdapter(mAdapter);
                tv_empty.setText("暂无红包");
                break;
            case MJQ:
                mDataList = new ArrayList<FinanceMJQItemData>();
                mAdapter = new FinanceMJQAdapter((List<FinanceMJQItemData>) mDataList);
                mListView.setAdapter(mAdapter);
                tv_empty.setText("暂无满减券");
                break;
            case JXQ:
                break;
            default:
                break;
        }

        mListView.setXListViewListener(this);
        mListView.setOnScrollListener(this);
        mListView.setOnItemClickListener(this);
    }

    private void initData() {
        switch (TabType) {
            case HB:
                mHttpFinanceHBService.getPrjBonus(prj_id, amount, isCollection);
                break;
            case MJQ:
                mHttpFinanceMJQService.getPrjTickets(prj_id, amount, isCollection);
                break;
            case JXQ:
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int location, long arg3) {
        //location = 1, 2, ...
        setCheckIndex(location, true);
        RewardCheckUtil.getInstance().setIs_perform_clicked(true);
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onLoadMore() {
        if (mHasMore) {
            loadMore();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount <= 0) {
            return;
        }
        if (firstVisibleItem + visibleItemCount >= totalItemCount) {
            mIsLastRow = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mIsLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mIsLastRow = false;
            onLoadMore();
        }
    }

    private void refresh() {
        DataChangeType = REFRESH;
//        initData();
        //假刷新
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mListView.stopRefresh();
                mListView.loadMoreDone();
                mListView.setRefreshTime();

            }
        }, 100);
    }

    private void loadMore() {
        DataChangeType = LOADMORE;
        mListView.loadMore();
    }

    @Override
    public void gainFinaceBonusSuccess(FinanceHBJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                mListView.stopRefresh();
                mListView.loadMoreDone();
                mListView.setRefreshTime();
                List<FinanceHBItemData> data = response.getData();
                if (data != null && data.size() > 0) {
                    ll_empty.setVisibility(View.GONE);
                } else {
                    ll_empty.setVisibility(View.VISIBLE);
                }
                if (DataChangeType == REFRESH) {
                    mDataList.clear();
                }
                ((List<FinanceHBItemData>) mDataList).addAll(data);
                mAdapter.notifyDataSetChanged();
                filterChecked();
            } else {
                ll_empty.setVisibility(View.VISIBLE);
            }
        } else {
            ll_empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void gainFinaceBonusFail() {

    }

    @Override
    public void gainFinaceTicketsSuccess(FinanceMJQJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                mListView.stopRefresh();
                mListView.loadMoreDone();
                mListView.setRefreshTime();
                List<FinanceMJQItemData> data = response.getData();
                if (data != null && data.size() > 0) {
                    ll_empty.setVisibility(View.GONE);
                } else {
                    ll_empty.setVisibility(View.VISIBLE);
                }
                if (DataChangeType == REFRESH) {
                    mDataList.clear();
                }
                ((List<FinanceMJQItemData>) mDataList).addAll(data);
                mAdapter.notifyDataSetChanged();
                filterChecked();
            } else {
                ll_empty.setVisibility(View.VISIBLE);
            }
        } else {
            ll_empty.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void gainFinaceTicketsFail() {

    }

    private void setCheckIndex(int position, boolean clear) {
        int is_usable = 0;
        switch (TabType) {
            case HB:
                FinanceHBItemData financeHBItemData = (FinanceHBItemData) mDataList.get(position - 1);
                is_usable = financeHBItemData.getIs_usable();
                if (is_usable == 1) {
                    if (mCheckedIndex == position - 1) {
                        mCheckedIndex = -1;
                    } else {
                        mCheckedIndex = position - 1;
                    }
                    ((FinanceHBAdapter) mAdapter).setCheckIndex(position - 1, clear);
                    mAdapter.notifyDataSetChanged();
                    if (clear) {
                        Intent intent = new Intent();
                        intent.setAction(BroadCastImpl.ACTION_CLEAR_CHECK_HB);
                        intent.putExtra("type", TabType);
                        getActivity().sendBroadcast(intent);
                    }
                }
                break;
            case MJQ:
                FinanceMJQItemData financeMJQItemData = (FinanceMJQItemData) mDataList.get(position - 1);
                is_usable = financeMJQItemData.getIs_usable();
                if (is_usable == 1) {
                    if (mCheckedIndex == position - 1) {
                        mCheckedIndex = -1;
                    } else {
                        mCheckedIndex = position - 1;
                    }
                    ((FinanceMJQAdapter) mAdapter).setCheckIndex(position - 1, clear);
                    mAdapter.notifyDataSetChanged();
                    if (clear) {
                        Intent intent = new Intent();
                        intent.setAction(BroadCastImpl.ACTION_CLEAR_CHECK_HB);
                        intent.putExtra("type", TabType);
                        getActivity().sendBroadcast(intent);
                    }
                }
                break;
            case JXQ:
                break;
            default:
                break;
        }
    }

    private void filterChecked() {
        switch (TabType) {
            case HB:
                if (reward_type != null && reward_type.equals("1")) {
                    float _bouns_rate = Float.valueOf(bouns_rate);
                    FinanceHBItemData financeHBItemData;
                    for (int i = 0; i < mDataList.size(); i++) {
                        financeHBItemData = (FinanceHBItemData) mDataList.get(i);
                        if (_bouns_rate == financeHBItemData.getRate()
                                && bouns_prj_term.equals(financeHBItemData.getPrj_term())) {
                            if (financeHBItemData.getIs_usable() == 1) {
                                setCheckIndex(i + 1, true);
                            }
                            break;
                        }
                    }
                }
                break;
            case MJQ:
                if (reward_type != null && reward_type.equals("2")) {
                    FinanceMJQItemData financeMJQItemData;
                    for (int i = 0; i < mDataList.size(); i++) {
                        financeMJQItemData = (FinanceMJQItemData) mDataList.get(i);
                        if (mjq_id != null && mjq_id.equals(financeMJQItemData.getId())) {
                            if (financeMJQItemData.getIs_usable() == 1) {
                                setCheckIndex(i + 1, true);
                            }
                            break;
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    class ClearCheckedHBReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra("type", -1);
            if (type != TabType) {
                if (mCheckedIndex != -1) {
                    setCheckIndex(mCheckedIndex + 1, false);
                }
            }
        }

    }

}
