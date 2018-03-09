package com.lcshidai.lc.service.account;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AccountIsZheshangCardImp;
import com.lcshidai.lc.model.account.AccountIsZheshangCardJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpIsZheshangCardService implements HttpServiceURL {
    TRJActivity mpa;
    AccountIsZheshangCardImp ai;

    public HttpIsZheshangCardService(TRJActivity mpa, AccountIsZheshangCardImp ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void isZheshangCard() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(IS_ZHESHANG_CARD, rp, new BaseJsonHandler<AccountIsZheshangCardJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, AccountIsZheshangCardJson response) {
                ai.gainIsZheshangCardSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  AccountIsZheshangCardJson errorResponse) {
                ai.gainIsZheshangCardFail();
            }

            @Override
            protected AccountIsZheshangCardJson parseResponse(String rawJsonData,
                                                              boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), AccountIsZheshangCardJson.class).next();
            }

        });
    }
}
