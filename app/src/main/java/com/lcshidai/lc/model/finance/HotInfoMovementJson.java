package com.lcshidai.lc.model.finance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotInfoMovementJson extends BaseJson {

	private List<HotInfoMovementData> data;

	public List<HotInfoMovementData> getData() {
		return data;
	}
	@JsonProperty("data")
	public void setData(List<HotInfoMovementData> data) {
		this.data = data;
	}

	 

	 
}
