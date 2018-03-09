package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoldFinanceJson extends BaseJson{
	private GoldFinanceData data;

	public GoldFinanceData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(GoldFinanceData data) {
		this.data = data;
	}
}
