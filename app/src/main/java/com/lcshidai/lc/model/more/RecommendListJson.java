package com.lcshidai.lc.model.more;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendListJson extends BaseJson{

	private List<RecommendDataItem> list;

 

	public List<RecommendDataItem> getList() {
		return list;
	}

	@JsonProperty("data")
	public void setList(List<RecommendDataItem> list) {
		this.list = list;
	}

	 
}
