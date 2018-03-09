package com.lcshidai.lc.service.finance;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.finance.GetFinanceProjectBorrowerRecordImpl;
import com.lcshidai.lc.model.finance.borrower.FinanceProjectInvestBorrowerListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * 获取项目投资记录
 */
public class GetFinanceProjectBorrowerRecordService implements HttpServiceURL {
    TRJActivity mpa;
    GetFinanceProjectBorrowerRecordImpl ai;

    public GetFinanceProjectBorrowerRecordService(TRJActivity mpa,
                                                  GetFinanceProjectBorrowerRecordImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void getFinanceProjectInvestRecord(String mPrjId, String mP, int mPagesize) {
        if (null == mpa)
            return;
        RequestParams params = new RequestParams();
        params.put("prj_id", mPrjId);               // prj_id 项目id
        params.put("page", mP);                     // p 分页页数
        params.put("pagesize", mPagesize + "");     // 每页多少条 ，不传默认每页10条
        mpa.post(FINACE_ITEM_BORROWER_RECORD, params,
                new BaseJsonHandler<FinanceProjectInvestBorrowerListJson>(mpa) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse,
                                          FinanceProjectInvestBorrowerListJson response) {
                        ai.getFinanceProjectBorrowerRecordSuccess(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          FinanceProjectInvestBorrowerListJson errorResponse) {
                        ai.getFinanceProjectBorrowerRecordFail();
                    }

                    @Override
                    protected FinanceProjectInvestBorrowerListJson parseResponse(
                            String rawJsonData, boolean isFailure)
                            throws Throwable {
                        super.parseResponse(rawJsonData, isFailure);
                        return new XHHMapper().readValues(
                                new JsonFactory().createParser(rawJsonData),
                                FinanceProjectInvestBorrowerListJson.class).next();
                    }

                });
    }
}
