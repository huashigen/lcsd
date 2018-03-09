package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.GoldFinanceRecordImpl;
import com.lcshidai.lc.model.account.GoldFinanceRecordJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpGoldFinanceRecordService implements HttpServiceURL {
    TRJActivity mpa;
    GoldFinanceRecordImpl ai;

    public HttpGoldFinanceRecordService(TRJActivity mpa,
                                        GoldFinanceRecordImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainGoldFinanceRecord(int mP, int mPagesize, boolean flagUrl) {
        if (null == mpa) return;
        RequestParams params = new RequestParams();

        params.put("p", String.valueOf(mP++));// p 分页页数
        params.put("page_size", mPagesize + "");// 每页多少条 ，不传默认每页10条
        mpa.post((flagUrl ? URL_LICAI : URL_TOUZI), params, new BaseJsonHandler<GoldFinanceRecordJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, GoldFinanceRecordJson response) {
                ai.gainGoldFinanceRecordsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  GoldFinanceRecordJson errorResponse) {
                ai.gainGoldFinanceRecordfail();
            }

            @Override
            protected GoldFinanceRecordJson parseResponse(String rawJsonData,
                                                          boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), GoldFinanceRecordJson.class).next();
            }

        });
    }
}
