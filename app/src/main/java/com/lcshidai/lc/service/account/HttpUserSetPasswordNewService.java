package com.lcshidai.lc.service.account;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserSetPasswordImpl;
import com.lcshidai.lc.model.account.UserRegisterSecJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.AesUtil;

import org.apache.http.Header;

public class HttpUserSetPasswordNewService implements HttpServiceURL {
    TRJActivity mpa;
    UserSetPasswordImpl ai;

    public HttpUserSetPasswordNewService(TRJActivity mpa, UserSetPasswordImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void setUserPassword(String password, String passwordAgain, String mobile) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        try {
            rp.put("password", AesUtil.getInstance().encrypt(password));
            rp.put("password_repeat", AesUtil.getInstance().encrypt(passwordAgain));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rp.put("mobile", mobile);
        String deviceid = ((TelephonyManager) mpa.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        rp.put("deviceid", deviceid);
        mpa.post(SETPWDNEW, rp, new BaseJsonHandler<UserRegisterSecJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, UserRegisterSecJson response) {
                ai.setPasswordSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  UserRegisterSecJson errorResponse) {
                ai.setPasswordFail();
            }

            @Override
            protected UserRegisterSecJson parseResponse(String rawJsonData,
                                                        boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), UserRegisterSecJson.class).next();
            }

        });
    }
}
