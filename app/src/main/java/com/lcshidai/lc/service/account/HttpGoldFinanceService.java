package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.GoldFinanceImpl;
import com.lcshidai.lc.model.account.GoldFinanceJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpGoldFinanceService implements HttpServiceURL {
    TRJActivity mpa;
    GoldFinanceImpl ai;

    public HttpGoldFinanceService(TRJActivity mpa, GoldFinanceImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainGoldFinance() {
        if (null == mpa) return;
        RequestParams params = new RequestParams();
        mpa.post(URL_GETTYJ_DATA, params, new BaseJsonHandler<GoldFinanceJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, GoldFinanceJson response) {
                ai.gainGoldFinancesuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  GoldFinanceJson errorResponse) {
                ai.gainGoldFinancefail();
            }

            @Override
            protected GoldFinanceJson parseResponse(String rawJsonData,
                                                    boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), GoldFinanceJson.class).next();
            }

        });
    }
}
