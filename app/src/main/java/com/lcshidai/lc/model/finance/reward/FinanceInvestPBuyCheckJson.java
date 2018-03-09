package com.lcshidai.lc.model.finance.reward;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FinanceInvestPBuyCheckJson extends BaseJson{
	
	private FinanceInvestPBuyCheckData data;

	public FinanceInvestPBuyCheckData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(FinanceInvestPBuyCheckData data) {
		this.data = data;
	}
	
}
