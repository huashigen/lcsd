package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepaymentList extends BaseData{
	private String repay_periods; // 回款期数
	private String repay_periods_view;//新回款期数
	private String pri_interest; // 应收本息
	private String principal; // 应收本金
	private String yield; // 应收利息
	private String rest_principal; // 剩余本金
	private String status; // 状态
	private String repay_date; // 回款日期

	public RepaymentList() {

	}

	public RepaymentList(String me) {

	}
	
	public String getRepay_periods() {
		return repay_periods;
	}

	@JsonProperty("repay_periods")
	public void setRepay_periods(String repay_periods) {
		this.repay_periods = repay_periods;
	}

	public String getRepay_periods_view() {
		return repay_periods_view;
	}
	@JsonProperty("repay_periods_view")
	public void setRepay_periods_view(String repay_periods_view) {
		this.repay_periods_view = repay_periods_view;
	}

	public String getPri_interest() {
		return pri_interest;
	}

	@JsonProperty("pri_interest")
	public void setPri_interest(String pri_interest) {
		this.pri_interest = pri_interest;
	}

	public String getPrincipal() {
		return principal;
	}

	@JsonProperty("principal")
	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getYield() {
		return yield;
	}

	@JsonProperty("yield")
	public void setYield(String yield) {
		this.yield = yield;
	}

	public String getRest_principal() {
		return rest_principal;
	}

	@JsonProperty("rest_principal")
	public void setRest_principal(String rest_principal) {
		this.rest_principal = rest_principal;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getRepay_date() {
		return repay_date;
	}

	@JsonProperty("repay_date")
	public void setRepay_date(String repay_date) {
		this.repay_date = repay_date;
	}

}
