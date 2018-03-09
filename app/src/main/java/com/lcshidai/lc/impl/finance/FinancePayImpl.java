package com.lcshidai.lc.impl.finance;

import com.lcshidai.lc.model.finance.FinanceApplyCashoutJson;
import com.lcshidai.lc.model.finance.FinanceCheckPayPasswordJson;
import com.lcshidai.lc.model.finance.FinanceDoCashJson;
import com.lcshidai.lc.model.finance.FinancePayResultJson;
import com.lcshidai.lc.model.finance.FinanceWithdrawalsMoneyJson;

public interface FinancePayImpl {
	
	void buyPiSuccess(FinancePayResultJson result);
	void buyPiFail(String message);
	
	void buyPiFSuccess(FinanceDoCashJson result);
	void buyPiFFail();
	
	void loadCashoutFeeDataSuccess(FinanceWithdrawalsMoneyJson result);
	void loadCashoutFeeDataFail();
	
	void applyCashoutSuccess(FinanceApplyCashoutJson response);
	void applyCashoutFail();
	
	void payPswCheckSuccess(FinanceCheckPayPasswordJson response);
	void payPswCheckFailed();
}
