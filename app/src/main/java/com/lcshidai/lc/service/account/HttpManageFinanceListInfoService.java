package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.ManageFinanceListInfoImpl;
import com.lcshidai.lc.model.account.ManageFinanceListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpManageFinanceListInfoService implements HttpServiceURL {
    TRJActivity mpa;
    ManageFinanceListInfoImpl ai;

    public HttpManageFinanceListInfoService(TRJActivity mpa,
                                            ManageFinanceListInfoImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainManageFinanceListInfo(boolean isTransfer, int mCurrentP, String type) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        if (!isTransfer) {
            rp.put("prj_type", mCurrentP + "");// prjtype 产品类型 1-短期宝 2-企益升 3-稳益保 4-抵益融
            rp.put("type", type);
        }
        String urlstr = isTransfer ? CASH_COUNT_URL : GET_COUNTS_BY_STATYS_URL;
        mpa.post(urlstr, rp, new BaseJsonHandler<ManageFinanceListJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, ManageFinanceListJson response) {
                ai.gainFundListsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  ManageFinanceListJson errorResponse) {
                ai.gainFundListfail();
            }

            @Override
            protected ManageFinanceListJson parseResponse(String rawJsonData,
                                                          boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), ManageFinanceListJson.class).next();
            }

        });
    }
}
