package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.SafeQuestionSelectImpl;
import com.lcshidai.lc.model.account.SafeQuestionSelectJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpSafeQuestionSelectService implements HttpServiceURL {
    TRJActivity mpa;
    SafeQuestionSelectImpl ai;

    public HttpSafeQuestionSelectService(TRJActivity mpa,
                                         SafeQuestionSelectImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainSafeQuestionSelect() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(GET_QUESTION_LIST_URL, rp, new BaseJsonHandler<SafeQuestionSelectJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, SafeQuestionSelectJson response) {
                ai.gainSafeQuestionSelectsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  SafeQuestionSelectJson errorResponse) {
                ai.gainSafeQuestionSelectfail();
            }

            @Override
            protected SafeQuestionSelectJson parseResponse(String rawJsonData,
                                                           boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), SafeQuestionSelectJson.class).next();
            }

        });
    }
}
