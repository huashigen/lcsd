package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepaymentListJson extends BaseJson {
	private RepaymentListData data;

	public RepaymentListData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(RepaymentListData data) {
		this.data = data;
	}

}
