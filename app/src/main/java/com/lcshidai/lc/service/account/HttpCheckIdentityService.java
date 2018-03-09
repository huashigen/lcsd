package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.CheckIdentityImpl;
import com.lcshidai.lc.model.account.CheckIdentityJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

public class HttpCheckIdentityService implements HttpServiceURL {
    TRJActivity mpa;
    CheckIdentityImpl ai;
    private String request_url;

    public HttpCheckIdentityService(TRJActivity mpa,
                                    CheckIdentityImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainCheckIdentity(int intentType, CustomEditTextLeftIcon et_1, CustomEditTextLeftIcon et_2, String selectQuestionCodeNo, CustomEditTextLeftIcon et_answer) {
        if (null == mpa) return;
        RequestParams params = new RequestParams();
        if (intentType == 0) {
            params.put("real_name", et_1.getEdtText());
            params.put("person_id", et_2.getEdtText());
            request_url = AUTH_VERIFY_URL_PWD;
        } else if (intentType == 1) {
            params.put("account_no", et_1.getEdtText());
            params.put("person_id", et_2.getEdtText());
            request_url = BANKCARD_VERIFY_URL_PWD;
        } else if (intentType == 2) {
            params.put("sqa_key", selectQuestionCodeNo);
            params.put("sqa_value", et_answer.getEdtText());
            request_url = SAFE_VERIFY_URL_PWD;
        }
        mpa.post(request_url, params, new BaseJsonHandler<CheckIdentityJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, CheckIdentityJson response) {
                ai.gainCheckIdentitysuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  CheckIdentityJson errorResponse) {
                ai.gainCheckIdentityfail();
            }

            @Override
            protected CheckIdentityJson parseResponse(String rawJsonData,
                                                      boolean isFailure) throws Throwable {
                // TODO Auto-generated method stub
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), CheckIdentityJson.class).next();
            }

        });
    }
}
