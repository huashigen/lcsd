package com.lcshidai.lc.service.account;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.ManageFinanceListInfoItemImpl;
import com.lcshidai.lc.model.account.ManageFinanceListInfoItemJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;

public class HttpManageFinanceListInfoItemService implements HttpServiceURL {
    TRJActivity mpa;
    ManageFinanceListInfoItemImpl ai;

    public HttpManageFinanceListInfoItemService(TRJActivity mpa,
                                                ManageFinanceListInfoItemImpl ai) {
        this.mpa = mpa;
        this.ai = ai;
    }

    public void gainManageFinanceListInfoItem(String type, String mType, int mP, int mPagesize,
                                              boolean isTransfer, String mStatus, String payment, String deal) {
        if (null == mpa) return;
        RequestParams rp = new RequestParams();
        rp.put("type", type);//
        rp.put("prj_type", mType);//
        rp.put("p", String.valueOf(mP++));//p 分页页数
        rp.put("page_size", mPagesize + "");// 每页多少条 ，不传默认每页10条
        if (null != payment) {
            rp.put("order_by_payment", payment);
        }
        if (null != deal) {
            rp.put("order_by_deal", deal);
        }
        String urlString = isTransfer ? FASTCASHLIST : FINANCLIST;
        if (isTransfer) {
            rp.put("type", mStatus);//
        } else {
            rp.put("status", mStatus);//
        }
        mpa.post(urlString, rp, new BaseJsonHandler<ManageFinanceListInfoItemJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, ManageFinanceListInfoItemJson response) {
                ai.gainManageFinanceListInfoItemsuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  ManageFinanceListInfoItemJson errorResponse) {
                ai.gainManageFinanceListInfoItemfail();
            }

            @Override
            protected ManageFinanceListInfoItemJson parseResponse(String rawJsonData,
                                                                  boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), ManageFinanceListInfoItemJson.class).next();
            }

        });
    }
}
