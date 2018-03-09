package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.GetSharkImpl;
import com.lcshidai.lc.model.account.GetSharkJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpGetSharkService implements HttpServiceURL {
    TRJActivity mpa;
    GetSharkImpl ai;

    public HttpGetSharkService(TRJActivity mpa, GetSharkImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainGetShark(String order_id, final boolean tmsonido) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("order_id", order_id);
        mpa.post(SHAKE_URL, rp, new BaseJsonHandler<GetSharkJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, GetSharkJson response) {
                ai.gainGetSharksuccess(response, tmsonido);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  GetSharkJson errorResponse) {
                ai.gainGetSharkfail();
            }

            @Override
            protected GetSharkJson parseResponse(String rawJsonData,
                                                 boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), GetSharkJson.class).next();
            }

        });
    }
}
