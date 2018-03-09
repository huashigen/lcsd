package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakIndexDataJson extends BaseJson {
	private BespeakIndexData data;

	public BespeakIndexData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(BespeakIndexData data) {
		this.data = data;
	}

}
