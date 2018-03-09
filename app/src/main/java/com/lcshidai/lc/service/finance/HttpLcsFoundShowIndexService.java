package com.lcshidai.lc.service.finance;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.LcsShowIndexImpl;
import com.lcshidai.lc.model.finance.lcs.LcsShowIndexJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

/**
 * @author 001355
 * @Description: TODO
 */
public class HttpLcsFoundShowIndexService implements HttpServiceURL {
    TRJActivity mpa;
    LcsShowIndexImpl ai;

    public HttpLcsFoundShowIndexService(TRJActivity mpa, LcsShowIndexImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainLcsList(String access_token, String user_token, String fund_type, String fund_name, String page_no) {
        if (null == mpa)
            return;
        RequestParams params = new RequestParams();
        params.put("access_token", access_token);
        params.put("user_token", user_token);
        params.put("fund_type", fund_type);
        params.put("fund_name", fund_name);
        params.put("page_no", page_no);
        LCHttpClient.postWithFullUrl(mpa, GET_FUND_LIST, params,
                new BaseJsonHandler<LcsShowIndexJson>(mpa) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse,
                                          LcsShowIndexJson response) {
                        ai.gainLcsShowIndexSuccess(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          LcsShowIndexJson errorResponse) {
                        ai.gainLcsShowIndexFail();
                    }

                    @Override
                    protected LcsShowIndexJson parseResponse(
                            String rawJsonData, boolean isFailure)
                            throws Throwable {
                        super.parseResponse(rawJsonData, isFailure);
                        return new XHHMapper().readValues(
                                new JsonFactory().createParser(rawJsonData),
                                LcsShowIndexJson.class).next();
                    }

                });
    }
}
