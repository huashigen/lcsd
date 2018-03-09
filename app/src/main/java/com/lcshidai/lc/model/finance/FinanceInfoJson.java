package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceInfoJson extends BaseJson {

	private FinanceInfoData data;

	public FinanceInfoData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(FinanceInfoData data) {
		this.data = data;
	}
}