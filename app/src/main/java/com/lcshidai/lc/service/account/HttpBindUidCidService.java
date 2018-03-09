package com.lcshidai.lc.service.account;

import android.content.Context;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.account.BindUidCidImpl;
import com.lcshidai.lc.model.account.BindUidCidJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;

import org.apache.http.Header;

/**
 * Created by RandyZhang on 16/7/7.
 */
public class HttpBindUidCidService implements HttpServiceURL {
    Context context;
    BindUidCidImpl bindUidCid;

    public HttpBindUidCidService(Context context, BindUidCidImpl bindUidCid) {
        this.context = context;
        this.bindUidCid = bindUidCid;
    }

    /**
     * 绑定给推clientId 和用户uid
     */
    public void bindUidCid(String uid, String cid, String client_type) {
        if (null == context)
            return;
        RequestParams param = new RequestParams();
        param.put("uid", uid);
        param.put("cid", cid);
        param.put("client_type", client_type);
        LCHttpClient.post(context, BIND_UID_CID, param, new BaseJsonHandler<BindUidCidJson>() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BindUidCidJson response) {
                bindUidCid.bindUidCidSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  BindUidCidJson errorResponse) {
                bindUidCid.bindUidCidFailed();
            }

            @Override
            protected BindUidCidJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BindUidCidJson.class).next();
            }

        });
    }

}
