package com.lcshidai.lc.service;

import android.content.Context;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.ModifyLeftPhoneImpl;
import com.lcshidai.lc.model.BaseJson;

import org.apache.http.Header;

/**
 * Created by RandyZhang on 16/7/6.
 */
public class HttpModifyLeftPhoneService implements HttpServiceURL {
    Context context;
    ModifyLeftPhoneImpl modifyLeftPhoneImpl;

    public HttpModifyLeftPhoneService(Context context, ModifyLeftPhoneImpl modifyLeftPhoneImpl) {
        this.context = context;
        this.modifyLeftPhoneImpl = modifyLeftPhoneImpl;
    }

    public void modifyLeftPhone(String originPhone , String nowPhone , String code) {
        if (null == context)
            return;
        RequestParams param = new RequestParams();
        param.put("ori_mobile", originPhone);
        param.put("new_mobile", nowPhone);
        param.put("mobile_code", code);
        LCHttpClient.post(context, UPDATE_ESCROW_LEFT_PHONE, param, new BaseJsonHandler<BaseJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseJson response) {
                modifyLeftPhoneImpl.modifyLeftPhoneSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                modifyLeftPhoneImpl.modifyLeftPhoneFailed(errorResponse);
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
            }

        });
    }

    public void gainModifyLeftPhoneSmsCode(String originPhone, String nowPhone) {
        if (null == context)
            return;
        RequestParams param = new RequestParams();
        param.put("ori_mobile", originPhone);
        param.put("new_mobile", nowPhone);
        LCHttpClient.post(context, GET_ESCROW_SMS_CODE, param, new BaseJsonHandler<BaseJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseJson response) {
                modifyLeftPhoneImpl.gainModifyLeftPhoneSmsCodeSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                modifyLeftPhoneImpl.gainModifyLeftPhoneSmsCodeFailed(errorResponse);
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
            }

        });

    }
}
