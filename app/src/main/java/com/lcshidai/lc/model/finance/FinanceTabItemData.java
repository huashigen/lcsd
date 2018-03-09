package com.lcshidai.lc.model.finance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.Page;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceTabItemData extends Page {

	private List<FinanceTabItemProductItem> list;
	private List<FinanceTabItemProductItem> prjList;// homelist
	private XinzhengdianModel xinzhengdian;
	private String switch_wap;

	
	public String getSwitch_wap() {
		return switch_wap;
	}

	@JsonProperty("switch_wap")
	public void setSwitch_wap(String switch_wap) {
		this.switch_wap = switch_wap;
	}
	
	public List<FinanceTabItemProductItem> getList() {
		return list;
	}

	@JsonProperty("list")
	public void setList(List<FinanceTabItemProductItem> list) {
		this.list = list;
	}

	public List<FinanceTabItemProductItem> getPrjList() {
		return prjList;
	}

	@JsonProperty("prjList")
	public void setPrjList(List<FinanceTabItemProductItem> prjList) {
		this.prjList = prjList;
	}
	public XinzhengdianModel getXinzhengdian() {
		return xinzhengdian;
	}

	@JsonProperty("xinzhengdian")
	public void setXinzhengdian(XinzhengdianModel xinzhengdian) {
		this.xinzhengdian = xinzhengdian;
	}

}
