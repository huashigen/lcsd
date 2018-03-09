package com.lcshidai.lc.service.licai;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.licai.RiskTestImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.licai.RiskTestQuestionJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpRiskTestService implements HttpServiceURL {
    TRJActivity mpa;
    RiskTestImpl ai;

    public HttpRiskTestService(TRJActivity mpa, RiskTestImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    /**
     * 获取分线测试问题列表
     * @param access_token access_token
     * @param user_token user_token
     */
    public void getRiskTestQuestionList(String access_token, String user_token) {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();

        rp.put("access_token", access_token);
        rp.put("user_token", user_token);
        LCHttpClient.postWithFullUrl(mpa, GET_RISK_TEST_QUESTION_LIST, rp, new BaseJsonHandler<RiskTestQuestionJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, RiskTestQuestionJson response) {
                ai.getRiskTestQuestionListSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  RiskTestQuestionJson errorResponse) {
                ai.getRiskTestQuestionListFail();
            }

            @Override
            protected RiskTestQuestionJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), RiskTestQuestionJson.class).next();
            }

        });
    }

    /**
     * 保存答题答案
     *
     * @param access_token access_token
     * @param user_token   user_token
     * @param question_ids 问题ID集合字符串,以逗号隔开
     * @param answer_ids   答案ID集合字符串,以逗号隔开
     */
    public void saveRiskTestAnswer(String access_token, String user_token, String question_ids, String answer_ids) {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.put("access_token", access_token);
        rp.put("user_token", user_token);
        rp.put("question_ids", question_ids);
        rp.put("answer_ids", answer_ids);

        LCHttpClient.postWithFullUrl(mpa, SAVE_RISK_TEST_ANSWER, rp, new BaseJsonHandler<BaseJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseJson response) {
                ai.saveRiskTestAnswerSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.saveRiskTestAnswerFail(errorResponse);
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
            }

        });
    }


}
