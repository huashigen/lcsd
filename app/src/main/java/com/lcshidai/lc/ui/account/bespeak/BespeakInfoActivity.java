package com.lcshidai.lc.ui.account.bespeak;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.PIPWCallBack;
import com.lcshidai.lc.impl.account.BespeakApplyImpl;
import com.lcshidai.lc.impl.account.BespeakCancelImpl;
import com.lcshidai.lc.impl.account.BespeakInfoDataImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.BespeakInfoData;
import com.lcshidai.lc.model.account.BespeakInfoDataJson;
import com.lcshidai.lc.model.account.BespeakInfoList;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckData;
import com.lcshidai.lc.service.account.HttpBespeakApplyService;
import com.lcshidai.lc.service.account.HttpBespeakCancelService;
import com.lcshidai.lc.service.account.HttpBespeakInfoDataService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.widget.PinnedSectionListView;
import com.lcshidai.lc.widget.PinnedSectionListView.PinnedSectionListAdapter;
import com.lcshidai.lc.widget.ppwindow.DialogPopupWindow;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow.PayPPForBespeakInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约记录详情页
 *
 * @author 000853
 */
public class BespeakInfoActivity extends TRJActivity implements
        OnClickListener, PIPWCallBack, PayPPForBespeakInterface, DialogPopupWindow.ChooseListener, BespeakInfoDataImpl, BespeakApplyImpl, BespeakCancelImpl {
    HttpBespeakInfoDataService hbids;
    HttpBespeakApplyService hbas;
    HttpBespeakCancelService hbcs;
    //	private static final String BESEPAK_INFO_URL = "Mobile2/Appoint/view";
    private Context mContext;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    ImageButton mBackBtn;
    private LinearLayout middle_ll;
    private TextView[] leftTV, rightTV;
    // private String[] leftValue = new String[]{"预约时间：", "预约金额：", "预期年化收益：",
    // "投资期限：",
    // "预约状态：", "匹配金额：", "匹配时间：", "匹配产品："};
    private String[] leftValue = new String[]{"预约时间：", "预约金额：", "预期年化收益率：",
            "预约有效期：", "预约状态：", "实际年化收益率：", "匹配金额：", "匹配时间："};
    // private String[] rightValue = new String[]{"", "", "", "", "", "", "",
    // ""};
    private String[] rightValue = new String[]{"", "", "", "", "", "", "", ""};
    private Button cancel_bt, cancel_bt_new, alter_bt;
    private String bespeak_id = "";
    private String is_limit_apply = "";
    private Dialog loading;
    private String prj_id = "";
    private PayPasswordPopupWindow payPP;

    private LinearLayout ll_btn;
    private String type, date, rate, money;
    private PinnedSectionListView record_lv;
    private RecordAdapter recordAdapter;
    private List<Item> itemList;
    private LinearLayout footerView;
    private int nowPage = 1, totalPage = 1, avePage = 10;
    private boolean isLoading = false;
    private Dialog cancelDialog;
    private String is_qyr = "";
    private String is_sdt = "";
    private String is_xyb = "";
    private String is_rys = "";
    private DialogPopupWindow dialogPopupWindow;
    private View mainView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (null != getIntent()) {
            bespeak_id = getIntent().getStringExtra("bespeak_id");
            is_limit_apply = getIntent().getStringExtra("is_limit_apply");
        }
        hbids = new HttpBespeakInfoDataService(this, this);
        hbas = new HttpBespeakApplyService(this, this);
        hbcs = new HttpBespeakCancelService(this, this);
        initView();
        getBespeakInfo();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_bespeak_info);
        mainView = findViewById(R.id.ll_main);
        dialogPopupWindow = new DialogPopupWindow(this, mainView, this);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("预约记录");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);

        ll_btn = (LinearLayout) findViewById(R.id.bespeak_info_bottom_ll);
        View header_view = LayoutInflater.from(mContext).inflate(
                R.layout.layout_bespeak_info_header, null);
        middle_ll = (LinearLayout) header_view
                .findViewById(R.id.bespeak_info_middle_ll);
        cancel_bt = (Button) findViewById(R.id.bespeak_info_bt_cancle);
        cancel_bt_new = (Button) findViewById(R.id.bespeak_info_bt_cancle_x);
        alter_bt = (Button) findViewById(R.id.bespeak_info_bt_alter);
        cancel_bt_new.setOnClickListener(this);
        alter_bt.setOnClickListener(this);
        createMiddleView();
        record_lv = (PinnedSectionListView) findViewById(R.id.bespeak_product_lv_record);
        loading = createLoadingDialog(mContext, "加载中", true);
        loading.show();
        cancelDialog = createDialog("您真的要取消自动投标吗？后期若再参与，可能需要更长时间的等待。", "确认取消", "返回", absoluteClickListener, negativeClickListener);
        payPP = new PayPasswordPopupWindow(this, this);

        record_lv.setShadowVisible(false);
        record_lv.addHeaderView(header_view);
        itemList = new ArrayList<Item>();
        recordAdapter = new RecordAdapter(itemList, mContext);
        record_lv.setAdapter(recordAdapter);

        record_lv.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) == totalItemCount
                        && totalPage >= nowPage && !isLoading) {
                    isLoading = true;
                    // loadBespeakRecord();
                }
            }
        });

        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ll_btn.measure(0, h);
        int height = ll_btn.getMeasuredHeight();
        footerView = new LinearLayout(mContext);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, height);
        footerView.setBackgroundColor(Color.parseColor("#FAFAFA"));
        footerView.setLayoutParams(lp);
        footerView.setGravity(Gravity.CENTER);
        footerView.setOrientation(LinearLayout.HORIZONTAL);
        ProgressBar footer_pb = new ProgressBar(mContext, null,
                android.R.attr.progressBarStyleSmall);
        TextView footer_tv = new TextView(mContext);
        footer_tv.setTextSize(14);
        footer_tv.setTextColor(Color.parseColor("#666666"));
        footer_tv.setPadding(DensityUtil.dip2px(mContext, 10), 0, 0, 0);
        footer_tv.setText("加载中...");
        footerView.addView(footer_pb);
        footerView.addView(footer_tv);
        footerView.setVisibility(View.INVISIBLE);
    }

    private void createMiddleView() {
        leftTV = new TextView[8];
        rightTV = new TextView[8];
        for (int i = 0; i < leftTV.length; i++) {
            LinearLayout ll = new LinearLayout(mContext);
            ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setPadding(DensityUtil.dip2px(mContext, 10), 0,
                    DensityUtil.dip2px(mContext, 10), 0);

            leftTV[i] = new TextView(mContext);
            leftTV[i].setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            leftTV[i].setPadding(0, DensityUtil.dip2px(mContext, 15),
                    DensityUtil.dip2px(mContext, 10),
                    DensityUtil.dip2px(mContext, 15));
            leftTV[i].setTextColor(Color.parseColor("#333333"));
            leftTV[i].setTextSize(14);
            leftTV[i].setText(leftValue[i]);

            rightTV[i] = new TextView(mContext);
            rightTV[i].setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            rightTV[i].setPadding(0, DensityUtil.dip2px(mContext, 15),
                    DensityUtil.dip2px(mContext, 10),
                    DensityUtil.dip2px(mContext, 15));
            rightTV[i].setTextColor(Color.parseColor("#333333"));
            rightTV[i].setTextSize(14);

            ll.addView(leftTV[i]);
            ll.addView(rightTV[i]);
            middle_ll.addView(ll);

            TextView div = new TextView(mContext);
            div.setWidth(LayoutParams.MATCH_PARENT);
            div.setHeight(DensityUtil.dip2px(mContext, 1));
            div.setBackgroundColor(Color.parseColor("#C2C2C2"));
            middle_ll.addView(div);
        }

        rightTV[2].setTextColor(Color.parseColor("#F68446"));
    }

    private void setRightValues() {
        for (int i = 0; i < rightTV.length; i++) {
            rightTV[i].setText(rightValue[i]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_back:
                BespeakInfoActivity.this.finish();
                break;
            case R.id.bespeak_info_bt_cancle_x:
                dialogPopupWindow.showWithMessage("您真的要取消自动投标吗？后期若再参与，可能需要更长时间的等待。", "6");
                break;
            case R.id.bespeak_info_bt_alter:
                Intent intent = new Intent(this, BespeakApplyActivity.class);
                intent.putExtra("bespeak_id", bespeak_id);
                intent.putExtra("appoint_money", rightValue[1]);
                intent.putExtra("appoint_rate", rightValue[2]);
                intent.putExtra("appoint_time_limit", rightValue[3]);
                startActivityForResult(intent, 10);
                break;
        }
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

    /**
     * 取消预约
     */
    private void cancelBespeak() {
        hbcs.getBespeakCancel(bespeak_id);
    }

    private void getBespeakInfo() {
        hbids.gainBespeakInfoData(bespeak_id);
    }

    protected void createMiddleViewPrj() {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(DensityUtil.dip2px(mContext, 10), 0, DensityUtil.dip2px(mContext, 10), 0);

        TextView leftTV = new TextView(mContext);
        leftTV.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        leftTV.setPadding(0, DensityUtil.dip2px(mContext, 15), DensityUtil.dip2px(mContext, 10),
                DensityUtil.dip2px(mContext, 15));
        leftTV.setTextColor(Color.parseColor("#333333"));
        leftTV.setTextSize(14);
        leftTV.setText("匹配产品：");

        TextView rightTV = new TextView(mContext);
        rightTV.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        rightTV.setPadding(0, DensityUtil.dip2px(mContext, 15),
                DensityUtil.dip2px(mContext, 10),
                DensityUtil.dip2px(mContext, 15));
        rightTV.setTextColor(Color.parseColor("#333333"));
        rightTV.setTextSize(14);
        rightTV.setText("");

        ll.addView(leftTV);
        ll.addView(rightTV);
        middle_ll.addView(ll);

        TextView div = new TextView(mContext);
        div.setWidth(LayoutParams.MATCH_PARENT);
        div.setHeight(DensityUtil.dip2px(mContext, 1));
        div.setBackgroundColor(Color.parseColor("#C2C2C2"));
        middle_ll.addView(div);
    }

    private class BtnClickListener implements OnClickListener {
        int flag;

        private BtnClickListener(int f) {
            this.flag = f;
        }

        @Override
        public void onClick(View v) {
            // 查看项目详情
            if (flag == 0) {
                Intent intent = new Intent();
                Bundle args = new Bundle();
                args.putString("prj_id", prj_id);
                args.putString("mTransferId", "");
                intent.putExtras(args);
                intent.setClass(mContext, FinanceProjectDetailActivity.class);
                startActivity(intent);
            }
            // 重新预约
            else {
                payPP.setBespeakData(type, date, rate, money, 0, new String[]{is_xyb, is_qyr, is_sdt, is_rys},
                        BespeakInfoActivity.this);
                payPP.goAnim("", v, 5, "", false);
            }

        }

    }

    @Override
    public void doBespeak(String pwd, int bp_applyflag) {
        applyBespeak(pwd, bp_applyflag);
    }

    @Override
    public void callPayCheckBack(boolean success) {

    }

    @Override
    public void doInvestSuccess(boolean success) {

    }

    private void applyBespeak(String pwd, int bp_applyflag) {
        hbas.gainBespeakApply(pwd, bespeak_id, "", "", "", "", "", "", false);
    }

    private class ViewHolder {
        TextView product_tv;
        TextView product_title_tv;
        Button product_btn;
    }

    private class RecordAdapter extends BaseAdapter implements
            PinnedSectionListAdapter {
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Item item = (Item) getItem(position);
            // BespeakItem br = item.bespeak;

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.layout_bespeak_product_record_list_item, null);
                holder = new ViewHolder();
                holder.product_tv = (TextView) convertView
                        .findViewById(R.id.bespeak_record_list_item_tv_product);
                holder.product_title_tv = (TextView) convertView
                        .findViewById(R.id.bespeak_record_list_item_tv_product_title);
                holder.product_btn = (Button) convertView
                        .findViewById(R.id.bespeak_info_bt_detail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.product_title_tv.setText(itemArray.get(position).bespeak.invest_time);
            holder.product_tv.setText(itemArray.get(position).bespeak.invest_money);

            holder.product_btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle args = new Bundle();
                    prj_id = itemArray.get(position).bespeak.prj_id;
                    args.putString("prj_id", prj_id);
                    args.putString("mTransferId", "");
                    intent.putExtras(args);
                    intent.setClass(mContext, FinanceProjectDetailActivity.class);
                    startActivity(intent);
                }
            });
            return convertView;

        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            // return viewType == Item.SECTION;
            return false;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            // return ((Item)getItem(position)).type;
            return 0;
        }
    }

    class Item {

        public final BespeakItem bespeak;

        public Item(BespeakItem bespeak) {
            this.bespeak = bespeak;
        }

    }

    private class BespeakItem {
        private String prj_order_id;
        private String prj_name;
        private String prj_id;
        private String invest_money;
        private String invest_time;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11 && resultCode == 3) {
            setResult(12);
            BespeakInfoActivity.this.finish();
        } else if (requestCode == 10 && resultCode == 2) {
//			Intent intent = new Intent(mContext,
//					BespeakAreaActivity.class);
//			intent.putExtra("intent_flag", "AREA");
//			startActivity(intent);
            BespeakInfoActivity.this.finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void doInvestCheckSuccess(boolean success, FinanceInvestPBuyCheckData model) {

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


    @Override
    public void gainBespeakCancelsuccess(BaseJson response) {
        if (loading.isShowing()) loading.dismiss();
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    showToast("预约已取消");
                    BespeakInfoActivity.this.finish();
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
    public void gainBespeakApplysuccess(BaseJson response) {
        if (loading.isShowing())
            loading.dismiss();
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    if (payPP.isShowing())
                        payPP.dismiss();
                    Intent intent = new Intent(mContext, BespeakApplySuccessActivity.class);
                    startActivityForResult(intent, 11);
                } else {
                    showToast(response.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainBespeakApplyfail() {
        if (loading.isShowing())
            loading.dismiss();
        showToast("网络不给力");
    }

    @Override
    public void gainBespeakInfoDatasuccess(BespeakInfoDataJson response) {
        if (loading.isShowing())
            loading.dismiss();
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    BespeakInfoData dataObj = response
                            .getData();
                    rightValue[0] = resolveString(
                            dataObj.getCtime(), "ctime", "-", "");
                    rightValue[1] = resolveString(
                            dataObj.getAppoint_money(), "appoint_money", "-", "元");
                    rightValue[2] = resolveString(
                            dataObj.getAppoint_rate(), "appoint_rate", "-", "");
                    rightValue[3] = resolveString(
                            dataObj.getAppoint_time_limit(), "appoint_time_limit", "-",
                            "天");
                    rightValue[4] = resolveString(
                            dataObj.getStatus_show(), "status_show", "-", "");
                    if (dataObj.getAppoint_income_rate() != null && dataObj.getAppoint_income_rate().equals("-1")) {
                        rightValue[5] = "-";
                    } else {
                        rightValue[5] = resolveString(
                                dataObj.getAppoint_income_rate(), "appoint_income_rate", "-", "");
                    }
                    rightValue[6] = resolveString(
                            dataObj.getMatch_money(), "match_money", "-", "元");
                    rightValue[7] = resolveString(
                            dataObj.getEtime(), "etime", "-", "");
                    is_qyr = resolveString(
                            dataObj.getIs_qyr(), "is_qyr", "-", "");
                    is_sdt = resolveString(
                            dataObj.getIs_sdt(), "is_sdt", "-", "");
                    is_xyb = resolveString(
                            dataObj.getIs_xyb(), "is_xyb", "-", "");
                    is_rys = resolveString(
                            dataObj.getIs_rys(), "is_rys", "-", "");
                    // rightValue[7] =
                    // resolveJsonToString(dataObj, "prj_name",
                    // "-", "");
                    List<BespeakInfoList> listArray = dataObj
                            .getPrj_list();
                    if (listArray != null) {
                        for (int i = 0; i < listArray.size(); i++) {
                            BespeakInfoList listObj = listArray
                                    .get(i);
                            BespeakItem b_item = new BespeakItem();
                            b_item.prj_order_id = listObj
                                    .getPrj_order_id();
                            b_item.prj_name = listObj
                                    .getPrj_name();
                            b_item.prj_id = listObj
                                    .getPrj_id();
                            b_item.invest_money = listObj
                                    .getInvest_money();
                            b_item.invest_time = listObj
                                    .getInvest_time();
                            Item item = new Item(b_item);
                            itemList.add(item);
                        }
                        createMiddleViewPrj();
                        recordAdapter.notifyDataSetChanged();
                    } else {
                        createMiddleViewPrj();
                    }
                    // prj_id = dataObj.optString("prj_id");
                    String status = dataObj.getStatus();
//					if (is_limit_apply!=null&&is_limit_apply.equals("1")) {
//						cancel_bt.setBackgroundResource(R.drawable.bg_button_cancel);
//						cancel_bt.setClickable(false);
//						cancel_bt.setText("人数已达上限，请等待");
//						cancel_bt.setVisibility(View.VISIBLE);
//						ll_btn.setVisibility(View.VISIBLE);
//					}else {
                    if ("4".equals(status)
                            || "3".equals(status)) {
                        if (is_limit_apply != null && is_limit_apply.equals("1")) {
                            cancel_bt.setBackgroundResource(R.drawable.bg_button_cancel);
                            cancel_bt.setClickable(false);
                            cancel_bt.setText("人数已达上限，请等待");
                            cancel_bt.setVisibility(View.VISIBLE);
                            ll_btn.setVisibility(View.VISIBLE);
                        } else {

                            cancel_bt.setText("重新发起预约");
                            cancel_bt_new.setVisibility(View.GONE);
                            alter_bt.setVisibility(View.GONE);
                            cancel_bt.setVisibility(View.VISIBLE);
                            ll_btn.setVisibility(View.VISIBLE);
                            cancel_bt
                                    .setOnClickListener(new BtnClickListener(
                                            1));
                        }
                    } else if ("2".equals(status) || "1".equals(status)) {
                        cancel_bt_new.setVisibility(View.VISIBLE);
                        alter_bt.setVisibility(View.VISIBLE);
                        cancel_bt.setVisibility(View.GONE);
                        ll_btn.setVisibility(View.VISIBLE);
                    }
                    setRightValues();

                    type = dataObj.getPrj_type();
                    date = dataObj
                            .getAppoint_time_limit();
                    if (!date.contains("天")) {
                        date = date + "天";
                    }
                    rate = dataObj.getAppoint_rate();
                    if (rate.contains("以上")) {
                        rate = rate.replace("以上", "");
                    }
                    money = dataObj.getAppoint_money();
                    if (!money.contains("元")) {
                        money = money + "元";
                    }
                } else {
                    showToast(response.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainBespeakInfoDatafail() {
        if (loading.isShowing())
            loading.dismiss();
        showToast("网络不给力");
    }
}
