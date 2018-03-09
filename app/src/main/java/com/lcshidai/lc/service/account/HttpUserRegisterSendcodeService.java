package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserRegisterSendcodeImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpUserRegisterSendcodeService implements HttpServiceURL {
    public static final int FORGET_PWD_URL_FLAG = 0;
    public static final int REGISTER_PWD_URL_FLAG = 1;
    TRJActivity mpa;
    UserRegisterSendcodeImpl ai;

    private boolean isActivity;

    public HttpUserRegisterSendcodeService(TRJActivity mpa,
                                           UserRegisterSendcodeImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
        isActivity = false;
    }

    /**
     * 设置为活动用户
     */
    public void isActivity() {
        isActivity = true;
    }

    public void gainUserRegisterSendcode(String mMobile, String vcode, int flag) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("mobile", mMobile);
        rp.put("vcode", vcode);
        String url = "";
        if (isActivity) {
            url = GET_ACT_USER_SMS_CODE;// 活动用户获取验证码接口
        } else {
            url = flag == REGISTER_PWD_URL_FLAG ? SEND_CODE : SMS_CODE;
        }
        mpa.post(url, rp, new BaseJsonHandler<BaseJson>(mpa, false) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.getRegisterSmsCodeSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.getRegisterSmsCodeFailed();
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
