package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityInfoData extends BaseJson {
	private SecurityData data;

	public SecurityData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(SecurityData data) {
		this.data = data;
	}

}
