package com.lcshidai.lc.ui.fragment.more;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.MyRewardRecordImpl;
import com.lcshidai.lc.model.account.MyRewardRecordData;
import com.lcshidai.lc.model.account.MyRewardRecordJson;
import com.lcshidai.lc.model.account.MyRewardUsed;
import com.lcshidai.lc.service.account.HttpMyRewardRecordService;
import com.lcshidai.lc.ui.account.FinancialCashActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.NetUtils;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.xlvfresh.XListView;
import com.lcshidai.lc.widget.xlvfresh.XListView.IXListViewListener;

/**
 * 已使用理财金
 */
public class GoldenTabItemUsedFragment extends TRJFragment implements MyRewardRecordImpl,
        IXListViewListener {
    private TextView bp_item_change_type_tv1;
    private XListView mListView;
    private ItemAdapter mAdapter;
    HttpMyRewardRecordService hmrs;

    private View mContent;
    boolean hasMore;
    private int currentPage = 1;
    public boolean isRundata = false;

    private int nowPage = 1;
    private int pagesize = 10;
    public String mCurrentPType = "0";

    boolean haseMore = false;

    public ArrayList<String> idList = new ArrayList<String>();
    private View mProgressBar;
    int mPosition = -1;
    private boolean isLoading = false; // 是否正在加载数据
    public List<MyRewardUsed> mfilelist = new ArrayList<MyRewardUsed>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        hmrs = new HttpMyRewardRecordService((FinancialCashActivity) getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContent = inflater.inflate(R.layout.fragment_golden_lisvtrecord_view, container, false);
        mAdapter = new ItemAdapter(getActivity(), mfilelist);
        mProgressBar = mContent.findViewById(R.id.progressContainer);
        mListView = (XListView) mContent.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
        mListView.setXListViewListener(this);
        View header = inflater.inflate(R.layout.item_header_golden_used, null);
        bp_item_change_type_tv1 = (TextView) header.findViewById(R.id.bp_item_change_type_tv1);
        mListView.addHeaderView(header);
        return mContent;
    }

    public void lds(boolean isReload) {
        if (!isReload && mAdapter != null && mAdapter.size > 1) {
            mListView.showHeader();
        } else {
            reLoadDatas();
        }
    }

    public synchronized void reLoadDatas() {
        if (mAdapter != null && !isRundata) {
            idList.clear();
            hasMore = false;
            mAdapter.clear();
            mListView.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            nowPage = 1;
            mProgressBar.setVisibility(View.VISIBLE);
            mContent.findViewById(R.id.rl_empty).setVisibility(View.GONE);
            loadDatas();
        }
    }

    public synchronized void reLoadDataDown() {
        if (mAdapter != null && !isRundata) {
            idList.clear();
            isDown = true;
            hasMore = false;
            nowPage = 1;
            loadDatas();
        }
    }

    boolean isDown = false;

    private void loadDatas() {
        if (getActivity() == null || !NetUtils.isNetworkConnected(getActivity())) {
            mProgressBar.setVisibility(View.GONE);
            mContent.findViewById(R.id.listView).setVisibility(View.GONE);
            mContent.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
            return;
        }
        isRundata = true;
        hmrs.gainMyRewardRecord(nowPage, pagesize);

    }

    @Override
    public void onStop() {
        super.onStop();
        isRundata = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
        mPosition = -1;
        if (mAdapter != null && mAdapter.size > 1) {
            mProgressBar.setVisibility(View.GONE);
            if (MemorySave.MS.goToFinanceAll)
                mListView.showHeader();
            MemorySave.MS.goToFinanceAll = false;
        } else {
            reLoadDatas();
        }
    }

    public class ItemAdapter extends ArrayAdapter<MyRewardUsed> {
        private Context mContext;
        public List<MyRewardUsed> mfilelist = new ArrayList<MyRewardUsed>();
        public int size;

        public ItemAdapter(Activity context, List<MyRewardUsed> lilelist) {
            super(context, 0);
            mContext = context;
            mfilelist = lilelist;
        }

        @Override
        public int getCount() {
            size = mfilelist.size();
            return size + (hasMore ? 1 : 0);
        }

        @Override
        public void add(MyRewardUsed object) {
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
        public MyRewardUsed getItem(int position) {
            return mfilelist.get(position);
        }

        @Override
        public View getView(final int position, View convertView,
                            final ViewGroup parent) {
            try {
                ViewHolders vh = null;
                if (position < size) {
                    if (convertView == null || convertView.getTag() == null) {
                        vh = new ViewHolders();
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.gold_used_item, null);
                        vh.bp_item_title_type = (TextView) convertView.findViewById(R.id.bp_item_title_type);
                        vh.bp_item_change_type_tv1 = (TextView) convertView.findViewById(R.id.bp_item_change_type_tv1);
                        vh.bp_item_amount_tv = (TextView) convertView.findViewById(R.id.bp_item_amount_tv);
                        vh.bp_item_amount_tv1 = (TextView) convertView.findViewById(R.id.bp_item_amount_tv1);
                        vh.bp_item_amount_tv2 = (TextView) convertView.findViewById(R.id.bp_item_amount_tv2);
                        vh.bp_item_amount_tv3 = (TextView) convertView.findViewById(R.id.bp_item_amount_tv3);
                        vh.bp_item_prj_name1_tv = (TextView) convertView.findViewById(R.id.bp_item_prj_name1_tv);
                        vh.bp_item_prj_name2_tv = (TextView) convertView.findViewById(R.id.bp_item_prj_name2_tv);
                        convertView.setTag(vh);
                    } else {
                        vh = (ViewHolders) convertView.getTag();
                    }
                    MyRewardUsed pi = mfilelist.get(position);
                    vh.bp_item_title_type.setText(pi.getCtime());
                    vh.bp_item_change_type_tv1.setText(pi.getYield());
                    vh.bp_item_amount_tv.setText("体验项目" + pi.getPrj_name());
//					vh.tvTime.setText(pi.getCtime());
                    String status = pi.getStatus();
                    if (!StringUtils.isEmpty(status) && status.equals("1")) {
                        vh.bp_item_prj_name1_tv.setTextColor(getResources().getColor(R.color.theme_color));
                    } else {
                        vh.bp_item_prj_name1_tv.setTextColor(getResources().getColor(R.color.color_1_1));
                    }
                    vh.bp_item_amount_tv1.setText(pi.getMoney() + "元");
                    vh.bp_item_amount_tv3.setText(pi.getMuji_day());
                    vh.bp_item_prj_name1_tv.setText(pi.getYield());
                    vh.bp_item_prj_name2_tv.setText("元  (" + pi.getInfo() + ")");
                    final String id = pi.getId();
                } else {
                    View view = View.inflate(getActivity(),
                            R.layout.loading_item, null);
                    loadDatas();
                    return view;
                }
            } catch (Exception e) {
                System.out.println("licaijin" + e);
            }
            return convertView;
        }

        public class ViewHolders {

            public TextView bp_item_prj_name2_tv;
            public TextView bp_item_prj_name1_tv;
            public TextView bp_item_amount_tv1, bp_item_amount_tv2, bp_item_amount_tv3;
            public TextView bp_item_amount_tv;
            public TextView bp_item_change_type_tv1;
            public TextView bp_item_title_type;
            TextView tvTitle, tvSource;
            TextView tvTime;
            ImageView iv_newsicon;
        }
    }

    @Override
    public void onRefresh() {
        reLoadDataDown();
    }

    @Override
    public void onLoadMore() {
        loadDatas();
    }

    @Override
    public void gainMyRewardRecordsuccess(MyRewardRecordJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    if (isDown) {
                        isDown = false;
                        mAdapter.clear();
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();
                    }
                    if (null != response.getData()) {
                        String sum_yield = response.getData().getSum_yield();
                        bp_item_change_type_tv1.setText(sum_yield);
                    }
                    if (null != response.getData() && null != response.getData().getData()) {
                        MyRewardRecordData dataObj = response.getData();
                        String totalPage = dataObj.getTotal_page();
                        currentPage = Integer.valueOf(dataObj.getCurrent_page());

                        if (String.valueOf(currentPage).equals(String.valueOf(nowPage))) {//nowPage
                            List<MyRewardUsed> dataArray = response.getData().getData();
                            if (dataArray.size() < pagesize) {
                                haseMore = false;
                                mListView.setPullLoadEnable(false);
                            }
                            for (int i = 0; i < dataArray.size(); i++) {
                                MyRewardUsed obj = dataArray.get(i);
                                mAdapter.add(obj);
                            }
                            if (Integer.valueOf(totalPage) > nowPage) {
                                haseMore = true;
                                nowPage += 1;
                            } else {
                                haseMore = false;
                                mListView.setPullLoadEnable(false);
                            }
                            isLoading = false;
                        }
                    }
                } else {
                    mContent.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
                    haseMore = false;
                }
                if (mAdapter.mfilelist.size() <= 0 && !StringUtils.isEmpty(response.getLogined())
                        && response.getLogined().equals("0")) {
                    mContent.findViewById(R.id.listView).setVisibility(View.GONE);
                    mContent.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
                } else {
                    mContent.findViewById(R.id.rl_empty).setVisibility(View.GONE);
                    mContent.findViewById(R.id.listView).setVisibility(View.VISIBLE);
                }
//			} else {
//				hasMore = false;
            }
        } catch (Exception e) {
            hasMore = false;
            e.printStackTrace();
        } finally {
            mProgressBar.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            isRundata = false;
        }

    }

    @Override
    public void gainMyRewardRecordfail() {
        ToastUtil.showToast(GoldenTabItemUsedFragment.this.getActivity(),
                getResources().getString(R.string.net_error));
        mProgressBar.setVisibility(View.GONE);
        isRundata = false;
    }
}