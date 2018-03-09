package com.lcshidai.lc.ui.account;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.PIPWCallBack;
import com.lcshidai.lc.impl.account.WithdrawalsImpl;
import com.lcshidai.lc.impl.account.WithdrawalsIsBespeakImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.WithdrawalsBank;
import com.lcshidai.lc.model.account.WithdrawalsCard;
import com.lcshidai.lc.model.account.WithdrawalsData;
import com.lcshidai.lc.model.account.WithdrawalsJson;
import com.lcshidai.lc.model.account.WithdrawalsList;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckData;
import com.lcshidai.lc.service.account.HttpWithdrawalsIsBespeakService;
import com.lcshidai.lc.service.account.HttpWithdrawalsService;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.widget.ppwindow.DialogPopupWindow;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow.DismissListener;
import com.lcshidai.lc.widget.ppwindow.WheelViewPopupWindow;
import com.lcshidai.lc.widget.ppwindow.WheelViewPopupWindow.OnWVPWClickListener;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;
import com.lcshidai.lc.widget.text.UserKeyboardEditText;
import com.lcshidai.lc.widget.text.UserKeyboardEditText.UserKeyBoardEditTextClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现
 *
 * @author 000853
 */
public class WithdrawalsActivity extends TRJActivity
        implements WithdrawalsImpl, WithdrawalsIsBespeakImpl, OnClickListener, OnWVPWClickListener, PIPWCallBack,
        DismissListener, UserKeyBoardEditTextClickListener, DialogPopupWindow.ChooseListener {
    private Dialog cancelDialog;
    private String va;
    HttpWithdrawalsService hws;
    HttpWithdrawalsIsBespeakService hwibs;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    ImageButton mBackBtn;
    private Context mContext;

    private TextView tv_yue, tv_xiane, tv_samecard, tv_cs, tv_samecardaccount;
    private CustomEditTextLeftIcon et_bankcard_number;
    private UserKeyboardEditText et_money;
    private ScrollView main_sv;
    private FrameLayout submit_fl, keyboard_fl;
    private LinearLayout ll_scroll_top;
    private View invisible_submit, invisible_keyboard;
    private LinearLayout ll_kaihuhang, ll_bank;
    private TextView kaihuhang_tv, bank_tv;
    private TextView free_edu_tv, free_time_tv;

    private int scroll_top_height;
    private boolean isKeyboardShowing = false;
    private WheelViewPopupWindow wheelPop;
    private String str_xiane = "为确保资金安全，该卡本次最多可提现";
    private LinearLayout ll_bank_card;
    private View line_bank1;
    private Dialog loading;
    private ImageView iv_toright_kaihuhang;
    private String withdrawals_yue;

    private List<BankInfo> bankInfoList;
    private String[] wheelItemArray;
    private boolean kaihuhangClickAble = false;
    private int bankListPosition = 0;
    private Button submit_bt;
    private static final String BANK_CARD_HINT = "请选择银行卡";
    private static final String KAIHUHANG_HINT = "请选择开户行";
    private PayPasswordPopupWindow payWindow;
    private int withdrwalasFlag = 0; // 0-新增 1-修改 2-普通提现 3-不可修改的卡但是没开户行
    private DialogPopupWindow dialogPopupWindow;
    private View mainView;
    private String message;
    private Dialog telephoneDialog;
    private String TELE_NUMBER = "400-158-3737 ";
    private View mv;
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    dialogPopupWindow.showWithMessage(message, "7");
                    // cancelDialog = createDialog(message, "确认", "取消",
                    // new MyAbsoluteClickListener(),
                    // negativeClickListener);
                    // if (!cancelDialog.isShowing())
                    // cancelDialog.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hws = new HttpWithdrawalsService(this, this);
        hwibs = new HttpWithdrawalsIsBespeakService(this, this);
        initView();
        initKeyborad();
        getWithdrawalsData();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_withdrawals);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setBackgroundColor(Color.TRANSPARENT);
        mSaveBtn.setTextColor(Color.BLACK);
        mSaveBtn.setPadding(0, 0, DensityUtil.dip2px(mContext, 10), 0);
        mSaveBtn.setText("明细");
        mSaveBtn.setTextSize(13);
        mSaveBtn.setVisibility(View.VISIBLE);
        mSaveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent_withdrawals = new Intent();
                // intent_withdrawals.setClass(WithdrawalsActivity.this,
                // WithdrawalsListActivity.class);
                // intent_withdrawals.putExtra("intent_flag", 1);
                // //
                // startActivity(intent_withdrawals);

                Intent new_recharge_intent = new Intent();
                new_recharge_intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/moneyRecord/51");
                new_recharge_intent.putExtra("title", "提现明细");
                new_recharge_intent.setClass(WithdrawalsActivity.this, MainWebActivity.class);
                startActivity(new_recharge_intent);
            }
        });
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("提现");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);

        payWindow = new PayPasswordPopupWindow(WithdrawalsActivity.this, this);
        payWindow.setDismissListener(this);
        loading = createLoadingDialog(mContext, "加载中", true);
        bankInfoList = new ArrayList<WithdrawalsActivity.BankInfo>();

        main_sv = (ScrollView) findViewById(R.id.withdrawals_main_scroll);
        submit_fl = (FrameLayout) findViewById(R.id.withdrawals_fl_submit);
        invisible_submit = findViewById(R.id.withdrawals_bottom_invisible_submit);
        invisible_keyboard = findViewById(R.id.withdrawals_bottom_invisible_keyboard);
        keyboard_fl = (FrameLayout) findViewById(R.id.withdrawals_fl_keyboard);
        ll_scroll_top = (LinearLayout) findViewById(R.id.withdrawals_ll_scroll_top);
        ll_bank = (LinearLayout) findViewById(R.id.withdrawals_ll_bank);
        ll_kaihuhang = (LinearLayout) findViewById(R.id.withdrawals_ll_kaihuhang);
        kaihuhang_tv = (TextView) findViewById(R.id.withdrawals_item_tv_value_kaihuhang);
        bank_tv = (TextView) findViewById(R.id.withdrawals_item_tv_value_bank);
        free_edu_tv = (TextView) findViewById(R.id.withdrawals_tv_free_edu);
        free_time_tv = (TextView) findViewById(R.id.withdrawals_tv_free_time);
        ll_bank_card = (LinearLayout) findViewById(R.id.withdrawwals_ll_bank_card);
        line_bank1 = findViewById(R.id.withdrawals_line_bank1);
        iv_toright_kaihuhang = (ImageView) findViewById(R.id.withdrawals_item_iv_toright_kaihuhang);
        submit_bt = (Button) findViewById(R.id.withdrawals_bt_submit);

        submit_bt.setOnClickListener(this);

        ll_bank_card.setVisibility(View.GONE);
        line_bank1.setVisibility(View.GONE);

        ll_bank.setOnClickListener(this);
        ll_kaihuhang.setOnClickListener(this);

        int h = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        submit_fl.measure(0, h);
        keyboard_fl.measure(0, h);
        ll_scroll_top.measure(0, h);
        int submit_height = submit_fl.getMeasuredHeight();
        int keyboard_height = keyboard_fl.getMeasuredHeight();
        scroll_top_height = ll_scroll_top.getMeasuredHeight();
        LayoutParams i_submit_lp = (LayoutParams) invisible_submit.getLayoutParams();
        i_submit_lp.height = submit_height;
        invisible_submit.setLayoutParams(i_submit_lp);
        invisible_submit.setVisibility(View.GONE);
        LayoutParams i_keyboard_lp = (LayoutParams) invisible_keyboard.getLayoutParams();
        i_keyboard_lp.height = keyboard_height - submit_height;
        invisible_keyboard.setLayoutParams(i_keyboard_lp);
        invisible_keyboard.setVisibility(View.GONE);

        tv_yue = (TextView) findViewById(R.id.withdrawals_item_tv_value_yue);
        tv_xiane = (TextView) findViewById(R.id.withdrawals_tv_bankcard_xiane);
        tv_cs = (TextView) findViewById(R.id.tv_cs);
        tv_cs.setOnClickListener(this);
        telephoneDialog = createDialog("拨打" + TELE_NUMBER + "？", "拨打", "取消", new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (telephoneDialog.isShowing())
                    telephoneDialog.dismiss();
                Intent phoneIntent = new Intent(Intent.ACTION_CALL,
                        Uri.parse("tel:" + TELE_NUMBER.replaceAll("-", "")));
                startActivity(phoneIntent);
            }
        }, new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (telephoneDialog.isShowing())
                    telephoneDialog.dismiss();
            }
        });
        tv_samecard = (TextView) findViewById(R.id.tv_samecard);
        tv_samecard.setClickable(true);
        tv_samecard.setMovementMethod(LinkMovementMethod.getInstance());
        tv_samecardaccount = (TextView) findViewById(R.id.tv_samecardaccount);
        NoUnderlineSpan mNoUnderlineSpan = new NoUnderlineSpan();
        UnderlineSpan underlineSpan = new UnderlineSpan();
        CharSequence text = tv_samecard.getText();
        // if (text instanceof Spannable) {
        SpannableString style = new SpannableString(text);

        style.setSpan(new TestSpanClick(this), 15, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 当然这个2和6不用写死，根据实际需要来取值
        style.setSpan(underlineSpan, 15, 19, Spanned.SPAN_MARK_MARK);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#FF811C")), 15, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 实现部分文字颜色改变
//		style.setSpan(what, start, end, flags);
        tv_samecard.setText(style);
        // }

        String xiane_value = str_xiane + "-" + "元";
        SpannableString ss_xiane = new SpannableString(xiane_value);
        ss_xiane.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7042")), str_xiane.length(),
                xiane_value.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_xiane.setText(ss_xiane);

        et_money = (UserKeyboardEditText) findViewById(R.id.withdrawals_et_money);
        // et_money.setHintParams(
        // "请输入提现金额，T+1日到账",
        // Color.parseColor("#C0C0C0"),
        // Integer.parseInt(getResources().getString(
        // R.string.dimens_withdrawals_item_edit_hint_string)));
        tv_samecardaccount.setText("0.00" + "元");// 预留字段
        et_money.setHintParams("此次最多可提现" + "0.00" + "元", Color.parseColor("#C0C0C0"), 14);
        et_money.setTextColor(Color.parseColor("#828282"));
        et_money.setTextSize(14);
        et_money.setDeleteButtonSize((int) getResources().getDimension(R.dimen.dimens_withdrawals_item_edit_hint));
        et_money.setSingleLine(true);
        et_money.setEditPadding(0, 0, (int) getResources().getDimension(R.dimen.dimens_withdrawals_item_edit_hint)
                + DensityUtil.dip2px(mContext, 5), 0);
        et_money.setClickListner(this);

        et_bankcard_number = (CustomEditTextLeftIcon) findViewById(R.id.withdrawals_et_bankcard_number);
        et_bankcard_number.setTextSize(14);
        et_bankcard_number.setBackgroundColor(Color.TRANSPARENT);
        et_bankcard_number.setEditMargin(0, 0, 0, 0);
        et_bankcard_number.setDeleteButtonMargin(0, 0, 0, 0);
        et_bankcard_number.getET().setBackgroundColor(Color.TRANSPARENT);
        et_bankcard_number.getET().setHint("请输入银行卡号");
        et_bankcard_number.getET().setHintTextColor(Color.parseColor("#C0C0C0"));
        et_bankcard_number.getET().setTextColor(Color.parseColor("#1572BD"));
        et_bankcard_number.getET().setSingleLine(true);
        et_bankcard_number
                .setDeleteButtonSize((int) getResources().getDimension(R.dimen.dimens_withdrawals_item_edit_hint));
        et_bankcard_number.setEditPadding(0, 0,
                (int) getResources().getDimension(R.dimen.dimens_withdrawals_item_edit_hint)
                        + DensityUtil.dip2px(mContext, 5),
                0);
        et_bankcard_number.cleanDefaultFoucus();
        et_bankcard_number.setInputType(InputType.TYPE_CLASS_NUMBER);

        et_bankcard_number.getET().setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (isKeyboardShowing) {
                        isKeyboardShowing = false;
                        // et_money.getET().clearFocus();
                        keyboard_fl.setVisibility(View.GONE);
                        invisible_keyboard.setVisibility(View.GONE);
                    }
                }
            }
        });

        et_bankcard_number.getET().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isKeyboardShowing) {
                    isKeyboardShowing = false;
                    // et_money.getET().clearFocus();
                    keyboard_fl.setVisibility(View.GONE);
                    invisible_keyboard.setVisibility(View.GONE);
                }
            }
        });

        et_bankcard_number.getET().setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            WithdrawalsActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

//		loading.show();
        mainView = findViewById(R.id.ll_main);
        dialogPopupWindow = new DialogPopupWindow(this, mainView, this);
    }

    private static class TestSpanClick extends ClickableSpan {
        private Context mContext;

        public TestSpanClick(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onClick(View widget) {
            Intent intent_activity = new Intent(this.mContext, MainWebActivity.class);
            intent_activity.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "#/sameCardRule");
            this.mContext.startActivity(intent_activity);
        }
    }

    private class NoUnderlineSpan extends UnderlineSpan {

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }
    }

    @Override
    protected void onPause() {
        if (null != wheelPop)
            wheelPop.dismiss();
        if (isKeyboardShowing) {
            isKeyboardShowing = false;
            // et_money.getET().clearFocus();
            keyboard_fl.setVisibility(View.GONE);
            invisible_keyboard.setVisibility(View.GONE);
        }
        super.onPause();
    }

    @Override
    public void onDelete() {
        editMoney = "";
    }

    @Override
    public void onUKBETClick() {
        et_bankcard_number.getET().clearFocus();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                WithdrawalsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (!isKeyboardShowing) {
            isKeyboardShowing = true;
            keyboard_fl.setVisibility(View.VISIBLE);
            invisible_keyboard.setVisibility(View.INVISIBLE);
            main_sv.scrollTo(0, scroll_top_height);
        }
    }

    @Override
    public void onSubmitClick(int position) {
        this.bankListPosition = position;

        khh_name = "";
        khh_sid = "";
        city_code = "";
        province_code = "";

        if (1 == bankInfoList.get(position).type) {
            et_bankcard_number.getET().setText("");
            String amount = bankInfoList.get(position).mcashoutAmount.replaceAll(",", "");
            String freeze_amount = bankInfoList.get(position).freeze_cashout_amount.replaceAll(",", "");
            String short_bank = bankInfoList.get(position).short_account_no;
            String shortNo = "(尾号" + short_bank.substring(1, short_bank.length());
            if (Float.parseFloat(amount) <= 0 && "0".equals(freeze_amount)) {
                withdrwalasFlag = 1;
                bank_tv.setText(bankInfoList.get(position).name + shortNo);
                kaihuhangClickAble = true;
                iv_toright_kaihuhang.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(bankInfoList.get(position).sub_bank_id)
                        || "0".equals(bankInfoList.get(position).sub_bank_id))
                    kaihuhang_tv.setText(KAIHUHANG_HINT);
                else
                    kaihuhang_tv.setText(bankInfoList.get(position).sub_bank);
                ll_bank_card.setVisibility(View.GONE);
                line_bank1.setVisibility(View.GONE);
                et_bankcard_number.setText(bankInfoList.get(position).account_no);
            } else {
                bank_tv.setText(bankInfoList.get(position).name + shortNo);
                if (TextUtils.isEmpty(bankInfoList.get(position).sub_bank_id)
                        || "0".equals(bankInfoList.get(position).sub_bank_id)) {
                    withdrwalasFlag = 3;
                    kaihuhangClickAble = true;
                    kaihuhang_tv.setText(KAIHUHANG_HINT);
                    iv_toright_kaihuhang.setVisibility(View.VISIBLE);
                } else {
                    withdrwalasFlag = 1;
                    kaihuhangClickAble = true;
                    kaihuhang_tv.setText(bankInfoList.get(position).sub_bank);
                    iv_toright_kaihuhang.setVisibility(View.VISIBLE);
                }
                et_bankcard_number.setText(bankInfoList.get(position).account_no);
                ll_bank_card.setVisibility(View.GONE);
                line_bank1.setVisibility(View.GONE);
            }
            String xiane_value = str_xiane + bankInfoList.get(position).cashoutAmount + "元";
            SpannableString ss_xiane = new SpannableString(xiane_value);
            ss_xiane.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7042")), str_xiane.length(),
                    xiane_value.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_xiane.setText(ss_xiane);
            tv_samecardaccount.setText(bankInfoList.get(position).cashoutAmount + "元");// 预留字段
            et_money.setHintParams("此次最多可提现" + bankInfoList.get(position).cashoutAmount + "元",
                    Color.parseColor("#C0C0C0"), 14);
        } else {
            withdrwalasFlag = 0;
            bank_tv.setText(bankInfoList.get(position).name);
            et_bankcard_number.getET().setText("");
            kaihuhangClickAble = true;
            ll_bank_card.setVisibility(View.GONE);
            line_bank1.setVisibility(View.GONE);
            kaihuhang_tv.setText(KAIHUHANG_HINT);
            iv_toright_kaihuhang.setVisibility(View.VISIBLE);
            String xiane_value = str_xiane + freeCashout + "元";
            SpannableString ss_xiane = new SpannableString(xiane_value);
            ss_xiane.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7042")), str_xiane.length(),
                    xiane_value.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv_xiane.setText(ss_xiane);
            tv_samecardaccount.setText(freeCashout + "元");// 预留字段
            et_money.setHintParams("此次最多可提现" + freeCashout + "元", Color.parseColor("#C0C0C0"), 14);
        }
    }

    @Override
    public void onDismiss() {
        // setResult(8);
        this.finish();
    }

    @Override
    public void callPayCheckBack(boolean success) {

    }

    @Override
    public void doInvestSuccess(boolean success) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                WithdrawalsActivity.this.finish();
                break;
            case R.id.tv_cs:
                if (!telephoneDialog.isShowing())
                    telephoneDialog.show();
                break;
            case R.id.withdrawals_ll_bank:
                if (null != wheelPop) {
                    if (isKeyboardShowing) {
                        isKeyboardShowing = false;
                        // et_money.getET().clearFocus();
                        keyboard_fl.setVisibility(View.GONE);
                        invisible_keyboard.setVisibility(View.GONE);
                    }
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            WithdrawalsActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    if (null != bankInfoList && bankInfoList.size() > 0) {
                        wheelPop.showWheelViewPop(v, bankListPosition);
                    }
                }
                break;
            case R.id.withdrawals_ll_kaihuhang:
//                if (kaihuhangClickAble) {
                if (!BANK_CARD_HINT.equals(bank_tv.getText().toString())) {
                    if (!TextUtils.isEmpty(bankInfoList.get(bankListPosition).code)) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, CityListActivity.class);
                        intent.putExtra("intent_flag", -1);
                        intent.putExtra("mCode", bankInfoList.get(bankListPosition).code);
                        startActivityForResult(intent, 20);
                    }
                } else {
                    // showToast("请先选择银行");
                    // dialogPopupWindow.showWithMessage("请先选择银行", "0");
                    createDialogDismissAuto("请先选择银行");
                }
//                }
                break;
            case R.id.withdrawals_bt_submit:
                try {
                    mv = v;
                    withdrawalsCheck(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void withdrawalsCheck(View v) throws Exception {
        String str_yue = "";
        if (!TextUtils.isEmpty(withdrawals_yue))
            str_yue = withdrawals_yue.replaceAll(",", "");
        if (TextUtils.isEmpty(withdrawals_yue) || Float.parseFloat(str_yue) == 0) {
            createDialogDismissAuto("账户资金不足");
            return;
        }
        String banktvStr = bank_tv.getText().toString();
        if (BANK_CARD_HINT.equals(banktvStr)) {
            createDialogDismissAuto("请选择银行卡");
            return;
        }
        if (KAIHUHANG_HINT.equals(kaihuhang_tv.getText().toString())
                || TextUtils.isEmpty(kaihuhang_tv.getText().toString())) {
            createDialogDismissAuto("请选择开户行");
            return;
        }
        if (TextUtils.isEmpty(editMoney) || Float.parseFloat(editMoney) == 0) {
            createDialogDismissAuto("请输入金额");
            return;
        }
        String cashAmount = TextUtils.isEmpty(bankInfoList.get(bankListPosition).cashoutAmount)
                ? freeCashout : bankInfoList.get(bankListPosition).cashoutAmount;
        cashAmount = cashAmount.replaceAll(",", "");
        if (Float.parseFloat(editMoney) > Float.parseFloat(cashAmount)) {
            createDialogDismissAuto("您输入的金额大于该次可提现的最大额度");
            return;
        }

        String sub_bank = TextUtils.isEmpty(khh_name) ? bankInfoList.get(bankListPosition).sub_bank : khh_name;
        String out_account_no = (withdrwalasFlag == 2 || withdrwalasFlag == 3)
                ? bankInfoList.get(bankListPosition).account_no : et_bankcard_number.getEdtText();
        String code = TextUtils.isEmpty(city_code) ? bankInfoList.get(bankListPosition).bank_city : city_code;
        String temp_pcode = TextUtils.isEmpty(province_code) ? bankInfoList.get(bankListPosition).bank_province
                : province_code;
        String sub_bank_id = TextUtils.isEmpty(khh_sid) ? bankInfoList.get(bankListPosition).sub_bank_id : khh_sid;
        String is_save_fund = (withdrwalasFlag == 2) ? "0" : "1";
        payWindow.setWithdrawalsParams(withdrwalasFlag, editMoney, bankInfoList.get(bankListPosition).myCode, sub_bank,
                out_account_no, bankInfoList.get(bankListPosition).bank_card_id, code, temp_pcode,
                bankInfoList.get(bankListPosition).name, sub_bank_id, is_save_fund);
        // payWindow.goAnim(editMoney, v, "", 2, "", false);
        isBespeak(editMoney, v);
    }

    private void isBespeak(final String editMoney, final View v) {
        hwibs.gainWithdrawalsIsBespeak(editMoney);
    }

    private String khh_name, khh_sid, city_code, province_code;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 20 && resultCode == 25) {
            khh_sid = data.getStringExtra("sid");
            khh_name = data.getStringExtra("khh_name");
            city_code = data.getStringExtra("city_code");
            province_code = data.getStringExtra("province_code");
            kaihuhang_tv.setText(khh_name);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String freeCashout = ""; // 自由提现额度

    private void getWithdrawalsData() {
        hws.gainWithdrawals();
    }

    class BankInfo {
        int type; // 0-银行 1-银行卡
        String bank_card_id; // 银行卡id
        String account_no; // 银行卡号
        String sub_bank; // 支行
        String sub_bank_id; // 支行id
        String bank_province; // 省份id
        String bank_city; // 城市id
        String cashoutAmount; // 可提现额度
        // String is_bankck; //银行卡是否可编辑 0-可以编辑 1-不能编辑
        String mcashoutAmount;
        String freeze_cashout_amount; // mcashoutAmount 和 freeze_cashout_amount
        // 都为0时银行卡可编辑
        String short_account_no;    // 已绑定银行卡缩写
        String name; // 银行名称
        String code; // 银行code
        String myCode; // 银行myCode
        String channel; // 充值通道
    }

    /**
     * 键盘
     */
    private Button bt_1, bt_2, bt_3, bt_4, bt_5, bt_6, bt_7, bt_8, bt_9, bt_dot, bt_0, bt_00;
    private ImageButton bt_delete, bt_hiden;
    private LinearLayout ll_plus, ll_reduce;
    private String editMoney = "";

    private void initKeyborad() {
        bt_1 = (Button) findViewById(R.id.keyborad_bt_1);
        bt_2 = (Button) findViewById(R.id.keyborad_bt_2);
        bt_3 = (Button) findViewById(R.id.keyborad_bt_3);
        bt_4 = (Button) findViewById(R.id.keyborad_bt_4);
        bt_5 = (Button) findViewById(R.id.keyborad_bt_5);
        bt_6 = (Button) findViewById(R.id.keyborad_bt_6);
        bt_7 = (Button) findViewById(R.id.keyborad_bt_7);
        bt_8 = (Button) findViewById(R.id.keyborad_bt_8);
        bt_9 = (Button) findViewById(R.id.keyborad_bt_9);
        bt_dot = (Button) findViewById(R.id.keyborad_bt_dot);
        bt_0 = (Button) findViewById(R.id.keyborad_bt_0);
        bt_00 = (Button) findViewById(R.id.keyborad_bt_00);
        bt_delete = (ImageButton) findViewById(R.id.keyboard_ib_delete);
        bt_hiden = (ImageButton) findViewById(R.id.keyboard_ib_hidden);
        ll_plus = (LinearLayout) findViewById(R.id.keyboard_ll_plus);
        ll_reduce = (LinearLayout) findViewById(R.id.keyboard_ll_reduce);

        bt_1.setOnClickListener(new KeyboardClickListener(1));
        bt_2.setOnClickListener(new KeyboardClickListener(2));
        bt_3.setOnClickListener(new KeyboardClickListener(3));
        bt_4.setOnClickListener(new KeyboardClickListener(4));
        bt_5.setOnClickListener(new KeyboardClickListener(5));
        bt_6.setOnClickListener(new KeyboardClickListener(6));
        bt_7.setOnClickListener(new KeyboardClickListener(7));
        bt_8.setOnClickListener(new KeyboardClickListener(8));
        bt_9.setOnClickListener(new KeyboardClickListener(9));
        bt_0.setOnClickListener(new KeyboardClickListener(0));
        bt_dot.setOnClickListener(new KeyboardClickListener(-1));
        bt_00.setOnClickListener(new KeyboardClickListener(-2));
        bt_delete.setOnClickListener(new KeyboardClickListener(-3));
        bt_hiden.setOnClickListener(new KeyboardClickListener(-4));
        ll_plus.setOnClickListener(new KeyboardClickListener(1000));
        ll_reduce.setOnClickListener(new KeyboardClickListener(-1000));
    }

    class KeyboardClickListener implements OnClickListener {

        int flag;

        KeyboardClickListener(int flag) {
            this.flag = flag;
        }

        @Override
        public void onClick(View v) {
            switch (flag) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 0:
                    if ("0".equals(editMoney)) {
                        editMoney = String.valueOf(flag);
                    } else {
                        if (editMoney.indexOf(".") == -1) {
                            if (editMoney.length() >= 11) {
                                return;
                            }
                        }

                        if (String.valueOf(editMoney).indexOf(".") != -1
                                && ((editMoney.length() - editMoney.indexOf(".") - 1) >= 2)) {
                            return;
                        }
                        editMoney = editMoney + String.valueOf(flag);
                    }
                    break;
                case 1000:
                    if ("".equals(editMoney) || "0".equals(editMoney)) {
                        editMoney = "1000";
                    } else {
                        if (editMoney.indexOf(".") != -1) {
                            String before = editMoney.substring(0, editMoney.indexOf("."));
                            String after = editMoney.substring(editMoney.indexOf("."), editMoney.length());
                            if (before.length() > 11) {
                                return;
                            } else if (before.length() == 11) {
                                if (String.valueOf((Long.parseLong(before) + 1000)).length() > 11) {
                                    return;
                                } else {
                                    editMoney = String.valueOf(Long.parseLong(before) + 1000) + after;
                                }
                            } else {
                                editMoney = String.valueOf(Long.parseLong(before) + 1000) + after;
                            }
                        } else {
                            if (editMoney.length() > 11) {
                                return;
                            } else if (editMoney.length() == 11) {
                                if (String.valueOf(Long.parseLong(editMoney) + 1000).length() > 11) {
                                    return;
                                } else {
                                    editMoney = String.valueOf(Long.parseLong(editMoney) + 1000);
                                }
                            } else {
                                editMoney = String.valueOf(Long.parseLong(editMoney) + 1000);
                            }
                        }
                    }
                    break;
                case -1000:
                    if ("".equals(editMoney) || "0".equals(editMoney)) {
                        editMoney = "";
                    } else {
                        if (editMoney.indexOf(".") != -1) {
                            String before = editMoney.substring(0, editMoney.indexOf("."));
                            String after = editMoney.substring(editMoney.indexOf("."), editMoney.length());
                            if (Long.parseLong(before) > 1000) {
                                editMoney = String.valueOf(Long.parseLong(before) - 1000) + after;
                            } else if (Long.parseLong(before) == 1000) {
                                editMoney = "0" + after;
                            } else {
                                editMoney = "";
                            }
                        } else {
                            if (Long.parseLong(editMoney) > 1000) {
                                editMoney = String.valueOf(Long.parseLong(editMoney) - 1000);
                            } else {
                                editMoney = "";
                            }
                        }
                    }
                    break;
                // 小数点
                case -1:
                    if (String.valueOf(editMoney).indexOf(".") == -1) {
                        if ("".equals(editMoney))
                            editMoney = "0.";
                        else
                            editMoney = editMoney + ".";
                    } else {
                        return;
                    }
                    break;
                // 00
                case -2:
                    if ("0".equals(editMoney)) {
                        return;
                    }
                    if ("".equals(editMoney)) {
                        editMoney = "0";
                    } else {
                        if (String.valueOf(editMoney).indexOf(".") != -1) {
                            if ((editMoney.length() - editMoney.indexOf(".") - 1) >= 2) {
                                return;
                            } else if ((editMoney.length() - editMoney.indexOf(".") - 1) == 1) {
                                editMoney = editMoney + "0";
                            } else {
                                editMoney = editMoney + "00";
                            }
                        } else {
                            if (editMoney.length() >= 11) {
                                return;
                            } else if (editMoney.length() == 10) {
                                editMoney = editMoney + "0";
                            } else {
                                editMoney = editMoney + "00";
                            }
                        }
                    }
                    break;
                // 删除
                case -3:
                    if (!"".equals(editMoney))
                        editMoney = editMoney.substring(0, editMoney.length() - 1);
                    break;
                // 隐藏
                case -4:
                    if (isKeyboardShowing) {
                        isKeyboardShowing = false;
                        // et_money.getET().clearFocus();
                        keyboard_fl.setVisibility(View.GONE);
                        invisible_keyboard.setVisibility(View.GONE);
                    }
                    break;
            }
            et_money.setText(editMoney);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isKeyboardShowing) {
                isKeyboardShowing = false;
                // et_money.getET().clearFocus();
                keyboard_fl.setVisibility(View.GONE);
                invisible_keyboard.setVisibility(View.GONE);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void gainWithdrawalssuccess(WithdrawalsJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    WithdrawalsData dataObj = response.getData();
                    String is_has_bank = dataObj.getIs_has_bank(); // 是否已经绑过银行卡
                    withdrawals_yue = dataObj.getAmount_use_view(); // 用户可用余额
                    freeCashout = dataObj.getFreeCashout(); // 自由提现额度
                    String free_money = dataObj.getFree_money(); // 抵用券
                    String free_times = dataObj.getFree_tixian_times(); // 免费提现次数

                    List<WithdrawalsList> listArray = dataObj.getData();
                    String cardNo = "";
                    if (listArray.size() > 0)
                        wheelItemArray = new String[listArray.size()];
                    for (int i = 0; i < listArray.size(); i++) {
                        BankInfo bankInfo = new BankInfo();
                        WithdrawalsList itemObj = listArray.get(i);
                        if ("card".equals(itemObj.getType())) {
                            bankInfo.type = 1;
                            WithdrawalsCard cardObj = itemObj.getCardObjdata();
                            WithdrawalsBank bankObj = itemObj.getBankObjdata();
                            bankInfo.bank_card_id = cardObj.getBank_card_id();
                            bankInfo.account_no = cardObj.getAccount_no();
                            bankInfo.channel = cardObj.getChannel();
                            bankInfo.code = cardObj.getCode();
                            bankInfo.name = cardObj.getName();
                            bankInfo.sub_bank = cardObj.getSub_bank();
                            bankInfo.sub_bank_id = cardObj.getSub_bank_id();
                            bankInfo.bank_province = cardObj.getBank_province();
                            bankInfo.bank_city = cardObj.getBank_city();
                            bankInfo.cashoutAmount = cardObj.getCashoutAmount();
                            bankInfo.myCode = bankObj.getMyCode();
                            bankInfo.short_account_no = cardObj.getShort_account_no();
                            // bankInfo.is_bankck =
                            // cardObj.getString("is_bankck");
                            if (cardObj.getMcashoutAmount() == null || ("").equals(cardObj.getMcashoutAmount())) {
                                bankInfo.mcashoutAmount = "0";
                            } else {
                                bankInfo.mcashoutAmount = cardObj.getMcashoutAmount();
                            }

                            if (cardObj.getFreeze_cashout_amount() == null
                                    || ("").equals(cardObj.getFreeze_cashout_amount())) {
                                bankInfo.freeze_cashout_amount = "0";
                            } else {
                                bankInfo.freeze_cashout_amount = cardObj.getFreeze_cashout_amount();
                            }
                            // bankInfo.mcashoutAmount =
                            // cardObj.isNull("mcashoutAmount") ? "0" :
                            // (TextUtils.isEmpty(cardObj.getString("mcashoutAmount"))
                            // ? "0" : cardObj.getString("mcashoutAmount"));
                            // bankInfo.freeze_cashout_amount =
                            // cardObj.isNull("freeze_cashout_amount") ? "0" :
                            // (TextUtils.isEmpty(cardObj.getString("freeze_cashout_amount"))
                            // ? "0" :
                            // cardObj.getString("freeze_cashout_amount"));

                            String card = cardObj.getShort_account_no();
                            cardNo = "(尾号" + card.substring(1, card.length());
                            wheelItemArray[i] = bankInfo.name + cardNo;
                        } else {
                            bankInfo.type = 0;
                            WithdrawalsBank bankObj = itemObj.getBankObjdata();
                            bankInfo.name = bankObj.getName();
                            bankInfo.code = bankObj.getCode();
                            bankInfo.myCode = bankObj.getMyCode();
                            // bankInfo.channel = bankObj.getString("channel");

                            wheelItemArray[i] = bankInfo.name;
                        }
                        bankInfoList.add(bankInfo);
                    }
                    wheelPop = new WheelViewPopupWindow(mContext, wheelItemArray, 1);
                    wheelPop.setOnWVPWClickListener(WithdrawalsActivity.this);

                    String amount_use_view = withdrawals_yue + "元";
                    free_edu_tv.setText("￥" + free_money);
                    free_time_tv.setText(free_times + "次");
                    SpannableString ss_yue = new SpannableString(amount_use_view);
                    ss_yue.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFF8732")), 0,
                            amount_use_view.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_yue.setText(ss_yue);
                    if (bankInfoList.size() <= 0)
                        return;
                    if ("1".equals(is_has_bank)) {
                        String amount = bankInfoList.get(0).mcashoutAmount.replaceAll(",", "");
                        String freeze_amount = bankInfoList.get(0).freeze_cashout_amount.replaceAll(",", "");
                        if (Float.parseFloat(amount) <= 0 && "0".equals(freeze_amount)) {
                            withdrwalasFlag = 1;
                            bank_tv.setText(bankInfoList.get(0).name + cardNo);
                            kaihuhangClickAble = true;
                            iv_toright_kaihuhang.setVisibility(View.VISIBLE);
                            if (TextUtils.isEmpty(bankInfoList.get(0).sub_bank_id)
                                    || "0".equals(bankInfoList.get(0).sub_bank_id))
                                kaihuhang_tv.setText(KAIHUHANG_HINT);
                            else
                                kaihuhang_tv.setText(bankInfoList.get(0).sub_bank);
                            ll_bank_card.setVisibility(View.GONE);
                            line_bank1.setVisibility(View.GONE);
                            et_bankcard_number.setText(bankInfoList.get(0).account_no);
                        } else {
                            bank_tv.setText(wheelItemArray[0]);
                            if (TextUtils.isEmpty(bankInfoList.get(0).sub_bank_id)
                                    || "0".equals(bankInfoList.get(0).sub_bank_id)) {
                                withdrwalasFlag = 3;
                                kaihuhangClickAble = true;
                                kaihuhang_tv.setText(KAIHUHANG_HINT);
                                iv_toright_kaihuhang.setVisibility(View.VISIBLE);
                            } else {
                                withdrwalasFlag = 2;
                                kaihuhangClickAble = false;
                                kaihuhang_tv.setText(bankInfoList.get(0).sub_bank);
                                iv_toright_kaihuhang.setVisibility(View.GONE);
                            }
                        }
                        String xiane_value = str_xiane + bankInfoList.get(0).cashoutAmount + "元";
                        SpannableString ss_xiane = new SpannableString(xiane_value);
                        ss_xiane.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7042")), str_xiane.length(),
                                xiane_value.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_xiane.setText(ss_xiane);

                        tv_samecardaccount.setText(bankInfoList.get(0).cashoutAmount + "元");// 预留字段
                        et_money.setHintParams("此次最多可提现" + bankInfoList.get(0).cashoutAmount + "元",
                                Color.parseColor("#C0C0C0"), 14);
                    } else {
                        kaihuhangClickAble = true;
                        bank_tv.setText(BANK_CARD_HINT);
                        kaihuhang_tv.setText(KAIHUHANG_HINT);
                        iv_toright_kaihuhang.setVisibility(View.VISIBLE);
                        String xiane_value = str_xiane + freeCashout + "元";
                        SpannableString ss_xiane = new SpannableString(xiane_value);
                        ss_xiane.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7042")), str_xiane.length(),
                                xiane_value.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_xiane.setText(ss_xiane);

                        et_money.setHintParams("此次最多可提现" + freeCashout + "元", Color.parseColor("#C0C0C0"), 14);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loading.isShowing()) {
            loading.dismiss();
        }
    }

    @Override
    public void gainWithdrawalsfail() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        showToast("网络不给力");
    }

    @Override
    public void doInvestCheckSuccess(boolean success, FinanceInvestPBuyCheckData model) {

    }

    @Override
    public void chooseItem(int markItem) {
        switch (markItem) {
            case 0:
                payWindow.goAnim(va, findViewById(R.id.ll_main), 2, "", false);
                break;
            case 1:

                break;
            default:
                break;
        }
    }

    @Override
    public void gainWithdrawalsIsBespeaksuccess(String emoney, BaseJson response) {
        if (loading.isShowing())
            loading.dismiss();
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    WithdrawalsActivity.this.va = emoney;
                    message = response.getMessage();
                    mHandler.sendEmptyMessage(0);
                } else {
                    payWindow.goAnim(emoney, mv, 2, "", false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainWithdrawalsIsBespeakfail() {
        if (loading.isShowing())
            loading.dismiss();
        showToast("网络不给力");
    }

}
