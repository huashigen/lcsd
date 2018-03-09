package com.lcshidai.lc.service.oneKeyInvest;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.onKeyInvest.IsOpenAutoInvestImpl;
import com.lcshidai.lc.model.oneKeyInvest.IsOpenAutoInvestJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

/**
 * 是否开启一键投资
 */
public class IsOpenAutoInvestService implements HttpServiceURL {
    TRJActivity mpa;
    IsOpenAutoInvestImpl ai;

    public IsOpenAutoInvestService(TRJActivity mpa, IsOpenAutoInvestImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void isOpenAutoInvest() {

        if (null == mpa)
            return;
        RequestParams rq = new RequestParams();
        mpa.post(IS_OPEN_ONE_KEY_INVEST, rq, new BaseJsonHandler<IsOpenAutoInvestJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, IsOpenAutoInvestJson response) {
                ai.getIsOpenAutoInvestSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  IsOpenAutoInvestJson errorResponse) {
                ai.getIsOpenAutoInvestFailed();
            }

            @Override
            protected IsOpenAutoInvestJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), IsOpenAutoInvestJson.class).next();
            }

        });
    }
}