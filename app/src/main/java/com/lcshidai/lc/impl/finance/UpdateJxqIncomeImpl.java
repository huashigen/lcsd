package com.lcshidai.lc.impl.finance;

import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckJson;

public interface UpdateJxqIncomeImpl {

    void updateDateJxqIncomeSuccess(FinanceInvestPBuyCheckJson result);

    void updateDateJxqIncomeFail(String message);
}
