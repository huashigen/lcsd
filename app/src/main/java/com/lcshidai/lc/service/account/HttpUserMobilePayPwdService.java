package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserMobilePayPwdImpl;
import com.lcshidai.lc.model.account.UserMobilePayPwdJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpUserMobilePayPwdService implements HttpServiceURL {
    TRJActivity mpa;
    UserMobilePayPwdImpl ai;

    public HttpUserMobilePayPwdService(TRJActivity mpa,
                                       UserMobilePayPwdImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUserMobilePayPwd() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(URL_AUTH_STATUS, rp, new BaseJsonHandler<UserMobilePayPwdJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, UserMobilePayPwdJson response) {
                ai.gainUserMobilePayPwdsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  UserMobilePayPwdJson errorResponse) {
                ai.gainUserMobilePayPwdfail();
            }

            @Override
            protected UserMobilePayPwdJson parseResponse(String rawJsonData,
                                                         boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), UserMobilePayPwdJson.class).next();
            }

        });
    }
}
