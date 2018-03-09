package com.lcshidai.lc.impl.licai;

import com.lcshidai.lc.model.licai.FundLoginJson;

public interface FundLoginImpl {

	void fundLoginSuccess(FundLoginJson response);

	void fundLoginFailed(FundLoginJson errorResponse);
}
