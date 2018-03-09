package com.lcshidai.lc.ui.account;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.account.RechargeListImpl;
import com.lcshidai.lc.model.account.ApplyCashoutData;
import com.lcshidai.lc.model.account.ApplyCashoutJson;
import com.lcshidai.lc.model.account.RechargeList;
import com.lcshidai.lc.model.account.RechargeListData;
import com.lcshidai.lc.model.account.RechargeListJson;
import com.lcshidai.lc.service.account.HttpRechargeListService;
import com.lcshidai.lc.ui.AgreementActivity;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.Thumbnail;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值记录
 *
 * @author 000853
 */
public class RechargeListActivity extends TRJActivity implements OnClickListener, OnScrollListener, OnItemClickListener, OnCheckedChangeListener, RechargeListImpl {
    HttpRechargeListService hrls;
    private final String RECHARGE_LIST_URL = "Mobile2/PayAccount/getUserRechargeRecord";
    //	private final String BALANCE_URL = "Mobile2/PayAccount/getApplyCashout";
    private final String RECHARGE_CHECK_URL = "Mobile2/Auth/authStatus";

    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    ImageButton mBackBtn;
    //	private CustomEditTextLeftIcon money_et;
    private LinearLayout keyboard_main, bottom_ll;
    //	private View gray_view;
    private TranslateAnimation showAnim, dismissAnim, moneyDismissAnim, moneyShowAnim;
    private boolean isKeyboardShowing = false;
    private boolean isKeyboardDismiss = true;
    private ListView recharge_lv;
    private List<RechargeItem> rechargeList;
    private RechargeListAdapter rechargeAdapter;
    private View footerView;
    private int currentPage = 1;
    private int perPage = 10;
    private int totalPages = 0;
    private boolean isLoading = false;    //是否正在加载数据
    private TextView balance_tv;
    private CheckBox agreement_cb;
    private TextView agreement_tv;
    private boolean isChecked = false;
    private LinearLayout money_info_ll;
    private LinearLayout input_ll;

    private Button bt_1, bt_2, bt_3, bt_4, bt_5, bt_6, bt_7, bt_8, bt_9, bt_dot, bt_0, bt_00;
    private ImageButton bt_delete, bt_hiden;
    private LinearLayout ll_plus, ll_reduce;
    private String editMoney = "";

    private RelativeLayout empty_rl;
    private Dialog loading;
    private Dialog alertDialog;
    private LinearLayout bank_ll;
    private TextView bank_tv;
    private ImageView bank_iv;
    private Thumbnail thumbnail = null;
    private String selectBankIconCode = "";
    private String bank_icon, bank_name, bank_channel, bank_code;

    private String recharge_lowest_limit = "0";    //充值最小限额

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hrls = new HttpRechargeListService(this, this);
        initView();
//		initKeyborad();
//		loadBalanceData();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        setContentView(R.layout.activity_recharge_list);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("充值");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);

//		money_et = (CustomEditTextLeftIcon) findViewById(R.id.recharge_list_et_money);
//		gray_view = findViewById(R.id.recharge_list_gray);
        keyboard_main = (LinearLayout) findViewById(R.id.keyboard_main);
        bottom_ll = (LinearLayout) findViewById(R.id.recharge_list_ll_bottom);
        bottom_ll.setClickable(true);

        money_info_ll = (LinearLayout) findViewById(R.id.recharge_list_moneyinfo_ll);
        empty_rl = (RelativeLayout) findViewById(R.id.rl_empty);
        input_ll = (LinearLayout) findViewById(R.id.recharge_list_ll_input);

        balance_tv = (TextView) findViewById(R.id.recharge_list_balance_tv);
        agreement_cb = (CheckBox) findViewById(R.id.recharge_list_argeement_cb);
        agreement_tv = (TextView) findViewById(R.id.recharge_list_argeement_tv);
        agreement_cb.setOnCheckedChangeListener(this);
        isChecked = agreement_cb.isChecked();
        //TextView分段点击事件
        agreement_tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence cs = agreement_tv.getText();
        if (cs instanceof Spannable) {
            int end = cs.length();
            Spannable sp = (Spannable) agreement_tv.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(cs);
            style.clearSpans();
            for (URLSpan url : urls) {
                MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
                style.setSpan(myURLSpan, sp.getSpanStart(url),
                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            agreement_tv.setText(style);
        }

        recharge_lv = (ListView) findViewById(R.id.recharge_list_recodelist_lv);
        footerView = LayoutInflater.from(RechargeListActivity.this).inflate(R.layout.loading_item, null);

        bank_ll = (LinearLayout) findViewById(R.id.recharge_list_ll_bank);
        bank_tv = (TextView) findViewById(R.id.recharge_list_tv_bank);
        bank_iv = (ImageView) findViewById(R.id.recharge_list_iv_bank);
        bank_ll.setOnClickListener(this);
        thumbnail = Thumbnail.init(RechargeListActivity.this);

        recharge_lv.setOnScrollListener(this);
        recharge_lv.setOnItemClickListener(this);
        rechargeAdapter = new RechargeListAdapter();
        rechargeList = new ArrayList<RechargeListActivity.RechargeItem>();

        loading = createLoadingDialog(RechargeListActivity.this, "加载中", true);
        alertDialog = createDialog("", "", "", dialogButtonClickListener);

        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        bottom_ll.measure(0, h);
        int height = input_ll.getMeasuredHeight();
        recharge_lv.setPadding(0, 0, 0, height);
    }

    OnClickListener dialogButtonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    };


    @Override
    protected void onResume() {
        currentPage = 1;
        rechargeList.clear();
        loading.show();
        loadData();
        super.onResume();
    }

    //获取余额
    private void loadBalanceData() {
        hrls.loadBalanceData();
        /*post(BALANCE_URL, new RequestParams(), new JsonHttpResponseHandler(RechargeListActivity.this){
            @Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if(null != response){
						String boolen = response.optString("boolen");
						if("1".equals(boolen)){
							JSONObject dataObj = response.getJSONObject("data");
							String amount = dataObj.optString("amount");	//可用余额
							//最低充值金额
							recharge_lowest_limit = !dataObj.isNull("recharge_lowest_limit")?dataObj.optString("recharge_lowest_limit"):"0";
							if(Float.parseFloat(recharge_lowest_limit) > 0){
//								money_et.setHint("起充金额" + recharge_lowest_limit + "元");
							}
							balance_tv.setText(amount);
							
							//默认银行
							String icon = dataObj.optString("myCode");
							String name = dataObj.optString("name");
							String channel = dataObj.optString("channel");
							String code = dataObj.optString("code");
							if(!"".equals(icon) && !"".equals(name) && !"".equals(channel)){
								bank_icon = icon;
								bank_name = name;
								bank_channel = channel;
								bank_code = code;
								selectBankIconCode = bank_icon;
								bank_iv.setImageBitmap(thumbnail.parse(bank_icon));
								bank_tv.setText(bank_name);
								bank_tv.setTextColor(Color.parseColor("#3E3E3E"));
								if(bank_iv.getVisibility() == View.GONE){
									bank_iv.setVisibility(View.VISIBLE);
								}
							}
						}
					}
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				showToast("网络不给力");
			}
		});*/
    }


    private void loadData() {
//		hrls.gainRechargeList(String.valueOf(currentPage),String.valueOf(perPage));
        RequestParams param = new RequestParams();
        param.put("p", String.valueOf(currentPage));
        param.put("perpage", String.valueOf(perPage));
        post(RECHARGE_LIST_URL, param, new JsonHttpResponseHandler(RechargeListActivity.this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                try {
                    if (null != response) {
                        String boolen = response.optString("boolen");
                        if ("1".equals(boolen)) {
                            JSONObject dataObj = response.getJSONObject("data");
                            totalPages = dataObj.getInt("totalPages");
                            String dataStr = dataObj.optString("data");
                            if (!"".equals(dataStr)) {
                                JSONArray dataArray = dataObj.getJSONArray("data");
                                if (null != dataArray && dataArray.length() > 0) {
                                    empty_rl.setVisibility(View.GONE);
                                    recharge_lv.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        RechargeItem item = new RechargeItem();
                                        item.id = dataArray.getJSONObject(i).optString("id");
                                        item.amount = dataArray.getJSONObject(i).optString("amount");
                                        item.out_account_no = dataArray.getJSONObject(i).optString("out_account_no");
                                        item.sub_bank = dataArray.getJSONObject(i).optString("sub_bank");
                                        item.ctime = dataArray.getJSONObject(i).optString("ctime");
                                        item.status = dataArray.getJSONObject(i).optString("status");
                                        rechargeList.add(item);
                                    }
                                    if (currentPage == 1) {
                                        if (totalPages > currentPage) {
                                            recharge_lv.addFooterView(footerView);
                                        }
                                        recharge_lv.setAdapter(rechargeAdapter);
                                    } else {
                                        if (totalPages == currentPage) {
                                            recharge_lv.removeFooterView(footerView);
                                        }
                                        rechargeAdapter.notifyDataSetChanged();
                                    }
                                    currentPage += 1;
                                    isLoading = false;
                                } else {
                                    if (currentPage == 1) {
                                        recharge_lv.setVisibility(View.GONE);
                                        empty_rl.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                if (currentPage == 1) {
                                    recharge_lv.setVisibility(View.GONE);
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
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                showToast("网络不给力");
            }
        });
    }

    /**
     * 充值身份验证（实名认证）
     */
    private void loadRechargeCheck() {
        post(RECHARGE_CHECK_URL, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                try {
                    if (null != response) {
                        String boolen = response.getString("boolen");
                        if ("1".equals(boolen)) {
                            JSONObject dataObj = response.getJSONObject("data");
                            String is_id_auth = dataObj.getString("is_id_auth");
                            if ("1".equals(is_id_auth)) {
                                Intent intent = new Intent(RechargeListActivity.this, MainWebActivity.class);
                                intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/addBank");
                                intent.putExtra("payType", bank_channel);
                                intent.putExtra("bankCode", bank_code);
                                intent.putExtra("bankIcon", bank_icon);
//								intent.putExtra("amount", money_et.getEdtText().toString());
                                startActivityForResult(intent, 20);
                            } else {
                                alertDialog = createDialog("提示", "充值前请先完成实名认证", "确定", dialogButtonClickListener);
                                if (!alertDialog.isShowing()) {
                                    alertDialog.show();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                showToast("网络不给力");
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent(RechargeListActivity.this, RechargeInfoActivity.class);
        intent.putExtra("id", rechargeList.get(arg2).id);
        intent.putExtra("status", rechargeList.get(arg2).status);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                RechargeListActivity.this.finish();
                break;
//		case R.id.recharge_list_gray:
//			if(isKeyboardShowing && !isKeyboardDismiss){
//				bottom_ll.startAnimation(dismissAnim);
//				money_info_ll.startAnimation(moneyDismissAnim);
//			}
//			break;
            case R.id.recharge_list_ll_bank:
                Intent intent = new Intent(RechargeListActivity.this, SelectBankCardActivity.class);
                intent.putExtra("bank_card_id", selectBankIconCode);
                intent.putExtra("intent_flag", 1);
                startActivityForResult(intent, 10);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 11) {
            bank_icon = data.getStringExtra("bank_icon");
            bank_name = data.getStringExtra("bank_name");
            bank_channel = data.getStringExtra("bank_channel");
            bank_code = data.getStringExtra("bank_code");

            selectBankIconCode = bank_icon;

            bank_iv.setImageBitmap(thumbnail.parse(bank_icon));
            bank_tv.setText(bank_name);
            bank_tv.setTextColor(Color.parseColor("#3E3E3E"));
            if (bank_iv.getVisibility() == View.GONE) {
                bank_iv.setVisibility(View.VISIBLE);
            }
        }
        if (requestCode == 20 && resultCode == 21) {
            loading.show();
            rechargeList.clear();
            currentPage = 1;
            totalPages = 0;
            loadData();
            loadBalanceData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    class RechargeItem {
        String id;
        String amount;    //金额
        String out_account_no;    //银行卡号
        String sub_bank;    //银行code
        String ctime;    //时间
        String status;    //状态 1-待处理 2-成功 3-失败
    }


    class ViewHolder {
        //		ImageView bank_icon;
//		TextView bankcard_no;
        TextView money_tv;
        TextView time_tv;
        TextView status_tv;
    }


    class RechargeListAdapter extends BaseAdapter {

        private Thumbnail thumbnail = null;

        private RechargeListAdapter() {
            this.thumbnail = Thumbnail.init(RechargeListActivity.this);
        }

        @Override
        public int getCount() {
            return rechargeList.size();
        }

        @Override
        public Object getItem(int position) {
            return rechargeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RechargeItem rechargeItem = rechargeList.get(position);
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(RechargeListActivity.this).inflate(
                        R.layout.layout_recharge_list_item, null);
//				holder.bank_icon = (ImageView) convertView.findViewById(R.id.rw_list_item_bankicon_iv);
//				holder.bankcard_no = (TextView) convertView.findViewById(R.id.rw_list_item_bankno_tv);
                holder.money_tv = (TextView) convertView.findViewById(R.id.rw_list_item_money_tv);
                holder.time_tv = (TextView) convertView.findViewById(R.id.rw_list_item_time_tv);
                holder.status_tv = (TextView) convertView.findViewById(R.id.rw_list_item_status_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//			holder.bank_icon.setImageBitmap(thumbnail.parse(rechargeItem.sub_bank));
//			if(null != rechargeItem.out_account_no && !"".equals(rechargeItem.out_account_no)){
//				String cardNo = rechargeItem.out_account_no.substring(
//						rechargeItem.out_account_no.length() - 4, rechargeItem.out_account_no.length());
//				holder.bankcard_no.setText("尾号" + cardNo);
//			}else{
//				holder.bankcard_no.setText("");
//			}
            holder.money_tv.setText(rechargeItem.amount);
            holder.time_tv.setText(rechargeItem.ctime);
            if (null != rechargeItem.status && !"".equals(rechargeItem.status)) {
                if ("1".equals(rechargeItem.status)) {
                    holder.status_tv.setText("待处理");
                    holder.status_tv.setTextColor(Color.parseColor("#0B8EFB"));
                } else if ("2".equals(rechargeItem.status)) {
                    holder.status_tv.setText("✓成功");
                    holder.status_tv.setTextColor(Color.parseColor("#3BAD08"));
                } else {
                    holder.status_tv.setText("×失败");
                    holder.status_tv.setTextColor(Color.parseColor("#0B8EFB"));
                }
            } else {
                holder.status_tv.setText("");
            }

            return convertView;
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        //判断是不是最后一个
        if ((firstVisibleItem + visibleItemCount) == totalItemCount &&
                totalPages >= currentPage && !isLoading) {
            isLoading = true;
            loadData();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isKeyboardShowing && !isKeyboardDismiss) {
                bottom_ll.startAnimation(dismissAnim);
                money_info_ll.startAnimation(moneyDismissAnim);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


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

                        if (String.valueOf(editMoney).indexOf(".") != -1 && ((editMoney.length() - editMoney.indexOf(".") - 1) >= 2)) {
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
                //小数点
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
                //00
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
                //删除
                case -3:
                    if ("".equals(editMoney))
                        return;
                    else
                        editMoney = editMoney.substring(0, editMoney.length() - 1);
                    break;
                //隐藏
                case -4:
                    if (isKeyboardShowing && !isKeyboardDismiss) {
                        bottom_ll.startAnimation(dismissAnim);
                        money_info_ll.startAnimation(moneyDismissAnim);
                    }
                    break;
            }
//			money_et.getET().setText(editMoney);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.isChecked = isChecked;
    }

    private class MyURLSpan extends ClickableSpan {
        private String mUrl;

        MyURLSpan(String url) {
            mUrl = url;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#1C75BE"));    //改变颜色
            ds.setUnderlineText(false);    //去除下划线
        }

        @Override
        public void onClick(View widget) {
            if (mUrl.equals("1")) {
                Intent intent = new Intent(RechargeListActivity.this, AgreementActivity.class);
                intent.putExtra("title", "理财时代资金管理服务协议");
                intent.putExtra("url", "/Index/Protocol/view?id=2");
                startActivity(intent);
            }
        }
    }

    @Override
    public void gainRechargeListsuccess(RechargeListJson response) {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    RechargeListData dataObj = response.getData();
                    totalPages = dataObj.getTotalPages();
                    String dataStr = dataObj.getData();
                    if (!"".equals(dataStr)) {
                        List<RechargeList> dataArray = dataObj.getList();
                        if (null != dataArray && dataArray.size() > 0) {
                            empty_rl.setVisibility(View.GONE);
                            recharge_lv.setVisibility(View.VISIBLE);
                            for (int i = 0; i < dataArray.size(); i++) {
                                RechargeItem item = new RechargeItem();
                                item.id = dataArray.get(i).getId();
                                item.amount = dataArray.get(i).getAmount();
                                item.out_account_no = dataArray.get(i).getOut_account_no();
                                item.sub_bank = dataArray.get(i).getSub_bank();
                                item.ctime = dataArray.get(i).getCtime();
                                item.status = dataArray.get(i).getStatus();
                                rechargeList.add(item);
                            }
                            if (currentPage == 1) {
                                if (totalPages > currentPage) {
                                    recharge_lv.addFooterView(footerView);
                                }
                                recharge_lv.setAdapter(rechargeAdapter);
                            } else {
                                if (totalPages == currentPage) {
                                    recharge_lv.removeFooterView(footerView);
                                }
                                rechargeAdapter.notifyDataSetChanged();
                            }
                            currentPage += 1;
                            isLoading = false;
                        } else {
                            if (currentPage == 1) {
                                recharge_lv.setVisibility(View.GONE);
                                empty_rl.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        if (currentPage == 1) {
                            recharge_lv.setVisibility(View.GONE);
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
    public void gainRechargeListfail() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        showToast("网络不给力");
    }

    @Override
    public void gainapplyCashoutsuccess(ApplyCashoutJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    ApplyCashoutData dataObj = response.getData();
                    String amount = dataObj.getAmount();    //可用余额
                    //最低充值金额
                    if (dataObj.getRecharge_lowest_limit() != null) {
                        recharge_lowest_limit = dataObj.getRecharge_lowest_limit();
                    } else {
                        recharge_lowest_limit = "0";
                    }
                    balance_tv.setText(amount);

                    //默认银行
                    String icon = dataObj.getMyCode();
                    String name = dataObj.getName();
                    String channel = dataObj.getChannel();
                    String code = dataObj.getCode();
                    if (!"".equals(icon) && !"".equals(name) && !"".equals(channel)) {
                        bank_icon = icon;
                        bank_name = name;
                        bank_channel = channel;
                        bank_code = code;
                        selectBankIconCode = bank_icon;
                        bank_iv.setImageBitmap(thumbnail.parse(bank_icon));
                        bank_tv.setText(bank_name);
                        bank_tv.setTextColor(Color.parseColor("#3E3E3E"));
                        if (bank_iv.getVisibility() == View.GONE) {
                            bank_iv.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainapplyCashoutfail() {
        showToast("网络不给力");
    }
}
