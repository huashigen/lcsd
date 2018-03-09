package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalsList extends BaseData{
	private String type;
	private WithdrawalsCard cardObjdata;
	private WithdrawalsBank bankObjdata;

	public WithdrawalsList() {

	}

	public WithdrawalsList(String me) {

	}
	
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	public WithdrawalsBank getBankObjdata() {
		return bankObjdata;
	}

	@JsonProperty("bank")
	public void setBankObjdata(WithdrawalsBank bankObjdata) {
		this.bankObjdata = bankObjdata;
	}

	public WithdrawalsCard getCardObjdata() {
		return cardObjdata;
	}

	@JsonProperty("fund_account")
	public void setCardObjdata(WithdrawalsCard cardObjdata) {
		this.cardObjdata = cardObjdata;
	}

}
