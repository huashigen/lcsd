package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakRecordListItem extends BaseData {
	private String id; //
	private String ctime; //
	private String appoint_money; //
	private String min_money; //
	private String max_money; //
	private String status; //
	private String status_show; //

	public BespeakRecordListItem() {

	}

	public BespeakRecordListItem(String me) {

	}
	
	
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getCtime() {
		return ctime;
	}

	@JsonProperty("ctime")
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getAppoint_money() {
		return appoint_money;
	}

	@JsonProperty("appoint_money")
	public void setAppoint_money(String appoint_money) {
		this.appoint_money = appoint_money;
	}

	public String getMin_money() {
		return min_money;
	}

	@JsonProperty("min_money")
	public void setMin_money(String min_money) {
		this.min_money = min_money;
	}

	public String getMax_money() {
		return max_money;
	}

	@JsonProperty("max_money")
	public void setMax_money(String max_money) {
		this.max_money = max_money;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus_show() {
		return status_show;
	}

	@JsonProperty("status_show")
	public void setStatus_show(String status_show) {
		this.status_show = status_show;
	}

}
