package com.lcshidai.lc.model.finance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FinanceHBJson extends BaseJson{

	private List<FinanceHBItemData> data;

	public List<FinanceHBItemData> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<FinanceHBItemData> data) {
		this.data = data;
	}
	
}
