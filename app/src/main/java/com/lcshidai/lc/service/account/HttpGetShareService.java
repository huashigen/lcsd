package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.GetShareImpl;
import com.lcshidai.lc.model.account.GetShareJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpGetShareService implements HttpServiceURL {
    TRJActivity mpa;
    GetShareImpl ai;

    public HttpGetShareService(TRJActivity mpa, GetShareImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainGetShare(final boolean isInit) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("pos", "invest");
        mpa.post(URL_GET_DATA_SHARE, rp, new BaseJsonHandler<GetShareJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, GetShareJson response) {
                ai.gainGetSharesuccess(response, isInit);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  GetShareJson errorResponse) {
                ai.gainGetSharefail();
            }

            @Override
            protected GetShareJson parseResponse(String rawJsonData,
                                                 boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), GetShareJson.class).next();
            }

        });
    }
}
