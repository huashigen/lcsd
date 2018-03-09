package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.GetProjectBaseInfoImpl;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class GetFinanceProjectBaseInfoService implements HttpServiceURL {
    GetProjectBaseInfoImpl impl;
    TRJActivity mpa;

    public GetFinanceProjectBaseInfoService(TRJActivity mpa,
                                            GetProjectBaseInfoImpl impl) {
        this.mpa = mpa;
        this.impl = impl;
    }

    /**
     * 获取项目详情之基本信息
     *
     * @param mPrjId 项目ID
     */
    public void getProBaseInfo(String mPrjId, int isCollection) {
        RequestParams params = new RequestParams();
        params.put("prj_id", mPrjId);// prj_id 项目id
        params.put("is_collection", isCollection);
        mpa.post(GET_PROJECT_BASE_INFO, params,
                new BaseJsonHandler<FinanceItemBaseInfoJson>(mpa) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse,
                                          FinanceItemBaseInfoJson response) {
                        impl.getProjectBaseInfoSuccess(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          FinanceItemBaseInfoJson errorResponse) {
                        impl.getProjectBaseInfoFail();
                    }

                    @Override
                    protected FinanceItemBaseInfoJson parseResponse(
                            String rawJsonData, boolean isFailure)
                            throws Throwable {
                        super.parseResponse(rawJsonData, isFailure);
                        return new XHHMapper().readValues(
                                new JsonFactory().createParser(rawJsonData),
                                FinanceItemBaseInfoJson.class).next();
                    }

                });
    }
}
