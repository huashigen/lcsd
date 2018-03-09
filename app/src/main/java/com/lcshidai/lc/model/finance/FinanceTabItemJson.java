package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceTabItemJson extends BaseJson {

	private FinanceTabItemData data;
	
	public FinanceTabItemData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(FinanceTabItemData data) {
		this.data = data;
	}
	
}
