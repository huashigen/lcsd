package com.lcshidai.lc.service.finance;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.FinanceInvestPBuyCheckImpl;
import com.lcshidai.lc.model.finance.FinanceMaxInvestMoneyJson;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;

public class HttpFinanceInvestCheckService implements HttpServiceURL {

    FinanceInvestPBuyCheckImpl impl;
    TRJActivity mpa;

    public HttpFinanceInvestCheckService(TRJActivity mpa,
                                         FinanceInvestPBuyCheckImpl impl) {
        this.mpa = mpa;
        this.impl = impl;
    }

    /**
     * 获取最大可投资金额
     *
     * @param id           项目id
     * @param isCollection 是否集合标 1是；0不是
     */
    public void getMaxAvaInvestAmount(String id, int isCollection) {
        RequestParams rp = new RequestParams();
        rp.put("prj_id", id);
        rp.put("is_collection", isCollection);
        mpa.post(GET_MAX_INVEST_MONEY, rp, new BaseJsonHandler<FinanceMaxInvestMoneyJson>(mpa, false) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, FinanceMaxInvestMoneyJson response) {
                impl.getMaxAvaInvestAmountSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  FinanceMaxInvestMoneyJson errorResponse) {
                impl.getMaxAvaInvestAmountFail();
            }

            @Override
            protected FinanceMaxInvestMoneyJson parseResponse(
                    String rawJsonData, boolean isFailure)
                    throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(
                        new JsonFactory().createParser(rawJsonData),
                        FinanceMaxInvestMoneyJson.class).next();
            }

        });
    }

    /**
     * 立即投资检查
     *
     * @param investAmount 投资金额
     * @param prjId        项目id
     * @param jxq_id       加息券id
     * @param isRepay      是否回款
     * @param yearRate     年化利率
     */
    public void doInvestCheck(final float investAmount, final String prjId, final String jxq_id,
                              final boolean isRepay, final String yearRate) {
        RequestParams rp = new RequestParams();
        rp.put("money", investAmount);
        rp.put("prjid", prjId);
        rp.put("is_pre_buy", isRepay ? "1" : "0");
        rp.put("addInterestId", jxq_id);
        mpa.post(NORMAL_PRJ_INVEST_CHECK, rp, new BaseJsonHandler<FinanceInvestPBuyCheckJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, FinanceInvestPBuyCheckJson response) {
                impl.doInvestCheckSuccess(response, investAmount, prjId, isRepay, yearRate);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  FinanceInvestPBuyCheckJson errorResponse) {
                try {
                    JSONObject json = new JSONObject(rawJsonData);
                    if (null != json) {
                        String message = json.getString("message");
                        impl.doInvestCheckFail(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected FinanceInvestPBuyCheckJson parseResponse(String rawJsonData, boolean isFailure)
                    throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(
                        new JsonFactory().createParser(rawJsonData),
                        FinanceInvestPBuyCheckJson.class).next();
            }
        });
    }

    /**
     * 集合标立即投资检查
     *
     * @param investAmount    投资金额
     * @param collectionPrjId 集合标id
     * @param jxq_id          加息券id
     * @param isRepay         是否回款
     * @param yearRate        年化利率
     */
    public void doCollectionInvestCheck(final float investAmount,
                                        final String collectionPrjId,
                                        final String jxq_id,
                                        final boolean isRepay,
                                        final String yearRate) {
        RequestParams rp = new RequestParams();
        rp.put("money", investAmount + "");
        rp.put("collection_id", collectionPrjId);
        rp.put("is_pre_buy", isRepay ? "1" : "0");
        rp.put("addInterestId", jxq_id);
        mpa.post(COLLECTION_PRJ_INVEST_CHECK, rp, new BaseJsonHandler<FinanceInvestPBuyCheckJson>(mpa) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String rawJsonResponse, FinanceInvestPBuyCheckJson response) {
                impl.doInvestCheckSuccess(response, investAmount, collectionPrjId, isRepay, yearRate);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
                                  FinanceInvestPBuyCheckJson errorResponse) {
                String message = null;
                try {
                    JSONObject json = new JSONObject(rawJsonData);
                    message = json.getString("message");
                    impl.doInvestCheckFail(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if(!CommonUtil.isNullOrEmpty(message)) {
                        impl.doInvestCheckFail(message);
                    } else {
                        impl.doInvestCheckFail("数据异常");
                    }
                }
            }

            @Override
            protected FinanceInvestPBuyCheckJson parseResponse(
                    String rawJsonData, boolean isFailure)
                    throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData),
                        FinanceInvestPBuyCheckJson.class).next();
            }
        });

    }
}
