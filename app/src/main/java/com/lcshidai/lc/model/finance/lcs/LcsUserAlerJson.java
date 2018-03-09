package com.lcshidai.lc.model.finance.lcs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LcsUserAlerJson extends BaseJson {

	private LcsUserAlerData data;

	public LcsUserAlerData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(LcsUserAlerData data) {
		this.data = data;
	}
}