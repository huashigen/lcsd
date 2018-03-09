package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageFinanceListInfoItemExta {

	private String name;
	private String icon;
	
	
	public String getName() {
		return name;
	}
	
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	
	@JsonProperty("big_icon")
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
}
