package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.ResetPwdImpl;
import com.lcshidai.lc.model.account.ResetPwdJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.AesUtil;

public class HttpResetPwdService implements HttpServiceURL {
    TRJActivity mpa;
    ResetPwdImpl ai;

    public HttpResetPwdService(TRJActivity mpa, ResetPwdImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainResetPwd(String setType, String edit_pwd, String edit_pwd_re) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("set_type", setType);
        try {
            rp.put("password", AesUtil.getInstance().encrypt(edit_pwd));
            rp.put("password_repeat", AesUtil.getInstance().encrypt(edit_pwd_re));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mpa.post(RESET_PWD_URL, rp, new BaseJsonHandler<ResetPwdJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, ResetPwdJson response) {
                ai.gainResetPwdsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  ResetPwdJson errorResponse) {
                ai.gainResetPwdfail();
            }

            @Override
            protected ResetPwdJson parseResponse(String rawJsonData,
                                                 boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), ResetPwdJson.class).next();
            }

        });
    }
}
