package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author 001355
 * @Description: 5、6、7
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinaceItemRepaymentPlayInItem {
	public String repay_periods;// : "11",回款期数
	public String pri_interest;// : "1,467.63元",应收本息
	public String principal;// : "1,212.91元",应收本金
	public String yield;// : "254.72元",应收利息
	public String rest_principal;// : "1,334.28元",剩余本金
	public String status;// : "待回款",状态
	public String repay_date;// : "2015-02-09"回款日期
	public String numfo;
	public String numString;
	public FinaceItemRepaymentPlayInItem() {

	}

	public FinaceItemRepaymentPlayInItem(String me) {

	}

	public String getRepay_date() {
		return repay_date;
	}

	@JsonProperty("repay_date")
	public void setRepay_date(String repay_date) {
		this.repay_date = repay_date;
	}

	public String getRest_principal() {
		return rest_principal;
	}

	@JsonProperty("rest_principal")
	public void setRest_principal(String rest_principal) {
		this.rest_principal = rest_principal;
	}

	public String getRepay_periods() {
		return repay_periods;
	}

	@JsonProperty("repay_periods")
	public void setRepay_periods(String repay_periods) {
		this.repay_periods = repay_periods;
	}

	public String getPrincipal() {
		return principal;
	}

	@JsonProperty("principal")
	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getPri_interest() {
		return pri_interest;
	}

	@JsonProperty("pri_interest")
	public void setPri_interest(String pri_interest) {
		this.pri_interest = pri_interest;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getYield() {
		return yield;
	}

	@JsonProperty("yield")
	public void setYield(String yield) {
		this.yield = yield;
	}

	public String getNumfo() {
		return numfo;
	}
	@JsonProperty("numfo")
	public void setNumfo(String numfo) {
		this.numfo = numfo;
	}

	public String getNumString() {
		return numString;
	}
 
	public void setNumString(String numString) {
		this.numString = numString;
	}

}
