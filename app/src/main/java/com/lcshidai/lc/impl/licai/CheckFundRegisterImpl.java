package com.lcshidai.lc.impl.licai;

import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.licai.CheckFundRegisterJson;

public interface CheckFundRegisterImpl {

	void checkFundRegisterSuccess(CheckFundRegisterJson response);

	void checkFundRegisterFailed(BaseJson errorResponse);
}
