package com.lcshidai.lc.service.licai;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.licai.ModifyAreaImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

/**
 * 修改区域接口
 */
public class HttpModifyAreaService implements HttpServiceURL {
    TRJActivity mpa;
    ModifyAreaImpl ai;

    public HttpModifyAreaService(TRJActivity mpa, ModifyAreaImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    /**
     * 基金账户登录
     *
     * @param access_token access_token
     * @param user_token   user_token
     * @param province     省份
     * @param city         城市
     */
    public void modifyArea(String access_token, String user_token, String province, String city) {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.put("access_token", access_token);
        rp.put("user_token", user_token);
        rp.put("province", province);
        rp.put("city", city);
        LCHttpClient.postWithFullUrl(mpa, MODIFY_AREA, rp, new BaseJsonHandler<BaseJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseJson response) {
                ai.modifyAreaSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.modifyAreaFailed(errorResponse);
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
            }

        });
    }

}
