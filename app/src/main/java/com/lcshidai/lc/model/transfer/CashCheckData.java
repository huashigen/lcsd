package com.lcshidai.lc.model.transfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CashCheckData extends BaseData {
	String max_cash_money;

	public CashCheckData() {

	}

	public CashCheckData(String me) {

	}

	public String getMax_cash_money() {
		return max_cash_money;
	}

	@JsonProperty("max_cash_money")
	public void setMax_cash_money(String max_cash_money) {
		this.max_cash_money = max_cash_money;
	}

}
