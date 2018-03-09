package com.lcshidai.lc.impl.licai;

import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.licai.IsShowCfyJson;

public interface IsShowCfyImpl {

	void isShowCfySuccess(IsShowCfyJson response);

	void isShowCfyFailed(BaseJson errorResponse);
}
