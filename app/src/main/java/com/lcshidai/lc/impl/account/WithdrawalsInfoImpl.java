package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.WithdrawalsInfoJson;

public interface WithdrawalsInfoImpl {

	void gainWithdrawalsInfosuccess(WithdrawalsInfoJson response);

	void gainWithdrawalsInfofail();

}
