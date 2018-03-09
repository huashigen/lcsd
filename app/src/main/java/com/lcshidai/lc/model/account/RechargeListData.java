package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RechargeListData extends BaseData {
	private int totalPages; //
	private String data;
	private List<RechargeList> list;

	public int getTotalPages() {
		return totalPages;
	}

	@JsonProperty("totalPages")
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public String getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(String data) {
		this.data = data;
	}

	public List<RechargeList> getList() {
		return list;
	}

	@JsonProperty("data")
	public void setList(List<RechargeList> list) {
		this.list = list;
	}

}
