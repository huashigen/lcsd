package com.lcshidai.lc.service.licai;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.licai.AddToBookingOrderImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpAddBookingOrderService implements HttpServiceURL {
    TRJActivity mpa;
    AddToBookingOrderImpl ai;

    public HttpAddBookingOrderService(TRJActivity mpa, AddToBookingOrderImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }


    /**
     * 获取我的预约列表
     *
     * @param access_token access_token
     * @param user_token   user_token
     * @param fund_id      基金id
     * @param amount       预约金额
     */
    public void addToBookingOrder(String access_token, String user_token, String fund_id, String amount, String remark) {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.put("access_token", access_token);
        rp.put("user_token", user_token);
        rp.put("fund_id", fund_id);
        rp.put("amount", amount);
        rp.put("remark", remark);

        LCHttpClient.postWithFullUrl(mpa, ADD_TO_BOOKING_ORDER, rp, new BaseJsonHandler<BaseJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseJson response) {
                ai.addToBookingOrderSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                ai.addToBookingOrderFailed(errorResponse);
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
            }

        });
    }

}
