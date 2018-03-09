package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SafeQuestionSelectData extends BaseData {
	private String id; //
	private String code_key; //
	private String code_no; //
	private String code_name; //
	private String lang_type; //
	private String order; //
	private String is_public; //

	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getCode_key() {
		return code_key;
	}

	@JsonProperty("code_key")
	public void setCode_key(String code_key) {
		this.code_key = code_key;
	}

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

	public String getLang_type() {
		return lang_type;
	}

	@JsonProperty("lang_type")
	public void setLang_type(String lang_type) {
		this.lang_type = lang_type;
	}

	public String getOrder() {
		return order;
	}

	@JsonProperty("order")
	public void setOrder(String order) {
		this.order = order;
	}

	public String getIs_public() {
		return is_public;
	}

	@JsonProperty("is_public")
	public void setIs_public(String is_public) {
		this.is_public = is_public;
	}

}
