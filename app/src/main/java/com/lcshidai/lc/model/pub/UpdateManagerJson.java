package com.lcshidai.lc.model.pub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateManagerJson {
	private int boolen;
	private UpdateManagerData data;

	public UpdateManagerData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(UpdateManagerData data) {
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
