package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SafeQuestionSelectJson extends BaseJson {
	private List<SafeQuestionSelectData> data;

	public List<SafeQuestionSelectData> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<SafeQuestionSelectData> data) {
		this.data = data;
	}

}
