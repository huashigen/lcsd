package com.lcshidai.lc.model.oneKeyInvest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IsOpenAutoInvestJson extends BaseJson{
    private IsOpenAutoInvestData data;

	public IsOpenAutoInvestData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(IsOpenAutoInvestData data) {
		this.data = data;
	}

}
