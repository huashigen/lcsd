package com.lcshidai.lc.service.licai;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.licai.ModifyAuditImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

/**
 * 修改合格认证条件接口
 */
public class HttpModifyAuditService implements HttpServiceURL {
    TRJActivity mpa;
    ModifyAuditImpl ai;

    public HttpModifyAuditService(TRJActivity mpa, ModifyAuditImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    /**
     * 基金账户登录
     *
     * @param access_token access_token
     * @param user_token   user_token
     * @param lcs_audit    lcs_audit
     */
    public void modifyAudit(String access_token, String user_token, String lcs_audit) {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.put("access_token", access_token);
        rp.put("user_token", user_token);
        rp.put("lcs_audit", lcs_audit);
        LCHttpClient.postWithFullUrl(mpa, MODIFY_AUDIT, rp, new BaseJsonHandler<BaseJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseJson response) {
                ai.modifyAuditSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.modifyAuditFailed(errorResponse);
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
            }

        });
    }

}
