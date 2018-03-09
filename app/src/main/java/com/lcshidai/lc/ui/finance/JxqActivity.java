package com.lcshidai.lc.ui.finance;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.BroadCastImpl;
import com.lcshidai.lc.impl.finance.FinanceJxqImpl;
import com.lcshidai.lc.model.finance.FinanceJxqItemData;
import com.lcshidai.lc.model.finance.FinanceJxqJson;
import com.lcshidai.lc.service.finance.HttpFinanceJxqService;
import com.lcshidai.lc.ui.adapter.FinanceJxqAdapter;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.RewardCheckUtil;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.xlvfresh.XListViewNew;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 加息券列表
 */
public class JxqActivity extends TRJActivity implements OnClickListener, FinanceJxqImpl, XListViewNew.IXListViewListener,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    @Bind(R.id.ib_top_bar_back)
    ImageButton ibTopBarBack;
    @Bind(R.id.tv_top_bar_title)
    TextView tvTopBarTitle;
    @Bind(R.id.tv_top_bar_right_action)
    TextView tvTopBarRightAction;
    @Bind(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @Bind(R.id.lv_jxq)
    XListViewNew lvJxq;
    @Bind(R.id.tv_empty)
    TextView tvEmpty;
    @Bind(R.id.ll_empty_container)
    LinearLayout llEmptyContainer;
    @Bind(R.id.fl_main_container)
    FrameLayout flMainContainer;
    @Bind(R.id.ll_main_container)
    LinearLayout llMainContainer;
    private String prj_id, amount, reward_type, bouns_rate, bouns_prj_term, jxq_id;
    private int isCollection = 0;

    private List<FinanceJxqItemData> mDataList;
    private FinanceJxqAdapter mAdapter;

    private HttpFinanceJxqService financeJxqService;

    private int mPage;
    private boolean mHasMore;
    private boolean mIsLastRow;
    private final int REFRESH = 1;
    private final int LOADMORE = 2;
    private int DataChangeType = REFRESH;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_jxq);
        ButterKnife.bind(this);

        prj_id = getIntent().getStringExtra("prj_id");
        amount = getIntent().getStringExtra("amount");
        reward_type = getIntent().getStringExtra("reward_type");
        bouns_rate = getIntent().getStringExtra("bouns_rate");
        bouns_prj_term = getIntent().getStringExtra("bouns_prj_term");
        jxq_id = getIntent().getStringExtra("jxq_id");
        isCollection = getIntent().getIntExtra(Constants.Project.IS_COLLECTION, 0);

        financeJxqService = new HttpFinanceJxqService(this, this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        ibTopBarBack.setOnClickListener(this);
        tvTopBarTitle.setText("加息券");
        tvEmpty.setText("暂无加息券");
        mDataList = new ArrayList<FinanceJxqItemData>();
        mAdapter = new FinanceJxqAdapter(mDataList);
        mAdapter.setCurrentSelectJxqId(jxq_id);
        lvJxq.setAdapter(mAdapter);
        lvJxq.setXListViewListener(this);
        lvJxq.setOnScrollListener(this);
        lvJxq.setOnItemClickListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastImpl.ACTION_CLEAR_CHECK_HB);

        // 请求数据
        financeJxqService.getJxqList(prj_id, amount, isCollection);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;

            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        if (RewardCheckUtil.getInstance().is_perform_clicked()) {
            if (RewardCheckUtil.getInstance().getHBCheckReference().get() != null) {
                RewardCheckUtil.getInstance().getHBCheckReference().get().updateJxq(true);
            }
        }
    }

    @Override
    public void gainJxqListSuccess(FinanceJxqJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                lvJxq.stopRefresh();
                lvJxq.loadMoreDone();
                lvJxq.setRefreshTime();
                List<FinanceJxqItemData> data = response.getData();
                if (data != null && data.size() > 0) {
                    llEmptyContainer.setVisibility(View.GONE);
                } else {
                    llEmptyContainer.setVisibility(View.VISIBLE);
                }
                if (DataChangeType == REFRESH) {
                    mDataList.clear();
                }
                mDataList.addAll(data);
                mAdapter.notifyDataSetChanged();
            } else {
                llEmptyContainer.setVisibility(View.VISIBLE);
            }
        } else {
            llEmptyContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void gainJxqListFail() {

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
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onLoadMore() {
        if (mHasMore) {
            loadMore();
        }
    }

    private void loadMore() {
        DataChangeType = LOADMORE;
        lvJxq.loadMore();
    }

    private void refresh() {
        DataChangeType = REFRESH;
//        initData();
        //假刷新
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                lvJxq.stopRefresh();
                lvJxq.loadMoreDone();
                lvJxq.setRefreshTime();

            }
        }, 100);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FinanceJxqItemData financeMJQItemData = mDataList.get(position - 1);
        if (financeMJQItemData != null && financeMJQItemData.getIs_usable() == 1) {// 如果可用
            RewardCheckUtil.getInstance().setIs_perform_clicked(true);
            mAdapter.isClearSelected(true);
            mAdapter.setCurrentSelectJxqId(financeMJQItemData.getId());
            mAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showShortToast(this, "当前项目不能使用该加息券");
        }
    }
}
