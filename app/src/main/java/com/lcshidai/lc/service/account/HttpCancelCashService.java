package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.CancelCashImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpCancelCashService implements HttpServiceURL {
    TRJActivity mpa;
    CancelCashImpl ai;

    public HttpCancelCashService(TRJActivity mpa,
                                 CancelCashImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainCancelCash(String prj_order_id) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("fid", prj_order_id);//
        mpa.post(urlCancelCash, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainCancelCashsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainCancelCashfail();
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
