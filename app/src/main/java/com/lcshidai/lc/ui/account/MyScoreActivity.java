package com.lcshidai.lc.ui.account;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.ScoreRecordImpl;
import com.lcshidai.lc.model.account.ScoreRecordData;
import com.lcshidai.lc.model.account.ScoreRecordInfo;
import com.lcshidai.lc.model.account.ScoreRecordJson;
import com.lcshidai.lc.service.account.HttpScoreRecordService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.xlvfresh.XListViewNew;

import java.util.ArrayList;

/**
 * 积分记录 Activity
 * <p/>
 * Created by RandyZhang on 16/8/29.
 */
public class MyScoreActivity extends TRJActivity implements View.OnClickListener, ScoreRecordImpl,
        XListViewNew.IXListViewListener, AbsListView.OnScrollListener {

    private ArrayList<ScoreRecordInfo> recordInfoArrayList = new ArrayList<ScoreRecordInfo>();
    private ScoreRecordAdapter scoreRecordAdapter = null;

    private XListViewNew lvScoreRecord;

    private HttpScoreRecordService scoreRecordService;

    private int page = 1;
    private int pageSize = 10;
    private boolean mIsLastRow = false;
    private boolean mHasMore = true;
    private boolean mIsFromStartPageOrFirstLoad = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);

        initViewsAndEvents();
    }

    private void initViewsAndEvents() {
        scoreRecordService = new HttpScoreRecordService(this, this);

        scoreRecordAdapter = new ScoreRecordAdapter(mContext);

        ImageView ivBackButton = (ImageView) findViewById(R.id.ib_top_bar_back);
        TextView tvTitleText = (TextView) findViewById(R.id.tv_top_bar_title);

        TextView tvMyScore = (TextView) findViewById(R.id.tv_my_score);
        TextView tvExchangeRecord = (TextView) findViewById(R.id.tv_exchange_record);
        lvScoreRecord = (XListViewNew) findViewById(R.id.lv_score_record);
        TextView tvUseRecord = (TextView) findViewById(R.id.tv_use_score);
        tvUseRecord.setVisibility(View.GONE);
        tvExchangeRecord.setVisibility(View.GONE);
        ivBackButton.setOnClickListener(this);
        tvExchangeRecord.setOnClickListener(this);
        tvUseRecord.setOnClickListener(this);


        lvScoreRecord.setAdapter(scoreRecordAdapter);
        lvScoreRecord.setXListViewListener(this);
        lvScoreRecord.setOnScrollListener(this);
        lvScoreRecord.setPullRefreshEnable(true);

        tvTitleText.setText("积分记录");

        String totalScore = getIntent().getStringExtra("totalScore");
        String todayScore = getIntent().getStringExtra("todayScore");
//        积分字体颜色设置
//        int colorStart = Color.parseColor("#ffecb5");
//        int colorEnd = Color.parseColor("#f7971c");
//        Shader shader = new LinearGradient(0, 0, 0, 80, colorStart, colorEnd, Shader.TileMode.CLAMP);
//        tvMyScore.getPaint().setShader(shader);
        if (!CommonUtil.isNullOrEmpty(totalScore)) {
            tvMyScore.setText(totalScore);
        }
        mIsFromStartPageOrFirstLoad = true;
        loadData(page, pageSize);
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
            case R.id.tv_exchange_record:
                ToastUtil.showToast((TRJActivity) mContext, "兑换记录");
                break;

            case R.id.tv_use_score:
                ToastUtil.showToast((TRJActivity) mContext, "使用积分");
                break;
            default:
                break;
        }

    }

    @Override
    public void onGetScoreListSuccess(ScoreRecordJson response) {
        if (null != response) {
            if (response.getBoolen().equals("1")) {
                // reset listView's state

                lvScoreRecord.stopRefresh();
                lvScoreRecord.stopLoadMore();
                lvScoreRecord.loadMoreDone();
                lvScoreRecord.setRefreshTime();
                ScoreRecordData scoreRecordData = response.getData();
                if (null != scoreRecordData) {
                    ArrayList<ScoreRecordInfo> tempList = (ArrayList<ScoreRecordInfo>) scoreRecordData.getData();
                    // 分页处理
                    if (mIsFromStartPageOrFirstLoad) {
                        recordInfoArrayList.clear();
                        mIsFromStartPageOrFirstLoad = false;
                    }
                    if (tempList != null) {
                        recordInfoArrayList.addAll(tempList);
                        if (pageSize > tempList.size() || scoreRecordData.getTotal_page().equals(page + "")) {
                            mHasMore = false;
                        }
                    }

                    scoreRecordAdapter.setList(recordInfoArrayList);
                    scoreRecordAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onGetScoreListFailed() {
        // 失败处理
        ToastUtil.showToast((TRJActivity) mContext, "获取积分记录失败");
    }

    @Override
    public void onRefresh() {
        mIsFromStartPageOrFirstLoad = true;
        page = 1;
        loadData(page, pageSize);
    }

    @Override
    public void onLoadMore() {
        mIsFromStartPageOrFirstLoad = false;
        if (mHasMore) {
            page++;
            loadData(page, pageSize);
        }
    }

    private void loadData(int page, int pageSize) {
        scoreRecordService.getScoreRecordList(page, pageSize);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (!mIsFromStartPageOrFirstLoad && mIsLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
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


    class ScoreRecordAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<ScoreRecordInfo> list;

        public ScoreRecordAdapter(Context context) {
            this.context = context;
        }

        public void setList(ArrayList<ScoreRecordInfo> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (null != list) {
                return list.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (null != list && list.size() > 0) {
                return list.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.score_record_list_item, parent, false);
                holder.tvInOrOut = (TextView) convertView.findViewById(R.id.tv_in_or_out);
                holder.tvScore = (TextView) convertView.findViewById(R.id.tv_score);
                holder.tvScoreDesc = (TextView) convertView.findViewById(R.id.tv_score_desc);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (null != list && list.get(position) != null) {
                ScoreRecordInfo scoreRecordInfo = list.get(position);
                if (!CommonUtil.isNullOrEmpty(scoreRecordInfo.getInOrOut())) {
                    if (scoreRecordInfo.getInOrOut().equals("1")) {
                        holder.tvInOrOut.setText("+");
                    } else {
                        holder.tvInOrOut.setText("-");
                    }
                }
                if (!CommonUtil.isNullOrEmpty(scoreRecordInfo.getScore())) {
                    holder.tvScore.setText(scoreRecordInfo.getScore());
                }
                if (!CommonUtil.isNullOrEmpty(scoreRecordInfo.getDescribe())) {
                    holder.tvScoreDesc.setText(scoreRecordInfo.getDescribe());
                }
                if (!CommonUtil.isNullOrEmpty(scoreRecordInfo.getTime())) {
                    holder.tvDate.setText(scoreRecordInfo.getTime());
                }
            }

            return convertView;
        }

        class ViewHolder {
            TextView tvInOrOut;
            TextView tvScore;
            TextView tvScoreDesc;
            TextView tvDate;
        }
    }
}