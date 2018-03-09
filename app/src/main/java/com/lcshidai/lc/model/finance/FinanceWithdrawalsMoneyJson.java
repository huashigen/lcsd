package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FinanceWithdrawalsMoneyJson extends BaseJson{
	
	private String money;
	private String cash_fee;
	private String fee;
	public String getMoney() {
		return money;
	}
	@JsonProperty("money")
	public void setMoney(String money) {
		this.money = money;
	}
	public String getCash_fee() {
		return cash_fee;
	}
	@JsonProperty("cash_fee")
	public void setCash_fee(String cash_fee) {
		this.cash_fee = cash_fee;
	}
	public String getFee() {
		return fee;
	}
	@JsonProperty("fee")
	public void setFee(String fee) {
		this.fee = fee;
	}
	
	
}
