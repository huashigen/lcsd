package com.lcshidai.lc.ui.account;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.WithdrawalsCancelImpl;
import com.lcshidai.lc.impl.account.WithdrawalsInfoImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.WithdrawalsInfoData;
import com.lcshidai.lc.model.account.WithdrawalsInfoJson;
import com.lcshidai.lc.service.account.HttpWithdrawalsCancelService;
import com.lcshidai.lc.service.account.HttpWithdrawalsInfoService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.widget.ppwindow.DialogPopupWindow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 提现确认
 *
 * @author 000853
 */
public class WithdrawalsInfoActivity extends TRJActivity implements WithdrawalsInfoImpl, WithdrawalsCancelImpl, OnClickListener {
    HttpWithdrawalsInfoService hwis;
    HttpWithdrawalsCancelService hwcs;

    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    ImageButton mBackBtn;
    private Context mContext;

    private LinearLayout chulitime_ll, chulibeckup_ll;
    private View chulitime_line, chulibeckup_line;
    private TextView time_tv, status_tv, tixianmoney_tv, diyongquanmoney_tv,
            fuwufeimoney_tv, shouxufeimoney_tv, bank_tv, chulitime_tv, chulibeckup_tv;

    private Button submitBT;

    private String id = "";
    private String status = "";

    private DialogPopupWindow dialogPopupWindow;
    private View mainView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Intent args = getIntent();
        if (args != null) {
            id = args.getStringExtra("id");
            status = args.getStringExtra("status");
        }
        hwis = new HttpWithdrawalsInfoService(this, this);
        hwcs = new HttpWithdrawalsCancelService(this, this);
        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_withdrawals_info);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("提现");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);

        chulitime_ll = (LinearLayout) findViewById(R.id.withdrawals_info_ll_chulitime);
        chulibeckup_ll = (LinearLayout) findViewById(R.id.withdrawals_info_ll_chulibeckup);
        chulitime_line = findViewById(R.id.withdrawals_info_line_chulitime);
        chulibeckup_line = findViewById(R.id.withdrawals_info_line_chulibeckup);

        time_tv = (TextView) findViewById(R.id.withdrawals_info_vlaue_time);
        status_tv = (TextView) findViewById(R.id.withdrawals_info_vlaue_status);
        tixianmoney_tv = (TextView) findViewById(R.id.withdrawals_info_vlaue_tixianmoney);
        diyongquanmoney_tv = (TextView) findViewById(R.id.withdrawals_info_vlaue_diyongquanmoney);
        fuwufeimoney_tv = (TextView) findViewById(R.id.withdrawals_info_vlaue_fuwufeimoney);
        shouxufeimoney_tv = (TextView) findViewById(R.id.withdrawals_info_vlaue_shouxufeimoney);
        bank_tv = (TextView) findViewById(R.id.withdrawals_info_vlaue_bank);
        chulitime_tv = (TextView) findViewById(R.id.withdrawals_info_vlaue_chulitime);
        chulibeckup_tv = (TextView) findViewById(R.id.withdrawals_info_vlaue_chulibeckup);

        submitBT = (Button) findViewById(R.id.withdrawals_info_button);
        submitBT.setOnClickListener(this);

        //处理中
        if ("1".equals(status)) {
            chulitime_ll.setVisibility(View.GONE);
            chulibeckup_ll.setVisibility(View.GONE);
            chulitime_line.setVisibility(View.GONE);
            chulibeckup_line.setVisibility(View.GONE);
            submitBT.setVisibility(View.VISIBLE);
            submitBT.setText("取消");
        }
        //成功
        else if ("2".equals(status)) {
            chulibeckup_ll.setVisibility(View.GONE);
            chulibeckup_line.setVisibility(View.GONE);
            submitBT.setVisibility(View.GONE);
            chulitime_ll.setVisibility(View.VISIBLE);
            chulitime_line.setVisibility(View.VISIBLE);
        }
        //失败
        else if ("3".equals(status)) {
            chulitime_ll.setVisibility(View.VISIBLE);
            chulibeckup_ll.setVisibility(View.VISIBLE);
            chulitime_line.setVisibility(View.VISIBLE);
            chulibeckup_line.setVisibility(View.VISIBLE);
            submitBT.setVisibility(View.GONE);
            submitBT.setText("重新申请");
        }
        //取消
        else {
            chulitime_ll.setVisibility(View.GONE);
            chulibeckup_ll.setVisibility(View.GONE);
            chulitime_line.setVisibility(View.GONE);
            chulibeckup_line.setVisibility(View.GONE);
            submitBT.setVisibility(View.GONE);
        }
        loadData();
        mainView = findViewById(R.id.ll_main);
        dialogPopupWindow = new DialogPopupWindow(this, mainView, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                WithdrawalsInfoActivity.this.finish();
                break;
            case R.id.withdrawals_info_button:
                if ("1".equals(status)) {
                    cancelWithdrawals();
                }
                break;
        }
    }


    private void loadData() {
        hwis.gainWithdrawalsInfo(id);
        /*RequestParams params = new RequestParams();
		params.put("id", id);
		post(WITHDRAWALS_INFO_URL, params, new JsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
				super.onSuccess(statusCode,headers, response);
				try{
					if(null != response){
						String boolen = response.getString("boolen");
						if("1".equals(boolen)){
							JSONObject dataObj = response.getJSONObject("data");
							String ctime = dataObj.getString("ctime");	//申请时间
							String status = dataObj.getString("status");	//状态 1-处理中 2-成功 3-失败 4-取消
							String amount = dataObj.getString("amount");	//提现金额（元）
							String free_money = dataObj.getString("free_money");	//资金服务费抵用券
							String fuwu_fee = dataObj.getString("fuwu_fee");	//资金服务费
							String tixian_fee = dataObj.getString("tixian_fee");	//提现手续费
							String free_tixian_times = dataObj.getString("free_tixian_times");	// 是否使用免收手续费
							String bank_name = dataObj.getString("bank_name");	//银行名称
							String sub_bank = dataObj.getString("sub_bank");	//支行名称
							String out_account_no = dataObj.getString("out_account_no");	//银行卡号
							String prj_recharge = dataObj.isNull("prj_recharge")?"":dataObj.getString("prj_recharge");	//如果有该项 请拼在资金服务费前面
							String deal_bak = dataObj.isNull("deal_bak")?"":dataObj.getString("deal_bak");	//处理备注（失败时才有）
							String deal_time = dataObj.isNull("deal_time")?"":dataObj.getString("deal_time");	//处理时间（失败时才有）
							
							time_tv.setText(setTextToTextView(ctime));
							if("1".equals(status)){
								status_tv.setText("处理中");
							}else if("2".equals(status)){
								status_tv.setText("成功");
							}else if("3".equals(status)){
								status_tv.setText("失败");
							}else if("4".equals(status)){
								status_tv.setText("取消");
							}
							tixianmoney_tv.setText(setTextToTextView(amount));
							diyongquanmoney_tv.setText(setTextToTextView(free_money));
							fuwufeimoney_tv.setText(setTextToTextView(fuwu_fee + prj_recharge));
							if("1".equals(free_tixian_times)){
								shouxufeimoney_tv.setText(setTextToTextView(tixian_fee) + "(使用免收手续费一次)");
							}else{
								shouxufeimoney_tv.setText(setTextToTextView(tixian_fee));
							}
							bank_tv.setText(setTextToTextView(bank_name + "\n" + sub_bank + "\n" + out_account_no));
							chulitime_tv.setText(setTextToTextView(deal_time));
							chulibeckup_tv.setText(setTextToTextView(deal_bak));
						}
					}
				}
				catch(Exception e){
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

    private String setTextToTextView(String str) {
        if (null == str || "".equals(str)) {
            return "--";
        }
        return str;
    }

    private void cancelWithdrawals() {
        hwcs.gainWithdrawalsCancel(id);
    }

    @Override
    public void gainWithdrawalsInfosuccess(WithdrawalsInfoJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    WithdrawalsInfoData dataObj = response.getData();
                    String ctime = dataObj.getCtime();    //申请时间
                    String status = dataObj.getStatus();    //状态 1-处理中 2-成功 3-失败 4-取消
                    String amount = dataObj.getAmount();    //提现金额（元）
                    String free_money = dataObj.getFree_money();    //资金服务费抵用券
                    String fuwu_fee = dataObj.getFuwu_fee();    //资金服务费
                    String tixian_fee = dataObj.getTixian_fee();    //提现手续费
                    String free_tixian_times = dataObj.getFree_tixian_times();    // 是否使用免收手续费
                    String bank_name = dataObj.getBank_name();    //银行名称
                    String sub_bank = dataObj.getSub_bank();    //支行名称
                    String out_account_no = dataObj.getOut_account_no();    //银行卡号
                    String prj_recharge = resolveString(dataObj.getPrj_recharge(), "", "", "");    //如果有该项 请拼在资金服务费前面
                    String deal_bak = resolveString(dataObj.getDeal_bak(), "", "", "");    //处理备注（失败时才有）
                    String deal_time = resolveString(dataObj.getDeal_time(), "", "", "");    //处理时间（失败时才有）

                    time_tv.setText(setTextToTextView(ctime));
                    if ("1".equals(status)) {
                        status_tv.setText("处理中");
                    } else if ("2".equals(status)) {
                        status_tv.setText("成功");
                    } else if ("3".equals(status)) {
                        status_tv.setText("失败");
                    } else if ("4".equals(status)) {
                        status_tv.setText("取消");
                    }
                    tixianmoney_tv.setText(setTextToTextView(amount));
                    diyongquanmoney_tv.setText(setTextToTextView(free_money));
                    fuwufeimoney_tv.setText(setTextToTextView(fuwu_fee + prj_recharge));
                    if ("1".equals(free_tixian_times)) {
                        shouxufeimoney_tv.setText(setTextToTextView(tixian_fee) + "(使用免收手续费一次)");
                    } else {
                        shouxufeimoney_tv.setText(setTextToTextView(tixian_fee));
                    }
                    bank_tv.setText(setTextToTextView(bank_name + "\n" + sub_bank + "\n" + out_account_no));
                    chulitime_tv.setText(setTextToTextView(deal_time));
                    chulibeckup_tv.setText(setTextToTextView(deal_bak));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainWithdrawalsInfofail() {
        showToast("网络不给力");
    }

    @Override
    public void gainWithdrawalsCancelsuccess(BaseJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    setResult(21);
                    finish();
                } else {
                    dialogPopupWindow.showWithMessage(response.getMessage(), "1");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainWithdrawalsCancelfail() {
        showToast("网络不给力");
    }
}
