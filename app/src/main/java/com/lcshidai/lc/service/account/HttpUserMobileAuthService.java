package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserMobileAuthImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpUserMobileAuthService implements HttpServiceURL {
    TRJActivity mpa;
    UserMobileAuthImpl ai;

    public HttpUserMobileAuthService(TRJActivity mpa, UserMobileAuthImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUserMobileAuth(String mobile, String dy_code, int mMode) {
        if (null == mpa) return;
        String url = "";
        if (mMode == 1) {
            url = URL_SEND_AUTH;
        } else {
            url = URL_SEND_UPDATE2;
        }
        RequestParams rq = new RequestParams();
        rq.put("mobile", mobile);
        rq.put("code", dy_code);
        mpa.post(url, rq, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainUserMobileAuthsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainUserMobileAuthfail();
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
