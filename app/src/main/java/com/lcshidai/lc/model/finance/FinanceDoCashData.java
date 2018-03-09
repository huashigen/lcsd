package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FinanceDoCashData {

	private String prj_order_id;
	private String prj_name;
	private String msg;
	
	public String getPrj_order_id() {
		return prj_order_id;
	}
	
	@JsonProperty("prj_order_id")
	public void setPrj_order_id(String prj_order_id) {
		this.prj_order_id = prj_order_id;
	}
	public String getPrj_name() {
		return prj_name;
	}
	
	@JsonProperty("prj_name")
	public void setPrj_name(String prj_name) {
		this.prj_name = prj_name;
	}
	public String getMsg() {
		return msg;
	}
	
	@JsonProperty("msg")
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
