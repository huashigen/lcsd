package com.lcshidai.lc.model.finance.lcs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LcsShowIndexJson extends BaseJson {

	private LcsShowIndexData data;

	public LcsShowIndexData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(LcsShowIndexData data) {
		this.data = data;
	}
}