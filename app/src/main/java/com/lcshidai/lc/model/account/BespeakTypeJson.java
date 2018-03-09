package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakTypeJson extends BaseJson{
	private BespeakTypeData data;

	public BespeakTypeData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(BespeakTypeData data) {
		this.data = data;
	}
}
