package com.lcshidai.lc.impl.finance;

import com.lcshidai.lc.model.finance.FinanceMaxInvestMoneyJson;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckJson;

public interface FinanceInvestPBuyCheckImpl {

    void getMaxAvaInvestAmountSuccess(FinanceMaxInvestMoneyJson result);

    void getMaxAvaInvestAmountFail();

    void doInvestCheckSuccess(FinanceInvestPBuyCheckJson result, float va, String id, boolean isRepay, String yearrate);

    void doInvestCheckFail(String response);
}
