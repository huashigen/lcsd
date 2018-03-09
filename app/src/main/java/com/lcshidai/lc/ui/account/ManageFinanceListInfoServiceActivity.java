package com.lcshidai.lc.ui.account;

import android.os.Bundle;

import com.lcshidai.lc.impl.account.ManageFinanceListInfoImpl;
import com.lcshidai.lc.model.account.ManageFinanceListData;
import com.lcshidai.lc.model.account.ManageFinanceListJson;
import com.lcshidai.lc.service.account.HttpManageFinanceListInfoService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.ToastUtil;

public abstract class ManageFinanceListInfoServiceActivity extends TRJActivity implements ManageFinanceListInfoImpl {

    protected HttpManageFinanceListInfoService hmflis;

    protected ManageFinanceListData jobj;

    public boolean isTransfer;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hmflis = new HttpManageFinanceListInfoService(this, this);
    }

    @Override
    public void gainFundListsuccess(ManageFinanceListJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    jobj = response.getData();
                    if (isTransfer) {
                        setItemCashView();
                    } else {
                        setItemView();
                    }
                } else {
                    String logined = response.getLogined();
                    if (logined.trim().equals("0")) {
                        GoLoginUtil.BaseToLoginActivity(mContext);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainFundListfail() {
        ToastUtil.showToast(this, "网络不给力!");
    }


    public abstract void setItemView();

    public abstract void setItemCashView();
}
