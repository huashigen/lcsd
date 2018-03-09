package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.UserRegisterSecJson;

public interface UserSetPasswordImpl {

	void setPasswordSuccess(UserRegisterSecJson response);

	void setPasswordFail();

}
