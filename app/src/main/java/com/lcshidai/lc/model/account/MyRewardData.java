package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyRewardData {
	private int total_page;
	private String hongbao_sum;
	private String current_page;
	private String total;
	public String getHongbao_sum() {
		return hongbao_sum;
	}

	@JsonProperty("hongbao_sum")
	public void setHongbao_sum(String hongbao_sum) {
		this.hongbao_sum = hongbao_sum;
	}

	private List<MyRewardList> data;

	public int getTotal_page() {
		return total_page;
	}

	@JsonProperty("total_page")
	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}

	public List<MyRewardList> getData() {
		return data;
	}

	@JsonProperty("list")
	public void setData(List<MyRewardList> data) {
		this.data = data;
	}
	@JsonProperty("total")
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	@JsonProperty("current_page")
	public String getCurrent_page() {
		return current_page;
	}

	public void setCurrent_page(String current_page) {
		this.current_page = current_page;
	}



}
