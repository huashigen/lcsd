package com.lcshidai.lc.model.finance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FinanceMJQJson extends BaseJson{

	private List<FinanceMJQItemData> data;

	public List<FinanceMJQItemData> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<FinanceMJQItemData> data) {
		this.data = data;
	}
	
}
