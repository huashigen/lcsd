package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScoreRecordJson extends BaseJson {
	private ScoreRecordData data;

	public ScoreRecordData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(ScoreRecordData data) {
		this.data = data;
	}

}
