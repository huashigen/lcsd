package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakIndexAppointRecord extends BaseData {
	private String prj_type_show; //
	private String is_all_money; //
	private String id; //
	private String appoint_money; //
	private String appoint_rate; //
	private String appoint_day; //

	public BespeakIndexAppointRecord() {

	}

	public BespeakIndexAppointRecord(String me) {

	}
	
	public String getPrj_type_show() {
		return prj_type_show;
	}

	@JsonProperty("prj_type_show")
	public void setPrj_type_show(String prj_type_show) {
		this.prj_type_show = prj_type_show;
	}

	public String getIs_all_money() {
		return is_all_money;
	}

	@JsonProperty("is_all_money")
	public void setIs_all_money(String is_all_money) {
		this.is_all_money = is_all_money;
	}

	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getAppoint_money() {
		return appoint_money;
	}

	@JsonProperty("appoint_money")
	public void setAppoint_money(String appoint_money) {
		this.appoint_money = appoint_money;
	}

	public String getAppoint_rate() {
		return appoint_rate;
	}

	@JsonProperty("appoint_rate")
	public void setAppoint_rate(String appoint_rate) {
		this.appoint_rate = appoint_rate;
	}

	public String getAppoint_day() {
		return appoint_day;
	}

	@JsonProperty("appoint_day")
	public void setAppoint_day(String appoint_day) {
		this.appoint_day = appoint_day;
	}

}
