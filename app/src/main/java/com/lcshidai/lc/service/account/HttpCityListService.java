package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.CityListImpl;
import com.lcshidai.lc.model.account.CityListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpCityListService implements HttpServiceURL {
    TRJActivity mpa;
    CityListImpl ai;

    public HttpCityListService(TRJActivity mpa,
                               CityListImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainCityList(String pcode) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        String url = "";
        if (!pcode.equals("")) {
            url = URL_GET_CITYS_LIST;
            rp.put("temp_pcode", pcode);
        } else {
            url = URL_GET_CITY_LIST;
        }
        mpa.post(url, rp, new BaseJsonHandler<CityListJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, CityListJson response) {
                ai.gainCityListsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  CityListJson errorResponse) {
                ai.gainCityListfail();
            }

            @Override
            protected CityListJson parseResponse(String rawJsonData,
                                                 boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), CityListJson.class).next();
            }

        });
    }
}
