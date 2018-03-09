package com.lcshidai.lc.model.finance.borrower;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceProjectInvestBorrowerListJson extends BaseJson {

	private FinanceProjectBorrowerRecordData data;

	public FinanceProjectBorrowerRecordData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(FinanceProjectBorrowerRecordData data) {
		this.data = data;
	}

}
