package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.CheckIdentityJson;

public interface CheckIdentityImpl {

	void gainCheckIdentitysuccess(CheckIdentityJson response);

	void gainCheckIdentityfail();

}
