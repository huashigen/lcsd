package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.GetSharkJson;

public interface GetSharkImpl {

	void gainGetSharksuccess(GetSharkJson response, boolean tmsonido);

	void gainGetSharkfail();

}
