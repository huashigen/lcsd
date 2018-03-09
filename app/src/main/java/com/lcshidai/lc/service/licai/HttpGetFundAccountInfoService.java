package com.lcshidai.lc.service.licai;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.licai.GetFundAccountInfoImpl;
import com.lcshidai.lc.model.licai.FundAccountInfoJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpGetFundAccountInfoService implements HttpServiceURL {
    TRJActivity mpa;
    GetFundAccountInfoImpl ai;

    public HttpGetFundAccountInfoService(TRJActivity mpa, GetFundAccountInfoImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    /**
     * 基金账户登录
     *
     * @param access_token client
     * @param user_token   user_token
     */
    public void getFundAccountInfo(String access_token, String user_token) {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.put("access_token", access_token);
        rp.put("user_token", user_token);
        LCHttpClient.postWithFullUrl(mpa, GET_FUND_ACCOUNT_INFO, rp, new BaseJsonHandler<FundAccountInfoJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, FundAccountInfoJson response) {
                ai.getFundAccountInfoSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  FundAccountInfoJson errorResponse) {
                ai.getFundAccountInfoFailed(errorResponse);
            }

            @Override
            protected FundAccountInfoJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), FundAccountInfoJson.class).next();
            }

        });
    }

}
