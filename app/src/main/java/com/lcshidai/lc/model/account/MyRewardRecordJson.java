package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyRewardRecordJson extends BaseJson {
	private MyRewardRecordData data;

	public MyRewardRecordData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(MyRewardRecordData data) {
		this.data = data;
	}

}
