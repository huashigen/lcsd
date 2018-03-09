package com.lcshidai.lc.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.GainOpenAccountImpl;
import com.lcshidai.lc.impl.account.AccountUserEscrowInfoImp;
import com.lcshidai.lc.model.OpenAccountData;
import com.lcshidai.lc.model.OpenAccountJson;
import com.lcshidai.lc.model.account.UserEscrowInfoData;
import com.lcshidai.lc.model.account.UserEscrowInfoJson;
import com.lcshidai.lc.service.HttpGainOpenAccountService;
import com.lcshidai.lc.service.account.HttpUserEscrowInfoService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.ToastUtil;

/**
 * 存款账户页面
 * Created by RandyZhang on 16/7/6.
 */
public class CunGuanAccountActivity extends TRJActivity implements View.OnClickListener, AccountUserEscrowInfoImp, GainOpenAccountImpl {

    private TextView tvEcwAccount;
    private RelativeLayout rlOpenBankContainer;
    private TextView tvEnableCunGuanAccount;                                        // 开通存管账户
    //    private TextView tvViewCunGuanAccount;                                          // 查看存管账户
    private TextView tvEcwCustomerName;
    private TextView tvCredentialsType;
    private TextView tvCredentialsNumber;
    private TextView tvBankCardNum;
    private TextView tvBelongBank;
    private RelativeLayout rlModifyContainer;
    private ImageView ivModifyPhone;
    private TextView tvLeftPhoneNumber;

    private HttpUserEscrowInfoService httpUserEscrowInfoService = null;

    private HttpGainOpenAccountService httpGainOpenAccountService = null;

    private Dialog loadingDialog = null;

    private boolean isNeedRefresh = false;

    private MessageReceiver mMessageReceiver;
    private String ACTION_INTENT_RECEIVER = "com.zsBank.czfinancial.receive.broadcastReceiver";
    private String openEscrowUrl;                   // wap 存管地址

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cun_guan_account);

        openEscrowUrl = getIntent().getStringExtra("openEscrowUrl");

        httpUserEscrowInfoService = new HttpUserEscrowInfoService(this, this);
        httpGainOpenAccountService = new HttpGainOpenAccountService(this, this);
        initViews();
        //请求数据
        refreshData(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData(isNeedRefresh);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isNeedRefresh = true;
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initViews() {
        ImageButton topBackBtn = (ImageButton) findViewById(R.id.ib_top_bar_back);
        TextView topTitleText = (TextView) findViewById(R.id.tv_top_bar_title);
        tvEcwAccount = (TextView) findViewById(R.id.tv_ecw_account);
        rlOpenBankContainer = (RelativeLayout) findViewById(R.id.rl_open_bank_container);
        tvEnableCunGuanAccount = (TextView) findViewById(R.id.tv_enable_cun_guan_account);
//        tvViewCunGuanAccount = (TextView) findViewById(R.id.tv_view_cun_guan_account);
        tvEcwCustomerName = (TextView) findViewById(R.id.tv_ecw_customer_name);
        tvCredentialsType = (TextView) findViewById(R.id.tv_credentials_type);
        tvCredentialsNumber = (TextView) findViewById(R.id.tv_credentials_number);
        tvBankCardNum = (TextView) findViewById(R.id.tv_bank_card_num);
        tvBelongBank = (TextView) findViewById(R.id.tv_belong_bank);
        rlModifyContainer = (RelativeLayout) findViewById(R.id.rl_modify_container);
        ivModifyPhone = (ImageView) findViewById(R.id.iv_modify_phone);
        tvLeftPhoneNumber = (TextView) findViewById(R.id.tv_left_phone_number);

        topBackBtn.setOnClickListener(this);
        topTitleText.setText("银行存管");

        tvEnableCunGuanAccount.setOnClickListener(this);
//        tvViewCunGuanAccount.setOnClickListener(this);
    }

    /**
     * 是否显示开通存管账户按钮
     *
     * @param isShowEnableCunGuanBtn 是否显示
     */
    private void showEnableCunGuanAccountBtn(boolean isShowEnableCunGuanBtn) {
        if (isShowEnableCunGuanBtn) {
            tvEnableCunGuanAccount.setVisibility(View.VISIBLE);
//            tvViewCunGuanAccount.setVisibility(View.GONE);
            rlOpenBankContainer.setVisibility(View.GONE);
        } else {
            tvEnableCunGuanAccount.setVisibility(View.GONE);
//            tvViewCunGuanAccount.setVisibility(View.GONE);
            rlOpenBankContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setViews(UserEscrowInfoData data) {
        if (null != data) {// 说明有数据，已开通
            // check if the data bind is right
            GoLoginUtil.saveBankLeftPhone(data.getEcw_mobile(), this);
            GoLoginUtil.saveCunGuanFlag(1, CunGuanAccountActivity.this);// 已开通
            tvEcwAccount.setText(data.getEcard_no());
            tvEcwAccount.setTextColor(getResources().getColor(R.color.black));
            tvEcwCustomerName.setText(data.getAccount_name());
            tvEcwCustomerName.setTextColor(getResources().getColor(R.color.black));
            if (!CommonUtil.isNullOrEmpty(data.getCert_type())) {
                int ecwCertType = Integer.parseInt(data.getCert_type());
                switch (ecwCertType) {// 证件类型
                    case 1:
                        tvCredentialsType.setText("身份证");
                        break;
                    case 2:
                        tvCredentialsType.setText("户口簿");
                        break;
                    case 3:
                        tvCredentialsType.setText("军人证");
                        break;
                    case 4:
                        tvCredentialsType.setText("武警证");
                        break;
                    case 5:
                        tvCredentialsType.setText("回乡证");
                        break;
                    case 6:
                        tvCredentialsType.setText("国外居留证");
                        break;
                    case 7:
                        tvCredentialsType.setText("境外护照");
                        break;
                    case 9:
                        tvCredentialsType.setText("港澳台通行证");
                        break;
                }
            }
            tvCredentialsType.setTextColor(getResources().getColor(R.color.black));
            showEnableCunGuanAccountBtn(false);// 不用显示开通按钮
            tvCredentialsNumber.setText(data.getCert_no());//证件号
            tvCredentialsNumber.setTextColor(getResources().getColor(R.color.black));
            tvBankCardNum.setText(data.getBankcard_no());
            tvBankCardNum.setTextColor(getResources().getColor(R.color.black));
            tvBelongBank.setText(data.getBankname());
            tvBelongBank.setTextColor(getResources().getColor(R.color.black));
            tvLeftPhoneNumber.setText(data.getEcw_mobile());
            tvLeftPhoneNumber.setTextColor(getResources().getColor(R.color.black));
            rlModifyContainer.setVisibility(View.VISIBLE);
            ivModifyPhone.setVisibility(View.VISIBLE);
            rlModifyContainer.setOnClickListener(this);
        } else {
            showEnableCunGuanAccountBtn(true);// 需要显示开通按钮
            tvEcwAccount.setText("待开通");
            tvEcwAccount.setTextColor(getResources().getColor(R.color.light_gray));
            tvBankCardNum.setText("待开通");
            tvBankCardNum.setTextColor(getResources().getColor(R.color.light_gray));
            tvEcwCustomerName.setText("待开通");
            tvEcwCustomerName.setTextColor(getResources().getColor(R.color.light_gray));
            tvCredentialsType.setText("待开通");
            tvCredentialsType.setTextColor(getResources().getColor(R.color.light_gray));
            tvCredentialsNumber.setText("待开通");
            tvCredentialsNumber.setTextColor(getResources().getColor(R.color.light_gray));
            tvBelongBank.setText("待开通");
            tvBelongBank.setTextColor(getResources().getColor(R.color.light_gray));
            tvLeftPhoneNumber.setText("待开通");
            tvLeftPhoneNumber.setTextColor(getResources().getColor(R.color.light_gray));
            rlModifyContainer.setVisibility(View.VISIBLE);
            ivModifyPhone.setVisibility(View.GONE);
            tvLeftPhoneNumber.setText("待开通");
            tvLeftPhoneNumber.setOnClickListener(null);
            GoLoginUtil.saveCunGuanFlag(2, CunGuanAccountActivity.this);//未开通
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterMessageReceiver();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;
            case R.id.tv_enable_cun_guan_account:
                if (CommonUtil.isNullOrEmpty(openEscrowUrl)) {
                    // 获取开户信息
                    httpGainOpenAccountService.getOpenAccountInfo();
                } else {
                    Intent intent = new Intent(mContext, MainWebActivity.class);
                    intent.putExtra("web_url", openEscrowUrl);
                    intent.putExtra("need_header", 0);
                    intent.putExtra("title", "开通存管");
                    startActivity(intent);
                }
                break;
            case R.id.rl_modify_container:
                isNeedRefresh = true;
                // 修改预留手机号
                Intent intent = new Intent(this, ModifyPreLeftPhoneNumberActivity.class);
                intent.putExtra(ModifyPreLeftPhoneNumberActivity.ENTRANCE, CunGuanAccountActivity.class.getSimpleName());
                startActivity(intent);
                break;
            default:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * 动态注册广播
     */
    private void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_RECEIVER);
        registerReceiver(mMessageReceiver, filter);
    }

    /**
     * 取消广播注册
     */
    private void unregisterMessageReceiver() {
        if (mMessageReceiver != null) {
            unregisterReceiver(mMessageReceiver);
        }
    }

    @Override
    public void gainUserEscrowInfoSuccess(UserEscrowInfoJson response) {
        if (response != null) {
            // if loading is shown, hide it
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            if (response.getBoolen().equals("1")) {
                UserEscrowInfoData data = response.getData();
                setViews(data);
            } else {
                String message = response.getMessage();
                ToastUtil.showToast(this, message);
            }
        }
    }

    @Override
    public void gainUserEscrowInfoFail() {
// if loading is shown, hide it
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void getOpenAccountInfoSuccess(OpenAccountJson response) {
        if (response != null)
            Log.i(tag, response.getMessage());

        if (response != null) {
            // if loading is shown, hide it
            if (response.getBoolen().equals("1")) {
                // 获取存管开户信息成功
                isNeedRefresh = true;
                OpenAccountData openAccountData = response.getData();
                if (openAccountData != null) {
                    CommonUtil.openAccount(CunGuanAccountActivity.this, openAccountData);
                    registerMessageReceiver();
                }
            } else {
                String message = response.getMessage();
                ToastUtil.showToast(this, message);
            }
        }
    }

    @Override
    public void getOpenAccountInfoFailed() {

    }

    private void refreshData(boolean isNeedRefresh) {
        if (isNeedRefresh) {
            httpUserEscrowInfoService.gainUserEscrowInfo();
            if (loadingDialog == null) {
                loadingDialog = createLoadingDialog(this, "正在加载", true);
                loadingDialog.show();
            } else {
                loadingDialog.show();
            }
        }
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_INTENT_RECEIVER)) {
                refreshData(isNeedRefresh);
            }
        }
    }
}