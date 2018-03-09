package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceItemBaseInfoJson extends BaseJson {

	private FinanceItemBaseInfoData data;

	public FinanceItemBaseInfoData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(FinanceItemBaseInfoData data) {
		this.data = data;
	}

}
