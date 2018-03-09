package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetQuestionJson extends BaseJson{
	private GetQuestionData data;

	public GetQuestionData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(GetQuestionData data) {
		this.data = data;
	}
}
