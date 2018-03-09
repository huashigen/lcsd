package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.UserPayPwdSetImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.AesUtil;

public class HttpUserPayPwdSetService implements HttpServiceURL {
    TRJActivity mpa;
    UserPayPwdSetImpl ai;

    public HttpUserPayPwdSetService(TRJActivity mpa,
                                    UserPayPwdSetImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainUserPayPwdSet(String old_pwd, String new_pwd, String again_pwd, int flag) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        if (flag == 1) {
            rp.put("set_type", "oldpasswd");
            try {
                rp.put("password_old", AesUtil.getInstance().encrypt(old_pwd));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            rp.put("set_type", "first");
        }
        try {
            rp.put("password", AesUtil.getInstance().encrypt(new_pwd));
            rp.put("password_repeat", AesUtil.getInstance().encrypt(again_pwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mpa.post(URL_PAY_PWD_UPDATE, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainUserPayPwdSetsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainUserPayPwdSetfail();
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
