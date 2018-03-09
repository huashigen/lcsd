package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.CheckPwdMobileCodeJson;

public interface CheckPwdMobileCodeImpl {

	void gainCheckPwdMobileCodesuccess(CheckPwdMobileCodeJson response);

	void gainCheckPwdMobileCodefail();

}
