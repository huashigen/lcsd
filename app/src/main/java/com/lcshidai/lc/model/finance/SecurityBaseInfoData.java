package com.lcshidai.lc.model.finance;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityBaseInfoData extends BaseData {
	private String org_type;
	private String title;
	private ArrayList<String> info;

	public SecurityBaseInfoData() {

	}

	public SecurityBaseInfoData(String me) {

	}

	public String getOrg_type() {
		return org_type;
	}

	@JsonProperty("org_type")
	public void setOrg_type(String org_type) {
		this.org_type = org_type;
	}

	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<String> getInfo() {
		return info;
	}

	@JsonProperty("info")
	public void setInfo(ArrayList<String> info) {
		this.info = info;
	}

}
