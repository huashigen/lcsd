package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.WithdrawalsListJson;

public interface WithdrawalsListImpl {

	void gainWithdrawalsListsuccess(WithdrawalsListJson response);

	void gainWithdrawalsListfail();

}
