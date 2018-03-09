package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.AccountSavepathJson;

public interface AccountSavepathImpl {

	void gainAccountSavepathsuccess(AccountSavepathJson response);

	void gainAccountSavepathfail();

}
