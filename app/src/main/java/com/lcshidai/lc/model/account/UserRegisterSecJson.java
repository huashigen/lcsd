package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegisterSecJson extends BaseJson {
	private UserRegisterSecData data;
	public UserRegisterSecData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(UserRegisterSecData data) {
		this.data = data;
	}
}
