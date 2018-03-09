package com.lcshidai.lc.service.licai;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.licai.AccessTokenImpl;
import com.lcshidai.lc.model.licai.AccessTokenJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpGetAccessTokenService implements HttpServiceURL {
    TRJActivity mpa;
    AccessTokenImpl ai;

    public HttpGetAccessTokenService(TRJActivity mpa, AccessTokenImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    /**
     * 获取access_token
     *
     * @param client        client
     * @param device_number device_number
     */
    public void getAccessToken(String client, String device_number) {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();

        rp.put("client", client);
        rp.put("device_number", device_number);
        LCHttpClient.postWithFullUrl(mpa, GET_ACCESS_TOKEN, rp, new BaseJsonHandler<AccessTokenJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, AccessTokenJson response) {
                ai.getAccessTokenSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  AccessTokenJson errorResponse) {
                ai.getAccessTokenFailed();
            }

            @Override
            protected AccessTokenJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), AccessTokenJson.class).next();
            }

        });
    }

}
