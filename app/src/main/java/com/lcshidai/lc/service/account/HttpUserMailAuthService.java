package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserMailAuthImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpUserMailAuthService implements HttpServiceURL {
    TRJActivity mpa;
    UserMailAuthImpl ai;

    public HttpUserMailAuthService(TRJActivity mpa,
                                   UserMailAuthImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUserMailAuth(String mail, int mMode) {
        if (null == mpa) return;
        RequestParams rq = new RequestParams();
        String url = "";
        if (mMode == 1) {
            url = URL_SEND_MIAL_AUTH;
            rq.put("mail", mail);
        } else {
            url = URL_SEND_MAIL_UPDATE2;
            rq.put("email", mail);
        }

        mpa.post(url, rq, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainUserMailAuthsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainUserMailAuthfail();
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
