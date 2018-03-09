package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountJson extends BaseJson {
	private AccountData data;

	public AccountData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(AccountData data) {
		this.data = data;
	}

}
