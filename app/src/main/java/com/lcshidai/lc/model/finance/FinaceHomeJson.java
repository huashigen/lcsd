package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinaceHomeJson extends BaseJson {

	private FinaceHomeData data;

	public FinaceHomeData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(FinaceHomeData data) {
		this.data = data;
	}
}