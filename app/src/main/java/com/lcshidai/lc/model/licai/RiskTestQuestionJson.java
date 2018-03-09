package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskTestQuestionJson extends BaseJson {
	private RiskTestQuestionData data;
	public RiskTestQuestionData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(RiskTestQuestionData data) {
		this.data = data;
	}
}
