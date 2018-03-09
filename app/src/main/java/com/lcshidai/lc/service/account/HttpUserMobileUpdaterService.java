package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserMobileUpdaterImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpUserMobileUpdaterService implements HttpServiceURL {
    TRJActivity mpa;
    UserMobileUpdaterImpl ai;

    public HttpUserMobileUpdaterService(TRJActivity mpa,
                                        UserMobileUpdaterImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUserMobileUpdater(String mobile, String dy_code) {
        if (null == mpa) return;
        RequestParams rq = new RequestParams();
        rq.put("mobile", mobile);
        rq.put("code", dy_code);
        mpa.post(URL_SEND_DATA, rq, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainUserMobileUpdatersuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainUserMobileUpdaterfail();
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
