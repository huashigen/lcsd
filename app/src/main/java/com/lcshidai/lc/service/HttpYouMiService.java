package com.lcshidai.lc.service;

import org.apache.http.Header;

import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;

/**
 * 有米广告请求
 *
 * @author Administrator
 */
public class HttpYouMiService implements HttpServiceURL {
    TRJActivity mpa;

    public HttpYouMiService(TRJActivity mpa) {
        this.mpa = mpa;
    }

    public void sendYouMi() {
        if (null == mpa)
            return;
        String imei = CommonUtil.getDeviceId(mpa);
        RequestParams param = new RequestParams();
        param.put("imei", imei);
        // 不用处理响应事件
        LCHttpClient.post(mpa, YOUMI_URL, param, new BaseJsonHandler<String>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, String response) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  String errorResponse) {
            }

            @Override
            protected String parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
//				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
                return "";
            }

        });
    }
}