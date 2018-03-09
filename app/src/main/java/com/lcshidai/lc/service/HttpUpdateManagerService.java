package com.lcshidai.lc.service;

import android.content.Context;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.UpdateManagerImpl;
import com.lcshidai.lc.model.pub.UpdateManagerJson;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpUpdateManagerService implements HttpServiceURL {
    TRJActivity mpa;
    UpdateManagerImpl ai;

    public HttpUpdateManagerService(TRJActivity mpa, UpdateManagerImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUpdateManager(String mVer, final Context context, final int flag) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("device", "2");
        rp.put("version", "1.0.0");
        mpa.post(CHECK_UPDATE, rp, new BaseJsonHandler<UpdateManagerJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, UpdateManagerJson response) {
                ai.gainUpdateManagersuccess(response, context, flag);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  UpdateManagerJson errorResponse) {
                ai.gainUpdateManagerfail();
            }

            @Override
            protected UpdateManagerJson parseResponse(String rawJsonData,
                                                      boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), UpdateManagerJson.class).next();
            }

        });
    }
}
