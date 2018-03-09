package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyRewardJson extends BaseJson {
	private MyRewardData data;

	public MyRewardData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(MyRewardData data) {
		this.data = data;
	}

}
