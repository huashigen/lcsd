package com.lcshidai.lc.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.GetHomePopImpl;
import com.lcshidai.lc.model.GetHomePopJson;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpGetHomePopService implements HttpServiceURL {
    TRJActivity mpa;
    GetHomePopImpl ai;

    public HttpGetHomePopService(TRJActivity mpa, GetHomePopImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    /**
     * 获取首页弹窗信息
     * @param device_id    设备唯一标识
     */
    public void getHomePopMessage(String device_id) {

        if (null == mpa)
            return;
        RequestParams rq = new RequestParams();
        rq.put("device_id", device_id);
        mpa.post(GET_HOME_POP_MESSAGE, rq, new BaseJsonHandler<GetHomePopJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, GetHomePopJson response) {
                ai.getHomePopSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  GetHomePopJson errorResponse) {
                ai.getHomePopFail();
            }

            @Override
            protected GetHomePopJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), GetHomePopJson.class).next();
            }

        });
    }
}