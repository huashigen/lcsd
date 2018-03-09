package com.lcshidai.lc.ui.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.finance.LcsShowIndexImpl;
import com.lcshidai.lc.model.finance.lcs.LcsItemData;
import com.lcshidai.lc.model.finance.lcs.LcsShowIndexData;
import com.lcshidai.lc.model.finance.lcs.LcsShowIndexJson;
import com.lcshidai.lc.service.finance.HttpLcsFoundShowIndexService;
import com.lcshidai.lc.ui.adapter.LicaiAdapter;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.widget.xlvfresh.XListViewNew;

import java.util.ArrayList;
import java.util.List;

/**
 * 基金列表Fragment
 */
public class FundFragment extends TRJFragment implements AdapterView.OnItemClickListener, XListViewNew.IXListViewListener, AbsListView.OnScrollListener,
        LcsShowIndexImpl {

    protected static final String FUND_TYPE = "fund_type";
    protected static final String FUND_TYPE_STR = "fund_type_str";

    private String mFundType;
    private XListViewNew mListView;
    private RelativeLayout rlEmptyContainer;

    private HttpLcsFoundShowIndexService mHttpLcsFoundShowIndexService;

    private List<LcsItemData> mDataList = new ArrayList<LcsItemData>();
    private LicaiAdapter mLicaiAdapter;
    private int mPage;
    private boolean mHasMore;
    private boolean mIsLastRow;

    private final int REFRESH = 1;
    private final int LOADMORE = 2;
    private int DataChangeType = REFRESH;

    private int mResultCode = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFundType = getArguments().getString(FUND_TYPE);
        }
    }

    public static FundFragment newInstance(String fundType) {
        FundFragment fragment = new FundFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FUND_TYPE, fundType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_licai, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHttpLcsFoundShowIndexService = new HttpLcsFoundShowIndexService((TRJActivity) getActivity(), this);

        initView(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isHidden()) {
            mResultCode = 0;
            return;
        }
        if (mResultCode == 0) {
            mPage = 1;
            refresh();
        } else {
            mResultCode = 0;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mResultCode == 0) {
                mPage = 1;
                refresh();
            }
        }
    }

    private void initView(View view, Bundle savedInstanceState) {
        mListView = (XListViewNew) view.findViewById(R.id.listView);
        rlEmptyContainer = (RelativeLayout) view.findViewById(R.id.rl_empty);

        mLicaiAdapter = new LicaiAdapter(mDataList);
        mListView.setAdapter(mLicaiAdapter);

        mListView.setXListViewListener(this);
        mListView.setOnScrollListener(this);
        mListView.setOnItemClickListener(this);
    }

    public void setFlag(int resultCode) {
        mResultCode = resultCode;
    }

    private void refresh() {
        DataChangeType = REFRESH;
        mPage = 1;
        mHttpLcsFoundShowIndexService.gainLcsList(GoLoginUtil.getAccessToken(getActivity()),
                GoLoginUtil.getUserToken(getActivity()), mFundType, "", String.valueOf(mPage));
    }

    private void loadMore() {
        DataChangeType = LOADMORE;
        mPage++;
        mListView.loadMore();
        mHttpLcsFoundShowIndexService.gainLcsList(GoLoginUtil.getAccessToken(getActivity()),
                GoLoginUtil.getUserToken(getActivity()), mFundType, "", String.valueOf(mPage));
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mIsLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mIsLastRow = false;
            onLoadMore();
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
    public void gainLcsShowIndexSuccess(LcsShowIndexJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
//                mBannerView.setVisibility(View.VISIBLE);
                mListView.stopRefresh();
                mListView.loadMoreDone();
                mListView.setRefreshTime();
                LcsShowIndexData data = response.getData();
                if (DataChangeType == REFRESH) {
                    mDataList.clear();
                }
                if (response.getStatus().equals("1")) {
                    if (data != null) {
                        mDataList.addAll(data.getList());
                        if (mDataList.size() > 0) {
                            rlEmptyContainer.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                        } else {
                            rlEmptyContainer.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        }
                    } else {
                        rlEmptyContainer.setVisibility(View.VISIBLE);
                    }
                }
                mLicaiAdapter.notifyDataSetChanged();
//                mPage = Integer.valueOf(data.getCurrent_page());
                mHasMore = data.getTotal_page() > mPage;
            }
        }
    }

    @Override
    public void gainLcsShowIndexFail() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String lcsId = mDataList.get(position - 1).getFund_id();
        Intent intent = new Intent();
//        String web_url = String.format(TRJHttpClient.RELEASEE_URL_WAP + "#/fundFinanceInfo/%s/", lcsId);
//        intent.putExtra("web_url", web_url);
//        intent.putExtra("need_header", "0");
//        intent.setClass(getActivity(), MainWebActivity.class);
        intent.setClass(mContext, FundProjectDetailActivity.class);
        intent.putExtra(Constants.FUND_ID, lcsId);
        startActivity(intent);
    }
}
