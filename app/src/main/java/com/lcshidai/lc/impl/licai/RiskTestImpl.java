package com.lcshidai.lc.impl.licai;

import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.licai.RiskTestQuestionJson;

public interface RiskTestImpl {

	void getRiskTestQuestionListSuccess(RiskTestQuestionJson response);

	void getRiskTestQuestionListFail();

	void saveRiskTestAnswerSuccess(BaseJson response);

	void saveRiskTestAnswerFail(BaseJson errorResponse);

}
