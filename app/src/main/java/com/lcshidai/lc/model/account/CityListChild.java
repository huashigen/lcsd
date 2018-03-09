package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityListChild extends BaseData {
	private String id;
	private String code;
	private String name_cn;

	public CityListChild() {

	}

	public CityListChild(String me) {

	}
	
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	@JsonProperty("code")
	public void setCode(String code) {
		this.code = code;
	}

	public String getName_cn() {
		return name_cn;
	}

	@JsonProperty("name_cn")
	public void setName_cn(String name_cn) {
		this.name_cn = name_cn;
	}

}
