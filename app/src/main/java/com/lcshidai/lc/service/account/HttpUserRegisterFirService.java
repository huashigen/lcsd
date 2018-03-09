package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserRegisterFirImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpUserRegisterFirService implements HttpServiceURL {
    TRJActivity mpa;
    UserRegisterFirImpl ai;

    public HttpUserRegisterFirService(TRJActivity mpa,
                                      UserRegisterFirImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUserRegisterFir(String mobile) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("mobile", mobile);
        mpa.post(IS_MOBILE_REG, rp, new BaseJsonHandler<BaseJson>(mpa, false) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainUserRegisterFirsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainUserRegisterFirfail();
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
