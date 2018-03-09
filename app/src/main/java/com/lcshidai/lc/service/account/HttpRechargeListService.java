package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.RechargeListImpl;
import com.lcshidai.lc.model.account.ApplyCashoutJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpRechargeListService implements HttpServiceURL {
    TRJActivity mpa;
    RechargeListImpl ai;

    public HttpRechargeListService(TRJActivity mpa, RechargeListImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void loadBalanceData() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(BALANCE_URL, rp, new BaseJsonHandler<ApplyCashoutJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, ApplyCashoutJson response) {
                ai.gainapplyCashoutsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  ApplyCashoutJson errorResponse) {
                ai.gainapplyCashoutfail();
            }

            @Override
            protected ApplyCashoutJson parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), ApplyCashoutJson.class).next();
            }

        });
    }
}
