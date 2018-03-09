package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.MyRewardRuleImpl;
import com.lcshidai.lc.model.account.MyRewardRuleJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpMyRewardRuleService implements HttpServiceURL {
    TRJActivity mpa;
    MyRewardRuleImpl ai;

    public HttpMyRewardRuleService(TRJActivity mpa, MyRewardRuleImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainMyRewardRule(String rule_id, String obj_type, String obj_id) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("rule_id", rule_id);
        rp.put("obj_type", obj_type);
        rp.put("obj_id", obj_id);
        mpa.post(MY_BONUS_RULE_URL, rp, new BaseJsonHandler<MyRewardRuleJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, MyRewardRuleJson response) {
                ai.gainMyRewardRulesuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  MyRewardRuleJson errorResponse) {
                ai.gainMyRewardRulefail();
            }

            @Override
            protected MyRewardRuleJson parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), MyRewardRuleJson.class).next();
            }

        });
    }
}
