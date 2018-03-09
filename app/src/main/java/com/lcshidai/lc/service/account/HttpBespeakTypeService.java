package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.BespeakTypeImpl;
import com.lcshidai.lc.model.account.BespeakTypeJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpBespeakTypeService implements HttpServiceURL {
    TRJActivity mpa;
    BespeakTypeImpl ai;

    public HttpBespeakTypeService(TRJActivity mpa,
                                  BespeakTypeImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainBespeakType() {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        mpa.post(BESPEAK_TYPE_URL, rp, new BaseJsonHandler<BespeakTypeJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BespeakTypeJson response) {
                ai.gainBespeakTypesuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BespeakTypeJson errorResponse) {
                ai.gainBespeakTypefail();
            }

            @Override
            protected BespeakTypeJson parseResponse(String rawJsonData,
                                                    boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BespeakTypeJson.class).next();
            }

        });
    }
}
