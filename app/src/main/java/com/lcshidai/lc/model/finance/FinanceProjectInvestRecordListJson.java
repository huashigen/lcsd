package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceProjectInvestRecordListJson extends BaseJson {

	private FinanceProjectInvestRecordData data;

	public FinanceProjectInvestRecordData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(FinanceProjectInvestRecordData data) {
		this.data = data;
	}

}
