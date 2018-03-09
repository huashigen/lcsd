package com.lcshidai.lc.model.transfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CashCheckJson extends BaseJson {

	private CashCheckData data;

	public CashCheckData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(CashCheckData data) {
		this.data = data;
	}

}
