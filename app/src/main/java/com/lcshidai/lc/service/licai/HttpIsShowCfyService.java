package com.lcshidai.lc.service.licai;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.licai.IsShowCfyImpl;
import com.lcshidai.lc.model.licai.IsShowCfyJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpIsShowCfyService implements HttpServiceURL {
    TRJActivity mpa;
    IsShowCfyImpl ai;

    public HttpIsShowCfyService(TRJActivity mpa, IsShowCfyImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    /**
     * 是否显示长富云部分
     */
    public void isShowCfy() {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();
        mpa.post(IS_CFY_SHOW, rp, new BaseJsonHandler<IsShowCfyJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, IsShowCfyJson response) {
                ai.isShowCfySuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  IsShowCfyJson errorResponse) {
                ai.isShowCfyFailed(errorResponse);
            }

            @Override
            protected IsShowCfyJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), IsShowCfyJson.class).next();
            }

        });
    }

}
