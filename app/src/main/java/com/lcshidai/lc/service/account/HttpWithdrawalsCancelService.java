package com.lcshidai.lc.service.account;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.account.WithdrawalsCancelImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpWithdrawalsCancelService implements HttpServiceURL {
    TRJActivity mpa;
    WithdrawalsCancelImpl ai;

    public HttpWithdrawalsCancelService(TRJActivity mpa,
                                        WithdrawalsCancelImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainWithdrawalsCancel(String id) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("id", id);
        mpa.post(CANCEL_WITHDRAWALS_URL, rp, new BaseJsonHandler<BaseJson>(mpa, false) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainWithdrawalsCancelsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainWithdrawalsCancelfail();
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
