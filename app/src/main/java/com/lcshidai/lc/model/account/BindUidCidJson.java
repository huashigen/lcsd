package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BindUidCidJson extends BaseJson{
	private Object data;

	public Object getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(BespeakTypeData data) {
		this.data = data;
	}
}
