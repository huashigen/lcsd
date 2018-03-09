package com.lcshidai.lc.service.account;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AccountCashOutConfirmImp;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpCashOutConfirmService implements HttpServiceURL {
    TRJActivity mpa;
    AccountCashOutConfirmImp ai;

    public HttpCashOutConfirmService(TRJActivity mpa,
                                     AccountCashOutConfirmImp ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void cashOutConfirm(String amount, String mobileCode, String pay_wd) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("amount", amount);
        rp.put("mobileCode", mobileCode);
        rp.put("pay_wd", pay_wd);
        mpa.post(CASH_OUT_CONFIRM, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.cashOutSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.cashOutFail(statusCode);
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData,
                                             boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
            }

        });
    }
}
