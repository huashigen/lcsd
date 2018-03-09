package com.lcshidai.lc.service.finance;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.GuaranteeIconImpl;
import com.lcshidai.lc.model.finance.GuaranteeIconJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpGuaranteeIconService implements HttpServiceURL {
    TRJActivity mpa;
    GuaranteeIconImpl ai;

    public HttpGuaranteeIconService(TRJActivity mpa, GuaranteeIconImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainGuaranteeIcons(String mPrjId) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("prj_id", mPrjId);// prj_id 项目id  prj_id
        mpa.post(GET_GUARANTEE_ICONS, rp, new BaseJsonHandler<GuaranteeIconJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, GuaranteeIconJson response) {
                ai.gainGuaranteeIconSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  GuaranteeIconJson errorResponse) {
                ai.gainGuaranteeIconFail();
            }

            @Override
            protected GuaranteeIconJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), GuaranteeIconJson.class).next();
            }

        });
    }
}
