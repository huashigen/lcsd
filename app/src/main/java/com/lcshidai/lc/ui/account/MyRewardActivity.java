package com.lcshidai.lc.ui.account;

import java.util.ArrayList;
import java.util.List;

import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.account.MyRewardImpl;
import com.lcshidai.lc.impl.account.MyRewardRuleImpl;
import com.lcshidai.lc.model.account.MyRewardRecordJson;
import com.lcshidai.lc.model.account.MyRewardRuleJson;
import com.lcshidai.lc.service.account.HttpMyRewardRuleService;
import com.lcshidai.lc.service.account.HttpMyRewardService;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.base.TRJActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 我的奖励
 *
 * @author 000331
 */
public class MyRewardActivity extends TRJActivity implements MyRewardImpl,
        MyRewardRuleImpl, OnClickListener, OnScrollListener {
    HttpMyRewardService hmrs;
    HttpMyRewardRuleService hmrrs;
    private Context mContext;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    ImageButton mBackBtn;
    private ListView mListView;
    private List<BonusEntity> mBonusList;
    private BPAdapter bpAdapter;

    private View footerView;
    private int currentPage = 1;
    private int pagesize = 10;
    private int totalPages = 0;
    private boolean isLoading = false; // 是否正在加载数据

    private RelativeLayout empty_rl;
    private Dialog loading;
    private Dialog rewardRuleDialog = null;
    private String switch_wap = "1";
    private Boolean switch_title = false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (getIntent().getExtras() != null) {
            switch_wap = getIntent().getExtras().getString("switch_wap");
            switch_title = getIntent().getExtras().getBoolean("switch_title");
        }
        ///#/myReward
        if (switch_wap != null && switch_wap.equals("1")) {
            Intent intent_activity = new Intent(MyRewardActivity.this, MainWebActivity.class);
            intent_activity.putExtra("isMyReword", true);
            intent_activity.putExtra("title", "我的奖励");
//			
            intent_activity.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/myReward");
            startActivity(intent_activity);
            finish();
        } else {
            hmrs = new HttpMyRewardService(this, this);
            hmrrs = new HttpMyRewardRuleService(this, this);
            initView();
        }
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_my_reward);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        if (switch_title) {
            mTvTitle.setText("历史流水记录");
        } else {
            mTvTitle.setText("我的奖励");
        }
        empty_rl = (RelativeLayout) findViewById(R.id.rl_empty);
        loading = createLoadingDialog(mContext, "数据加载中", true);

        mListView = (ListView) findViewById(R.id.balance_payments_list_lv);
        footerView = LayoutInflater.from(mContext).inflate(
                R.layout.loading_item, null);
        mListView.setOnScrollListener(this);

        mBonusList = new ArrayList<>();
        bpAdapter = new BPAdapter();
        if (!loading.isShowing()) {
            loading.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (switch_wap == null || !switch_wap.equals("1")) {
            loadData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                MyRewardActivity.this.finish();
                break;
        }

    }

    class BonusEntity {
        String id;
        String type; // 红包类型：1-收入，2-支出
        String amount; // 金额
        String serial_no; // 流水号
        String ctime; // 时间
        String rule_id; // 规则ID，查看规则的时候要传递的参数
        String prj_name; // 项目名称，type=2的时候才有的
        String status; // 1-有效，2-失效
        String fetch_time; // 失效时间
        String prj_id; // 项目ID；
        String obj_id; // 查看规则使用
        String obj_type; // 查看规则使用
        String bonus_type_name; // 红包的类型名称
        String memo; // 红包流水记录的备注
    }

    class ViewHolder {
        TextView title_type_tv;
        TextView title_time_tv;
        TextView amount_tv;
        TextView type_tv;
        TextView bonus_time_tv;
        TextView prj_name_tv;
        TextView rule_look_tv;
        TextView serial_no_tv;
        LinearLayout bonus_item_time_ll;
        LinearLayout bonus_item_prj_ll;
        TextView bonus_item_time_view_tv;
        TextView bonus_item_change_view_tv;
    }

    class BPAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBonusList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBonusList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final BonusEntity entity = (BonusEntity) getItem(position);
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_my_reward_list_item, null);
                holder.title_type_tv = (TextView) convertView
                        .findViewById(R.id.bonus_item_title_type);
                holder.title_time_tv = (TextView) convertView
                        .findViewById(R.id.bonus_item_title_time);
                holder.amount_tv = (TextView) convertView
                        .findViewById(R.id.bonus_item_amount_tv);
                holder.type_tv = (TextView) convertView
                        .findViewById(R.id.bonus_item_type_tv);
                holder.bonus_time_tv = (TextView) convertView
                        .findViewById(R.id.bonus_item_time_tv);
                holder.prj_name_tv = (TextView) convertView
                        .findViewById(R.id.bonus_item_prj_name_tv);
                holder.rule_look_tv = (TextView) convertView
                        .findViewById(R.id.bonus_item_look_tv);
                holder.serial_no_tv = (TextView) convertView
                        .findViewById(R.id.bonus_item_serial_no_tv);
                holder.bonus_item_time_ll = (LinearLayout) convertView
                        .findViewById(R.id.bonus_item_time_ll);
                holder.bonus_item_prj_ll = (LinearLayout) convertView
                        .findViewById(R.id.bonus_item_prj_ll);
                holder.bonus_item_time_view_tv = (TextView) convertView
                        .findViewById(R.id.bonus_item_time_view_tv);
                holder.bonus_item_change_view_tv = (TextView) convertView
                        .findViewById(R.id.bonus_item_change_view_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title_time_tv.getPaint().setFakeBoldText(true);
            holder.amount_tv.getPaint().setFakeBoldText(true);
            holder.type_tv.getPaint().setFakeBoldText(true);
            holder.bonus_time_tv.getPaint().setFakeBoldText(true);
            holder.prj_name_tv.getPaint().setFakeBoldText(true);
            holder.amount_tv.setText(entity.amount);
            holder.rule_look_tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadRuleData(entity.rule_id, entity.obj_id, entity.obj_type);
                }
            });
            holder.title_type_tv.setText(entity.bonus_type_name);
            holder.type_tv.setText(entity.memo);
            if (entity.status.equals("1")) {// 有效
                // 存入
                if (entity.type.equals("1")) {
//					holder.title_type_tv.setText("红包");
                    holder.amount_tv.setTextColor(Color.parseColor("#FF8500"));
                    holder.type_tv.setTextColor(Color.parseColor("#FF8500"));
//					holder.type_tv.setText("存入");
                    holder.bonus_item_time_ll.setVisibility(View.VISIBLE);
                    holder.bonus_item_prj_ll.setVisibility(View.VISIBLE);
                    holder.bonus_item_time_view_tv.setText("有效期限：");
                    holder.bonus_time_tv.setText(entity.fetch_time);// 有效期限
                    holder.title_time_tv.setText(entity.ctime);// 红包获取时间
                    holder.prj_name_tv.setVisibility(View.GONE);
                    holder.rule_look_tv.setVisibility(View.VISIBLE);
                    holder.bonus_item_change_view_tv.setText("红包规则：");
                    holder.rule_look_tv.setTextColor(Color
                            .parseColor("#2079FF"));
                }
                // 支出
                else if (entity.type.equals("2")) {
//					holder.title_type_tv.setText("投资");
                    holder.amount_tv.setTextColor(Color.parseColor("#71CE2C"));
                    holder.type_tv.setTextColor(Color.parseColor("#71CE2C"));
                    holder.type_tv.setText("支出");
                    holder.bonus_item_time_ll.setVisibility(View.GONE);
                    holder.bonus_item_prj_ll.setVisibility(View.VISIBLE);
                    holder.prj_name_tv.setText(entity.prj_name);
                    holder.title_time_tv.setText(entity.fetch_time);// 红包使用时间
                    holder.prj_name_tv.setVisibility(View.VISIBLE);
                    holder.rule_look_tv.setVisibility(View.GONE);
                    holder.bonus_item_change_view_tv.setText("项目名称：");
                }
            } else {// 失效
//				holder.title_type_tv.setText("红包");
                holder.amount_tv.setTextColor(Color.parseColor("#333333"));
                holder.type_tv.setTextColor(Color.parseColor("#333333"));
//				holder.type_tv.setText("失效");
                holder.bonus_item_time_ll.setVisibility(View.VISIBLE);
                holder.bonus_item_prj_ll.setVisibility(View.GONE);
                holder.bonus_item_time_view_tv.setText("失效日期：");
                holder.bonus_time_tv.setText(entity.fetch_time);// 失效日期
                holder.title_time_tv.setText(entity.ctime);// 红包获取时间

            }
            holder.serial_no_tv.setText(entity.serial_no);
            return convertView;
        }

    }

    private void loadRuleData(String rule_id, String obj_type, String obj_id) {
        hmrrs.gainMyRewardRule(rule_id, obj_type, obj_id);
    }

    OnClickListener absoluteClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (rewardRuleDialog.isShowing()) {
                rewardRuleDialog.dismiss();
            }
        }
    };

    private void loadData() {
        hmrs.gainMyReward(currentPage, pagesize);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // 判断是不是最后一个
        if ((firstVisibleItem + visibleItemCount) == totalItemCount
                && totalPages >= currentPage && !isLoading) {
            isLoading = true;
            loadData();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void gainMyRewardsuccess(MyRewardRecordJson response) {
    }

    @Override
    public void gainMyRewardfail() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        showToast("网络不给力");
    }


    @Override
    public void gainMyRewardRulesuccess(MyRewardRuleJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    String dataObj = response.getData();
                    if (rewardRuleDialog != null) {
                        if (rewardRuleDialog.isShowing()) {
                            rewardRuleDialog.dismiss();
                        }
                    }
                    rewardRuleDialog = createDialog("使用规则", dataObj, "关闭",
                            absoluteClickListener);
                    if (!rewardRuleDialog.isShowing()) {
                        rewardRuleDialog.show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainMyRewardRulefail() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        showToast("网络不给力");
    }


}
