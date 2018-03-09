package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.GesturePwdImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.AesUtil;

public class HttpGesturePwdService implements HttpServiceURL {
    TRJActivity mpa;
    GesturePwdImpl ai;

    public HttpGesturePwdService(TRJActivity mpa,
                                 GesturePwdImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainGesturePwd(String uname, String pwd) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("account", uname);
        try {
            rp.put("password", AesUtil.getInstance().encrypt(pwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mpa.post(URL_CHECK_PWD, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainGesturePwdsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainGesturePwdfail();
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
