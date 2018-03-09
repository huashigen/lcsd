package com.lcshidai.lc.service.licai;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.licai.GetFundTypeListImpl;
import com.lcshidai.lc.model.licai.FundTypeListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpGetFundTypeListService implements HttpServiceURL {
    TRJActivity mpa;
    GetFundTypeListImpl ai;

    public HttpGetFundTypeListService(TRJActivity mpa, GetFundTypeListImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }


    /**
     * 获取我的预约列表
     *
     * @param access_token access_token
     * @param user_token   user_token
     */
    public void getFundTypeList(String access_token, String user_token) {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.put("access_token", access_token);
        rp.put("user_token", user_token);

        LCHttpClient.postWithFullUrl(mpa, GET_FUND_TYPE_LIST, rp, new BaseJsonHandler<FundTypeListJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, FundTypeListJson response) {
                ai.getFundTypeListSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  FundTypeListJson errorResponse) {
                ai.getFundTypeListFailed(errorResponse);
            }

            @Override
            protected FundTypeListJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), FundTypeListJson.class).next();
            }

        });
    }

}
