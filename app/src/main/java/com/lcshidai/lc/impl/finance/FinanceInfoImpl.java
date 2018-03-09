package com.lcshidai.lc.impl.finance;

import com.lcshidai.lc.model.finance.FinanceInfoJson;

public interface FinanceInfoImpl {

	void getFinanceProjectDetailSuccess(FinanceInfoJson response);

	void getFinanceProjectDetailFail();

}
