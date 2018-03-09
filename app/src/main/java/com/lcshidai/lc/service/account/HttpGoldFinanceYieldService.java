package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.GoldFinanceYieldImpl;
import com.lcshidai.lc.model.account.GoldFinanceYieldJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpGoldFinanceYieldService implements HttpServiceURL {
    TRJActivity mpa;
    GoldFinanceYieldImpl ai;

    public HttpGoldFinanceYieldService(TRJActivity mpa, GoldFinanceYieldImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainGoldFinanceYield() {
        if (null == mpa) return;
        RequestParams params = new RequestParams();
        mpa.post(BANNAER_URL, params, new BaseJsonHandler<GoldFinanceYieldJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, GoldFinanceYieldJson response) {
                ai.gainGoldFinanceYieldsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  GoldFinanceYieldJson errorResponse) {
                ai.gainGoldFinanceYieldfail();
            }

            @Override
            protected GoldFinanceYieldJson parseResponse(String rawJsonData,
                                                         boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), GoldFinanceYieldJson.class).next();
            }

        });
    }
}
