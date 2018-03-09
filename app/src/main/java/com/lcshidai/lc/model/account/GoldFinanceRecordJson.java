package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoldFinanceRecordJson extends BaseJson{
	private GoldFinanceRecordData data;

	public GoldFinanceRecordData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(GoldFinanceRecordData data) {
		this.data = data;
	}
}
