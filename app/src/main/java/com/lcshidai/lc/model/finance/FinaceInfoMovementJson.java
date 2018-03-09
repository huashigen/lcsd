package com.lcshidai.lc.model.finance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinaceInfoMovementJson extends BaseJson {

	private List<FinaceInfoMovementData> data;

	public List<FinaceInfoMovementData> getData() {
		return data;
	}
	@JsonProperty("data")
	public void setData(List<FinaceInfoMovementData> data) {
		this.data = data;
	}

	 

	 
}
