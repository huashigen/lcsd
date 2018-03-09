package com.lcshidai.lc.ui.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.GoldFinanceRecordImpl;
import com.lcshidai.lc.model.account.GoldFinanceRecord;
import com.lcshidai.lc.model.account.GoldFinanceRecordJson;
import com.lcshidai.lc.service.account.HttpGoldFinanceRecordService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.NetUtils;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.xlvfresh.XListView;
import com.lcshidai.lc.widget.xlvfresh.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 理财金 投资记录 ，获取记录
 */
public class GoldFinanceRecordActivity extends TRJActivity implements
        OnClickListener, IXListViewListener, GoldFinanceRecordImpl {
    HttpGoldFinanceRecordService hgfls;
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;

    View mProgressBar, fragmentEmpty, rl_empty;
    private XListView mListView;
    private ItemAdapter mAdapter;
    private List<ProductItem> mfilelist;
    public boolean isRundata = false;
    private int mPagesize = 10;
    private int mP = 1;
    boolean hasMore;
    private ImageView imgSourceBottom;// icon
    private TextView tvSourceBottom;// 拥有理财金
    private TextView tvSourceMoney;// 金额
    private boolean flagUrl = false;
    private TextView tvTitle3, tvTitle2, tvTitle1;
    private View linearEmpty;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gold_finance_record);
        hgfls = new HttpGoldFinanceRecordService(this, this);
        initView();
        loadData();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        flag = true;
        mProgressBar = findViewById(R.id.progressContainer);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setVisibility(View.VISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        imgSourceBottom = (ImageView) findViewById(R.id.imgSourceBottom);
        tvSourceBottom = (TextView) findViewById(R.id.tvSourceBottom);
        tvSourceMoney = (TextView) findViewById(R.id.tvSourceMoney);
        tvTitle1 = (TextView) findViewById(R.id.tvTitle1);
        tvTitle2 = (TextView) findViewById(R.id.tvTitle2);
        tvTitle3 = (TextView) findViewById(R.id.tvTitle3);
        linearEmpty = findViewById(R.id.linearEmpty);
        Intent intent = getIntent();
        String title = intent.getStringExtra("mTvTitle");
        String sourceMoney = intent.getStringExtra("sourceMoney");
        flagUrl = intent.getBooleanExtra("flagUrl", false);
        mTvTitle.setText(title);
        tvSourceMoney.setText(sourceMoney);
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.GONE);

        mBackBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        mfilelist = new ArrayList<ProductItem>();
        mAdapter = new ItemAdapter(this);
        mProgressBar = findViewById(R.id.progressContainer);
        mListView = (XListView) findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mListView.setPullLoadEnable(false);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this);
        fragmentEmpty = findViewById(R.id.fragmentEmpty);
        rl_empty = findViewById(R.id.rl_empty);

        if (flagUrl) {
            tvSourceBottom.setText(getResources().getString(
                    R.string.gold_text_11));
            tvSourceMoney.setTextColor(getResources().getColor(
                    R.color.account_manage_finance_text1));
            imgSourceBottom.setImageResource(R.drawable.icon_licaijin_bottom2);
            tvTitle1.setText(getResources().getString(R.string.gold_text_4));
            tvTitle2.setText(getResources().getString(R.string.gold_text_5));
            tvTitle3.setText(getResources().getString(R.string.gold_text_6));
        } else {
            tvSourceBottom.setText(getResources().getString(
                    R.string.gold_text_12));
            tvSourceMoney.setTextColor(getResources().getColor(
                    R.color.xzd_qidai));
            imgSourceBottom
                    .setImageResource(R.drawable.icon_licaijin_bottom_jilu2);
            tvTitle1.setText(getResources().getString(R.string.gold_text_8));
            tvTitle2.setText(getResources().getString(R.string.gold_text_9));
            tvTitle3.setText(getResources().getString(R.string.gold_text_10));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRundata = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //
            case R.id.btn_back:
                GoldFinanceRecordActivity.this.finish();
                break;

        }
    }

    public void ld(boolean isReload) {
        if (!isReload && mAdapter != null && mAdapter.size > 1) {
            mListView.showHeader();
        } else {
            reLoadData();
        }
    }

    public synchronized void reLoadData() {
        if (mAdapter != null && !isRundata) {

            hasMore = false;
            mAdapter.clear();
            mListView.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            mP = 1;
            mProgressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.linearEmpty).setVisibility(View.GONE);
            loadData();
        }
    }

    boolean isDown = false;

    public synchronized void reLoadDataDown() {
        if (mAdapter != null && !isRundata) {
            isDown = true;
            hasMore = false;
            mP = 1;
            loadData();
        }
    }

    private void loadData() {
        if (!NetUtils.isNetworkConnected(GoldFinanceRecordActivity.this)) {
            mProgressBar.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
            linearEmpty.setVisibility(View.VISIBLE);
            rl_empty.setVisibility(View.VISIBLE);
            rl_empty.setBackgroundColor(getResources().getColor(R.color.line_listview));
            return;
        }
        isRundata = true;
        hgfls.gainGoldFinanceRecord(mP, mPagesize, flagUrl);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            GoldFinanceRecordActivity.this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public class ItemAdapter extends ArrayAdapter<GoldFinanceRecord> {
        private Context mContext;
        private List<GoldFinanceRecord> mfilelist = new ArrayList<GoldFinanceRecord>();
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
        public void add(GoldFinanceRecord object) {
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
        public GoldFinanceRecord getItem(int position) {
            return mfilelist.get(position);
        }

        @Override
        public View getView(final int position, View convertView,
                            final ViewGroup parent) {

            try {
                final ViewHolders vh;
                if (position < size) {
                    if (convertView == null || convertView.getTag() == null) {
                        vh = new ViewHolders();
                        if (flagUrl) {
                            convertView = LayoutInflater.from(mContext)
                                    .inflate(R.layout.gold_finance_record_item,
                                            null);
                        } else {
                            convertView = LayoutInflater.from(mContext)
                                    .inflate(
                                            R.layout.gold_finance_record_item2,
                                            null);
                        }

                        vh.tvSource = (TextView) convertView
                                .findViewById(R.id.tvSource);
                        vh.tvTime = (TextView) convertView
                                .findViewById(R.id.tvTime);
                        vh.tvMoney = (TextView) convertView
                                .findViewById(R.id.tvMoney);
                        vh.tvStutas = (TextView) convertView
                                .findViewById(R.id.tvStutas);
                        vh.tvMStatus = (TextView) convertView
                                .findViewById(R.id.tvMStatus);
                        convertView.setTag(vh);
                    } else {
                        vh = (ViewHolders) convertView.getTag();
                    }
                    final GoldFinanceRecord pi = mfilelist.get(position);
                    vh.tvMoney.setText(pi.money);
                    if (flagUrl) {
                        vh.tvTime.setText(pi.ctime);
                        vh.tvSource.setText(pi.source);
                        if (!StringUtils.isEmpty(pi.source)) {
                            // if (pi.source.equals("1")) {
                            // vh.tvSource.setText("新用户注册");
                            // } else if (pi.source.equals("2")) {
                            // vh.tvSource.setText("推荐用户注册");
                            // } else if (pi.source.equals("3")) {
                            // vh.tvSource.setText("3参加活动");
                            // }
                        }
                        if (!StringUtils.isEmpty(pi.mstatus)) {
                            if (pi.mstatus.equals("1")) {
                                vh.tvStutas.setText("使用中");
                                vh.tvStutas.setTextColor(getResources()
                                        .getColor(R.color.xzd_qidai));
                            } else if (pi.mstatus.equals("2")) {
                                vh.tvStutas.setText("已使用");
                                vh.tvStutas.setTextColor(getResources()
                                        .getColor(R.color.dialog_tv2));
                            } else if (pi.mstatus.equals("3")) {
                                vh.tvStutas.setText("已过期");
                                vh.tvStutas.setTextColor(getResources()
                                        .getColor(R.color.dialog_tv4));
                            }

                        }
                        vh.tvMStatus.setText(pi.tyj_time_limit);
                    } else {

                        vh.tvSource.setText(pi.prj_name);
                        vh.tvTime.setText(pi.muji_day + "/年化" + pi.year_rate);

                        vh.tvStutas.setText(pi.yield);
                        vh.tvStutas.setTextColor(getResources().getColor(
                                R.color.licaijin_moneny));

                        if (!StringUtils.isEmpty(pi.status)) {
                            if (pi.status.equals("1")) {

                                vh.tvMStatus.setTextColor(getResources()
                                        .getColor(R.color.xzd_qidai));
                            } else if (pi.mstatus.equals("2")) {

                                vh.tvMStatus.setTextColor(getResources()
                                        .getColor(R.color.dialog_tv3));
                            }
                            vh.tvMStatus.setText(pi.info);
                        }

                    }

                } else {
                    View view = View.inflate(GoldFinanceRecordActivity.this,
                            R.layout.loading_item, null);
                    loadData();
                    return view;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            return convertView;
        }

        public class ViewHolders {

            TextView tvSource;
            TextView tvTime;
            TextView tvMoney;
            TextView tvStutas;
            TextView tvMStatus;
        }
    }

    public static class ProductItem implements Serializable {

        private static final long serialVersionUID = 1L;
        /**
         *
         */
        public String prj_name;// ："项目名称",
        public String money;// :"理财金",
        public String yield;// :"收益",
        public String muji_day;// :"募集天数",
        public String status;// :"回款状态1待回款2已回款",
        public String mstatus;// : "理财金使用状态1使用中2已使用3已过期",
        public String expect_repay_time;// :"预计回款时间",
        public String source;// ："来源类型1新用户注册2推荐用户注册3参加活动",

        public String ctime;//
        public String mtime;//
        public String id;//
        public String uid;//

        public String tyj_time_limit;
        public String year_rate;
        public String actual_repay_time;
        public String info;
    }

    public String formatLong(long time) {
        StringBuffer str = new StringBuffer();
        long second = time;
        // pi.start_bid_time_diff = second + "";
        long s = second % 60; // 秒
        long mi = (second - s) / 60 % 60; // 分钟
        long h = ((second - s) / 60 - mi) / 60 % 24; // 小时
        long d = (((second - s) / 60 - mi) / 60 - h) / 24; // 天
        if (d > 0) {
            str.append(d + "天");
        }
        str.append(h + "小时");
        str.append(mi + "分");
        str.append(s + "秒");
        return str.toString();
    }

    @Override
    public void onRefresh() {
        reLoadDataDown();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    @Override
    public void gainGoldFinanceRecordsuccess(GoldFinanceRecordJson response) {
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
                    List<GoldFinanceRecord> jarray = null;

                    jarray = response.getData().getList();

                    hasMore = jarray.size() >= mPagesize;
                    if (hasMore) {
                        if (StringUtils.isInteger(response
                                .getData().getTotal_page())
                                && StringUtils
                                .isInteger(response
                                        .getData().getCurrent_page())) {
                            int count = Integer
                                    .parseInt(response
                                            .getData().getTotal_page());
                            if (Integer.parseInt(response
                                    .getData().getCurrent_page()) >= count) {
                                hasMore = false;
                            }
                        }
                    }
                    GoldFinanceRecord jobj = null;
                    ProductItem item = null;
                    for (int j = 0; j < jarray.size(); j++) {
                        jobj = jarray.get(j);
                        item = new ProductItem();
//						for (java.lang.reflect.Field field2 : item
//								.getClass().getFields()) {
//							String str = jobj.optString(field2
//									.getName());
//							if (str != null
//									&& !str.equals("null")) {
//								field2.set(item, str.trim());
//							} else {
//								field2.set(item, "");
//							}
//						}

                        mAdapter.add(jobj);
                    }
                } else {
                    hasMore = false;
                }

            } else {
                hasMore = false;
            }
        } catch (Exception e) {
            hasMore = false;
            e.printStackTrace();
            mProgressBar.setVisibility(View.GONE);
        } finally {
            mProgressBar.setVisibility(View.GONE);

            isRundata = false;
            if (mAdapter.mfilelist.size() <= 0) {
                mListView.setVisibility(View.GONE);
                linearEmpty.setVisibility(View.VISIBLE);
                fragmentEmpty.setVisibility(View.GONE);
                rl_empty.setVisibility(View.VISIBLE);
                rl_empty.setBackgroundColor(getResources().getColor(R.color.line_listview));
            } else {
                linearEmpty.setVisibility(View.GONE);
                fragmentEmpty.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void gainGoldFinanceRecordfail() {
        ToastUtil.showToast(GoldFinanceRecordActivity.this,
                "网络不给力!");
        mProgressBar.setVisibility(View.GONE);
        if (mAdapter.mfilelist.size() <= 0) {
            mListView.setVisibility(View.GONE);
            linearEmpty.setVisibility(View.VISIBLE);
            rl_empty.setVisibility(View.VISIBLE);
            rl_empty.setBackgroundColor(getResources().getColor(R.color.line_listview));
            fragmentEmpty.setVisibility(View.GONE);
        } else {
            linearEmpty.setVisibility(View.GONE);
            fragmentEmpty.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }

        mAdapter.notifyDataSetChanged();
        isRundata = false;
    }
}
