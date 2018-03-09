package com.lcshidai.lc.impl.finance;

import com.lcshidai.lc.model.finance.FinanceMJQJson;

public interface FinanceMJQImpl {

	void gainFinaceTicketsSuccess(FinanceMJQJson response);

	void gainFinaceTicketsFail();

}
