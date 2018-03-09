package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepaymentListData {
	private List<RepaymentList> data;
	private String money ;
	private String time_limit ;
	private String sum_pri_interest ;
	private String sum_principal ;
	private String sum_yield ;
	

	public String getMoney() {
		return money;
	}
	@JsonProperty("money")
	public void setMoney(String money) {
		this.money = money;
	}

	public String getTime_limit() {
		return time_limit;
	}
	
	@JsonProperty("time_limit")
	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	public String getSum_pri_interest() {
		return sum_pri_interest;
	}
	
	@JsonProperty("sum_pri_interest")
	public void setSum_pri_interest(String sum_pri_interest) {
		this.sum_pri_interest = sum_pri_interest;
	}

	public String getSum_principal() {
		return sum_principal;
	}
	
	@JsonProperty("sum_principal")
	public void setSum_principal(String sum_principal) {
		this.sum_principal = sum_principal;
	}

	public String getSum_yield() {
		return sum_yield;
	}
	
	@JsonProperty("sum_yield")
	public void setSum_yield(String sum_yield) {
		this.sum_yield = sum_yield;
	}

	public List<RepaymentList> getData() {
		return data;
	}

	@JsonProperty("list")
	public void setData(List<RepaymentList> data) {
		this.data = data;
	}

}
