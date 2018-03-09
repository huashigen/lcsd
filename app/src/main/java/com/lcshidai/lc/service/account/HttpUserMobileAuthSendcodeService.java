package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserMobileAuthSendcodeImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpUserMobileAuthSendcodeService implements HttpServiceURL {
    TRJActivity mpa;
    UserMobileAuthSendcodeImpl ai;

    public HttpUserMobileAuthSendcodeService(TRJActivity mpa,
                                             UserMobileAuthSendcodeImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUserMobileAuthSendcode(String mobile, int mMode) {
        if (null == mpa) return;
        String url = "";
        if (mMode == 1) {
            url = URL_SEND_AUTH_CODE;
        } else {
            url = URL_SEND_EDIT_CODE2;
        }
        RequestParams rq = new RequestParams();
        rq.put("mobile", mobile);
        mpa.post(url, rq, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainUserMobileAuthSendcodesuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainUserMobileAuthSendcodefail();
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
