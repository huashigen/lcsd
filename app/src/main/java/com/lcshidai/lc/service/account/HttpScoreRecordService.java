package com.lcshidai.lc.service.account;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.ScoreRecordImpl;
import com.lcshidai.lc.model.account.ScoreRecordJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpScoreRecordService implements HttpServiceURL {
    TRJActivity mpa;
    ScoreRecordImpl ai;

    public HttpScoreRecordService(TRJActivity mpa, ScoreRecordImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void getScoreRecordList(int page, int pageSize) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("page", String.valueOf(page));
        rp.put("pagesize", String.valueOf(pageSize));
        LCHttpClient.get(mpa, SCORE_RECORD_LIST, rp, new BaseJsonHandler<ScoreRecordJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                  ScoreRecordJson response) {
                ai.onGetScoreListSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  String rawJsonData, ScoreRecordJson errorResponse) {
                ai.onGetScoreListFailed();
            }

            @Override
            protected ScoreRecordJson parseResponse(String rawJsonData, boolean isFailure)
                    throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData),
                        ScoreRecordJson.class).next();
            }

        });
    }


}
