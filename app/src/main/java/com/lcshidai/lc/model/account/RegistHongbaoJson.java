package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistHongbaoJson extends BaseJson{
	private RegistHongbaoData data;

	public RegistHongbaoData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(RegistHongbaoData data) {
		this.data = data;
	}
}
