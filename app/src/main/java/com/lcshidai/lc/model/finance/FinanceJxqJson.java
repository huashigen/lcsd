package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FinanceJxqJson extends BaseJson{

	private List<FinanceJxqItemData> data;

	public List<FinanceJxqItemData> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<FinanceJxqItemData> data) {
		this.data = data;
	}
	
}
