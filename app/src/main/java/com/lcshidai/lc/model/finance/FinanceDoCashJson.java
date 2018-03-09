package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FinanceDoCashJson extends BaseJson{

	private FinanceDoCashData data;

	public FinanceDoCashData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(FinanceDoCashData data) {
		this.data = data;
	}
	
}
