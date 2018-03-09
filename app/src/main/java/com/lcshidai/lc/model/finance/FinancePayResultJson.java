package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FinancePayResultJson extends BaseJson{

	private FinancePayResultData data;

	public FinancePayResultData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(FinancePayResultData data) {
		this.data = data;
	}
	
}
