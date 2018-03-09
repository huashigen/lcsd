package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalsInfoJson extends BaseJson {
	private WithdrawalsInfoData data;

	public WithdrawalsInfoData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(WithdrawalsInfoData data) {
		this.data = data;
	}

}
