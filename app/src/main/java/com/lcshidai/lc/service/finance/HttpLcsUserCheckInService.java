package com.lcshidai.lc.service.finance;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.LcsUserCheckInImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

/**
 * @author 001355
 * @Description: TODO
 */
public class HttpLcsUserCheckInService implements HttpServiceURL {
    TRJActivity mpa;
    LcsUserCheckInImpl ai;

    public HttpLcsUserCheckInService(TRJActivity mpa, LcsUserCheckInImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void userCheckIn() {
        if (null == mpa)
            return;
        RequestParams params = new RequestParams();
        mpa.post(LCS_USER_CHECKIN, params,
                new BaseJsonHandler<BaseJson>(mpa) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse,
                                          BaseJson response) {
                        ai.userCheckInSuccess(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseJson errorResponse) {
                        ai.userCheckInFail();
                    }

                    @Override
                    protected BaseJson parseResponse(
                            String rawJsonData, boolean isFailure)
                            throws Throwable {
                        super.parseResponse(rawJsonData, isFailure);
                        return new XHHMapper().readValues(
                                new JsonFactory().createParser(rawJsonData),
                                BaseJson.class).next();
                    }

                });
    }
}
