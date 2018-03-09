package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalancePaymentsList extends BaseData{
	private int totalPages;
	private List<BalancePaymentsListEntity> data;

	public BalancePaymentsList() {

	}

	public BalancePaymentsList(String me) {

	}
	
	public int getTotalPages() {
		return totalPages;
	}

	@JsonProperty("totalPages")
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<BalancePaymentsListEntity> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<BalancePaymentsListEntity> data) {
		this.data = data;
	}

}
