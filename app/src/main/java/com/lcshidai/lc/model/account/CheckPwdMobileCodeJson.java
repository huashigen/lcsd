package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckPwdMobileCodeJson extends BaseJson{
	private CheckPwdMobileCodeData data;

	public CheckPwdMobileCodeData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(CheckPwdMobileCodeData data) {
		this.data = data;
	}
}
