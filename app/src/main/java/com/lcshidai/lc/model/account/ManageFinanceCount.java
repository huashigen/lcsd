package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageFinanceCount {
	private String type_1;//
	private String type_2;//
	public String getType_1() {
		return type_1;
	}

	@JsonProperty("type_1")
	public void setType_1(String type_1) {
		this.type_1 = type_1;
	}
	public String getType_2() {
		return type_2;
	}
	@JsonProperty("type_2")
	public void setType_2(String type_2) {
		this.type_2 = type_2;
	}

	
}
