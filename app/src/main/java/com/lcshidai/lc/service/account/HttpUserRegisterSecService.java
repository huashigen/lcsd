package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserRegisterSecImpl;
import com.lcshidai.lc.model.account.UserRegisterSecJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.AesUtil;
import com.lcshidai.lc.utils.CommonUtil;

public class HttpUserRegisterSecService implements HttpServiceURL {
    TRJActivity mpa;
    UserRegisterSecImpl ai;

    public HttpUserRegisterSecService(TRJActivity mpa,
                                      UserRegisterSecImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUserRegisterSec(String mPwd, String mPwdAgain, String mMobile, String mDyCode, String mRedCode) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        try {
            rp.put("password", AesUtil.getInstance().encrypt(mPwd));
            rp.put("password_repeat", AesUtil.getInstance().encrypt(mPwdAgain));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rp.put("mobile", mMobile);
        rp.put("code", mDyCode);
        rp.put("username", "");
        rp.put("hongbaoCode", mRedCode);
        rp.put("deviceid", CommonUtil.getDeviceId(mpa));
        mpa.post(REGISTER_URL, rp, new BaseJsonHandler<UserRegisterSecJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, UserRegisterSecJson response) {
                ai.gainUserRegisterSecsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  UserRegisterSecJson errorResponse) {
                ai.gainUserRegisterSecfail();
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
