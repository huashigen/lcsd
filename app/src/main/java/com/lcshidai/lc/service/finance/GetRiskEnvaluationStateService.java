package com.lcshidai.lc.service.finance;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.finance.GetProjectBaseInfoImpl;
import com.lcshidai.lc.impl.finance.GetRiskEnvaluationStateImpl;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoJson;
import com.lcshidai.lc.model.finance.RiskEnvaluationStateJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class GetRiskEnvaluationStateService implements HttpServiceURL {
    GetRiskEnvaluationStateImpl impl;
    TRJActivity mpa;

    public GetRiskEnvaluationStateService(TRJActivity mpa,
                                          GetRiskEnvaluationStateImpl impl) {
        this.mpa = mpa;
        this.impl = impl;
    }

    /**
     * 获取项目详情之基本信息
     *
     */
    public void getRiskEnvaluationState() {
        RequestParams params = new RequestParams();
        mpa.post(IS_INVEST_RISK_ENVALUATION, params,
                new BaseJsonHandler<RiskEnvaluationStateJson>(mpa) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse,
                                          RiskEnvaluationStateJson response) {
                        impl.getRiskEnvaluationStateSuccess(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          RiskEnvaluationStateJson errorResponse) {
                        impl.getRiskEnvaluationStateFail();
                    }

                    @Override
                    protected RiskEnvaluationStateJson parseResponse(
                            String rawJsonData, boolean isFailure)
                            throws Throwable {
                        super.parseResponse(rawJsonData, isFailure);
                        return new XHHMapper().readValues(
                                new JsonFactory().createParser(rawJsonData),
                                RiskEnvaluationStateJson.class).next();
                    }

                });
    }
}
