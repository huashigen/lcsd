package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.RechargeInfoImpl;
import com.lcshidai.lc.model.account.RechargeInfoJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpRechargeInfoService implements HttpServiceURL {
    TRJActivity mpa;
    RechargeInfoImpl ai;

    public HttpRechargeInfoService(TRJActivity mpa, RechargeInfoImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainRechargeInfo(String id) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("id", id);
        mpa.post(RECHARGE_INFO_URL, rp, new BaseJsonHandler<RechargeInfoJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, RechargeInfoJson response) {
                ai.gainRechargeInfosuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  RechargeInfoJson errorResponse) {
                ai.gainRechargeInfofail();
            }

            @Override
            protected RechargeInfoJson parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), RechargeInfoJson.class).next();
            }

        });
    }
}
