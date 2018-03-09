package com.lcshidai.lc.impl.licai;

import com.lcshidai.lc.model.licai.FundAccountInfoJson;

public interface GetFundAccountInfoImpl {

	void getFundAccountInfoSuccess(FundAccountInfoJson response);

	void getFundAccountInfoFailed(FundAccountInfoJson errorResponse);
}
