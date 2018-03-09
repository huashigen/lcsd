package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalsListData extends BaseData{
	private int totalPages; //
	private List<WithdrawalsRecord> list;

	public int getTotalPages() {
		return totalPages;
	}

	@JsonProperty("totalPages")
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<WithdrawalsRecord> getList() {
		return list;
	}

	@JsonProperty("data")
	public void setList(List<WithdrawalsRecord> list) {
		this.list = list;
	}

}
