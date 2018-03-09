package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyCashoutJson extends BaseJson{
	private ApplyCashoutData data;

	public ApplyCashoutData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(ApplyCashoutData data) {
		this.data = data;
	}
}
