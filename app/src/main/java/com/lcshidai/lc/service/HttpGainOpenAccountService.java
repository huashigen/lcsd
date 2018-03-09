package com.lcshidai.lc.service;

import android.content.Context;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.GainOpenAccountImpl;
import com.lcshidai.lc.model.OpenAccountJson;

import org.apache.http.Header;

/**
 * Created by RandyZhang on 16/7/7.
 */
public class HttpGainOpenAccountService implements HttpServiceURL {
    Context context;
    GainOpenAccountImpl gainOpenAccountImpl;

    public HttpGainOpenAccountService(Context context, GainOpenAccountImpl gainOpenAccountImpl) {
        this.context = context;
        this.gainOpenAccountImpl = gainOpenAccountImpl;
    }

    /**
     * 获取开户所需信息
     */
    public void getOpenAccountInfo() {
        if (null == context)
            return;
        RequestParams param = new RequestParams();

        LCHttpClient.get(context, GET_OPEN_ACCOUNT_INFO, param, new BaseJsonHandler<OpenAccountJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, OpenAccountJson response) {
                gainOpenAccountImpl.getOpenAccountInfoSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  OpenAccountJson errorResponse) {
                gainOpenAccountImpl.getOpenAccountInfoFailed();
            }

            @Override
            protected OpenAccountJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), OpenAccountJson.class).next();
            }

        });
    }

}
