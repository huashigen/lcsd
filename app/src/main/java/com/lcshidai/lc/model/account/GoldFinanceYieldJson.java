package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoldFinanceYieldJson extends BaseJson{
	private  GoldFinanceYieldData data;

	public GoldFinanceYieldData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(GoldFinanceYieldData data) {
		this.data = data;
	}
}
