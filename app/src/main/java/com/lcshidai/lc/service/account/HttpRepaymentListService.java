package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.RepaymentListImpl;
import com.lcshidai.lc.model.account.RepaymentListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpRepaymentListService implements HttpServiceURL {
    TRJActivity mpa;
    RepaymentListImpl ai;

    public HttpRepaymentListService(TRJActivity mpa, RepaymentListImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainRepaymentList(String mId) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("order_id", mId);
        mpa.post(ORDER_REPAY_URL, rp, new BaseJsonHandler<RepaymentListJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, RepaymentListJson response) {
                ai.gainRepaymentListsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  RepaymentListJson errorResponse) {
                ai.gainRepaymentListfail();
            }

            @Override
            protected RepaymentListJson parseResponse(String rawJsonData,
                                                      boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), RepaymentListJson.class).next();
            }

        });
    }
}
