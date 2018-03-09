package com.lcshidai.lc.model.transfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CashCheckAddJson extends BaseJson {

	private CashCheckAddData data;

	public CashCheckAddData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(CashCheckAddData data) {
		this.data = data;
	}

}
