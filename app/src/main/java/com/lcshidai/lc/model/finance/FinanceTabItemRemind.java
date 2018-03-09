package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FinanceTabItemRemind {
	
	private String remind_id;
	private String is_available;
	
	public String getRemind_id() {
		return remind_id;
	}
	@JsonProperty("remind_id")
	public void setRemind_id(String remind_id) {
		this.remind_id = remind_id;
	}
	public String getIs_available() {
		return is_available;
	}
	
	@JsonProperty("is_available")
	public void setIs_available(String is_available) {
		this.is_available = is_available;
	}
	
}
