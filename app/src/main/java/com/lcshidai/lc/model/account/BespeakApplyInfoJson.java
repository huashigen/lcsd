package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakApplyInfoJson extends BaseJson{
	private BespeakApplyInfoData data;

	public BespeakApplyInfoData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(BespeakApplyInfoData data) {
		this.data = data;
	}
}
