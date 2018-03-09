package com.lcshidai.lc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown=true)
public class PageModel extends BaseData{
	
	private  String current_page;
	private  String total;
	private  String total_page;
	
	
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
	
	
}
