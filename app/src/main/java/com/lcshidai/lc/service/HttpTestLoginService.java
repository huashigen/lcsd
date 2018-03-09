package com.lcshidai.lc.service;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.TestLoginImpl;
import com.lcshidai.lc.model.Loading.TestLoginJson;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpTestLoginService implements HttpServiceURL {
    TRJActivity mpa;
    TestLoginImpl ai;

    public HttpTestLoginService(TRJActivity mpa,
                                TestLoginImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainTestLogin() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(TEST_LOGIN_URL, rp, new BaseJsonHandler<TestLoginJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, TestLoginJson response) {
                ai.gainTestLoginsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  TestLoginJson errorResponse) {
                ai.gainTestLoginfail();
            }

            @Override
            protected TestLoginJson parseResponse(String rawJsonData,
                                                  boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), TestLoginJson.class).next();
            }

        });
    }
}
