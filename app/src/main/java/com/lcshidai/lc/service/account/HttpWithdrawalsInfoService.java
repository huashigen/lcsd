package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.WithdrawalsInfoImpl;
import com.lcshidai.lc.model.account.WithdrawalsInfoJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpWithdrawalsInfoService implements HttpServiceURL {
    TRJActivity mpa;
    WithdrawalsInfoImpl ai;

    public HttpWithdrawalsInfoService(TRJActivity mpa,
                                      WithdrawalsInfoImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainWithdrawalsInfo(String id) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("id", id);
        mpa.post(WITHDRAWALS_INFO_URL, rp, new BaseJsonHandler<WithdrawalsInfoJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, WithdrawalsInfoJson response) {
                ai.gainWithdrawalsInfosuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  WithdrawalsInfoJson errorResponse) {
                ai.gainWithdrawalsInfofail();
            }

            @Override
            protected WithdrawalsInfoJson parseResponse(String rawJsonData,
                                                        boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), WithdrawalsInfoJson.class).next();
            }

        });
    }
}
