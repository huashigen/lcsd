package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetQuestionData extends BaseData {
	private String code_no;//
	private String code_name;//

	public String getCode_no() {
		return code_no;
	}

	@JsonProperty("code_no")
	public void setCode_no(String code_no) {
		this.code_no = code_no;
	}

	public String getCode_name() {
		return code_name;
	}

	@JsonProperty("code_name")
	public void setCode_name(String code_name) {
		this.code_name = code_name;
	}
}
