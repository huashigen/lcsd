package com.lcshidai.lc.service.account;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AccountCheckPayPassImp;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpCheckPayPassService implements HttpServiceURL {
    TRJActivity mpa;
    AccountCheckPayPassImp ai;

    public HttpCheckPayPassService(TRJActivity mpa, AccountCheckPayPassImp ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainEcwSmsCode(String pay_pwd) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("pay_pwd", pay_pwd);
        mpa.post(URL_CHECK_PAY_PWD, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.checkPayPassSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.checkPayPassFail();
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
