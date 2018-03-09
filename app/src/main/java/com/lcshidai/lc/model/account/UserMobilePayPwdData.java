package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMobilePayPwdData extends BaseData {
	private String is_paypwd_mobile_set;//
	private String is_id_auth;//
	private String is_binding_bank;//
	private String is_set_sqa;//

	public String getIs_paypwd_mobile_set() {
		return is_paypwd_mobile_set;
	}

	@JsonProperty("is_paypwd_mobile_set")
	public void setIs_paypwd_mobile_set(String is_paypwd_mobile_set) {
		this.is_paypwd_mobile_set = is_paypwd_mobile_set;
	}

	public String getIs_id_auth() {
		return is_id_auth;
	}

	@JsonProperty("is_id_auth")
	public void setIs_id_auth(String is_id_auth) {
		this.is_id_auth = is_id_auth;
	}

	public String getIs_binding_bank() {
		return is_binding_bank;
	}

	@JsonProperty("is_binding_bank")
	public void setIs_binding_bank(String is_binding_bank) {
		this.is_binding_bank = is_binding_bank;
	}

	public String getIs_set_sqa() {
		return is_set_sqa;
	}

	@JsonProperty("is_set_sqa")
	public void setIs_set_sqa(String is_set_sqa) {
		this.is_set_sqa = is_set_sqa;
	}

}
