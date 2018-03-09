package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.ResetPwdJson;

public interface ResetPwdImpl {

	void gainResetPwdsuccess(ResetPwdJson response);

	void gainResetPwdfail();

}
