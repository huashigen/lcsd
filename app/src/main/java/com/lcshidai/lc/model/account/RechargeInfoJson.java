package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RechargeInfoJson extends BaseJson{
	private RechargeInfoData data;

	public RechargeInfoData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(RechargeInfoData data) {
		this.data = data;
	}
}
