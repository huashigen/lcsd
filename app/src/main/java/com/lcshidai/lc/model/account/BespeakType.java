package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakType extends BaseData {
	private String post_key; //
	private String prj_type_name; //

	public String getPost_key() {
		return post_key;
	}

	@JsonProperty("post_key")
	public void setPost_key(String post_key) {
		this.post_key = post_key;
	}

	public String getPrj_type_name() {
		return prj_type_name;
	}

	@JsonProperty("prj_type_name")
	public void setPrj_type_name(String prj_type_name) {
		this.prj_type_name = prj_type_name;
	}

}
