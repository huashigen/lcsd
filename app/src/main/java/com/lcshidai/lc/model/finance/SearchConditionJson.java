package com.lcshidai.lc.model.finance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchConditionJson extends BaseJson {

	private List<SearchConditionData> data;

	public List<SearchConditionData> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<SearchConditionData> data) {
		this.data = data;
	}
	
}