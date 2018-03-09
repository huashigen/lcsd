package com.lcshidai.lc.service.oneKeyInvest;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.onKeyInvest.CloseAutoInvestImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

/**
 * 关闭一键投资
 */
public class CloseAutoInvestService implements HttpServiceURL {
    TRJActivity mpa;
    CloseAutoInvestImpl ai;

    public CloseAutoInvestService(TRJActivity mpa, CloseAutoInvestImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void closeAutoInvest(String code) {

        if (null == mpa)
            return;
        RequestParams rq = new RequestParams();
        rq.put("code", code);
        mpa.post(CLOSE_ONE_KEY_INVEST, rq, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseJson response) {
                ai.closeAutoInvestSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.closeAutoInvestFailed();
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
            }

        });
    }
}