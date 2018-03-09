package com.lcshidai.lc.service.licai;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.licai.CheckFundRegisterImpl;
import com.lcshidai.lc.model.licai.CheckFundRegisterJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpCheckFundRegisterService implements HttpServiceURL {
    TRJActivity mpa;
    CheckFundRegisterImpl ai;

    public HttpCheckFundRegisterService(TRJActivity mpa, CheckFundRegisterImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    /**
     * 基金账户登录
     *
     * @param access_token client
     * @param c_uid        c_uid
     */
    public void checkFundRegister(String access_token, String c_uid) {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.put("access_token", access_token);
        rp.put("c_uid", c_uid);
        LCHttpClient.postWithFullUrl(mpa, CHECK_FUND_REGISTER, rp, new BaseJsonHandler<CheckFundRegisterJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, CheckFundRegisterJson response) {
                ai.checkFundRegisterSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  CheckFundRegisterJson errorResponse) {
                ai.checkFundRegisterFailed(errorResponse);
            }

            @Override
            protected CheckFundRegisterJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), CheckFundRegisterJson.class).next();
            }

        });
    }

}
