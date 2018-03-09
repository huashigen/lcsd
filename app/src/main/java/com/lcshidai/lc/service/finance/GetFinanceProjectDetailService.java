package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.FinanceInfoImpl;
import com.lcshidai.lc.model.finance.FinanceInfoJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

/**
 * 获取项目具体信息
 */
public class GetFinanceProjectDetailService implements HttpServiceURL {
    TRJActivity mpa;
    FinanceInfoImpl ai;

    public GetFinanceProjectDetailService(TRJActivity mpa, FinanceInfoImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    /**
     * 获取项目详情
     *
     * @param mPrjId 项目ID
     */
    public void getFinanceProjectDetail(String mPrjId, int isCollection) {
        if (null == mpa)
            return;
        RequestParams params = new RequestParams();
        params.put("prj_id", mPrjId);// prj_id 项目id  prj_id
        params.put("is_collection", isCollection);
        mpa.post(GET_PROJECT_DETAIL, params,
                new BaseJsonHandler<FinanceInfoJson>(mpa) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse,
                                          FinanceInfoJson response) {
                        ai.getFinanceProjectDetailSuccess(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          FinanceInfoJson errorResponse) {
                        ai.getFinanceProjectDetailFail();
                    }

                    @Override
                    protected FinanceInfoJson parseResponse(
                            String rawJsonData, boolean isFailure)
                            throws Throwable {
                        super.parseResponse(rawJsonData, isFailure);
                        return new XHHMapper().readValues(
                                new JsonFactory().createParser(rawJsonData),
                                FinanceInfoJson.class).next();
                    }

                });
    }
}
