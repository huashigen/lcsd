package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.WithdrawalsIsBespeakImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpWithdrawalsIsBespeakService implements HttpServiceURL {
    TRJActivity mpa;
    WithdrawalsIsBespeakImpl ai;

    public HttpWithdrawalsIsBespeakService(TRJActivity mpa,
                                           WithdrawalsIsBespeakImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainWithdrawalsIsBespeak(final String editMoney) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("type", "1");
        rp.put("amount", editMoney);
        mpa.post(BESPEAK_IS, rp, new BaseJsonHandler<BaseJson>(mpa, false) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainWithdrawalsIsBespeaksuccess(editMoney, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainWithdrawalsIsBespeakfail();
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
