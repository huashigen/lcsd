package com.lcshidai.lc.service.licai;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.licai.FundLoginImpl;
import com.lcshidai.lc.model.licai.FundLoginJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpFundLoginService implements HttpServiceURL {
    TRJActivity mpa;
    FundLoginImpl ai;

    public HttpFundLoginService(TRJActivity mpa, FundLoginImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    /**
     * 基金账户登录
     *
     * @param access_token client
     * @param uc_token     device_number
     * @param uname        理财时代用户名
     * @param mobile       理财时代手机号
     * @param person_id    person_id
     * @param address      地址
     * @param c_uid        c_uid
     * @param auth         auth
     * @param from_client  来源
     */
    public void fundLogin(String access_token, String uc_token, String uname, String mobile, String person_id,
                          String address, String c_uid, String auth, String from_client) {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.put("access_token", access_token);
        rp.put("uc_token", uc_token);
        rp.put("uname", uname);
        rp.put("mobile", mobile);
        rp.put("person_id", person_id);
        rp.put("address", address);
        rp.put("c_uid", c_uid);
        rp.put("auth", auth);
        rp.put("from_client", from_client);
        LCHttpClient.postWithFullUrl(mpa, FUND_LOGIN, rp, new BaseJsonHandler<FundLoginJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, FundLoginJson response) {
                ai.fundLoginSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  FundLoginJson errorResponse) {
                ai.fundLoginFailed(errorResponse);
            }

            @Override
            protected FundLoginJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), FundLoginJson.class).next();
            }

        });
    }

}
