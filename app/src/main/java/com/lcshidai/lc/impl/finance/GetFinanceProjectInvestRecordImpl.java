package com.lcshidai.lc.impl.finance;

import com.lcshidai.lc.model.finance.FinanceProjectInvestRecordListJson;

public interface GetFinanceProjectInvestRecordImpl {

	void getFinanceProjectInvestRecordSuccess(
			FinanceProjectInvestRecordListJson response);

	void getFinanceProjectInvestRecordFail();

}
