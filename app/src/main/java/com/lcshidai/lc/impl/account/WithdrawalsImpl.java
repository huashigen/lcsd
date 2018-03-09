package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.WithdrawalsJson;

public interface WithdrawalsImpl {

	void gainWithdrawalssuccess(WithdrawalsJson response);

	void gainWithdrawalsfail();

}
