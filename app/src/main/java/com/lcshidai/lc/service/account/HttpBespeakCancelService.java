package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.BespeakCancelImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpBespeakCancelService implements HttpServiceURL {
    TRJActivity mpa;
    BespeakCancelImpl ai;

    public HttpBespeakCancelService(TRJActivity mpa,
                                    BespeakCancelImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void getBespeakCancel(String current_bespeak_id) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("id", current_bespeak_id);
        mpa.post(BESPEAK_CANCEL, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainBespeakCancelsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainBespeakCancelfail();
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData,
                                             boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
            }

        });
    }
}
