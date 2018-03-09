package com.lcshidai.lc.ui.account;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.entity.BankCardInfo;
import com.lcshidai.lc.impl.PIPWCallBack;
import com.lcshidai.lc.impl.account.WithdrawalsListImpl;
import com.lcshidai.lc.model.account.WithdrawalsListData;
import com.lcshidai.lc.model.account.WithdrawalsListJson;
import com.lcshidai.lc.model.account.WithdrawalsRecord;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckData;
import com.lcshidai.lc.service.account.HttpWithdrawalsListService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.Thumbnail;
import com.lcshidai.lc.widget.ppwindow.DialogPopupWindow;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow;

/**
 * 提现列表页
 *
 * @author 000853
 */
public class WithdrawalsListActivity extends TRJActivity implements WithdrawalsListImpl, OnClickListener,
        OnItemClickListener, OnScrollListener, PIPWCallBack, PayPasswordPopupWindow.DismissListener {
    HttpWithdrawalsListService hwls;

    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    ImageButton mBackBtn;
    private Context mContext;

    private PayPasswordPopupWindow payWindow;

    //	private CustomEditTextLeftIcon money_et;
    private LinearLayout keyboard_main, bottom_ll;
    private View gray_view;
    private TranslateAnimation showAnim, dismissAnim, moneyDismissAnim, moneyShowAnim;
    private boolean isKeyboardShowing = false;
    private boolean isKeyboardDismiss = true;

    private List<WithdrawalsItem> withdrawalsItemList;
    private BankListAdapter bankListAdapter;
    private View footerView;
    private int currentPage = 1;
    private int perPage = 10;
    private int totalPages = 0;
    private boolean isLoading = false;    //是否正在加载数据
    private ListView withdrawals_lv;

    private float float_cashoutwable_amount = 0;

    private LinearLayout free_ll, free_ll_value;
    private TextView balance_tv, diyongquan_tv, miantixianfei_tv, diyongquan_tv_vlaue, miantixianfei_tv_value;
    private LinearLayout money_info_ll, input_ll;
    private TextView money_alert_tv;

    private Button bt_1, bt_2, bt_3, bt_4, bt_5, bt_6, bt_7, bt_8, bt_9, bt_dot, bt_0, bt_00;
    private ImageButton bt_delete, bt_hiden;
    private LinearLayout ll_plus, ll_reduce;
    private String editMoney = "";

    private RelativeLayout empty_rl;
    private Dialog alertDialog;
    private Dialog paypwdDialog;
    private Dialog loading;
    private DialogPopupWindow dialogPopupWindow;
    private View mainView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hwls = new HttpWithdrawalsListService(this, this);
        initView();
        initKeyborad();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_withdrawals_list);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("提现");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);

//		money_et = (CustomEditTextLeftIcon) findViewById(R.id.withdrawals_list_et_money);
        gray_view = findViewById(R.id.withdrawals_list_gray);
        keyboard_main = (LinearLayout) findViewById(R.id.keyboard_main);
        bottom_ll = (LinearLayout) findViewById(R.id.withdrawals_list_ll_bottom);
        bottom_ll.setClickable(true);
        balance_tv = (TextView) findViewById(R.id.withdrawals_list_balance_tv);
        diyongquan_tv = (TextView) findViewById(R.id.withdrawals_list_tv_diyongquan);
        miantixianfei_tv = (TextView) findViewById(R.id.withdrawals_list_tv_miantixianfei);
        diyongquan_tv_vlaue = (TextView) findViewById(R.id.withdrawals_list_tv_diyongquan_value);
        miantixianfei_tv_value = (TextView) findViewById(R.id.withdrawals_list_tv_miantixianfei_vlaue);
        free_ll = (LinearLayout) findViewById(R.id.withdrawals_list_ll_free);
        free_ll_value = (LinearLayout) findViewById(R.id.withdrawals_list_ll_free_value);
        money_info_ll = (LinearLayout) findViewById(R.id.withdrawals_list_ll_moneyinfo);
        input_ll = (LinearLayout) findViewById(R.id.withdrawals_list_ll_input);
        money_alert_tv = (TextView) findViewById(R.id.withdrawals_list_tv_moneyalert);

        footerView = LayoutInflater.from(mContext).inflate(R.layout.loading_item, null);
        withdrawals_lv = (ListView) findViewById(R.id.withdrawals_list_recodelist_lv);
        withdrawals_lv.setOnScrollListener(this);
        withdrawals_lv.setOnItemClickListener(this);
        withdrawalsItemList = new ArrayList<WithdrawalsListActivity.WithdrawalsItem>();
        bankListAdapter = new BankListAdapter();

        empty_rl = (RelativeLayout) findViewById(R.id.rl_empty);
        loading = createLoadingDialog(mContext, "加载中", true);
        alertDialog = createDialog("提示", "", "确定", dialogButtonListener);
        paypwdDialog = createDialog("您未设置手机支付密码", "设置", "取消", new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (paypwdDialog.isShowing()) {
                            paypwdDialog.dismiss();
                        }
                        Intent intent = new Intent(mContext, UserPayPwdFirstSetActivity.class);
                        intent.putExtra("intent_from_withdrawals", 1);
                        startActivity(intent);
                    }
                },
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (paypwdDialog.isShowing()) {
                            paypwdDialog.dismiss();
                        }
                    }
                });

        loadData();
        loading.show();

//		money_et.getET().clearFocus();
//		money_et.getET().setInputType(InputType.TYPE_NULL); 
//		money_et.setHint("提现金额");
//		money_et.setOnClickListener(this);
        gray_view.setOnClickListener(this);

        payWindow = new PayPasswordPopupWindow(WithdrawalsListActivity.this, this);
        payWindow.loadBankData();
        payWindow.setDismissListener(this);
        mainView = findViewById(R.id.ll_main);
        dialogPopupWindow = new DialogPopupWindow(this, mainView, null);
//		money_et.getET().setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(!isKeyboardShowing && isKeyboardDismiss){
//					isKeyboardShowing = true;
//					bottom_ll.startAnimation(showAnim);
//					money_info_ll.startAnimation(moneyShowAnim);
//				}
//			}
//		});

//		money_et.getDeleteBT().setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				editMoney = "";
//				money_et.getET().setText("");
//			}
//		});

        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//		keyboard_main.measure(0, h);
//		final int height = keyboard_main.getMeasuredHeight();
//		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bottom_ll.getLayoutParams();
//		lp.setMargins(0, 0, 0, -height);
//		bottom_ll.setLayoutParams(lp);

        input_ll.measure(0, h);
        int inputHeight = input_ll.getMeasuredHeight();
        withdrawals_lv.setPadding(0, 0, 0, inputHeight);

//		showAnim = new TranslateAnimation(0, 0, 0, -height);
//		showAnim.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				gray_view.setVisibility(View.VISIBLE);
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bottom_ll.getLayoutParams();
//				lp.setMargins(0, 0, 0, 0);
//				bottom_ll.setLayoutParams(lp);
//				bottom_ll.clearAnimation();
//				isKeyboardDismiss = false;
//			}
//		});
//		showAnim.setDuration(500);
//		
//		dismissAnim = new TranslateAnimation(0, 0, 0, height);
//		dismissAnim.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				isKeyboardDismiss = true;
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bottom_ll.getLayoutParams();
//				lp.setMargins(0, 0, 0, -height);
//				bottom_ll.setLayoutParams(lp);
//				gray_view.setVisibility(View.GONE);
//				bottom_ll.clearAnimation();
//				isKeyboardShowing = false;
//			}
//		});
//		dismissAnim.setDuration(500);
//
//		money_info_ll.measure(0, h);
//		int moneyHeight = money_info_ll.getMeasuredHeight();
//		moneyShowAnim = new TranslateAnimation(0, 0, moneyHeight, 0);
//		moneyShowAnim.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				money_info_ll.setVisibility(View.VISIBLE);
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				money_info_ll.clearAnimation();
//			}
//		});
//		moneyShowAnim.setDuration(300);
//		
//		moneyDismissAnim = new TranslateAnimation(0, 0, 0, moneyHeight);
//		moneyDismissAnim.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				money_info_ll.setVisibility(View.GONE);
//				money_info_ll.clearAnimation();
//			}
//		});
//		moneyDismissAnim.setDuration(300);
    }

    OnClickListener dialogButtonListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    };

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                WithdrawalsListActivity.this.finish();
                break;
            case R.id.withdrawals_list_gray:
                if (isKeyboardShowing && !isKeyboardDismiss) {
                    bottom_ll.startAnimation(dismissAnim);
                    money_info_ll.startAnimation(moneyDismissAnim);
                }
                break;
        }
    }


    private class BankListAdapter extends BaseAdapter {

        private Thumbnail thumbnail = null;

        private BankListAdapter() {
            this.thumbnail = Thumbnail.init(mContext);
        }

        @Override
        public int getCount() {
            return withdrawalsItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return withdrawalsItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            WithdrawalsItem item = (WithdrawalsItem) getItem(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_withdrawals_list_item, null);
                holder.bank_icon = (ImageView) convertView.findViewById(R.id.rw_list_item_bankicon_iv);
                holder.bankcard_no = (TextView) convertView.findViewById(R.id.rw_list_item_bankno_tv);
                holder.money_tv = (TextView) convertView.findViewById(R.id.rw_list_item_money_tv);
                holder.time_tv = (TextView) convertView.findViewById(R.id.rw_list_item_time_tv);
                holder.status_tv = (TextView) convertView.findViewById(R.id.rw_list_item_status_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.bank_icon.setImageBitmap(thumbnail.parse(item.bank));
            if (null != item.out_account_no && !"".equals(item.out_account_no)) {
                String cardNo = item.out_account_no.substring(
                        item.out_account_no.length() - 4, item.out_account_no.length());
                holder.bankcard_no.setText("尾号" + cardNo);
            } else {
                holder.bankcard_no.setText("");
            }
            holder.money_tv.setText(item.amount);
            holder.time_tv.setText(item.ctime);
            if (null != item.status && !"".equals(item.status)) {
                if ("1".equals(item.status)) {
                    holder.status_tv.setText("待处理");
                    holder.status_tv.setTextColor(Color.parseColor("#0B8EFB"));
                } else if ("2".equals(item.status)) {
                    holder.status_tv.setText("✓成功");
                    holder.status_tv.setTextColor(Color.parseColor("#3BAD08"));
                } else if ("3".equals(item.status)) {
                    holder.status_tv.setText("×失败");
                    holder.status_tv.setTextColor(Color.parseColor("#0B8EFB"));
                } else {
                    holder.status_tv.setText("取消");
                    holder.status_tv.setTextColor(Color.parseColor("#0B8EFB"));
                }
            } else {
                holder.status_tv.setText("");
            }
            return convertView;
        }

    }


    private class ViewHolder {
        ImageView bank_icon;
        TextView bankcard_no;
        TextView money_tv;
        TextView time_tv;
        TextView status_tv;
    }


    private class WithdrawalsItem {
        String id;
        String amount;    //金额
        String out_account_no;    //银行卡号
        String bank;    //银行code
        String ctime;    //时间
        String status;    //状态 1-处理中 2-成功 3-失败 4-取消
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent(mContext, WithdrawalsInfoActivity.class);
        intent.putExtra("id", withdrawalsItemList.get(arg2).id);
        intent.putExtra("status", withdrawalsItemList.get(arg2).status);
        startActivityForResult(intent, 20);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        try {
            if (1 == intent.getIntExtra("set_pay_pwd", 0)) {
                Intent withdrawals_intent = new Intent(mContext, WithdrawalsActivity.class);
                startActivityForResult(withdrawals_intent, 7);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 20 && resultCode == 21) {
            dialogPopupWindow.showWithMessage("取消成功", "2");
            if (!loading.isShowing()) loading.show();
            currentPage = 1;
            withdrawalsItemList.clear();
            loadData();
        }
        if (requestCode == 10 && resultCode == 11) {
            payWindow.refreshBankCard((BankCardInfo) data.getParcelableExtra("select_bank_card_info"));
        }

        //提现成功后回来列表刷新
        if (requestCode == 7 && resultCode == 8) {
            if (!loading.isShowing()) loading.show();
            currentPage = 1;
            withdrawalsItemList.clear();
            loadData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadData() {
        hwls.gainWithdrawalsList(currentPage, perPage);
        /*RequestParams params = new RequestParams();
        params.put("p", String.valueOf(currentPage));
		params.put("perpage", String.valueOf(perPage));
		post(WITHDRAWALS_LIST_URL, params, new JsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
				super.onSuccess(statusCode,headers, response);
				if(loading.isShowing()){
					loading.dismiss();
				}
				try{
					if(null != response){
						String boolen = response.getString("boolen");
						if("1".equals(boolen)){
							JSONObject dataObj = response.getJSONObject("data");
							totalPages = dataObj.getInt("totalPages");
							String dataStr = dataObj.getString("data");
							if(!"".equals(dataStr)){
								JSONArray dataArray = dataObj.getJSONArray("data");
								if(null != dataArray && dataArray.length() > 0){
									empty_rl.setVisibility(View.GONE);
									withdrawals_lv.setVisibility(View.VISIBLE);
									for(int i=0; i<dataArray.length(); i++){
										WithdrawalsItem item = new WithdrawalsItem();
										item.id = dataArray.getJSONObject(i).getString("id");
										item.amount = dataArray.getJSONObject(i).getString("amount");
										item.out_account_no = dataArray.getJSONObject(i).getString("out_account_no");
										item.bank = dataArray.getJSONObject(i).getString("bank");
										item.ctime = dataArray.getJSONObject(i).getString("ctime");
										item.status = dataArray.getJSONObject(i).getString("status");
										withdrawalsItemList.add(item);
									}
									if(currentPage == 1){
										if(totalPages > currentPage){
											withdrawals_lv.addFooterView(footerView);
										}
										withdrawals_lv.setAdapter(bankListAdapter);
									}else{
										if(totalPages == currentPage){
											withdrawals_lv.removeFooterView(footerView);
										}
										bankListAdapter.notifyDataSetChanged();
									}
									currentPage += 1;
									isLoading = false;
								}else{
									if(currentPage == 1){
										withdrawals_lv.setVisibility(View.GONE);
										empty_rl.setVisibility(View.VISIBLE);
									}
								}
							}else{
								if(currentPage == 1){
									withdrawals_lv.setVisibility(View.GONE);
									empty_rl.setVisibility(View.VISIBLE);
								}
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if(loading.isShowing()){
					loading.dismiss();
				}
				showToast("网络不给力");
			}
		});*/
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

                        if (String.valueOf(editMoney).contains(".") && ((editMoney.length() - editMoney.indexOf(".") - 1) >= 2)) {
                            return;
                        }
                        editMoney = editMoney + String.valueOf(flag);
                    }
                    break;
                case 1000:
                    if ("".equals(editMoney) || "0".equals(editMoney)) {
                        editMoney = "1000";
                    } else {
                        if (editMoney.contains(".")) {
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
                        if (editMoney.contains(".")) {
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
                    if (!String.valueOf(editMoney).contains(".")) {
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
        }

    }


    @Override
    public void callPayCheckBack(boolean success) {

    }

    @Override
    public void onDismiss() {
        currentPage = 1;
        withdrawalsItemList.clear();
        loadData();
    }

    @Override
    public void doInvestSuccess(boolean success) {

    }

    @Override
    public void gainWithdrawalsListsuccess(WithdrawalsListJson response) {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    WithdrawalsListData dataObj = response.getData();
                    totalPages = dataObj.getTotalPages();
//					String dataStr = dataObj.getData();
                    List<WithdrawalsRecord> dataArray = dataObj.getList();
                    if (dataArray != null) {
                        if (dataArray.size() > 0) {
                            empty_rl.setVisibility(View.GONE);
                            withdrawals_lv.setVisibility(View.VISIBLE);
                            for (int i = 0; i < dataArray.size(); i++) {
                                WithdrawalsItem item = new WithdrawalsItem();
                                item.id = dataArray.get(i).getId();
                                item.amount = dataArray.get(i).getAmount();
                                item.out_account_no = dataArray.get(i).getOut_account_no();
                                item.bank = dataArray.get(i).getBank();
                                item.ctime = dataArray.get(i).getCtime();
                                item.status = dataArray.get(i).getStatus();
                                withdrawalsItemList.add(item);
                            }
                            if (currentPage == 1) {
                                if (totalPages > currentPage) {
                                    withdrawals_lv.addFooterView(footerView);
                                }
                                withdrawals_lv.setAdapter(bankListAdapter);
                            } else {
                                if (totalPages == currentPage) {
                                    withdrawals_lv.removeFooterView(footerView);
                                }
                                bankListAdapter.notifyDataSetChanged();
                            }
                            currentPage += 1;
                            isLoading = false;
                        } else {
                            if (currentPage == 1) {
                                withdrawals_lv.setVisibility(View.GONE);
                                empty_rl.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        if (currentPage == 1) {
                            withdrawals_lv.setVisibility(View.GONE);
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
    public void gainWithdrawalsListfail() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        showToast("网络不给力");
    }

    @Override
    public void doInvestCheckSuccess(boolean success, FinanceInvestPBuyCheckData model) {

    }
}
