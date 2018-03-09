package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakInfoList extends BaseData {
	private String prj_order_id; //
	private String prj_name; //
	private String prj_id; //
	private String invest_money; //
	private String invest_time; //

	public String getPrj_order_id() {
		return prj_order_id;
	}

	@JsonProperty("prj_order_id")
	public void setPrj_order_id(String prj_order_id) {
		this.prj_order_id = prj_order_id;
	}

	public String getPrj_name() {
		return prj_name;
	}

	@JsonProperty("prj_name")
	public void setPrj_name(String prj_name) {
		this.prj_name = prj_name;
	}

	public String getPrj_id() {
		return prj_id;
	}

	@JsonProperty("prj_id")
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}

	public String getInvest_money() {
		return invest_money;
	}

	@JsonProperty("invest_money")
	public void setInvest_money(String invest_money) {
		this.invest_money = invest_money;
	}

	public String getInvest_time() {
		return invest_time;
	}

	@JsonProperty("invest_time")
	public void setInvest_time(String invest_time) {
		this.invest_time = invest_time;
	}

}
