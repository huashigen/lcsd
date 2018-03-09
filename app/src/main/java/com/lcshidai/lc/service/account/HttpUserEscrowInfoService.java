package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AccountUserEscrowInfoImp;
import com.lcshidai.lc.model.account.UserEscrowInfoJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpUserEscrowInfoService implements HttpServiceURL {
    TRJActivity mpa;
    AccountUserEscrowInfoImp ai;

    public HttpUserEscrowInfoService(TRJActivity mpa, AccountUserEscrowInfoImp ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUserEscrowInfo() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(GET_ESCROW_USER_INFO, rp, new BaseJsonHandler<UserEscrowInfoJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, UserEscrowInfoJson response) {
                ai.gainUserEscrowInfoSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData, UserEscrowInfoJson errorResponse) {
                ai.gainUserEscrowInfoFail();
            }

            @Override
            protected UserEscrowInfoJson parseResponse(String rawJsonData,
                                                       boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), UserEscrowInfoJson.class).next();
            }

        });
    }
}
