package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountSettingData extends BaseData {
	private String uname;//
	private String safe_level_label;//
	private String is_id_auth;//
	private String is_email_auth;//
	private String is_mobile_auth;//
	private String is_paypwd_mobile_set;//
	private String is_binding_bank;//
	private String is_set_sqa;//
	private String mobile;//
	private String real_name;//
	private String email;//
	private AccountSettingImg ava;
	private int safe_level ;
	
	

	public int getSafe_level() {
		return safe_level;
	}

	@JsonProperty("safe_level")
	public void setSafe_level(int safe_level) {
		this.safe_level = safe_level;
	}

	public String getUname() {
		return uname;
	}

	@JsonProperty("uname")
	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getSafe_level_label() {
		return safe_level_label;
	}

	@JsonProperty("safe_level_label")
	public void setSafe_level_label(String safe_level_label) {
		this.safe_level_label = safe_level_label;
	}

	public String getIs_id_auth() {
		return is_id_auth;
	}

	@JsonProperty("is_id_auth")
	public void setIs_id_auth(String is_id_auth) {
		this.is_id_auth = is_id_auth;
	}

	public String getIs_email_auth() {
		return is_email_auth;
	}

	@JsonProperty("is_email_auth")
	public void setIs_email_auth(String is_email_auth) {
		this.is_email_auth = is_email_auth;
	}

	public String getIs_mobile_auth() {
		return is_mobile_auth;
	}

	@JsonProperty("is_mobile_auth")
	public void setIs_mobile_auth(String is_mobile_auth) {
		this.is_mobile_auth = is_mobile_auth;
	}

	public String getIs_paypwd_mobile_set() {
		return is_paypwd_mobile_set;
	}

	@JsonProperty("is_paypwd_mobile_set")
	public void setIs_paypwd_mobile_set(String is_paypwd_mobile_set) {
		this.is_paypwd_mobile_set = is_paypwd_mobile_set;
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

	public String getMobile() {
		return mobile;
	}

	@JsonProperty("mobile")
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getReal_name() {
		return real_name;
	}

	@JsonProperty("real_name")
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	public AccountSettingImg getAva() {
		return ava;
	}

	@JsonProperty("ava")
	public void setAva(AccountSettingImg ava) {
		this.ava = ava;
	}

}
