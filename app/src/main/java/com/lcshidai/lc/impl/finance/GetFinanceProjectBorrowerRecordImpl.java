package com.lcshidai.lc.impl.finance;

import com.lcshidai.lc.model.finance.borrower.FinanceProjectInvestBorrowerListJson;

public interface GetFinanceProjectBorrowerRecordImpl {

	void getFinanceProjectBorrowerRecordSuccess(
            FinanceProjectInvestBorrowerListJson response);

	void getFinanceProjectBorrowerRecordFail();

}
