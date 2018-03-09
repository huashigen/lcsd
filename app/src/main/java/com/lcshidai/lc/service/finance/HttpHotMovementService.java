package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.HotInfoMovementImpl;
import com.lcshidai.lc.model.finance.HotInfoMovementJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpHotMovementService implements HttpServiceURL {
    HotInfoMovementImpl impl;
    TRJActivity mpa;

    public HttpHotMovementService(TRJActivity mpa,
                                  HotInfoMovementImpl impl) {
        this.mpa = mpa;
        this.impl = impl;
    }

    public void gainHotInfo(String activity_status, String page, String pagesize) {

        RequestParams params = new RequestParams();
//		params.put("activity_status", activity_status);// prj_id 项目id
        params.put("page", page);
        params.put("pagesize", pagesize);
        mpa.post(GET_HOT_ACTIVITY_LIST, params,
                new BaseJsonHandler<HotInfoMovementJson>(mpa) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse,
                                          HotInfoMovementJson response) {
                        impl.gainHotMovementSuccess(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          HotInfoMovementJson errorResponse) {
                        impl.gainHotMovementFail();
                    }

                    @Override
                    protected HotInfoMovementJson parseResponse(
                            String rawJsonData, boolean isFailure)
                            throws Throwable {
                        super.parseResponse(rawJsonData, isFailure);
                        return new XHHMapper().readValues(
                                new JsonFactory().createParser(rawJsonData),
                                HotInfoMovementJson.class).next();
                    }

                });
    }
}
