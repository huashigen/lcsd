package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.MessageCentreImpl;
import com.lcshidai.lc.model.account.MessageCentreJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpMessageCentreService implements HttpServiceURL {
    TRJActivity mpa;
    MessageCentreImpl ai;

    public HttpMessageCentreService(TRJActivity mpa, MessageCentreImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainMessageCentre(int type, int nowPage, int aVG_PAGE_NUM) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("page", String.valueOf(nowPage));
        rp.put("pagesize", String.valueOf(aVG_PAGE_NUM));
        rp.put("type", String.valueOf(type));
        mpa.post(GET_MESSAGE_URL, rp, new BaseJsonHandler<MessageCentreJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, MessageCentreJson response) {
                ai.gainMessageCentresuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  MessageCentreJson errorResponse) {
                ai.gainMessageCentrefail();
            }

            @Override
            protected MessageCentreJson parseResponse(String rawJsonData,
                                                      boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), MessageCentreJson.class).next();
            }

        });
    }
}
