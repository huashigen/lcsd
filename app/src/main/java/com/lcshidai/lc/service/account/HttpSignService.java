package com.lcshidai.lc.service.account;

import android.content.Context;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.account.SignImpl;
import com.lcshidai.lc.model.account.SignJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;

import org.apache.http.Header;

/**
 * Created by RandyZhang on 16/7/7.
 */
public class HttpSignService implements HttpServiceURL {
    Context context;
    SignImpl signImpl;

    public HttpSignService(Context context, SignImpl signImpl) {
        this.context = context;
        this.signImpl = signImpl;
    }

    /**
     * 签到
     */
    public void sign() {
        if (null == context)
            return;
        RequestParams param = new RequestParams();

        LCHttpClient.get(context, SIGN, param, new BaseJsonHandler<SignJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, SignJson response) {
                signImpl.signSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  SignJson errorResponse) {
                signImpl.signFailed();
            }

            @Override
            protected SignJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), SignJson.class).next();
            }

        });
    }

}
