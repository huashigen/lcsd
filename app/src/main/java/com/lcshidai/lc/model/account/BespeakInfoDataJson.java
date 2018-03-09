package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakInfoDataJson extends BaseJson {
	private BespeakInfoData data;

	public BespeakInfoData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(BespeakInfoData data) {
		this.data = data;
	}

}
