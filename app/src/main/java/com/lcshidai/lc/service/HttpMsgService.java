package com.lcshidai.lc.service;

import org.apache.http.Header;

import android.content.Context;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.MessageImpl;
import com.lcshidai.lc.model.MessageJson;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.StringUtils;

/**
 * 获取所有消息数据
 *
 * @author Administrator
 */
public class HttpMsgService implements HttpServiceURL {
    Context mpa;
    MessageImpl impl;

    public HttpMsgService(Context mpa, MessageImpl impl) {
        this.mpa = mpa;
        this.impl = impl;
    }


    public void getAllMsg(String uid, int invest_sequence, int discovery_sequence, int account_sequence) {
        if (null == mpa)
            return;
        if (StringUtils.isEmpty(uid)) {
            uid = "0";
        }
        String imei = CommonUtil.getDeviceId(mpa);
        String conts = "";
//		[{"type":"invest","sequence":100},{"type":"discovery","sequence":100},{"type":"account""uid":"10","sequence":101}]
        if (uid.equals("0")) {
            conts = "[{\"type\":\"invest\" , \"sequence\":" + invest_sequence + "},{\"type\":\"discovery\" , \"sequence\":" + discovery_sequence + "}]";
        } else {
            conts = "[{\"type\":\"invest\" , \"sequence\":" + invest_sequence + "},{\"type\":\"discovery\" , \"sequence\":" + discovery_sequence + "},{\"type\":\"account\" , \"uid\":\"" + uid + "\",\"sequence\":" + account_sequence + "}]";
        }
        RequestParams param = new RequestParams();
        param.put("deviceno", imei);
        param.put("uids", "[" + uid + "]");
        param.put("conts", conts);
        // 不用处理响应事件
        LCHttpClient.post(mpa, GET_ALL_NEW_MSG_LIST, param, new BaseJsonHandler<MessageJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, MessageJson response) {
                impl.gainMessageSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  MessageJson errorResponse) {
                impl.gainMessageFail();
            }

            @Override
            protected MessageJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), MessageJson.class).next();
            }

        });
    }
}