package com.lcshidai.lc.service.account;

import android.content.Intent;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.BroadCastImpl;
import com.lcshidai.lc.impl.account.LoginImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.BaseLoginJson;
import com.lcshidai.lc.model.account.BaseLoginStrJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.AesUtil;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.LoginStatusHelper;
import com.lcshidai.lc.utils.MemorySave;

import org.apache.http.Header;

public class HttpLoginService implements HttpServiceURL {
    TRJActivity mpa;
    LoginImpl ai;

    public HttpLoginService(TRJActivity mpa, LoginImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainBaseLogin(String account, String pwd, String code) {
        MemorySave.MS.mIsLogining = true;
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("mobile", account);
        try {
            rp.put("password", AesUtil.getInstance().encrypt(pwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rp.put("code", code);
        rp.put("deviceid", CommonUtil.getDeviceId(mpa));
        mpa.post(LOGIN, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.loginSuccess(response);

                Intent intent = new Intent();
                intent.setAction(BroadCastImpl.ACTION_LOGIN_SUCCEED_OR_FAILED);
                intent.putExtra("loginSucceed", true);
                mpa.sendBroadcast(intent);
                MemorySave.MS.mIsLogining = false;
                LoginStatusHelper.LoginStatusChange = true;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.loginFailed();

                Intent intent = new Intent();
                intent.setAction(BroadCastImpl.ACTION_LOGIN_SUCCEED_OR_FAILED);
                intent.putExtra("loginSucceed", false);
                mpa.sendBroadcast(intent);
                MemorySave.MS.mIsLogining = false;
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData,
                                             boolean isFailure) throws Throwable {
                BaseJson baseJson = super.parseResponse(rawJsonData, isFailure);
                if (baseJson.getBoolen().equals("1")) {
                    return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseLoginJson.class).next();
                } else {
                    return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseLoginStrJson.class).next();
                }
            }

        });
    }
}
