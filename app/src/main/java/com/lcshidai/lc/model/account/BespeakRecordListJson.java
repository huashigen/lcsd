package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakRecordListJson extends BaseJson {
	private BespeakRecordListData data;

	public BespeakRecordListJson() {

	}

	public BespeakRecordListJson(String me) {

	}
	
	public BespeakRecordListData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(BespeakRecordListData data) {
		this.data = data;
	}

}
