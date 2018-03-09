package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.WithdrawalsImpl;
import com.lcshidai.lc.model.account.WithdrawalsJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpWithdrawalsService implements HttpServiceURL {
    TRJActivity mpa;
    WithdrawalsImpl ai;

    public HttpWithdrawalsService(TRJActivity mpa,
                                  WithdrawalsImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainWithdrawals() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(GET_BANK_LIST_URL, rp, new BaseJsonHandler<WithdrawalsJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, WithdrawalsJson response) {
                ai.gainWithdrawalssuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  WithdrawalsJson errorResponse) {
                ai.gainWithdrawalsfail();
            }

            @Override
            protected WithdrawalsJson parseResponse(String rawJsonData,
                                                    boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), WithdrawalsJson.class).next();
            }

        });
    }
}
