package com.lcshidai.lc.ui.account.bespeak;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.BespeakCancelImpl;
import com.lcshidai.lc.impl.account.BespeakIndexDataImpl;
import com.lcshidai.lc.impl.account.BespeakRecordListImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.Page;
import com.lcshidai.lc.model.account.BespeakIndexAppointRecord;
import com.lcshidai.lc.model.account.BespeakIndexData;
import com.lcshidai.lc.model.account.BespeakIndexDataJson;
import com.lcshidai.lc.model.account.BespeakRecordListData;
import com.lcshidai.lc.model.account.BespeakRecordListItem;
import com.lcshidai.lc.model.account.BespeakRecordListJson;
import com.lcshidai.lc.service.account.HttpBespeakCancelService;
import com.lcshidai.lc.service.account.HttpBespeakIndexDataService;
import com.lcshidai.lc.service.account.HttpBespeakRecordListService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.widget.PinnedSectionListView;
import com.lcshidai.lc.widget.PinnedSectionListView.PinnedSectionListAdapter;
import com.lcshidai.lc.widget.ppwindow.DialogPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约专区（我的预约）
 *
 * @author 000853
 */
public class BespeakAreaActivity extends TRJActivity implements BespeakIndexDataImpl, BespeakRecordListImpl, BespeakCancelImpl, OnClickListener, DialogPopupWindow.ChooseListener {

    private enum BespeakAreaEnum {

        AREA(new String[]{"预约自动投标", "可预约收益率区间：", "预约期限范围：", "当前可预约总金额：",
                "预约成功：", "预约产品：", "成交金额：", "预约人数：", "申请预约"}, R.drawable.feedback_submit_bg_xml, 1),
        MINE(new String[]{"我的预约", "我的预期年化收益率：", "我的预约期限：", "我的预约金额：",
                "预约成功：", "预约产品：", "成交金额：", "预约人数：", "取消预约"}, R.drawable.bg_button_corner_blue, -1);

        private String[] strArray;
        private int btBg;
        private int flag;

        BespeakAreaEnum(String[] strArray, int btBg, int flag) {
            this.strArray = strArray;
            this.btBg = btBg;
            this.flag = flag;
        }

        public String[] getStrArray() {
            return strArray;
        }

        public int getBtBg() {
            return btBg;
        }

        public int getFlag() {
            return flag;
        }

    }

    HttpBespeakIndexDataService hbids;
    HttpBespeakRecordListService hbrls;
    HttpBespeakCancelService hbcs;
//	private static final String BESPEAK_RECORD_LIST = "Mobile2/Appoint/appoint_list";

    private Context mContext;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    ImageButton mBackBtn;
    //跳转状态 	AREA:预约专区	MINE:我的预约
    private String intentFlag = "AREA";
    private BespeakAreaEnum bespeakAreaEnum;

    private PinnedSectionListView record_lv;
    private RecordAdapter recordAdapter;
    private List<Item> itemList;
    private View bottomView;
    private ObjectAnimator showAnim, dismissAnim;
    private Button apply_bt;
    private LinearLayout footerView;

    private TextView profit_tv, profit_value_tv, time_tv, time_vlaue_tv, money_tv, money_value_tv,
            info_success_tv, info_success_value_tv, info_profit_tv, info_profit_value_tv,
            info_money_tv, info_money_vlaue_tv, info_product_tv, info_product_value_tv;
    private Dialog loading;
    private Dialog cancelDialog;

    private int nowPage = 1, totalPage = 1, avePage = 10;
    private boolean isLoading = false;

    private String current_bespeak_id = "";
    private int position;
    private String is_limit_apply;
    private DialogPopupWindow dialogPopupWindow;
    private View mainView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Intent intent = getIntent();
        if (null != intent) {
            String flag = intent.getStringExtra("intent_flag");
            if (null != flag && !"".equals(flag)) intentFlag = flag;
        }
        bespeakAreaEnum = BespeakAreaEnum.valueOf(BespeakAreaEnum.class, intentFlag);
        hbids = new HttpBespeakIndexDataService(this, this);
        hbrls = new HttpBespeakRecordListService(this, this);
        hbcs = new HttpBespeakCancelService(this, this);
        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    protected void onResume() {
        if (!loading.isShowing()) loading.show();
        loadData();
        nowPage = 1;
        totalPage = 1;
        itemList.clear();
        recordAdapter.notifyDataSetChanged();
        Item head_item = new Item(Item.SECTION, null);
        itemList.add(head_item);
        loadBespeakRecord();
        super.onResume();
    }

    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_bespeak_area);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);

        bottomView = findViewById(R.id.bespaek_area_bottom);
        apply_bt = (Button) findViewById(R.id.bespeak_area_bt_apply);
        record_lv = (PinnedSectionListView) findViewById(R.id.bespeak_area_lv_record);
        apply_bt.setOnClickListener(this);
        apply_bt.setVisibility(View.INVISIBLE);
        itemList = new ArrayList<Item>();

        loading = createLoadingDialog(mContext, "加载中", true);
        cancelDialog = createDialog("您真的要取消自动投标吗？后期若再参与，可能需要更长时间的等待。", "确认取消", "返回", absoluteClickListener, negativeClickListener);
        mainView = findViewById(R.id.ll_main);
        dialogPopupWindow = new DialogPopupWindow(this, mainView, this);
        View header_view = LayoutInflater.from(mContext).inflate(R.layout.layout_bespeak_area_header, null);
        profit_tv = (TextView) header_view.findViewById(R.id.bespeak_area_tv_profit);
        profit_value_tv = (TextView) header_view.findViewById(R.id.bespeak_area_tv_profit_value);
        time_tv = (TextView) header_view.findViewById(R.id.bespeak_area_tv_time);
        time_vlaue_tv = (TextView) header_view.findViewById(R.id.bespeak_area_tv_time_value);
        money_tv = (TextView) header_view.findViewById(R.id.bespeak_area_tv_money);
        money_value_tv = (TextView) header_view.findViewById(R.id.bespeak_area_tv_money_value);
        info_success_tv = (TextView) header_view.findViewById(R.id.bespeak_area_info_tv_success);
        info_success_value_tv = (TextView) header_view.findViewById(R.id.bespeak_area_info_tv_success_value);
        info_profit_tv = (TextView) header_view.findViewById(R.id.bespeak_area_info_tv_profit);
        info_profit_value_tv = (TextView) header_view.findViewById(R.id.bespeak_area_info_tv_profit_value);
        info_money_tv = (TextView) header_view.findViewById(R.id.bespeak_area_info_tv_money);
        info_money_vlaue_tv = (TextView) header_view.findViewById(R.id.bespeak_area_info_tv_money_value);
        info_product_tv = (TextView) header_view.findViewById(R.id.bespeak_area_info_tv_product);
        info_product_value_tv = (TextView) header_view.findViewById(R.id.bespeak_area_info_tv_product_value);

        setViewText();

        record_lv.setShadowVisible(false);
        record_lv.addHeaderView(header_view);

        recordAdapter = new RecordAdapter(itemList, mContext);
        record_lv.setAdapter(recordAdapter);

        record_lv.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case OnScrollListener.SCROLL_STATE_IDLE:    //空闲状态
                        if (android.os.Build.VERSION.SDK_INT >= 12) {
                            showAnim.start();
                        } else {
                            bottomView.setVisibility(View.VISIBLE);
                        }
                        break;
                    case OnScrollListener.SCROLL_STATE_FLING:    //滚动状态
                        break;
                    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:    //触摸后滚动
                        if (android.os.Build.VERSION.SDK_INT >= 12) {
                            dismissAnim.start();
                        } else {
                            bottomView.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) == totalItemCount &&
                        totalPage >= nowPage && !isLoading) {
                    isLoading = true;
                    loadBespeakRecord();
                }
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= 12) {
            showAnim = ObjectAnimator.ofFloat(bottomView, "YFraction", 1, 0);
            showAnim.setDuration(100);
            dismissAnim = ObjectAnimator.ofFloat(bottomView, "YFraction", 0, 1);
            dismissAnim.setDuration(100);
        }
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        bottomView.measure(0, h);
        int height = bottomView.getMeasuredHeight();
        footerView = new LinearLayout(mContext);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        footerView.setBackgroundColor(Color.parseColor("#FAFAFA"));
        footerView.setLayoutParams(lp);
        footerView.setGravity(Gravity.CENTER);
        footerView.setOrientation(LinearLayout.HORIZONTAL);
        ProgressBar footer_pb = new ProgressBar(mContext, null, android.R.attr.progressBarStyleSmall);
        TextView footer_tv = new TextView(mContext);
        footerView.addView(footer_pb);
        footerView.addView(footer_tv);
        footer_tv.setTextSize(14);
        footer_tv.setTextColor(Color.parseColor("#666666"));
        footer_tv.setPadding(DensityUtil.dip2px(mContext, 10), 0, 0, 0);
        footer_tv.setText("加载中...");
        footerView.setVisibility(View.INVISIBLE);
        record_lv.addFooterView(footerView);

        record_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == 0 || arg2 == 1 || arg2 == (itemList.size() + 1)) {
                    return;
                }
                position = arg2;
                Intent intent = new Intent(mContext, BespeakInfoActivity.class);
                if (null == itemList.get(arg2 - 1).bespeak) return;
                intent.putExtra("bespeak_id", itemList.get(arg2 - 1).bespeak.id);
                if (is_limit_apply != null && is_limit_apply.equals("1")) {
                    intent.putExtra("is_limit_apply", is_limit_apply);
                }
                startActivityForResult(intent, 10);
            }

        });
    }

    OnClickListener absoluteClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (cancelDialog.isShowing()) {
                cancelDialog.dismiss();
            }
            if (!loading.isShowing()) loading.show();
            cancelBespeak();
        }
    };

    OnClickListener negativeClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (cancelDialog.isShowing()) {
                cancelDialog.dismiss();
            }
        }
    };

    private void setViewText() {
        mTvTitle.setText(bespeakAreaEnum.getStrArray()[0]);
        profit_tv.setText(bespeakAreaEnum.getStrArray()[1]);
        time_tv.setText(bespeakAreaEnum.getStrArray()[2]);
        money_tv.setText(bespeakAreaEnum.getStrArray()[3]);
        info_success_tv.setText(bespeakAreaEnum.getStrArray()[4]);
        info_profit_tv.setText(bespeakAreaEnum.getStrArray()[5]);
        info_money_tv.setText(bespeakAreaEnum.getStrArray()[6]);
        info_product_tv.setText(bespeakAreaEnum.getStrArray()[7]);
        apply_bt.setText(bespeakAreaEnum.getStrArray()[8]);
        apply_bt.setBackgroundDrawable(getResources().getDrawable(bespeakAreaEnum.getBtBg()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.bespeak_area_bt_apply:
                //申请 flag == 1
                if (bespeakAreaEnum.getFlag() == 1) {
                    Intent intent = new Intent(mContext, BespeakApplyActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    dialogPopupWindow.showWithMessage("您真的要取消自动投标吗？后期若再参与，可能需要更长时间的等待。", "6");
                }
                break;
            default:
                break;
        }
    }

    private class ViewHolder {
        TextView time_tv;
        TextView money_tv;
        TextView status_tv;
    }

    private class RecordAdapter extends BaseAdapter implements PinnedSectionListAdapter {
        private List<Item> itemArray;
        private Context context;

        private RecordAdapter(List<Item> list, Context context) {
            this.itemArray = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return itemArray.size();
        }

        @Override
        public Object getItem(int arg0) {
            return itemArray.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Item item = (Item) getItem(position);
            BespeakItem br = item.bespeak;

            if (item.type == Item.SECTION) {
                View titleView = LayoutInflater.from(mContext).inflate(R.layout.layout_bespeak_list_item_title, null);
                return titleView;
            } else {
                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.layout_bespeak_record_list_item, null);
                    holder = new ViewHolder();
                    holder.time_tv = (TextView) convertView.findViewById(R.id.bespeak_record_list_item_tv_time);
                    holder.money_tv = (TextView) convertView.findViewById(R.id.bespeak_record_list_item_tv_money);
                    holder.status_tv = (TextView) convertView.findViewById(R.id.bespeak_record_list_item_tv_status);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.time_tv.setText(br.ctime);
                holder.status_tv.setText(resolveString(br.status_show, "", "", "").replace("null", ""));
                holder.money_tv.setText(br.amount + "元");
                if ("0".equals(br.status) || "3".equals(br.status)) {
                    holder.status_tv.setTextColor(Color.parseColor("#409FEC"));
                } else if ("1".equals(br.status)) {
                    holder.status_tv.setTextColor(Color.parseColor("#FC5443"));
                } else if ("2".equals(br.status)) {
                    holder.status_tv.setTextColor(Color.parseColor("#63BE3B"));
                } else {
                    holder.status_tv.setTextColor(Color.parseColor("#333333"));
                }
                return convertView;
            }

        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType == Item.SECTION;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return ((Item) getItem(position)).type;
        }
    }

    class Item {
        public static final int ITEM = 0;
        public static final int SECTION = 1;
        public final int type;
        public final BespeakItem bespeak;
        public int sectionPosition;
        public int listPosition;

        public Item(int type, BespeakItem bespeak) {
            this.type = type;
            this.bespeak = bespeak;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 4) {
            Intent intent = new Intent(mContext, BespeakAreaActivity.class);
            intent.putExtra("intent_flag", "MINE");
            startActivity(intent);
            finish();
        }
        if (requestCode == 10 && resultCode == 12) {
            Intent intent = new Intent(mContext, BespeakAreaActivity.class);
            intent.putExtra("intent_flag", "MINE");
            startActivity(intent);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadData() {
        hbids.gainBespeakIndexData();
    }

    private class BespeakItem {
        private String id;
        private String ctime;    //预约时间
        private String amount;    //预约成功时候的预约金额
        private String min_money;    //预约未成功时最小预约金额
        private String max_money;    //预约未成功时最大预约金额
        private String status;    //1 预约中		2 预约成功		0 取消预约		3 预约失败
        private String status_show;
    }

    private void loadBespeakRecord() {
        hbrls.gainBespeakRecordListData(nowPage, avePage);
    }

    /**
     * 取消预约
     */
    private void cancelBespeak() {
        hbcs.getBespeakCancel(current_bespeak_id);
    }

    @Override
    public void gainBespeakIndexDatasuccess(BespeakIndexDataJson response) {
        if (loading.isShowing()) loading.dismiss();
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    BespeakIndexData dataObj = response.getData();
                    int is_has_appoint = dataObj.getIs_has_appoint();
                    //0是预约专区 1是我的预约
                    if (is_has_appoint == 1)
                        bespeakAreaEnum = BespeakAreaEnum.valueOf(BespeakAreaEnum.class, "MINE");
                    else
                        bespeakAreaEnum = BespeakAreaEnum.valueOf(BespeakAreaEnum.class, "AREA");
                    setViewText();
                    apply_bt.setVisibility(View.VISIBLE);

                    //我的
                    if (is_has_appoint == 1) {
                        //预约人数
                        info_product_tv.setVisibility(View.INVISIBLE);
                        info_product_value_tv.setVisibility(View.INVISIBLE);

                        //预约成功数
//						info_success_value_tv.setText(resolveJsonToString(dataObj, "appoint_success_num", "-", "笔"));
                        info_success_value_tv.setText(resolveString(dataObj.getAppoint_success_num(), "appoint_success_num", "-", "笔"));
                        //成交金额
//						info_money_vlaue_tv.setText(resolveJsonToString(dataObj, "appoint_success_amount", "-", ""));
                        info_money_vlaue_tv.setText(resolveString(dataObj.getAppoint_success_amount(), "appoint_success_amount", "-", ""));
                        BespeakIndexAppointRecord appointObj = dataObj.getAppoint_record();
                        //预约产品
//						info_profit_value_tv.setText(resolveJsonToString(dataObj, "appoint_success_prj_type", "-", ""));
                        info_profit_value_tv.setText(resolveString(appointObj.getPrj_type_show(), "prj_type_show", "-", ""));

                        String is_all_money = appointObj.getIs_all_money();

                        //id
                        current_bespeak_id = appointObj.getId();

                        //我的预约金额
                        if ("1".equals(is_all_money)) {
                            money_value_tv.setText("当前账户实际余额");
                        } else {
                            String appoint_money = resolveString(appointObj.getAppoint_money(), "appoint_money", "", "");
                            money_value_tv.setText(appoint_money);
                        }

                        //我的预约利率
                        String appoint_rate = resolveString(appointObj.getAppoint_rate(), "appoint_rate", "", "");
                        profit_value_tv.setText(appoint_rate);

                        //我的预约期限
                        String appoint_day = resolveString(appointObj.getAppoint_day(), "appoint_day", "", "天");
                        time_vlaue_tv.setText(appoint_day);
                    }
                    //专区
                    else {
                        //预约平均年化收益
                        String min_avg_rate = resolveString(dataObj.getMin_avg_rate(), "min_avg_rate", "", "");
                        String max_avg_rate = resolveString(dataObj.getMax_avg_rate(), "max_avg_rate", "", "");
                        if (min_avg_rate.equals(max_avg_rate)) {
                            profit_value_tv.setText(min_avg_rate);
                        } else {
                            profit_value_tv.setText(min_avg_rate + " - " + max_avg_rate);
                        }

                        //预约期限最小值
                        String time_limit_min = resolveString(dataObj.getTime_limit_min(), "time_limit_min", "", "");
                        //预约期限最大值
                        String time_limit_max = resolveString(dataObj.getTime_limit_max(), "time_limit_max", "", "");
                        if (TextUtils.isEmpty(time_limit_min) && TextUtils.isEmpty(time_limit_max)) {
                            time_vlaue_tv.setText("-");
                        } else if (TextUtils.isEmpty(time_limit_min)) {
                            time_vlaue_tv.setText(time_limit_max + "天");
                        } else if (TextUtils.isEmpty(time_limit_max)) {
                            time_vlaue_tv.setText(time_limit_min + "天");
                        } else {
                            time_vlaue_tv.setText(time_limit_min + " - " + time_limit_max + "天");
                        }
                        //当前总可预约金额
                        money_value_tv.setText(resolveString(dataObj.getRemaining_amount(), "remaining_amount", "-", ""));
                        //预约成功总数量
                        info_success_value_tv.setText(resolveString(dataObj.getSucc_count(), "succ_count", "-", "笔"));
                        //预约产品
                        info_profit_value_tv.setText(resolveString(dataObj.getPrj_type(), "prj_type", "-", ""));
                        //预约成交总金额
                        info_money_vlaue_tv.setText(resolveString(dataObj.getSucc_amount(), "succ_amount", "-", ""));
                        //预约人数
                        info_product_value_tv.setText(resolveString(dataObj.getUid_count(), "uid_count", "-", "人"));
                        is_limit_apply = resolveString(dataObj.getIs_limit_apply(), "", "", "");
                        if (is_limit_apply.equals("1")) {
                            apply_bt.setBackgroundResource(R.drawable.bg_button_cancel);
                            apply_bt.setClickable(false);
                            apply_bt.setText("人数已达上限，请等待");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainBespeakIndexDatafail() {
        if (loading.isShowing()) loading.dismiss();
        showToast("网络不给力");
    }

    @Override
    public void gainBespeakRecordListsuccess(BespeakRecordListJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    BespeakRecordListData dataObj = response.getData();
                    if (null == dataObj) return;
                    Page pageObj = dataObj.getPage();
                    if (nowPage == pageObj.getPage()) {
                        totalPage = pageObj.getTotalPages();
                        List<BespeakRecordListItem> listArray = dataObj.getList();
                        for (int i = 0; i < listArray.size(); i++) {
                            BespeakRecordListItem listObj = listArray.get(i);
                            BespeakItem b_item = new BespeakItem();
                            b_item.id = listObj.getId();
                            b_item.ctime = listObj.getCtime();
                            b_item.amount = listObj.getAppoint_money();
                            b_item.min_money = listObj.getMin_money();
                            b_item.max_money = listObj.getMax_money();
                            b_item.status = listObj.getStatus();
                            b_item.status_show = listObj.getStatus_show();
                            Item item = new Item(Item.ITEM, b_item);
                            itemList.add(item);
                        }

                        recordAdapter.notifyDataSetChanged();
                        if (nowPage < totalPage)
                            footerView.setVisibility(View.VISIBLE);
                        else
                            footerView.setVisibility(View.INVISIBLE);
                        nowPage++;
                        isLoading = false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainBespeakRecordListfail() {
        showToast("预约记录加载失败");
    }

    @Override
    public void gainBespeakCancelsuccess(BaseJson response) {
        if (loading.isShowing()) loading.dismiss();
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    showToast("预约已取消");
                    Intent intent = new Intent(mContext, BespeakAreaActivity.class);
                    intent.putExtra("intent_flag", "AREA");
                    startActivity(intent);
                    finish();
                } else {
                    showToast(response.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainBespeakCancelfail() {
        if (loading.isShowing()) loading.dismiss();
        showToast("网络不给力");
    }

    @Override
    public void chooseItem(int markItem) {
        switch (markItem) {
            case 0:
                cancelBespeak();
                break;
            case 1:

                break;
            default:
                break;
        }
    }
}
