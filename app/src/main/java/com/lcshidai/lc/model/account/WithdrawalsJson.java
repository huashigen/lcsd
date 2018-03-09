package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalsJson extends BaseJson {
	private WithdrawalsData data;

	public WithdrawalsData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(WithdrawalsData data) {
		this.data = data;
	}

}
