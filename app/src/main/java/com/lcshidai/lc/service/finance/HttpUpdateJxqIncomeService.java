package com.lcshidai.lc.service.finance;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.UpdateJxqIncomeImpl;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpUpdateJxqIncomeService implements HttpServiceURL {

    UpdateJxqIncomeImpl impl;
    TRJActivity mpa;

    public HttpUpdateJxqIncomeService(TRJActivity mpa, UpdateJxqIncomeImpl impl) {
        this.mpa = mpa;
        this.impl = impl;
    }

    public void updateJxqIncome(final String va, final String prj_id, String jxq_id, final boolean isRepay) {
        RequestParams rp = new RequestParams();
        rp.put("money", va);
        rp.put("prjid", prj_id);
        rp.put("is_pre_buy", isRepay ? "1" : "0");
        rp.put("addInterestId", jxq_id);
        mpa.post(NORMAL_PRJ_INVEST_CHECK, rp, new BaseJsonHandler<FinanceInvestPBuyCheckJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, FinanceInvestPBuyCheckJson response) {
                impl.updateDateJxqIncomeSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  FinanceInvestPBuyCheckJson errorResponse) {
                try {
                    JSONObject json = new JSONObject(rawJsonData);
                    if (null != json) {
                        String message = json.getString("message");
                        impl.updateDateJxqIncomeFail(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected FinanceInvestPBuyCheckJson parseResponse(
                    String rawJsonData, boolean isFailure)
                    throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(
                        new JsonFactory().createParser(rawJsonData),
                        FinanceInvestPBuyCheckJson.class).next();
            }
        });
    }
}
