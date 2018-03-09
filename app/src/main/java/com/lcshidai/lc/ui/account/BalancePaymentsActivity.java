package com.lcshidai.lc.ui.account;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.BalancePaymentsImpl;
import com.lcshidai.lc.model.account.BalancePaymentsData;
import com.lcshidai.lc.model.account.BalancePaymentsJson;
import com.lcshidai.lc.model.account.BalancePaymentsList;
import com.lcshidai.lc.model.account.BalancePaymentsListEntity;
import com.lcshidai.lc.service.account.HttpBalancePaymentsService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.widget.ppwindow.BalancePayMentsTypePopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * 收支记录
 */
public class BalancePaymentsActivity extends TRJActivity implements
        BalancePaymentsImpl, OnClickListener,
        BalancePayMentsTypePopupWindow.ChooseListener, OnScrollListener {
    HttpBalancePaymentsService hbps;
    private Context mContext;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    ImageButton mBackBtn;
    private RelativeLayout type_select_rl;
    private BalancePayMentsTypePopupWindow bpt_pp;
    private TextView type_select_tv;
    private ImageView type_select_iv;

    private int selectStatus = -1; // 查询状态
    private ListView bp_lv;
    private List<BPEntity> bpEntityList;
    private BPAdapter bpAdapter;

    private View footerView;
    private int currentPage = 1;
    private int perPage = 10;
    private int totalPages = 0;
    private boolean isLoading = false; // 是否正在加载数据

    private RelativeLayout empty_rl;
    private Dialog loading;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hbps = new HttpBalancePaymentsService(this, this);
        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_balance_payments);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("资金记录");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);

        type_select_rl = (RelativeLayout) findViewById(R.id.balance_payments_typeselect_rl);
        type_select_rl.setOnClickListener(this);

        type_select_tv = (TextView) findViewById(R.id.balance_payments_typeselect_tv);
        type_select_iv = (ImageView) findViewById(R.id.balance_payments_typeselect_iv);
        empty_rl = (RelativeLayout) findViewById(R.id.rl_empty);
        loading = createLoadingDialog(mContext, "数据加载中", true);

        bpt_pp = new BalancePayMentsTypePopupWindow(mContext, this, 1);

        bp_lv = (ListView) findViewById(R.id.balance_payments_list_lv);
        footerView = LayoutInflater.from(mContext).inflate(R.layout.loading_item, null);
        bp_lv.setOnScrollListener(this);

        bpEntityList = new ArrayList<>();
        bpAdapter = new BPAdapter();
        if (!loading.isShowing()) {
            loading.show();
        }
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.balance_payments_typeselect_rl:
                bpt_pp.goAnim(v, 0, 0, 1);
                type_select_iv.setImageDrawable(getResources().getDrawable(
                        R.drawable.icon_indicator_up));
                break;
        }

    }

    @Override
    public void onDismiss() {
        type_select_iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_indicator_down));
    }

    @Override
    public void chooseItem(int markItem) {
        switch (markItem) {
            // 全部
            case 0:
                type_select_tv.setText("全部");
                selectStatus = -1;
                break;
            // 充值
            case 1:
                type_select_tv.setText("充值");
                selectStatus = 1;
                break;
            // 投资
            case 2:
                type_select_tv.setText("投资");
                selectStatus = 2;
                break;
            // 回款
            case 3:
                type_select_tv.setText("回款");
                selectStatus = 3;
                break;
            // 提现
            case 4:
                type_select_tv.setText("提现");
                selectStatus = 4;
                break;
            // 奖励
            case 5:
                type_select_tv.setText("奖励");
                selectStatus = 5;
                break;
            // 债权交易
//		case 6:
//			type_select_tv.setText("变现交易");
//			selectStatus = 6;
//			break;
            //
//		case 7:
//			type_select_tv.setText("免费提现额度");
//			selectStatus = 7;
//			break;
        }
        reloadData();
    }

    class BPEntity {
        String status; // 状态类型: 充值-1 投资-2 回款-3 提现-4 奖金-5
        // 债权交易-6（买入还是卖出可通过show_type判断） 资金服务费抵用券-7
        String ctime; // 时间
        String money_change; // 账户变化金额
        int show_type; // 变化类型 1-存入 2-支出 3-冻结
        String amount; // 当前可用余额
        String prj_name; // 项目名称 没有为'--'
        String record_no; // 流水号
        String remark;
        String money_change1;
        String show_type1;
        String type;
    }

    class ViewHolder {
        View ll_left;
        TextView tvLeft1;
        TextView tvLeft2;
        TextView title_type_tv;
        TextView title_time_tv;
        TextView tvRight1;
        TextView tvRight2;
        TextView amount_tv;
        TextView prj_name1_tv;
        TextView prj_name2_tv;
        TextView record_no_tv;
    }

    class BPAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bpEntityList.size();
        }

        @Override
        public Object getItem(int position) {
            return bpEntityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BPEntity entity = (BPEntity) getItem(position);
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_balance_payments_list_item, null);
                holder.title_type_tv = (TextView) convertView.findViewById(R.id.bp_item_title_type);
                holder.title_time_tv = (TextView) convertView.findViewById(R.id.bp_item_title_time);
                holder.tvRight1 = (TextView) convertView.findViewById(R.id.tvRight1);
                holder.tvRight2 = (TextView) convertView.findViewById(R.id.tvRight2);
                holder.amount_tv = (TextView) convertView.findViewById(R.id.bp_item_amount_tv);
                holder.prj_name1_tv = (TextView) convertView.findViewById(R.id.bp_item_prj_name1_tv);
                holder.prj_name2_tv = (TextView) convertView.findViewById(R.id.bp_item_prj_name2_tv);
                holder.record_no_tv = (TextView) convertView.findViewById(R.id.bp_item_record_no_tv);
                holder.tvLeft1 = (TextView) convertView.findViewById(R.id.tvLeft1);
                holder.tvLeft2 = (TextView) convertView.findViewById(R.id.tvLeft2);
                holder.ll_left = convertView.findViewById(R.id.ll_left);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title_type_tv.setText(entity.type);
            holder.title_time_tv.setText(entity.ctime);
            holder.tvRight1.setText("");
            holder.tvRight2.setText("");
            holder.tvLeft1.setText("");
            holder.tvLeft2.setText("");
            // 存入
            if (entity.show_type == 1) {
                holder.tvLeft1.setText(entity.money_change);
                holder.tvLeft2.setText("存入");
            }
            // 支出
            else if (entity.show_type == 2) {
                holder.tvRight1.setText(entity.money_change);
                holder.tvRight2.setText("支出");
            }
            // 冻结
            else {
                holder.tvRight1.setText(entity.money_change);
                holder.tvRight2.setText("冻结");
            }
            holder.amount_tv.setText(entity.amount);
            holder.prj_name1_tv.setText(entity.remark);

            if (entity.money_change1 != null && !entity.money_change1.equals("")) {
                if (entity.show_type1.equals("1")) {// 黄色FF6100
                    holder.tvLeft1.setText(entity.money_change1);
                    holder.tvLeft2.setText("解冻");
                } else {// 绿色
                    holder.tvRight2.setText("支出");
                    holder.tvRight1.setText(entity.money_change1);
                }
            }
            holder.record_no_tv.setText(entity.record_no);
            return convertView;
        }

    }

    private void loadData() {
        hbps.gainBalancePayments(selectStatus, currentPage, perPage);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // 判断是不是最后一个
        if ((firstVisibleItem + visibleItemCount) == totalItemCount && totalPages >= currentPage
                && !isLoading) {
            isLoading = true;
            loadData();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    private void reloadData() {
        currentPage = 1;
        totalPages = 0;
        bpEntityList.clear();
        if (bp_lv.getFooterViewsCount() > 0) {
            bp_lv.removeFooterView(footerView);
        }
        if (!loading.isShowing()) {
            loading.show();
        }
        loadData();
    }

    @Override
    public void gainBalancePaymentssuccess(BalancePaymentsJson response) {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    BalancePaymentsData dataObj = response.getData();
                    BalancePaymentsList listObj = dataObj.getListdata();
                    totalPages = listObj.getTotalPages();
                    List<BalancePaymentsListEntity> dataArray = listObj.getData();
                    if (null != dataArray && dataArray.size() > 0) {
                        empty_rl.setVisibility(View.GONE);
                        bp_lv.setVisibility(View.VISIBLE);
                        for (int i = 0; i < dataArray.size(); i++) {
                            BPEntity entity = new BPEntity();
                            entity.type = dataArray.get(i).getType();
                            entity.status = dataArray.get(i).getStatus();
                            entity.ctime = dataArray.get(i).getCtime();
                            entity.money_change = dataArray.get(i).getMoney_change();
                            entity.show_type = dataArray.get(i).getShow_type();
                            entity.amount = dataArray.get(i).getAmount();
                            entity.prj_name = dataArray.get(i).getPrj_name();
                            entity.record_no = dataArray.get(i).getRecord_no();
                            entity.remark = dataArray.get(i).getRemark();
                            entity.money_change1 = dataArray.get(i).getMoney_change1();
                            entity.show_type1 = dataArray.get(i).getShow_type1();
                            bpEntityList.add(entity);
                        }
                        if (currentPage == 1) {
                            if (totalPages > currentPage) {
                                bp_lv.addFooterView(footerView);
                            }
                            bp_lv.setAdapter(bpAdapter);
                        } else {
                            if (totalPages == currentPage) {
                                bp_lv.removeFooterView(footerView);
                            }
                            bpAdapter.notifyDataSetChanged();
                        }
                        currentPage += 1;
                        isLoading = false;
                    } else {
                        if (currentPage == 1) {
                            bp_lv.setVisibility(View.GONE);
                            empty_rl.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainBalancePaymentsfail() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        showToast("网络不给力");
    }

}
