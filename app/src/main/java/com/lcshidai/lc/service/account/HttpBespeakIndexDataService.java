package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.BespeakIndexDataImpl;
import com.lcshidai.lc.model.account.BespeakIndexDataJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpBespeakIndexDataService implements HttpServiceURL {
    TRJActivity mpa;
    BespeakIndexDataImpl ai;

    public HttpBespeakIndexDataService(TRJActivity mpa,
                                       BespeakIndexDataImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainBespeakIndexData() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(BESPEAK_INDEX_DATA, rp, new BaseJsonHandler<BespeakIndexDataJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BespeakIndexDataJson response) {
                ai.gainBespeakIndexDatasuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BespeakIndexDataJson errorResponse) {
                ai.gainBespeakIndexDatafail();
            }

            @Override
            protected BespeakIndexDataJson parseResponse(String rawJsonData,
                                                         boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BespeakIndexDataJson.class).next();
            }

        });
    }
}
