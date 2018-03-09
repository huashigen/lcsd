package com.lcshidai.lc.ui.account;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.RepaymentListImpl;
import com.lcshidai.lc.model.account.RepaymentList;
import com.lcshidai.lc.model.account.RepaymentListData;
import com.lcshidai.lc.model.account.RepaymentListJson;
import com.lcshidai.lc.service.account.HttpRepaymentListService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.StringUtils;

/**
 * 回款列表
 *
 * @author 000853
 */
public class RepaymentListActivity extends TRJActivity implements
        RepaymentListImpl, OnClickListener {
    HttpRepaymentListService hrls;
    private Context mContext;

    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    ImageButton mBackBtn;
    private RelativeLayout emptyRL;

    private String mId;

    private LinearLayout mainLL;
    private ListView repaymentListView;
    private List<Repayment> repaymentList;
    private RepaymentListAdapter repaymentAdapter;
    private TextView tv_sum_pri_interest, tv_sum_principal, tv_sum_yield, tvInfoUnit;

    View progressContainer;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mId = bundle.getString("id");
        }
        hrls = new HttpRepaymentListService(this, this);
        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_repayment_new);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("还款计划");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        tv_sum_pri_interest = (TextView) findViewById(R.id.tv_sum_pri_interest);
        tv_sum_principal = (TextView) findViewById(R.id.tv_sum_principal);
        tv_sum_yield = (TextView) findViewById(R.id.tv_sum_yield);
        tvInfoUnit = (TextView) findViewById(R.id.tv_info_un);

        repaymentList = new ArrayList<RepaymentListActivity.Repayment>();

        mainLL = (LinearLayout) findViewById(R.id.repayment_ll_main);
        emptyRL = (RelativeLayout) findViewById(R.id.rl_empty);
        repaymentListView = (ListView) findViewById(R.id.repayment_lv_list);
        progressContainer = findViewById(R.id.progressContainer);

        repaymentAdapter = new RepaymentListAdapter();
        repaymentListView.setAdapter(repaymentAdapter);
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.btn_back:
                RepaymentListActivity.this.finish();
                break;
        }
    }

    class Repayment {
        String repay_periods; // 回款期数
        String repay_periods_view;// 新回款期数
        String pri_interest; // 应收本息
        String principal; // 应收本金
        String yield; // 应收利息
        String rest_principal; // 剩余本金
        String status; // 状态
        String repay_date; // 回款日期
    }

    private void loadData() {
        progressContainer.setVisibility(View.VISIBLE);
        hrls.gainRepaymentList(mId);
    }

    class RepaymentListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return repaymentList.size();
        }

        @Override
        public Object getItem(int position) {
            return repaymentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_repayment_new_list_item, null);
                holder.tv_1 = (TextView) convertView.findViewById(R.id.repayment_item_tv_a);
                holder.tv_2 = (TextView) convertView.findViewById(R.id.repayment_item_tv_b);
                holder.tv_3 = (TextView) convertView.findViewById(R.id.repayment_item_tv_c);
                holder.tv_4 = (TextView) convertView.findViewById(R.id.repayment_item_tv_d);
                holder.tv_5 = (TextView) convertView.findViewById(R.id.repayment_item_tv_e);
                holder.tv_time = (TextView) convertView.findViewById(R.id.repayment_item_tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (repaymentList.size() > 0) {
                Repayment repayment = repaymentList.get(position);
                holder.tv_1.setText(repayment.repay_periods_view);
                holder.tv_2.setText(moneyString(repayment.pri_interest));
                holder.tv_3.setText(moneyString(repayment.principal));
                holder.tv_4.setText(moneyString(repayment.yield));
                String rest = moneyString(repayment.rest_principal);
                if (StringUtils.isEmpty(rest)) {
                    rest = "0.00";
                } else {
                    if ("-".equals(rest)) {
                        rest = "0.00";
                    }
                }
                holder.tv_5.setText(rest);
                holder.tv_time.setText(moneyString(repayment.repay_date));
                if (repayment.status.equals("回款结束") || repayment.status.equals("已回款")) {
                    holder.tv_time.append(" (已回款)");
                }
            }
            return convertView;
        }
    }

    class ViewHolder {
        TextView tv_1;
        TextView tv_2;
        TextView tv_3;
        TextView tv_4;
        TextView tv_5;
        TextView tv_time;
    }

    private String moneyString(String str) {
        String money = str.replaceAll("元", "");
        if (money.equals("") || money.equals("0.00") || money.equals("0")) {
            return "-";
        }
        return money;
    }

    @Override
    public void gainRepaymentListsuccess(RepaymentListJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    RepaymentListData dataObj = response.getData();
                    String money = dataObj.getMoney();
                    String time_limit = dataObj.getTime_limit();
                    String sum_pri_interest = dataObj.getSum_pri_interest();
                    String sum_principal = dataObj.getSum_principal();
                    String sum_yield = dataObj.getSum_yield();

                    if (!StringUtils.isEmpty(sum_pri_interest)) {
                        tv_sum_pri_interest.setText(sum_pri_interest);
                    } else {
                        tv_sum_pri_interest.setText("0.00");
                    }
                    if (!StringUtils.isEmpty(sum_principal)) {
                        tv_sum_principal.setText(sum_principal);
                    } else {
                        tv_sum_principal.setText("0.00");
                    }
                    if (!StringUtils.isEmpty(sum_yield)) {
                        tv_sum_yield.setText(sum_yield);
                    } else {
                        tv_sum_yield.setText("0.00");
                    }

                    List<RepaymentList> listArray = dataObj.getData();
                    boolean isExit = false;
                    if (listArray != null && listArray.size() > 0) {
                        for (int i = 0; i < listArray.size(); i++) {
                            RepaymentList jobj = listArray.get(i);
                            Repayment repay = new Repayment();
                            repay.repay_periods = jobj.getRepay_periods();
                            repay.repay_periods_view = jobj.getRepay_periods_view();
                            repay.pri_interest = jobj.getPri_interest();
                            repay.principal = jobj.getPrincipal();
                            repay.yield = jobj.getYield();
                            repay.rest_principal = jobj.getRest_principal();
                            repay.status = jobj.getStatus();
                            repay.repay_date = jobj.getRepay_date();
                            String info = repay.yield.replace("元", "");
                            repaymentList.add(repay);
                        }
                        mainLL.setVisibility(View.VISIBLE);
                        repaymentAdapter.notifyDataSetChanged();
                        tvInfoUnit.setText("单位：元");
                    } else {
                        emptyRL.setVisibility(View.VISIBLE);
                    }
                } else {
                    showToast(response.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            progressContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void gainRepaymentListfail() {
        progressContainer.setVisibility(View.GONE);
    }

}