package com.lcshidai.lc.model.pub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgreementJson extends BaseJson {

	private AgreementData data;

	public AgreementData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(AgreementData data) {
		this.data = data;
	}

}
