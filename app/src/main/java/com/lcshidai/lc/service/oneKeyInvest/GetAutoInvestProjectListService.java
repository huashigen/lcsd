package com.lcshidai.lc.service.oneKeyInvest;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.onKeyInvest.GetAutoInvestProjectListImpl;
import com.lcshidai.lc.model.oneKeyInvest.GetAutoInvestProjectListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

/**
 * 获取集合表项目列表
 */
public class GetAutoInvestProjectListService implements HttpServiceURL {
    TRJActivity mpa;
    GetAutoInvestProjectListImpl ai;

    public GetAutoInvestProjectListService(TRJActivity mpa, GetAutoInvestProjectListImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void getAutoInvestProjectList(String collect_id) {

        if (null == mpa)
            return;
        RequestParams rp = new RequestParams();
        rp.put("collect_id", collect_id);
        mpa.post(GET_COLLECTION_PROJECTS, rp, new BaseJsonHandler<GetAutoInvestProjectListJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse,
                                  GetAutoInvestProjectListJson response) {
                ai.getAutoInvestProListSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  GetAutoInvestProjectListJson errorResponse) {
                ai.getAutoInvestProListFailed();
            }

            @Override
            protected GetAutoInvestProjectListJson parseResponse(
                    String rawJsonData, boolean isFailure)
                    throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(
                        new JsonFactory().createParser(rawJsonData),
                        GetAutoInvestProjectListJson.class).next();
            }

        });
    }
}