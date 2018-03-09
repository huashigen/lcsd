package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.GetEscrowRemindImpl;
import com.lcshidai.lc.model.account.GetEscrowRemindJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpGetEscrowRemindService implements HttpServiceURL {
    TRJActivity mpa;
    GetEscrowRemindImpl ai;

    public HttpGetEscrowRemindService(TRJActivity mpa, GetEscrowRemindImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void getEscrowRemind() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(GET_ESCROW_REMIND, rp, new BaseJsonHandler<GetEscrowRemindJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, GetEscrowRemindJson response) {
                ai.getEscrowRemindSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  GetEscrowRemindJson errorResponse) {
                ai.getEscrowRemindFail();
            }

            @Override
            protected GetEscrowRemindJson parseResponse(String rawJsonData,
                                                        boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), GetEscrowRemindJson.class).next();
            }

        });
    }
}
