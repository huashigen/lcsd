package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalsListJson extends BaseJson {
	private WithdrawalsListData data;

	public WithdrawalsListData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(WithdrawalsListData data) {
		this.data = data;
	}

}
