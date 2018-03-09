package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinaceHomeData extends BaseData {
	public XinzhengdianModel xinzhengdian;
	public FinaceHomePrjListData prjList;
	public String switch_wap;

	public FinaceHomeData() {

	}

	public FinaceHomeData(String me) {

	}

	public XinzhengdianModel getXinzhengdian() {
		return xinzhengdian;
	}

	@JsonProperty("xinzhengdian")
	public void setXinzhengdian(XinzhengdianModel xinzhengdian) {
		this.xinzhengdian = xinzhengdian;
	}

	public FinaceHomePrjListData getPrjList() {
		return prjList;
	}

	@JsonProperty("prjList")
	public void setPrjList(FinaceHomePrjListData prjList) {
		this.prjList = prjList;
	}

	public String getSwitch_wap() {
		return switch_wap;
	}

	@JsonProperty("switch_wap")
	public void setSwitch_wap(String switch_wap) {
		this.switch_wap = switch_wap;
	}

}
