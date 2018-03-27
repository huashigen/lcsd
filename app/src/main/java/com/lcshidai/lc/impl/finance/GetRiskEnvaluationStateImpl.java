package com.lcshidai.lc.impl.finance;

import com.lcshidai.lc.model.finance.RiskEnvaluationStateJson;

public interface GetRiskEnvaluationStateImpl {
	// FinanceItemBaseInfoJson
	void getRiskEnvaluationStateSuccess(RiskEnvaluationStateJson response);

	void getRiskEnvaluationStateFail();

}
