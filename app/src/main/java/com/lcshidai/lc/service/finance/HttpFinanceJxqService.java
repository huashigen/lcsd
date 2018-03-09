package com.lcshidai.lc.service.finance;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.FinanceJxqImpl;
import com.lcshidai.lc.model.finance.FinanceJxqJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpFinanceJxqService implements HttpServiceURL {

    FinanceJxqImpl impl;
    TRJActivity mActivity;

    public HttpFinanceJxqService(FinanceJxqImpl impl, TRJActivity mActivity) {
        this.impl = impl;
        this.mActivity = mActivity;
    }

    public void getJxqList(String prj_id, String amount, int isCollection) {
        RequestParams rp = new RequestParams();
        rp.put("prj_id", prj_id);
        rp.put("amount", amount);
        rp.put("is_collection", isCollection);
        mActivity.post(GET_PRJ_JXQ_LIST, rp, new BaseJsonHandler<FinanceJxqJson>(mActivity) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                  FinanceJxqJson response) {
                impl.gainJxqListSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  String rawJsonData, FinanceJxqJson errorResponse) {
                impl.gainJxqListFail();
            }

            @Override
            protected FinanceJxqJson parseResponse(String rawJsonData, boolean isFailure)
                    throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(
                        new JsonFactory().createParser(rawJsonData), FinanceJxqJson.class).next();
            }

        });

    }
}
