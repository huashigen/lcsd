package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.GetEscrowRemindJson;

public interface GetEscrowRemindImpl {

	void getEscrowRemindSuccess(GetEscrowRemindJson response);

	void getEscrowRemindFail();

}
