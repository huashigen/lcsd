package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.BaseJson;


public interface UserRegisterSendcodeImpl {

	void getRegisterSmsCodeSuccess(BaseJson response);

	void getRegisterSmsCodeFailed();

}
