package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.WithdrawalsListImpl;
import com.lcshidai.lc.model.account.WithdrawalsListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpWithdrawalsListService implements HttpServiceURL {
    TRJActivity mpa;
    WithdrawalsListImpl ai;

    public HttpWithdrawalsListService(TRJActivity mpa,
                                      WithdrawalsListImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainWithdrawalsList(int currentPage, int perPage) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("p", String.valueOf(currentPage));
        rp.put("perpage", String.valueOf(perPage));
        mpa.post(WITHDRAWALS_LIST_URL, rp, new BaseJsonHandler<WithdrawalsListJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, WithdrawalsListJson response) {
                ai.gainWithdrawalsListsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  WithdrawalsListJson errorResponse) {
                ai.gainWithdrawalsListfail();
            }

            @Override
            protected WithdrawalsListJson parseResponse(String rawJsonData,
                                                        boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), WithdrawalsListJson.class).next();
            }

        });
    }
}
