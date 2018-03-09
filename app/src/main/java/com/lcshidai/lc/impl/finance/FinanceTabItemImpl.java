package com.lcshidai.lc.impl.finance;

import com.lcshidai.lc.model.finance.FinanceTabItemJson;


public interface FinanceTabItemImpl {
	
	void gainFinanceTabItemSuccess(FinanceTabItemJson response);

	void gainFinanceTabItemFail();
}
