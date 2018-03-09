package com.lcshidai.lc.impl.finance;

import com.lcshidai.lc.model.finance.FinancePosterJson;

public interface FinancePosterImpl {

	void gainFinancePostersuccess(FinancePosterJson response);

	void gainFinancePosterfail();

}
