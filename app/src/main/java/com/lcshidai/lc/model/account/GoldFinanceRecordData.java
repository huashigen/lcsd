package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoldFinanceRecordData extends BaseData {
	private String total_page;//
	private String current_page;//
	private List<GoldFinanceRecord> list;

	public String getTotal_page() {
		return total_page;
	}

	@JsonProperty("total_page")
	public void setTotal_page(String total_page) {
		this.total_page = total_page;
	}

	public String getCurrent_page() {
		return current_page;
	}

	@JsonProperty("current_page")
	public void setCurrent_page(String current_page) {
		this.current_page = current_page;
	}

	public List<GoldFinanceRecord> getList() {
		return list;
	}

	@JsonProperty("list")
	public void setList(List<GoldFinanceRecord> list) {
		this.list = list;
	}

}
