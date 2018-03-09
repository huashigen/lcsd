package com.lcshidai.lc.service.finance;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.LcsUserAlerImpl;
import com.lcshidai.lc.model.finance.lcs.LcsUserAlerJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

/**
 * @author 001355
 * @Description: TODO
 */
public class HttpLcsUserAlertService implements HttpServiceURL {
    TRJActivity mpa;
    LcsUserAlerImpl ai;

    public HttpLcsUserAlertService(TRJActivity mpa, LcsUserAlerImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void getUserAlert() {
        if (null == mpa)
            return;
        RequestParams params = new RequestParams();
        mpa.post(LCS_USER_ALERT, params,
                new BaseJsonHandler<LcsUserAlerJson>(mpa) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse,
                                          LcsUserAlerJson response) {
                        ai.gainLcsUserAlerSuccess(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          LcsUserAlerJson errorResponse) {
                        ai.gainLcsUserAlerFail();
                    }

                    @Override
                    protected LcsUserAlerJson parseResponse(
                            String rawJsonData, boolean isFailure)
                            throws Throwable {
                        super.parseResponse(rawJsonData, isFailure);
                        return new XHHMapper().readValues(
                                new JsonFactory().createParser(rawJsonData),
                                LcsUserAlerJson.class).next();
                    }

                });
    }
}
