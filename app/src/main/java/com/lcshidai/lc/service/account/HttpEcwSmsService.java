package com.lcshidai.lc.service.account;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AccountEcwSmsImp;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpEcwSmsService implements HttpServiceURL {
    TRJActivity mpa;
    AccountEcwSmsImp ai;

    public HttpEcwSmsService(TRJActivity mpa,
                             AccountEcwSmsImp ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainEcwSmsCode(String amount, String type) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("amount", amount);
        rp.put("type", type);
        mpa.post(ECW_SMS_CODE_URL, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainEcwSmsCodeSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainEcwSmsCodeFail();
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
