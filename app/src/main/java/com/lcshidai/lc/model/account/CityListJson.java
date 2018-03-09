package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityListJson extends BaseJson {
	private List<CityListData> data;

	public List<CityListData> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<CityListData> data) {
		this.data = data;
	}

}
