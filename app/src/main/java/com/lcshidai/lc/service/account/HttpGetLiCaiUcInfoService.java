package com.lcshidai.lc.service.account;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.GetLiCaiUcInfoImpl;
import com.lcshidai.lc.model.account.LiCaiUcInfoJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpGetLiCaiUcInfoService implements HttpServiceURL {
    TRJActivity mpa;
    GetLiCaiUcInfoImpl ai;

    public HttpGetLiCaiUcInfoService(TRJActivity mpa, GetLiCaiUcInfoImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void getLiCaiUcInfo(String uc_id) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("uc_id", uc_id);
        mpa.post(GET_LICAI_UC_INFO, rp, new BaseJsonHandler<LiCaiUcInfoJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, LiCaiUcInfoJson response) {
                ai.getLiCaiUcInfoSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, LiCaiUcInfoJson errorResponse) {
                ai.getLiCaiUcInfoFailed();
            }

            @Override
            protected LiCaiUcInfoJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), LiCaiUcInfoJson.class).next();
            }

        });
    }
}
