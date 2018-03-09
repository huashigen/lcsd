package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.BalancePaymentsJson;

public interface BalancePaymentsImpl {

	void gainBalancePaymentssuccess(BalancePaymentsJson response);

	void gainBalancePaymentsfail();

}
