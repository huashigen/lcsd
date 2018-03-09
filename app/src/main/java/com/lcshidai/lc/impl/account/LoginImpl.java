package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.BaseJson;

public interface LoginImpl {

	void loginSuccess(BaseJson response);

	void loginFailed();

}
