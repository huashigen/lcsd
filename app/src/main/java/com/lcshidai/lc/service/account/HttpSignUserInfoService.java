package com.lcshidai.lc.service.account;

import android.content.Context;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.account.SignUserInfoImpl;
import com.lcshidai.lc.model.account.SignUserInfoJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;

import org.apache.http.Header;

/**
 * Created by RandyZhang on 16/7/7.
 */
public class HttpSignUserInfoService implements HttpServiceURL {
    Context context;
    SignUserInfoImpl signUserInfoImpl;

    public HttpSignUserInfoService(Context context, SignUserInfoImpl signUserInfoImpl) {
        this.context = context;
        this.signUserInfoImpl = signUserInfoImpl;
    }

    /**
     * 签到
     */
    public void getSignUserInfo() {
        if (null == context)
            return;
        RequestParams param = new RequestParams();

        LCHttpClient.get(context, SIGN_SCORE_INFO, param, new BaseJsonHandler<SignUserInfoJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, SignUserInfoJson response) {
                signUserInfoImpl.getSignUserInfoSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  SignUserInfoJson errorResponse) {
                signUserInfoImpl.getSignUserInfoFailed();
            }

            @Override
            protected SignUserInfoJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), SignUserInfoJson.class).next();
            }

        });
    }

}
