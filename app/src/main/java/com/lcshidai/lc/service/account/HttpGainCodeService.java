package com.lcshidai.lc.service.account;

import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.lcshidai.lc.impl.account.GainCodeImpl;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpGainCodeService implements HttpServiceURL {
    TRJActivity mpa;
    GainCodeImpl ai;

    public HttpGainCodeService(TRJActivity mpa, GainCodeImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void getBitmapData(final String url, final String content, final String title, final String qrcode) {
        LCHttpClient.get(mpa, url, new BinaryHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                ai.gainBitmapDatasuccess(binaryData, url, content, title, qrcode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {
                ai.gainBitmapDatafail();
            }

        });
    }
}
