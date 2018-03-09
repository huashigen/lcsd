package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.PayPwdQuestionImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.PayPwdQuestionJson;
import com.lcshidai.lc.model.account.PayPwdQuestionStrJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpPayPwdQuestionService implements HttpServiceURL {
    TRJActivity mpa;
    PayPwdQuestionImpl ai;

    public HttpPayPwdQuestionService(TRJActivity mpa, PayPwdQuestionImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainPayPwdQuestion() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(GET_QUESTION_URL, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainPayPwdQuestionsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainPayPwdQuestionfail();
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData,
                                             boolean isFailure) throws Throwable {
                BaseJson baseJson = super.parseResponse(rawJsonData, isFailure);
                if (baseJson.getBoolen().equals("1")) {
                    return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), PayPwdQuestionJson.class).next();
                } else {
                    return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), PayPwdQuestionStrJson.class).next();
                }
            }

        });
    }
}
