package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageFinanceListInfoItemJson extends BaseJson {
	private ManageFinanceListInfoItemData data;

 

	public ManageFinanceListInfoItemData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(ManageFinanceListInfoItemData data) {
		this.data = data;
	}

	



}
