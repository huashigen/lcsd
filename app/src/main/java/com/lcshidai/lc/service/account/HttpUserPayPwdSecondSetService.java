package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserPayPwdSecondSetImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.AesUtil;

public class HttpUserPayPwdSecondSetService implements HttpServiceURL {
    TRJActivity mpa;
    UserPayPwdSecondSetImpl ai;

    public HttpUserPayPwdSecondSetService(TRJActivity mpa,
                                          UserPayPwdSecondSetImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUserPayPwdSecondSet(String firstPwd, String inputPwd) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("set_type", "first");
        try {
            rp.put("password", AesUtil.getInstance().encrypt(firstPwd));
            rp.put("password_repeat", AesUtil.getInstance().encrypt(inputPwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mpa.post(URL_PAY_PWD_UPDATE, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainUserPayPwdSecondSetsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainUserPayPwdSecondSetfail();
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
