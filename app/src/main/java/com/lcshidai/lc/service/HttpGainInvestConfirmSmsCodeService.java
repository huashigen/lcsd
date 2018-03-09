package com.lcshidai.lc.service;

import android.content.Context;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.GainInvestConfirmSmsCodeImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.LocalInvestPayModel;

import org.apache.http.Header;

/**
 * Created by RandyZhang on 16/7/8.
 */
public class HttpGainInvestConfirmSmsCodeService implements HttpServiceURL {
    private GainInvestConfirmSmsCodeImpl gainInvestConfirmSmsCode = null;
    private Context context = null;

    public HttpGainInvestConfirmSmsCodeService(GainInvestConfirmSmsCodeImpl gainInvestConfirmSnsCode, Context context) {
        this.gainInvestConfirmSmsCode = gainInvestConfirmSnsCode;
        this.context = context;
    }

    public void gainInvestConfirmSmsCode(LocalInvestPayModel localInvestPayModel) {
        RequestParams rp = new RequestParams();
        rp.put("money", localInvestPayModel.getInvestAmount());
        rp.put("paypwd", localInvestPayModel.getPayPwd());
        rp.put("prj_id", localInvestPayModel.getInvestPrjId());
        LCHttpClient.get(context, GAIN_INVEST_CONFIRM_SMS_CODE, rp, new BaseJsonHandler<BaseJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseJson response) {
                gainInvestConfirmSmsCode.gainInvestConfirmSmsCodeSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  BaseJson errorResponse) {
                gainInvestConfirmSmsCode.gainInvestConfirmSmsCodeFailed(errorResponse);
            }

            @Override
            protected BaseJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
            }

        });
    }
}
