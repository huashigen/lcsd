package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.GetShareJson;

public interface GetShareImpl {

	void gainGetSharesuccess(GetShareJson response, boolean isInit);

	void gainGetSharefail();

}
