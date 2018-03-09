package com.lcshidai.lc.ui.account.bespeak;

import java.util.ArrayList;
import java.util.List;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.PIPWCallBack;
import com.lcshidai.lc.impl.account.BespeakAlterImpl;
import com.lcshidai.lc.impl.account.BespeakApplyImpl;
import com.lcshidai.lc.impl.account.BespeakApplyInfoImpl;
import com.lcshidai.lc.impl.account.BespeakCancelImpl;
import com.lcshidai.lc.impl.account.BespeakTypeImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.BespeakApplyInfoData;
import com.lcshidai.lc.model.account.BespeakApplyInfoJson;
import com.lcshidai.lc.model.account.BespeakType;
import com.lcshidai.lc.model.account.BespeakTypeData;
import com.lcshidai.lc.model.account.BespeakTypeJson;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckData;
import com.lcshidai.lc.service.account.HttpBespeakAlterService;
import com.lcshidai.lc.service.account.HttpBespeakApplyInfoService;
import com.lcshidai.lc.service.account.HttpBespeakApplyService;
import com.lcshidai.lc.service.account.HttpBespeakCancelService;
import com.lcshidai.lc.service.account.HttpBespeakTypeService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.widget.MyGridView;
import com.lcshidai.lc.widget.ppwindow.DialogPopupWindow;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow.PayPPForBespeakInterface;
import com.lcshidai.lc.widget.ppwindow.WheelViewPopupWindow;
import com.lcshidai.lc.widget.ppwindow.WheelViewPopupWindow.OnWVPWClickListener;
import com.lcshidai.lc.widget.quickaction.QuickAction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 预约申请
 */
public class BespeakApplyActivity extends TRJActivity implements OnClickListener,
        OnWVPWClickListener, PIPWCallBack, PayPPForBespeakInterface,
        DialogPopupWindow.ChooseListener, BespeakApplyImpl, BespeakCancelImpl, BespeakApplyInfoImpl,
        BespeakAlterImpl, BespeakTypeImpl {
    HttpBespeakApplyService hbas;
    HttpBespeakCancelService hbcs;
    HttpBespeakApplyInfoService hbais;
    HttpBespeakAlterService hbass;
    HttpBespeakTypeService hbts;
    private Context mContext;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    ImageButton mBackBtn;
    private TextView date_left_tv, date_right_tv;
    private WheelViewPopupWindow wheelPop;
    private EditText profit_left_et, profit_right_et, money_left_et,
            money_right_et;
    private Button submit_bt, cancel_bt_new, alter_bt;
    private LinearLayout balance_ll, apply_ll, info_ll;
    private ImageView balance_iv, title_tv_2_img;
    private TextView balance_tv;
    private boolean isSetBalance = false;
    private String[] wheelItemArray = new String[]{"30天", "60天", "90天", "180天", "365天"};
    private String[] wheelItemValue = new String[]{"30", "60", "90", "180", "365"};
    private int leftPosition = -1, rightPosition = -1;
    private int whichFlag = 1; // 1-左边 2-右边
    // private CheckBox radio_rys,radio_xyb, radio_qyr, radio_sdt;
    private FrameLayout fl_keyboard;
    private View invisible_keyboard;
    private PayPasswordPopupWindow payPP;
    private Dialog loading;
    private MyGridView gv;
    private GvAdapter gvadapter;
    private List<Item> itemList;
    private String bespeak_id_new;
    private String appoint_money_new;
    private String appoint_rate_new;
    private String appoint_time_limit_new;
    private Dialog cancelDialog;
    private String[] iv_check_data;
    private DialogPopupWindow dialogPopupWindow;
    private View mainView;
    private QuickAction qa;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Intent intent = getIntent();
        hbas = new HttpBespeakApplyService(this, this);
        hbcs = new HttpBespeakCancelService(this, this);
        hbais = new HttpBespeakApplyInfoService(this, this);
        hbass = new HttpBespeakAlterService(this, this);
        hbts = new HttpBespeakTypeService(this, this);
        initView();
        if (null != intent.getExtras()) {
            String bespeak_id = intent.getStringExtra("bespeak_id");
            if (null != bespeak_id && !"".equals(bespeak_id))
                bespeak_id_new = bespeak_id;
            loadAppointData();
            apply_ll.setVisibility(View.GONE);
            info_ll.setVisibility(View.VISIBLE);
        } else {
            apply_ll.setVisibility(View.VISIBLE);
            info_ll.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void loadAppointData() {
        hbais.gainBespeakApplyInfo(bespeak_id_new);
    }

    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_bespeak_apply);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("预约自动投标");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);

        payPP = new PayPasswordPopupWindow(this, this);
        loading = createLoadingDialog(mContext, "加载中", true);
        cancelDialog = createDialog("您真的要取消自动投标吗？后期若再参与，可能需要更长时间的等待。", "确认取消", "返回",
                absoluteClickListener, negativeClickListener);
        profit_left_et = (EditText) findViewById(R.id.bespeak_apply_et_profit_left);
        setPricePoint(profit_left_et);
        profit_right_et = (EditText) findViewById(R.id.bespeak_apply_et_profit_right);
        money_left_et = (EditText) findViewById(R.id.bespeak_apply_et_money_left);
        money_right_et = (EditText) findViewById(R.id.bespeak_apply_et_money_right);
        date_left_tv = (TextView) findViewById(R.id.bespeak_apply_tv_data_left);
        date_right_tv = (TextView) findViewById(R.id.bespeak_apply_tv_data_right);
        submit_bt = (Button) findViewById(R.id.bespeak_apply_bt_submit);
        cancel_bt_new = (Button) findViewById(R.id.bespeak_info_bt_cancle_x);
        alter_bt = (Button) findViewById(R.id.bespeak_info_bt_alter);
        balance_ll = (LinearLayout) findViewById(R.id.bespeak_apply_ll_balance);
        apply_ll = (LinearLayout) findViewById(R.id.bespeak_apply_bottom_ll);
        info_ll = (LinearLayout) findViewById(R.id.bespeak_info_bottom_ll);
        balance_iv = (ImageView) findViewById(R.id.bespeak_apply_iv_balance);
        balance_tv = (TextView) findViewById(R.id.bespeak_apply_tv_balance);
        qa = new QuickAction(this);
        qa.dismissView();
        title_tv_2_img = (ImageView) findViewById(R.id.title_tv_2_img);
        // radio_rys = (CheckBox) findViewById(R.id.bespeak_apply_radio_rys);
        // radio_xyb = (CheckBox) findViewById(R.id.bespeak_apply_radio_xyb);
        // radio_qyr = (CheckBox) findViewById(R.id.bespeak_apply_radio_qyr);
        // radio_sdt = (CheckBox) findViewById(R.id.bespeak_apply_radio_sdt);
        fl_keyboard = (FrameLayout) findViewById(R.id.bespeak_apply_fl_keyboard);
        invisible_keyboard = findViewById(R.id.bespeak_apply_invisible_keyboard);
        // radio_xyb.setOnCheckedChangeListener(this);
        // radio_qyr.setOnCheckedChangeListener(this);
        // radio_sdt.setOnCheckedChangeListener(this);
        itemList = new ArrayList<Item>();
        gv = (MyGridView) findViewById(R.id.bespeak_apply_radio_gv);
        gvadapter = new GvAdapter();
        gv.setAdapter(gvadapter);
        date_left_tv.setOnClickListener(this);
        date_right_tv.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        balance_ll.setOnClickListener(this);
        cancel_bt_new.setOnClickListener(this);
        alter_bt.setOnClickListener(this);
        title_tv_2_img.setOnClickListener(this);
        int h = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        fl_keyboard.measure(0, h);
        int keyboard_height = fl_keyboard.getMeasuredHeight();
        LayoutParams i_keyboard_lp = (LayoutParams) invisible_keyboard
                .getLayoutParams();
        i_keyboard_lp.height = keyboard_height;
        invisible_keyboard.setLayoutParams(i_keyboard_lp);
        invisible_keyboard.setVisibility(View.INVISIBLE);

        wheelPop = new WheelViewPopupWindow(mContext, wheelItemArray, 0);
        wheelPop.setOnWVPWClickListener(this);

        if (!loading.isShowing())
            loading.show();
        loadData();
        mainView = findViewById(R.id.ll_main);
        dialogPopupWindow = new DialogPopupWindow(this, mainView, this);
    }

    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        wheelPop.dismiss();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_back:
                finish();
                break;
            case R.id.title_tv_2_img:
                qa.getContentView().setText("例如    设置90天，系统自动帮您在\n90天内循环投资短期宝项目");
                qa.showBespeakTOP(title_tv_2_img);
                break;
            case R.id.bespeak_apply_tv_data_left:
                whichFlag = 1;
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                wheelPop.showWheelViewPop(v, leftPosition < 0 ? 0 : leftPosition);
                break;

            case R.id.bespeak_apply_tv_data_right:
                whichFlag = 2;
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                wheelPop.showWheelViewPop(v, rightPosition < 0 ? 0 : rightPosition);
                break;

            case R.id.bespeak_apply_bt_submit:
                applyCheck(v, 0);
                break;

            case R.id.bespeak_apply_ll_balance:
                if (!isSetBalance) {
                    isSetBalance = true;
                    balance_ll.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.bg_button_corner_blue_half));
                    balance_iv.setImageDrawable(getResources().getDrawable(
                            R.drawable.icon_bespeak_apply_actual_balance_checked));
                    balance_tv.setTextColor(Color.parseColor("#228cff"));
                    balance_tv.setTextSize(18);
                    money_left_et.setEnabled(false);
                    money_left_et.setTextColor(Color.parseColor("#C5C5C5"));
                    money_left_et.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.bg_button_gray_half));
                    money_right_et.setEnabled(false);
                    money_right_et.setTextColor(Color.parseColor("#C5C5C5"));
                    money_right_et.setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.bg_button_gray_half));

                } else {
                    isSetBalance = false;
                    balance_ll.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.bg_button_corner_gray_half));
                    balance_iv.setImageDrawable(getResources().getDrawable(
                            R.drawable.icon_bespeak_apply_actual_balance));
                    balance_tv.setTextColor(Color.parseColor("#CCCCCC"));
                    balance_tv.setTextSize(18);
                    money_left_et.setEnabled(true);
                    money_left_et.setTextColor(Color.parseColor("#666666"));
                    money_left_et.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.bg_square_border));
                    money_right_et.setEnabled(true);
                    money_right_et.setTextColor(Color.parseColor("#666666"));
                    money_right_et.setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.bg_square_border));
                }
                break;
            case R.id.bespeak_info_bt_cancle_x:
                dialogPopupWindow.showWithMessage("您真的要取消自动投标吗？后期若再参与，可能需要更长时间的等待。", "6");
                break;
            case R.id.bespeak_info_bt_alter:
                applyCheck(v, 1);
                break;
        }
    }

    OnClickListener absoluteClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (cancelDialog.isShowing()) {
                cancelDialog.dismiss();
            }
            if (!loading.isShowing())
                loading.show();
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
        hbcs.getBespeakCancel(bespeak_id_new);
    }

    @Override
    public void onSubmitClick(int position) {
        if (1 == whichFlag) {
            leftPosition = position;
            date_left_tv.setText(wheelItemArray[position]);
        } else if (2 == whichFlag) {
            rightPosition = position;
            date_right_tv.setText(wheelItemArray[position]);
        }
    }

    @Override
    public void callPayCheckBack(boolean success) {

    }

    @Override
    public void doInvestSuccess(boolean success) {

    }

    private void applyCheck(View v, int applyflag) {
        if (!prj_type_a.equals("1") && !prj_type_b.equals("1") && !prj_type_f.equals("1")
                && !prj_type_h.equals("1")) {
            dialogPopupWindow.showWithMessage("请选择您要预约的产品", "0");
            return;
        }
        if (TextUtils.isEmpty(profit_left_et.getText().toString())) {
            dialogPopupWindow.showWithMessage("请输入预约年化收益率", "0");
            return;
        }

        if (!StringUtils.isFloat(profit_left_et.getText().toString())) {
            dialogPopupWindow.showWithMessage("预约年化收益率设置错误", "0");
            return;
        }

        if (leftPosition == -1) {
            dialogPopupWindow.showWithMessage("请选择预约有效期", "0");
            return;
        }

        if (!isSetBalance && (TextUtils.isEmpty(money_left_et.getText().toString()))) {
            dialogPopupWindow.showWithMessage("请输入预约金额", "0");
            return;
        }

        if (!isSetBalance && (!StringUtils.isFloat(money_left_et.getText().toString()))) {
            dialogPopupWindow.showWithMessage("预约金额设置错误", "0");
            return;
        }

        if (!isSetBalance && (Float.parseFloat(money_left_et.getText().toString()) < 1000)) {
            dialogPopupWindow.showWithMessage("最小预约金额不能小于1000元", "0");
            return;
        }

        if (!isSetBalance && (Float.parseFloat(money_left_et.getText().toString()) % 1000 != 0)) {
            dialogPopupWindow.showWithMessage("投资金额必须是1000的整数倍", "0");
            return;
        }

        if (!isSetBalance && (Float.parseFloat(money_left_et.getText().toString()) > 100000)) {
            dialogPopupWindow.showWithMessage("自动投标目前最大投资金额为10万元，请修改金额，谢谢！", "0");
            return;
        }

        String type = "", date = "", rate = "", money = "";

        if (leftPosition == rightPosition) {
            date = wheelItemArray[leftPosition];
        } else {
            date = wheelItemArray[leftPosition];
        }

        min_time = wheelItemValue[leftPosition];

        rate = String.valueOf(Float.parseFloat(profit_left_et.getText().toString())) + "%";
        min_rate = profit_left_et.getText().toString();

        if (isSetBalance) {
            is_all_money = "1";
            money = "当前账户实际余额";
        } else {
            is_all_money = "0";
            money = String.valueOf(Float.parseFloat(money_left_et.getText().toString())) + "元";
            min_money = money_left_et.getText().toString();
        }
        iv_check_data = new String[]{prj_type_f, prj_type_b, prj_type_h, prj_type_a};
        payPP.setBespeakData(type, date, rate, money, applyflag, iv_check_data, this);
        payPP.goAnim("", v, 5, "", false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == 3) {
            setResult(4);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void doBespeak(String payPwd, int bp_applyflag) {
        if (!loading.isShowing())
            loading.show();
        applyBespeak(payPwd, bp_applyflag);
    }

    private String min_rate, max_rate, min_time, max_time, min_money,
            max_money, prj_type_a = "1", prj_type_b = "1", prj_type_f = "1", prj_type_h = "1",
            is_all_money;

    private void applyBespeak(String pwd, int bp_applyflag) {
        if (bp_applyflag == 0) {
            hbas.gainBespeakApply(pwd, min_rate, min_time, min_money, prj_type_a, prj_type_b,
                    prj_type_f, prj_type_h, true);
        } else if (bp_applyflag == 1) {
            alterBespeak(pwd);
        }
    }

    private void alterBespeak(String pwd) {
        hbass.gainBespeakAlter(bespeak_id_new, pwd, min_rate, min_time, min_money, prj_type_a,
                prj_type_b, prj_type_f, prj_type_h);
    }

    private void loadData() {
        hbts.gainBespeakType();
    }

    public class GvAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // 根据位置得到视图
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_bespeak_type_item, null);
                holder = new ViewHolder();
                holder.checkBox_type = (CheckBox) convertView.findViewById(R.id.bespeak_apply_radio);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (itemList.size() == 1 && itemList.get(position).bespeak.prj_type_name
                    .equals("短期宝")) {
                holder.checkBox_type.setClickable(false);
                prj_type_a = "1";
                prj_type_h = "0";
                prj_type_b = "0";
                prj_type_f = "0";
            }
            holder.checkBox_type.setText(itemList.get(position).bespeak.prj_type_name);
            holder.checkBox_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        buttonView.setTextColor(Color.parseColor("#2B91FF"));
                    } else {
                        buttonView.setTextColor(Color.parseColor("#AAAAAA"));
                    }

                    if (buttonView.getText().equals("短期宝") && isChecked) {
                        prj_type_a = "1";
                    } else if (buttonView.getText().equals("短期宝") && !isChecked) {
                        prj_type_a = "0";
                    }

                    if (buttonView.getText().equals("速转让") && isChecked) {
                        prj_type_h = "1";
                    } else if (buttonView.getText().equals("速转让") && !isChecked) {
                        prj_type_h = "0";
                    }

                    if (buttonView.getText().equals("企益融") && isChecked) {
                        prj_type_b = "1";
                    } else if (buttonView.getText().equals("企益融") && !isChecked) {
                        prj_type_b = "0";
                    }

                    if (buttonView.getText().equals("hayb") && isChecked) {
                        prj_type_f = "1";
                    } else if (buttonView.getText().equals("hayb") && !isChecked) {
                        prj_type_f = "0";
                    }
                }
            });

            return convertView;
        }

    }

    private class ViewHolder {
        CheckBox checkBox_type;
    }

    class Item {
        public final BespeakItem bespeak;

        public Item(BespeakItem bespeak) {
            this.bespeak = bespeak;
        }

    }

    private class BespeakItem {
        private String post_key;
        private String prj_type_name;
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
    public void gainBespeakTypesuccess(BespeakTypeJson response) {
        if (loading.isShowing())
            loading.dismiss();
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    BespeakTypeData dataObj = response.getData();
                    BespeakType dataObjrys = dataObj.getA();
                    BespeakType dataObjsdt = dataObj.getH();
                    BespeakType dataObjxyb = dataObj.getF();
                    BespeakType dataObjxyr = dataObj.getB();
                    if (dataObjrys != null) {
                        BespeakItem b_item = new BespeakItem();
                        b_item.post_key = dataObjrys.getPost_key();
                        b_item.prj_type_name = dataObjrys.getPrj_type_name();
                        Item item = new Item(b_item);
                        itemList.add(item);
                    }
                    if (dataObjsdt != null) {
                        BespeakItem b_item = new BespeakItem();
                        b_item.post_key = dataObjsdt.getPost_key();
                        b_item.prj_type_name = dataObjsdt.getPrj_type_name();
                        Item item = new Item(b_item);
                        itemList.add(item);
                    }
                    if (dataObjxyb != null) {
                        BespeakItem b_item = new BespeakItem();
                        b_item.post_key = dataObjxyb.getPost_key();
                        b_item.prj_type_name = dataObjxyb.getPrj_type_name();
                        Item item = new Item(b_item);
                        itemList.add(item);
                    }
                    if (dataObjxyr != null) {
                        BespeakItem b_item = new BespeakItem();
                        b_item.post_key = dataObjxyr.getPost_key();
                        b_item.prj_type_name = dataObjxyr.getPrj_type_name();
                        Item item = new Item(b_item);
                        itemList.add(item);
                    }
                    gvadapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainBespeakTypefail() {
        if (loading.isShowing())
            loading.dismiss();
        showToast("网络不给力");
    }

    @Override
    public void gainBespeakAltersuccess(BaseJson response) {
        if (loading.isShowing())
            loading.dismiss();
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    if (payPP.isShowing())
                        payPP.dismiss();
                    showToast("预约已修改");
                    setResult(2);
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
    public void gainBespeakAlterfail() {
        if (loading.isShowing())
            loading.dismiss();
        showToast("网络不给力");
    }

    @Override
    public void gainBespeakApplyInfosuccess(BespeakApplyInfoJson response) {
        if (loading.isShowing())
            loading.dismiss();
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    BespeakApplyInfoData dataObj = response.getData();
                    String appoint_money = dataObj.getAppoint_money();
                    if (null != appoint_money && !"".equals(appoint_money)) {
                        appoint_money_new = appoint_money;
                    }
                    money_left_et.setText(appoint_money_new);
                    String appoint_rate = dataObj.getAppoint_rate();
                    if (null != appoint_rate && !"".equals(appoint_rate)) {
                        appoint_rate_new = appoint_rate;
                    }
                    profit_left_et.setText(appoint_rate_new);
                    String appoint_time_limit = dataObj.getAppoint_day() + "天";
                    if (!"".equals(appoint_time_limit)) {
                        appoint_time_limit_new = appoint_time_limit;
                    }
                    date_left_tv.setText(appoint_time_limit_new);
                    leftPosition = 0;
                } else {
                    showToast(response.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainBespeakApplyInfofail() {
        if (loading.isShowing())
            loading.dismiss();
        showToast("网络不给力");
    }

    @Override
    public void gainBespeakCancelsuccess(BaseJson response) {
        if (loading.isShowing())
            loading.dismiss();
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    showToast("预约已取消");
                    setResult(2);
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
        if (loading.isShowing())
            loading.dismiss();
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
                    startActivityForResult(intent, 2);
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
}
