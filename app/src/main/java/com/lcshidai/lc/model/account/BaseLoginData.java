package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseLoginData extends BaseData {
	public String is_paypwd_edit;// 1,
	public String uid;// 119,
	public String is_change_uname;// 0,
	public String sex;// 0,
	public String person_id;// 321322198912165665,
	public String is_active;// 1,
	public String uname;// mao,
	public String mi_id;// 1,
	public String is_id_auth;// 1,
	public String safe_level;// 80,
	public String dept_id;// 43,
	public String identity_no;// I101,I102,I103,I104,
	public String is_email_auth;// 0,
	public String vip_group_id;// ,1,,
	public String real_name;// 毛迎兰,
	public String is_mobile_auth;// 1,
	public String before_logintime;// 1392693365,
	public String mobile;// 18994374723
	public String is_set_uname = "";
	public String is_all;// 是否完成新手指引 0 未完成 1 已完成
	public String is_newbie; // 是否是新客0-不是 1-是新客
	public String is_paypwd_mobile_set; // 是否设置手机支付密码
	public String is_recharged; // 是否充值用户
	public boolean user_is_qfx; // 是否yq付用户
	// added by randy at 2016/10/11
	private String uc_id;
	private AccountSettingImg ava;
	private List<BaseLogin> remindList;
//	
	public String is_binding_bank; // 是否充值用户
	public String getUid() {
		return uid;
	}

	@JsonProperty("uid")
	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getIs_paypwd_edit() {
		return is_paypwd_edit;
	}

	@JsonProperty("is_paypwd_edit")
	public void setIs_paypwd_edit(String is_paypwd_edit) {
		this.is_paypwd_edit = is_paypwd_edit;
	}

	public String getIs_change_uname() {
		return is_change_uname;
	}

	@JsonProperty("is_change_uname")
	public void setIs_change_uname(String is_change_uname) {
		this.is_change_uname = is_change_uname;
	}

	public String getIs_binding_bank() {
		return is_binding_bank;
	}
	@JsonProperty("is_binding_bank")
	public void setIs_binding_bank(String is_binding_bank) {
		this.is_binding_bank = is_binding_bank;
	}

	public String getSex() {
		return sex;
	}

	@JsonProperty("sex")
	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPerson_id() {
		return person_id;
	}

	@JsonProperty("person_id")
	public void setPerson_id(String person_id) {
		this.person_id = person_id;
	}

	public String getIs_active() {
		return is_active;
	}

	@JsonProperty("is_active")
	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}

	public String getUname() {
		return uname;
	}

	@JsonProperty("uname")
	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getMi_id() {
		return mi_id;
	}

	@JsonProperty("mi_id")
	public void setMi_id(String mi_id) {
		this.mi_id = mi_id;
	}

	public String getIs_id_auth() {
		return is_id_auth;
	}

	@JsonProperty("is_id_auth")
	public void setIs_id_auth(String is_id_auth) {
		this.is_id_auth = is_id_auth;
	}

	public String getSafe_level() {
		return safe_level;
	}

	@JsonProperty("safe_level")
	public void setSafe_level(String safe_level) {
		this.safe_level = safe_level;
	}

	public String getDept_id() {
		return dept_id;
	}

	@JsonProperty("dept_id")
	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}

	public String getIdentity_no() {
		return identity_no;
	}

	@JsonProperty("identity_no")
	public void setIdentity_no(String identity_no) {
		this.identity_no = identity_no;
	}

	public String getIs_email_auth() {
		return is_email_auth;
	}

	@JsonProperty("is_email_auth")
	public void setIs_email_auth(String is_email_auth) {
		this.is_email_auth = is_email_auth;
	}

	public String getVip_group_id() {
		return vip_group_id;
	}

	@JsonProperty("vip_group_id")
	public void setVip_group_id(String vip_group_id) {
		this.vip_group_id = vip_group_id;
	}

	public String getReal_name() {
		return real_name;
	}

	@JsonProperty("real_name")
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getIs_mobile_auth() {
		return is_mobile_auth;
	}

	@JsonProperty("is_mobile_auth")
	public void setIs_mobile_auth(String is_mobile_auth) {
		this.is_mobile_auth = is_mobile_auth;
	}

	public String getBefore_logintime() {
		return before_logintime;
	}

	@JsonProperty("before_logintime")
	public void setBefore_logintime(String before_logintime) {
		this.before_logintime = before_logintime;
	}

	public String getMobile() {
		return mobile;
	}

	@JsonProperty("mobile")
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIs_set_uname() {
		return is_set_uname;
	}

	@JsonProperty("is_set_uname")
	public void setIs_set_uname(String is_set_uname) {
		this.is_set_uname = is_set_uname;
	}

	public String getIs_all() {
		return is_all;
	}

	@JsonProperty("is_all")
	public void setIs_all(String is_all) {
		this.is_all = is_all;
	}

	public String getIs_newbie() {
		return is_newbie;
	}

	@JsonProperty("is_newbie")
	public void setIs_newbie(String is_newbie) {
		this.is_newbie = is_newbie;
	}

	public String getIs_paypwd_mobile_set() {
		return is_paypwd_mobile_set;
	}

	@JsonProperty("is_paypwd_mobile_set")
	public void setIs_paypwd_mobile_set(String is_paypwd_mobile_set) {
		this.is_paypwd_mobile_set = is_paypwd_mobile_set;
	}

	public String getIs_recharged() {
		return is_recharged;
	}

	@JsonProperty("is_recharged")
	public void setIs_recharged(String is_recharged) {
		this.is_recharged = is_recharged;
	}

	public boolean isUser_is_qfx() {
		return user_is_qfx;
	}

	@JsonProperty("user_is_qfx")
	public void setUser_is_qfx(boolean user_is_qfx) {
		this.user_is_qfx = user_is_qfx;
	}

	public String getUc_id() {
		return uc_id;
	}

	@JsonProperty("uc_id")
	public void setUc_id(String uc_id) {
		this.uc_id = uc_id;
	}

	public AccountSettingImg getAva() {
		return ava;
	}

	@JsonProperty("ava")
	public void setAva(AccountSettingImg ava) {
		this.ava = ava;
	}

	public List<BaseLogin> getRemindList() {
		return remindList;
	}

	@JsonProperty("remindList")
	public void setRemindList(List<BaseLogin> remindList) {
		this.remindList = remindList;
	}

}
