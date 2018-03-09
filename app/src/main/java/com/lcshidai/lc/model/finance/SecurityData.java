package com.lcshidai.lc.model.finance;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityData extends BaseData {
	private ArrayList<String> guarantee_measure;
	private String prj_manager_no;

	public ArrayList<String> getGuarantee_measure() {
		return guarantee_measure;
	}

	@JsonProperty("guarantee_measure")
	public void setGuarantee_measure(ArrayList<String> guarantee_measure) {
		this.guarantee_measure = guarantee_measure;
	}

	public String getPrj_manager_no() {
		return prj_manager_no;
	}

	@JsonProperty("prj_manager_no")
	public void setPrj_manager_no(String prj_manager_no) {
		this.prj_manager_no = prj_manager_no;
	}

}
