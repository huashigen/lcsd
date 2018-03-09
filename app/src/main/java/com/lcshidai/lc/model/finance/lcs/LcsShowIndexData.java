package com.lcshidai.lc.model.finance.lcs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LcsShowIndexData extends BaseData {
	private List<LcsItemData> list;
	private String current_page;
	private String total;
	private int total_page;

	private String page_count;

	public List<LcsItemData> getList() {
		return list;
	}

	@JsonProperty("rows")
	public void setList(List<LcsItemData> list) {
		this.list = list;
	}

	public String getCurrent_page() {
		return current_page;
	}

	public void setCurrent_page(String current_page) {
		this.current_page = current_page;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public int getTotal_page() {
		return total_page;
	}

	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}

	public String getPage_count() {
		return page_count;
	}

	public void setPage_count(String page_count) {
		this.page_count = page_count;
	}
}
