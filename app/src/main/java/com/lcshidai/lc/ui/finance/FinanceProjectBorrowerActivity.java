package com.lcshidai.lc.ui.finance;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.finance.GetFinanceProjectBorrowerRecordImpl;
import com.lcshidai.lc.model.finance.borrower.FinanceProjectBorrowerRecordData;
import com.lcshidai.lc.model.finance.borrower.FinanceProjectBorrowerRecordItem;
import com.lcshidai.lc.model.finance.borrower.FinanceProjectInvestBorrowerListJson;
import com.lcshidai.lc.service.finance.GetFinanceProjectBorrowerRecordService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.widget.xlvfresh.XListView;

import java.util.ArrayList;
import java.util.List;

public class FinanceProjectBorrowerActivity extends TRJActivity implements GetFinanceProjectBorrowerRecordImpl, XListView.IXListViewListener {
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private String mPrjId;
    public View mStateView;
    private XListView lv_record;
    private int mPageSize = 10;
    private int mP = 1;
    boolean hasMore;
    GetFinanceProjectBorrowerRecordService getBorrowerRecordService;
    private View headerView;

    private int totalPages = 0;
    volatile boolean isLoading = false; // 是否正在加载数据
    private TextView tvInvestInfo;
    private int isCollection = 0;

    ItemAdapter iAdapter = new ItemAdapter();

    private static final String TAG = "FinanceProjectBorrowerA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_project_borrower);
        mBackBtn = (ImageButton) findViewById(R.id.ib_top_bar_back);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("借款人详情");
        Bundle bundle = getIntent().getExtras();
        mPrjId = bundle.getString(Constants.Project.PROJECT_ID);

        headerView = LayoutInflater.from(this).inflate(R.layout.finance_borrower_record_lv_head, null);
        tvInvestInfo = (TextView) headerView.findViewById(R.id.tv_invest_info);

        lv_record = (XListView) findViewById(R.id.lv_record);
        mStateView = findViewById(R.id.iv_state);
        lv_record.setCacheColorHint(Color.TRANSPARENT);
        lv_record.setPullRefreshEnable(false);    // 禁用下拉刷新
        lv_record.setPullLoadEnable(true);

        lv_record.addHeaderView(headerView);
        lv_record.setXListViewListener(this);
        lv_record.setAdapter(iAdapter);

        getBorrowerRecordService = new GetFinanceProjectBorrowerRecordService(this, this);
    }


    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData() {
        mP = 1;
        hasMore = false;
        iAdapter.mItems.clear();
        iAdapter.notifyDataSetChanged();
        loadMore();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    public void getFinanceProjectBorrowerRecordSuccess(FinanceProjectInvestBorrowerListJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    FinanceProjectBorrowerRecordData data = response.getData();
                    dealWithInvestRecordData(data);
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
        }
    }

    /**
     * 处理数据
     *
     * @param data FinanceProjectInvestRecordData
     */
    private void dealWithInvestRecordData(FinanceProjectBorrowerRecordData data) {
        if (null != data) {
            List<FinanceProjectBorrowerRecordItem> financeProjectInvestRecordItems = data.getList();
            totalPages = data.getPage().getTotalPages();
            if (null != financeProjectInvestRecordItems) {//jaitems 不为空的话jobd.getTop_Ranks()一定不为空
                hasMore = mP <= totalPages;
                if (!hasMore) {
                    hasMore = false;
                    lv_record.setPullLoadEnable(false);
                }
                iAdapter.mItems.addAll(financeProjectInvestRecordItems);
                tvInvestInfo.setVisibility(View.VISIBLE);
                tvInvestInfo.setText("共" + data.getPage().getTotal() + "人，合计" + data.getTotalMoney() + "元");
            }
        }
        if (isLoading) {
            lv_record.stopLoadMore();
            isLoading = false;
        }
    }

    @Override
    public void getFinanceProjectBorrowerRecordFail() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        isLoading = true;
        loadMore();
    }

    class ItemAdapter extends BaseAdapter {

        public ArrayList<FinanceProjectBorrowerRecordItem> mItems = new ArrayList<>();

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
                FinanceProjectBorrowerRecordItem item = mItems.get(position);
                if (convertView == null || convertView.getTag() == null
                        || !(convertView.getTag() instanceof ViewHolder)) {
                    holder = new ViewHolder();
//                    投标人  投标金额  投标时间 布局
                    convertView = LayoutInflater.from(FinanceProjectBorrowerActivity.this).inflate(
                            R.layout.finance_borrower_record_lvitem, parent, false);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.tv_id_num = (TextView) convertView.findViewById(R.id.tv_id_num);
                    holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tv_name.setText(item.custName);
                holder.tv_id_num.setText(item.idNo);
                holder.tv_amount.setText(item.amount);
            } else {
                View view = View.inflate(FinanceProjectBorrowerActivity.this, R.layout.loading_item, null);
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

    void loadMore() {
        getBorrowerRecordService.getFinanceProjectInvestRecord(mPrjId, String.valueOf(mP++), mPageSize);
    }

    private class ViewHolder {
        TextView tv_name;
        TextView tv_id_num;
        TextView tv_amount;
    }

    class Item {
        public String current_page;// -> 当前页
        public String total_page;// -> 总页数
        public String total;// -> 数据总条数
    }
}
