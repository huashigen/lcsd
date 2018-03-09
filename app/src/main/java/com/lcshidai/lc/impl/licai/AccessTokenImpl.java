package com.lcshidai.lc.impl.licai;

import com.lcshidai.lc.model.licai.AccessTokenJson;

public interface AccessTokenImpl {

	void getAccessTokenSuccess(AccessTokenJson response);

	void getAccessTokenFailed();
}
