package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.ApplyCashoutJson;
import com.lcshidai.lc.model.account.RechargeListJson;

public interface RechargeListImpl {

	void gainRechargeListsuccess(RechargeListJson response);

	void gainRechargeListfail();

	void gainapplyCashoutsuccess(ApplyCashoutJson response);

	void gainapplyCashoutfail();

}
