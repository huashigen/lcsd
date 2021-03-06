package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.MyRewardRecordImpl;
import com.lcshidai.lc.model.account.MyRewardRecordJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpMyRewardRecordService implements HttpServiceURL {
    TRJActivity mpa;
    MyRewardRecordImpl ai;

    public HttpMyRewardRecordService(TRJActivity mpa, MyRewardRecordImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }


    public void gainMyRewardRecord(int currentPage, int pagesize) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("page", String.valueOf(currentPage));
        rp.put("pagesize", String.valueOf(pagesize));
        mpa.post(MY_BONUS_RECORD_URL, rp, new BaseJsonHandler<MyRewardRecordJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, MyRewardRecordJson response) {
                ai.gainMyRewardRecordsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  MyRewardRecordJson errorResponse) {
                ai.gainMyRewardRecordfail();
            }

            @Override
            protected MyRewardRecordJson parseResponse(String rawJsonData,
                                                       boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), MyRewardRecordJson.class).next();
            }

        });
    }

}
