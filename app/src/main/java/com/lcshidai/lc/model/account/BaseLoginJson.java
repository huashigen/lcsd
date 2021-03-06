package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseLoginJson extends BaseJson {
	private BaseLoginData data;

	public BaseLoginData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(BaseLoginData data) {
		this.data = data;
	}

}
