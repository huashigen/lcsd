package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountSavepathJson {
	private int boolen;
	private String message;

	public int getBoolen() {
		return boolen;
	}

	@JsonProperty("boolen")
	public void setBoolen(int boolen) {
		this.boolen = boolen;
	}

	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

}
