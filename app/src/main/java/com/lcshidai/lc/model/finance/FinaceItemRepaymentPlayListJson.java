package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinaceItemRepaymentPlayListJson extends BaseJson {

	private FinaceItemRepaymentPlayData data;

	public FinaceItemRepaymentPlayListJson() {

	}

	public FinaceItemRepaymentPlayListJson(String me) {

	}

	public FinaceItemRepaymentPlayData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(FinaceItemRepaymentPlayData data) {
		this.data = data;
	}

}
