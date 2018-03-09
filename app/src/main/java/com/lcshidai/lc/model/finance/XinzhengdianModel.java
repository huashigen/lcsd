package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XinzhengdianModel {
	
	private String start_bid_time_diff;
	private String prj_id;
	
	public String getStart_bid_time_diff() {
		return start_bid_time_diff;
	}
	
	@JsonProperty("start_bid_time_diff")
	public void setStart_bid_time_diff(String start_bid_time_diff) {
		this.start_bid_time_diff = start_bid_time_diff;
	}
	
	public String getPrj_id() {
		return prj_id;
	}
	
	@JsonProperty("prj_id")
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	
	
}
