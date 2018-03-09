package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakApplyInfoData extends BaseData {
	private String appoint_money; //
	private String appoint_rate; //
	private String appoint_day; //

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
