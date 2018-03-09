package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.CheckSafeQuestionImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpCheckSafeQuestionService implements HttpServiceURL {
    TRJActivity mpa;
    CheckSafeQuestionImpl ai;

    public HttpCheckSafeQuestionService(TRJActivity mpa,
                                        CheckSafeQuestionImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainCheckSafeQuestion() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();

        mpa.post(CHECK_IS_SETTING_URL, rp, new BaseJsonHandler<BaseJson>(mpa, false) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainCheckSafeQuestionsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainCheckSafeQuestionfail();
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
