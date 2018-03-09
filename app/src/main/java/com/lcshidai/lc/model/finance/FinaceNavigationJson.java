package com.lcshidai.lc.model.finance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinaceNavigationJson extends BaseJson {

	private List<FinaceNavigationData> data;

	public List<FinaceNavigationData> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<FinaceNavigationData> data) {
		this.data = data;
	}
	
}