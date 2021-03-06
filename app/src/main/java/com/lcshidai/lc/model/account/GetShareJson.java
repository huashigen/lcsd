package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetShareJson {
	private GetShareData data;
	private int boolen;

	public GetShareData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(GetShareData data) {
		this.data = data;
	}

	public int getBoolen() {
		return boolen;
	}

	@JsonProperty("boolen")
	public void setBoolen(int boolen) {
		this.boolen = boolen;
	}

}
