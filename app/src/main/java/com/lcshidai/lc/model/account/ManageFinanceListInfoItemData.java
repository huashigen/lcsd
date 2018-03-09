package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.PageModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageFinanceListInfoItemData {

	private PageModel page;
	private String current_page;
	private String total;
	private String total_page;
	private List<ManageFinanceListInfoItem> list;
	private ManageFinanceCount count;

	public List<ManageFinanceListInfoItem> getList() {
		return list;
	}

	@JsonProperty("list")
	public void setList(List<ManageFinanceListInfoItem> list) {
		this.list = list;
	}

	public PageModel getPage() {
		return page;
	}

	@JsonProperty("page")
	public void setPage(PageModel page) {
		this.page = page;
	}

	public String getCurrent_page() {
		return current_page;
	}

	@JsonProperty("current_page")
	public void setCurrent_page(String current_page) {
		this.current_page = current_page;
	}

	public String getTotal() {
		return total;
	}

	@JsonProperty("total")
	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotal_page() {
		return total_page;
	}

	@JsonProperty("total_page")
	public void setTotal_page(String total_page) {
		this.total_page = total_page;
	}

	public ManageFinanceCount getCount() {
		return count;
	}
	@JsonProperty("count")
	public void setCount(ManageFinanceCount count) {
		this.count = count;
	}

}
