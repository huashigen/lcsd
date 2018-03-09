package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseLogin extends BaseData {
	private String obj_id; //
	private String remind_time; //
	private String prj_type_name; //
	private String start_bid_time; //

	public String getObj_id() {
		return obj_id;
	}

	@JsonProperty("obj_id")
	public void setObj_id(String obj_id) {
		this.obj_id = obj_id;
	}

	public String getRemind_time() {
		return remind_time;
	}

	@JsonProperty("remind_time")
	public void setRemind_time(String remind_time) {
		this.remind_time = remind_time;
	}

	public String getPrj_type_name() {
		return prj_type_name;
	}

	@JsonProperty("prj_type_name")
	public void setPrj_type_name(String prj_type_name) {
		this.prj_type_name = prj_type_name;
	}

	public String getStart_bid_time() {
		return start_bid_time;
	}

	@JsonProperty("start_bid_time")
	public void setStart_bid_time(String start_bid_time) {
		this.start_bid_time = start_bid_time;
	}

}
