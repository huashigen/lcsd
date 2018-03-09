package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalancePaymentsData {
	private BalancePaymentsList listdata;

	public BalancePaymentsList getListdata() {
		return listdata;
	}

	@JsonProperty("list")
	public void setListdata(BalancePaymentsList listdata) {
		this.listdata = listdata;
	}

}
