package com.lcshidai.lc.service.account;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AccountEcwCashoutMoneyImp;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpEcwCashoutMoneyService implements HttpServiceURL {
    TRJActivity mpa;
    AccountEcwCashoutMoneyImp ai;

    public HttpEcwCashoutMoneyService(TRJActivity mpa,
                                      AccountEcwCashoutMoneyImp ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void getEcwCashoutMoney() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(URL_ECW_CASHOUT_MONEY, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainEcwCashoutMoneySuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainEcwCashoutMoneyFail();
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
