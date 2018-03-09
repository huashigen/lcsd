package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AccountLoginoutImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.LoginStatusHelper;

public class HttpLoginoutService implements HttpServiceURL {
    TRJActivity mpa;
    AccountLoginoutImpl ai;

    public HttpLoginoutService(TRJActivity mpa, AccountLoginoutImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainAccountLoginout() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(LOGOUT, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainAccountLoginoutsuccess(response);
                LoginStatusHelper.LoginStatusChange = true;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainAccountLoginoutfail();
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
