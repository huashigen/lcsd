package com.lcshidai.lc.service.oneKeyInvest;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.onKeyInvest.GetAutoInvestSmsCodeImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

/**
 * 关闭一键投资
 */
public class GetAutoInvestSmsCodeService implements HttpServiceURL {
    TRJActivity mpa;
    GetAutoInvestSmsCodeImpl ai;

    public GetAutoInvestSmsCodeService(TRJActivity mpa, GetAutoInvestSmsCodeImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void getAutoInvestSmsCode() {
        if (null == mpa)
            return;
        RequestParams rq = new RequestParams();
        mpa.post(GET_ONE_KEY_INVEST_SMS_CODE, rq, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseJson response) {
                ai.getAutoInvestSmsCodeSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.getAutoInvestSmsCodeFailed();
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
            }

        });
    }
}