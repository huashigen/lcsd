package com.lcshidai.lc.service.licai;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.licai.GetFundDetailInfoImpl;
import com.lcshidai.lc.model.licai.FundDetailInfoJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpGetFundDetailInfoService implements HttpServiceURL {
    TRJActivity mpa;
    GetFundDetailInfoImpl ai;

    public HttpGetFundDetailInfoService(TRJActivity mpa, GetFundDetailInfoImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    /**
     * 基金账户登录
     *
     * @param access_token access_token
     * @param user_token   user_token
     * @param fund_id      基金id
     */
    public void getFundDetailInfo(String access_token, String user_token, String fund_id) {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.put("access_token", access_token);
        rp.put("user_token", user_token);
        rp.put("fund_id", fund_id);
        LCHttpClient.postWithFullUrl(mpa, GET_FUND_DETAIL_INFO, rp, new BaseJsonHandler<FundDetailInfoJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, FundDetailInfoJson response) {
                ai.getFundDetailInfoSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  FundDetailInfoJson errorResponse) {
                ai.getFundDetailInfoFailed();
            }

            @Override
            protected FundDetailInfoJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), FundDetailInfoJson.class).next();
            }

        });
    }

}
