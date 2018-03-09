package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyRewardRecordData {
	private String total_page;
	private String current_page;
	private String total;
	private String sum_yield;
//
	private List<MyRewardUsed> data;






	public List<MyRewardUsed> getData() {
		return data;
	}

	public String getTotal_page() {
		return total_page;
	}
	@JsonProperty("total_page")
	public void setTotal_page(String total_page) {
		this.total_page = total_page;
	}

	public String getSum_yield() {
		return sum_yield;
	}
	@JsonProperty("sum_yield")
	public void setSum_yield(String sum_yield) {
		this.sum_yield = sum_yield;
	}

	@JsonProperty("list")
	public void setData(List<MyRewardUsed> data) {
		this.data = data;
	}
	
	public String getTotal() {
		return total;
	}
	@JsonProperty("total")
	public void setTotal(String total) {
		this.total = total;
	}

	public String getCurrent_page() {
		return current_page;
	}
	@JsonProperty("current_page")
	public void setCurrent_page(String current_page) {
		this.current_page = current_page;
	}



}
