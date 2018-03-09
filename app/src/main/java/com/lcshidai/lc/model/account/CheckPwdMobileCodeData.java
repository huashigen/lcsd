package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckPwdMobileCodeData extends BaseData {
	private String is_id_auth;//
	private String is_sqa;//
	private String is_binding_bank;//

	public String getIs_id_auth() {
		return is_id_auth;
	}

	@JsonProperty("is_id_auth")
	public void setIs_id_auth(String is_id_auth) {
		this.is_id_auth = is_id_auth;
	}

	public String getIs_sqa() {
		return is_sqa;
	}

	@JsonProperty("is_sqa")
	public void setIs_sqa(String is_sqa) {
		this.is_sqa = is_sqa;
	}

	public String getIs_binding_bank() {
		return is_binding_bank;
	}

	@JsonProperty("is_binding_bank")
	public void setIs_binding_bank(String is_binding_bank) {
		this.is_binding_bank = is_binding_bank;
	}

}
