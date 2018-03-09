package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GainCodeJson extends BaseJson{
	private AccountSettingData data;

	public AccountSettingData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(AccountSettingData data) {
		this.data = data;
	}
}
