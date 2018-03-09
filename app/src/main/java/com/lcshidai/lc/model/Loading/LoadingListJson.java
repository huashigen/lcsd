package com.lcshidai.lc.model.Loading;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadingListJson extends BaseJson {

	private List<LoadingData> list;

	public List<LoadingData> getList() {
		return list;
	}

	@JsonProperty("data")
	public void setList(List<LoadingData> list) {
		this.list = list;
	}

}
