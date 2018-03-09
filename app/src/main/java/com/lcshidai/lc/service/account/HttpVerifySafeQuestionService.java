package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import android.widget.EditText;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.VerifySafeQuestionImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpVerifySafeQuestionService implements HttpServiceURL {
    TRJActivity mpa;
    VerifySafeQuestionImpl ai;

    public HttpVerifySafeQuestionService(TRJActivity mpa,
                                         VerifySafeQuestionImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainVerifySafeQuestion(String selectQuestionCodeNo, EditText et_answer) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("sqa_key", selectQuestionCodeNo);
        rp.put("sqa_value", et_answer.getText().toString());
        mpa.post(VERIFY_SAFE_QUESTION_URL, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainVerifySafeQuestionsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainVerifySafeQuestionfail();
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
