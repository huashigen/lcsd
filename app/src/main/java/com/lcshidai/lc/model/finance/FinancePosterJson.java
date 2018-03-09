package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancePosterJson extends BaseJson {

	private FinancePosterData data;

	public FinancePosterData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(FinancePosterData data) {
		this.data = data;
	}
}