package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.BespeakApplyImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpBespeakApplyService implements HttpServiceURL {
    TRJActivity mpa;
    BespeakApplyImpl ai;

    public HttpBespeakApplyService(TRJActivity mpa,
                                   BespeakApplyImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainBespeakApply(String pwd, String bespeak_id, String min_time, String min_money,
                                 String prj_type_a, String prj_type_b, String prj_type_f,
                                 String prj_type_h, boolean flag) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        if (flag) {
            rp.put("safe_pwd", pwd);
            rp.put("appoint_rate", bespeak_id);
            rp.put("appoint_day", min_time);
            rp.put("appoint_money", min_money);
            rp.put("prj_type_a", prj_type_a);
            rp.put("prj_type_b", prj_type_b);
            rp.put("prj_type_f", prj_type_f);
            rp.put("prj_type_h", prj_type_h);
            rp.put("is_agree_agreement", "1");
        } else {

            rp.put("safe_pwd", pwd);
            rp.put("re_apply_id", bespeak_id);
        }
        mpa.post(BESPEAK_APPLY_URL, rp, new BaseJsonHandler<BaseJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, BaseJson response) {
                ai.gainBespeakApplysuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.gainBespeakApplyfail();
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
