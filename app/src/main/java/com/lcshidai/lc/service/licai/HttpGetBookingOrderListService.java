package com.lcshidai.lc.service.licai;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.licai.GetMyBookingListImpl;
import com.lcshidai.lc.model.licai.MyBookingListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpGetBookingOrderListService implements HttpServiceURL {
    TRJActivity mpa;
    GetMyBookingListImpl ai;

    public HttpGetBookingOrderListService(TRJActivity mpa, GetMyBookingListImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }


    /**
     * 获取我的预约列表
     *
     * @param access_token access_token
     * @param user_token   user_token
     * @param status       status 预约状态
     */
    public void getBookingOrderList(String access_token, String user_token, String status, String page_no) {
        if (null == mpa) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.put("access_token", access_token);
        rp.put("user_token", user_token);
        rp.put("status", status);
        rp.put("page_no", page_no);

        LCHttpClient.postWithFullUrl(mpa, MY_BOOKING_LIST, rp, new BaseJsonHandler<MyBookingListJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, MyBookingListJson response) {
                ai.getMyBookingListSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  MyBookingListJson errorResponse) {
                ai.getMyBookingListFailed();
            }

            @Override
            protected MyBookingListJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), MyBookingListJson.class).next();
            }

        });
    }

}
