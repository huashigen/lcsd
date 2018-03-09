package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserMailUpdaterImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.AesUtil;

public class HttpUserMailUpdaterService implements HttpServiceURL {
    TRJActivity mpa;
    UserMailUpdaterImpl ai;

    public HttpUserMailUpdaterService(TRJActivity mpa,
                                      UserMailUpdaterImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUserMailUpdater(String mail, String pwd) {
        if (null == mpa) return;

        RequestParams rq = new RequestParams();
        rq.put("email", mail);
        try {
            rq.put("pwd", AesUtil.getInstance().encrypt(pwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mpa.post(URL_SEND_MAIL_DATA, rq, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainUserMailUpdatersuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainUserMailUpdaterfail();
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData,
                                             boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
            }

        });
    }
}
