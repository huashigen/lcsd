package com.lcshidai.lc.ui.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.licai.GetMyBookingListImpl;
import com.lcshidai.lc.model.licai.BookingListItemBean;
import com.lcshidai.lc.model.licai.MyBookingListJson;
import com.lcshidai.lc.service.licai.HttpGetBookingOrderListService;
import com.lcshidai.lc.ui.adapter.base.ListViewDataAdapter;
import com.lcshidai.lc.ui.adapter.base.ViewHolderBase;
import com.lcshidai.lc.ui.adapter.base.ViewHolderCreator;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.widget.xlvfresh.XListViewNew;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MyInvestAppointmentListActivity extends TRJActivity implements XListViewNew.IXListViewListener,
        AbsListView.OnScrollListener, GetMyBookingListImpl {

    @Bind(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @Bind(R.id.ib_top_bar_back)
    ImageButton topBackBtn;
    @Bind(R.id.tv_top_bar_title)
    TextView topTitleText;
    @Bind(R.id.tv_top_bar_right_action)
    TextView topActionText;
    @Bind(R.id.lv_my_appointment_list)
    XListViewNew lvMyAppointmentList;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmptyContainer;
    //    状态，1：待处理，2：处理中，3：成功，4：失败，5：已结算，0：不限

    private int mCurrentPage = 1;
    private boolean mHasMore;
    private boolean mIsLastRow;
    private final int REFRESH = 1;
    private final int LOADMORE = 2;
    private int DataChangeType = REFRESH;
    String status = "0";

    private Shader shader;
    private PopupWindow filterPopWin;
    private List<FilterItem> filterItemList = new ArrayList<>();
    private int mSelectedPos = 0;
    private List<BookingListItemBean> investAppointmentInfoList = new ArrayList<>();
    private ListViewDataAdapter<BookingListItemBean> infoListViewDataAdapter = null;
    private HttpGetBookingOrderListService getBookingOrderListService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invest_appointment_list);
        ButterKnife.bind(this);

        int colorStart = Color.parseColor("#FFE31C");
        int colorEnd = Color.parseColor("#FF811C");
        shader = new LinearGradient(0, 0, 0, 80, colorStart, colorEnd, Shader.TileMode.CLAMP);
        getBookingOrderListService = new HttpGetBookingOrderListService(this, this);
        initViewsAndEvents();
        onRefresh();
    }

    /**
     * 初始化筛选弹出窗口
     */
    private void initFilterPopWin() {
        @SuppressLint("InflateParams") final
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_win_filter, null);
        ListView lvFilter = (ListView) contentView.findViewById(R.id.lv_filter);
        ListViewDataAdapter<FilterItem> listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<FilterItem>() {
            @Override
            public ViewHolderBase<FilterItem> createViewHolder(int position) {
                return new ViewHolderBase<FilterItem>() {
                    TextView tvFilterName;
                    ImageView ivFilterMark;
                    View vDivider;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View convertView = layoutInflater.inflate(R.layout.filter_pop_win_list_item, null);
                        tvFilterName = (TextView) convertView.findViewById(R.id.tv_filter_name);
                        ivFilterMark = (ImageView) convertView.findViewById(R.id.iv_filter_mark);
                        vDivider = convertView.findViewById(R.id.v_divider);
                        return convertView;
                    }

                    @Override
                    public void showData(int position, FilterItem itemData) {
                        tvFilterName.setText(itemData.getFilterName());
                        if (mSelectedPos == position) {
                            ivFilterMark.setVisibility(View.VISIBLE);
                        } else {
                            ivFilterMark.setVisibility(View.GONE);
                        }
                        if (position == filterItemList.size() - 1) {
                            vDivider.setVisibility(View.GONE);
                        } else {
                            vDivider.setVisibility(View.VISIBLE);
                        }
                    }
                };
            }
        });
        filterItemList.add(new FilterItem("全部", "0"));
        filterItemList.add(new FilterItem("审核中", "12"));
        filterItemList.add(new FilterItem("预约成功", "3"));
        filterItemList.add(new FilterItem("预约失败", "4"));
        filterItemList.add(new FilterItem("已结算", "5"));
        listViewDataAdapter.getDataList().addAll(filterItemList);
        lvFilter.setAdapter(listViewDataAdapter);
        lvFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedPos = position;
                status = filterItemList.get(position).getFilterCode();
                // 刷新数据
                onRefresh();
                filterPopWin.dismiss();
            }
        });
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopWin.dismiss();
            }
        });
        filterPopWin = new PopupWindow(contentView, MATCH_PARENT, MATCH_PARENT, true);
    }

    private void initViewsAndEvents() {
        topTitleText.setText("我的预约");
        topActionText.setVisibility(View.VISIBLE);
        topActionText.setText("筛选");

        infoListViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<BookingListItemBean>() {
            @Override
            public ViewHolderBase<BookingListItemBean> createViewHolder(int position) {
                return new ViewHolderBase<BookingListItemBean>() {
                    TextView tvFundName, tvFundType, tvAmount, tvInvestorName, tvInvestorPhone, tvTime;
                    ImageView ivFundStatus;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View convertView = layoutInflater.inflate(R.layout.invest_appintment_list_item, null);
                        tvFundName = (TextView) convertView.findViewById(R.id.tv_fund_name);
                        tvFundType = (TextView) convertView.findViewById(R.id.tv_fund_type);
                        tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
                        tvInvestorName = (TextView) convertView.findViewById(R.id.tv_investor_name);
                        tvInvestorPhone = (TextView) convertView.findViewById(R.id.tv_investor_phone);
                        ivFundStatus = (ImageView) convertView.findViewById(R.id.iv_fund_status);
                        tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                        return convertView;
                    }

                    @Override
                    public void showData(int position, BookingListItemBean itemData) {
                        if (null != itemData) {
                            tvFundName.setText(itemData.getFund_name());
                            tvFundType.setText(itemData.getFund_type_str());
                            tvInvestorName.setText(itemData.getInvestor_name());
                            tvInvestorPhone.setText(itemData.getInvestor_phone());
//                            状态，1：待处理，2：处理中，3：成功，4：失败，5：已结算，12：审核中
                            if (itemData.getStatus().equals("12")) {
                                ivFundStatus.setImageResource(R.drawable.fund_status_auditing);
                                tvAmount.getPaint().setShader(shader);
                            } else if (itemData.getStatus().equals("3")) {
                                ivFundStatus.setImageResource(R.drawable.fund_status_audit_success);
                                tvAmount.getPaint().setShader(shader);
                            } else if (itemData.getStatus().equals("4")) {
                                ivFundStatus.setImageResource(R.drawable.fund_status_failed);
                                tvAmount.setTextColor(getResources().getColor(R.color.text_default));
                                tvAmount.getPaint().setShader(null);
                            } else if (itemData.getStatus().equals("5")) {
                                ivFundStatus.setImageResource(R.drawable.fund_status_ended);
                                tvAmount.getPaint().setShader(shader);
                            } else if (itemData.getStatus().equals("1") || itemData.getStatus().equals("2")) {
                                ivFundStatus.setImageResource(R.drawable.fund_status_auditing);
                                tvAmount.getPaint().setShader(shader);
                            }
                            tvAmount.setText(itemData.getAmount() + "万");
                            tvTime.setText(itemData.getTime());
                        }
                    }
                };
            }
        });
        infoListViewDataAdapter.getDataList().addAll(investAppointmentInfoList);
        lvMyAppointmentList.setAdapter(infoListViewDataAdapter);
        lvMyAppointmentList.setXListViewListener(this);
        lvMyAppointmentList.setOnScrollListener(this);
        lvMyAppointmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lcsId = infoListViewDataAdapter.getDataList().get(position - 1).getFund_id();
                Intent intent = new Intent();
                intent.setClass(mContext, FundProjectDetailActivity.class);
                intent.putExtra(Constants.FUND_ID, lcsId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (filterPopWin != null && filterPopWin.isShowing()) {
            filterPopWin.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return true;
    }

    @OnClick({R.id.ib_top_bar_back, R.id.tv_top_bar_right_action})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;
            case R.id.tv_top_bar_right_action:
                // 添加筛选弹窗
                if (null == filterPopWin) {
                    initFilterPopWin();
                    filterPopWin.showAsDropDown(rlTopBar);
                } else {
                    filterPopWin.showAsDropDown(rlTopBar);
                }
                break;
        }
    }

    @Override
    public void getMyBookingListSuccess(MyBookingListJson response) {
        hideLoadingDialog();
        if (null != response) {
            if (null != response.getData() && response.getData().getBookingList() != null &&
                    response.getData().getBookingList().size() > 0) {
                lvMyAppointmentList.stopRefresh();
                lvMyAppointmentList.loadMoreDone();
                lvMyAppointmentList.setRefreshTime();
                lvMyAppointmentList.setVisibility(View.VISIBLE);
                rlEmptyContainer.setVisibility(View.GONE);
                if (DataChangeType == REFRESH) {
                    infoListViewDataAdapter.getDataList().clear();
                }
                List<BookingListItemBean> temp = response.getData().getBookingList();
                infoListViewDataAdapter.getDataList().addAll(temp);
                infoListViewDataAdapter.notifyDataSetChanged();
                try {
                    int totalPage = Integer.valueOf(response.getData().getPage_count());
                    mHasMore = totalPage > mCurrentPage;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                infoListViewDataAdapter.getDataList().clear();
                lvMyAppointmentList.setVisibility(View.GONE);
                rlEmptyContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void getMyBookingListFailed() {
        hideLoadingDialog();
    }

    @Override
    public void onRefresh() {
        DataChangeType = REFRESH;
        mCurrentPage = 1;
        getBookingOrderListService.getBookingOrderList(GoLoginUtil.getAccessToken(this),
                GoLoginUtil.getUserToken(this), status, mCurrentPage + "");
        showLoadingDialog(mContext, "正在加载", true);
    }

    @Override
    public void onLoadMore() {
        if (mHasMore) {
            DataChangeType = LOADMORE;
            mCurrentPage++;
            getBookingOrderListService.getBookingOrderList(GoLoginUtil.getAccessToken(this),
                    GoLoginUtil.getUserToken(this), status, mCurrentPage + "");
            showLoadingDialog(mContext, "正在加载", true);
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

    class FilterItem {
        String filterName;
        String filterCode;

        public FilterItem(String filterName, String filterCode) {
            this.filterName = filterName;
            this.filterCode = filterCode;
        }

        public String getFilterName() {
            return filterName;
        }

        public void setFilterName(String filterName) {
            this.filterName = filterName;
        }

        public String getFilterCode() {
            return filterCode;
        }

        public void setFilterCode(String filterCode) {
            this.filterCode = filterCode;
        }
    }
}
