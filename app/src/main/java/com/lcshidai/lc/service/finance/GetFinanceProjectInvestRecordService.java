package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.GetFinanceProjectInvestRecordImpl;
import com.lcshidai.lc.model.finance.FinanceProjectInvestRecordListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

/**
 * 获取项目投资记录
 */
public class GetFinanceProjectInvestRecordService implements HttpServiceURL {
    TRJActivity mpa;
    GetFinanceProjectInvestRecordImpl ai;

    public GetFinanceProjectInvestRecordService(TRJActivity mpa,
                                                GetFinanceProjectInvestRecordImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void getFinanceProjectInvestRecord(String mPrjId, String mP, int mPagesize, int isCollection) {
        if (null == mpa)
            return;
        RequestParams params = new RequestParams();
        params.put("prj_id", mPrjId);               // prj_id 项目id
        params.put("page", mP);                     // p 分页页数
        params.put("pagesize", mPagesize + "");     // 每页多少条 ，不传默认每页10条
        params.put("is_collection", isCollection);
        mpa.post(FINACE_ITEM_INVEST_RECORD, params,
                new BaseJsonHandler<FinanceProjectInvestRecordListJson>(mpa) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse,
                                          FinanceProjectInvestRecordListJson response) {
                        ai.getFinanceProjectInvestRecordSuccess(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          FinanceProjectInvestRecordListJson errorResponse) {
                        ai.getFinanceProjectInvestRecordFail();
                    }

                    @Override
                    protected FinanceProjectInvestRecordListJson parseResponse(
                            String rawJsonData, boolean isFailure)
                            throws Throwable {
                        super.parseResponse(rawJsonData, isFailure);
                        return new XHHMapper().readValues(
                                new JsonFactory().createParser(rawJsonData),
                                FinanceProjectInvestRecordListJson.class).next();
                    }

                });
    }
}
