package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinaceInfoMovementData extends BaseData {
	public String prj_id;
	public String prj_no;

	public String getPrj_id() {
		return prj_id;
	}

	@JsonProperty("prj_id")
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}

	public String getPrj_no() {
		return prj_no;
	}

	@JsonProperty("prj_no")
	public void setPrj_no(String prj_no) {
		this.prj_no = prj_no;
	}// 项目编号

}
