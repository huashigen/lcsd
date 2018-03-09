package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.BespeakInfoDataImpl;
import com.lcshidai.lc.model.account.BespeakInfoDataJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpBespeakInfoDataService implements HttpServiceURL {
    TRJActivity mpa;
    BespeakInfoDataImpl ai;

    public HttpBespeakInfoDataService(TRJActivity mpa,
                                      BespeakInfoDataImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainBespeakInfoData(String bespeak_id) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("id", bespeak_id);
        LCHttpClient.get(mpa, BESEPAK_INFO_URL, rp, new BaseJsonHandler<BespeakInfoDataJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BespeakInfoDataJson response) {
                ai.gainBespeakInfoDatasuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BespeakInfoDataJson errorResponse) {
                ai.gainBespeakInfoDatafail();
            }

            @Override
            protected BespeakInfoDataJson parseResponse(String rawJsonData,
                                                        boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BespeakInfoDataJson.class).next();
            }

        });
    }
}
