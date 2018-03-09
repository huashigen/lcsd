package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalancePaymentsJson extends BaseJson {
	private BalancePaymentsData data;

	public BalancePaymentsData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(BalancePaymentsData data) {
		this.data = data;
	}

}
