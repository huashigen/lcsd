package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMobilePayPwdJson extends BaseJson{
	private UserMobilePayPwdData data;

	public UserMobilePayPwdData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(UserMobilePayPwdData data) {
		this.data = data;
	}
}
