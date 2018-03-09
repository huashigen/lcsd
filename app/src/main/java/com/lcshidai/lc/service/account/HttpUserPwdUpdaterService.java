package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserPwdUpdaterImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.AesUtil;

public class HttpUserPwdUpdaterService implements HttpServiceURL {
    TRJActivity mpa;
    UserPwdUpdaterImpl ai;
    private boolean flag = false;

    public HttpUserPwdUpdaterService(TRJActivity mpa,
                                     UserPwdUpdaterImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUserPwdUpdater(String old_pwd, String new_pwd, String again_pwd, int mMode) {
        if (null == mpa) return;
        RequestParams rq = new RequestParams();
        try {
            rq.put("oldPwd", AesUtil.getInstance().encrypt(old_pwd));
            rq.put("pwd1", AesUtil.getInstance().encrypt(new_pwd));
            rq.put("pwd2", AesUtil.getInstance().encrypt(again_pwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url;
        if (mMode == 1) {
            url = UPDATE_LOGIN_PSW;
        } else {
            url = UPDATE_PAY_PSW;
        }
        mpa.post(url, rq, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainUserPwdUpdatersuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainUserPwdUpdaterfail();
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
