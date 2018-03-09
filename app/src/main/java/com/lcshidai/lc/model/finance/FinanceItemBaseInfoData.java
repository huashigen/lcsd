package com.lcshidai.lc.model.finance;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceItemBaseInfoData extends BaseData {
	public List<FinanceItemBaseInfoExtensionVInItem> cailiaoImgList = new ArrayList<FinanceItemBaseInfoExtensionVInItem>();
	public List<FinanceItemBaseInfoMaterialItem> cailiaoList;
	public List<FinanceItemBaseInfoExtensionItem> extensionList;
	public List<FinanceItemBaseInfoExtensionVInItem> baseInfoList;

	public List<FinanceItemBaseInfoBorrowerVInItem> borrowerInfoList;
	public List<FinanceItemBaseInfoFundInfoVInItem> fundInfoList;
	//
	private String baoli_tips;// 保理或者非保理的提示 非保理的时候为空

	private String prj_attribute_tips;
	
	private FinanceItemBaseInfoBaseDescribeItem base_dscr ; // 项目简介
	

	public FinanceItemBaseInfoBaseDescribeItem getBase_dscr() {
		return base_dscr;
	}
	
	@JsonProperty("base_dscr")
	public void setBase_dscr(FinanceItemBaseInfoBaseDescribeItem base_dscr) {
		this.base_dscr = base_dscr;
	}

	public FinanceItemBaseInfoData() {

	}

	public FinanceItemBaseInfoData(String me) {

	}

	public List<FinanceItemBaseInfoFundInfoVInItem> getFundInfoList() {
		return fundInfoList;
	}

	@JsonProperty("fund_info")
	public void setFundInfoList(List<FinanceItemBaseInfoFundInfoVInItem> fundInfoList) {
		this.fundInfoList = fundInfoList;
	}

	public List<FinanceItemBaseInfoExtensionVInItem> getCailiaoImgList() {
		return cailiaoImgList;
	}

	public void setCailiaoImgList(List<FinanceItemBaseInfoExtensionVInItem> cailiaoImgList) {
		this.cailiaoImgList = cailiaoImgList;
	}

	public List<FinanceItemBaseInfoBorrowerVInItem> getBorrowerInfoList() {
		return borrowerInfoList;
	}

	@JsonProperty("borrower")
	public void setBorrowerInfoList(List<FinanceItemBaseInfoBorrowerVInItem> borrowerInfoList) {
		this.borrowerInfoList = borrowerInfoList;
	}

	public String getPrj_attribute_tips() {
		return prj_attribute_tips;
	}

	@JsonProperty("prj_attribute_tips")
	public void setPrj_attribute_tips(String prj_attribute_tips) {
		this.prj_attribute_tips = prj_attribute_tips;
	}

	public String getBaoli_tips() {
		return baoli_tips;
	}

	@JsonProperty("baoli_tips")
	public void setBaoli_tips(String baoli_tips) {
		this.baoli_tips = baoli_tips;
	}

	public List<FinanceItemBaseInfoMaterialItem> getCailiaoList() {
		return cailiaoList;
	}

	@JsonProperty("cailiao")
	public void setCailiaoList(List<FinanceItemBaseInfoMaterialItem> cailiaoList) {
		this.cailiaoList = cailiaoList;
	}

	public List<FinanceItemBaseInfoExtensionItem> getExtensionList() {
		return extensionList;
	}

	@JsonProperty("extension")
	public void setExtensionList(List<FinanceItemBaseInfoExtensionItem> extensionList) {
		this.extensionList = extensionList;
	}

	public List<FinanceItemBaseInfoExtensionVInItem> getBaseInfoList() {
		return baseInfoList;
	}

	@JsonProperty("base_info")
	public void setBaseInfoList(List<FinanceItemBaseInfoExtensionVInItem> baseInfoList) {
		this.baseInfoList = baseInfoList;
	}

}
