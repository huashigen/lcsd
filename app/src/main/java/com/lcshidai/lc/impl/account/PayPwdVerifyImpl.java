package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.PayPwdVerifyJson;

public interface PayPwdVerifyImpl {

	void gainPayPwdVerifysuccess(PayPwdVerifyJson response);

	void gainPayPwdVerifyfail();

}
