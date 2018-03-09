package com.lcshidai.lc.service.finance;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.FinancePayImpl;
import com.lcshidai.lc.model.LocalInvestPayModel;
import com.lcshidai.lc.model.finance.FinanceApplyCashoutJson;
import com.lcshidai.lc.model.finance.FinanceCheckPayPasswordJson;
import com.lcshidai.lc.model.finance.FinanceDoCashJson;
import com.lcshidai.lc.model.finance.FinancePayResultJson;
import com.lcshidai.lc.model.finance.FinanceWithdrawalsMoneyJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.AesUtil;
import com.lcshidai.lc.utils.CommonUtil;

public class HttpPayService implements HttpServiceURL {

    FinancePayImpl impl;
    TRJActivity mActivity;

    public HttpPayService(FinancePayImpl impl, TRJActivity mActivity) {
        this.impl = impl;
        this.mActivity = mActivity;
    }

    /**
     * 投资支付接口
     *
     * @param localInvestPayModel 本地保存的支付实体对象
     */
    public void doInvest(LocalInvestPayModel localInvestPayModel) {
        RequestParams rp = new RequestParams();
        rp.put("money", localInvestPayModel.getInvestAmount());
        rp.put("prj_id", localInvestPayModel.getInvestPrjId());
        rp.put("addInterestId", localInvestPayModel.getInvestJxqId());
        if (!CommonUtil.isNullOrEmpty(localInvestPayModel.getInvestUsedRewardType())) {
            rp.put("reward_type", localInvestPayModel.getInvestUsedRewardType());
        } else {
            rp.put("reward_type", "0");
        }
        if (!CommonUtil.isNullOrEmpty(localInvestPayModel.getInvestHbBonusRate())) {
            rp.put("bouns_rate", localInvestPayModel.getInvestHbBonusRate());
        }
        if (!CommonUtil.isNullOrEmpty(localInvestPayModel.getInvestHbBonusRateTimeLimit())) {
            rp.put("bouns_prj_term", localInvestPayModel.getInvestHbBonusRateTimeLimit());
        }
        if (!CommonUtil.isNullOrEmpty(localInvestPayModel.getInvestHbOrMjqId())) {
            rp.put("ticket_id", localInvestPayModel.getInvestHbOrMjqId());
        }
        try {
            rp.put("paypwd", AesUtil.getInstance().encrypt(localInvestPayModel.getPayPwd()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (localInvestPayModel.isRepay()) {//是否预售（暂时不用，默认为0)
            rp.put("is_pre_buy", "1");
        } else {
            rp.put("is_pre_buy", "0");
        }
        rp.put("deviceid", CommonUtil.getDeviceId(mActivity));
        rp.put("mobileCode", localInvestPayModel.getInvestMobileCode());
        mActivity.post(localInvestPayModel.isXXB() ? BUY_EXPERIENCE_PROJECT : NORMAL_PRJ_INVEST, rp, new BaseJsonHandler<FinancePayResultJson>(mActivity) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, FinancePayResultJson response) {
                impl.buyPiSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, FinancePayResultJson errorResponse) {
                try {
                    if (!CommonUtil.isNullOrEmpty(rawJsonData)) {
                        JSONObject json = new JSONObject(rawJsonData);
                        String message = json.getString("message");
                        impl.buyPiFail(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected FinancePayResultJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), FinancePayResultJson.class).next();
            }

        });

    }

    /**
     * @param prj_order_id
     * @param prj_id
     * @param jxq_id
     * @param sdt_prj_name
     * @param cash_rate
     * @param cash_money
     * @param cash_pwd
     */
    public void buyPiF(String prj_order_id, String prj_id, String jxq_id, String sdt_prj_name, String cash_rate, String cash_money, String cash_pwd) {
        RequestParams rp = new RequestParams();
        rp.put("prj_order_id", prj_order_id);
        rp.put("prj_id", prj_id);
        rp.put("addInterestId", jxq_id);
        rp.put("sdt_prj_name", sdt_prj_name);
        rp.put("cash_rate", cash_rate);
        rp.put("cash_money", cash_money);
        try {
            rp.put("cash_pwd", AesUtil.getInstance().encrypt(cash_pwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mActivity.post(DO_CASH, rp, new BaseJsonHandler<FinanceDoCashJson>(mActivity) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, FinanceDoCashJson response) {
                impl.buyPiFSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, FinanceDoCashJson errorResponse) {
                impl.buyPiFFail();
            }

            @Override
            protected FinanceDoCashJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), FinanceDoCashJson.class).next();
            }
        });
    }

    /**
     * 集合标投资
     *
     * @param localInvestPayModel
     */
    public void doCollectionPrjInvest(LocalInvestPayModel localInvestPayModel) {
        RequestParams rp = new RequestParams();
        rp.put("money", localInvestPayModel.getInvestAmount());
        rp.put("collection_id", localInvestPayModel.getInvestPrjId());
        rp.put("addInterestId", localInvestPayModel.getInvestJxqId());
        if (!CommonUtil.isNullOrEmpty(localInvestPayModel.getInvestUsedRewardType())) {
            rp.put("reward_type", localInvestPayModel.getInvestUsedRewardType());
        } else {
            rp.put("reward_type", "0");
        }
        if (!CommonUtil.isNullOrEmpty(localInvestPayModel.getInvestHbBonusRate())) {
            rp.put("bouns_rate", localInvestPayModel.getInvestHbBonusRate());
        }
        if (!CommonUtil.isNullOrEmpty(localInvestPayModel.getInvestHbBonusRateTimeLimit())) {
            rp.put("bouns_prj_term", localInvestPayModel.getInvestHbBonusRateTimeLimit());
        }
        if (!CommonUtil.isNullOrEmpty(localInvestPayModel.getInvestHbOrMjqId())) {
            rp.put("ticket_id", localInvestPayModel.getInvestHbOrMjqId());
        }
        try {
            rp.put("paypwd", AesUtil.getInstance().encrypt(localInvestPayModel.getPayPwd()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (localInvestPayModel.isRepay()) {//是否预售（暂时不用，默认为0)
            rp.put("is_pre_buy", "1");
        } else {
            rp.put("is_pre_buy", "0");
        }
        rp.put("deviceid", CommonUtil.getDeviceId(mActivity));
        rp.put("mobileCode", localInvestPayModel.getInvestMobileCode());
        mActivity.post(COLLECTION_PRJ_INVEST, rp, new BaseJsonHandler<FinancePayResultJson>(mActivity) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, FinancePayResultJson response) {
                impl.buyPiSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, FinancePayResultJson errorResponse) {
                try {
                    if (!CommonUtil.isNullOrEmpty(rawJsonData)) {
                        JSONObject json = new JSONObject(rawJsonData);
                        String message = json.getString("message");
                        impl.buyPiFail(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected FinancePayResultJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), FinancePayResultJson.class).next();
            }

        });

    }

    /**
     * 实际到账金额获取
     *
     * @param money
     */
    public void loadCashoutFeeData(String money) {
        RequestParams params = new RequestParams();
        params.put("money", money);
        mActivity.post(WITHDRAWALS_MONEY_URL, params, new BaseJsonHandler<FinanceWithdrawalsMoneyJson>(mActivity) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, FinanceWithdrawalsMoneyJson response) {
                impl.loadCashoutFeeDataSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  FinanceWithdrawalsMoneyJson errorResponse) {
                impl.loadCashoutFeeDataFail();
            }

            @Override
            protected FinanceWithdrawalsMoneyJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData),
                        FinanceWithdrawalsMoneyJson.class).next();
            }

        });
    }

    public void loadWithdrawalsData(String w_money, String w_bank, String w_sub_bank, String w_out_account_no,
                                    String inputPwd, int w_flag, String w_out_account_id, String w_code,
                                    String w_temp_pcode, String w_bank_name, String w_sub_bank_id,
                                    String w_is_save_fund) {
        RequestParams params = new RequestParams();
        params.put("money", w_money); // 金额 单位元
        params.put("use_reward_money", "1"); // 是否使用奖励金额 默认写 1
        params.put("bank", w_bank); // 银行Code
        params.put("sub_bank", w_sub_bank); // 支行信息
        params.put("bak", ""); // 备注
        params.put("out_account_no", w_out_account_no); // 银行账户
        try {
            params.put("payPwd", AesUtil.getInstance().encrypt(inputPwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (1 == w_flag || 2 == w_flag || 3 == w_flag) {
            params.put("out_account_id", w_out_account_id); // 银行账户id
        }
        params.put("free_times", "1"); // 是否使用提现的次数
        params.put("code", w_code); // 城市code
        params.put("temp_pcode", w_temp_pcode); // 省会code
        params.put("bank_name", w_bank_name); // 银行名称
        params.put("is_default", "0"); // 是否默认提现卡
        params.put("sub_bank_id", w_sub_bank_id); // 支行id
        params.put("is_save_fund", w_is_save_fund); // 是否编辑或新增银行卡 0-否 1-是
        mActivity.post(WITHDRAWALS_URL, params, new BaseJsonHandler<FinanceApplyCashoutJson>(mActivity) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                  FinanceApplyCashoutJson response) {
                impl.applyCashoutSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  String rawJsonData, FinanceApplyCashoutJson errorResponse) {
                impl.applyCashoutFail();
            }

            @Override
            protected FinanceApplyCashoutJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData),
                        FinanceApplyCashoutJson.class).next();
            }

        });
    }

    /**
     * 支付密码校验接口
     *
     * @param uid      用户uid
     * @param inputPwd 用户输入密码
     */
    public void checkPayPwd(String uid, String inputPwd) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        try {
            params.put("payPassword", AesUtil.getInstance().encrypt(inputPwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mActivity.post(PAY_PWD_CHECK_URL, params, new BaseJsonHandler<FinanceCheckPayPasswordJson>(mActivity, false) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                  FinanceCheckPayPasswordJson response) {
                impl.payPswCheckSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
                                  FinanceCheckPayPasswordJson errorResponse) {
                impl.payPswCheckFailed();
            }

            @Override
            protected FinanceCheckPayPasswordJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                super.parseResponse(rawJsonData, isFailure);
                return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData),
                        FinanceCheckPayPasswordJson.class).next();
            }

        });
    }
}
