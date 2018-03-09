package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.CheckPwdMobileCodeImpl;
import com.lcshidai.lc.model.account.CheckPwdMobileCodeJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpCheckPwdMobileCodeService implements HttpServiceURL {
    TRJActivity mpa;
    CheckPwdMobileCodeImpl ai;

    public HttpCheckPwdMobileCodeService(TRJActivity mpa, CheckPwdMobileCodeImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainCheckPwdMobileCode(String edit_account, String edit_dynamic_code) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("mobile", edit_account);
        rp.put("code", edit_dynamic_code);
        mpa.post(NEXT_TO_SECOND, rp, new BaseJsonHandler<CheckPwdMobileCodeJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, CheckPwdMobileCodeJson response) {
                ai.gainCheckPwdMobileCodesuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  CheckPwdMobileCodeJson errorResponse) {
                ai.gainCheckPwdMobileCodefail();
            }

            @Override
            protected CheckPwdMobileCodeJson parseResponse(String rawJsonData,
                                                           boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), CheckPwdMobileCodeJson.class).next();
            }

        });
    }
}
