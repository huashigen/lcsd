package com.lcshidai.lc.service.account;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AccountHasEscrowedImp;
import com.lcshidai.lc.model.account.AccountHasEscrowedJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpHasEscrowedService implements HttpServiceURL {
    TRJActivity mpa;
    AccountHasEscrowedImp ai;

    public HttpHasEscrowedService(TRJActivity mpa, AccountHasEscrowedImp ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void hasEscrowed() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(HAS_ESCROWED, rp, new BaseJsonHandler<AccountHasEscrowedJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, AccountHasEscrowedJson response) {
                ai.gainHasEscrowedSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  AccountHasEscrowedJson errorResponse) {
                ai.gainHasEscrowedFail();
            }

            @Override
            protected AccountHasEscrowedJson parseResponse(String rawJsonData,
                                                           boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), AccountHasEscrowedJson.class).next();
            }

        });
    }
}
